<template>
  <div class="login-container">
    <h2>註冊</h2>
    <form @submit.prevent="handleRegister">
      <!-- 帳號輸入框（右側人像圖示） -->
      <div class="input-row">
        <input v-model="username" placeholder="Account" @input="username = username.toLowerCase(); removeSpaces('username')" />
        <span class="input-icon">
          <!-- 人像 SVG -->
          <svg width="24" height="24" viewBox="0 0 24 24">
            <circle cx="12" cy="8" r="4" fill="#888"/>
            <path d="M4 20c0-4 16-4 16 0" stroke="#888" stroke-width="2" fill="none"/>
          </svg>
        </span>
      </div>
      <div class="password-row">
        <input v-model="password" :type="showPassword ? 'text' : 'password'" placeholder="Password" class="password-input" @input="removeSpaces('password')" />
        <span class="toggle-icon" @click="showPassword = !showPassword">
          <svg v-if="showPassword" width="24" height="24" viewBox="0 0 24 24">
            <path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12zm11 5a5 5 0 1 0 0-10 5 5 0 0 0 0 10zm0-2a3 3 0 1 1 0-6 3 3 0 0 1 0 6z" fill="#888"/>
          </svg>
          <svg v-else width="24" height="24" viewBox="0 0 24 24">
            <path d="M17.94 17.94A10.97 10.97 0 0 1 12 19c-7 0-11-7-11-7a21.77 21.77 0 0 1 5.06-6.06M1 1l22 22M9.53 9.53A3 3 0 0 0 12 15a3 3 0 0 0 2.47-5.47" stroke="#888" stroke-width="2" fill="none"/>
          </svg>
        </span>
      </div>
      <div class="password-row">
        <input v-model="confirmPassword" :type="showConfirmPassword ? 'text' : 'password'" placeholder="Confirm Password" class="password-input" @input="removeSpaces('confirmPassword')" />
        <span class="toggle-icon" @click="showConfirmPassword = !showConfirmPassword">
          <svg v-if="showConfirmPassword" width="24" height="24" viewBox="0 0 24 24">
            <path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12zm11 5a5 5 0 1 0 0-10 5 5 0 0 0 0 10zm0-2a3 3 0 1 1 0-6 3 3 0 0 1 0 6z" fill="#888"/>
          </svg>
          <svg v-else width="24" height="24" viewBox="0 0 24 24">
            <path d="M17.94 17.94A10.97 10.97 0 0 1 12 19c-7 0-11-7-11-7a21.77 21.77 0 0 1 5.06-6.06M1 1l22 22M9.53 9.53A3 3 0 0 0 12 15a3 3 0 0 0 2.47-5.47" stroke="#888" stroke-width="2" fill="none"/>
          </svg>
        </span>
      </div>
      <!-- 信箱輸入框（右側信件圖示） -->
      <div class="input-row">
        <input v-model="email" placeholder="Email" @input="removeSpaces('email')" />
        <span class="input-icon">
          <!-- 信件 SVG -->
          <svg width="24" height="24" viewBox="0 0 24 24">
            <rect x="3" y="6" width="18" height="12" rx="2" fill="#888"/>
            <polyline points="3,6 12,13 21,6" stroke="#fff" stroke-width="2" fill="none"/>
          </svg>
        </span>
      </div>
<!--      <input v-model="email" placeholder="Email" />-->
<!--      <input v-model="nickname" placeholder="Nickname" />-->
<!--      <input v-model="phone" placeholder="Phone" />-->
      <select v-model="roleId">
        <option value="" disabled>請選擇角色</option>
        <option v-for="role in roles" :key="role.id" :value="role.id">{{ role.description }}</option>
      </select>
      <div class="captcha-row">
        <input v-model="captcha" placeholder="輸入驗證碼" @input="removeSpaces('captcha')" />
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
import {useRouter} from 'vue-router'
import {apiFetch} from '@/utils/apiFetch'
import {getCaptchaUrl} from '@/utils/getCaptchaUrl'

const username = ref('')
// const nickname = ref('')
const email = ref('')
// const phone = ref('')
const password = ref('')
const confirmPassword = ref('')
const roleId = ref('')
const roles = ref([])
const captcha = ref('')
const captchaUrl = ref('')
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const router = useRouter()

async function reloadCaptcha() {
  captchaUrl.value = await getCaptchaUrl()
}

function removeSpaces(field) {
  if (field === 'username') username.value = username.value.replace(/\s/g, '')
  if (field === 'password') password.value = password.value.replace(/\s/g, '')
  if (field === 'confirmPassword') confirmPassword.value = confirmPassword.value.replace(/\s/g, '')
  if (field === 'email') email.value = email.value.replace(/\s/g, '')
  if (field === 'captcha') captcha.value = captcha.value.replace(/\s/g, '')
}

onMounted(async () => {
  reloadCaptcha()
  try {
    const res = await apiFetch('/role/account')
    roles.value = await res.json()
  } catch (e) {
    roles.value = []
  }
})

async function handleRegister() {
  if (!username.value) {alert('請輸入帳號');return;}
  if (!password.value) {alert('請輸入密碼');return;}
  if (password.value !== confirmPassword.value) {alert('密碼不一致');return;}
  if (!email.value) {alert('請輸入信箱');return;}
  if (!roleId.value) {alert('請選擇角色');return;}
  if (!captcha.value) {alert('請輸入驗證碼');return;}

  const res = await apiFetch('/account/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: username.value,
      // nickname: nickname.value,
      email: email.value,
      // phone: phone.value,
      password: password.value,
      roleId: roleId.value,
      captcha: captcha.value
    })
  })
  const data = await res.json()
  if (!res.ok) {
    let msg = data.error
    if (Array.isArray(msg)) {
      msg = msg.join('\n')
    }
    alert(msg)
    if (msg.includes('逾期')) {
      reloadCaptcha()
    }
    return
  }

  alert('註冊成功')
  // After successful registration, navigate to login page
  router.push('/login')
}
</script>

<style scoped>
.input-row {
  position: relative;
  width: 100%;
}
.input-row input {
  width: 100%;
  box-sizing: border-box;
  padding-right: 40px;
}
.input-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  display: flex;
  align-items: center;
}
.password-row {
  position: relative;
  width: 100%;
}
.password-input {
  width: 100%;
  box-sizing: border-box;
  padding-right: 40px;
}
.toggle-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  user-select: none;
  display: flex;
  align-items: center;
}
.login-container {
  max-width: 350px;
  width: 100%;
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
