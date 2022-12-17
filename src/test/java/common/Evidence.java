package common;

import java.io.File;

import lombok.Data;

/**
 * 証跡
 * @author DE-TEIU
 *
 */
@Data
public class Evidence {

	/**
	 * コンストラクタ
	 * @param fileName ファイル名
	 * @param comment コメント
	 */
	public Evidence(File file, String comment) {
		this.file = file;
		this.comment = comment;
	}

	/** スクリーンショットのファイル名 */
	private File file;
	/** ファイルに対応するコメント */
	private String comment;
}
