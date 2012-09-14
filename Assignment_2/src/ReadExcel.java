import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {
	private String DATA_FILE;
	public void setInputFile(String file_name) {
	    this.DATA_FILE = file_name;	    
	}
	public void read() throws IOException  {
		    File inputWorkbook = new File(DATA_FILE);
		    Workbook w;
		    try {
		      w = Workbook.getWorkbook(inputWorkbook);
		      // Get the first sheet
		      Sheet sheet = w.getSheet(0);      
		      for (int j = 0; j < sheet.getRows(); j++) {
		        for (int i = 0; i < sheet.getColumns(); i++) {
		          Cell cell = sheet.getCell(i,j);
		          CellType type = cell.getType();
		          System.out.println(cell.getContents());
		        }
		      }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
	 }
}
