package pizze;

import java.util.*;
import java.sql.*;

public class PaginaPizza {

    String risultato;

    public PaginaPizza() {
        risultato = "";
    }

    public void setRisultato(String s) {
        risultato = s;
    }

    public String getRisultato() {
        return risultato;
    }

    public void visualizzaModificaPizza(String cod) {

        try {
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE COD='"+cod+"'");
            while (rs.next()) {
                    risultato = "<img src=\"img/" + rs.getString("NOME") + ".jpg\" align=\"left\" heigth=\"350\" width=\"350\"/ alt=\"Immagine non trovata.\">";
                    risultato = "<form name=\"modificaPizza\" action=\"Gestore\" onSubmit=\"return verificaInput(this);\" method=\"post\">";
                    risultato += "<table>";
                    risultato += "<tr><td>COD:</td><td><input type=\"text\" name=\"cod\" value=\""+cod+"\" size=\"14\"></td>";
                    risultato +="</tr>";
                    risultato += "<tr><td>Nome:</td><td><input type=\"text\" name=\"nome\" value=\""+rs.getString("NOME")+"\" size=\"30\"></td></tr>";                    
                    risultato += "<tr><td>Descrizione:</td><td><input type=\"text\" name=\"descrizione\" value=\""+rs.getString("DESCRIZIONE")+"\"></td></tr>";
                    risultato += "<tr><td>Prezzo:</td><td><input type=\"text\" size=\"20\" name=\"prezzo\" value=\""+rs.getDouble("PREZZO")+"\"></td></tr>";
                    risultato += "<tr><td>Immagine:</td><td><input type=\"text\" size=\"20\" name=\"immagine\" value=\""+rs.getString("IMMAGINE")+"\"></td></tr>";
                    risultato += "</table>";
                    risultato += "<input type=\"hidden\" name=\"action\" value=\"modificaPizza\">";                    
                    risultato += "<input type=\"submit\" value=\"Modifica\" method=\"post\">";
                    risultato += "</form>";
                    risultato += "<form>";                                                          
                    risultato += "<input type=\"hidden\" name=\"action\" value=\"visualizzaPizza\">";
                    risultato += "<input type=\"hidden\" name=\"cod\" value=\""+cod+"\">";    
                    risultato += "<input type=\"submit\" value=\"Annulla\">";
                    risultato += "</form>";
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public void modificaPizza(String cod, String nome, String descrizione, double prezzo, String immagine) {
       System.out.println(cod+nome+descrizione+prezzo+immagine);
        try {
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            String query = "UPDATE PIZZE SET NOME='"+nome+"', ";
            query += "DESCRIZIONE='"+descrizione+"', PREZZO="+prezzo+", IMMAGINE='"+immagine+"' WHERE COD='"+cod+"'";
            st.executeUpdate(query);
            st.close();
            conn.close();
            visualizzaPizza(cod); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void visualizzaPizza(String cod){
        
    try {
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE COD='"+cod+"'");
            while (rs.next()) {
                risultato = "<form name=\"aggiungiAlCarrello\" action=\"Gestore\" onSubmit=\"return verificaQuantita(this)\" method=\"post\"><table border=\"0\"><tr><td>";
                risultato += "<img src=\"img/"+rs.getString("IMMAGINE")+"\" align=\"left\" heigth=\"300\" width=\"300\"/ alt=\"Immagine non trovata.\"></td><td>";
                risultato += "<h1>" + rs.getString("NOME")+"</h1><br><br>";
                risultato += "<h2>EUR "+rs.getDouble("PREZZO")+"</h2><br><br>";
                risultato += "<h3>COD: "+cod+"</h3></p>";
                risultato += "Quantità<input type=\"text\" name=\"quantita\" size=\"3\" value=\"1\"/>";
                risultato += "<input type=\"hidden\" name=\"action\" value=\"aggiungiAlCarrello\">";
                risultato += "<input type=\"hidden\" name=\"isbn\" value=\""+cod+"\">";
                risultato += "<input name=\"invia il modulo\" type=\"image\" src=\"img/aggiungiCarrello.png\" value=\"Aggiungi al carrello\"></td></table></form>";
                risultato += "<br><div id=\"descrizionepizza\">"+rs.getString("DESCRIZIONE")+"</div>";
            }
            risultato += "<br>Modifica effettuata con successo";
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }    
    }
}

