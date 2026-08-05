from appium.webdriver.common.appiumby import AppiumBy
from pages.base_page import BasePage

class DashboardPage(BasePage):
    GREETING_TEXT = (AppiumBy.ID, "com.example.fittrack:id/tv_greeting")
    FITNESS_SCORE_CARD = (AppiumBy.ID, "com.example.fittrack:id/card_fitness_score")
    DIET_INTAKE_CARD = (AppiumBy.ID, "com.example.fittrack:id/card_diet_intake")
    WATER_TRACKER_CARD = (AppiumBy.ID, "com.example.fittrack:id/card_water_tracker")
    WORKOUT_BANNER = (AppiumBy.ID, "com.example.fittrack:id/card_workout_banner")
    STRESS_MAPPER_WIDGET = (AppiumBy.ID, "com.example.fittrack:id/card_stress_widget")
    
    # Bottom Nav
    NAV_HOME = (AppiumBy.ID, "com.example.fittrack:id/nav_home")
    NAV_WORKOUT = (AppiumBy.ID, "com.example.fittrack:id/nav_workout")
    NAV_DIET = (AppiumBy.ID, "com.example.fittrack:id/nav_diet")
    NAV_PROFILE = (AppiumBy.ID, "com.example.fittrack:id/nav_profile")

    def navigate_to(self, nav_item):
        if nav_item == "workout": self.click(*self.NAV_WORKOUT)
        elif nav_item == "diet": self.click(*self.NAV_DIET)
        elif nav_item == "profile": self.click(*self.NAV_PROFILE)
        else: self.click(*self.NAV_HOME)
