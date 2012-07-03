import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.LinkedList;

import com.rits.cloning.Cloner;


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
				
			    try {
			    	pValue = Double.valueOf( line.split("\t") [3] );
			    	
			    	if ( pValue >= threshold ) {
			    		continue;
			    	}
			    	
			    	addVertexesFromLine ( line );
					
					// Read node names from current line 
				    fromNode = extractVertexes ( line ) [0];
				    toNode = extractVertexes ( line ) [1];
				    
				    correlationP = Double.valueOf( line.split("\t") [4] );
				    
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
	 * Returns the number of the neighbours of a vertex
	 * @param nodeName
	 * @return
	 */
	public int getNumberOfVertexNeighbours (String nodeName) 
	{
		return this.getVertexNeighbours(nodeName).size();
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
	
	/**
	 * 
	 */
	public HashMap<String, HashMap<String, double[]>> getKCore (int k) 
	{
		// TODO realize clone!
		Cloner cloner = new Cloner();
		
		HashMap<String, HashMap<String, double[]>> kCore = cloner.deepClone(this.graph);
		 
		int oldSize = 0;
		
		do {
			oldSize = kCore.size();
			
			for ( String vertex : this.graph.keySet() ) 
			{				
				if(k <= kCore.get(vertex).size()) {
					// OK!
				} else {
					// - delete the vertex itself 
					kCore.remove(vertex);
					
					// - delete all outgoing edges of this vertex
					for ( String otherVertex : this.graph.keySet() ) 
					{
						if (true == kCore.containsKey(otherVertex))
							kCore.get(otherVertex).remove(vertex);
					}
				}
			}
			if ( oldSize == this.graph.size() ) { 
				break; 
			}
			
		} while ( true );
		
		return kCore;
		
	}
	
	
	
	/**
	 * 
	 * @return a m x m matrix. each element in this matrix is a pair of 
	 * 		(shortestDistance, numberOfShortestPaths) between node i and node j
	 */
	public HashMap<String, HashMap<String, Integer>> getDistanceMatrix ()
	{
		HashMap<String, HashMap<String, Integer>> distMat = 
				new HashMap<String, HashMap<String, Integer>> ();
		
		LinkedList<String>  nodes = new LinkedList<String>();
		LinkedList<String>  visit = new LinkedList<String>();
		LinkedList<Integer> dists = new LinkedList<Integer>();
		
		
		for ( String startVertex : this.graph.keySet() )
		{
			System.out.println("startVertex:" + startVertex);
			// for each node in the graph: start a breadth first search from this node
			visit.clear();
			distMat.put(startVertex, new HashMap <String, Integer> ());
			
			// start with the current node
			nodes.clear();
			nodes.add(startVertex);
			// keep nodes and distances in 2 separate lists
			dists.clear();
			dists.add(0);
			
			// while there are elements in the list
			while(nodes.size() != 0)
			{
				// get the current node: FIFO principle
				String currentNode  = nodes.removeFirst();
				Integer currentDist = dists.removeFirst();
				visit.addLast(currentNode);
				
				// store the current distance
				distMat.get(startVertex).put(currentNode, currentDist);
				
				// add all neighbours of the current node to the end of the list, if they are not already in the list
				for ( String nextVertex : this.graph.get(currentNode).keySet() )
				{
					if (nodes.contains(nextVertex) == false && visit.contains(nextVertex) == false)
					{
						nodes.addLast(nextVertex);
						dists.addLast(currentDist + 1);
					}
				}
			}
				
		}
		
		return distMat;
	}
	
	
}


