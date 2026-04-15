import type { Page } from '@playwright/test';

const BASE = '/api';

/** A tiny 1×1 white PNG — enough to satisfy the <img> tag */
const DUMMY_CAPTCHA_PNG = Buffer.from(
  'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwADhQGAWjR9awAAAABJRU5ErkJggg==',
  'base64'
);

/** Mock GET /api/auth/captcha — returns a dummy image, no backend session needed */
export async function mockCaptchaEndpoint(page: Page): Promise<void> {
  await page.route(`${BASE}/auth/captcha**`, (route) =>
    route.fulfill({
      status: 200,
      contentType: 'image/png',
      body: DUMMY_CAPTCHA_PNG,
    })
  );
}

/** Mock GET /api/role/account — returns a minimal role list */
export async function mockRolesEndpoint(page: Page): Promise<void> {
  await page.route(`${BASE}/role/account`, (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify([
        { id: 1, name: 'MEMBER',    description: '一般成員' },
        { id: 2, name: 'VOLUNTEER', description: '志工' },
      ]),
    })
  );
}

/** Mock POST /api/auth/login — success */
export async function mockLoginSuccess(page: Page): Promise<void> {
  await page.route(`${BASE}/auth/login`, (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      headers: { Authorization: 'Bearer mock.jwt.token' },
      body: JSON.stringify({}),
    })
  );
}

/** Mock POST /api/auth/login — wrong credentials */
export async function mockLoginWrongCredentials(page: Page): Promise<void> {
  await page.route(`${BASE}/auth/login`, (route) =>
    route.fulfill({
      status: 401,
      contentType: 'application/json',
      body: JSON.stringify({ error: '帳號不存在或密碼有誤' }),
    })
  );
}

/** Mock POST /api/auth/login — expired captcha */
export async function mockLoginExpiredCaptcha(page: Page): Promise<void> {
  await page.route(`${BASE}/auth/login`, (route) =>
    route.fulfill({
      status: 400,
      contentType: 'application/json',
      body: JSON.stringify({ error: '驗證碼逾期，驗證' }),
    })
  );
}

/** Mock POST /api/auth/login — wrong captcha (not expired) */
export async function mockLoginInvalidCaptcha(page: Page): Promise<void> {
  await page.route(`${BASE}/auth/login`, (route) =>
    route.fulfill({
      status: 400,
      contentType: 'application/json',
      body: JSON.stringify({ error: '驗證碼驗證' }),
    })
  );
}

/** Mock POST /api/account/register — success */
export async function mockRegisterSuccess(page: Page): Promise<void> {
  await page.route(`${BASE}/account/register`, (route) =>
    route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({ message: '註冊成功' }),
    })
  );
}

/** Mock POST /api/account/register — duplicate username */
export async function mockRegisterDuplicateUsername(page: Page): Promise<void> {
  await page.route(`${BASE}/account/register`, (route) =>
    route.fulfill({
      status: 400,
      contentType: 'application/json',
      body: JSON.stringify({ error: '帳號已存在' }),
    })
  );
}

/** Mock POST /api/account/register — server validation errors (array form) */
export async function mockRegisterValidationError(page: Page): Promise<void> {
  await page.route(`${BASE}/account/register`, (route) =>
    route.fulfill({
      status: 400,
      contentType: 'application/json',
      body: JSON.stringify({ error: ['帳號格式不符', '密碼強度不足'] }),
    })
  );
}
