<template>
  <div>
    <!-- 懸浮聊天按鈕 -->
    <div class="chat-fab" @click="toggleChat">
      <span v-if="!isOpen">💬</span>
      <span v-else>✖</span>
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
                <button @click="selectTarget(team, 'team')">{{ team.name }}</button>
              </li>
            </ul>
          </div>
          <div>
            <strong>群組</strong>
            <ul>
              <li v-for="group in groups" :key="group.id">
                <button @click="selectTarget(group, 'group')">{{ group.name }}</button>
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
import {nextTick, onMounted, onUnmounted, ref} from 'vue'
import SockJS from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import {apiContextPath, stompAppPrefix} from "@/utils/apiFetch";

const isOpen = ref(false)
const selectedTarget = ref(null)
const selectedType = ref('')
const teams = ref([])
const groups = ref([])
const loading = ref(true)
const messages = ref([])
const input = ref('')
let stompClient = null
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
  connectWebSocket()
}
function resetTarget() {
  selectedTarget.value = null
  selectedType.value = ''
  messages.value = []
  if (stompClient) {
    try {
      // disconnect compat client
      if (typeof stompClient.disconnect === 'function') {
        stompClient.disconnect(() => {
          stompClient = null
        })
      } else if (typeof stompClient.deactivate === 'function') {
        stompClient.deactivate()
        stompClient = null
      } else {
        stompClient = null
      }
    } catch (e) {
      stompClient = null
    }
  }
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
  if (!input.value.trim() || !stompClient) return
  const content = input.value
  const senderName = currentUser.value || ''
  // generate a short clientId for dedupe matching
  const clientId = Date.now() + '-' + Math.random().toString(36).slice(2,8)
  const payload = {
    from: senderName,
    to: selectedTarget.value.id,
    type: selectedType.value,
    content: content,
    clientId // include clientId so server can echo it back
  }

  // send via STOMP to application destination - adjust destination if backend expects a different path
  try {
    stompClient.send(stompAppPrefix + '/chat.' + selectedType.value, {}, JSON.stringify(payload))
  } catch (e) {
    console.error('send failed', e)
  }

  // optimistic push with timestamp, senderName and clientId
  const now = new Date().toISOString()
  messages.value.push({
    id: Date.now(),
    content: content,
    fromSelf: true,
    senderName: senderName,
    createdAt: now,
    clientId
  })
  input.value = ''
  nextTick(() => scrollToBottom(true))
}
function connectWebSocket() {
  // ensure previous client is closed
  if (stompClient) {
    try { stompClient.disconnect(() => {}) } catch (e) { /* ignore */ }
    stompClient = null
  }

  // Use SockJS over the same origin; since server has context-path /api, endpoint is /api/ws
  const sockUrl = apiContextPath + '/ws'
  const socket = new SockJS(sockUrl)
  // create a STOMP client over SockJS (compat mode)
  stompClient = Stomp.over(socket)
  // disable verbose debug logs
  stompClient.debug = () => {}

  stompClient.connect({}, function(frame) {
    console.info('STOMP connected', frame)

    // subscribe to a room for this target; adjust destination to match backend conventions
    const subDestination = `/room/${selectedType.value}/${selectedTarget.value.id}`
    try {
      stompClient.subscribe(subDestination, function(message) {
        let body = message.body
        try { body = JSON.parse(message.body) } catch (e) { /* not json */ }

        // normalize content and createdAt
        const content = (body && body.content) ? body.content : (typeof body === 'string' ? body : JSON.stringify(body))
        const createdAt = (body && (body.createdAt || body.ts || body.time)) ? (body.createdAt || body.ts || body.time) : new Date().toISOString()

        // determine sender identity and display name using several common fields
        const senderCandidates = [body && body.senderName, body && body.sender, body && body.from, body && body.username, body && body.user, body && body.senderId]
        const senderName = senderCandidates.find(s => typeof s === 'string' && s) || null
        const senderIdCandidate = (body && (body.senderId || body.fromId || body.userId)) || null
        const isFromSelf = (() => {
          if (senderName) {
            try { return currentUser.value && String(senderName).toLowerCase() === String(currentUser.value).toLowerCase() } catch (e) { return false }
          }
          // fallback: if server sends some id and we had no name, we don't know
          return false
        })()

        // after parsing body, try to obtain clientId
        const incomingClientId = body && body.clientId ? body.clientId : null

        // if server echoes clientId we can match optimistic message reliably
        if (incomingClientId) {
          const match = messages.value.find(m => m.clientId === incomingClientId)
          if (match) {
            // update match with authoritative fields from server
            match.createdAt = createdAt
            if (senderName) match.senderName = senderName
            match.fromSelf = isFromSelf
            nextTick(() => scrollToBottom())
            return
          }
        }

        // dedupe heuristic: match content + senderName (if available) and timestamp proximity
        const similar = messages.value.find(m => m.content === content && (senderName ? m.senderName === senderName : true) && m.fromSelf === isFromSelf && m.createdAt && Math.abs(new Date(m.createdAt).getTime() - new Date(createdAt).getTime()) < 5000)
        if (similar) {
          similar.createdAt = createdAt
          nextTick(() => scrollToBottom())
          return
        }

        // fallback: if no senderName provided, try to match optimistic self messages by content and time
        if (!senderName) {
          const opt = messages.value.find(m => m.content === content && m.fromSelf === true && m.createdAt && Math.abs(new Date(m.createdAt).getTime() - new Date(createdAt).getTime()) < 5000)
          if (opt) {
            opt.createdAt = createdAt
            nextTick(() => scrollToBottom())
            return
          }
        }

        messages.value.push({ id: Date.now(), content, fromSelf: isFromSelf, senderName: senderName, createdAt })
        nextTick(() => scrollToBottom())
      })
    } catch (e) {
      console.error('subscribe failed', e)
    }

    // // optionally notify server that we joined (backend may or may not expect this)
    // try {
    //   stompClient.send(stompAppPrefix + '/chat.join', {}, JSON.stringify({ targetId: selectedTarget.value.id, targetType: selectedType.value }))
    // } catch (e) { /* ignore if backend doesn't handle it */ }

  }, function(err) {
    console.error('STOMP connection error', err)
  })
}

function handleUserLogin() {
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
}
function handleUserLogout() {
  currentUser.value = ''
}

onMounted(async () => {
  // read current user from localStorage so we can mark own messages
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
  // listen for global login/logout events so we update marker when user changes
  try { window.addEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.addEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }

  // TODO: 替換為實際 API 取得團隊/群組
  // 範例：fetch('/api/user/teams-groups')
  // 回傳 { teams: [...], groups: [...] }
  try {

    // 假資料
    teams.value = [ { id: 't1', name: '團隊A' }, { id: 't2', name: '團隊B' } ]
    groups.value = [ { id: 'g1', name: '群組X' }, { id: 'g2', name: '群組Y' } ]
  } finally {
    loading.value = false
  }
})
onUnmounted(() => {
  if (stompClient) {
    try { stompClient.disconnect(() => {}) } catch (e) { /* ignore */ }
    stompClient = null
  }
  document.removeEventListener('mousedown', handleClickOutside)
  try { window.removeEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.removeEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }
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
</style>
