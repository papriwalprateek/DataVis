import java.io.File;
import java.util.*;
import java.io.IOException;

import prefuse.data.Tree;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Table;
import jxl.Cell;
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
	        nodeData.addColumn("gender", String.class);
	        nodeData.addColumn("type", String.class);
	        nodeData.addColumn("education", String.class);
	        nodeData.addColumn("age", String.class);	        
	        nodeData.addColumn("attendance", String.class);
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
			  node.set("type", "country");			  	  
	    	  TreeMap<String,Integer> hm = new TreeMap<String,Integer>();	
	    	  TreeMap<String,TreeMap<String,Integer>> parties= new TreeMap<String,TreeMap<String,Integer>>();
		      for (int j = 1; j < sheet.getRows(); j++) {
		   	  //  for (int i = 0; i < sheet.getColumns(); i++) {
		          Cell cell = sheet.getCell(4,j);		      
		          if(cell.getContents()!=null && cell.getContents()!="")
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
				   node.set("type", "state");					   
				   hm.put(me.getKey().toString(),++index);
				   TreeMap<String,Integer> state_parties = new TreeMap<String,Integer>();
				   for (int j = 1; j < sheet.getRows(); j++) {
					   	if(sheet.getCell(4, j).getContents().equals(me.getKey()))
					   	{
					   		state_parties.put(sheet.getCell(6,j).getContents(),-1);
					   	}					   					  
				   }
				   parties.put(me.getKey().toString(), state_parties);
				   Set set1 = state_parties.entrySet();
				   // Get an iterator
				   Iterator i1 = set1.iterator();
				   int index1=0;
				   // Display elements
				   while(i1.hasNext()) {
					   Map.Entry me1 = (Map.Entry)i1.next();
					   Node party = tree.addNode();
					   state_parties.put(me1.getKey().toString(),++index);
					   tree.addEdge(node, party);
					   party.set("label", me1.getKey());
					   party.set("type", "party");					   
				   }
			   } 
			   for(int j = 1;j<sheet.getRows(); j++)
			   {
				   Cell state = sheet.getCell(4,j);
				   Cell district = sheet.getCell(5,j);
				   Cell party = sheet.getCell(6,j);
				   Cell name = sheet.getCell(0,j);
				   if(!state.getContents().equals(""))
				   {
					   int state_index = hm.get(state.getContents());
					   TreeMap parties_in_state = (TreeMap) parties.get(state.getContents());
					   int partyindex = (Integer) parties_in_state.get(party.getContents());
					   Node current_party = tree.getNode(partyindex);
					   Node district_node = tree.addNode();
					   Node politician = tree.addNode();
					   tree.addEdge(current_party, district_node);
					   tree.addEdge(district_node, politician);
					   district_node.set("label",district.getContents());
					   district_node.set("type","district");					   
					   politician.set("label",name.getContents());
					   politician.set("type","politician");
					   politician.set("gender",sheet.getCell(7,j).getContents());
					   politician.set("education",sheet.getCell(8,j).getContents());
					   politician.set("age",sheet.getCell(10,j).getContents());	
					   politician.set("attendance",sheet.getCell(14,j).getContents());					   
				   }
			   }
		    } catch (BiffException e) {
		      e.printStackTrace();
		    }
		    return tree;
	 }
}
