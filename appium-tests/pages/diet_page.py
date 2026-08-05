from appium.webdriver.common.appiumby import AppiumBy
from pages.base_page import BasePage

class DietPage(BasePage):
    BREAKFAST_NAME = (AppiumBy.ID, "com.example.fittrack:id/tv_meal_breakfast_name")
    LUNCH_NAME = (AppiumBy.ID, "com.example.fittrack:id/tv_meal_lunch_name")
    DINNER_NAME = (AppiumBy.ID, "com.example.fittrack:id/tv_meal_dinner_name")
    SNACKS_NAME = (AppiumBy.ID, "com.example.fittrack:id/tv_meal_snacks_name")
    
    CHECK_BREAKFAST = (AppiumBy.ID, "com.example.fittrack:id/iv_check_breakfast")
    CHECK_LUNCH = (AppiumBy.ID, "com.example.fittrack:id/iv_check_lunch")
    BTN_ADD_DINNER = (AppiumBy.ID, "com.example.fittrack:id/btn_add_dinner")
    FAB_ADD_MEAL = (AppiumBy.ID, "com.example.fittrack:id/fab_add_meal")
    
    # Regional Diet
    OPTION_REGIONAL = (AppiumBy.ID, "com.example.fittrack:id/option_regional")
    BTN_SOUTH_INDIAN = (AppiumBy.ID, "com.example.fittrack:id/btn_view_south")
    BTN_NORTH_INDIAN = (AppiumBy.ID, "com.example.fittrack:id/btn_view_north")
    BTN_APPLY_PLAN = (AppiumBy.ID, "com.example.fittrack:id/btn_apply_plan")

    def toggle_breakfast(self):
        self.click(*self.CHECK_BREAKFAST)

    def apply_south_indian_plan(self):
        self.click(*self.FAB_ADD_MEAL)
        self.click(*self.OPTION_REGIONAL)
        self.click(*self.BTN_SOUTH_INDIAN)
        self.click(*self.BTN_APPLY_PLAN)
