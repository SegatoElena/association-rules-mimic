import javax.swing.JOptionPane;

public class main {
	public static void main(String[] args) {
    MySQLAccess dao = new MySQLAccess();
    
    //while(true) {
	    /*String measure = JOptionPane.showInputDialog("Inserire measure");
	    String pre = JOptionPane.showInputDialog("Inserire pre");
	    String post = JOptionPane.showInputDialog("Inserire post");
	    String event = JOptionPane.showInputDialog("Inserire event");
	    String output = JOptionPane.showInputDialog("Inserire output desiderato");
	    double support = Double.parseDouble(JOptionPane.showInputDialog("Inserire valore support"));
	    double confidence = Double.parseDouble(JOptionPane.showInputDialog("Inserire valore confidence"));
	    */
    	
    	String measure = "Respiratory Rate";
    	String pre = "STEADY";
    	String post = "STEADY";
    	String event = "";
    	String output = "event";
    	double support = 0.00018;
    	double confidence = 0.00018;
    	
    	System.out.println("> START");
	    dao.readDataBase(measure, pre, post, event, output, support, confidence);
	    
	    String res = "";
	    String resconf = "";
	    String ressup = "";
	    
	    if(support != -1 && confidence != -1) {
		    for(int i = 0; i < dao.resultsupconf.size(); i++) {
		    	res += " " + dao.resultsupconf.get(i).toString();
		    }
	    	if(dao.resultsupconf.size() == 0) JOptionPane.showMessageDialog(null, "Nessun risultato");
	    	else JOptionPane.showMessageDialog(null, res);
	    }else if(support != -1) {
		    for(int i = 0; i < dao.risultato.size(); i++) {
		    	ressup += " " + dao.risultato.get(i).toString();
		    }
	    	if(dao.risultato.size() == 0) JOptionPane.showMessageDialog(null, "Nessun risultato");
	    	JOptionPane.showMessageDialog(null, ressup);
	    }else if(confidence != -1) {
		    for(int i = 0; i < dao.risultatoconf.size(); i++) {
		    	resconf += " " + dao.risultatoconf.get(i).toString();
		    }
	    	if(dao.risultatoconf.size() == 0) JOptionPane.showMessageDialog(null, "Nessun risultato");
	    	JOptionPane.showMessageDialog(null, resconf);
	    }
	   
    }
 // }
}