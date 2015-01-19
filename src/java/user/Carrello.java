package user;
import java.sql.*;
import java.util.*;


import pizze.PizzaPrenotazione;

public class Carrello {
	double totale;
	String carrello;
	String carrellino;
	ArrayList<PizzaPrenotazione> caret;
	
	public Carrello(){
		caret = new ArrayList<>();
		totale=0;
		carrellino = "<h3><a href=\"Gestore?action=carrello\">Nel tuo carrello:</a></h3><br><br>Totale: "+totale+"<br>";
		carrello="Il tuo carrello è vuoto: sfoglia il catalogo!<br>";
		carrello+="<form><input type=\"hidden\" name=\"action\" value=\"cerca\">";
		carrello+="<input type=\"submit\" value=\"Torna al catalogo\"></form>";
	}
	public void setTotale(double t){
		totale += t;
		totale = Math.rint(totale*100)/100;
	}
	public double getTotale(){
		return totale;
	}
	public void setCarrello(String s){
		carrello = s;
	}
	public String getCarrello(){
		return carrello;
	}
	public void setCarrellino(String s){
		carrellino = s;
	}
	public String getCarrellino(){
		return carrellino;
	}
	public boolean isEmpty(){
		return caret.isEmpty();
	}
	public ArrayList<PizzaPrenotazione> getCaret(){
		return caret;
	}
	
	public void buildHtml(ArrayList<PizzaPrenotazione> res){
		if(res.isEmpty() || res==null){
			totale=0;
			
			carrello="Il tuo carrello è vuoto: sfoglia il catalogo!<br>";
			carrello+="<form><input type=\"hidden\" name=\"action\" value=\"cerca\">";
			carrello+="<input type=\"submit\" value=\"Torna al catalogo\"></form>";
			
		}else{
			carrello="";
			for(int i=0; i<res.size(); i++){
				carrello += "<div class=pizze><form name=\"rimuoviDalCarrello\" action=\"Gestore\" onSubmit=\"verificaQuantita(this)\" method=\"post\"><table border=\"0\"><tr><td>";
				carrello += "<img src=\"img/"+res.get(i).getNome()+".jpg\" align=\"left\" heigth=\"200\" width=\"200\"/></td><td>";
				carrello += "<h1>"+res.get(i).getNome()+"</h1>";
				carrello += ", <font size=\"4\">EUR "+res.get(i).getPrezzo()+"</font><br><br>";
				carrello += "<font size=\"2\" color=\"grey\">"+res.get(i).getDescrizione()+"</font><br>";
				carrello += "<h4>COD: "+res.get(i).getCOD()+"</h4></p>";
				carrello += "Quantit�<input type=\"text\" name=\"quantita\" size=\"3\" value=\""+res.get(i).getQuantita()+"\"/>";
				carrello +="<input type=\"hidden\" name=\"action\" value=\"rimuoviDalCarrello\">";
				carrello +="<input type=\"hidden\" name=\"cod\" value=\""+res.get(i).getCOD()+"\">";
                                carrello +="<input type=\"hidden\" name=\"quantita\" value=\""+res.get(i).getQuantita()+"\">";
				carrello +="<input type=\"submit\" value=\"Rimuovi dal carrello\"></td></table></div></form><br><br>";
							
			}
			carrello += "<br><form name=\"ordine\" action=\"Gestore\" method=\"post\">";
			carrello += "<input type=\"hidden\" name=\"action\" value=\"buy\">";
			carrello += "<input type=\"submit\" value=\"Effettua ordine\"></form>";
			carrello += "<br><form name=\"ordine\" action=\"Gestore\" method=\"post\">";
			carrello += "<input type=\"hidden\" name=\"action\" value=\"svuota\">";
			carrello += "<input type=\"submit\" value=\"Svuota carrello\"></form>";

		}
		costruisciCarrellino(res);
	}
	
	public void costruisciCarrellino(ArrayList<PizzaPrenotazione> res){
		carrellino = "<h3><a href=\"Gestore?action=carrello\">Nel tuo carrello:</a></h3>";
		for(int i=0; i<res.size(); i++){
			carrellino += "<br>"+res.get(i).getNome();
		}
		carrellino += "<br><br>";
		carrellino += "Totale: "+totale;
	}
	
	public boolean isInto(String t){
		for(int i=0; i<caret.size(); i++) 
			if(caret.get(i).getNome().equals(t)) return true;
		return false;
	}
	
	public boolean rimuovi(String COD, int quantita){
		
			for(int i=0; i<caret.size(); i++){
					if(caret.get(i).getCOD().equals(COD) && quantita<=caret.get(i).getQuantita()){
						setTotale(-(caret.get(i).getPrezzo()*quantita));
						caret.get(i).setQuantita(-quantita);
						if(caret.get(i).getQuantita()==0)
							caret.remove(i);
						buildHtml(caret);
						return true;
					}
				}
			
			return false;		
	}
	
	public void svuota(){
		caret = new ArrayList<PizzaPrenotazione>();
		totale = 0;
		buildHtml(caret);
	}
	
	
	public boolean aggiungi(String COD, int quantita){
		
           try{
			boolean found = false;
		    String url = "jdbc:derby://localhost:1527/Pizze";
		    String user="fabio";
		    String pass="fabio";
		    Connection conn = DriverManager.getConnection(url, user, pass);
		    Statement st = conn.createStatement();
		    ResultSet rs;
		    rs = st.executeQuery("SELECT * FROM PIZZE WHERE COD='"+COD+"'");
		    
		    		while(rs.next()) {
		    			for(int i=0; i<caret.size(); i++){
		    				if(COD.equals(caret.get(i).getCOD())){
		    					setTotale(caret.get(i).getPrezzo()*quantita);
		    					caret.get(i).setQuantita(quantita);
		    					found=true;
		    				}
		    			}
		    			if(!found){
		    			found=true;
		    			String nome = rs.getString("NOME");
		    			String imm = rs.getString("IMMAGINE");
                                        String cod = rs.getString("COD");
		    			double prezzo = rs.getDouble("PREZZO");
		    			String descrizione = rs.getString("DESCRIZIONE");
		    			setTotale(prezzo*quantita);
		    			caret.add(new PizzaPrenotazione(nome, imm, cod, prezzo, descrizione, quantita));
		    			}
		    		}		    	
		    
		    buildHtml(caret);
		    st.close(); conn.close();
		    return found;
		    }
                    catch(SQLException e){ 
		    	System.out.println(e.getMessage());
		    	return false;
		    }
                  
	}
	
	public void compra(String username){
		try{
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
		    String url = "jdbc:derby://localhost:1527/Pizze";
		    String user="fabio";
		    String pass="fabio";
		    Connection conn = DriverManager.getConnection(url, user, pass);
		    Statement st = conn.createStatement();
		    Calendar cal = new GregorianCalendar();
		    int giorno = cal.get(Calendar.DAY_OF_MONTH);
		    int mese = cal.get(Calendar.MONTH);
		    int anno = cal.get(Calendar.YEAR);
		    int ora = cal.get(Calendar.HOUR_OF_DAY);
		    int min = cal.get(Calendar.MINUTE);
		    String date = giorno+"/"+mese+"/"+anno+", "+ora+":"+min;
		    st.executeUpdate("INSERT INTO PRENOTAZIONI (USERNAME, ORDINE, DATA, TOTALE, EVASO) VALUES ('"+username+"', '"+toString()+"', '"+date+"', "+totale+", 0)"); 
		    //ogni nuova prenotazione viene settata a 0, non evasa
		    st.close();
		    conn.close();
		    }catch(SQLException e){ System.out.println(e.getMessage());}
	}
	
	public String toString(){
		String out="";
		for(int i=0; i<caret.size(); i++)
			out+=caret.get(i).toString();
			
		return out;
	}

	
}
