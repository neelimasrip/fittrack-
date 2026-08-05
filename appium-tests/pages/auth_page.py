from appium.webdriver.common.appiumby import AppiumBy
from pages.base_page import BasePage

class AuthPage(BasePage):
    # Locators
    EMAIL_INPUT = (AppiumBy.ID, "com.example.fittrack:id/et_email")
    PASSWORD_INPUT = (AppiumBy.ID, "com.example.fittrack:id/et_password")
    LOGIN_BTN = (AppiumBy.ID, "com.example.fittrack:id/btn_login")
    GOOGLE_BTN = (AppiumBy.ID, "com.example.fittrack:id/btn_google")
    SIGNUP_LINK = (AppiumBy.ID, "com.example.fittrack:id/tv_signup")
    FORGOT_PASS_LINK = (AppiumBy.ID, "com.example.fittrack:id/tv_forgot_password")
    
    # SignUp Locators
    FULL_NAME_INPUT = (AppiumBy.ID, "com.example.fittrack:id/et_name")
    CONFIRM_PASSWORD_INPUT = (AppiumBy.ID, "com.example.fittrack:id/et_confirm_password")
    CREATE_ACCOUNT_BTN = (AppiumBy.ID, "com.example.fittrack:id/btn_signup")

    def login(self, email, password):
        self.type_text(*self.EMAIL_INPUT, email)
        self.type_text(*self.PASSWORD_INPUT, password)
        self.click(*self.LOGIN_BTN)

    def login_with_google(self):
        self.click(*self.GOOGLE_BTN)

    def trigger_forgot_password(self, email):
        self.type_text(*self.EMAIL_INPUT, email)
        self.click(*self.FORGOT_PASS_LINK)
