import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class main {
	/*
	 * Funzione principale che si interfaccia con la classe DBConnector per comunicare con il database,
	 * con la classe DataInput per gestire gli input dell'utente, con la classe Rule per il calcolo della support e della confidence
	 * ed infine con la classe Phase che gestisce tutte le fasi dall'algoritmo.
	 */
	public static void main(String[] args) {
		//Inizializzo gli oggetti
		Rule rule = new Rule();
	    DBConnector connection;
	    DataInput dataInput = new DataInput();
	    Phases phases = new Phases();
	    
		try {
			//////////////////////////////////////////////////////////////
			// Inizializzo la connessione con il database
			connection = phases.connection();
			
			// Richiamo il metodo per gestire la fase di input, da linea di comando, dei parametri
			// necessari per svolgere i vari calcoli
			phases.insertParameters(rule, dataInput, connection);
			
			System.out.println("> ELABORATION ");
			
			//////////////////////////////////////////////////////////////
			// Calcolo delle righe totali all'iterno del DB
			phases.getTotalRowsOfDatabase(rule, connection);
			
			// Gestione delle fasi da eseguire a seconda della scelta effettuata dall'utente,
			// durante la fasi di inserimento dei parametri
			switch (dataInput.getCalc()) {
			case 1:
			case 3:
				phases.supportCalc(rule, dataInput, connection);
				phases.confidenceCalc(rule, dataInput, connection);
				break;
			case 2:
				phases.supportCalc(rule, dataInput, connection);
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + dataInput.getCalc());
			}

		} catch (SQLException e1) {
			connection = null;
			e1.printStackTrace();
 	        System.out.println(e1);
			System.exit(0);
		} catch (IOException e2) {
			System.out.print("Error occurred during inputs phase: " + e2.toString());
    		System.exit(0);
		}
	}
}
