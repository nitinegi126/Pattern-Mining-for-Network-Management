package nitin.codebind;

import java.io.*;
import java.util.*;
public class ReadCSV 
{    
//	static int numOfEdges = 7;
	static Algo constraint = new Algo();
	public static void main(String fileLocation) throws IOException {
		try {
			int l=0;		// for extracting edge number from string
			for (int i=0; i<constraint.numOfEdges; i++) {
//				ArrayList<String> ar = new ArrayList<String>();
				File csvFile = new File(fileLocation);
				BufferedReader br = new BufferedReader(new FileReader(csvFile));
				String line = "";
				StringTokenizer st = null;
				int lineNumber = 0;
				int tokenNumber = 0;
				int flag = 0;			
				ArrayList<Integer> valueList = new ArrayList();
				ArrayList<Integer> edge = new ArrayList();
				
				while ((line = br.readLine()) != null) {
//					String[] arr = line.split(",");
					//for the first line it'll print
					if (flag == 0) {
//						System.out.println(line);
						String[] arr = line.split(",");
						
//						System.out.println(arr[l].substring(2)+arr[l+1].substring(0,arr[l+1].length()-2)+"\n");
						edge.add(Integer.parseInt(arr[l].substring(2)));
						edge.add(Integer.parseInt(arr[l+1].substring(0,arr[l+1].length()-2)));
//						System.out.println(Arrays.asList(arr[2*i],arr[(2*i)+1]));
						flag = 1;
						l += 2;
					}
					else {
						String[] arr = line.split(",");
						valueList.add(Integer.parseInt(arr[i]));
					}
//						System.out.println(arr[0]); // Vito
					lineNumber++;
					//use comma as token separator
				}
//				System.out.println(edge +": valueList: "+ valueList);
				Algo trafficData = new Algo();
				trafficData.addtr(edge, valueList);
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		finally{
			//System.out.println("done");
		}
	}  
}  