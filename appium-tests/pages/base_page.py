from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from config import AppiumConfig

class BasePage:
    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, AppiumConfig.EXPLICIT_WAIT_TIMEOUT) if driver else None

    def find(self, by, locator):
        if not self.driver: return None
        return self.wait.until(EC.presence_of_element_located((by, locator)))

    def click(self, by, locator):
        element = self.find(by, locator)
        if element: element.click()

    def type_text(self, by, locator, text):
        element = self.find(by, locator)
        if element:
            element.clear()
            element.send_keys(text)

    def get_text(self, by, locator):
        element = self.find(by, locator)
        return element.text if element else ""

    def is_displayed(self, by, locator):
        try:
            element = self.find(by, locator)
            return element.is_displayed() if element else False
        except Exception:
            return False
