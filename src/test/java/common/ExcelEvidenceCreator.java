package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 画面のスクリーンショットを貼ってコメントを付けたExcelファイルを作成する
 * @author DE-TEIU
 *
 */
public class ExcelEvidenceCreator {

	/** 出力開始行 */
	private static int START_ROW = 1;
	/** 出力開始列 */
	private static int START_COLUMN = 1;
	/** スクリーンショット間の余白行数 */
	private static int EVIDENCE_ROW_SPACE = 3;
	/** テンプレートのセルのサイズ */
	private static int CELL_SIZE = 25;

	/**
	 * Excelファイル作成
	 * @param evidences 証跡リスト
	 * @param filePath 出力するExcelファイルのパス
	 * @param sheetName シート名
	 * @throws Exception 例外
	 */
	public static void create(List<Evidence> evidences, String filePath, String sheetName) throws Exception {
		//ワークブック作成
		File file = new File(filePath);
		Workbook workbook = initializeWorkbook(file);

		//テンプレートシートをコピー
		Sheet sheet = workbook.cloneSheet(0);
		workbook.setSheetName(workbook.getSheetIndex(sheet), sheetName);

		//スクリーンショットとコメントを出力
		int currentRow = START_ROW;
		for (int i = 0; i < evidences.size(); i++) {
			Evidence evidence = evidences.get(i);
			putValueToCell(sheet, currentRow, START_COLUMN, evidence.getComment());
			currentRow++;
			try {
				currentRow = putImageToCell(sheet, currentRow, START_COLUMN, evidence.getFile()) + EVIDENCE_ROW_SPACE;
			} catch (Exception e) {
				throw e;
			}
		}

		//templateシートを削除
		int templateSheetIndex = workbook.getSheetIndex("template");
		if (templateSheetIndex >= 0) {
			workbook.removeSheetAt(templateSheetIndex);
		}

		//ワークブックをファイルに出力
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			workbook.write(fileOutputStream);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * ワークブックを初期化
	 * @param file 読み込むExcelファイル
	 * @return ワークブック
	 * @throws Exception 例外
	 */
	private static Workbook initializeWorkbook(File file) throws Exception {
		Workbook workbook = null;
		try {
			//Excelファイルの読み込みを行う
			//ファイルが存在しなかったらテンプレートから作成
			if (file.exists()) {
				workbook = new XSSFWorkbook(new FileInputStream(file));
			} else {
				File template = new File("src/test/resources/template.xlsx");
				workbook = WorkbookFactory.create(new FileInputStream(template));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Excelファイルの読み込みに失敗しました。");
		}
		return workbook;
	}

	/**
	 * 指定したセルに文字列を出力
	 * @param sheet シート
	 * @param row 行番号
	 * @param col 列番号
	 * @param value 出力する値
	 */
	private static void putValueToCell(Sheet sheet, int row, int col, String value) {
		Row targetRow = sheet.getRow(row);
		if (targetRow == null) {
			targetRow = sheet.createRow(row);
		}
		Cell targetCell = targetRow.getCell(col);
		if (targetCell == null) {
			targetCell = targetRow.createCell(col);
		}
		targetCell.setCellValue(value);
	}

	/**
	 * 指定したセルに画像を出力
	 * @param sheet シート
	 * @param row 行番号
	 * @param col 列番号
	 * @param imageFile 画像ファイル
	 * @return 画像の下側の位置に対応する行番号
	 * @throws Exception 例外
	 */
	private static int putImageToCell(Sheet sheet, int row, int col, File imageFile) throws Exception {
		// 画像ファイル読み込み
		InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(imageFile));
		byte[] imageBytes = is.readAllBytes();
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
		is.close();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		ClientAnchor anchor = sheet.getWorkbook().getCreationHelper().createClientAnchor();
		//画像挿入位置・サイズを指定 
		anchor.setCol1(col);
		anchor.setRow1(row);
		int endCol = col + imageWidth / CELL_SIZE;
		int endRow = row + imageHeight / CELL_SIZE;
		anchor.setCol2(endCol);
		anchor.setRow2(endRow);
		//画像のアンカータイプを設定
		anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
		//画像を出力
		int index = sheet.getWorkbook().addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
		Picture picture = sheet.createDrawingPatriarch().createPicture(anchor, index);
		picture.setLineStyleColor(0, 0, 0);

		return endRow;
	}
}
