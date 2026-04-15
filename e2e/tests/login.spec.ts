import { test, expect } from '../fixtures';
import {
  mockLoginSuccess,
  mockLoginWrongCredentials,
  mockLoginExpiredCaptcha,
  mockLoginInvalidCaptcha,
} from '../helpers/mock-api';

const VALID_USER = 'testuser01';
const VALID_PASS = 'Passw0rd!';

test.describe('Login page', () => {
  // ------------------------------------------------------------------
  // Happy path
  // ------------------------------------------------------------------

  test('successful login redirects to /tasks', async ({ page, loginHelper }) => {
    await mockLoginSuccess(page);
    await loginHelper.navigate();
    await loginHelper.fillAndSubmit(VALID_USER, VALID_PASS);

    await expect(page).toHaveURL(/\/tasks/);
  });

  test('successful login stores accessToken in localStorage', async ({ page, loginHelper }) => {
    await mockLoginSuccess(page);
    await loginHelper.navigate();
    await loginHelper.fillAndSubmit(VALID_USER, VALID_PASS);

    await page.waitForURL(/\/tasks/);
    const token = await page.evaluate(() => localStorage.getItem('accessToken'));
    expect(token).toBe('Bearer mock.jwt.token');
  });

  // ------------------------------------------------------------------
  // Wrong credentials
  // ------------------------------------------------------------------

  test('wrong credentials shows alert and stays on /login', async ({ page, loginHelper }) => {
    await mockLoginWrongCredentials(page);
    await loginHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await loginHelper.fillAndSubmit('wronguser1', 'WrongPass1!');

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('帳號不存在或密碼有誤');
    await dialog.dismiss();

    await expect(page).toHaveURL(/\/login/);
  });

  // ------------------------------------------------------------------
  // Empty fields — login has no client-side guards, API is always called
  // ------------------------------------------------------------------

  test('submitting empty form shows error from API', async ({ page, loginHelper }) => {
    await mockLoginWrongCredentials(page);
    await loginHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    // submit without filling any fields
    await page.click('button[type="submit"]');

    const dialog = await alertPromise;
    expect(dialog.message()).toBeTruthy();
    await dialog.dismiss();

    await expect(page).toHaveURL(/\/login/);
  });

  // ------------------------------------------------------------------
  // Captcha errors
  // ------------------------------------------------------------------

  test('expired captcha shows alert with 逾期 and triggers captcha reload', async ({ page, loginHelper }) => {
    await mockLoginExpiredCaptcha(page);
    await loginHelper.navigate();

    // Count captcha reload requests after the initial page load
    let captchaReloadCount = 0;
    await page.route('/api/auth/captcha**', (route) => {
      captchaReloadCount++;
      route.fulfill({ status: 200, contentType: 'image/png', body: Buffer.alloc(0) });
    });

    const alertPromise = page.waitForEvent('dialog');
    await loginHelper.fillAndSubmit(VALID_USER, VALID_PASS, 'WRONG');

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('逾期');
    await dialog.dismiss();

    // Vue calls reloadCaptcha() when message includes '逾期'
    await expect(async () => {
      expect(captchaReloadCount).toBeGreaterThan(0);
    }).toPass({ timeout: 3_000 });
  });

  test('wrong captcha shows alert and stays on /login', async ({ page, loginHelper }) => {
    await mockLoginInvalidCaptcha(page);
    await loginHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await loginHelper.fillAndSubmit(VALID_USER, VALID_PASS, 'BADCAP');

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('驗證碼');
    await dialog.dismiss();

    await expect(page).toHaveURL(/\/login/);
  });
});
