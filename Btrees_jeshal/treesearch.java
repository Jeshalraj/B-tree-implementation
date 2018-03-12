import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class treesearch {
	
	public static void main(String[] args) throws NumberFormatException, IOException		// This is the main program which also comprises of parsing
	{
		BufferedReader br = new BufferedReader(new FileReader(args[0]));					// open the input file to read
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("output_file.txt"));			// output file to be written to
		int m = Integer.parseInt(br.readLine());											// parse the order of the tree
		BtreeNode root = Initialize(4);
		String line = "";
		float minValue = Integer.MAX_VALUE;
		float maxValue = Integer.MIN_VALUE;
		while((line=br.readLine())!=null)													// read the input line and check for the commands
		{
			
			if(line.contains("Insert"))														// if command is insert parse and execute Insert function and write the output back 
			{
				String[] line1 = line.split(",");
				//System.out.println(line1[0]);
				String line2 = line1[0].substring(7,line1[0].length());
				String key = line2;
				String value = line1[1].substring(0,line1[1].length()-1);
				//System.out.println("Key Value is = " + key + "Value is = " + value);
				Insert(root,key,value);
				float val1 = Float.parseFloat(key);
				float val2 = Float.parseFloat(key);
				if(val1 >= maxValue) maxValue = val1;
				if(val2 <= minValue) minValue = val2;
			}
			else if(line.contains("Search") && !line.contains(","))							// if the command is Search, parse and execute the Search and 2 keys and write the output back
			{
				String key = line.substring(7,line.length()-1);
				ArrayList<KeyValuePair> al = Search(root,key,key);
				if(al.size()==0) bw.write("Null");
				else
				{
					bw.write(al.get(0).value);
					for(int i=1;i<al.size();i++)
					{
						bw.write(",");
						bw.write(al.get(i).value);
					}
				}
				bw.newLine();
			}
			else if(line.contains("Search") && line.contains(","))							// if it is a single Search command and write the output back
			{
				String[] line1 = line.split(",");
				String key1 = line1[0].substring(7,line1[0].length());
				String key2 = line1[1].substring(0,line1[1].length()-1);
				ArrayList<KeyValuePair> al = Search(root,key1,key2);
				if(al.size()==0) bw.write("Null");
				else
				{
					bw.write( "(" + al.get(0).key + ","  + al.get(0).value + ")");
					for(int i=1;i<al.size();i++)
					{
						bw.write(",");
						bw.write( "(" + al.get(i).key + ","  + al.get(i).value + ")");
					}
				}
				bw.newLine();
			}
			
			
		}
		bw.close();
		//Search(root,"-500","500");
	}
	
	public static BtreeNode Initialize(int m)						// B+tree initialize function
	{
		return(new BtreeNode(m,null));
	}
	public static void Insert(BtreeNode root, String key1, String Value)		//B+tree Insert function
	{
		root.add(Float.parseFloat(key1),Value,root);	
	}
	
	public  static ArrayList<KeyValuePair> Search(BtreeNode root,String key1,String key2) 	//B+tree Search function
	{	
		return(root.findRange(root,Float.parseFloat(key1),Float.parseFloat(key2)));
	}
}
