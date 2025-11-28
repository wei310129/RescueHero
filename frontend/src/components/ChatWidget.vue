<template>
  <div v-if="isVisible">
    <!-- 懸浮聊天按鈕 -->
    <div class="chat-fab" @click="toggleChat">
      <span v-if="!isOpen">💬</span>
      <span v-else>✖</span>
      <div v-if="totalUnread" class="fab-badge">{{ totalUnread }}</div>
    </div>
    <!-- 聊天視窗 -->
    <div :class="['chat-window', !selectedTarget ? 'chat-window--compact' : '']" v-if="isOpen" ref="chatWindowRef">
      <div v-if="!selectedTarget">
        <h3>選擇聊天對象</h3>
        <div v-if="loading">載入中...</div>
        <div v-else>
          <div>
            <strong>團隊</strong>
            <ul>
              <li v-for="team in teams" :key="team.id">
                <button @click="selectTarget(team, 'team')">
                  {{ team.name }}
                  <span v-if="unreadFor('team', team.id)" class="room-badge">{{ unreadFor('team', team.id) }}</span>
                </button>
              </li>
            </ul>
          </div>
          <div>
            <strong>群組</strong>
            <ul>
              <li v-for="group in groups" :key="group.id">
                <button @click="selectTarget(group, 'group')">
                  {{ group.name }}
                  <span v-if="unreadFor('group', group.id)" class="room-badge">{{ unreadFor('group', group.id) }}</span>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div v-else>
        <div class="chat-header">
          <span>與 {{ selectedTarget.name }} 聊天 ({{ selectedType === 'team' ? '團隊' : '群組' }})</span>
          <button @click="resetTarget">切換對象</button>
        </div>
        <div class="chat-messages" ref="messagesContainerRef">
          <div v-for="(msg, idx) in messages" :key="msg.id" :class="msg.fromSelf ? 'self' : 'other'">
            <div v-if="!msg.fromSelf && (!messages[idx-1] || messages[idx-1].senderName !== msg.senderName) && msg.senderName" class="msg-sender">{{ msg.senderName }}</div>
            <div class="msg-content">{{ msg.content }}</div>
            <div class="msg-time">{{ formatTime(msg.createdAt) }}</div>
          </div>
        </div>
        <form @submit.prevent="sendMessage">
          <input v-model="input" placeholder="輸入訊息..." />
          <button type="submit">送出</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, nextTick, onMounted, onUnmounted, ref, watch} from 'vue'
import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import {apiContextPath, stompAppPrefix} from "@/utils/apiFetch";

const isOpen = ref(false)
const isVisible = ref(false) // only show the chat FAB when user is logged in
const selectedTarget = ref(null)
const selectedType = ref('')
const teams = ref([])
const groups = ref([])
const loading = ref(true)
// global view array shown for the currently selected room (bound to UI)
const messages = ref([])
// per-room messages cache: key = `${type}:${id}` -> array of messages
const messagesMap = ref({})
// per-room unread counts
const unreadMap = ref({})
const totalUnread = computed(() => {
  try { return Object.values(unreadMap.value).reduce((a,b) => a + (parseInt(b) || 0), 0) } catch (e) { return 0 }
})
const input = ref('')
function unreadFor(type, id) { return unreadMap.value[`${type}:${id}`] || 0 }
let stompClient = null
const subscriptions = {} // map of "type:id" -> stomp subscription; keep subscriptions active when switching
// queue of subscriptions to attempt once websocket connects
let pendingSubscribeQueue = []
let stompConnected = false
const chatWindowRef = ref(null)
const messagesContainerRef = ref(null)
const currentUser = ref('')

function scrollToBottom(smooth = false) {
  try {
    const el = messagesContainerRef.value
    if (!el) return
    if (smooth && 'scrollTo' in el) {
      el.scrollTo({ top: el.scrollHeight, behavior: 'smooth' })
    } else {
      el.scrollTop = el.scrollHeight
    }
  } catch (e) { /* ignore */ }
}

function toggleChat() {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    nextTick(() => {
      document.addEventListener('mousedown', handleClickOutside)
      // ensure scroll at open
      nextTick(() => scrollToBottom())
    })
  } else {
    document.removeEventListener('mousedown', handleClickOutside)
  }
}
function handleClickOutside(e) {
  // 點擊 chat-window 或 chat-fab 不收闔
  const chatWindow = chatWindowRef.value
  const fab = document.querySelector('.chat-fab')
  if (chatWindow && (chatWindow.contains(e.target) || (fab && fab.contains(e.target)))) return
  isOpen.value = false
  document.removeEventListener('mousedown', handleClickOutside)
}
function selectTarget(target, type) {
  selectedTarget.value = target
  selectedType.value = type
  // ensure there's an array for this room
  const key = `${type}:${target.id}`
  if (!messagesMap.value[key]) messagesMap.value[key] = []
  // bind the UI messages to the room's array so it persists across switches
  messages.value = messagesMap.value[key]
  // reset unread for this room since user opened it
  try { unreadMap.value[key] = 0 } catch (e) { /* ignore */ }
  // subscribe to new target without disconnecting the websocket
  subscribeToTarget()
}
function resetTarget() {
  // do not unsubscribe here; keep websocket subscriptions active
  // do not clear per-room messages - keep cached messages for when user returns
  selectedTarget.value = null
  selectedType.value = ''
  // do not modify messages.value so the cache remains intact
}
function formatTime(ts) {
  if (!ts) return ''
  try {
    const d = new Date(ts)
    return d.toLocaleString()
  } catch (e) {
    return ''
  }
}
function sendMessage() {
  if (!input.value.trim()) return
  if (!selectedTarget.value || !selectedType.value) return // require a target
  const content = input.value
  const senderName = currentUser.value || ''
  // generate a short clientId for dedupe matching
  const clientId = Date.now() + '-' + Math.random().toString(36).slice(2,8)
  const payload = {
    from: senderName,
    to: selectedTarget.value ? selectedTarget.value.id : null,
    type: selectedType.value,
    content: content,
    clientId // include clientId so server can echo it back
  }

  // send via STOMP to application destination - adjust destination if backend expects a different path
  try {
    if (stompClient && stompConnected && typeof stompClient.send === 'function') {
      stompClient.send(stompAppPrefix + '/chat.' + selectedType.value, {}, JSON.stringify(payload))
    } else {
      // if not yet connected, optionally queue or warn — here we log and still optimistic-push
      console.warn('STOMP not connected yet, sending will fail until connected')
    }
  } catch (e) {
    console.error('send failed', e)
  }

  // optimistic push with timestamp, senderName and clientId into per-room cache
  const now = new Date().toISOString()
  const roomKey = `${selectedType.value}:${selectedTarget.value.id}`
  if (!messagesMap.value[roomKey]) messagesMap.value[roomKey] = []
  const optimistic = { id: Date.now(), content: content, fromSelf: true, senderName: senderName, createdAt: now, clientId }
  messagesMap.value[roomKey].push(optimistic)
  // if currently viewing this room, ensure UI array points to it (it should already) and scroll
  const isViewing = (selectedType.value && selectedTarget.value && messages.value === messagesMap.value[roomKey])
  if (isViewing) {
    messages.value = messagesMap.value[roomKey]
  } else {
    // not viewing: increment unread count
    try { unreadMap.value[roomKey] = (unreadMap.value[roomKey] || 0) + 1 } catch (e) { /* ignore */ }
  }
  input.value = ''
  nextTick(() => scrollToBottom(true))
}

// subscribe helper: unsubscribes previous subscription and subscribes to the selected target
function subscribeRoom(type, id) {
  if (!type || !id) return
  const key = `${type}:${id}`
  const subDestination = `/room/${type}/${id}`

  const doSubscribe = () => {
    if (!stompClient) return
    // if already subscribed to this room, do nothing
    if (subscriptions[key]) return

    try {
      subscriptions[key] = stompClient.subscribe(subDestination, function(message) {
        let body = message.body
        try { body = JSON.parse(message.body) } catch (e) { /* not json */ }

        const content = (body && body.content) ? body.content : (typeof body === 'string' ? body : JSON.stringify(body))
        const createdAt = (body && (body.createdAt || body.ts || body.time)) ? (body.createdAt || body.ts || body.time) : new Date().toISOString()

        const senderCandidates = [body && body.senderName, body && body.sender, body && body.from, body && body.username, body && body.user, body && body.senderId]
        const senderName = senderCandidates.find(s => typeof s === 'string' && s) || null
        const isFromSelf = (() => {
          if (senderName) {
            try { return currentUser.value && String(senderName).toLowerCase() === String(currentUser.value).toLowerCase() } catch (e) { return false }
          }
          return false
        })()

        const incomingClientId = body && body.clientId ? body.clientId : null

        const roomKey = `${type}:${id}`
        if (!messagesMap.value[roomKey]) messagesMap.value[roomKey] = []

        if (incomingClientId) {
          const match = messagesMap.value[roomKey].find(m => m.clientId === incomingClientId)
          if (match) {
            match.createdAt = createdAt
            if (senderName) match.senderName = senderName
            match.fromSelf = isFromSelf
            if (selectedType.value === type && selectedTarget.value && String(selectedTarget.value.id) === String(id)) nextTick(() => scrollToBottom())
            return
          }
        }

        const similar = messagesMap.value[roomKey].find(m => m.content === content && (senderName ? m.senderName === senderName : true) && m.fromSelf === isFromSelf && m.createdAt && Math.abs(new Date(m.createdAt).getTime() - new Date(createdAt).getTime()) < 5000)
        if (similar) {
          similar.createdAt = createdAt
          if (selectedType.value === type && selectedTarget.value && String(selectedTarget.value.id) === String(id)) nextTick(() => scrollToBottom())
          return
        }

        if (!senderName) {
          const opt = messagesMap.value[roomKey].find(m => m.content === content && m.fromSelf === true && m.createdAt && Math.abs(new Date(m.createdAt).getTime() - new Date(createdAt).getTime()) < 5000)
          if (opt) {
            opt.createdAt = createdAt
            if (selectedType.value === type && selectedTarget.value && String(selectedTarget.value.id) === String(id)) nextTick(() => scrollToBottom())
            return
          }
        }

        // push into per-room cache
        messagesMap.value[roomKey].push({ id: Date.now(), content, fromSelf: isFromSelf, senderName: senderName, createdAt })
        const isViewing = (selectedType.value === type && selectedTarget.value && String(selectedTarget.value.id) === String(id))
        if (isViewing) {
          // viewing room: ensure UI bound array and clear unread for that room
          if (messages.value !== messagesMap.value[roomKey]) messages.value = messagesMap.value[roomKey]
          try { unreadMap.value[roomKey] = 0 } catch (e) { /* ignore */ }
          nextTick(() => scrollToBottom())
        } else {
          // not viewing: increment unread count
          try { unreadMap.value[roomKey] = (unreadMap.value[roomKey] || 0) + 1 } catch (e) { /* ignore */ }
        }
      })
    } catch (e) {
      console.error('subscribe failed', e)
      if (subscriptions[key]) subscriptions[key] = null
    }
  }

  if (stompConnected && stompClient) {
    doSubscribe()
  } else {
    // push to pending queue
    pendingSubscribeQueue.push(() => doSubscribe())
  }
}

function subscribeToTarget() {
  if (!selectedTarget.value || !selectedType.value) return
  subscribeRoom(selectedType.value, selectedTarget.value.id)
}

function subscribeAllRooms() {
  try {
    teams.value.forEach(t => subscribeRoom('team', t.id))
    groups.value.forEach(g => subscribeRoom('group', g.id))
  } catch (e) { /* ignore */ }
}

function connectWebSocket() {
  // if already created and connected or connecting, do nothing
  if (stompClient) return

  // Use SockJS over the same origin; since server has context-path /api, endpoint is /api/ws
  const sockUrl = apiContextPath + '/ws'
  const socket = new SockJS(sockUrl)
  // create a STOMP client over SockJS (compat mode)
  stompClient = Stomp.over(socket)
  // disable verbose debug logs
  stompClient.debug = () => {}

  stompClient.connect({}, function(frame) {
    console.info('STOMP connected', frame)
    stompConnected = true

    // flush pending subscribe queue
    try {
      while (pendingSubscribeQueue.length) {
        const fn = pendingSubscribeQueue.shift()
        try { fn() } catch (e) { console.error('pending subscribe failed', e) }
      }
    } catch (e) { /* ignore */ }

  }, function(err) {
    console.error('STOMP connection error', err)
    stompConnected = false
  })
}

// disconnect helper that safely unsubscribes and tears down the STOMP/SockJS client
function disconnectWebSocket() {
  try {
    // unsubscribe all subscriptions we created
    Object.keys(subscriptions).forEach(k => {
      const sub = subscriptions[k]
      if (sub && typeof sub.unsubscribe === 'function') {
        try { sub.unsubscribe() } catch (e) { /* ignore */ }
      }
      try { delete subscriptions[k] } catch (e) { /* ignore */ }
    })
  } catch (e) { /* ignore */ }

  // clear pending subscribe queue
  try { pendingSubscribeQueue = [] } catch (e) { /* ignore */ }

  // disconnect or deactivate stomp client
  if (stompClient) {
    try {
      if (typeof stompClient.disconnect === 'function') {
        try { stompClient.disconnect(() => { stompClient = null; stompConnected = false }) } catch (e) { stompClient = null; stompConnected = false }
      } else if (typeof stompClient.deactivate === 'function') {
        try { stompClient.deactivate() } catch (e) { /* ignore */ }
        stompClient = null
        stompConnected = false
      } else {
        stompClient = null
        stompConnected = false
      }
    } catch (e) {
      stompClient = null
      stompConnected = false
    }
  }
}

function handleUserLogin() {
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
  // when user logs in, show widget and (re)connect websocket
  try { isVisible.value = true } catch (e) { /* ignore */ }
  try { connectWebSocket() } catch (e) { /* ignore */ }
  // initialize rooms/subscriptions after login
  try { initRoomsAndSubscriptions() } catch (e) { /* ignore */ }
}
function handleUserLogout() {
  currentUser.value = ''
  // close websocket, hide chat icon and close window immediately on logout
  try { disconnectWebSocket() } catch (e) { /* ignore */ }
  try { isOpen.value = false } catch (e) { /* ignore */ }
  try { isVisible.value = false } catch (e) { /* ignore */ }
  // clear in-memory caches to avoid leaking data between users
  try { messagesMap.value = {}; messages.value = []; unreadMap.value = {} } catch (e) { /* ignore */ }
}

// initialize teams/groups and subscribe to rooms; usable on mount (if user logged in) or on login
function initRoomsAndSubscriptions() {
  try {
    // TODO: replace with real API fetch
    teams.value = [ { id: 't1', name: '團隊A' }, { id: 't2', name: '團隊B' } ]
    groups.value = [ { id: 'g1', name: '群組X' }, { id: 'g2', name: '群組Y' } ]
  } catch (e) {
    teams.value = []
    groups.value = []
  } finally {
    loading.value = false
  }

  try { syncMessageMapWithRooms() } catch (e) { /* ignore */ }
  try { subscribeAllRooms() } catch (e) { /* ignore */ }
  // ensure watchers are set to keep messageMap in sync
  try { watch([teams, groups], () => syncMessageMapWithRooms(), { deep: true }) } catch (e) { /* ignore */ }
}

onMounted(async () => {
  // read current user from localStorage so we can mark own messages
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
  // only show widget if a user is logged in
  try { isVisible.value = !!currentUser.value } catch (e) { /* ignore */ }
  // listen for global login/logout events so we update marker when user changes
  try { window.addEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.addEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }

  // if user already logged in when component mounts, initialize rooms and websocket
  if (isVisible.value) {
    try { connectWebSocket() } catch (e) { console.error('connectWebSocket failed on mount', e) }
    try { initRoomsAndSubscriptions() } catch (e) { /* ignore */ }
  } else {
    // not logged in: leave loading false and wait for user-login
    loading.value = false
  }
})

// Keep messagesMap in sync with current team/group lists: create empty arrays for new rooms and remove caches for deleted rooms
function syncMessageMapWithRooms() {
  try {
    const keys = new Set()
    teams.value.forEach(t => keys.add(`team:${t.id}`))
    groups.value.forEach(g => keys.add(`group:${g.id}`))

    // create arrays for new rooms
    keys.forEach(k => {
      if (!messagesMap.value[k]) {
        messagesMap.value[k] = []

        // auto-subscribe to new room (parse type:id)
        try {
          const parts = k.split(':')
          if (parts.length === 2) {
            const t = parts[0]
            const id = parts[1]
            // subscribeRoom checks for existing subscriptions and queues if needed
            try { subscribeRoom(t, id) } catch (e) { /* ignore */ }
          }
        } catch (e) { /* ignore */ }
        // ensure unread entry exists
        try { if (!unreadMap.value[k]) unreadMap.value[k] = 0 } catch (e) { /* ignore */ }
      }
    })

    // remove caches and subscriptions for rooms that no longer exist
    Object.keys(messagesMap.value).forEach(k => {
      if (!keys.has(k)) {
        try {
          // unsubscribe if we have an active subscription for that room
          if (subscriptions[k] && typeof subscriptions[k].unsubscribe === 'function') {
            try { subscriptions[k].unsubscribe() } catch (e) { /* ignore */ }
          }
          // remove subscription entry
          try { delete subscriptions[k] } catch (e) { /* ignore */ }
          // remove cached messages and unread entry
          try { delete messagesMap.value[k] } catch (e) { /* ignore */ }
          try { delete unreadMap.value[k] } catch (e) { /* ignore */ }
        } catch (e) { /* ignore */ }
      }
    })
  } catch (e) { /* ignore */ }
}

onUnmounted(() => {
  // disconnect websocket only when component unmounts
  document.removeEventListener('mousedown', handleClickOutside)
  try { window.removeEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.removeEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }

  // reuse disconnect helper so logout and unmount behave the same
  try { disconnectWebSocket() } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.chat-fab {
  position: fixed;
  right: 32px;
  bottom: 32px;
  width: 56px;
  height: 56px;
  background: #409eff;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
  cursor: pointer;
  z-index: 9999;
}
.chat-window {
  position: fixed;
  right: 32px;
  bottom: 96px;
  width: 320px;
  max-height: 480px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.2);
  z-index: 9999;
  display: flex;
  flex-direction: column;
  padding: 12px 16px 12px 16px;
  transition: width 0.2s, padding 0.2s;
  align-items: stretch;
  text-align: left;
}
.chat-window.chat-window--compact {
  width: 170px !important;
  min-width: unset !important;
  max-width: 90vw !important;
  padding: 8px 10px 8px 10px !important;
  align-items: stretch;
  text-align: left;
}
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}
.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 8px;
  background: #f5f7fa;
  border-radius: 4px;
  padding: 8px;
  min-height: 120px;
  max-height: 320px; /* limit height and enable scrollbar when content exceeds */
}
.chat-messages .self {
  text-align: right;
  color: #409eff;
}
.chat-messages .other {
  text-align: left;
  color: #333;
}
.chat-messages .msg-sender {
  font-weight: 600;
  margin-bottom: 4px;
  color: #222;
}
.chat-messages .msg-content { white-space: pre-wrap; }
.chat-messages .msg-time {
  font-size: 0.75rem;
  color: #888;
  margin-top: 2px;
}
form {
  display: flex;
  gap: 8px;
}
input {
  flex: 1;
  padding: 4px 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
button {
  padding: 2px 16px;
  border-radius: 4px;
  border: none;
  background: #409eff;
  color: #fff;
  cursor: pointer;
  margin-bottom: 4px;
  font-size: 15px;
  min-width: 0;
  display: block;
  width: 100%;
  max-width: none;
  box-sizing: border-box;
}
ul, li, button, h3, strong {
  width: 100%;
  box-sizing: border-box;
}
ul {
  list-style: none;
  padding: 0;
  margin: 4px 0 8px 0;
  text-align: left;
}
li {
  margin-bottom: 2px;
  text-align: left;
}
strong {
  display: block;
  margin-bottom: 2px;
  font-size: 17px;
  text-align: left;
}
h3 {
  margin-bottom: 10px;
  font-size: 20px;
  text-align: left;
}
/* badge styles */
.chat-fab .fab-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: #ff4d4f;
  color: white;
  border-radius: 12px;
  padding: 2px 6px;
  font-size: 12px;
  font-weight: 600;
  min-width: 20px;
  text-align: center;
}
.room-badge {
  display: inline-block;
  margin-left: 8px;
  background: #ff4d4f;
  color: #fff;
  border-radius: 10px;
  padding: 0 6px;
  font-size: 12px;
  min-width: 20px;
  text-align: center;
}
</style>
