<template>
  <header class="header">
    <div class="left" @click="$router.push('/')">
      <img src="@/assets/logo.png" alt="logo" class="logo-img" />
      <span class="title">Rescue Hero 救援英雄</span>
    </div>
    <div class="right">
      <button class="menu-btn" ref="menuBtn" @click="toggleMenu">
        <span class="menu-icon">&#9776;</span>
      </button>
      <nav v-if="menuOpen" ref="navMenu" class="nav-menu">
        <router-link to="/login">登入</router-link>
        <router-link to="/news">最新消息</router-link>
        <router-link to="/tasks">任務總覽</router-link>
        <router-link to="/contact">聯絡我們</router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue'

const menuOpen = ref(false)
const navMenu = ref(null)
const menuBtn = ref(null)

function toggleMenu() {
  menuOpen.value = !menuOpen.value
}

function handleClickOutside(event) {
  if (!menuOpen.value) return
  const menu = navMenu.value
  const btn = menuBtn.value
  if (menu && !menu.contains(event.target) && btn && !btn.contains(event.target)) {
    menuOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})
onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
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
.logo {
  font-size: 2rem;
  margin-right: 12px;
}
.title {
  font-size: 1.3rem;
  font-weight: 700;
  letter-spacing: 2px;
}
.right {
  position: relative;
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
  display: flex;
  flex-direction: column;
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

</style>
