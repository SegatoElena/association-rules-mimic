import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/*
 * Questa classe gestisce gli input dati dall'utente. Presi i valori in input, secondo 
 * il pattern predefinito, inizializza le variabili. 
 */

public class DataInput {

	private BufferedReader reader;
	private LinkedList<String> inputValues;
	private String yColumn;
	private double param;
	
	/*
	 * Costruttore vuto per la classe DataInput
	 */
	public DataInput() {
		inputValues = new LinkedList<String>();
		reader = null;
		param = 0.01;
	}
	
	public void setReader() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public String getReader() throws IOException {
		return reader.readLine();
	}
	
	public void addInputValues(String tmp) {
		inputValues.add(tmp);  
	}
	
	public void setInputValues(int index, String s) {
		inputValues.set(index,s); 
	}
	
	public int getSizeInput() {
		return inputValues.size();
	}
	
	public LinkedList<String> getInputValues() {
		return inputValues;
	}
	
	public String GetInputValuesElement(int i) {
		return inputValues.get(i);
	}
	
	public void setYColumn(String tmp) {
		yColumn = tmp;
	}
	
	public String getYColumn() {
		return yColumn;
	}
	
	public void setParam(double value) {
		param = value;
	}
	
	public double getParam() {
		return param;
	}
	
	/*
	 * metodo per la gestione dei diversi input passati dall'utente
	 * index = 0: input degli elementi delle X
	 * index = 1: input dell'elemento Y
	 * index = 2: input del valore utilizzato nelle query per la support e la confidence
	 */
	public void InputValues(int index) throws IOException {
		switch (index) {
		case 0:
			setReader();
			String value = getReader();
			value = value.replaceAll("\\s+", "");
			String[] tmp = value.split(",");
			for (int i = 0; i<tmp.length; i++)
				addInputValues(tmp[i].toString()); 
			break;
		case 1:
			setReader();
			setYColumn(reader.readLine());
			break;
		case 2:
			setReader();
			setParam(Double.parseDouble(reader.readLine()));   
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + index);
		}
	}
	
	/*
	 * metodo per cambiare gli indici all'interno della lista nei valori effettivamente presenti
	 * all'interno del database
	 * Riceve in input l'oggetto DBConnector per poter comunicare con il database
	 */
	public void swapItems(DBConnector connection) {
		int index = 0;
	    for (String s : getInputValues()) {
	        setInputValues(index,connection.getColumnName(Integer.parseInt(s)));  
	        index++;
	    }
	}
}
