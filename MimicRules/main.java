import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class main {
	/*
	 * Funzione principale che si interfaccia con la classe DBConnector per comunicare con il database,
	 * con la classe DataInput per gestire gli input dell'utente e con la classe Rule per il calcolo della support e della confidence
	 */
			
	public static void main(String[] args) {
		
	    Rule rule = new Rule();
	    DBConnector connection;
	    DataInput dataInput = new DataInput();
		try {
			connection = new DBConnector("jdbc:postgresql://localhost:5432/postgres");
			connection.printColumn();
		} catch (SQLException e1) {
			connection = null;
			e1.printStackTrace();
			System.exit(0);
		}
	   
    	try {
    		System.out.println("Insert the parameters X: (es: 0, 31, 1)\n");
            dataInput.InputValues(0); 
    	    System.out.println("Insert the parameter Y: (es: 2)\n");
    		dataInput.InputValues(1);  
    		System.out.println("Insert a parameter for the support/confidence: (es: 0.01)");
    		
    		rule.SetColumn(dataInput.getSizeInput()+1);
        	if (rule.checkElement(dataInput.getYColumn(), dataInput.getInputValues())) 
        		throw new Exception();
    	    dataInput.swapItems(connection);  
            dataInput.setYColumn(connection.getColumnName(Integer.parseInt(dataInput.getYColumn())));
    		dataInput.InputValues(2);  
    	} catch (Exception e) { 
    		System.out.print("Error occurred during inputs phase: " + e.toString());
    		System.exit(0);
    	}
	    	    
	    System.out.println("> ELABORATION ");
		rule.xParameters = new LinkedList<String>();

		try {
			ResultSet denominatore = connection.DBReplay("SELECT COUNT(id) AS total FROM tefd.atear_divided_results");
			if(denominatore.next() == false) {
				throw new Exception("denominatore empty");
			} else {
				rule.SetRow(Integer.parseInt(denominatore.getString(1)));
			}
		} catch (Exception e) {
  	         e.printStackTrace();
  	         System.out.println(e);
  	         System.err.println(e.getClass().getName() + ": " + e.getMessage());
  	         System.exit(0);
		}
		
		Object[] objArray = dataInput.getInputValues().toArray(); 
		  
	    // Convert Object[] to String[] 
	    String[] x = Arrays.copyOf(objArray, 
	                               objArray.length, 
	                               String[].class); 		
		System.out.println(Arrays.toString(x));
		//Costruisco in modo dinamico la query per ottenere tutti i valori della mia X
		String selectSql = rule.QueryGenerator(x, dataInput.getParam());
		
		
		
		//Esegue la query e popolo le liste xList e yList che contengono ripettivamente i parametri della X e i parametri della Y
		try {
			ResultSet resultSet = connection.DBReplay(selectSql);
			//System.out.println(selectSql);	
			if(resultSet.next() == false) {
				throw new Exception("ResultSet empty: " + selectSql);
			}
			
			rule.XYList(resultSet);
			System.out.println("> CALCULATING SUPPORT DONE");
			System.out.println();
			
		} catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(e);
        	System.err.println(e.getClass().getName() + ": " + e.getMessage());
        	System.exit(0);
		}
		
		System.out.println("X -> Y | SUPPORT (%) | CONFIDENCE (%)");
		for (int i = 0; i < rule.xList.size(); i++) {
			String selectSql2 = rule.QueryGenerator(x, 
					dataInput.getYColumn(), 
					rule.xList.get(i), 
					rule.yList.get(i).riga.get(0), 
					dataInput.getParam());
			try {
				ResultSet resultSet2 = connection.DBReplay(selectSql2);
				
				if(resultSet2.next() == false) {
					throw new Exception("ResultSet empty: " + selectSql2);
				} else {
					rule.PrintResults(resultSet2, i);
				}
				
			} catch (Exception e) {
	        	e.printStackTrace();
	        	System.out.println(e);
	        	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	        	System.exit(0);
			}
		}
		System.out.print("> DONE");
	}
}
