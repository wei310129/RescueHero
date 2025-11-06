import {apiContextPath} from './apiFetch'

/**
 * 取得驗證碼圖片網址（自動帶時間戳避免快取）
 * @returns {Promise<string>} 圖片網址
 */
export async function getCaptchaUrl() {
  return `${apiContextPath}/auth/captcha?ts=${Date.now()}`
}
