package common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

/**
 * 自動打鍵テストクラスの基底クラス
 * @author DE-TEIU
 *
 */
public class TestBase {

	/**
	 * テストメソッドが実行終了するたびに呼び出されるメソッド
	 */
	@AfterEach
	void finishTest() {
		Selenide.clearBrowserCookies();
	}

	/**
	 * テスト実行前の初期化処理
	 * @param directory ファイル出力先ディレクトリ
	 * @throws IOException 例外
	 */
	protected static void setupClass(String directory) throws IOException {
		Configuration.browser = "chrome";
		Configuration.baseUrl = "http://localhost:8080";
		Configuration.remote = null;
		Configuration.timeout = 1000L;
		Configuration.reportsFolder = "report" + directory;
		Configuration.reopenBrowserOnFail = true;
		Configuration.browserSize = "1366x768";

		cleanOutputDirectory();
	}

	/**
	 * テスト実行結果を出力するディレクトリを空にする
	 * @throws IOException 例外
	 */
	private static void cleanOutputDirectory() throws IOException {
		String path = getOutputDirectoryPath();
		File directory = new File(path);
		if (!directory.exists()) {
			return;
		}
		FileUtils.cleanDirectory(new File(path));
	}

	/**
	 * テスト実行結果出力先のディレクトリを取得
	 * @return ディレクトリパス
	 */
	private static String getOutputDirectoryPath() {
		StringBuilder sb = new StringBuilder(new File(".").getAbsoluteFile().getParent());
		sb.append(File.separator);
		sb.append(Configuration.reportsFolder);
		String path = sb.toString().replace("/", File.separator);

		return path;
	}

	/**
	 * 証跡をまとめたExcelファイルを作成する
	 * @param evidences 証跡
	 * @param fileName ファイル名
	 * @param sheetName シート名
	 * @throws Exception 例外
	 */
	protected void createExcelEvidence(List<Evidence> evidences, String fileName, String sheetName) throws Exception {
		String filePath = getOutputDirectoryPath() + File.separator + fileName + ".xlsx";
		ExcelEvidenceCreator.create(evidences, filePath, sheetName);
		//Excelファイルに出力済みの画像を削除
		evidences.forEach(evidence -> {
			evidence.getFile().delete();
		});
	}
}
