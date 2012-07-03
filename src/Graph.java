
public class Graph {
	
	/**
	 * Constructor
	 */
	public Graph()
	{
		
	}
	
	//Graph [i][j][0=p ; 1=r]
	
	/**
	 *  lists the degrees (in, out, sum incident) of the vertices
	 *  returns a list of triples (indegree, outdegree, strength)
	 *  the index of the list is the index of the vertex
	 */
	public void listDegree()
	{
		double[][][] g ={ { {0.1, 0.2} , null ,       null },
						  { null ,       {0.2, 0.3} , {0.7, 0.8 } },
						  { {0.8, 0.9} , {0.9, 1.0} , {1.0, 1.1 } }
					    };
		
		int size = 3;
		
		
		int out = 0;
		int in = 0;
		double strength = 0.0;
		
		int[] outdeg = new int[size];
		int[]  indeg = new int[size];
		double[] strengths = new double[size];
		
		for(int i=0; i<size; i++){
			out = 0;
			in = 0;
			strength = 0.0;
			for(int j=0; j<size; j++){
				if( g[i][j] != null && i != j){
					in++;
					strength += g[i][j][1];
				}
				if( g[j][i] != null && i != j){
					out++;
				}
			}
			outdeg[i] = out;
			indeg[i] = in;
			strengths[i] = strength;
		}
		
		System.out.println("Outdegs");
		for(int j=0; j<size; j++){
			System.out.println(indeg[j]);
		}
		
		System.out.println("Indegs");
		for(int j=0; j<size; j++){
			System.out.println(outdeg[j]);
		}
		
		System.out.println("Strength");
		for(int j=0; j<size; j++){
			System.out.println(strengths[j]);
		}
		
		// TODO return
	}
	
	
}


