package nitin.codebind;

//A Java program for Floyd Warshall All Pairs Shortest Path algorithm.
import java.util.*;
import java.lang.*;
import java.io.*;


class AllPairShortestPath
{
	final static int INF = Integer.MAX_VALUE;
	Hashtable<NodePair, List<Integer>> ps = new Hashtable();
	
	void PrintMap(Hashtable<NodePair, List<Integer>> map)
	{
		//System.out.print("Edge \t Shortest Path \n");
		for (Map.Entry PairEntry : map.entrySet()) {
		    System.out.println (PairEntry.getKey() + " : " + PairEntry.getValue());
		}
	}
	
	void findPath(int[][] path, int i, int j, List<Integer> route)
 {
     if (path[i][j] == i) {
         return;
     }
     findPath(path, i, path[i][j], route);
     route.add(path[i][j]);
 }
	
	void findPs(int dist[][], int path[][], int N)
	{
		// System.out.println("Node Pair \t Shortest Distance \t Shortest Path");
		for (int i=0; i<N-1; ++i)
		{
			for (int j=i+1; j<N; ++j)
			{
				
				NodePair nodePair = new NodePair();
				nodePair.setSource(i);
				nodePair.setDestination(j);
				
				
				List<Integer> route = new ArrayList<>();
             route.add(i);
//             System.out.println("findPath: "+path+" "+i+" "+j+" "+route);
             findPath(path, i, j, route);
             route.add(j);
//             System.out.println("route: "+ route);
				
				if (dist[i][j]==INF)
					ps.put(nodePair, route);
				else
					ps.put(nodePair, route);
			}
		}
	}

	void FloydWarshall(int graph[][], int N)
	{
		int dist[][] = new int[N][N];
		int path[][] = new int[N][N];
		
		// Initializing dist and path matrix
		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++)
			{
				dist[i][j] = graph[i][j];
				if (i == j) {
                 path[i][j] = 0;
             }
             else if (dist[i][j] != INF) {
                 path[i][j] = i;
             }
             else {
                 path[i][j] = -1;
             }
			}
		}
		
		// Floyd Warshall Algorithm
		for (int k = 0; k < N; k++)
		{
			for (int i = 0; i < N; i++)
			{
				for (int j = 0; j < N; j++)
				{
					if ( dist[i][k] != INF && dist[k][j] != INF && (dist[i][k] + dist[k][j] < dist[i][j]) )
					{
						dist[i][j] = dist[i][k] + dist[k][j];
                     path[i][j] = path[k][j];
					}
					if (dist[i][j] < 0)
	                {
	                    System.out.print("Negative distance value found!!");
	                    return;
	                }
				}
			}
		}
		
		findPs(dist, path, N);
		//PrintPs();
	}
}