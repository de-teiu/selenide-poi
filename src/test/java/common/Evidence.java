package common;

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
	public Evidence(String fileName, String comment) {
		this.fileName = fileName;
		this.comment = comment;
	}
	
	/** スクリーンショットのファイル名 */
	private String fileName;
	/** ファイルに対応するコメント */
	private String comment;
}
