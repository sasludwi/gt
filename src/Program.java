import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Program {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
				
		long startTime = System.currentTimeMillis();
		
		// 10Stk            10^-6     5*10^-6    10^-5   5*10^-5  10^-4   9*10^-4  10^-3   5*10^-3 
		double[] thetas = {0.000001, 0.000005, 0.00001, 0.00005, 0.0001, 0.0009,  0.001,   0.005,  0.05,  0.09};
		// double[] thetas = {0.000001, 0.005};
		
		HashMap <Double, Graph> humanGraphs = new HashMap<Double, Graph> ();
		HashMap <Double, Graph> apeGraphs = new HashMap<Double, Graph> ();
		Graph g = new Graph ();
		
		for ( double theta : thetas ) 
		{
			// human
			g = new Graph ();
			g.loadGraphFile("./data/TFhvshCORTab.txt", theta);
			humanGraphs.put(theta, g);
			
			// ape
			g = new Graph ();
			g.loadGraphFile("./data/TFcvscCORTab.txt", theta);
			apeGraphs.put(theta, g);
		}
		
		// Export vertex / neighbours => barchart visualization
		Graph.exportDescVertexNeighbours (humanGraphs, apeGraphs, 30);
		
		Graph.exportThresholdVsNumberEdges(humanGraphs, apeGraphs);
		
		Graph.exportThresholdVsNumberComponents(humanGraphs, apeGraphs);
		
		Graph.exportThresholdVsDegreeDistribution(humanGraphs, apeGraphs);
		
		Graph.exportThresholdVsComponentSize(humanGraphs, apeGraphs);
		
		Graph.exportThresholdVsEdgeWeights(humanGraphs, apeGraphs);
		
		Graph.exportFixedThresholdVsEigenwert(humanGraphs, apeGraphs, 30);
		
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );				
	}
	
}
