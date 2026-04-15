import {expect, test} from '../fixtures';
import {mockRegisterDuplicateUsername, mockRegisterSuccess, mockRegisterValidationError,} from '../helpers/mock-api';

const VALID_USER  = 'newuser01';
const VALID_PASS  = 'Passw0rd!';
const VALID_EMAIL = 'newuser@example.com';

test.describe('Register page', () => {
  // ------------------------------------------------------------------
  // Happy path
  // ------------------------------------------------------------------

  test('successful registration shows alert and redirects to /login', async ({ page, registerHelper }) => {
    await mockRegisterSuccess(page);
    await registerHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await registerHelper.fillForm({
      username: VALID_USER,
      password: VALID_PASS,
      confirmPassword: VALID_PASS,
      email: VALID_EMAIL,
      roleIndex: 0,
    });
    await registerHelper.submit();

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('註冊成功');
    await dialog.accept();

    await expect(page).toHaveURL(/\/login/);
  });

  // ------------------------------------------------------------------
  // Duplicate username
  // ------------------------------------------------------------------

  test('duplicate username shows error alert and stays on /register', async ({ page, registerHelper }) => {
    await mockRegisterDuplicateUsername(page);
    await registerHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await registerHelper.fillForm({
      username: 'existinguser',
      password: VALID_PASS,
      confirmPassword: VALID_PASS,
      email: VALID_EMAIL,
      roleIndex: 0,
    });
    await registerHelper.submit();

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('帳號已存在');
    await dialog.dismiss();

    await expect(page).toHaveURL(/\/register/);
  });

  // ------------------------------------------------------------------
  // Password mismatch — client-side guard, no API call
  // ------------------------------------------------------------------

  test('mismatched passwords shows 密碼不一致 alert without calling API', async ({ page, registerHelper }) => {
    await registerHelper.navigate();

    let apiCalled = false;
    await page.route('/api/account/register', async (route) => { apiCalled = true; await route.abort(); });

    // Client-side guard fires alert() synchronously inside the click handler.
    // page.once('dialog') auto-dismisses so page.click() is not blocked.
    let dialogMessage = '';
    page.once('dialog', async (dialog) => { dialogMessage = dialog.message(); await dialog.dismiss(); });

    await registerHelper.fillForm({
      username: VALID_USER,
      password: VALID_PASS,
      confirmPassword: 'Different1!',
      email: VALID_EMAIL,
      roleIndex: 0,
    });
    await registerHelper.submit();

    expect(dialogMessage).toContain('密碼不一致');
    expect(apiCalled).toBe(false);
    await expect(page).toHaveURL(/\/register/);
  });

  // ------------------------------------------------------------------
  // Missing required fields — client-side guards, no API call
  // ------------------------------------------------------------------

  const missingFieldCases = [
    {
      label: 'no username',
      fill: { username: '', password: VALID_PASS, confirmPassword: VALID_PASS, email: VALID_EMAIL },
      expected: '請輸入帳號',
    },
    {
      label: 'no password',
      fill: { username: VALID_USER, password: '', confirmPassword: '', email: VALID_EMAIL },
      expected: '請輸入密碼',
    },
    {
      label: 'no email',
      fill: { username: VALID_USER, password: VALID_PASS, confirmPassword: VALID_PASS, email: '' },
      expected: '請輸入信箱',
    },
  ];

  for (const { label, fill, expected } of missingFieldCases) {
    test(`missing field (${label}) shows "${expected}" alert without calling API`, async ({ page, registerHelper }) => {
      await registerHelper.navigate();

      let apiCalled = false;
      await page.route('/api/account/register', async (route) => { apiCalled = true; await route.abort(); });

      // Synchronous alert — auto-dismiss via page.once so page.click() is not blocked.
      let dialogMessage = '';
      page.once('dialog', async (dialog) => { dialogMessage = dialog.message(); await dialog.dismiss(); });

      await registerHelper.fillForm({ ...fill, roleIndex: 0 });
      await registerHelper.submit();

      expect(dialogMessage).toContain(expected);
      expect(apiCalled).toBe(false);
    });
  }

  // ------------------------------------------------------------------
  // No role selected — client-side guard
  // ------------------------------------------------------------------

  test('no role selected shows 請選擇角色 alert without calling API', async ({ page, registerHelper }) => {
    await registerHelper.navigate();

    let apiCalled = false;
    await page.route('/api/account/register', async (route) => { apiCalled = true; await route.abort(); });

    // Synchronous alert — auto-dismiss via page.once so page.click() is not blocked.
    let dialogMessage = '';
    page.once('dialog', async (dialog) => { dialogMessage = dialog.message(); await dialog.dismiss(); });

    // Fill all fields but leave the role <select> at its disabled placeholder
    await page.fill('input[placeholder="Account"]', VALID_USER);
    await page.fill('input[placeholder="Password"]', VALID_PASS);
    await page.fill('input[placeholder="Confirm Password"]', VALID_PASS);
    await page.fill('input[placeholder="Email"]', VALID_EMAIL);
    await page.fill('input[placeholder="輸入驗證碼"]', 'XXXXX');
    // Do NOT select a role — default value is '' (disabled placeholder)
    await page.click('button[type="submit"]');

    expect(dialogMessage).toContain('請選擇角色');
    expect(apiCalled).toBe(false);
  });

  // ------------------------------------------------------------------
  // Server validation error array
  // ------------------------------------------------------------------

  test('server validation error array joins messages in alert', async ({ page, registerHelper }) => {
    await mockRegisterValidationError(page);
    await registerHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await registerHelper.fillForm({
      username: 'bad',
      password: 'weak',
      confirmPassword: 'weak',
      email: VALID_EMAIL,
      roleIndex: 0,
    });
    await registerHelper.submit();

    const dialog = await alertPromise;
    // Vue joins the error array with '\n'
    expect(dialog.message()).toContain('帳號格式不符');
    expect(dialog.message()).toContain('密碼強度不足');
    await dialog.dismiss();
  });

  // ------------------------------------------------------------------
  // Expired captcha on register
  // ------------------------------------------------------------------

  test('expired captcha shows alert with 逾期 and stays on /register', async ({ page, registerHelper }) => {
    await page.route('/api/account/register', (route) =>
      route.fulfill({
        status: 400,
        contentType: 'application/json',
        body: JSON.stringify({ error: '驗證碼逾期，驗證' }),
      })
    );
    await registerHelper.navigate();

    const alertPromise = page.waitForEvent('dialog');
    await registerHelper.fillForm({
      username: VALID_USER,
      password: VALID_PASS,
      confirmPassword: VALID_PASS,
      email: VALID_EMAIL,
      roleIndex: 0,
    });
    await registerHelper.submit();

    const dialog = await alertPromise;
    expect(dialog.message()).toContain('逾期');
    await dialog.dismiss();

    await expect(page).toHaveURL(/\/register/);
  });
});
