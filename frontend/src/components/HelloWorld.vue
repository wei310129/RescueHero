<template>
  <div>
    <h1>{{ message }}</h1>
    <button @click="changeMessage">切换消息</button>
  </div>
</template>

<script setup>
import {ref} from 'vue'

const message = ref('你好，Vue 3 + Composition API！')

function changeMessage() {
  message.value = '消息已切换！'
}
</script>
