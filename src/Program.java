public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BigFile file = new BigFile("./data/TFcvscCORTab.txt");
			
			for (String line : file)
			    System.out.println(line);
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		 		 
	}

}
