<template>
  <header class="header">
    <div class="left" @click="$router.push('/')">
      <img src="@/assets/logo.png" alt="logo" class="logo-img" />
      <span class="title">Rescue Hero 救援英雄</span>
    </div>
    <div class="right">
      <div class="user-area">
        <span v-if="currentUser" class="greeting">Hi, {{ currentUser }}</span>
        <div class="bell-container" ref="bellBtn">
          <button class="bell-btn" @click="toggleBell" aria-label="notifications">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#fff" stroke-width="1.5">
              <path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0 1 18 14.158V11a6 6 0 1 0-12 0v3.159c0 .538-.214 1.055-.595 1.436L4 17h11z"/>
            </svg>
            <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
          </button>
          <div v-if="bellOpen" ref="bellMenu" class="bell-dropdown">
            <div class="dropdown-header">Notifications</div>
            <ul>
              <li v-for="item in notifications" :key="item.id" :class="{ unread: !item.read }" @click="openNotification(item)">
                <div class="notif-title">{{ item.title || item.message || item || '（無標題）' }}</div>
                <div class="notif-time">{{ formatTime(item.createdAt) }}</div>
              </li>
              <li v-if="notifications.length === 0" class="empty">沒有通知</li>
            </ul>
          </div>
        </div>
      </div>
      <button class="menu-btn" ref="menuBtn" @click="toggleMenu">
        <span class="menu-icon">&#9776;</span>
      </button>
      <nav v-if="menuOpen" ref="navMenu" class="nav-menu">
        <template v-if="!currentUser">
          <router-link to="/login">登入</router-link>
        </template>
        <template v-else>
          <a href="#" @click.prevent="handleLogout">登出</a>
        </template>
        <router-link to="/news">最新消息</router-link>
        <router-link to="/tasks">任務總覽</router-link>
        <router-link to="/contact">聯絡我們</router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {apiFetch} from '@/utils/apiFetch'

const router = useRouter()
const menuOpen = ref(false)
const navMenu = ref(null)
const menuBtn = ref(null)

const bellOpen = ref(false)
const bellMenu = ref(null)
const bellBtn = ref(null)
const notifications = ref([])

const currentUser = ref('')

function toggleMenu() {
  menuOpen.value = !menuOpen.value
}

async function fetchNotifications() {
  try {
    const res = await apiFetch('/api/notification/user')
    if (!res.ok) return
    const data = await res.json()
    notifications.value = Array.isArray(data) ? data : []
  } catch (e) {
    notifications.value = []
  }
}

const unreadCount = computed(() => notifications.value.filter(n => !n.read).length)

async function toggleBell() {
  bellOpen.value = !bellOpen.value
  if (bellOpen.value) {
    await fetchNotifications()
  }
}

async function markAsRead(ids) {
  if (!ids || ids.length === 0) return
  try {
    await apiFetch('/api/notification/mark-read', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ids })
    })
  } catch (e) {
    // ignore network errors
  }
}

async function openNotification(item) {
  if (!item) return
  // mark locally
  if (!item.read) {
    item.read = true
    // fire mark-read for this item
    markAsRead([item.id])
  }
  // navigate if url provided
  if (item.url) {
    // if url is internal route, use router
    if (item.url.startsWith('/')) {
      router.push(item.url)
    } else {
      window.open(item.url, '_blank')
    }
  }
  // optionally close bell dropdown
  bellOpen.value = false
}

function formatTime(ts) {
  if (!ts) return ''
  const d = new Date(ts)
  return d.toLocaleString()
}

function handleClickOutside(event) {
  // handle menu
  if (menuOpen.value) {
    const menu = navMenu.value
    const btn = menuBtn.value
    if (menu && !menu.contains(event.target) && btn && !btn.contains(event.target)) {
      menuOpen.value = false
    }
  }
  // handle bell dropdown
  if (bellOpen.value) {
    const menu = bellMenu.value
    const btn = bellBtn.value
    if (menu && !menu.contains(event.target) && btn && !btn.contains(event.target)) {
      bellOpen.value = false
    }
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
  try { window.addEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.addEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }
})
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
  try { window.removeEventListener('user-login', handleUserLogin) } catch (e) { /* ignore */ }
  try { window.removeEventListener('user-logout', handleUserLogout) } catch (e) { /* ignore */ }
})

function handleUserLogin() {
  try { currentUser.value = localStorage.getItem('currentUser') || '' } catch (e) { currentUser.value = '' }
  // optionally refresh notifications after login
  fetchNotifications()
}

function handleLogout() {
  try {
    localStorage.removeItem('currentUser')
  } catch (e) {
    alert("無法登出，請檢查瀏覽器設定");
  }
  currentUser.value = ''
  // 通知 header 立即更新
  try {
    window.dispatchEvent(new Event('user-logout'))
  } catch (e) {
    alert("登出失敗，請檢查瀏覽器設定");
  }
  // 導回登入頁
  router.push('/login')
}

function handleUserLogout() {
  currentUser.value = ''
}
</script>

<style scoped>
.header {
  width: 100%;
  height: 64px;
  background: linear-gradient(90deg, #1976d2 0%, #42a5f5 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(33,150,243,0.12);
}
.left {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.title {
  font-size: 1.3rem;
  font-weight: 700;
  letter-spacing: 2px;
}
.right {
  position: relative;
  display: flex;
  align-items: center;
  height: 64px;
}
.menu-btn {
  background: none;
  border: none;
  color: #fff;
  font-size: 2rem;
  cursor: pointer;
  padding: 12px 32px; /* 上下 12px，左右 16px */
}
.nav-menu {
  position: absolute;
  top: 56px;
  right: 24px;
  background: #fff;
  color: #1976d2;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(33,150,243,0.12);
  display: flex;
  flex-direction: column;
  min-width: 140px;
  max-width: calc(100vw - 16px); /* 保留安全邊界 */
  padding: 12px 0;
  box-sizing: border-box;

  /* 靠左對齊 */
  align-items: flex-start;
}

.nav-menu a {
  padding: 10px 24px;
  text-decoration: none;
  color: #1976d2;
  font-weight: 600;
  transition: background 0.2s;
}
.nav-menu a:hover {
  background: #e3f2fd;
}
.logo-img {
  height: 100%;
  max-height: 48px;
  width: auto;
  margin-right: 12px;
  object-fit: contain;
  display: block;
}
.user-area {
  display: flex;
  align-items: center;
  height: 64px; /* 與 header 同高，確保垂直置中 */
}
.greeting {
  margin-right: 12px;
  font-size: 1rem;
  line-height: 64px; /* 垂直置中 */
}
.bell-container {
  display: flex;
  align-items: center;
  height: 64px;
  margin-right: 12px;
  position: relative; /* 新增，讓 dropdown 以這裡為定位基準 */
}
.bell-btn {
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 0 8px;
  height: 40px;
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #f44336;
  color: #fff;
  border-radius: 50%;
  padding: 4px 8px;
  font-size: 0.75rem;
}
.bell-dropdown {
  position: absolute;
  top: 56px;
  left: 100%; /* 從 bell-btn 右側開始 */
  right: auto;
  transform: translateX(calc(32px - 100%)); /* 32px 為 menu-btn 的 padding-right，讓右緣對齊 */
  background: #fff;
  color: #1976d2;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(33,150,243,0.12);
  width: 300px;
  max-width: calc(100vw - 16px);
  z-index: 200;
  display: flex;
  flex-direction: column;
}
.dropdown-header {
  background: #f5f5f5;
  padding: 12px;
  font-weight: 600;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}
.empty {
  text-align: center;
  padding: 12px;
  color: #999;
}
.notif-title {
  font-weight: 500;
  text-align: left;
}
.notif-time {
  font-size: 0.875rem;
  color: #999;
}
</style>
