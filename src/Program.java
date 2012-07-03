public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			
		//Graph g = new Graph();
		//g.listDegree();
		
		long startTime = System.currentTimeMillis();
		
		// TODO Ask the user about the threshold
		
		double threshold = 0.05;
		
		Graph g = new Graph ();
		
		// read in a file
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
				
		/*
		threshold = 0.8;
		
		g = new Graph ();
		
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "" );

		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
		*/
		
		System.out.println ("");
		
		// ---------------------------------------------------------------------------
		/**
		 * Distributions of degree
		 */
		for ( String key : g.getGraph ().keySet() ) {
			System.out.println ( "Vertex " + key + " has " + g.getNumberOfVertexNeighbours (key) );
		}
		
		// ---------------------------------------------------------------------------
		
		long endTime = System.currentTimeMillis();

		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );
		
	}
}
