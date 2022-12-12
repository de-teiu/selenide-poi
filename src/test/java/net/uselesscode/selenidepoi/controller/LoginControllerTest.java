package net.uselesscode.selenidepoi.controller;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import common.Evidence;
import common.TestBase;

/**
 * ログイン機能テスト
 * @author DE-TEIU
 *
 */
class LoginControllerTest extends TestBase {
	
	private final String EVIDENCE_FILE_NAME = "ログイン";
	
	private final String USER_GENERAL_MAIL = "general@example.com";
	private final String USER_GENERAL_PASSWORD = "general";
	private final String USER_ADMIN_MAIL = "admin@example.com";
	private final String USER_ADMIN_PASSWORD = "admin";
	
	@BeforeAll
	static void beforeTest() throws IOException {
		setupClass("/loginTest");
	}

	/**
	 * 一般ユーザーでログインするテスト
	 */
	@Test
	void doLoginGeneral() {
		List<Evidence> evidences = new ArrayList<Evidence>();
		
		//ログイン画面を表示
		open("/");
		evidences.add(new Evidence(screenshot("init"), "ログイン画面を表示"));
		//ログイン情報を入力
		$("input[name=username]").setValue(USER_GENERAL_MAIL);
		$("input[name=password]").setValue(USER_GENERAL_PASSWORD);
		evidences.add(new Evidence($(".form-wrapper").screenshot().getName(), "ログイン情報を入力"));
		//ログイン実行
		$("form").submit();
		evidences.add(new Evidence(screenshot("login"), "一般ユーザーでログイン完了"));
		
		//一般ユーザーのマイページに遷移できているかテスト
		assertTrue($(".container").innerText().contains("一般ユーザー"));
		
		//スクリーンショットを貼り付けたExcelファイルを生成
		createExcelEvidence(evidences, EVIDENCE_FILE_NAME, "一般ユーザーログイン");
	}

}
