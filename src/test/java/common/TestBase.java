package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.codeborne.selenide.Configuration;

public class TestBase {
	
	
	protected static void setupClass(String directory) throws IOException {
		Configuration.browser = "chrome";
		Configuration.baseUrl = "http://localhost:8080";
		Configuration.remote = null;
		Configuration.timeout = 1000L;
		Configuration.reportsFolder = "report" + directory;
		Configuration.reopenBrowserOnFail = true;
			
		cleanOutputDirectory();
	}
	
	private static void cleanOutputDirectory() throws IOException {
        String path = getOutputDirectoryPath();
		FileUtils.cleanDirectory(new File(path));
	}
	
	private static String getOutputDirectoryPath() {
		StringBuilder sb = new StringBuilder(new File(".").getAbsoluteFile().getParent());
		sb.append(File.separator);
		sb.append(Configuration.reportsFolder);
		String path = sb.toString().replace("/", File.separator);
		
		return path;
	}
	
	
	protected void createExcelEvidence(List<Evidence> evidences,String fileName,String sheetName) {
		String filePath = getOutputDirectoryPath() + File.separator + fileName + ".xlsx";
		Workbook workbook = null;
		File file = new File(filePath);
		if(file.exists()) {
			try {
				workbook = new XSSFWorkbook(file);
			} catch (InvalidFormatException | IOException e) {
				e.printStackTrace();
			}
		}else {
			workbook = new XSSFWorkbook();
		}
		
		Sheet sh = workbook.createSheet(sheetName);
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
			workbook.write(fileOutputStream);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
