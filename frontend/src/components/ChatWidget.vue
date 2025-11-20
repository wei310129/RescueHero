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
        <div class="chat-messages">
          <div v-for="msg in messages" :key="msg.id" :class="msg.fromSelf ? 'self' : 'other'">
            <span>{{ msg.content }}</span>
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

function toggleChat() {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    nextTick(() => {
      document.addEventListener('mousedown', handleClickOutside)
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
function sendMessage() {
  if (!input.value.trim() || !stompClient) return
  const payload = {
    to: selectedTarget.value.id,
    type: selectedType.value,
    content: input.value
  }

  // send via STOMP to application destination - adjust destination if backend expects a different path
  try {
    stompClient.send(stompAppPrefix + '/chat.' + selectedType.value, {}, JSON.stringify(payload))
  } catch (e) {
    console.error('send failed', e)
  }

  messages.value.push({
    id: Date.now(),
    content: input.value,
    fromSelf: true
  })
  input.value = ''
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
        const content = (body && body.content) ? body.content : (typeof body === 'string' ? body : JSON.stringify(body))
        messages.value.push({ id: Date.now(), content, fromSelf: false })
      })
    } catch (e) {
      console.error('subscribe failed', e)
      // fallback: subscribe to a generic room
      // try {
      //   stompClient.subscribe('/room/messages', function(message) {
      //     let body = message.body
      //     try { body = JSON.parse(message.body) } catch (e) {}
      //     messages.value.push({ id: Date.now(), content: body.content || body, fromSelf: false })
      //   })
      // } catch (err) {
      //   console.error('fallback subscribe failed', err)
      // }
    }

    // // optionally notify server that we joined (backend may or may not expect this)
    // try {
    //   stompClient.send(stompAppPrefix + '/chat.join', {}, JSON.stringify({ targetId: selectedTarget.value.id, targetType: selectedType.value }))
    // } catch (e) { /* ignore if backend doesn't handle it */ }

  }, function(err) {
    console.error('STOMP connection error', err)
  })
}

onMounted(async () => {
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
}
.chat-messages .self {
  text-align: right;
  color: #409eff;
}
.chat-messages .other {
  text-align: left;
  color: #333;
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
