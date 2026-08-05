import os

class AppiumConfig:
    APPIUM_SERVER_URL = "http://127.0.0.1:4723"
    PLATFORM_NAME = "Android"
    AUTOMATION_NAME = "UiAutomator2"
    DEVICE_NAME = "Android_Emulator"
    APP_PACKAGE = "com.example.fittrack"
    APP_ACTIVITY = ".frontend.SplashActivity"
    IMPLICIT_WAIT_TIMEOUT = 10
    EXPLICIT_WAIT_TIMEOUT = 15

    # Test Data
    DEFAULT_USER_EMAIL = "arjun@fittrack.com"
    DEFAULT_USER_PASS = "123456"
    NEW_USER_NAME = "Ramya Sri Mangalagiri"
    NEW_USER_EMAIL = "ramyasri@fittrack.com"
    NEW_USER_PASS = "Pass1234!"
    
    # Reports
    REPORT_OUTPUT_DIR = os.path.dirname(os.path.abspath(__file__))
    EXCEL_REPORT_NAME = "FitTrack_E2E_Test_Report.xlsx"
