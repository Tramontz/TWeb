package user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import pizze.PizzaPrenotazione;

public class Prenotazione {
	String prenotazione;
	
	public Prenotazione(){
		prenotazione="";
	}
	
	public void setPrenotazione(String p){
		prenotazione = p;
	}
	public String getPrenotazione(){
		return prenotazione;
	}
	
	public String data(){
		Calendar cal = new GregorianCalendar();
	    int giorno = cal.get(Calendar.DAY_OF_MONTH);
	    int mese = cal.get(Calendar.MONTH);
	    int anno = cal.get(Calendar.YEAR);
	    int ora = cal.get(Calendar.HOUR_OF_DAY);
	    int min = cal.get(Calendar.MINUTE);
	    return (giorno+"/"+mese+"/"+anno+", "+ora+"."+min);
	}
	
	
	
	/*
	 *  0 = non evaso
	 *  1 = evaso
	 */
	public boolean aggiungiPrenotazione(Carrello c, String username){
			try{
				String url = "jdbc:derby://localhost:1527/Pizze";
				String user="fabio";
				String pwd="fabio";
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			Connection conn = DriverManager.getConnection(url,user,pwd);
		
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO PRENOTAZIONI (USERNAME, DATA, TOTALE, EVASO) VALUES ('"+username+"', '"+data()+"', "+c.getTotale()+", 0)");
			ResultSet rs = st.executeQuery("SELECT MAX(COD) AS CODICE FROM PRENOTAZIONI WHERE USERNAME='"+username+"'");
			int cod = 0;
			while(rs.next())
				cod = rs.getInt("CODICE");
			rs.close();
			for(int i=0; i<c.getCaret().size(); i++){
				PizzaPrenotazione lP = c.getCaret().get(i);				
					st.executeUpdate("INSERT INTO PRENOTAZIONI_PIZZE (COD, NOME, PREZZO, QUANTITA) VALUES ("+cod+", '"+lP.getNome()+"', "+lP.getPrezzo()+", "+lP.getQuantita()+" )");
			}
			st.close();
			conn.close();
			c.svuota();
			visualizzaPrenotazioni(username);
			return true;
		} catch (SQLException e) {System.out.println("IN AGGIUNGI: "+e.getMessage());}
		
		return false;
	}
	
	public boolean cancellaOrdine(String username, String cod){
		try{
		    String url = "jdbc:derby://localhost:1527/Pizze";
		    String user="fabio";
		    String pass="fabio";
		    Connection conn = DriverManager.getConnection(url, user, pass);
		    Statement st = conn.createStatement();
		    st.executeUpdate("DELETE FROM PRENOTAZIONI_PIZZE WHERE COD="+cod);
		    st.close();
		    Statement st2 = conn.createStatement();
		    st2.executeUpdate("DELETE FROM PRENOTAZIONI WHERE COD="+cod);
		    st2.close();
		    conn.close();
		    	
		    visualizzaPrenotazioni(username);
		    return true;
		}catch(SQLException e){ System.out.println(e.getMessage());}
		return false;
	}
	
	
	
	public void visualizzaPrenotazione(String username){
		System.out.println("siamo arrivati a visualizzaprenotazioni");
           visualizzaPrenotazioni(username);
	}
	
//	public void buildHtml(String username){
//		try{
//			String url = "jdbc:derby://localhost:1527/sample";
//			String user="siff";
//			String pwd="siff";
//			//DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
//			Connection conn = DriverManager.getConnection(url,user,pwd);
//			Statement st = conn.createStatement();
//			String prenotazione1="Le tue prenotazioni, "+username+":<br><br>";
//			String prenotazione2="";
//			int pre_cod = -1;
//			ResultSet rsPre = st.executeQuery("SELECT PRENOTAZIONI.COD, DATA, QUANTITA, ISBN, EVASO FROM PRENOTAZIONI INNER JOIN PRENOTAZIONILIBRI ON PRENOTAZIONI.COD=PRENOTAZIONILIBRI.COD AND PRENOTAZIONI.USERNAME='"+username+"'");
//			while(rsPre.next()){
//				int actual_cod = rsPre.getInt("COD");
//				if(rsPre.getInt("EVASO")==0){
//					if(actual_cod!=pre_cod){
//						prenotazione1+="</ul>";
//						if(pre_cod!=(-1) && rsPre.getInt("EVASO")==0) {
//							prenotazione1+="</div><form>"; //ho il pre_cod!
//							prenotazione1+="<input type=\"hidden\" name=\"action\" value=\"cancellaOrdine\">";
//							prenotazione1+="<input type=\"hidden\" name=\"cod\" value=\""+pre_cod+"\">";
//							prenotazione1+="<input type=\"submit\" value=\"Cancella Ordine\">";
//							prenotazione1+="</form><hr>";
//						}
//						prenotazione1+="<div id=\"prenotazione\"><br><br>Prenotazione numero <font size=\"4\">"+actual_cod+"</font> del "+rsPre.getString("DATA");
//						prenotazione1+="<ul>";
//						pre_cod=actual_cod;
//					}
//					prenotazione1 += HTMLLibro(rsPre.getInt("QUANTITA"), rsPre.getString("ISBN"), conn);
//				}else{
//					if(actual_cod!=pre_cod){
//						prenotazione2+="</div></ul><br><br><hr>";
//						prenotazione2+="<div id=\"prenotazione\">Prenotazione numero <font size=\"4\">"+actual_cod+"</font> del "+rsPre.getString("DATA");
//						prenotazione2+="<ul>";
//						pre_cod=actual_cod;
//					}
//					prenotazione2 += HTMLLibro(rsPre.getInt("QUANTITA"), rsPre.getString("ISBN"), conn);
//				}
//			}
//			if(pre_cod!=(-1)) {
//				prenotazione1+="</ul><form>"; //ho il pre_cod!
//				prenotazione1+="<input type=\"hidden\" name=\"action\" value=\"cancellaOrdine\">";
//				prenotazione1+="<input type=\"hidden\" name=\"cod\" value=\""+pre_cod+"\">";
//				prenotazione1+="<input type=\"submit\" value=\"Cancella Ordine\">";
//				prenotazione1+="</form><hr>";
//			}else{
//				prenotazione = "Non risultano prenotazioni registrate.<br>";
//			}
//			prenotazione = prenotazione1;
//			prenotazione += prenotazione2;
//			rsPre.close();
//			st.close();
//			conn.close();
//		} catch (SQLException e) {System.out.println("IN BUILD: "+e.getMessage());}
//	}
	
	public String HTMLPizza(int quantita, String NOME, Connection conn){
		String out = "";
		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE NOME='"+NOME+"'");
			while(rs.next())
				out+="<li>"+quantita+" - "+rs.getString("NOME")+", EUR "+rs.getDouble("PREZZO")+"</li>";
			
			rs.close();
			st.close();
			return out;
		} catch (SQLException e) {System.out.println("IN BUILD: "+e.getMessage());}
		return out;
	}
	
	public String HtmlPrenotazione(int cod, Connection conn){
		String res = "";
		try{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM PRENOTAZIONI_PIZZE WHERE COD="+cod);
			while(rs.next()){
				res +=HTMLPizza(rs.getInt("QUANTITA"), rs.getString("NOME"), conn);
			}
			rs.close();
			st.close();
			return res;
		}catch (SQLException e) {System.out.println("IN BUILD: "+e.getMessage());}
		return res;
	}
	
	public void visualizzaPrenotazioni(String username){
		try{
			String url = "jdbc:derby://localhost:1527/Pizze";
			String user="fabio";
			String pwd="fabio";
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			Connection conn = DriverManager.getConnection(url,user,pwd);
			Statement st = conn.createStatement();
			String prenotazione1="Le tue prenotazioni, "+username+":<br><br>";
			String prenotazione2="";
			ResultSet rs = st.executeQuery("SELECT * FROM PRENOTAZIONI WHERE USERNAME='"+username+"'");
			while(rs.next()){
				int cod = rs.getInt("COD");
				if((rs.getInt("EVASO"))==0){ //non evaso
					prenotazione1 += "<div class=\"prenotazione\"><br>";
					prenotazione1 += "Prenotazione numero <font size=\"4\">"+cod+"</font> del "+rs.getString("DATA");
					prenotazione1 += "<ul>";
					prenotazione1 += HtmlPrenotazione(cod, conn);
					prenotazione1 += "</ul>";
					prenotazione1+="<form><input type=\"hidden\" name=\"action\" value=\"cancellaOrdine\">";
					prenotazione1+="<input type=\"hidden\" name=\"cod\" value=\""+cod+"\">";
					prenotazione1+="<input type=\"submit\" value=\"Cancella Ordine\"></form><br><br>";
					prenotazione1+="</div>";					
				}else{ //evaso
					prenotazione2 += "<div class=\"prenotazione\"><br>";
					prenotazione2 += "Prenotazione numero <font size=\"4\">"+cod+"</font> del "+rs.getString("DATA");
					prenotazione2 += "<ul>";
					prenotazione2 += HtmlPrenotazione(cod, conn);
					prenotazione2 += "</ul>";
					prenotazione2 +="<br>Ordine evaso<br><br>";
					prenotazione2 +="</div>";					
				}
			}
			prenotazione = prenotazione1;
			prenotazione += prenotazione2;
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {System.out.println("IN BUILD: "+e.getMessage());}
	}
}
