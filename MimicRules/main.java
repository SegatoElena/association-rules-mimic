import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
	/*
	 * Funzione principale che si interfaccia con la classe DBConnector per comunicare con il database
	 * e con la classe Rule per il calcolo della support e della confidence
	 */
			
	public static void main(String[] args) {
		
	    Rule rule = new Rule();
	    DBConnector connection;
		try {
			connection = new DBConnector("jdbc:postgresql://localhost:5432/postgres");
		} catch (SQLException e1) {
			connection = null;
			e1.printStackTrace();
			System.exit(0);
		}
	    
	    //DEBUG VALUES
	    Boolean check = false;
	    LinkedList<String> inputValues = new LinkedList<String>();
	    String yColumn = "";

    	System.out.println("Insert the parameters: (x-value | y-value)\n");
	    while(true) {
	    	try {
	            BufferedReader reader =
	                    new BufferedReader(new InputStreamReader(System.in));
	            String value = reader.readLine();
	            
	            if (value.charAt(0) == 'x') {
	            	inputValues.add(value.replace("x-", ""));
	            } else {
	            	yColumn = value.replace("y-", "");
	            	break;
	            }
	    	} catch (Exception e) { 
	    		System.out.print("Error occurred" + e.toString());
	    		System.exit(0);
	    	}
	    }
	    
	    rule.SetColumn(inputValues.size()+1);
	    rule.checkElement(yColumn, inputValues);
	    
	    System.out.println("> ELABORATION ");
	    if (!check) {
			rule.xParameters = new LinkedList<String>();
			String yString;
			
			/*System.out.println("Insert the parameters X:\n");
			for(int i = 0; i < inputValues.size()-1; i++) {
		    	try {
		            BufferedReader reader =
		                    new BufferedReader(new InputStreamReader(System.in));
		            String value = reader.readLine();
		            
		            rule.xParameters.add(value);
		            
		    	} catch (Exception e) { 
		    		System.out.print("Error occurred" + e.toString());
		    		System.exit(0);
		    	}
			}
			try {
	            BufferedReader reader =
	                    new BufferedReader(new InputStreamReader(System.in));
	            String value = reader.readLine();
	            
	            yString = value;
	            
	    	} catch (Exception e) { 
	    		System.out.print("Error occurred" + e.toString());
	    		System.exit(0);
	    	}
			
			*/
			//Query per contare il numero di elementi nella tabella sorgente
			try {
				ResultSet denominatore = connection.DBReplay("SELECT COUNT(id) AS total FROM tefd.atear_divided_results");
				denominatore.next();
			    rule.SetRow(Integer.parseInt(denominatore.getString(1)));
			} catch (Exception e) {
	  	         e.printStackTrace();
	  	         System.out.println(e);
	  	         System.err.println(e.getClass().getName() + ": " + e.getMessage());
	  	         System.exit(0);
			}
			
			 Object[] objArray = inputValues.toArray(); 
			  
		        // Convert Object[] to String[] 
		      String[] x = Arrays.copyOf(objArray, 
		                                      objArray.length, 
		                                      String[].class); 
			
			//String[] x = inputValues.toArray(new String[inputValues.size()]);
			System.out.println(Arrays.toString(x));
			//Costruisco in modo dinamico la query per ottenere tutti i valori della mia X
			String selectSql = rule.QueryGenerator(x);
			System.out.println(selectSql);
			
			
			//Esegue la query e popolo le liste xList e yList che contengono ripettivamente i parametri della X e i parametri della Y
			try {
				ResultSet resultSet = connection.DBReplay(selectSql);
				System.out.println("> QUERY DONE");	
				
				rule.XYList(resultSet);
				System.out.println("> CALCULATING SUPPORT DONE");
				System.out.println();
				
			} catch (Exception e) {
            	e.printStackTrace();
            	System.out.println(e);
            	System.err.println(e.getClass().getName() + ": " + e.getMessage());
            	System.exit(0);
			}
			
			
			//Fare più volte la query epr ogni riga del support
			System.out.println("X -> Y | SUPPORT (%) | CONFIDENCE (%)");
			for (int i = 0; i < rule.xList.size(); i++) {
				//cambiare xllist e ylist con il solo indice
				String selectSql2 = rule.QueryGenerator(x, yColumn, rule.xList.get(i), rule.yList.get(i).riga.get(0));
				System.out.println(selectSql2);
				try {
					ResultSet resultSet2 = connection.DBReplay(selectSql2);
					
					
					//rule.XYList(resultSet);
					rule.PrintResults(resultSet2, i);
					
					System.out.println();
					
				} catch (Exception e) {
		        	e.printStackTrace();
		        	System.out.println(e);
		        	System.err.println(e.getClass().getName() + ": " + e.getMessage());
		        	System.exit(0);
				}
			}
			System.out.print("DONE");
		} else { System.exit(0); }
	}
}
