
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBConnector {

	private Connection connection;
	private Statement statement;
	private List<String> columnName;
	
	/*
	 * Costruttore vuoto della classe DBConnector
	 * inizializza la connessione con il data base a null
	 */
	public DBConnector() {
		connection = null;
	}
	
	/*
	 * Costruttore della classe DBConnector che riceve in input:
	 * str: Stringa di connesisone con il database
	 * 
	 */
	public DBConnector(String str) throws SQLException {
		connection = DriverManager.getConnection(str.toString(), "postgres", "postgres");
		connection.setSchema("tefd");
		
		String schema = connection.getSchema();
		
		System.out.println("Successful connection - Schema: " + schema);
		System.out.println("=========================================");
		
		columnName = new LinkedList<String>();
		
		printColumn();
		
	}
	
	// stampa il nome di tutte le colonne affiancate da un numero identificativo
	// inoltre salva il nome delle singole colonne all'interno di una lista
	public void printColumn() throws SQLException {	
		ResultSet rs = DBReplay("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'atear_divided_results';");
		if(rs.next() == false) {
			throw new SQLException("Empty table");
		} else {
			int index = 0;
			while (rs.next()) {
				setColumnName(rs.getString(1));
				System.out.println(index  + " - " + rs.getString(1));
				index++;
			}
		}
	}
	
	/*
	 * Metodo per richiamare la connessione con il databese e ottenere il risultato della query. 
	 * Questo metodo ha come input:
	 * query: stringa contentente la query da sottopporre al data base
	 * 
	 * Ritorna un ResultSet, contentente la risposta dal database
	 */	
	public ResultSet DBReplay(String query) throws SQLException {
		statement = connection.createStatement();
		return statement.executeQuery(query);
	}
	
	// Metodo per aggiungere un valore alla lista contente tutti i nomi delle colonne
	public void setColumnName(String s) {
		columnName.add(s);
	}	
	
	// Metodo per ritornare il nome di una data colonna, passato in input l'indice
	public String getColumnName(int index) {
		return columnName.get(index);
	}
	

	
}
