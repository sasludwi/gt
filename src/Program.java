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
		// double[] thetas = {0.000001, 0.000005, 0.00001, 0.00005, 0.0001, 0.0009,  0.001,   0.005,  0.05,  0.09};
		double[] thetas = {0.000001, 0.005};
		
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
		// Graph.exportDescVertexNeighbours (graphs, 30);
		
		// Graph.exportThresholdVsNumberEdges(humanGraphs, apeGraphs);
		
		// Graph.exportThresholdVsNumberComponents(humanGraphs, apeGraphs);
		
		Graph.exportThresholdVsDegreeDistribution(humanGraphs, apeGraphs);
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );		
		
		
		
		
		
		
		
		
		
		
		// task 1
		/*
		if( makePlots )
		{		
			FileOutputStream edgesJsonFO = null;
			PrintStream edgesJsonPS = null;
			
			FileOutputStream edgesFO = null;
			PrintStream edgesPS = null;
			FileOutputStream componentFO = null;
			PrintStream componentPS = null;
			FileOutputStream degreesFO = null;
			PrintStream degreesPS = null;
			FileOutputStream weightsFO = null;
			PrintStream weightsPS = null;
			FileOutputStream compsFO = null;
			PrintStream compsPS = null;
			
			edgesJsonFO = new FileOutputStream("web/threshold_vs_NumberEdges/data.jsopn");
			edgesJsonPS =
			
			componentFO = new FileOutputStream("out/components.dat");
			componentPS = new PrintStream(componentFO);
			componentPS.println("# threshold   number_of_components");
			
			edgesFO = new FileOutputStream("out/edges.dat");
			edgesPS = new PrintStream(edgesFO);
			edgesPS.println("# threshold   number_of_edges");
			
			// for each theta value
			for (double threshold : thetas) 
			{
				gh = new Graph ();
				gh.loadGraphFile("./data/TFhvshCORTab.txt", threshold);	// read in a file
				
				gc = new Graph ();
				gc.loadGraphFile("./data/TFcvscCORTab.txt", threshold);	// read in a file
				
				// (a) theta vs number edges
				// a.1 JSON
				
				
				
				// a.2 gnuplot
				edgesPS.println(String.valueOf(threshold) + "\t" + String.valueOf( gh.getNumberOfEdges() ));
				System.out.println ( "theta: " + threshold + " - edges: " + gh.getNumberOfEdges () + " - nodes: " + gh.getVertexes().size());	
				
				// (b) theta vs edge weights
				weightsFO = new FileOutputStream("out/weights_" + String.valueOf(threshold) + ".dat");
				weightsPS = new PrintStream(weightsFO);
				weightsPS.println("# weights");
				for ( String from : gh.getGraph().keySet() ) {
					for ( String to : gh.getGraph().get(from).keySet() ) {
						if(gh.getGraph().get(from).get(to) != null){
							double val = gh.getGraph().get(from).get(to)[1];
							weightsPS.println(String.valueOf( val ));
						}
					}
				}
				weightsFO.close();
				weightsPS.close();
				
				
				// (c) theta vs degree-dist
				degreesFO = new FileOutputStream("out/deg_" + String.valueOf(threshold) + ".dat");
				degreesPS = new PrintStream(degreesFO);
				degreesPS.println("# degree");
				for ( String key : gh.getGraph ().keySet() ) {
					degreesPS.println(String.valueOf(gh.getNumberOfVertexNeighbours (key)));
				}
				degreesFO.close();
				degreesPS.close();
				
				
				// (d) theta vs number of components
				LinkedList<LinkedList<String>> compList = gh.getComponents();
				
				componentPS.println(String.valueOf(threshold) + "\t" + String.valueOf( compList.size() ));
				System.out.println ( "theta: " + threshold + " - components: " + compList.size() );
				
				// (e) theta vs component-size-distribution
				compsFO = new FileOutputStream("out/comp_" + String.valueOf(threshold) + ".dat");
				compsPS = new PrintStream(compsFO);
				compsPS.println("# number components");
				for ( LinkedList<String> component : compList ) {
					compsPS.println(String.valueOf( component.size() ));
				}
				compsFO.close();
				compsPS.close();
				
			}
			
			edgesJsonFO.close();
			edgesJsonPS.close();
			
			componentFO.close();
			componentPS.close();
			edgesPS.close();
			edgesFO.close();
			System.out.println ("");
		}
		*/
		/*
		System.out.println("Going to load graph");
		gh.loadGraphFile("./data/TFhvshCORTab.txt", 0.00001);
		
		System.out.println("Going to get centroid values");
		HashMap<String, Integer> CV = gh.getCentroidValue();
		for(String vertex : CV.keySet()){
			System.out.println(vertex + ": " + CV.get(vertex));
		}
		
		
		// Testing purpose only
		Random generator = new Random();
		HashMap<String, Integer> CV = new HashMap<String, Integer>();
		for(String key : g.graph.keySet()){
			CV.put(key, generator.nextInt(500));
		}
		*/
		// ---------------------------------------------------------------------------
		/**
		 * k-Cores
		 * bis ca. k=71
		
		int biggestK = 0;
		for(int k = 80;k > 0; k--){
			if( gh.getKCore(k).size() > 0 ){
				biggestK = k;
				break;
			}
		}
		System.out.println("BiggestK = " + String.valueOf(biggestK));
		
		FileOutputStream jsonFO = new FileOutputStream("web/circlepack_files/flare.json");
		PrintStream jsonPS = new PrintStream(jsonFO);
		jsonPS.println("{");
		jsonPS.println("  \"name\": \"all\",");
		jsonPS.println("  \"children\": [");
		
		gh.saveKCoreToJson(biggestK-8, biggestK, jsonPS, CV );
		
		jsonPS.println(" ]");
		jsonPS.println("}");
		jsonPS.close();
		jsonFO.close();
		
		for(int k = biggestK - 10;k <= biggestK; k++){
			System.out.println("Going to calc kCore for k=" + String.valueOf(k));
			HashMap<String, HashMap<String, double[]>> kCore = gh.getKCore(k);
			System.out.println ( kCore.size() );
		}
		// ---------------------------------------------------------------------------
 */ 
		
		/*
		System.out.println("Going to get eigenvectors");
		LinkedList<double[]> EVs = g.getMaximumEigenVector();
		for(double[] ev : EVs)
		{
			if(ev != null){
				System.out.println("Neuer Eigenvektor\n\n");
				for(double e : ev)
				{
					System.out.println(e);
				}
			}
		}
		
		System.out.println("Going to get excentricities");
		HashMap<String, Integer> EC = g.getExcentricities();
		for(String vertex : EC.keySet()){
			System.out.println(vertex + ": " + EC.get(vertex));
		}
		
		System.out.println("Going to get statuses");
		HashMap<String, Integer> S = g.getStatuses();
		for(String vertex : S.keySet()){
			System.out.println(vertex + ": " + S.get(vertex));
		}
		
		System.out.println("Going to get centroid values");
		HashMap<String, Integer> CV = g.getCentroidValue();
		for(String vertex : CV.keySet()){
			System.out.println(vertex + ": " + CV.get(vertex));
		}
		*/
		
		
		/*
		HashMap<String, HashMap<String, Integer>> dists = g.getNumberShortestPathsMatrix();
		Integer k = dists.get("ADAR").get("ARNT");
		k = k + 1;
		for ( String startVertex : dists.keySet() ){
			for ( String endVertex : dists.get(startVertex).keySet() ){
				System.out.println (startVertex + "-" + endVertex + ": " + dists.get(startVertex).get(endVertex).toString() );
			}
		}
		*/
		// ---------------------------------------------------------------------------
		/**
		 * Distributions of degrees
		 */
		/*
		System.out.println ("Distribution of degrees:");
		System.out.println ("");
		for ( String key : g.getGraph ().keySet() ) {
			System.out.println ( "Vertex " + key + " has " + g.getNumberOfVertexNeighbours (key) + " neighbours" );
		}
		*/
		// ---------------------------------------------------------------------------
		/**
		 * k-Cores
		 */
		// int k = 100;
		// System.out.println ("Task 3: k-cores");
		// System.out.println ( g.getKCore(k).size() );
		// ---------------------------------------------------------------------------

		
		// ---------------------------------------------------------------------------
		// 4. Eigenvector
		//System.out.println ("Task 4: Eigenvector: ");
		//double[] maxEV = g.getMaximumEigenVector();
		//for (double entry : maxEV) {
		//	System.out.println (String.valueOf(entry));
		//}
		// ---------------------------------------------------------------------------
		
		
		// ---------------------------------------------------------------------------
		// 5. distance matrix
		//System.out.println ("Task 5: distance-matrix: ");
		//HashMap<String, HashMap<String, Integer>> distMat = g.getDistanceMatrix();
		/*
		HashMap<String, HashMap<String, Integer>> distMat = g.getDistanceMatrixFloyd();
		for ( String startVertex : distMat.keySet() )
		{
			for ( String endVertex : distMat.get(startVertex).keySet() )
			{
				System.out.println (startVertex + ": " + distMat.get(startVertex).get(endVertex).toString() );
			}
		}
		*/
		
	}
	
}
