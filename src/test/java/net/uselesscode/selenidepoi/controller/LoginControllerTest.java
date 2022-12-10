package net.uselesscode.selenidepoi.controller;

import static com.codeborne.selenide.Selenide.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;

import common.TestBase;

class LoginControllerTest extends TestBase {

	@BeforeEach
	void beforeTest() {
		setupClass();
		Configuration.reportsFolder = Configuration.reportsFolder + "/loginTest";
	}

	@Test
	void doLoginGeneral() {
		open("/");
		screenshot("init");
		$("input[name=username]").setValue("general@example.com");
		$("input[name=password]").setValue("general");
		$("form").submit();
		screenshot("login");
	}

}
