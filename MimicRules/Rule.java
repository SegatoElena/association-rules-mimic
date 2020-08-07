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
    
    public Rule() {
    	xList = new LinkedList<Righe>();
	    yList = new LinkedList<Righe>();
	    countList = new LinkedList<CountY>();
	    xParameters = new LinkedList<String>();
	    columnNumber = 3;
	    numberRow = 0;
    }
    
	/*
	 * Funzione che controlla che la colonna di output(yColumn) non sia utilizzata anche come valore di input.
	 * columnNumber: 		numero di colonne della tabella
	 * yColumn: 			nome del valore che vogliamo in output
	 * x: 					lista che contiene i nomi delle colonne da usare come input
	 * 
	 * return: 				true se l'elemento yColumn è presente nella lista x
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
		
		//CONFIDENCE
		//Controllo quante volte le mie X si presentano in combinazione con l'elemento Y
		//Salvo in una lista countList nella posizione index tutte le combinazioni XY e il numero di volte in cui si ripete questa combianzione
		
		
		
		//int countX = 0;
		//n*costante
		//for (int l = 0; l < xList.size(); l++) {
		//	if (xList.get(l).equalsRow(xParameters)) {
		//		countX++;
		//		List<String> s = yList.get(l).get();
		//		int index = countList.indexOf(new CountY(s.get(0).toString(), 0));
		//		if (index != -1)
		//			countList.get(index).count++;
		//		else
		//			countList.add(new CountY(s.get(0).toString(), 1));						
		//	}
		//}
		
		
		//Richiamo il metodo rules della classe CountY per calcolare i valori di conficende e di support per ogni combinazione XY trovata precedentemente
		for (int j = 0; j < countList.size(); j++) {
			//countList.get(j).rules(numberRow, countX);
			/*System.out.println(countList.get(j).toString());*/
			
		}
	}
	
	public void PrintResults(ResultSet resultSet, int index) throws SQLException {
		while(resultSet.next()) {
			System.out.println(xList.get(index).toString() + " -> " + resultSet.getString(1) + " | "
				+ (Integer.parseInt(yList.get(0).toString().replace(" ", "")) / (float)numberRow ) * 100.0 + "%" 
				+ " | " + (Integer.parseInt(resultSet.getString(2)) / (float)(Integer.parseInt(yList.get(0).toString().replace(" ", "")))) * 100.0 + "%" );
			//CONTROLALRE SE VA NUMBER OF ROW O NUMERO DI X
		}
	}
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
		/*
		 * measure, trend

            SELECT yColumn, count(*)

            FROM tefd.tep_divided_results

            WHERE x[0] = {variabile1 della query precedente} AND x[1] = {variabile2 della query precedente}

            GROUP BY yColumn

            HAVING COUNT(*) > (totaleEventiConQuellAntecedente*minConfidence)

            


		 */
		return selectSql;
	}
	
	public String QueryGenerator(String[] x) {
		String selectSql = "SELECT "; 
		selectSql += Arrays.toString(x).replace("[", "").replace("]", "") + ", COUNT(*)";	
		selectSql += " FROM tefd.atear_divided_results GROUP BY " + Arrays.toString(x).replace("[", "").replace("]", "")	
				+ " HAVING COUNT(*) > (" + this.numberRow*0.01 + ");";
		        
/*
        SELECT x[0] , x[1]  {fallo dinamicio, count(*)
        FROM tefd.atear_divided_results
        GROUP BY   x[0] , x[1]
        HAVING COUNT(*) > (total*minSupport)
*/

		return selectSql;
	}
}