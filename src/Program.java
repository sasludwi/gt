import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class Program {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
				
		long startTime = System.currentTimeMillis();
		
		// TODO Ask the user about the threshold
		
		// task 1
		double[] thetas = {0.00001, 0.00002, 0.00005, 0.0001, 0.0002, 0.0005, 0.001, 0.002, 0.005, 0.01, 0.02, 0.05};
		
		
		FileOutputStream edgesFO = null;
		PrintStream edgesPS = null;
		FileOutputStream componentFO = null;
		PrintStream componentPS = null;
		FileOutputStream degreesFO = null;
		PrintStream degreesPS = null;
		FileOutputStream weightsFO = null;
		PrintStream weightsPS = null;
		
		componentFO = new FileOutputStream("out/components.dat");
		componentPS = new PrintStream(componentFO);
		componentPS.println("# threshold   number_of_components");
		
		edgesFO = new FileOutputStream("out/edges.dat");
		edgesPS = new PrintStream(edgesFO);
		edgesPS.println("# threshold   number_of_edges");
		
		// for each theta value
		for (double threshold : thetas) 
		{
			Graph g = new Graph ();
			g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);	// read in a file
			/*
			// (a) theta vs number edges
			edgesPS.println(String.valueOf(threshold) + "\t" + String.valueOf( g.getNumberOfEdges() ));
			System.out.println ( "theta: " + threshold + " - edges: " + g.getNumberOfEdges () + " - nodes: " + g.getVertexes().size());	
			
			// (b) theta vs edge weights
			weightsFO = new FileOutputStream("out/weights_" + String.valueOf(threshold) + ".dat");
			weightsPS = new PrintStream(weightsFO);
			weightsPS.println("# weights");
			for ( String from : g.getGraph().keySet() ) {
				for ( String to : g.getGraph().get(from).keySet() ) {
					if(g.getGraph().get(from).get(to) != null){
						double val = g.getGraph().get(from).get(to)[1];
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
			for ( String key : g.getGraph ().keySet() ) {
				degreesPS.println(String.valueOf(g.getNumberOfVertexNeighbours (key)));
			}
			degreesFO.close();
			degreesPS.close();
			*/
			// (d) theta vs number of components
			componentPS.println(String.valueOf(threshold) + "\t" + String.valueOf( g.getNumberOfComponents().size() ));
			System.out.println ( "theta: " + threshold + " - components: " + g.getNumberOfComponents().size() );	
		}
		
		componentFO.close();
		componentPS.close();
		edgesPS.close();
		edgesFO.close();
		System.out.println ("");
		
		
		Graph g = new Graph ();
		g.loadGraphFile("./data/TFcvscCORTab.txt", 0.001);	// read in a file
		
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
		// ---------------------------------------------------------------------------

		long endTime = System.currentTimeMillis();
		
		g.exportDescVertexNeighbours(30);

		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );
		
	}
}
