import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.LinkedList;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import com.rits.cloning.Cloner;


public class Graph {
	
	protected HashMap<String, HashMap<String, double[]>> graph;
	protected Vector <String> vertexes;
	protected Integer inf = 999999;
	
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
				    fromNode = extractVertexes ( line ) [0].replace("\"", "");
				    toNode = extractVertexes ( line ) [1].replace("\"", "");
				    
				    correlationP = Double.valueOf( line.split("\t") [4] );
				    
				    
			    } catch ( Exception e) {
			    	System.out.println ( "Problems casting pValue and correlation P! " + e.getMessage () );
			    	continue;
			    }
			    
			    // handle fromNode
			    // fromNode has an entry
			    if ( true == this.graph.containsKey(fromNode) ) {
			    	
			    	if ( ! fromNode.equals( toNode ) ) {
			    		value = new double [2];
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
			    	if ( ! fromNode.equals( toNode ) ) {
			    		value = new double [2];
			    		value [0] = pValue;
				    	value [1] = correlationP;
				    	
				    	this.graph.get(fromNode).put (toNode, value);
			    	}
			    }
			    
			    /*
			    // add symmetric entry (but has not changed the number of edges compared to normal insert)
			    // handle toNode
			    // toNode has an entry
			    if ( true == this.graph.containsKey(toNode) ) {
			    	
			    	if ( ! toNode.equals( fromNode ) ) {
			    		value = new double [2];
				    	value [0] = pValue;
				    	value [1] = correlationP;
				    	
				    	this.graph.get(toNode).put (fromNode, value);
			    	}
			    	
			    } else {
			    	
			    	// toNode has NO entry
			    	this.graph.put(toNode, new HashMap <String,double[]> ());
			    	
			    	// set toNode > toNode to null
			    	this.graph.get(toNode).put (toNode, null);
			    	
			    	// set toNode > fromNode, if toNode != fromNode
			    	if ( ! toNode.equals( fromNode ) ) {
				    	value = new double [2];
				    	value [0] = pValue;
				    	value [1] = correlationP;
				    	
				    	this.graph.get(toNode).put (fromNode, value);
			    	}
			    }
			    */
			    
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public String [] extractVertexes (String line) 
	{
		String [] vertexes = {
			line.split("\t") [1].replaceAll("\\.", "-").replace('"', ' ').trim(),
			line.split("\t") [2].replaceAll("\\.", "-").replace('"', ' ').trim()
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
		Cloner cloner = new Cloner();
		
		HashMap<String, HashMap<String, double[]>> kCore = cloner.deepClone(this.graph);
		 
		int oldSize = 0;
		
		do {
			oldSize = kCore.size();
			
			for ( String vertex : this.graph.keySet() ) 
			{			
				if( kCore.containsKey(vertex) ){
					if(k <= kCore.get(vertex).size()) {
						// OK!
					} else {
						// - delete the vertex itself 
						kCore.remove(vertex);
						
						// - delete all outgoing edges of this vertex
						for ( String otherVertex : this.graph.keySet() ) 
						{
							if (true == kCore.containsKey(otherVertex))
							{
								if( kCore.get(otherVertex).containsKey(vertex)){
									kCore.get(otherVertex).remove(vertex);
								}
							}
						}
					}
				}
			}
			if ( oldSize == kCore.size() ) { 
				break; 
			}
			
		} while ( true );
		
		return kCore;
	}
	
	
	public HashMap<String, HashMap<String, Integer>> getDistanceMatrixFloyd ()
	{
		HashMap<String, HashMap<String, Integer>> distMat = 
				new HashMap<String, HashMap<String, Integer>> ();
		
		/*
		 * Algorithmus von Floyd
			(1) Für alle i,j : d[i,j] = w[i,j]
			(2) Für k = 1 bis n
			(3)    Für alle Paare i,j
			(4)        d[i,j] = min (d[i,j],d[i,k] + d[k,j])
		 */
		
		
		// (1) - set every item in the distance matrix to either 1 or infinite, depending if the edge is present or not
		for ( String startVertex : this.graph.keySet() )
		{
			distMat.put(startVertex, new HashMap <String, Integer> ());
			for ( String endVertex : this.graph.keySet() )
			{
				if (startVertex == endVertex)
				{
					distMat.get(startVertex).put(endVertex, 0);
				}
				else
				{
					if(this.graph.get(startVertex).keySet().contains(endVertex))
					{
						distMat.get(startVertex).put(endVertex, 1);
					}
					else
					{
						distMat.get(startVertex).put(endVertex, inf);
					}
				}
			}
		}
		Integer val1 = 0;
		Integer val2 = 0;
		
		// (2) Für k = 1 bis n
		for ( String k : this.graph.keySet() )
		{
			// (3 ) Für alle Paare i,j
			for ( String i : this.graph.keySet() )
			{
				for ( String j : this.graph.keySet() )
				{
					// (4) d[i,j] = min (d[i,j],d[i,k] + d[k,j])
					val1 = distMat.get(i).get(j);
					val2 = distMat.get(i).get(k) + distMat.get(k).get(j);
					if(val1 < val2)
					{
						distMat.get(i).put(j, val1);
					}
					else
					{
						distMat.get(i).put(j, val2);
					}
				}
			}
		}
		
		return distMat;
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
	
	
	// using Dijkstra
	/* 	 1  Funktion Dijkstra(Graph, Startknoten):
		 2      initialisiere(Graph,Startknoten,abstand[],Q)
		 3      solange Q nicht leer:                       // Der eigentliche Algorithmus
		 4          u := Knoten in Q mit kleinstem Wert in abstand[]
		 5          entferne u aus Q                                // für u ist der kürzeste Weg nun bestimmt
		 6          für jeden Nachbarn v von u:
		 7              falls v in Q:
		 8                 distanz_update(u,v,abstand[])   // prüfe Abstand vom Startknoten zu v
		 
		 1  Methode initialisiere(Graph,Startknoten,abstand[],Q):
		 2      für jeden Knoten v in Graph:
		 3          abstand[v] := unendlich
		 4      abstand[Startknoten] := 0
		 5      Q := Die Menge aller Knoten in Graph
		 
		 1  Methode distanz_update(u,v,abstand[]):
		 2      alternativ := abstand[u] + abstand_zwischen(u, v)   // Weglänge vom Startknoten nach v über u
		 3      falls alternativ < abstand[v]:
		 4          abstand[v] := alternativ
	 */
	public HashMap<String, HashMap<String, Integer>> getNumberShortestPathsMatrix ()
	{
		HashMap<String, HashMap<String, Integer>> pathLengthsMatrix = 
				new HashMap<String, HashMap<String, Integer>> ();
		HashMap<String, HashMap<String, Integer>> numberPathsMatrix = 
				new HashMap<String, HashMap<String, Integer>> ();
		LinkedList<String> nodes = new LinkedList<String>();
		
		int min = inf;
		int alternativ = 0;
		String u = null;
		
		// for every startVertex
		for ( String startVertex : this.graph.keySet() )
		{
			System.out.println("startVertex:" + startVertex);
			pathLengthsMatrix.put(startVertex, new HashMap <String, Integer>() );
			numberPathsMatrix.put(startVertex, new HashMap <String, Integer>() );
			
			// 2 - initialisiere(Graph,Startknoten,abstand[],Q)
			// 2+3 - für jeden Knoten v in Graph: abstand[v] := unendlich
			for ( String toVertex : this.graph.keySet() )
			{
				pathLengthsMatrix.get(startVertex).put(toVertex, inf);
				numberPathsMatrix.get(startVertex).put(toVertex, 0);
				// 5 - Q := Die Menge aller Knoten in Graph
				nodes.addLast(toVertex);
			}
			// 4 - abstand[Startknoten] := 0
			pathLengthsMatrix.get(startVertex).put(startVertex, 0);
			numberPathsMatrix.get(startVertex).put(startVertex, 1);	// 0 everywhere, but start-node = 1
			
			// start algorithm
			// 3 - solange Q nicht leer:
			while(nodes.size() != 0)
			{
				// 4 - u := Knoten in Q mit kleinstem Wert in abstand[]
				min = inf;
				u = null;
				for ( String vertex : nodes ){
					if( pathLengthsMatrix.get(startVertex).get(vertex) < min ){
						min = pathLengthsMatrix.get(startVertex).get(vertex);
						u = vertex;
					}
				}
				// 5 - entferne u aus Q
				nodes.remove(u);
				// 6 - für jeden Nachbarn v von u:
				if (this.graph.get(u) != null){
					for ( String neighbourVertex : this.graph.get(u).keySet() )
					{
						//  7 - falls v in Q:
						if( nodes.contains(neighbourVertex) ){
							// 8 - distanz_update(u,v,abstand[])
							// 2 - alternativ := abstand[u] + abstand_zwischen(u, v)
							alternativ = pathLengthsMatrix.get(startVertex).get(u) + 1;
							// 3 - falls alternativ < abstand[v]:
							if( alternativ < pathLengthsMatrix.get(startVertex).get(neighbourVertex) ){
								// 4 abstand[v] := alternativ
								pathLengthsMatrix.get(startVertex).put(neighbourVertex, alternativ);
								numberPathsMatrix.get(startVertex).put(neighbourVertex, 1);	// reset to 1
							}
							if( alternativ == pathLengthsMatrix.get(startVertex).get(neighbourVertex) ){
								Integer oldNum  = numberPathsMatrix.get(startVertex).get(neighbourVertex);
								Integer fromNum = numberPathsMatrix.get(startVertex).get(u);
								numberPathsMatrix.get(startVertex).put(neighbourVertex, oldNum + fromNum);	// add shortest paths
							}
						}
					}
				}
			}
			
			//for(String n : numberPathsMatrix.get(startVertex).keySet())
			//{
			//	System.out.println(startVertex +  "-" + n + ": " + numberPathsMatrix.get(startVertex).get(n) );
			//}
			
		}
		
		return pathLengthsMatrix;
	}
	
	
	
	
	// TODO: for each component
	public LinkedList<double[]> getMaximumEigenVector()
	{
		
		LinkedList<double[]> compsEV = new LinkedList<double[]>();
		LinkedList<LinkedList<String>> components = getComponents();
		
		
		for(LinkedList<String> comp :  components)
		{
			if( comp.size() > 5)
			{
				double[] maxEV = new double[0];
				
				int size = comp.size();
				double[][] matrixData = new double[size][size];
				
				int x = 0;
				int y = 0;
				
				for ( String startVertex : comp )
				{
					y = 0;
					for ( String endVertex : comp )
					{
						if (startVertex == endVertex)
						{
							matrixData[x][y] = 0;
						}
						else
						{
							if(this.graph.get(startVertex).keySet().contains(endVertex))
							{
								matrixData[x][y] = 1;
							}
							else
							{
								matrixData[x][y] = 0;
							}
						}
						y += 1;
					}
					x += 1;
				}
				
				RealMatrix m = new Array2DRowRealMatrix(matrixData);
				EigenDecomposition ed = new EigenDecomposition(m, 0);
				ed.getRealEigenvalues();
				maxEV = ed.getEigenvector(0).toArray();
				
				compsEV.addLast(maxEV);
			}
			else
			{
				compsEV.addLast(null);
			}
		}
				
		return compsEV;
	}
	
	
	public LinkedList<LinkedList<String>> getComponents()
	{
		
		LinkedList<LinkedList<String>> allComponents = new LinkedList<LinkedList<String>>();
		LinkedList<String> nodes = new LinkedList<String>();
		LinkedList<String> currentComponents = new LinkedList<String>();
		LinkedList<String>  stillToVisit = new LinkedList<String>();
		for ( String node : this.graph.keySet() ) {
			stillToVisit.addLast(node);
		}
		
		// if there are still unvisited nodes
		while(stillToVisit.size() != 0)
		{
			// pop the first unvisted node
			String startVertex = stillToVisit.getFirst();
			
			// start a new BFS with the current node
			nodes.clear();
			nodes.add(startVertex);
			currentComponents = new LinkedList<String>();
			
			// while there are elements in the list
			while(nodes.size() != 0)
			{
				// get the current node: FIFO principle
				String currentNode = nodes.removeFirst();
				// this node has now been visited
				stillToVisit.remove(currentNode);
				currentComponents.add(currentNode);
				
				// add all neighbours of the current node to the end of the list, if they are not already in the list
				for ( String nextVertex : this.graph.get(currentNode).keySet() )
				{
					if (nodes.contains(nextVertex) == false && stillToVisit.contains(nextVertex) == true)
					{
						nodes.addLast(nextVertex);
					}
				}
			}
			allComponents.add(currentComponents);
		}
		
		return allComponents;
	}
	
	
	public HashMap<String, Integer> getExcentricities ()
	{
		HashMap<String, Integer> excentricities = new HashMap<String, Integer>();
		HashMap<String, HashMap<String, Integer>> distMat = getDistanceMatrixFloyd();
		
		System.out.println("got the distance matrix");
		for(String fromVertex : this.graph.keySet()){
			excentricities.put(fromVertex, 0);
			for(String toVertex : distMat.get(fromVertex).keySet()) {
				if(distMat.get(fromVertex).get(toVertex) > excentricities.get(fromVertex) && (! distMat.get(fromVertex).get(toVertex).equals(inf)) )
					excentricities.put(fromVertex, distMat.get(fromVertex).get(toVertex));
			}
		}
		
		return excentricities;
	}

	
	public HashMap<String, Integer> getCentroidValue ()
	{
		HashMap<String, Integer> centroidValues = new HashMap<String, Integer>();
		HashMap<String, Integer> statuses = getStatuses();
		LinkedList<LinkedList<String>> components = getComponents();
		
		for(String vertex : this.graph.keySet()){
			Integer min = inf;
			LinkedList<String> currentComponent = null;
			for(LinkedList<String> cl : components){
				if(cl.contains(vertex))
					currentComponent = cl;
			}
			
			for(String otherVertex : statuses.keySet()) {
				if(currentComponent != null 
						&& currentComponent.contains(otherVertex) 
						&&  (! vertex.equals(otherVertex)) 
						&& statuses.get(otherVertex) < min ){
					min = statuses.get(otherVertex);
				}
			}
			if(min != inf){
				centroidValues.put(vertex, statuses.get(vertex) - min);
			}
			else {
				centroidValues.put(vertex, -1);
			}
		}
		
		return centroidValues;
	}
	
	
	public HashMap<String, Integer> getStatuses ()
	{
		HashMap<String, Integer> statutes = new HashMap<String, Integer>();
		HashMap<String, HashMap<String, Integer>> distMat = getDistanceMatrixFloyd();
		
		System.out.println("got the distance matrix");
		for(String fromVertex : this.graph.keySet()){
			statutes.put(fromVertex, 0);
			for(String toVertex : distMat.get(fromVertex).keySet()) {
				if( (! distMat.get(fromVertex).get(toVertex).equals(inf)) )
					statutes.put(fromVertex, statutes.get(fromVertex) + distMat.get(fromVertex).get(toVertex));
			}
		}
		
		return statutes;
	}
	
	/**
	 * 
	 * @param max
	 * @param humanGraphs
	 */
	public static void exportDescVertexNeighbours (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs, int max) 
	{
		PrintStream stdout = System.out;
		
		try {
			System.setOut(new PrintStream(new File("./web/Vertex_Neighbours/data.json")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		int i = 0;
		HashMap <Double, String [][]> vertexNeighbours = new HashMap <Double, String [][]> ();
		String [][] tmp = null;
		ArrayList <String> vertexes = new ArrayList <String> ();
		
		for ( double theta : humanGraphs.keySet() ) 
		{
			i = 0;
			
			tmp = vertexNeighbours.put(
				theta,
				new String [humanGraphs.get(theta).getVertexes().size()][2]
			);
			
			for ( String vertex : humanGraphs.get(theta).getVertexes() )
			{
				try {
					// System.out.println ( vertex + " -> " + g.getNumberOfVertexNeighbours(vertex));
					vertexNeighbours.get(theta) [i++] = new String[] { 
						vertex, 
						String.valueOf(humanGraphs.get(theta).getNumberOfVertexNeighbours(vertex))
					};
				} catch ( Exception e ) {
					System.out.println (e.getMessage());
				}
				
				if (-1 == vertexes.indexOf(vertex)) {
					vertexes.add(vertex);
				}
			}
			
			Arrays.sort(vertexNeighbours.get(theta), new Comparator<Object>() {
		        @Override
		        public int compare(Object o1, Object o2) {
		            // cast the object args back to string arrays
		            String[] row1 = (String[])o1;
		            String[] row2 = (String[])o2;
	
		            // compare the desired column values
		            return Integer.valueOf(row1 [1]) < Integer.valueOf(row2 [1]) ? 1 : 0;
		        }
		    });
		}

		boolean first = true;
		i = 0;
		
		System.out.println ("{ \"categories\": [");
		
		for ( String vertex : vertexes )
		{
			if ( first == false ) {
				System.out.print(",");
			} else {
				first = false;
			}
			
			System.out.println ("\"" + vertex + "\"");
			
			if ( i++ == max ) break;
		}
		
		i = 0;
		first = true;
		
		System.out.println ("],");
		System.out.println ("\"series\": [");
		
		for ( double theta : humanGraphs.keySet() ) 
		{
			if ( first == false ) {
				System.out.print(",");
			} else {
				first = false;
			}
			
			System.out.println ("{");
			System.out.println ("\"name\": \"Human - " + theta + "\",");
	        System.out.println ("\"data\": [");
	        
	        first = true;
	       
	        for ( String vertex : humanGraphs.get(theta).getVertexes() )
			{
				if ( first == false ) {
					System.out.print(",");
				} else {
					first = false;
				}
				
				System.out.println (humanGraphs.get(theta).getNumberOfVertexNeighbours(vertex));
				
				if ( i++ == max ) break;
			}
	        
	        i = 0;
			
	        System.out.println ("]");
	        System.out.println ("}");
		}
		
		i = 0;
		
		for ( double theta : apeGraphs.keySet() ) 
		{
			if ( first == false ) {
				System.out.print(",");
			} else {
				first = false;
			}
			
			System.out.println ("{");
			System.out.println ("\"name\": \"Ape - " + theta + "\",");
	        System.out.println ("\"data\": [");
	        
	        first = true;
	       
	        for ( String vertex : apeGraphs.get(theta).getVertexes() )
			{
				if ( first == false ) {
					System.out.print(",");
				} else {
					first = false;
				}
				
				System.out.println (apeGraphs.get(theta).getNumberOfVertexNeighbours(vertex));
				
				if ( i++ == max ) break;
			}
	        
	        i = 0;
			
	        System.out.println ("]");
	        System.out.println ("}");
		}
			
		System.out.println ("]");
		System.out.println ("}");
		
		System.setOut(stdout);
	}
	
	// also give as parameter: OldNodes 
	public void saveKCoreToJson(int currentK, int biggestK, PrintStream PS, HashMap<String, Integer> values){
		Cloner cloner = new Cloner();
		
		// Start
		PS.println("{");
		PS.println("\"name\": \"k=" + String.valueOf(currentK) + "\",");
		PS.println("\"children\": [");
		
		// recursion
		if(currentK < biggestK){
			saveKCoreToJson(currentK + 1, biggestK, PS, values);
		}
		
		// +++ save data +++
		// get the new nodes of this k-core by substracting the old ones
		// get nodes, to substract them in the next recursion level
		HashMap<String, HashMap<String, double[]>> currentNodes = this.getKCore(currentK);
		HashMap<String, HashMap<String, double[]>> nextNodes = this.getKCore(currentK + 1);
		int n_size = nextNodes.size();
		if(n_size > 0){
			Set<String> ks =  cloner.deepClone( currentNodes.keySet() );
			for(String vertex : ks ){
				if(nextNodes.containsKey(vertex)){
					currentNodes.remove(vertex);
				}
			}
		}
		
		// now save at most 5 nodes (the most central ones)
		boolean first = true;
		for(int k = 0; k < 5; k++){
			// find 5 times the biggest value and save it
			int size = currentNodes.size();
			if(size != 0){
				Integer min = inf;
				String min_node = "";
				for(String node : currentNodes.keySet()){
					if(values.get(node) < min){
						min = values.get(node);
						min_node = node;
					}
				}
				// add "," between node entries, but not in the inner most level at first position
				if(first && (currentK == biggestK)){
					first = false; }
				else{
					PS.println(","); }
				// save data
				PS.println("{\"name\": \"" + min_node + "\", \"size\": " + String.valueOf(500 - min) + "}");
				
				//remove biggest value
				currentNodes.remove(min_node);
			}
		}
		
		// End
		PS.println("]");
		PS.println("}");
	}
	
	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportThresholdVsNumberEdges (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_NumberEdges/data.json"));
			
			/**
			 *  TODO nachsortieren der Keys
			 */
			/*
			Arrays.sort(vertexNeighbours.get(theta), new Comparator<Object>() {
		        @Override
		        public int compare(Object o1, Object o2) {
		            // cast the object args back to string arrays
		            String[] row1 = (String[])o1;
		            String[] row2 = (String[])o2;
	
		            // compare the desired column values
		            return Integer.valueOf(row1 [1]) < Integer.valueOf(row2 [1]) ? 1 : 0;
		        }
		    });
		    */
			
			boolean first = true;
			
			output.println ("{ \"categories\": [");
			
			for ( double theta : humanGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + theta + "\"");
			}
			
			first = true;
			
			output.println ("],");
			output.println ("\"series\": [");
			output.println ("{");
			output.println ("\"name\": \"HUMAN\",");
			output.println ("\"data\": [");
			
			for ( double theta : humanGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
		        
		        output.println ("" + humanGraphs.get(theta).getNumberOfEdges() + "");
			}
			
			output.println ("]");
	        output.println ("},");
	        
	        first = true;
	        
	        output.println ("{");
			output.println ("\"name\": \"CHIMPANZEE\",");
			output.println ("\"data\": [");
			
			for ( double theta : apeGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
		        
		        output.println ("" + apeGraphs.get(theta).getNumberOfEdges() + "");
			}
			
			output.println ("]");
	        output.println ("}");
				
			output.println ("]");
			output.println ("}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportThresholdVsNumberComponents (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_NumberComponents/data.json"));
			
			/**
			 *  TODO nachsortieren der Keys
			 */
			/*
			Arrays.sort(vertexNeighbours.get(theta), new Comparator<Object>() {
		        @Override
		        public int compare(Object o1, Object o2) {
		            // cast the object args back to string arrays
		            String[] row1 = (String[])o1;
		            String[] row2 = (String[])o2;
	
		            // compare the desired column values
		            return Integer.valueOf(row1 [1]) < Integer.valueOf(row2 [1]) ? 1 : 0;
		        }
		    });
		    */
			
			boolean first = true;
			
			output.println ("{ \"categories\": [");
			
			for ( double theta : humanGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + theta + "\"");
			}
			
			first = true;
			
			output.println ("],");
			output.println ("\"series\": [");
			output.println ("{");
			output.println ("\"name\": \"HUMAN\",");
			output.println ("\"data\": [");
			
			for ( double theta : humanGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
		        
		        output.println ("" + humanGraphs.get(theta).getComponents().size() + "");
			}
			
			output.println ("]");
	        output.println ("},");
	        
	        first = true;
	        
	        output.println ("{");
			output.println ("\"name\": \"CHIMPANZEE\",");
			output.println ("\"data\": [");
			
			for ( double theta : apeGraphs.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
		        
		        output.println ("" + apeGraphs.get(theta).getComponents().size() + "");
			}
			
			output.println ("]");
	        output.println ("}");
				
			output.println ("]");
			output.println ("}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportThresholdVsDegreeDistribution (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_DegreeDistribution/data.json"));
			
			boolean first = true;
			
			int [] humanDegrees = new int [705];
			int [] apeDegrees = new int [705];
			int index = 0;
			
			output.println ("{ \"categories\": [");
			
			for ( int i = 0; i < 705; ++i ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + i + "\"");
				
				humanDegrees [i] = 0;
				apeDegrees [i] = 0;
			}
			
			first = true;
			boolean firstfirst = true;
			
			output.println ("],");
			output.println ("\"series\": [");
			
			for ( double threshold : humanGraphs.keySet() ) 
			{			
				for ( int i = 0; i < 705; ++i ) {
					humanDegrees [i] = 0; apeDegrees [i] = 0;
				}
			
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				for ( String vertex : humanGraphs.get(threshold).getGraph().keySet() ) 
				{
					index = humanGraphs.get(threshold).getNumberOfVertexNeighbours(vertex);
					humanDegrees [index] += 1;
				}

				for ( String vertex : apeGraphs.get(threshold).getGraph().keySet() ) 
				{
					index = apeGraphs.get(threshold).getNumberOfVertexNeighbours(vertex);
					apeDegrees [index] += 1;
				}
				
				firstfirst = true;
				
				// Output data
				// human
				output.println ("{");
				output.println ("\"name\": \"Human - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 705; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (humanDegrees [i]);
				}
				
				output.println ("]");
		        output.println ("},");
		        
		        firstfirst = true;
		        
		        // ape
		        output.println ("{");
				output.println ("\"name\": \"Chimpanzee - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 705; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (apeDegrees [i]);
				}
				
				output.println ("]");
		        output.println ("}");
			}
	        
				
			output.println ("]");
			output.println ("}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportThresholdVsComponentSize (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_ComponentSize/data.json"));
			
			// TODO: andere visualisierung
			
			boolean first = true;
			
			int [] humanDegrees = new int [705];
			int [] apeDegrees = new int [705];
			int index = 0;
			
			output.println ("{ \"categories\": [");
			
			for ( int i = 0; i < 705; ++i ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + i + "\"");
				
				humanDegrees [i] = 0;
				apeDegrees [i] = 0;
			}
			
			first = true;
			boolean firstfirst = true;
			
			output.println ("],");
			output.println ("\"series\": [");
			
			for ( double threshold : humanGraphs.keySet() ) 
			{			
				for ( int i = 0; i < 705; ++i ) {
					humanDegrees [i] = 0; apeDegrees [i] = 0;
				}
			
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				for ( LinkedList<String> component : humanGraphs.get(threshold).getComponents() ) 
				{
					humanDegrees [component.size()] += 1;
				}

				for ( LinkedList<String> component : apeGraphs.get(threshold).getComponents() ) 
				{
					apeDegrees [component.size()] += 1;
				}
				
				firstfirst = true;
				
				// Output data
				// human
				output.println ("{");
				output.println ("\"name\": \"Human - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 705; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (humanDegrees [i]);
				}
				
				output.println ("]");
		        output.println ("},");
		        
		        firstfirst = true;
		        
		        // ape
		        output.println ("{");
				output.println ("\"name\": \"Chimpanzee - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 705; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (apeDegrees [i]);
				}
				
				output.println ("]");
		        output.println ("}");
			}
	        
				
			output.println ("]");
			output.println ("}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportThresholdVsEdgeWeights (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_EdgeWeights/data.json"));
			
			// TODO: andere visualisierung
			
			boolean first = true;
			
			int [] humanWeights = new int [25];
			int [] apeWeights = new int [25];
			
			output.println ("{ \"categories\": [");
			
			for ( int i = 0; i < 25; ++i ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + (i*4) + "\"");
				
				humanWeights [i] = 0;
				apeWeights [i] = 0;
			}
			
			first = true;
			boolean firstfirst = true;
			double weight = 0;
			int j = 0;
			
			output.println ("],");
			output.println ("\"series\": [");
			
			for ( double threshold : humanGraphs.keySet() ) 
			{			
				for ( int i = 0; i < 25; ++i ) {
					humanWeights [i] = 0; apeWeights [i] = 0;
				}
			
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				for ( String startVertex : humanGraphs.get(threshold).getGraph().keySet() ) 
				{
					for ( String targetVertex : humanGraphs.get(threshold).getGraph().get(startVertex).keySet())
					{
						if ( humanGraphs.get(threshold).getGraph().get(startVertex).get(targetVertex) == null ) continue;
						
						weight = humanGraphs.get(threshold).getGraph().get(startVertex).get(targetVertex)[1];
						
						for ( int i = 0; i < 25; ++i ) {
							
							if ( weight < (i*0.04) ) {
								humanWeights [0 < i ? i-1 : 0] += 1;
								break;
							}
						}
					}
				}

				for ( String startVertex : apeGraphs.get(threshold).getGraph().keySet() ) 
				{
					for ( String targetVertex : apeGraphs.get(threshold).getGraph().get(startVertex).keySet())
					{
						if ( apeGraphs.get(threshold).getGraph().get(startVertex).get(targetVertex) == null ) continue;
						
						weight = apeGraphs.get(threshold).getGraph().get(startVertex).get(targetVertex)[1];
						
						for ( int i = 0; i < 25; ++i ) {
							if ( weight < (i*0.04) ) {
								apeWeights [0 < i ? i-1 : 0] += 1;
								break;
							}
						}
					}
				}
				
				firstfirst = true;
				
				// Output data
				// human
				output.println ("{");
				output.println ("\"name\": \"Human - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 25; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (humanWeights [i]);
				}
				
				output.println ("]");
		        output.println ("},");
		        
		        firstfirst = true;
		        
		        // ape
		        output.println ("{");
				output.println ("\"name\": \"Chimpanzee - " + threshold + "\",");
				output.println ("\"data\": [");
				
				for ( int i = 0; i < 25; ++i ) 
				{
					if ( firstfirst == false ) {
						output.print(",");
					} else {
						firstfirst = false;
					}
			        
			        output.println (apeWeights [i]);
				}
				
				output.println ("]");
		        output.println ("}");
			}
	        
				
			output.println ("]");
			output.println ("}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	 * @param humanGraphs
	 * @param apeGraphs
	 */
	public static void exportFixedThresholdVsEigenwert (HashMap <Double, Graph> humanGraphs, HashMap <Double, Graph> apeGraphs, int max) 
	{
		PrintStream output;
		try {
			output = new PrintStream(new FileOutputStream("./web/threshold_vs_Centroids/data.json"));
			
			double threshold = 0;
			boolean first = true;
			
			int i = 0;
			
			for ( double t : humanGraphs.keySet()) {
				threshold = t;
				break;
			}
			
			HashMap <String, Integer> humanCentroids = humanGraphs.get(threshold).getCentroidValue();
			HashMap <String, Integer> apeCentroids = apeGraphs.get(threshold).getCentroidValue();
			
			// TODO sort centroids
			
			output.println ("{ \"categories\": [");
			
			for ( String vertex : humanCentroids.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ("\"" + vertex + "\"");
				
				if ( i++ == max ) break;
			}
			
			first = true;
			i = 0;
			
			output.println ("],");
			output.println ("\"series\": [");
			
			output.println ("{");
			output.println ("\"name\": \"Human - " + threshold + "\",");
			output.println ("\"data\": [");
			
			for ( String vertex : humanCentroids.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ( humanCentroids.get(vertex) );
				
				if ( i++ == max ) break;
			}
			
			output.println ("]");
			output.println ("},");
			
			i = 0;
			first = true;

			output.println ("{");
			output.println ("\"name\": \"Ape - " + threshold + "\",");
			output.println ("\"data\": [");
			
			for ( String vertex : apeCentroids.keySet() ) 
			{
				if ( first == false ) {
					output.print(",");
				} else {
					first = false;
				}
				
				output.println ( apeCentroids.get(vertex) );
				
				if ( i++ == max ) break;
			}
			
			output.println ("]");
			output.println ("}");
			
			output.println ("]");
			output.println ("}");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


