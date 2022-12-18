package net.uselesscode.selenidepoi.controller;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;

import common.Evidence;
import common.TestBase;

/**
 * ログイン機能テスト
 * @author DE-TEIU
 *
 */
class LoginControllerTest extends TestBase {

	/** 証跡Excelのファイル名 */
	private final static String EVIDENCE_FILE_NAME = "証跡_ログイン";

	/** 一般ユーザーメールアドレス */
	private final String USER_GENERAL_MAIL = "general@example.com";
	/** 一般ユーザーパスワード */
	private final String USER_GENERAL_PASSWORD = "general";
	/** 管理者ユーザーメールアドレス */
	private final String USER_ADMIN_MAIL = "admin@example.com";
	/** 管理者ユーザーパスワード */
	private final String USER_ADMIN_PASSWORD = "admin";

	/**
	 * テスト実行前処理
	 * @throws IOException 例外
	 */
	@BeforeAll
	static void beforeTest() throws IOException {
		setupClass("/loginTest");
	}

	@AfterAll
	static void afterTest() {
		removeTemplateSheetFromWorkbook(EVIDENCE_FILE_NAME);
	}

	/**
	 * 一般ユーザーでログインするテスト
	 * @throws Exception 例外
	 */
	@Test
	void doLoginGeneral() throws Exception {
		List<Evidence> evidences = new ArrayList<Evidence>();

		//ログイン画面を表示
		open("/");
		evidences.add(new Evidence(screenshot(OutputType.FILE), "ログイン画面を表示"));

		//ログイン情報を入力
		$("input[name=username]").setValue(USER_GENERAL_MAIL);
		$("input[name=password]").setValue(USER_GENERAL_PASSWORD);
		evidences.add(new Evidence($(".form-wrapper").screenshot(), "ログイン情報を入力"));
		//ログイン実行
		$("form").submit();
		evidences.add(new Evidence(screenshot(OutputType.FILE), "一般ユーザーでログイン完了"));

		//一般ユーザーのマイページに遷移できているかテスト
		assertTrue($(".container").innerText().contains("一般ユーザー"));

		//スクリーンショットを貼り付けたExcelファイルを生成
		createExcelEvidence(evidences, EVIDENCE_FILE_NAME, "1_一般ユーザーログイン");
	}

	/**
	 * 管理者権限ユーザーでログインするテスト
	 * @throws Exception 例外
	 */
	@Test
	void doLoginAdmin() throws Exception {
		List<Evidence> evidences = new ArrayList<Evidence>();

		//ログイン画面を表示
		open("/");
		evidences.add(new Evidence(screenshot(OutputType.FILE), "ログイン画面を表示"));

		//ログイン情報を入力
		$("input[name=username]").setValue(USER_ADMIN_MAIL);
		$("input[name=password]").setValue(USER_ADMIN_PASSWORD);
		evidences.add(new Evidence($(".form-wrapper").screenshot(), "ログイン情報を入力"));
		//ログイン実行
		$("form").submit();
		evidences.add(new Evidence(screenshot(OutputType.FILE), "管理者ユーザーでログイン完了"));

		//一般ユーザーのマイページに遷移できているかテスト
		assertTrue($(".container").innerText().contains("管理者ユーザー"));

		//スクリーンショットを貼り付けたExcelファイルを生成
		createExcelEvidence(evidences, EVIDENCE_FILE_NAME, "2_管理者ユーザーログイン");
	}

	/**
	 * ログイン失敗させるテスト
	 * @throws Exception 例外
	 */
	@Test
	void doLoginFailure() throws Exception {
		List<Evidence> evidences = new ArrayList<Evidence>();

		//ログイン画面を表示
		open("/");
		evidences.add(new Evidence(screenshot(OutputType.FILE), "ログイン画面を表示"));

		//ログイン情報を入力
		$("input[name=username]").setValue(USER_GENERAL_MAIL);
		$("input[name=password]").setValue("wrongpassword");
		evidences.add(new Evidence($(".form-wrapper").screenshot(), "ログイン情報を入力"));
		//ログイン実行
		$("form").submit();
		evidences.add(new Evidence(screenshot(OutputType.FILE), "ログイン失敗"));

		//一般ユーザーのマイページに遷移できているかテスト
		assertTrue($(".alert").innerText().contains("メールアドレスまたはパスワードに誤りがあります。"));

		//スクリーンショットを貼り付けたExcelファイルを生成
		createExcelEvidence(evidences, EVIDENCE_FILE_NAME, "3_ログイン失敗");
	}
}
