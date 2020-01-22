import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class MySQLAccess {
	public Support sup = new Support();
	public Confidence conf = new Confidence();
	public LinkedList<SupportMatrix> list = new LinkedList<SupportMatrix>();
	public LinkedList<String> clonelist = new LinkedList<String>();
	public LinkedList<Double> valori = new LinkedList<Double>();
	public LinkedList<Double> valoriconf = new LinkedList<Double>();
	public LinkedList<ConfidenceMatrix> listconf = new LinkedList<ConfidenceMatrix>();
	public LinkedList<String> risultato = new LinkedList<String>();
	public LinkedList<String> risultatoconf = new LinkedList<String>();
	public LinkedList<String> resultsupconf = new LinkedList<String>();
	  
	public MySQLAccess() {
	}
	
	public void readDataBase(String measure, String pre, String post, String event, String output, double valuesup, double valueconf){
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
			connection.setSchema("tefd");
			String schema = connection.getSchema();
			 
			System.out.println("Successful connection - Schema: " + schema);
			System.out.println("=========================================");
			 
			String selectSql = "SELECT measure, trend_pre, event, trend_post FROM atear_divided_results ORDER BY id ASC LIMIT 1000";
			
			//System.out.println(selectSql);
			
			try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(selectSql)
			) {

                // Print results from select statement
                //System.out.println("Results");
                while (resultSet.next())
                {
                   /* System.out.println(resultSet.getString(1) + " "
                        + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4));*/
                	//System.out.println(resultSet.getString(1).toString() + " " + resultSet.getString(2).toString() + " " + resultSet.getString(3).toString() + " " + resultSet.getString(4).toString());
                	int index = 0;
                	if (output.equals("measure")) index = 1;
                	if (output.equals("pre")) index = 2;
                	if (output.equals("post")) index = 4;
                	if (output.equals("event")) index = 3;
                	//System.out.println("Index = " + index);
                	
        			//SUPPORT
            		if(valuesup != -1) {
            			int mod = -1;
            			for (int i = 0; i < list.size(); i++) {
	                		if (resultSet.getString(index).equals(list.get(i).output)) {
	               				mod = i;
	               				break;
	               			}
            			}
            			if(mod > -1) 
            				list.set(mod, new SupportMatrix(resultSet.getString(index), list.get(mod).count+1));
            			else
	               			list.add(list.size(), new SupportMatrix(resultSet.getString(index), 1));
            				
            			if(list.size() == 0)
            				list.add(0, new SupportMatrix(resultSet.getString(index), 1));
            				
        	            /*for(int i = 0; i < list.size(); i++) {
        	            	if (index == 1)
        	            		valori.add(sup.support(list.get(i).output, pre, post, event, list.get(i).count));
        	            	if (index == 2)
        	            		valori.add(sup.support(measure, list.get(i).output, post, event, list.get(i).count));
        	            	if (index == 3)
        	            		valori.add(sup.support(measure, pre, post, list.get(i).output, list.get(i).count));
        	            	if (index == 4)
        	            		valori.add(sup.support(measure, pre, list.get(i).output, event, list.get(i).count));
        	            	//System.out.println(valori);
        	             }
        	                
        	             for(int i = 0; i < valori.size(); i++) {
        	               	if(valori.get(i) == valuesup) {
        	               		System.out.println("> UPDATE");
        	               		risultato.add(list.get(i).output);  
        	               	}
        	            }*/
           			}
            		
            		//CONFIDENCE
            		if(valueconf != -1) {
            			int mod = -1;
                		for (int i = 0; i < listconf.size(); i++) {
                			if (resultSet.getString(index).equals(listconf.get(i).output)) {
                				mod = i;
                				break;
                			}
                		}
                		if (mod > -1) 
                			listconf.set(mod, new ConfidenceMatrix(resultSet.getString(index), listconf.get(mod).count+1));
                		else
                			listconf.add(listconf.size(), new ConfidenceMatrix(resultSet.getString(index), 1));
                		
                		if(listconf.size() == 0)
                			listconf.add(0, new ConfidenceMatrix(resultSet.getString(index), 1));
                		
                		/*for(int i = 0; i < listconf.size(); i++) {
                			if(index == 1)
                				valoriconf.add(conf.confidence(list.get(i).output, pre, post, event, list.get(i).count));
                			if(index == 2)
                				valoriconf.add(conf.confidence(measure, list.get(i).output, post, event, list.get(i).count));
                			if(index == 3)
                				valoriconf.add(conf.confidence(measure, pre, post, list.get(i).output, list.get(i).count));
                			if(index == 4)
                				valoriconf.add(conf.confidence(measure, pre, list.get(i).output, event, list.get(i).count));	
                			
                		}
    	                
    	                for(int i = 0; i < valoriconf.size(); i++) {
    	                	if(valoriconf.get(i) == valueconf) {
    	                		risultatoconf.add(listconf.get(i).output);
    	                		System.out.println("> ADD");
    	                	}
    	                }*/
            		}
            		
            		//Confidence+Support
            		//Probabile errore di java heap. Bisogna modificare la lista su cui si sta ciclando ma al difuori del ciclo.
            		//Non posso aggiungere un elemento ad una lista su cui sto ciclando
	                /*for(int i = 0; i < risultato.size(); i++) {
	            		clonelist = (LinkedList) resultsupconf.clone();
	                	for(int j = 0; j < clonelist.size(); i++) {
	                		if(risultato.get(i) == clonelist.get(j)) {
	                			System.out.println("> UPDATE");
	                			resultsupconf.add(risultato.get(i));
	                		}	
	                	}
	                }*/
                }
            
            	int index = 0;
            	if (output.equals("measure")) index = 1;
            	if (output.equals("pre")) index = 2;
            	if (output.equals("post")) index = 4;
            	if (output.equals("event")) index = 3;
            	//System.out.println("Index = " + index);
                
                for(int i = 0; i < list.size(); i++) {
                	//System.out.println(list.get(i).output);
	            	if (index == 1)
	            		valori.add(sup.support(list.get(i).output, pre, post, event, list.get(i).count));
	            	if (index == 2)
	            		valori.add(sup.support(measure, list.get(i).output, post, event, list.get(i).count));
	            	if (index == 3)
	            		valori.add(sup.support(measure, pre, post, list.get(i).output, list.get(i).count));
	            	if (index == 4)
	            		valori.add(sup.support(measure, pre, list.get(i).output, event, list.get(i).count));
	             }
	                
	             for(int i = 0; i < valori.size(); i++) {
	               	if(valori.get(i) >= valuesup) {
	               		//System.out.println("> UPDATE");
	               		risultato.add(list.get(i).output);  
	               	}
	             }
	             
	             for(int i = 0; i < listconf.size(); i++) {
         			if(index == 1)
         				valoriconf.add(conf.confidence(list.get(i).output, pre, post, event, list.get(i).count));
         			if(index == 2)
         				valoriconf.add(conf.confidence(measure, list.get(i).output, post, event, list.get(i).count));
         			if(index == 3)
         				valoriconf.add(conf.confidence(measure, pre, post, list.get(i).output, list.get(i).count));
         			if(index == 4)
         				valoriconf.add(conf.confidence(measure, pre, list.get(i).output, event, list.get(i).count));	
         			
         		}
	                
                for(int i = 0; i < valoriconf.size(); i++) {
                	if(valoriconf.get(i) >= valueconf) {
                		risultatoconf.add(listconf.get(i).output);
                		//System.out.println("> ADD");
                	}
                }
                
                for(int i = 0; i < risultato.size(); i++) {
                	for(int j = 0; j < risultatoconf.size(); j++) {
	            		if(risultato.get(i) == risultatoconf.get(j)) {
	            			resultsupconf.add(risultato.get(i));
            		}	
            	}
            }
                
            connection.close();
            
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
