import java.util.LinkedList;
import java.util.List;

/*
 * Questa classe che presenta una riga di una tabella. Viene utilizzata per eseguire operazioni sul risultato della query.
 * 
 */

public class Righe {

	public List<String> riga;
	
	/*
	 * Costruttore vuoto per la classe Righe
	 */
	public Righe() {
		riga = new LinkedList<String>();
	}
	
	/*
	 * Costruttore che aspetta in input una stringa s. Questa viene aggiunta subito alla lista della classe che rappresenta la righa.
	 *
	 */
	public Righe(String s) {
		riga = new LinkedList<String>();
		this.set(s);
	}
	/*
	 * Prende in input una stringa s e l'aggiunge alla lista
	 */
	public void set(String s) {
		riga.add(s);
	}
	/*
	 * Ritorna la lista riga
	 */
	public List<String> get() {
		return riga;
	}
	/*
	 * Ritorna una stringa che rappresenta il contenuto della lista riga
	 */
	@Override
	public String toString() {
		String output = "";
		
		for(int i = 0; i < riga.size(); i++) {
			output += " " + riga.get(i);
			
		}
		
		return output;
	}
	
	/*
	 * Aspetta in input un valore
	 * row: lista di righe
	 * Ritorna in output un valore booleano
	 * false se esiste nella lista row un elemento uguale alla list<String> riga
	 * true altrimenti
	 */
	public boolean equalsRow(List<String> row) {
		int k =0;
		for (String s : riga) {
			if (!s.toString().equalsIgnoreCase(riga.get(k).toString())) 
				return false;
			k = k+1;
		}
		return true;
	}
}
