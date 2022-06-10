package nitin.codebind;

import java.util.*;
import java.io.*;
import java.lang.Math;

//Data Must Start From Monday
public class Algo {
	final static int INF = Integer.MAX_VALUE;	// INF = Infinite
	final static int totalNumOfDays = 365;		// Total number of days
	static double thDist = 0;	// Distance threshold
	static int numOfNodes = 157;
	static int numOfEdges = 162;
	final static int timeSlotsPerDay = 12*24;	//Total Slots per day
	final static int totalTimeSlots = timeSlotsPerDay*totalNumOfDays;
	static double GraphDist = 0;
	static double DailythIM = 0;	// Interest Measure Threshold for Daily Patterns
	static double WeeklythIM = 0;	// Interest Measure Threshold for Weekly Patterns
	static double WeekendthIM = 0;	// Interest Measure Threshold for WEekend Patterns

	//private static final double MIN_VALUE = 0;
	static Hashtable<NodePair, List<Integer>> tr = new Hashtable<>();
	static Hashtable<NodePair, List<Integer>> cumulativeSumTr = new Hashtable();

	public static void helper(List<TimePeriod> combinations, int data[], int start, int end, int index) {
	    if (index == data.length) {
//	    	if ( (data[1]-data[0])>=4 && (data[1]-data[0])<=12 ) { //for 15 min dataset
//	    	if ( (data[1]-data[0])>=2 && (data[1]-data[0])<=6 ) { //for 30 min dataset
	    	if ( (data[1]-data[0])>=12 && (data[1]-data[0])<=36 ) { //for Seattle dataset
	    		// Hotspot can be of max 3 hour
				TimePeriod t = new TimePeriod();
				t.setStartTime(data[0]);
				t.setEndTime(data[1]);
				combinations.add(t);
			}

	    } else if (start <= end) {
	        data[index] = start;
	        helper(combinations, data, start + 1, end, index + 1);
	        helper(combinations, data, start + 1, end, index);
	    }
	}
	
	public static List<TimePeriod>generate(int n) {
		int r=2;
	    List combinations = new ArrayList<TimePeriod>();
	    helper(combinations, new int[r], 0, n-1, 0);
	    return combinations;
	}
	
	public static Collection<NodePair> EdgesOfPath(List<Integer> path){
		Collection<NodePair> l = new ArrayList<>();
		for(int i=0; i<path.size()-1; i++) {
			NodePair n = new NodePair();

			if (path.get(i) < path.get(i + 1)) {
				n.setSource(path.get(i));
				n.setDestination(path.get(i+1));
			} else {
				n.setSource(path.get(i+1));
				n.setDestination(path.get(i));
			}
			l.add(n);
		}
		//System.out.println(l);
		return l;
	}
	
	public static void addtr (ArrayList<Integer> edge, ArrayList<Integer> values) {
		NodePair n = new NodePair();
		n.setSource(edge.get(0));
		n.setDestination(edge.get(1));
		tr.put(n, values);
	}
	
	public static void FindCandidateHotspots(int Distance[][],
											 Hashtable<NodePair, List<Integer>> Ps,
											 Hashtable<NodePair, List<Integer>> tr,
											 List<TimePeriod> combinations,
											 List<Integer> cumulativeGraphTraffic) {
		
		List<String> Periodicity = new ArrayList<>(Arrays.asList("Daily", "Weekly", "Weekend"));
		//Collection<List<Integer>> Ck = new ArrayList<>();
		//System.out.println(Periodicity);
		//find st,et pair
		

		Hashtable< String, Hashtable< TimePeriod, Hashtable<List<Integer>, Double> > >  Dataset = new Hashtable<>();
		//double MaxIM = Double.MIN_VALUE;
		
		for (String pr : Periodicity) {
			Hashtable< TimePeriod, Hashtable<List<Integer>, Double> > dataForPr = new Hashtable<>();
			List<Double> list = new ArrayList<>();
//			System.out.println("\n---------------------------  " + pr + " Pattern  -------------------------------");
			for (TimePeriod StEtPair : combinations) {
				
//				System.out.println(StEtPair);
				Hashtable<List<Integer>, Double> IM = new Hashtable<>();
				//Hashtable<List<Integer>, Double> DI = new Hashtable<>();
				//double totalDensityIM0 = 0;
				//Hashtable<List<Integer>, Double> DI1 = new Hashtable<>();
				//double totalDensityIM1 = 0;
				// Find DI for every Periodicity and <ST,ET> for all the paths
				
				for (List<Integer> path : Ps.values()) {
					
					int start = StEtPair.getStartTime();
					int end = StEtPair.getEndTime();
					double totalDistIM0 = 0;
					double totalTrafficIM0 = 0;
					double totalDistIM1 = 0;
					double totalTrafficIM1 = 0;
					double expTr0 = 0;
					double expDist0 = 0;
					double expTr1 = 0;
					double expDist1 = 0;
					
//					System.out.println("Path: " + path);
					Collection<NodePair> Edges = EdgesOfPath(path);
					//System.out.println("Edges: " + Edges);
					
					int itr=1;
					if (pr == "Weekend") {
						start = 5*timeSlotsPerDay + StEtPair.getStartTime();	// Day 6th is Saturday i.e. First Saturday of dataset
						end = start + StEtPair.getEndTime()-StEtPair.getStartTime();
					}
					//System.out.println("p = " + p);
					for (NodePair e : Edges) {
						
						int flag0=0;
						int flag1=0;
						
						while (start<totalTimeSlots && end<totalTimeSlots) {		
							totalDistIM0 += (end-start+1)*Distance[e.getSource()][e.getDestination()];	
//							System.out.println(cumulativeSumTr.get(e)+" \tend+1: "+(end+1)+" start: "+start);
							totalTrafficIM0 += cumulativeSumTr.get(e).get(end+1)-cumulativeSumTr.get(e).get(start);
							expDist0 += (end-start+1)*GraphDist;
							expTr0 += cumulativeGraphTraffic.get(end+1)-cumulativeGraphTraffic.get(start);
								
							if (flag1 == 0) {
								totalTrafficIM1 += cumulativeSumTr.get(e).get(totalTimeSlots);
								totalDistIM1 += totalTimeSlots*Distance[e.getSource()][e.getDestination()];
								flag1 = 1;
							}
							/*
							if (flag1==0) {
								expDist1 = totalTimeSlots*GraphDist;
								expTr1 = cumulativeGraphTraffic.get(totalTimeSlots);
								flag1=1;
							}
							*/
									
							if (pr == "Daily")
								start += timeSlotsPerDay;
							else if (pr == "Weekly")
								start += timeSlotsPerDay*7;
							else if (pr == "Weekend") {
								if ( Math.ceil((double)(start+1)/(double)timeSlotsPerDay)%((6*itr)+(itr-1)) == 0) {	// Saturday
									start +=timeSlotsPerDay;		// Skipping 1 day to get a Sunday
									itr += 1;
								}
								else if ( (Math.ceil((double)(start+1)/(double)timeSlotsPerDay))%7 == 0)	//	Sunday
									start += 6*timeSlotsPerDay;		// Skipping 6 days to get a Saturday
							}
							end = start + StEtPair.getEndTime()-StEtPair.getStartTime();
						}
						
					}
					totalTrafficIM1 = totalTrafficIM1-totalTrafficIM0;
					totalDistIM1 = totalDistIM1-totalDistIM0;
					expTr1 = cumulativeGraphTraffic.get(totalTimeSlots)-expTr0;
					expDist1 = (totalTimeSlots*GraphDist)-expDist0;
					double IM0 = (totalTrafficIM0/totalDistIM0)/((expTr0/expDist0)*totalDistIM0);
					double IM1 = (totalTrafficIM1/totalDistIM1)/((expTr1/expDist1)*totalDistIM1);
					
					/*
					System.out.println("["+ StEtPair.get(0) + "," + StEtPair.get(1) +"]");
					System.out.println(path+"actual: "+ (totalTrafficIM0/totalDistIM0) + " exp: "+ (expTr0/expDist0)*totalDistIM0 + " IM0: "+IM0);
					System.out.println(path+"actual: "+ (totalTrafficIM1/totalDistIM1) + " exp: "+ (expTr1/expDist1)*totalDistIM1 + " IM1: "+IM1);
					System.out.println("\n");
					*/
					
					IM.put( path, ( IM0/IM1 ) );	//IM = IM0/IM1
//					System.out.println("line 234 : "+IM);
					list.add((IM0/IM1));
					/*
					if(totalTrafficIM0 != 0 && totalDistIM0 != 0) {
						DI.put(path, (double)totalTrafficIM0/(double)totalDistIM0);
						totalDenstiyIM0 += (double)totalTrafficIM0/(double)totalDistIM0;
					}
					
					if(totalTrafficIM1 != 0 && totalDistIM1 != 0) {
						DI1.put(path, (double)totalTrafficIM1/(double)totalDistIM1);
						totalDenstiyIM1 += (double)totalTrafficIM1/(double)totalDistIM1;
					}
					*/
				}

				//System.out.println("STET : " + StEtPair + "\n");
				
				/*
				//Calculate IM using DI
				for (List<Integer> path : DI.keySet()) {
					
					IM.put( path, ( (DI.get(path)/(expDI0))/(DI1.get(path)/(totalDenstiyIM1-DI1.get(path)))) );	//IM = IM0/IM1
					//System.out.println("IM : " + IM);
					MaxIM = Math.max( MaxIM, ((DI.get(path)/(totalDenstiyIM0-DI.get(path)))/(DI1.get(path)/(totalDenstiyIM1-DI1.get(path)))) );
					//System.out.println("MaxIM : " + MaxIM);// + "DIgetpath " + DI.get(path) + "totalDenstiyIM0-DI.get(path): " + (totalDenstiyIM0-DI.get(path)));
				}
				*/
				dataForPr.put(StEtPair, IM);
//				System.out.println("line 262 : "+IM);
			} // <st,et> Pair
			Dataset.put(pr, dataForPr);
			//Collections.sort(list);
			//System.out.println("list: " + list);
			if (pr == "Daily")
				DailythIM = Collections.max(list);
			else if (pr == "Weekly")
				WeeklythIM = Collections.max(list);
			else if (pr == "Weekend")
				WeekendthIM = Collections.max(list);
			
		} // Periodicity
		System.out.println("Inside Find candidate hotspots");
		
		var MaxIM = Double.MIN_VALUE;
		for (String pr : Dataset.keySet()) {
			
			System.out.println("\n\n************************************** " + pr + " Patterns **************************************");
			if (pr == "Daily")
				MaxIM = DailythIM;
			else if (pr == "Weekly")
				MaxIM = WeeklythIM;
			else if (pr == "Weekend")
				MaxIM = WeekendthIM;
			
			System.out.println("MaxIM: " + MaxIM);
			
			Object[] keys = Dataset.get(pr).keySet().toArray();
			Arrays. sort(keys);
			
			for(Object SE : keys) {
				
//				System.out.println("\n[S,E] : " + SE);
				// comment the following for loop to print all the interest measure values
				for(List<Integer> path : Dataset.get(pr).get(SE).keySet()) {	
					
//					if(Dataset.get(pr).get(SE).get(path) > MaxIM*0.6 )
//						System.out.println(path + " = " + Dataset.get(pr).get(SE).get(path));
				}
				//System.out.println(Dataset + "\n");	// uncomment to print all the IM values
			}
		}
		//System.out.println("Max (IM0/IM1) : " + MaxIM);
		//System.out.println("Data : " + data);
	}
	
	public static void main(String[] args) throws IOException {
		
		double start = System.currentTimeMillis();
		try {	PrintStream fileOut = new PrintStream("./out.txt");
				System.setOut(fileOut);
		}
		catch(FileNotFoundException ex) {	
			 ex.printStackTrace();
		}
		
		ArrayList myList = new ArrayList<String>(Arrays.asList("[0,1]", "[2,0]", "[3,2]", "[4,3]", "[5,4]", "[6,5]", "[7,6]", "[8,7]", "[9,8]", "[10,9]",
				"[11,10]", "[12,11]", "[13,12]", "[14,13]", "[15,14]", "[16,15]", "[17,16]", "[18,17]", "[19,18", "[20,19]", "[21,20]", "[22,21]",
				"[23,22]", "[24,23]", "[25,24]", "[26,25]", "[27,26]", "[28,27]", "[29,28]", "[30,29]", "[31,30]", "[32,31]", "[33,32]", "[34,33]",
				"[35,34]", "[36,35]", "[37,36]", "[38,37]", "[39,38]", "[40,39]", "[41,40]", "[42,41]", "[43,42]", "[44,43]", "[45,44]", "[46,45]",
				"[47,46]", "[48,47]", "[49,48]", "[50,49]", "[51,50]", "[52,51]", "[53,52]", "[54,53]", "[55,54]", "[56,55]", "[57,56]", "[58,57]",
				"[59,58]", "[60,59]", "[61,60]", "[62,61]", "[63,62]", "[64,63]", "[65,64]", "[66,65]", "[67,66]", "[68,67]", "[69,68]", "[70,69]",
				"[71,70]", "[72,71]", "[73,72]", "[74,29]", "[75,74]", "[76,75]", "[77,76]", "[78,77]", "[79,78]", "[80,79]", "[81,80]", "[82,81]",
				"[83,82]", "[84,83]", "[85,84]", "[86,85]", "[87,86]", "[88,87]", "[89,88]", "[90,89]", "[91,90]", "[92,1]", "[93,92]", "[94,93]",
				"[95,94]", "[96,95]", "[97,96]", "[98,97]", "[99,98]", "[100,99]", "[101,100]", "[102,101]", "[103,102]", "[104,103]", "[105,104]",
				"[106,105]", "[107,106]", "[108,107]", "[109,108]", "[110,109]", "[111,110]", "[112,111]", "[113,112]", "[114,113]", "[115,114]",
				"[116,115]", "[117,116]", "[118,117]", "[119,118]", "[120,119]", "[121,120]", "[122,121]", "[123,122]", "[124,123]", "[125,124]",
				"[126,125]", "[127,126]", "[128,127]", "[129,128]", "[130,129]", "[131,130]", "[132,131]", "[133,132]", "[134,133]", "[135,134]",
				"[136,135]", "[137,136]", "[138,39]", "[139,138]", "[140,139]", "[141,140]", "[142,141]", "[143,142]", "[144,143]", "[145,144]",
				"[146,145]", "[147,146]", "[148,147]", "[149,148]", "[150,149]", "[151,150]", "[152,151]", "[153,152]", "[154,153]", "[155,154]",
				"[156,155]", "[38,138]", "[73,137]", "[112,87]", "[114,87]", "[122,150]", "[152,122]"));
		        		  
		int [][] Distance= new int[numOfNodes][numOfNodes];
		//distance array
		for(int i=0; i<numOfNodes; i++) {
			for(int j=0; j<numOfNodes; j++) {
				String r = "[" + Integer.toString(i) + "," + Integer.toString(j) + "]";
				String s = "[" + Integer.toString(j) + "," + Integer.toString(i) + "]";
//				System.out.println("s: "+ s);
				if (i==j) {
					Distance[i][j] = 0;
				}
				else {
					if (myList.contains(r) || myList.contains(s)){
							Random rand = new Random(); //instance of random class
						    //generate random values from 0-20
						    int temp = (int)rand.nextInt(19)+1; 
						    Distance[i][j] = temp;
						    Distance[j][i] = temp;
					}	
					else {
						Distance[i][j] = INF;
						Distance[j][i] = INF;
					}
						
				}
			}
		}
		/*
		//printing distance matrix
		for(int i=0; i<numOfNodes; i++) {
			for(int j=0; j<numOfNodes; j++) {
				System.out.print(Distance[i][j]+" , ");
			}
			System.out.println();
		}
		*/
		
		double max = Double.MIN_VALUE;
		for(int i=0; i<numOfNodes; i++) {
			for(int j=i; j<numOfNodes; j++) {
				if(Distance[i][j] != INF) {
					if ((double)Distance[i][j]>max)
						max = Distance[i][j];
					GraphDist += Distance[i][j];
				}
			}
		}
		//System.out.println("MAx : " + max);
		//thDist = 0.15*max;
		//System.out.println("thDist : " + thDist);
		//System.out.println("GraphDist : " + GraphDist);
		
		//Read csv file into "tr"
		ReadCSV.main("/home/lenovo/eclipse-workspace/MTP/src/nitin/codebind/Updated_Seattle_5min_1year_dataset.csv");
//		System.out.println("tr: " + tr);
		
		// Cumulative sum of the traffic data for every edge
		for (NodePair edge : tr.keySet()) {
			ArrayList sumList = new ArrayList();
			int sumSoFar = 0;
			sumList.add(sumSoFar);
			for (int i=0; i<totalTimeSlots; i++) {
				sumSoFar += tr.get(edge).get(i);
				sumList.add(sumSoFar);
			}
			cumulativeSumTr.put(edge, sumList);
//			System.out.println("cumsum of edge: "+edge);
		}
		
//		System.out.println("Cumulative Sum of Traffic Data: " + cumulativeSumTr);
		
		//Storing total traffic of graph at different time intervals
		List<Integer> cumulativeGraphTraffic = new ArrayList<Integer>();
		cumulativeGraphTraffic.add(0);
		int sum=0;
		for(int i=0; i<totalTimeSlots; i++) {
			for(NodePair edge : tr.keySet()) {
				//System.out.println("edge: " + edge);
				sum+=tr.get(edge).get(i);
			}
			cumulativeGraphTraffic.add(sum);
		}
		//System.out.println("GT: " + cumulativeGraphTraffic);
		
		/*
		// Corner case for number of nodes
		if(N<2)
		{
			System.out.println("\n\nINVALID INPUT!!! \nNUMBER OF NODES MUST BE GREATER THAN 1");
			return;
		}
		*/
		
		// Find all Pair Shortest Paths
		System.out.println("APSP");
		AllPairShortestPath APSP = new AllPairShortestPath();
		APSP.FloydWarshall(Distance, numOfNodes);
		System.out.println("APSP over");
//		System.out.println("Node Pair : Shortest Path");
//		APSP.PrintMap(APSP.ps);
		
		//System.out.print("\nEdge : Traffic Data \n");
		//APSP.PrintMap(tr);
		
		System.out.println("Combinations\n");
		List<TimePeriod> combinations = generate(timeSlotsPerDay);
		//System.out.println("[St, Et]: " + combinations);
		System.out.println("Combinations over");
		System.out.println("Find Candidate Hotspots");
		FindCandidateHotspots(Distance, APSP.ps, tr, combinations, cumulativeGraphTraffic);
		System.out.println("Find candidates hotspots over");
		double end = System.currentTimeMillis();
		double elapsedTime = end - start;
		System.out.println("elapsedTime: "+ elapsedTime/1000);
		
		System.out.print("\n");
	}

}