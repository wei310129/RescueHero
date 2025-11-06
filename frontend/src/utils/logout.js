import router from '../router'
import {apiFetch} from './apiFetch'

export async function globalLogout() {
  // 先通知後端登出
  try {
    await apiFetch('/auth/logout', { method: 'POST' })
  } catch (e) {
    // 可選：顯示提示，但仍繼續本地登出
    alert('後端登出失敗，已強制本地登出');
  }
  try {
    localStorage.removeItem('currentUser')
    localStorage.removeItem('accessToken')
  } catch (e) {
    alert('無法登出，請檢查瀏覽器設定');
  }
  // 通知 header 立即更新
  try {
    window.dispatchEvent(new Event('user-logout'))
  } catch (e) {
    alert('登出失敗，請檢查瀏覽器設定');
  }
  // 導回登入頁
  if (router && typeof router.push === 'function') {
    router.push('/login')
  }
}
