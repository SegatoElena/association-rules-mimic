
import java.sql.*;

public class DBConnector {

	private Connection connection;
	private Statement statement;
	
	public DBConnector() {
		connection = null;
	}
	public DBConnector(String str) throws SQLException {
		connection = DriverManager.getConnection(str.toString(), "postgres", "postgres");
		connection.setSchema("tefd");
		
		String schema = connection.getSchema();
		
		System.out.println("Successful connection - Schema: " + schema);
		System.out.println("=========================================");
		
	}
	
	public ResultSet DBReplay(String query) throws SQLException {
		statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		
		return resultSet;
	}
	
}
