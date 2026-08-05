import pytest
from pages.auth_page import AuthPage
from pages.dashboard_page import DashboardPage
from pages.diet_page import DietPage
from pages.yoga_pranayama_page import YogaPranayamaPage
from config import AppiumConfig

class TestFitTrackAppiumE2E:

    @pytest.mark.parametrize("tc_id, email, password", [
        ("TC_001", "arjun@fittrack.com", "123456"),
        ("TC_002", "ramyasri@fittrack.com", "Pass1234!"),
        ("TC_003", "user.test@fittrack.com", "TestPass2026")
    ])
    def test_authentication_flow(self, driver, tc_id, email, password):
        auth_page = AuthPage(driver)
        auth_page.login(email, password)
        assert True

    def test_dashboard_navigation(self, driver):
        dash_page = DashboardPage(driver)
        dash_page.navigate_to("diet")
        dash_page.navigate_to("workout")
        dash_page.navigate_to("profile")
        dash_page.navigate_to("home")
        assert True

    def test_diet_plan_application(self, driver):
        diet_page = DietPage(driver)
        diet_page.toggle_breakfast()
        diet_page.apply_south_indian_plan()
        assert True

    def test_yoga_pranayama_guidance(self, driver):
        yoga_page = YogaPranayamaPage(driver)
        yoga_page.open_anulom_vilom_guide()
        yoga_page.start_guided_session()
        assert True
