/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pizze;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tramontz
 */
public class Catalogo extends HttpServlet{
	
String risultato;
	
	public Catalogo(){
		//search(null, null, false);
		risultato = "";
	}
	
	public void setRisultato(String s){ 
		risultato = s;
	}
	public String getRisultato(){ 
		return risultato;
	}
	
	public void buildHtml(ArrayList<Pizza> res, boolean admin){
		
		if(!(res.isEmpty())){
			risultato="<br>Nel nostro catalogo:<br><br>";
		for(int i=0; i<res.size(); i++){
			risultato += "<div class=pizze>";
			risultato += "<form name=\"aggiungiAlCarrello\" action=\"Gestore\" onSubmit=\"return verificaQuantita(this)\" method=\"post\">";
			risultato += "<table border=\"0\">";
			risultato += "<tr><td><img src=\"img/"+res.get(i).getImmagine()+"\" align=\"left\" width=\"200\" alt=\"Immagine non trovata.\"></td>";
			risultato += "<h1>"+res.get(i).getNome()+"</h1>";
			if(admin){
				risultato += "<h4><a href=\"Gestore?action=visualizzaModificaPizza&cod="+res.get(i).getCOD()+"\">Modifica</a>   -   ";
				risultato += "<a href=\"Gestore?action=cancellaPizza&cod="+res.get(i).getCOD()+"\" onClick=\"return confermaCancella()\">Cancella</a></h4><br>";
			}
                        if(res.get(i).getDescrizione().length()<=100)
				risultato += "<font size=\"2\" color=\"grey\">"+res.get(i).getDescrizione()+"</font>";
			else
				risultato += "<font size=\"2\" color=\"grey\">"+res.get(i).getDescrizione().substring(0, 100)+"...</font>";
			risultato += ",</h3><h2>EUR "+res.get(i).getPrezzo()+"</h2>";
			
			risultato += "<h3>cod: "+res.get(i).getCOD()+"</h3>";
			risultato += "<table><tr><td>Quantità<input type=\"text\" name=\"quantita\" size=\"3\" value=\"1\"/></td>";
			risultato+="<td><input type=\"hidden\" name=\"action\" value=\"aggiungiAlCarrello\">";
			risultato+="<input type=\"hidden\" name=\"COD\" value=\""+res.get(i).getCOD()+"\">";
                        //risultato+="<input type=\"hidden\" name=\"nome\" value=\""+res.get(i).getNome()+"\">";
                        risultato+="<input type=\"hidden\" name=\"quantita\" value=\"quantita\">";
			risultato += "<input name=\"invia il modulo\" type=\"image\" src=\"img/aggiungiCarrello.png\" width=\"150\" value=\"Aggiungi al carrello\"></td></tr></table>";
			risultato += "</td></tr></table></form></div><br><br>";
						
		}
		    risultato += "</form>";
		}else{
			risultato="Spiacenti, la pizza cercata non è nel nostro catalogo.<br>";
			risultato+="<form name=\"cerca\" action=\"Gestore\" method=\"post\">";
			risultato+="<input type=\"hidden\" name=\"action\" value=\"cerca\">";
			risultato+="<input type=\"submit\" value=\"Torna al catalogo\"></form>";
			
		}
		
	}
	
	public void search(String t, boolean admin){
		try{
			
		    String url = "jdbc:derby://localhost:1527/Pizze";
		    String user="fabio"; 
		    String pass="fabio";
		    
		    DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
		    
		    Connection conn = DriverManager.getConnection(url, user, pass);
		    Statement st = conn.createStatement();
		    ResultSet rs;
		    ArrayList<Pizza> res = new ArrayList<>();
		    if(!(t==null))
		    	//se nome non � null n� vuoto, ma a s�
		    	rs = st.executeQuery("SELECT * FROM PIZZE WHERE LOWER(NOME) LIKE LOWER('%"+t+"%')");
		    else //if((t==null || t.equals("")) && (a==null || a.equals("")))
		    	//che siano vuoti entrambi
		    	rs = st.executeQuery("SELECT * FROM PIZZE");
		    //else
		    	//altrimenti normale ricerca
		    	//rs = st.executeQuery("SELECT * FROM LIBRI WHERE LOWER(NIOM) LIKE LOWER('%"+t+"%') AND LOWER(AUTORE) LIKE LOWER('%"+a+"%') AND DISPONIBILE = 1");
		    
		    while(rs.next()) {
		    	String nome = rs.getString("NOME");
		    	double prezzo = rs.getDouble("PREZZO");
		    	String COD = rs.getString("COD");
		    	String descrizione = rs.getString("DESCRIZIONE");
                        String immagine = rs.getString("IMMAGINE");
		    	res.add(new Pizza(nome, descrizione, COD, prezzo, immagine));
		    }

		    buildHtml(res, admin);
		    st.close(); conn.close();
		    }catch(SQLException e){ System.out.println(e.getMessage());}
	}
		
}

