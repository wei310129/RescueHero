import { test as base, expect, type Page, type Dialog } from '@playwright/test';
import { mockCaptchaEndpoint, mockRolesEndpoint } from '../helpers/mock-api';
import { LOGIN, REGISTER } from '../helpers/selectors';

// ---------------------------------------------------------------------------
// Page-object helpers
// ---------------------------------------------------------------------------

export class LoginPageHelper {
  constructor(private readonly page: Page) {}

  async navigate() {
    await this.page.goto('/login');
  }

  async fillForm(username: string, password: string, captchaValue = 'XXXXX') {
    await this.page.fill(LOGIN.usernameInput, username);
    await this.page.fill(LOGIN.passwordInput, password);
    await this.page.fill(LOGIN.captchaInput, captchaValue);
  }

  async submit() {
    await this.page.click(LOGIN.submitButton);
  }

  /**
   * Click submit and wait for the resulting alert/confirm dialog.
   *
   * Works for both sync alerts (fired during the click handler, e.g. client-side
   * guards) and async alerts (fired after an API call completes):
   *  - The click is dispatched but NOT awaited, so Playwright's event loop can
   *    process the dialog event even when the browser is blocked by a sync alert.
   *  - waitForEvent('dialog') resolves as soon as the dialog appears, whether
   *    that is immediately (sync) or after an API round-trip (async).
   */
  async submitAndWaitForDialog(): Promise<Dialog> {
    const dialogPromise = this.page.waitForEvent('dialog');
    void this.page.click(LOGIN.submitButton); // intentionally fire-and-forget
    return dialogPromise;
  }

  async fillAndSubmit(username: string, password: string, captchaValue = 'XXXXX') {
    await this.fillForm(username, password, captchaValue);
    await this.submit();
  }

  async fillAndSubmitThenDialog(username: string, password: string, captchaValue = 'XXXXX'): Promise<Dialog> {
    await this.fillForm(username, password, captchaValue);
    return this.submitAndWaitForDialog();
  }
}

export class RegisterPageHelper {
  constructor(private readonly page: Page) {}

  async navigate() {
    await this.page.goto('/register');
  }

  async fillForm(opts: {
    username: string;
    password: string;
    confirmPassword: string;
    email: string;
    roleIndex?: number;
    captchaValue?: string;
  }) {
    const {
      username,
      password,
      confirmPassword,
      email,
      roleIndex = 0,
      captchaValue = 'XXXXX',
    } = opts;

    await this.page.fill(REGISTER.usernameInput, username);
    await this.page.fill(REGISTER.passwordInput, password);
    await this.page.fill(REGISTER.confirmPasswordInput, confirmPassword);
    await this.page.fill(REGISTER.emailInput, email);
    // index 0 is the disabled placeholder, so shift by 1 to reach real roles
    await this.page.locator(REGISTER.roleSelect).selectOption({ index: roleIndex + 1 });
    await this.page.fill(REGISTER.captchaInput, captchaValue);
  }

  async submit() {
    await this.page.click(REGISTER.submitButton);
  }

  /** Same fire-and-forget pattern as LoginPageHelper.submitAndWaitForDialog */
  async submitAndWaitForDialog(): Promise<Dialog> {
    const dialogPromise = this.page.waitForEvent('dialog');
    void this.page.click(REGISTER.submitButton); // intentionally fire-and-forget
    return dialogPromise;
  }
}

// ---------------------------------------------------------------------------
// Custom fixtures
// ---------------------------------------------------------------------------

type AppFixtures = {
  loginHelper: LoginPageHelper;
  registerHelper: RegisterPageHelper;
};

export const test = base.extend<AppFixtures>({
  loginHelper: async ({ page }, use) => {
    await mockCaptchaEndpoint(page);
    await use(new LoginPageHelper(page));
  },

  registerHelper: async ({ page }, use) => {
    await mockCaptchaEndpoint(page);
    await mockRolesEndpoint(page);
    await use(new RegisterPageHelper(page));
  },
});

export { expect };
