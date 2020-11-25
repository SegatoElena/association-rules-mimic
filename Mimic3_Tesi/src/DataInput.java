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
	private double paramC;
	private double paramS;
	private int calc;
	private int tep_id;
	
	/*
	 * Costruttore vuto per la classe DataInput
	 */
	public DataInput() {
		inputValues = new LinkedList<String>();
		reader = null;
		paramC = 0;
		paramS = 0;
		calc = 1;
		tep_id = -1;
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
	
	public void setTepid(int tep_id_value) {
		tep_id = tep_id_value;
	}
	
	public int getTepid() {
		return tep_id;
	}
	
	public void setYColumn(String tmp) {
		yColumn = tmp;
	}
	
	public String getYColumn() {
		return yColumn;
	}
	
	public void setParamS(double value) {
		paramS = value;
	}
	
	public double getParamS() {
		return paramS;
	}
	
	public void setParamC(double value) {
		paramC = value;
	}
	
	public double getParamC() {
		return paramC;
	}
	
	public void setCalc(int value) {
		calc = value;
	}
	
	public int getCalc() {
		return calc;
	}
	
	/*
	 * metodo per la gestione dei diversi input passati dall'utente
	 * index = 0: input degli elementi delle X
	 * index = 1: input dell'elemento Y
	 * index = 2: input del parametro support
	 * index = 3: input del parametro confidence
	 * index = 4: input definire il calcolo da eseguire
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
			setParamS(Double.parseDouble(getReader()));   
			break;
		case 3:
			setReader();
			setParamC(Double.parseDouble(getReader()));   
			break;
		case 4:
			setReader();
			setCalc(Integer.parseInt(getReader()));
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + index);
		}
	}
	
	/*
	 * Metodo per convertire un numero intero in una stringa,che rappresenta il nome della colonna nel DB
	 * Per la conversione viene utilizzato il valore intero come indice all'interno di una striga, 
	 * cosi da poter selezionare il valore nella posizione corrispondente
	 */
	public void swapItems(DBConnector connection) {
		int index = 0;
	    for (String s : getInputValues()) {
	        setInputValues(index,connection.getColumnName(Integer.parseInt(s)));  
	        index++;
	    }
	}
}
