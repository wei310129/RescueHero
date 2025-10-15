<template>
  <div class="login-container">
    <h2>登入</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="username" placeholder="Account" />
      <input v-model="password" type="password" placeholder="Password" />
      <button type="submit">登入</button>
    </form>
  </div>
</template>

<script setup>
import {ref} from 'vue'

const username = ref('')
const password = ref('')

async function handleLogin() {
  const res = await fetch('/api/login', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({
      username: username.value,
      password: password.value
    })
  })
  const data = await res.json()
  // 根據 data 處理登入結果
  alert(JSON.stringify(data))
}
</script>

<style scoped>
.login-container {
  max-width: 350px;
  margin: 100px auto;
  padding: 32px 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  margin-bottom: 24px;
  color: #1976d2;
  font-weight: 700;
  letter-spacing: 2px;
}

form {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

input {
  padding: 10px 14px;
  border: 1px solid #bdbdbd;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.2s;
}

input:focus {
  border-color: #1976d2;
  outline: none;
}

button {
  padding: 12px 0;
  background: linear-gradient(90deg, #1976d2 0%, #42a5f5 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(33,150,243,0.12);
  transition: background 0.2s;
}

button:hover {
  background: linear-gradient(90deg, #1565c0 0%, #1e88e5 100%);
}
</style>
