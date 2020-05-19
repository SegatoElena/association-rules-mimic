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
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class Rule {
	/*
	 * Funzione che controlla che la colonna di output(yColumn) non sia utilizzata anche come valore di input.
	 * columnNumber: 		numero di colonne della tabella
	 * yColumn: 			nome del valore che vogliamo in output
	 * x: 					lista che contiene i nomi delle colonne da usare come input
	 * 
	 * return: 				true se l'elemento yColumn è presente nella lista x
	 * 						false altrimenti
	 */
	public static boolean checkElement(int columnNumber, String yColumn,String[] x) {
		for(int i = 0; i < columnNumber-1; i++) {
	    	if(yColumn.equals(x[i])) {
	    		return true;
	    	}
	    }
		return false;
	}
	
	/* Funzione main: funzione principale che esegue tutti i calcoli principali.
	 * 
	 */
	public static void main(String[] args) {
		
	    int columnNumber = 3;
	    List<Righe> xList = new LinkedList<Righe>();
	    List<Righe> yList = new LinkedList<Righe>();
	    List<CountY> countList = new LinkedList<CountY>();
	    
	    int numberRow = 0;
	    Boolean check = false;
	    String[] x = new String[2];
	    x[0] = "measure";
	    x[1] = "trend_pre";
	    
	    String yColumn = "event";
	    
	    checkElement(columnNumber, yColumn, x);
	    
	    // Se i parametri scelti come input e output sono adeguati e non presentano uguaglianze, allora viene eseguito il calcolo.
		if (!check) {
			List<String> xParameters = new LinkedList<String>();
			
			
			xParameters.add("Respiratory Rate");
			xParameters.add("STEADY");
			String yString = "event";
			
			//Connessione con il database per ottenere la tabella contenente tutti i parametri richiesti 
			Connection connection = null;
			try {
				connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
				connection.setSchema("tefd");
				String schema = connection.getSchema();
				
				System.out.println("Successful connection - Schema: " + schema);
				System.out.println("=========================================");
				
				
				//Query per contate il numero di elementi nella tabella sorgente
				try (Statement statement = connection.createStatement();
					ResultSet denominatore = statement.executeQuery("SELECT COUNT(id) AS total FROM tefd.atear_divided_results")	
				) {
					denominatore.next();
				    numberRow = Integer.parseInt(denominatore.getString(1));
				} catch (Exception e) {
		  	         e.printStackTrace();
		  	         System.out.println(e);
		  	         System.err.println(e.getClass().getName() + ": " + e.getMessage());
		  	         System.exit(0);
				}
				
				
				//Costruisco in modo dinamico la query per ottenere tutti i valori della mia X
				String selectSql = "SELECT "; 
				for(int i = 0; i < columnNumber-1; i++) {
					selectSql += x[i];
					if (i != 1) {
						selectSql += ", ";
					} else {
						selectSql += "";
					}
				}				
				selectSql += ", " + yColumn + " FROM tefd.atear_divided_results ORDER BY id ASC";
				
				System.out.println(selectSql);
				
				//Esegue la query e popolo le liste xList e yList che contengono ripettivamente i parametri della X e i parametri della Y
				try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(selectSql)
				) {
					System.out.println("> QUERY DONE");
					Righe tmp;
					while (resultSet.next()) {
						tmp = new Righe();
						for(int j = 1; j < columnNumber; j++) 
							tmp.set(resultSet.getString(j));
						
						xList.add(tmp);
						yList.add(new Righe(resultSet.getString(columnNumber)));
					}
					
					
					System.out.println("> LIST'S CREATED");
					
					//CONFIDENCE
					//Controllo quante volte le mie X si presentano in combinazione con l'elemento Y
					//Salvo in una lista countList nella posizione index tutte le combinazioni XY e il numero di volte in cui si ripete questa combianzione
					
					int countX = 0;
					//n*costante
					for (int l = 0; l < xList.size(); l++) {
						if (xList.get(l).equalsRow(xParameters)) {
							countX++;
							List<String> s = yList.get(l).get();
							int index = countList.indexOf(new CountY(s.get(0).toString(), 0));
							if (index != -1)
								countList.get(index).count++;
							else
								countList.add(new CountY(s.get(0).toString(), 1));						
						}
					}
					
					
					//Richiamo il metodo rules della classe CountY per calcolare i valori di conficende e di support per ogni combinazione XY trovata precedentemente
					for (int j = 0; j < countList.size(); j++) {
						countList.get(j).rules(numberRow, countX);
					}
					
					System.out.println("> CALCULATING X DONE");
					
				} catch (Exception e) {
	            	e.printStackTrace();
	            	System.out.println(e);
	            	System.err.println(e.getClass().getName() + ": " + e.getMessage());
	            	System.exit(0);
				}
			} catch (Exception e) {
	  	         e.printStackTrace();
	  	         System.out.println(e);
	  	         System.err.println(e.getClass().getName() + ": " + e.getMessage());
	  	         System.exit(0);
			}
		}
	}
}