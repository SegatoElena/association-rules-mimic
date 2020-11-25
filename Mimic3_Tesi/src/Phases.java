import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Phases {
	
	private String[] x;
	public Phases() { }
	private ResultSet resultSet;
	
	// Metodo per instanziare un oggetto DBConnector definendo una stringa di connessione da utilizzare
	public DBConnector connection() throws SQLException {
		return new DBConnector("jdbc:postgresql://localhost:5432/postgres");
	}
	
	// Metodo per la gestione dei valori in input, utili a calcolare support e/o confidence.
	public void insertParameters(Rule rule, DataInput dataInput, DBConnector connection) throws IOException {
		
		// Richiedo all'utente che calcoli vuole eseguire
		insertCalc(dataInput);
		
		// Gestione degli input a seconda delle scelte effettuate dall'utente
		switch (dataInput.getCalc()) {
			case 1:// Input dei parametri per il calcolo della support e confidence
				/*System.out.println("Insert the parameters X: (es: 0, 31, 1)\n");
		        dataInput.InputValues(0); 
			    System.out.println("Insert the parameter Y: (es: 2)\n");
				dataInput.InputValues(1);  */
				System.out.println("Insert a parameter for the support: (es: 0.01)");
				
				rule.SetColumn(dataInput.getSizeInput()+1);
				// Controllo sulla correttezza dei parametri inseriti. Le X non devono contenere i valori in Y e viceversa
		    	//if (rule.checkElement(dataInput.getYColumn(), dataInput.getInputValues())) 
		    	//	throw new IOException();
			    //dataInput.swapItems(connection);  
		        //dataInput.setYColumn(connection.getColumnName(Integer.parseInt(dataInput.getYColumn())));
				//dataInput.InputValues(2);  
				System.out.println("Insert a parameter for the confidence: (es: 0.01)");
				dataInput.InputValues(3); 
				break;				
			case 2: // Input dei parametri per il calcolo della support
				//System.out.println("Insert the parameters X: (es: 0, 31, 1)\n");
		        //dataInput.InputValues(0); 
				System.out.println("Insert a parameter for the support: (es: 0.01)");
				
				rule.SetColumn(dataInput.getSizeInput()+1);
			    //dataInput.swapItems(connection);  
				dataInput.InputValues(2);  				
				break;
			default:
				throw new IOException("No valid input");
		}
	}
	
	// Metodo per richiedere all'utente che calcoli eseguire durante l'esecuzioe del programma
	public void insertCalc(DataInput dataInput) throws IOException {
		System.out.println("Calculate:\n 1- Support/Confidance \n 2- Support\n\nInsert: ");
		dataInput.InputValues(4);
	}
	
	// Metodo per calcolare il numero totale di righe all'interno del DB
	public ResultSet getTotalRowsOfDatabase(Rule rule, DBConnector connection) throws SQLException {
		ResultSet denominatore = connection.DBReplay("SELECT COUNT(id) AS total FROM public.icustay_tep");
		if(denominatore.next() == false) {
			throw new SQLException("denominatore empty");
		} else {
			rule.SetRow(Integer.parseInt(denominatore.getString(1)));
		}
		
		return denominatore;
	}
	
	// Calcolo del valore della support
	public void supportCalc(Rule rule, DataInput dataInput, DBConnector connection) throws SQLException {
		String selectSql = rule.QueryGeneratorWithTepid(dataInput.getParamS());
		
		resultSet = connection.DBReplay(selectSql);
		//System.out.println(selectSql);	
		if(resultSet.next() == false) {
			throw new SQLException("ResultSet empty: " + selectSql);
		}
		
		rule.XYList(resultSet);
		
		// Controllo sulla scelta dell'utente.
		// Il valore della support viene visualizzato a schermo solamente nel caso in cui l'utente abbia richiesto il calcolo di quest'ultima
		if (dataInput.getCalc() == 2) {
			System.out.println("> CALCULATING SUPPORT DONE");
			System.out.println();
			rule.printSupport(connection);
			System.out.print("> DONE");
		}
	}
	
	// Viene creata l'array di stringhe X contenenti i parametri inseriti dall'utente per il calcolo della support
	private void createVariable(Rule rule, DataInput dataInput) {
		Object[] objArray = dataInput.getInputValues().toArray(); 
	    // Convert Object[] to String[] 
	    x = Arrays.copyOf(objArray, 
	                               objArray.length, 
	                               String[].class); 		
		System.out.println(Arrays.toString(x));		
	}	
	
	/*public void createTableTep(DBConnector connection) throws SQLException {
		String query = "CREATE TABLE trend_event_pattern ( "
				+ "id SERIAL PRIMARY KEY,"
				+ "tep VARCHAR(100) UNIQUE NOT NULL"
				+ ")";

		connection.executeQuery(query);

	}
	
	public void insertTepIntoDB(DBConnector connection, Rule rule) throws SQLException {
		String query;
		for (int i = 0; i < rule.YListSize(); i++) {
			query = "INSERT INTO trend_event_pattern(tep) VALUES ('"
			+ rule.GetXList(i).toString().replace(" ", "_") + "');";
			try {
				connection.executeQuery(query);
			} catch (org.postgresql.util.PSQLException e) {
				System.out.println("Duplicate entry...");
			}
		}
	}*/
	
	// Metodo per il calcolo della confidence 
	public void confidenceCalc(Rule rule, DataInput dataInput, DBConnector connection) throws SQLException {
		ResultSet resultSet2 = null;	
		if (dataInput.getCalc() == 1) {
			System.out.println("X -> Y | SUPPORT (%) | CONFIDENCE (%)");
		} else {
			System.out.println("X -> Y | CONFIDENCE (%)");
		}	
		for (int i = 0; i < rule.xList.size(); i++) {
			String selectSql2 = rule.QueryGenerator(x, 
					dataInput.getYColumn(), 
					rule.xList.get(i), 
					rule.yList.get(i).riga.get(0), 
					dataInput.getParamC());
			dbQA(rule, dataInput, connection, resultSet2, selectSql2, i);
		}
		System.out.print("> DONE");
	}
	
	// Metodo per interrogare il DB ed ottenere i valori di confidence sugli input passati.
	// Se l'utente ha richiesto il calcolo della sola confidence, viene richiamato un metodo per stampare i soli valori di confidece
	// Nel caso contrario viene richiamato un metodo per stampare i risultati nella loro totalità (confidence e support)
	private void dbQA(Rule rule, DataInput dataInput, DBConnector connection, ResultSet resultSet, String selectSql, int index) throws SQLException {
		resultSet = connection.DBReplay(selectSql);
		if(resultSet.next() == false) {
			throw new SQLException("ResultSet empty: " + selectSql);
		} else {
				rule.PrintResults(resultSet, index);
		}
	}
}
