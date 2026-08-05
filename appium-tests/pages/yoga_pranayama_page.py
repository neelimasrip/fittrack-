from appium.webdriver.common.appiumby import AppiumBy
from pages.base_page import BasePage

class YogaPranayamaPage(BasePage):
    CARD_STRESS_RELIEF = (AppiumBy.ID, "com.example.fittrack:id/card_stress_relief")
    CARD_MORNING_YOGA = (AppiumBy.ID, "com.example.fittrack:id/card_morning_yoga")
    CARD_ANULOM_VILOM = (AppiumBy.ID, "com.example.fittrack:id/card_anulom_vilom")
    CARD_KAPALBHATI = (AppiumBy.ID, "com.example.fittrack:id/card_kapalbhati")
    TV_RECOMMENDATION = (AppiumBy.ID, "com.example.fittrack:id/tv_recommendation")
    
    # Guide Dialog
    TV_GUIDE_TITLE = (AppiumBy.ID, "com.example.fittrack:id/tv_guide_title")
    TV_GUIDE_BENEFITS = (AppiumBy.ID, "com.example.fittrack:id/tv_guide_benefits")
    TV_GUIDE_STEPS = (AppiumBy.ID, "com.example.fittrack:id/tv_guide_steps")
    BTN_START_GUIDED = (AppiumBy.ID, "com.example.fittrack:id/btn_start_guided_session")

    def open_anulom_vilom_guide(self):
        self.click(*self.CARD_ANULOM_VILOM)

    def start_guided_session(self):
        self.click(*self.BTN_START_GUIDED)
