
/*
 * Classe utilizzata per contare le occorrenze e calcolare la classe support e confidence
 * 
 * parametri:
 * name: nome della Y
 * count: numero di righe che contengono sia i valori di X che di Y
 * support: valore
 * confidence: valore
 */

public class CountY {
	public String name;
	public int count;
	public double support;
	public double confidence;
	
	/*
	 * costruttore vuoto della classe CountY
	 * inizializza i parametri a vuoto ed a zero i parametri della classe
	 */
	public CountY() {
		name = "";
		count = 0;
		support = 0;
		confidence = 0;
	}
	
	/*
	 * costruttore che riceve in input
	 * name: nome della Y
	 * count: numero di volte che il name è presente nella tabella dei risultati
	 */
	public CountY(String name, int count) {
		this.name = name;
		this.count = count;
		support = 0;
	}
	
	/*
	 * Calcola il valore di support e confidence. Riceve in input
	 * den: denominatore, numero totale di righe ottenute della query
	 * countX: numero di righe in cui i valori di X sono presenti nella tabella della query
	 * 
	 * Non ritorna null
	 */
	public void rules(int den, int countX) {
		System.out.println(count);
		support = count / den;
		confidence = count / countX;
	}

	
	/*
	 * ritorno un valore valore hashcode
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/*
	 * Utile per controllare se due nomi sono uguali
	 * ritorno
	 * true se incontra un'uguaglianza
	 * false altrimenti
	 */
	public boolean equals(String name) {
		if (this.name.equalsIgnoreCase(name))
			return true;
		return false;
	}
	
	/*mi serve il to string
	
	@Override
	public String toString() {
		String output = "";
		
		for(int i = 0; i < CountY.size(); i++) {
			output += " " + CountY.get(i);
			
		}
		
		return output;
	}
	*/
}
