export const LOGIN = {
  usernameInput: 'input[placeholder="Account"]',
  passwordInput: 'input.password-input',
  captchaInput:  'input[placeholder="輸入驗證碼"]',
  captchaImage:  'img[alt="captcha"]',
  submitButton:  'button[type="submit"]',
} as const;

export const REGISTER = {
  usernameInput:        'input[placeholder="Account"]',
  passwordInput:        'input[placeholder="Password"]',
  confirmPasswordInput: 'input[placeholder="Confirm Password"]',
  emailInput:           'input[placeholder="Email"]',
  roleSelect:           'select',
  captchaInput:         'input[placeholder="輸入驗證碼"]',
  captchaImage:         'img[alt="captcha"]',
  submitButton:         'button[type="submit"]',
} as const;
