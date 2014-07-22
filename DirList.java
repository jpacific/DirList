import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class  DirList
{
  
   
  public static void main(String[] args)
  {
  	Scanner console = new Scanner(System.in);
  	System.out.print("Enter the top directory name: ");
  	String top = console.nextLine();
  	String outFile = top;
  	System.out.print("Enter the threshold for percent matched lines to report: ");
  	double percent = console.nextDouble()/100;
  	System.out.println();
    File dir = new File(top);
    String[] chld = dir.list();
	ArrayList <Prog> progs = new  ArrayList<Prog>();
	boolean done = false;
    FileHelp f; 
   
   	  // Directory SEARCH:
      for(int i = 0; i < chld.length; i++)
      { 
      	String folderName = chld[i];  
      	File dirIn = new File(dir,folderName); 
      	if (dirIn.isDirectory())
    	{
    		String[] chldIn = dirIn.list();
    		for(int j = 0; j < chldIn.length; j++)
      		{ 
      			String fileNameIn = chldIn[j];
      			if(fileNameIn.contains(".java"))
      				progs.add(new Prog(folderName,fileNameIn));
      		}
    	}

      }
      // ENDS DIRECTORY SEARCH
      
      // gets each file
      for(int i = 0; i < progs.size(); i++)
      {
      	String p = top+"/"+ progs.get(i).name+"/"+ progs.get(i).fileName ; 
       	File temp = new File(progs.get(i).name,progs.get(i).fileName); 
      	f = new FileHelp(p,progs.get(i));       
      	f.read();  // load file in to array
      }
     
      Prog curr = new Prog("dummy", "dummy");
      Prog other = new Prog("dummy", "dummy");;
      double numMatches = 0;
     
      // all programs
      int p = 0;int n = 0;
      while(n < progs.size()-1)
      {
      	curr = progs.get(n);
       	Prog next = progs.get(n+1);
      	p=n;
      	//get out of current folder      	
      	if(curr.name.equals(next.name))
      	{  		
      		while(p <  progs.size()-1 && curr.name.equals(next.name))
      		{
      			p++;
				next = progs.get(p);      		
      		}
			
      	 }
      	 else
      	 	p++;
      	if (p == progs.size()-1)
      		done = true;
      	for(int k = p; !done && k< progs.size(); k++)
      	{  
      		next = progs.get(k);
      		for(int j = 0 ; j < curr.contents.size() && 
      				    	j < next.contents.size();j++)
      		{
      			if(curr.contents.get(j).equals(next.contents.get(j)))
      			{      				
      				numMatches ++;       				
      			}	
      			
      		}
      		if(numMatches > 0)
      		{
      		
      			Match temp = new Match(curr, next);  
      			temp.setMatches(numMatches);    				
      			curr.match.add(temp);  
      			numMatches = 0;	
      		}
      	 
      		
      	}
      	n++;
      }
      // report matches
      write(progs,outFile,percent);
      /*int m = 0;
      for(int j = 0; j < progs.size(); j++)
      {
      	curr = progs.get(j);
     
      	for(int i = 0; i < curr.match.size(); i++)
      	{  
      		Match temp = curr.match.get(i);      
      		temp.percent = temp.matchedLines / temp.numLines; 
      		if(temp.percent > percent)
      		{
      			if(m == 0 )
      				System.out.println("--- Matches found  in folder "+ curr.name +"  for Program "+ curr.fileName  + "--");
       
      			m++;
      			System.out.println(m + ".\tMatched code in folder = " + temp.program.name +  ", file = " + temp.program.fileName + 
      	                     "\n\t" + (temp.percent*100) + " %");     		
      
      			System.out.println();
      			System.out.println("\t\tOrig prog has " + (int)temp.numLines + " total lines");
      			System.out.println("\t\tMatched prog has " + temp.program.contents.size() + " total lines");
      			System.out.println();
      			System.out.println();
      		}
      	}
      	m=0;
      }*/
   }
   public static void write(ArrayList<Prog> progs, String file, double p)
	{
		try
		{
		
		// write data
		String f = file+".txt";
		File outFile = new File(f);
		PrintWriter outPrinter = new PrintWriter(outFile);
	 	outPrinter.println("Reporting matches for Subfolders of Directory " + file);
	 	outPrinter.println("Threshold for Percent of Matched Lines = " + Math.round(p*100) + "%");
      	outPrinter.println();
      	outPrinter.println();
      	int m = 0;
      	for(int j = 0; j < progs.size(); j++)
      	{
      		Prog curr = progs.get(j);
     
      		for(int i = 0; i < curr.match.size(); i++)
      		{  
      			Match temp = curr.match.get(i);      
      			temp.percent = temp.matchedLines / temp.numLines; 
      			if(temp.percent > p)
      			{
      				if(m == 0 )
      				{
      					System.out.println("--- Matches found  in folder "+ curr.name +"  for Program "+ curr.fileName  + "--");
      				
      					outPrinter.println("--- Matches found  in folder "+ curr.name +"  for Program "+ curr.fileName  + "--");
       				}
      				m++;
      				//write to file
      				outPrinter.println(m + ".\tMatched code in folder = " + temp.program.name +  ", file = " + temp.program.fileName + 
      	                     "\n\t" + (temp.percent*100) + " %");      
      				outPrinter.println();
      				outPrinter.println("\t\tOrig prog has " + (int)temp.numLines + " total lines");
      				outPrinter.println("\t\tMatched prog has " + temp.program.contents.size() + " total lines");
      				outPrinter.println();
      				outPrinter.println();
      				//write to console
      				System.out.println(m + ".\tMatched code in folder = " + temp.program.name +  ", file = " + temp.program.fileName + 
      	                     "\n\t" + (temp.percent*100) + " %"); 
      	                  				outPrinter.println();
      				System.out.println("\t\tOrig prog has " + (int)temp.numLines + " total lines");
      				System.out.println("\t\tMatched prog has " + temp.program.contents.size() + " total lines");
      				System.out.println();
      				System.out.println();   
      			}
      		}
      		m=0;
      	}
		outPrinter.close();
		} 
		catch (Exception e)
		{
			System.err.println("File output error");
		}
	}
}
class Prog
{
	public String name;  //folder name
	public String fileName; //program name
	public ArrayList <String> contents = new ArrayList <String>();
	public ArrayList <Match> match = new ArrayList <Match>(); //info about matches
	public Prog(String n, String y)
	{
		name  = n;
		fileName = y;
		 
	}
	public void addLines(String l)
	{
		contents.add(l);
	}
	public String toString()
	{
		return "folder-"+name+","+" name -" + fileName;
	}
	
}
class Match
{
	public double numLines;
	public double matchedLines;
	public double percent;
	public Prog program;
	
	public Match(Prog p, Prog prog)
	{
		numLines = p.contents.size();
		matchedLines = 0;
		percent = 0;
		program = prog;
	}
	public void setMatches(double m)
	{
		matchedLines  = m;
	}
	public String toString()
	{
		return ("Program = " + program + " matched lines = " + matchedLines);
	}
}
class FileHelp
{
	public String path;
	public Prog prog;
	
	public FileHelp(String p, Prog pr)
	{
		path = p;
		prog = pr;
		
		
	}
	public void set(String p)
	{
		path = p;
	}
	
	public void read()
	{
        	try
			{
				
				 Scanner in=new Scanner(new File(path));
        
           		 while (in.hasNextLine()) {
           		 	// HERE
                	VariableEditor VarEdit = new VariableEditor();
                	
					String line = (in.nextLine()).trim(); 
                	if(line.length()==0)
                	{
                		//skip- blank line
                	}
                	else
                	if(line.length()>=2 &&(line.substring(0,2)).equals("//") )
                	{
                			//skipping comments
                	 
                	}
                	else 
                	if(line.length()>=2 &&(line.substring(0,2)).equals("/*"))
                	{
                		int end = -1;
                		while (end < 0 && in.hasNextLine())
                		{
                			end = line.indexOf("*/");
                			if(end < 0 && in.hasNextLine())
                				line = in.nextLine();
                		}
               
                	}
                	else {
                	 	// Get and modify the line.
                	 	VarEdit.setLine(line);	// Get
                	 	VarEdit.modifyLine();	// Modify
                	 	
                	 	// Finally, add the line to the list.
                		prog.addLines(VarEdit.getLine());
                		System.out.println(VarEdit.getLine());
                	}
				}

				in.close();
			} 
			catch (Exception e)
			{
				System.err.println("File input error");
			}
    }    

}

class VariableEditor {
	// Variables
	public int varCount;	// Current count of variables in current file.
	public String line;		// Line recieved from code.
	public String modLine;	// Modified line.
	
	// Datatype Enumeration
	public enum DATATYPE {
		INT(3), DOUBLE(6), CHAR(4), STRING(6), BOOLEAN(7);
		private int value;
		
		private DATATYPE(int value) {
			this.value = value;
		}
	}
	
	// Constructor
	public VariableEditor() {
		varCount = 0;	// Set the count to 0.
		line = "";		// Set the line to a null string.
	}
	
	// Methods:
	// Sets the VariableEditor's line.
	public void setLine(String line) {	
		this.line = line;
	}
	
	// Modified the code in the line.
	public void modifyLine() {
		int indexOfVar = -1;	// index of found data type
		DATATYPE type;			// Type of datatype found
		
		// We do this to find where to begin searching for variables that are being created.
		if(line.indexOf("int") != -1) {
			type = DATATYPE.INT;
			modLine = line.substring(0,3) + " " + "VARIABLE" + varCount + line.substring(line.indexOf("="));
			System.out.println("MODDED: " + modLine);
			++varCount;
		}
		else if(line.indexOf("double") != -1) {
			type = DATATYPE.DOUBLE;
		}
		else if(line.indexOf("char") != -1) {
			type = DATATYPE.CHAR;
		}
		else if(line.indexOf("String") != -1) {
			type = DATATYPE.STRING;
		}
		else if(line.indexOf("boolean") != -1) {
			type = DATATYPE.BOOLEAN;
		}
		// Search for variables that are being used
		else {
			// If we're inside here, the variable isn't being created; it's being used.
			indexOfVar = line.indexOf("=");
			
			// If the line contains the '=' operator, but not the '=='...
			if((indexOfVar != -1) && (line.indexOf("==") == -1))
				modLine = "VARIABLE DETECTED: " + varCount + " " + line.substring(indexOfVar);
		}
	}
	
	// Returns the line.
	public String getLine() {
		return modLine;
	}
}
