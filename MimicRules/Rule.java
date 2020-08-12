import javax.swing.JOptionPane;


import java.awt.EventQueue;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Rule {
	
	
	public List<Righe> xList;
    public List<Righe> yList;
    public List<CountY> countList;
	public List<String> xParameters;
    int columnNumber;
    int numberRow;
    
    /*
     * Costruttore vuoto della classe Rule. Inizializza tutte le variabili.
     */
    public Rule() {
    	xList = new LinkedList<Righe>();
	    yList = new LinkedList<Righe>();
	    countList = new LinkedList<CountY>();
	    xParameters = new LinkedList<String>();
	    columnNumber = 3;
	    numberRow = 0;
    }
    
	/*
	 * Metodo che controlla che la colonna di output(yColumn) non sia utilizzata anche come valore di input.
	 * columnNumber: 		numero di colonne della tabella
	 * yColumn: 			nome del valore che vogliamo in output
	 * x: 					lista che contiene i nomi delle colonne da usare come input
	 * 
	 * return: 				true se l'elemento yColumn � presente nella lista x
	 * 						false altrimenti
	 */
	public boolean checkElement(int columnNumber, String yColumn,String[] x) {
		for(int i = 0; i < columnNumber-1; i++) {
	    	if(yColumn.equals(x[i])) {
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
		while (resultSet.next()) {
			tmp = new Righe();
			for(int j = 1; j < columnNumber; j++) 
				tmp.set(resultSet.getString(j));
			xList.add(tmp);
			yList.add(new Righe(resultSet.getString(columnNumber)));
		}
		
		System.out.println("> LIST'S CREATED");
	}
	
	/*
	 * Metodo per la stampa dei risultati ottenuto.
	 * resultSet: contiene il risultato della query sottoposta al database
	 * index: indice della riga xList a cui i risultati fanno riferimento.
	 * 
	 * return: void
	 */
	public void PrintResults(ResultSet resultSet, int index) throws SQLException {
		while(resultSet.next()) {
			System.out.println(xList.get(index).toString() + " -> " + resultSet.getString(1) + " | "
				+ (Integer.parseInt(yList.get(0).toString().replace(" ", "")) / (float)numberRow ) * 100.0 + "%" 
				+ " | " + (Integer.parseInt(resultSet.getString(2)) / (float)(Integer.parseInt(yList.get(0).toString().replace(" ", "")))) * 100.0 + "%" );
			
		}
	}
	
	
	/*
	 * Metodo per la generazione della query per il calcolo della support
	 * x: lista contenente i valori di input
	 * return:
	 * selectSql: query ottenuta dalla combinazione dei valori ricevuti in input
	 */
	public String QueryGenerator(String[] x) {
		String selectSql = "SELECT "; 
		selectSql += Arrays.toString(x).replace("[", "").replace("]", "") + ", COUNT(*)";	
		selectSql += " FROM tefd.atear_divided_results GROUP BY " + Arrays.toString(x).replace("[", "").replace("]", "")	
				+ " HAVING COUNT(*) > (" + this.numberRow*0.01 + ");";	        

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
	public String QueryGenerator(String[] x, String[] y, Righe values, String total) {
		String selectSql = "SELECT "; 
		selectSql += Arrays.toString(y).replace("[", "").replace("]", "") + ", COUNT(*)";	
		selectSql += " FROM tefd.atear_divided_results" ;
				
		String tmp = " WHERE ";
		
		tmp += Arrays.toString(x).replace("[", "");
				
		for (int i = 0; i < values.riga.size()-1; i++) {
			tmp = tmp.replace(",", " = '" + values.riga.get(i) + "' AND ");
		}
				
		tmp = tmp.replace("]", "= '" + values.riga.get(values.riga.size()-1))
			+ "' GROUP BY " + Arrays.toString(y).replace("[", "").replace("]", "")	
			+ " HAVING COUNT(*) > (" + Integer.parseInt(total)*0.01 + ")"
			+ " ORDER BY COUNT(*) DESC;";
		
		selectSql += tmp;
		
		return selectSql;
	}
}