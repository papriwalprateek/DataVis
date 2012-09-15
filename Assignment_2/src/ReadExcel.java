import java.io.File;
import java.io.IOException;

import prefuse.data.Graph;
import prefuse.data.Tree;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Table;
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
	public Tree read() throws IOException  {
	        Table nodeData = new Table();
	        Table edgeData = new Table();
	        nodeData.addColumn("label", String.class);
	        edgeData.addColumn(Tree.DEFAULT_SOURCE_KEY, int.class);
	        edgeData.addColumn(Tree.DEFAULT_TARGET_KEY, int.class);
	  	    Tree tree = new Tree(nodeData,edgeData);			
		    File inputWorkbook = new File(DATA_FILE);
		    Workbook w;
		    try {
		      Node node = null;
		      w = Workbook.getWorkbook(inputWorkbook);
		      // Get the first sheet
		      Sheet sheet = w.getSheet(0);  
		  	  node = tree.addNode();
		  	  node.set("label","India");		  
		      for (int j = 1; j < sheet.getRows(); j++) {
		      //  for (int i = 0; i < sheet.getColumns(); i++) {
		          Cell cell = sheet.getCell(4,j);
		          CellType type = cell.getType();
		          System.out.println(cell.getContents());
		      //  }
		      }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		    return tree;
	 }
}
