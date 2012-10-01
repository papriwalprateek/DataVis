package Assignment2.visualizations;

import java.io.*;
import java.util.*;

import java.io.File;
import java.util.*;
import java.io.IOException;

import prefuse.data.Node;
import prefuse.data.Table;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import prefuse.data.Table;
import jxl.read.biff.BiffException;

public class ReadGeoData
{
	/**
	 * Writes a kml file for use in Google Maps API
	 */
	public static void main(String args[])
	{
		String DATA_FILE = "data/INDIA.txt";
		try {
			FileOutputStream fos = new FileOutputStream("data/data.kml");
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8"); 
			
			// adding headers to file
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");			
			out.write("<kml xmlns=\"http://www.google.com/earth/kml/2\">\n");
			out.write("<Document>\n");
			out.write("\n");
			File inputWorkbook = new File("data/MPTrack-15.xls");
			Workbook w; 	    
		    w = Workbook.getWorkbook(inputWorkbook);
		    // Get the first sheet
		    Sheet sheet = w.getSheet(0); 
		    CityStruct a = null;
		    TreeMap<String,CityStruct> hm = new TreeMap<String,CityStruct>();
		    for (int j = 1; j < sheet.getRows(); j++) {
			          Cell cell = sheet.getCell(5,j);		      
			          if(cell.getContents()!=null && cell.getContents()!="")
			          {
			        	  	//get data for city from excel file 
			        	  	String city = cell.getContents().toUpperCase();
			    		    a = new CityStruct();
			    		    a.candidate = sheet.getCell(0,j).getContents();
			    		    a.party = sheet.getCell(6,j).getContents();
			    		    a.state = sheet.getCell(4,j).getContents();
			    		    a.gender = sheet.getCell(7,j).getContents();
			    		    a.age = sheet.getCell(10,j).getContents();
			    		    a.education = sheet.getCell(8,j).getContents();
			    		    a.attendance = sheet.getCell(24,j).getContents();
			        	  	hm.put(city,a);
			          }
			      //  }
			}
	    
			String str;
			File file = new File(DATA_FILE);	
			FileInputStream fin = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));	
			int count = 0;
			while ((str = br.readLine()) != null) 
			{
				str = str.trim();
				String[] temp = str.split("\\s+");				
				if(temp.length==6)
				{
					//extracting latitude longitude information and converting to float values
					String city = (temp[0]+" "+temp[1]).toUpperCase();
					temp[3] = temp[3].split("'")[0];
					float lat1 = Float.parseFloat(temp[3].split("o")[0]);
					float lat2 = Float.parseFloat(temp[3].split("o")[1]);
					float latitude = lat1+lat2/100;
					temp[5] = temp[5].split("'")[0];
					float lon1 = Float.parseFloat(temp[5].split("o")[0]);
					float lon2 = Float.parseFloat(temp[5].split("o")[1]);
					float longitude = lon1+lon2/100;												
					if(hm.containsKey(city))
					{
						CityStruct b = hm.get(city);
						//adding location in kml file in specified format
		    		    out.write("<Placemark>\n");
		    		    out.write("<name>"+city+"</name>\n");
		    		    out.write("<description><![CDATA[\nState: "+b.state+"<br />Candidate: "+b.candidate+"<br />Party: "+b.party+"<br />Gender: "+b.gender+"<br />Age: "+b.age+"<br />Educaition: "+b.education+"<br />Attendance: "+b.attendance+"<br />"+"]]></description>\n");
		    		    out.write("<Point>\n<coordinates>"+longitude+","+latitude+"</coordinates>\n</Point>\n</Placemark>\n");
		    			out.write("\n");
					}	
				}
				else if(temp.length==5)
				{	
					String city = temp[0];
					temp[2] = temp[2].split("'")[0];
					float lat1 = Float.parseFloat(temp[2].split("o")[0]);
					float lat2 = Float.parseFloat(temp[2].split("o")[1]);
					float latitude = lat1+lat2/100;
					temp[4] = temp[4].split("'")[0];
					float lon1 = Float.parseFloat(temp[4].split("o")[0]);
					float lon2 = Float.parseFloat(temp[4].split("o")[1]);
					float longitude = lon1+lon2/100;				
					if(hm.containsKey(city))
					{
						CityStruct b = hm.get(city);
		    		    out.write("<Placemark>\n");
		    		    out.write("<name>"+city+"</name>\n");
		    		    out.write("<description><![CDATA[\nState: "+b.state+"<br />Candidate: "+b.candidate+"<br />Party: "+b.party+"<br />Gender: "+b.gender+"<br />Age: "+b.age+"<br />Educaition: "+b.education+"<br />Attendance: "+b.attendance+"<br />"+"]]></description>\n");
	    		    out.write("<Point>\n<coordinates>"+longitude+","+latitude+"</coordinates>\n</Point>\n</Placemark>\n");
		    			out.write("\n");
					}	
				}	
		
			}
			out.write("</Document>\n");
			out.write("</kml>\n");
			out.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		};
	}		
}
