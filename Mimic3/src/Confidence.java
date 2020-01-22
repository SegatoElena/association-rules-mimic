import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Confidence {
	  public double confidence;
	  
	  public Confidence() {
	  }
	  
	  public double confidence(String measure, String pre, String post, String event, double value) {
		  Connection connection = null;
	      try {
	    	 int count = 0;
	         connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
	         connection.setSchema("tefd");
	         
	         
	         String confidenceNumeratore = "SELECT COUNT (id) FROM atear_divided_results WHERE ";
	         if (!measure.equals("")) {
	        	 confidenceNumeratore += "measure = '" + measure + "'";
	        	count = 1;
	         }
	         if(!pre.equals("")) {
	        	 if (count == 1)
	        		 confidenceNumeratore += "AND trend_pre = '" + pre + "'";
	        	 else
	        		 confidenceNumeratore += "trend_pre = '" + pre + "'";
	        	 count = 1;
	         }
	         if(!event.equals("")) {
	        	 if (count == 1)
	        		 confidenceNumeratore += "AND event = '" + event + "'";
	        	 else
	        		 confidenceNumeratore += "event = '" + event + "'";
		         count = 1;
	         }
	         if(!post.equals("")) {
	        	 if(count == 1)
	        		 confidenceNumeratore += "AND trend_post = '" + post + "'";
	        	 else
	        		 confidenceNumeratore += "trend_post = '" + post + "'";
	         }
	         
	         
	         String confidenceDenominatore = "SELECT COUNT (id) FROM atear_divided_results WHERE ";
	         if (!measure.equals("")) {
	        	 confidenceDenominatore += "measure = '" + measure + "'";
	        	count = 1;
	         }
	         if(!pre.equals("")) {
	        	 if (count == 1)
	        		 confidenceDenominatore += "AND trend_pre = '" + pre + "'";
	        	 else
	        		 confidenceDenominatore += "trend_pre = '" + pre + "'";
	        	 count = 1;
	         }
	         if(!post.equals("")) {
	        	 if(count == 1)
	        		 confidenceDenominatore += "AND trend_post = '" + post + "'";
	        	 else
	        		 confidenceDenominatore += "trend_post = '" + post + "'";
	         }
	         
	         
	         try {
	        	 Statement statement = connection.createStatement();
	        	 ResultSet numeratore = statement.executeQuery(confidenceNumeratore);
	        	 
	        	 numeratore.next();
	        	 confidence = Integer.parseInt(numeratore.getString(1));
	        	 Statement statement2 = connection.createStatement();
	        	 ResultSet denominatore = statement2.executeQuery(confidenceDenominatore);
	        	 denominatore.next();
			     confidence = confidence/Integer.parseInt(denominatore.getString(1));
			     //System.out.println(confidence);
	         } catch (Exception e) {
	        	 System.out.println("confidence");
	        	 System.out.println(e);
	         }
	         connection.close();
	      }
	      catch (Exception e) {
	    	  System.out.println("confidence2");
	    	  e.printStackTrace();
	    	  System.err.println(e.getClass().getName()+": "+e.getMessage());
	    	  System.exit(0);
	         }
	      
	      return confidence;
	}

}