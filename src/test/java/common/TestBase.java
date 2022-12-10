package common;

import com.codeborne.selenide.Configuration;

public class TestBase {

	protected void setupClass() {
		Configuration.browser = "chrome";
		Configuration.baseUrl = "http://localhost:8080";
		Configuration.remote = null;
		Configuration.timeout = 1000L;
		Configuration.reportsFolder = "report";
		Configuration.reopenBrowserOnFail = true;
	}
}
