import java.io.File;
import java.util.*;
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
	    	  HashMap<String,Integer> hm = new HashMap<String,Integer>();		  	  
		      for (int j = 1; j < sheet.getRows(); j++) {
		   	  //  for (int i = 0; i < sheet.getColumns(); i++) {
		          Cell cell = sheet.getCell(4,j);		          
		          hm.put(cell.getContents(),-1);
		      //  }
		      }
		       Set set = hm.entrySet();
			   // Get an iterator
			   Iterator i = set.iterator();
			   int index=0;
			   // Display elements
			   while(i.hasNext()) {
				   Map.Entry me = (Map.Entry)i.next();
				   node = tree.addNode();
				   Node parent = tree.getNode(0);
				   Edge e = tree.addEdge(parent,node);
				   node.set("label", me.getKey());
				   hm.put(me.getKey().toString(),++index);
			   } 
			   for(int j = 1;j <sheet.getRows(); j++)
			   {
				   Cell state = sheet.getCell(4,j);
				   Cell district = sheet.getCell(5,j);
				   Cell name = sheet.getCell(0,j);
				   int state_index = hm.get(state.getContents());
				   Node district_node = tree.addNode();
				   Node state_node = tree.getNode(state_index);
				   Node politician = tree.addNode();
				   tree.addEdge(state_node, district_node);
				   tree.addEdge(district_node, politician);
				   district_node.set("label",district.getContents());
				   politician.set("label",name.getContents());				   
			   }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		    return tree;
	 }
}
