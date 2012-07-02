import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class Graph {
	
	protected HashMap<String, HashMap<String, double[]>> graph;
	protected Vector <String> vertexes;
	
	/**
	 * Constructor
	 */
	public Graph()
	{
		this.graph = new HashMap<String, HashMap<String, double[]>> ();
		this.vertexes = new Vector <String> ();
	}
	
	public Vector<String> getVertexes () 
	{
		return this.vertexes;
	}
	
	public HashMap <String, HashMap<String, double[]>> getGraph () 
	{
		return this.graph;
	}
	
	/**
	 * Loads a file into the Graph instance
	 * @param fileName
	 */
	public void loadGraphFile ( String fileName, double threshold ) 
	{
		try {
			BigFile file = new BigFile (fileName);
			boolean jump = true;
			String fromNode = "", toNode = "";
			double [] value = new double [2];
			double pValue = 0,
					correlationP = 0;			
			
			for (String line : file) {
				
				if ( true == jump ) {
					jump = false;
					continue;
				}
				
				addVertexesFromLine ( line );
				
				// Read node names from current line 
			    fromNode = extractVertexes ( line ) [0];
			    toNode = extractVertexes ( line ) [1];
			    
			    try {
			    	pValue = Double.valueOf( line.split("\t") [3] );
			    	correlationP = Double.valueOf( line.split("\t") [4] );
			    	
			    	if ( pValue > threshold ) {
			    		continue;
			    	}
			    } catch ( Exception e) {
			    	System.out.println ( "Problems casting pValue and correlation P! " + e.getMessage () );
			    	continue;
			    }
			    
			    // handle fromNode
			    // fromNode has an entry
			    if ( true == this.graph.containsKey(fromNode) ) {
			    	
			    	if ( fromNode != toNode ) {
				    	value [0] = pValue;
				    	value [1] = correlationP;
				    	
				    	this.graph.get(fromNode).put (toNode, value);
			    	}
			    	
			    } else {
			    	
			    	// fromNode has NO entry
			    	this.graph.put(fromNode, new HashMap <String,double[]> ());
			    	
			    	// set fromNode > fromNode to null
			    	this.graph.get(fromNode).put (fromNode, null);
			    	
			    	// set fromNode > toNode, if fromNode != toNode
			    	if ( fromNode != toNode ) {
				    	value [0] = pValue;
				    	value [1] = correlationP;
				    	
				    	this.graph.get(fromNode).put (toNode, value);
			    	}
			    }
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String [] extractVertexes (String line) 
	{
		String [] vertexes = {
			line.split("\t") [1].replaceAll("\\.", "-"),
			line.split("\t") [2].replaceAll("\\.", "-")
		};
		
		return vertexes; 
	}
	
	public void addVertexesFromLine (String line)
	{
		String fromNode = this.extractVertexes(line) [0];
		String toNode = this.extractVertexes(line) [1];
		
		// from node i
		if ( -1 == this.vertexes.indexOf( fromNode ) ) {
	    	this.vertexes.add( fromNode );
	    }
		
		// to node j
		if ( -1 == this.vertexes.indexOf( toNode ) ) {
			this.vertexes.add( toNode );
	    }
	}
	
	/**
	 *  lists the degrees of the vertices
	 *  returns a list of pairs (indegree, outdegree)
	 *  the index of the list is the index of the vertex
	 */
	public void listDegree()
	{
		float[][] g = { { 0, 1, 2, 3, 4 },
						{ 5, 6, 7, 8, 9 } };
		
		
	}	
	
	/**
	 * Returns a list of names of the neighbours
	 * @param nodeName
	 * @return
	 */
	public ArrayList <String> getVertexNeighbours (String nodeName) 
	{
		ArrayList <String> neighbours = new ArrayList <String> ();
		
		if (true == this.graph.containsKey(nodeName)) 
		{
			for ( String neighbour : this.graph.get(nodeName).keySet() ) {
				if (false == neighbours.contains(neighbour) ) {
					neighbours.add(neighbour);
				}
			}
		}
		
		return neighbours;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfEdges () 
	{
		int numberOfEdges = 0;
		
		for ( String key : this.graph.keySet() ) 
		{
			numberOfEdges += this.getVertexNeighbours(key).size();
		}
		
		return numberOfEdges;
	}
}


