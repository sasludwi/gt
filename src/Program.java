public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			
		long startTime = System.currentTimeMillis();
		
		double threshold = 0.1;
		
		Graph g = new Graph ();
		
		// read in a file
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
		
		
		/*
		threshold = 0.3;
		
		g = new Graph ();
		
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "" );

		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
		

		
		threshold = 0.5;
		
		g = new Graph ();
		
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "" );

		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
		*/

		
		threshold = 0.8;
		
		g = new Graph ();
		
		g.loadGraphFile("./data/TFcvscCORTab.txt", threshold);
		
		System.out.println ( "" );

		System.out.println ( "Threshold for p value: " + threshold );
		
		System.out.println ( "Number of nodes: " + g.getVertexes().size() );
		
		System.out.println ( "Number of edges: " + g.getNumberOfEdges () );
		
		long endTime = System.currentTimeMillis();

		System.out.println("");
		System.out.println("---------------------------------");
		System.out.println("Program runs " + ( endTime-startTime ) / 1000 + " seconds" );
		
	}
}
