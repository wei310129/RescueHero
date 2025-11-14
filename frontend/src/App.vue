<template>
  <AppHeader />
  <div style="padding-top: 64px;">
    <router-view />
  </div>
  <ChatWidget />
</template>

<script setup>
import AppHeader from './components/AppHeader.vue'
import ChatWidget from './components/ChatWidget.vue'
</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
