public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		Graph g = new Graph ();
		int i = 1;
		
		// read in a file
		g.loadGraphFile("./data/TFcvscCORTab.txt");
		
		for ( String key : g.getGraph().keySet() ) 
		{
			System.out.println ( i++ + ") " + key + " has " + g.getVertexNeighbours(key).size() + " neighbours");
		}
		
		System.out.println ("Number of nodes: " + g.getVertexes().size());
	}
}
