import { test as base, expect, type Page } from '@playwright/test';
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

  async fillAndSubmit(username: string, password: string, captchaValue = 'XXXXX') {
    await this.fillForm(username, password, captchaValue);
    await this.submit();
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
