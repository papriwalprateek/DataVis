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
		      // Loop over first 10 column and lines
		      for (int j = 0; j < sheet.getColumns(); j++) {
		        for (int i = 0; i < sheet.getRows(); i++) {
		          Cell cell = sheet.getCell(j, i);
		          CellType type = cell.getType();
		          if (cell.getType() == CellType.LABEL) {
		            System.out.println("I got a label "
		                + cell.getContents());
		          }
		          if (cell.getType() == CellType.NUMBER) {
		            System.out.println("I got a number "
		                + cell.getContents());
		          }
		        }
		      }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		  }
}
