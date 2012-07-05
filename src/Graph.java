import java.io.File;
import java.io.FileNotFoundException;
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
				double[] evs = ed.getRealEigenvalues();
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
	 */
	public void exportDescVertexNeighbours (int max) 
	{
		PrintStream stdout = System.out;
		
		try {
			// System.setOut(new PrintStream(new File("./web/forcegraph_files/flare.json")));
			System.setOut(new PrintStream(new File("./web/bubble_files/flare.json")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		int i = 0;
		String [][] vertexes = new String [this.graph.keySet().size()][2];
		
		for ( String vertex : this.graph.keySet() )
		{
			// System.out.println ( vertex + " -> " + g.getNumberOfVertexNeighbours(vertex));
			vertexes [i++] = new String[] { vertex, String.valueOf(this.getNumberOfVertexNeighbours(vertex)) };
		}
		
		Arrays.sort(vertexes, new Comparator<Object>() {
	        @Override
	        public int compare(Object o1, Object o2) {
	            // cast the object args back to string arrays
	            String[] row1 = (String[])o1;
	            String[] row2 = (String[])o2;

	            // compare the desired column values
	            return Integer.valueOf(row1 [1]) < Integer.valueOf(row2 [1]) ? 1 : 0;
	        }
	    });

		boolean first = true;
		i = 0;
		
		System.out.println ("{ \"name\":\"center\", \"children\": [");
		
		for ( String [] ele : vertexes )
		{
			if ( first == false ) {
				System.out.print(",");
			} else {
				first = false;
			}
			
			System.out.print("{");
			
			System.out.println ("\"name\":\"" + ele [0] + "\",");
			System.out.println ("\"size\":\"" + (Integer.valueOf(ele [1])*1000) + "\"");

			
			System.out.println ("}");
			
			if ( i++ == max ) break;
		}
		
		System.out.println ("]}");
		
		System.setOut(stdout);
	}
}


