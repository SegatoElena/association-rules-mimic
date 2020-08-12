
import java.sql.*;

public class DBConnector {

	private Connection connection;
	private Statement statement;
	
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
		ResultSet resultSet = statement.executeQuery(query);
		
		return resultSet;
	}
	
}
