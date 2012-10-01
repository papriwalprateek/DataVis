package Assignment2.stats;
import java.io.*;
import java.util.*;

import jxl.*;
public class writeMatlab
{
	public static void main(String [] args)
	{
		try
		{
		String fileName ="data/MPTrack-15.xls";
		Sheet sheet = Workbook.getWorkbook(new File(fileName)).getSheet(0);
		ArrayList<Integer> Age=getColumn(1,sheet.getRows(),sheet,10);
		ArrayList<Integer> Attendance =getColumn(1,sheet.getRows(),sheet,14);
		ArrayList<Integer> OldMpsAttendance=new ArrayList<Integer>();
		ArrayList<Integer> YoungMpsAttendance=new ArrayList<Integer>();
		double m=mean(Age);
		for(int i=0;i<Age.size();i++)
		{
			if(Attendance.get(i) !=null)
			{
				if(Age.get(i)>=m)
					OldMpsAttendance.add(Attendance.get(i));
				else
					YoungMpsAttendance.add(Attendance.get(i));
			}
		}
		welchCode(OldMpsAttendance,YoungMpsAttendance,"matlab/AgeAttendance.m",true);
		String[] LS={"Rajasthan","Madhya Pradesh","Maharashtra","Andhra Pradesh","Uttar Pradesh","Jammu and Kashmir","Gujarat","Karnataka","Orissa","Chhattisgarh","Tamil Nadu","Bihar","West Bengal","Arunachal Pradesh","Jharkhand","Assam"};
		runWelch(LS,4,sheet,Attendance,true,"matlab/stateSizeAttendanceNew.m");
		String [] NDA1={"Bharatiya Janata Party","Janata Dal (United)","Shiv Sena","Shiromani Akali Dal","Telangana Rashtra Samithi","Asom Gana Parishad","Nagaland People's Front","Uttarakhand Kranti Dal","Janata Party","Jharkhand Mukti Morcha","All Jharkhand Students Union","Maharashtrawadi Gomantak Party","Haryana Janhit Congress"};
		String [] UPA1={"Indian National Congress","Nationalist Congress Party","Dravida Munnetra Kazhagam","Indian Union Muslim League","Jammu & Kashmir National Conference","All India Majlis-e-Ittehadul Muslimeen","Kerala Congress","Viduthalai Chiruthaigal Katchi","Rashtriya Lok Dal","Sikkim Democratic Front","All India United Democratic Front"};
		runWelch(UPA1,6,sheet,Attendance,false,"matlab/UPAvsOthersNew.m");
		runWelch(NDA1,6,sheet,Attendance,false,"matlab/NDAvsOthersNew.m");
		String[] south={"Andhra Pradesh","Karnataka","Kerala","Tamil Nadu","Lakshadweep","Pondicherry","Maharashtra"};
		runWelch(south,4,sheet,Attendance,true,"matlab/northVsSouthnew.m");
		String [] mE={"Post Graduate","Graduate","Doctorate"};
		runWelch(mE,8,sheet,Attendance,false,"matlab/moreEducatednew.m");
		ArrayList<Integer> EduLevelMale=new ArrayList<Integer>();
		ArrayList<Integer> EduLevelFemale=new ArrayList<Integer>();
		for(int i=1;i<sheet.getRows();i++)
		{
			String edu= sheet.getCell(8,i).getContents();
			int j=0;
			switch(edu)
			{
				case "Under Matric" : j=5; break;
				case "Matric" : j=10;break;
				case "Inter/ Higher Secondary" : j=12;break;
				case "Diploma Course" :j=13;break;
				case "Graduate" : j=16;break;
				case "Professional Graduate" : j=17; break;
				case "Post Graduate" : case "Doctorate": j = 20;break;
				default :j=0;
			}
			if(sheet.getCell(7,i).getContents().equals("Male"))
				EduLevelMale.add(j);
			else
				EduLevelFemale.add(j);
		}
		welchCode(EduLevelMale,EduLevelFemale,"matlab/GenderEduCorelation.m",true);
		}
		catch(Exception e)
		{
			System.out.println(e.toString()+" oops2");
		}
	}
/**
 * Returns an ArrayList of Integer from Column no. column of Sheet s1 from row no. r1 till r2. stores null in case of a NumberFormatException
 * @param r1 starting row no. of data
 * @param r2 ending row no. of data
 * @param s1 sheet in which data is present
 * @param column COLUMN no. in the sheet
 * @return ArrayList<Integer>
 */
	static ArrayList<Integer> getColumn(int r1,int r2,Sheet s1,int column)
	{
		ArrayList<Integer> A= new ArrayList<Integer>();
		Cell c;
		for(int i=r1;i<r2;i++)
		{
			c=s1.getCell(column,i);
			try
			{
				String s=c.getContents();
				String snew=s.replace("%"," ").trim();
				Integer a=Integer.parseInt(snew);
				A.add(a);
			}
			catch(NumberFormatException nfe1)
			{
				A.add(null);
			}
		}
		return A;
	}
/**
 * Writes a matlab code for doing welch t-test in a file named fileName (.m format)
 * A1 and A2 contain values which are data for two sets of populations
 * The null hypothesis of test is that mean of population 1 = mean of population 2
 * Alternative hypothesis is mean of population 1 < mean of population 2 if b=true
 * Alternative hypothesis is mean of population 1 != mean of population 2 if b=false
 * @param A1 ArrayList<Integer> data of population 1
 * @param A2 ArrayList<Integer> data of population 2
 * @param fileName String filename for the matlab code(relative addres in workspace)
 * @param b boolean whether left tailed welch test is to be performed or both sided 
 */
	static void welchCode(ArrayList<Integer> A1,ArrayList<Integer> A2,String fileName,boolean b)
	{
		try{
		PrintWriter Pr=new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
		StringBuilder s=new StringBuilder("x = ");
		s.append(AtoString(A1));
		s.append(";\ny= ");
		s.append(AtoString(A2));
		if(b)
			s.append(";\n[h,k]=ttest2(x,y,0.05,'left','unequal')");
		else
			s.append(";\n[h,k]=ttest2(x,y)");
		Pr.print(s.toString());
		Pr.close();
		}
		catch(IOException ioe1)
		{
			System.out.println(ioe1.getMessage()+"oops1");
		}
	}
/**
 * returns a String with elements of an integer arraylist seperated by space.
 * @param A ArrayList of Integers
 * @return String version of A with elements separated by space
 */
	static String AtoString(ArrayList<Integer> A)
	{
		StringBuilder s=new StringBuilder("[");
		for(int i=0;i<A.size();i++)
		{
			s.append(" ");
			s.append(A.get(i));
		}
		s.append("]");
		return s.toString().replace("null","NaN");
	}
/**
 * calculates mean of integers in an arraylist
 * @param A arraylist of integers whose mean is to be calculated
 * @return double format mean of all integers in A
 */
	public static double mean(ArrayList<Integer> A)
	{
		int count=0,sum=0;
		for(int i=0;i<A.size();i++)
		{
			if(A.get(i) != null)
			{
				sum=sum+A.get(i);
				count++;
			}
		}
		return 1.0*sum/count;
	}
	/**
	 * runs welchCode with two populations as population 1 belonging to S and population 2 not belonging to S.
	 * the mean of corresponding values stored in X are to be compared.
	 * S and X are mapped to each other through Sheet sheet's column no. column ,keeping in mind that X[i-1] corresponds to element(i) of column.
	 * boolean value b specifies if the alternative hypothesis is mean(population S)< mean (population non-S)
	 * or if b is false then mean(population S)> mean (population non-S)
	 * @param S an string array classifying population as population 1 and population 2
	 * @param column column index in sheet corresponding to classifying property in S
	 * @param X a arraylist of integers whose mean is under test
	 * @param b boolean value b specifies if the alternative hypothesis is mean(population S)< mean (population non-S) for true and > for false
	 * @param filename desired matlab code file name
	 * */
	public static void runWelch(String[] S,int column,Sheet sheet,ArrayList<Integer> X,boolean b,String fileName )
	{
		ArrayList<String> S1= new ArrayList<String>(Arrays.asList(S));
		ArrayList<Integer> nonSX=new ArrayList<Integer>();
		ArrayList<Integer> SX=new ArrayList<Integer>();
		for(int i=1;i<sheet.getRows();i++)
		{
			if(S1.contains(sheet.getCell(column,i).getContents()))
				SX.add(X.get(i-1));
			else
				nonSX.add(X.get(i-1));				
		}
		if(b)
			welchCode(SX,nonSX,fileName,true);
		else
			welchCode(nonSX,SX,fileName,true);
	}
}
