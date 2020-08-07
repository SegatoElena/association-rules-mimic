import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class main {
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
	    String[] x = new String[2];
	    x[0] = "measure";
	    x[1] = "trend_pre";
	    String[] yColumn = {"event"};
	    
	    rule.checkElement(rule.columnNumber, yColumn[0], x);
	    
	    // Se i parametri scelti come input e output sono adeguati e non presentano uguaglianze, allora viene eseguito il calcolo.
		if (!check) {
			rule.xParameters = new LinkedList<String>();
			
			//DEBUG VALUES
			rule.xParameters.add("Respiratory Rate");
			rule.xParameters.add("STEADY");
			String yString = "event";
			
			//Query per contare il numero di elementi nella tabella sorgente
			try {
				ResultSet denominatore = connection.DBReplay("SELECT COUNT(id) AS total FROM tefd.atear_divided_results");
				denominatore.next();
			    rule.numberRow = Integer.parseInt(denominatore.getString(1));
			} catch (Exception e) {
	  	         e.printStackTrace();
	  	         System.out.println(e);
	  	         System.err.println(e.getClass().getName() + ": " + e.getMessage());
	  	         System.exit(0);
			}
			
			
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
		}
	}
}
