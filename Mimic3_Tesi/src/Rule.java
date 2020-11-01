import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Rule {
	
	public List<Righe> xList;
    public List<Righe> yList;
    public List<CountY> countList;
    private int columnNumber;
    private int numberRow;
    
    /*
     * Costruttore vuoto della classe Rule. Inizializza tutte le variabili.
     */
    public Rule() {
    	xList = new LinkedList<Righe>();
	    yList = new LinkedList<Righe>();
	    countList = new LinkedList<CountY>();
	    columnNumber = 0;
	    numberRow = 0;
    }
    
    public void SetXList(Righe tmp) {
    	xList.add(tmp);
    }
    
    public Righe GetXList(int index) {
    	return xList.get(index);
    }
    
    public void SetYList(Righe tmp) {
    	yList.add(tmp);
    }
    
    public Righe GetYList(int index) {
    	return yList.get(index);
    }
    
    public void SetColumn(int value) {
    	columnNumber = value;
    }
    
    public int GetColumn() {
    	return columnNumber;
    }
    
    public void SetRow(int value) {
    	numberRow = value;
    }
    
    public int GetRow() {
    	return numberRow;
    }
	/*
	 * Metodo che controlla che la colonna di output(yColumn) non sia utilizzata anche come valore di input.
	 * columnNumber: 		numero di colonne della tabella
	 * yColumn: 			nome del valore che vogliamo in output
	 * x: 					lista che contiene i nomi delle colonne da usare come input
	 * 
	 * return: 				true se l'elemento yColumn è presente nella lista x
	 * 						false altrimenti
	 */
	public boolean checkElement(String yColumn, LinkedList<String> x) {
		for(int i = 0; i < GetColumn()-1; i++) {
	    	if(yColumn.equals(x.get(i))) {
	    		return true;
	    	}
	    }
		return false;
	}
	
	/*
	 * Metodo popola le liste per il calcolo della support e della confidence
	 * resultSet: contiene il risultato della query sottoposta al database
	 * 
	 * return: void
	 */
	public void XYList(ResultSet resultSet) throws SQLException {
		Righe tmp;
		
		do  {
			tmp = new Righe();
			for(int j = 1; j < GetColumn(); j++) 
				tmp.set(resultSet.getString(j));
			SetXList(tmp);
			SetYList(new Righe(resultSet.getString(GetColumn())));
		} while(resultSet.next());
	}
	
	/*
	 * Metodo per la stampa dei risultati ottenuti.
	 * resultSet: contiene il risultato della query sottoposta al database
	 * index: indice della riga xList a cui i risultati fanno riferimento.
	 * 
	 * return: void
	 */
	public void PrintResults(ResultSet resultSet, int index) throws SQLException {
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		String supp = df.format(((Integer.parseInt(GetYList(index).toString().replace(" ", "")) / (float)GetRow() ) * 100.0));
		String conf = df.format(((Integer.parseInt(resultSet.getString(2)) / (float)(Integer.parseInt(GetYList(index).toString().replace(" ", "")))) * 100.0));
		System.out.println(GetXList(index).toString() + " -> " + resultSet.getString(1) + " | "
			+ supp + "%" 
			+ " | " + conf + "%" );
	}
	/*
	 * Metodo per la stampa della sola confidence
	 * resultSet: contiene il risultato della query sottoposta al database
	 * index: indice della riga xList a cui i risultati fanno riferimento.
	 * 
	 * return: void
	 */
	public void PrintConfidence(ResultSet resultSet, int index) throws SQLException {
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		String conf = df.format(((Integer.parseInt(resultSet.getString(2)) / (float)(Integer.parseInt(GetYList(index).toString().replace(" ", "")))) * 100.0));
		System.out.println(GetXList(index).toString() + " -> " + resultSet.getString(1) 
			+ " | " + conf + "%" );
	}
	
	/*
	 * Metodo per stampare i valori della support
	 * 
	 * return: void
	 */
	public void printSupport() {
		System.out.println("X  | SUPPORT (%) ");
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		for(int index = 0; index < yList.size()-1; index++)  {
			String supp = df.format(((Integer.parseInt(GetYList(index).toString().replace(" ", "")) / (float)GetRow() ) * 100.0));
			index++;
			System.out.println(GetXList(index).toString() + " | " + supp + "%");
		}
	}
	
	/*
	 * Metodo per la generazione della query per il calcolo della support
	 * x: lista contenente i valori di input
	 * return:
	 * selectSql: query ottenuta dalla combinazione dei valori ricevuti in input
	 */
	public String QueryGenerator(String[] x, double param) {
		String selectSql = "SELECT "; 
		selectSql += Arrays.toString(x).replace("[", "").replace("]", "") + ", COUNT(*)";	
		selectSql += " FROM tefd.atear_divided_results GROUP BY " + Arrays.toString(x).replace("[", "").replace("]", "")	
				+ " HAVING COUNT(*) > (" + GetRow()*param + ");";	        

		return selectSql;
	}
	
	/* 
	 * Metodo per la generazione della query per il calcolo della confidence
	 * x: lista contenente i valori di input
	 * y: lista contentente la y per il calcolo della confidence
	 * values: tutti i valori che le x possono assumere
	 * total: numero totale di x con un valore(values) all'iterno del database
	 * 
	 * return:
	 * selectSql: query ottenuta dalla combinazione dei valori ricevuti in input
	 */
	public String QueryGenerator(String[] x, String y, Righe values, String total, double param) {
		String selectSql = "SELECT "; 
		selectSql += y.replace("[", "").replace("]", "") + ", COUNT(*)";	
		selectSql += " FROM tefd.atear_divided_results" ;
				
		String tmp = " WHERE ";
		
		tmp += Arrays.toString(x).replace("[", "");
				
		for (int i = 0; i < values.riga.size(); i++) {
			tmp = tmp.replaceFirst(",", " = '" + values.get().get(i) + "' AND ");
		}
				
		tmp = tmp.replace("]", "= '" + values.get().get(values.get().size()-1))
			+ "' GROUP BY " + y.replace("[", "").replace("]", "")	
			+ " HAVING COUNT(*) > (" + Integer.parseInt(total)*param + ")"
			+ " ORDER BY COUNT(*) DESC;";
		
		selectSql += tmp;
		
		return selectSql;
	}
}