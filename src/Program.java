import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class Program {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
				
		long startTime = System.currentTimeMillis();
		
		// 10Stk            10^-6     5*10^-6    10^-5   5*10^-5  10^-4   9*10^-4  10^-3   5*10^-3 
		// double[] thetas = {0.000001, 0.000005, 0.00001, 0.00005, 0.0001, 0.0009,  0.001,   0.005,  0.05,  0.09};
		
		/*
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
		*/
		
		
		
		// GNU-Plot Stuff
		
		// 6Stk             10^-6     10^-5     10^-4   10^-3   10^-2 
		double[] thetas = {0.000001,  0.00001, 0.0001,  0.001,   0.01,  0.05};
				
		
		boolean makePlots = true;
		Graph gh = new Graph ();
		Graph gc = new Graph ();
		
		if( makePlots )
		{	
			
			// k-core Stuff
			if(true)
			{
				gh = new Graph ();
				gh.loadGraphFile("./data/TFhvshCORTab.txt", 0.00001);	// read in a file

				gc = new Graph ();
				gc.loadGraphFile("./data/TFcvscCORTab.txt", 0.00001);	// read in a file
				
				System.out.println("Going to get centroid values");
				HashMap<String, Integer> CV_h = gh.getCentroidValue();
				for(String vertex : CV_h.keySet()){
					System.out.println(vertex + ": " + CV_h.get(vertex));
				}
				
				System.out.println("Going to get centroid values");
				HashMap<String, Integer> CV_c = gc.getCentroidValue();
				for(String vertex : CV_c.keySet()){
					System.out.println(vertex + ": " + CV_c.get(vertex));
				}
				
				
				int biggestK_h = 0;
				for(int k = 300;k > 0; k--){
					if( gh.getKCore(k).size() > 0 ){
						biggestK_h = k;
						break;
					}
				}
				System.out.println("BiggestK_h = " + String.valueOf(biggestK_h));

				FileOutputStream jsonFO_h = new FileOutputStream("web/flare_h.json");
				PrintStream jsonPS_h = new PrintStream(jsonFO_h);
				gh.saveKCoreToJson(biggestK_h-9, biggestK_h, jsonPS_h, CV_h);
				
				int biggestK_c = 0;
				for(int k = 300;k > 0; k--){
					if( gc.getKCore(k).size() > 0 ){
						biggestK_c = k;
						break;
					}
				}
				System.out.println("BiggestK_c = " + String.valueOf(biggestK_c));

				FileOutputStream jsonFO_c = new FileOutputStream("web/flare_c.json");
				PrintStream jsonPS_c = new PrintStream(jsonFO_c);
				gc.saveKCoreToJson(biggestK_c-9, biggestK_c, jsonPS_c, CV_c);
			}
			
			
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

			componentFO = new FileOutputStream("out/components.dat");
			componentPS = new PrintStream(componentFO);
			componentPS.println("# threshold   num_comp_human   num_comp_chimp");

			edgesFO = new FileOutputStream("out/edges.dat");
			edgesPS = new PrintStream(edgesFO);
			edgesPS.println("# threshold   num_edges_human   num_edges_chimp");

			// for each theta value
			for (double threshold : thetas)
			{
				gh = new Graph ();
				gh.loadGraphFile("./data/TFhvshCORTab.txt", threshold);	// read in a file

				gc = new Graph ();
				gc.loadGraphFile("./data/TFcvscCORTab.txt", threshold);	// read in a file

				// (a) theta vs number edges
				edgesPS.println(String.valueOf(threshold) + "\t" +  gh.getNumberOfEdges()  + "\t" +  gc.getNumberOfEdges() );
				System.out.println ( "human: theta: " + threshold + " - edges: " + gh.getNumberOfEdges () + " - nodes: " + gh.getVertexes().size());	
				System.out.println ( "chimp: theta: " + threshold + " - edges: " + gc.getNumberOfEdges () + " - nodes: " + gc.getVertexes().size());
				
				
				// (b) theta vs edge weights
				weightsFO = new FileOutputStream("out/weights_h_" + String.valueOf(threshold) + ".dat");
				weightsPS = new PrintStream(weightsFO);
				
				double val_h = 0;
				weightsPS.println("# weights");
				for ( String from : gh.getGraph().keySet() ) {
					for ( String to : gh.getGraph().get(from).keySet() ) {
						if(gh.getGraph().get(from).get(to) != null){
							val_h = gh.getGraph().get(from).get(to)[1];
							weightsPS.println(String.valueOf( val_h ));
						}
					}
				}
				weightsFO.close();
				weightsPS.close();

				
				weightsFO = new FileOutputStream("out/weights_c_" + String.valueOf(threshold) + ".dat");
				weightsPS = new PrintStream(weightsFO);
				
				double val_c = 0;
				weightsPS.println("# weights");
				for ( String from : gc.getGraph().keySet() ) {
					for ( String to : gc.getGraph().get(from).keySet() ) {
						if(gc.getGraph().get(from).get(to) != null){
							val_c = gc.getGraph().get(from).get(to)[1];
							weightsPS.println(String.valueOf( val_c ));
						}
					}
				}
				weightsFO.close();
				weightsPS.close();
				
				
				// (c) theta vs degree-dist
				degreesFO = new FileOutputStream("out/deg_h_" + String.valueOf(threshold) + ".dat");
				degreesPS = new PrintStream(degreesFO);
				degreesPS.println("# degrees");
				for ( String key : gh.getGraph ().keySet() ) {
					degreesPS.println( gh.getNumberOfVertexNeighbours(key) );
				}
				degreesFO.close();
				degreesPS.close();
				
				degreesFO = new FileOutputStream("out/deg_c_" + String.valueOf(threshold) + ".dat");
				degreesPS = new PrintStream(degreesFO);
				degreesPS.println("# degrees");
				for ( String key : gc.getGraph ().keySet() ) {
					degreesPS.println( gc.getNumberOfVertexNeighbours(key) );
				}
				degreesFO.close();
				degreesPS.close();

				
				// (d) theta vs number of components
				LinkedList<LinkedList<String>> compList_h = gh.getComponents();
				LinkedList<LinkedList<String>> compList_c = gc.getComponents();
				
				componentPS.println(String.valueOf(threshold) + "\t" + String.valueOf( compList_h.size() ) + "\t" + String.valueOf( compList_c.size() ));
				System.out.println ( "human: theta: " + threshold + " - components: " + compList_h.size() );
				System.out.println ( "chimp: theta: " + threshold + " - components: " + compList_c.size() );

				
				// (e) theta vs component-size-distribution
				compsFO = new FileOutputStream("out/comp_h_" + String.valueOf(threshold) + ".dat");
				compsPS = new PrintStream(compsFO);
				compsPS.println("# num_comp");
				for ( LinkedList<String> component : compList_h ) {
					compsPS.println(String.valueOf( component.size() ));
				}
				compsFO.close();
				compsPS.close();
				
				
				compsFO = new FileOutputStream("out/comp_c_" + String.valueOf(threshold) + ".dat");
				compsPS = new PrintStream(compsFO);
				compsPS.println("# num_comp");
				for ( LinkedList<String> component : compList_c ) {
					compsPS.println(String.valueOf( component.size() ));
				}
				compsFO.close();
				compsPS.close();
				
			}

			componentFO.close();
			componentPS.close();
			edgesPS.close();
			edgesFO.close();
			System.out.println ("");
		}
		
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );				
	}
	
}
