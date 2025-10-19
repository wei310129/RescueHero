<template>
  <div class="login-container">
    <h2>註冊</h2>
    <form @submit.prevent="handleRegister">
      <input v-model="username" placeholder="Account" />
      <input v-model="password" type="password" placeholder="Password" />
      <input v-model="confirmPassword" type="password" placeholder="Confirm Password" />
      -
      <input v-model="email" placeholder="Email" />
      -
      <input v-model="nickname" placeholder="Nickname" />
      <input v-model="phone" placeholder="Phone" />
      <select v-model="roleId">
        <option value="" disabled>請選擇角色</option>
        <option v-for="role in roles" :key="role.id" :value="role.id">{{ role.name }}</option>
      </select>
      <div class="captcha-row">
        <input v-model="captcha" placeholder="輸入驗證碼" />
        <img :src="captchaUrl" @click="reloadCaptcha" alt="captcha" class="captcha-img" />
      </div>
      <button type="submit">註冊</button>
    </form>
    <div class="switch-link">
      已有帳號？<router-link to="/login">登入</router-link>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'

const username = ref('')
const nickname = ref('')
const email = ref('')
const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const roleId = ref('')
const roles = ref([])
const captcha = ref('')
const captchaUrl = ref('')

function reloadCaptcha() {
  captchaUrl.value = `/api/auth/captcha?ts=${Date.now()}`
}

onMounted(async () => {
  reloadCaptcha()
  try {
    const res = await fetch('/api/role/account')
    roles.value = await res.json()
  } catch (e) {
    roles.value = []
  }
})

async function handleRegister() {
  if (password.value !== confirmPassword.value) {
    alert('密碼不一致')
    return
  }
  if (!roleId.value) {
    alert('請選擇角色')
    return
  }
  const res = await fetch('/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: username.value,
      nickname: nickname.value,
      email: email.value,
      phone: phone.value,
      password: password.value,
      roleId: roleId.value,
      captcha: captcha.value
    })
  })
  const data = await res.json()
  if (!res.ok) {
    alert(data.error || JSON.stringify(data))
    reloadCaptcha()
    return
  }
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

input, select {
  padding: 10px 14px;
  border: 1px solid #bdbdbd;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.2s;
}

input:focus, select:focus {
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

.switch-link {
  margin-top: 18px;
  font-size: 15px;
  color: #666;
  text-align: center;
}

.switch-link a {
  color: #1976d2;
  text-decoration: underline;
  margin-left: 4px;
  cursor: pointer;
}

.captcha-row {
  display: flex;
  gap: 4px;
  align-items: center;
  width: 100%;
}

.captcha-row input {
  width: 60%;
}

.captcha-img {
  height: 48px;
  width: 40%;
  object-fit: contain;
  cursor: pointer;
  border: none;
  border-radius: 6px;
}
</style>
