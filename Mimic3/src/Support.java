import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Support {
	public double support;
	
	public Support(){
	}
	
	public double support(String measure, String pre, String post, String event, double value) {
		Connection connection = null;
	      try {
	    	 int count = 0;
	         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
	         connection.setSchema("tefd");

	         String supportNumeratore = "SELECT COUNT (id) AS total FROM tefd.atear_divided_results WHERE ";
	         if (!measure.equals("")) {
	        	supportNumeratore += "measure = '" + measure + "'";
	        	count = 1;
	         }
	         if(!pre.equals("")) {
	        	 if (count == 1)
	        		supportNumeratore += " AND trend_pre = '" + pre + "'";
	        	 else
	        		supportNumeratore += "trend_pre = '" + pre + "'";
	        	 count = 1;
	         }
	         if(!event.equals("")) {
	        	 if (count == 1)
	        		 supportNumeratore += " AND event = '" + event + "'";
	        	 else
	        		 supportNumeratore += "event = '" + event + "'";
		         count = 1;
	         }
	         if(!post.equals("")) {
	        	 if(count == 1)
	        		 supportNumeratore += " AND trend_post = '" + post + "'";
	        	 else
	        		 supportNumeratore += "trend_post = '" + post + "'";
	         }
	         
	         //System.out.println(supportNumeratore);
	         
	         try {
	        	 //System.out.println("entra");
	        	 Statement statement = connection.createStatement();
	        	 //System.out.println("entra1");
	        	 ResultSet numeratore = statement.executeQuery(supportNumeratore);
	        	 //System.out.println("entra2");
	        	 
	        	 //System.out.println("entra3");
	        	 numeratore.next();
			     support = Integer.parseInt(numeratore.getString(1));
			     //System.out.println(support);
			     Statement statement2 = connection.createStatement();
			     //System.out.println("entraa");
			     ResultSet denominatore = statement2.executeQuery("SELECT COUNT(id) AS total FROM atear_divided_results");
			     denominatore.next();
			     support = support/Integer.parseInt(denominatore.getString(1));
			     //System.out.println(support);
	         } catch (Exception e) {
	        	 System.out.println(e);
	         }
	         connection.close();
	      }
	      catch (Exception e) {
		         e.printStackTrace();
		         System.err.println(e.getClass().getName()+": "+e.getMessage());
		         System.exit(0);
		         }
	      
	      return support;
	}
	
}