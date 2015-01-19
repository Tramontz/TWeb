/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amministrazione;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tramontz
 */
import pizze.Pizza;

public class Amministrazione {

    String amministrazione;

    public Amministrazione() {
        amministrazione = "";
    }

    public String getAmministrazione() {
        return amministrazione;
    }

    public void setAmministrazione(String a) {
        amministrazione = a;
    }

    public void visualizzaPrenotazioni() {
        amministrazione = "<form>";
        amministrazione += "<p>Visualizza prenotazioni cliente:</p>";
        amministrazione += "<p><input type=\"text\" name=\"utente\"></p>";
        amministrazione += "<input type=\"hidden\" name=\"action\" value=\"visualizzaPrenotazioni\">";
        amministrazione += "<input type=\"submit\" value=\"Visualizza\"><br></form><br><br>";
    }

    public void evadiOrdine(int cod) {
        try {
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE PRENOTAZIONI SET EVASO = 1 WHERE COD="+cod);
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String HTMLPizza(int quantita, String COD, Connection conn) {
        String s = "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PIZZE WHERE COD='" + COD + "'");
            while (rs.next()) {
                s += "<li>" + quantita + " - " + rs.getString("NOME") + ", EUR " + rs.getDouble("PREZZO") + "</li>";
            }

            rs.close();
            st.close();
            return s;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    public String HtmlPrenotazione(int cod, Connection conn) {
        String res = "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PRENOTAZIONILIBRI WHERE COD=" + cod);
            while (rs.next()) {
                res += HTMLPizza(rs.getInt("QUANTITA"), rs.getString("COD"), conn);
            }
            rs.close();
            st.close();
            return res;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public void visualizzaPrenotazioni(String username) {
        try {
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());		//forse non serve
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            String prenotazione1 = "";
            String prenotazione2 = "";

            visualizzaPrenotazioni();

            ResultSet rs;
            if (username == null || username.equals("") || username.equals("null")) {
                rs = st.executeQuery("SELECT * FROM PRENOTAZIONI");
            } else {
                rs = st.executeQuery("SELECT * FROM PRENOTAZIONI WHERE USERNAME='" + username + "'");
            }

            while (rs.next()) {
                int cod = rs.getInt("COD");
                if ((rs.getInt("EVASO")) == 0) { //non evaso
                    prenotazione1 += "<div class=\"prenotazione\"><br>";
                    prenotazione1 += "Prenotazione numero <font size=\"4\">" + cod + "</font>, di " + rs.getString("USERNAME") + ",  del " + rs.getString("DATA");
                    prenotazione1 += "<ul>";
                    prenotazione1 += HtmlPrenotazione(cod, conn);
                    prenotazione1 += "</ul>";
                    prenotazione1 += "<form>";
                    prenotazione1 += "<input type=\"hidden\" name=\"who\" value=\"" + username + "\">";
                    prenotazione1 += "<input type=\"hidden\" name=\"cod\" value=\"" + cod + "\">";
                    prenotazione1 += "<input type=\"hidden\" name=\"action\" value=\"evadiOrdine\">";
                    prenotazione1 += "<input type=\"submit\" value=\"Evadi Ordine\"><br><br>";
                    prenotazione1 += "</form>";
                    prenotazione1 += "</div>";
                } else { //evaso
                    prenotazione2 += "<div class=\"prenotazione\"><br>";
                    prenotazione2 += "Prenotazione numero <font size=\"4\">" + cod + "</font>, di " + rs.getString("USERNAME") + ",   del " + rs.getString("DATA");
                    prenotazione2 += "<ul>";
                    prenotazione2 += HtmlPrenotazione(cod, conn);
                    prenotazione2 += "</ul>";
                    prenotazione2 += "<br>Ordine evaso<br><br>";
                    prenotazione2 += "</div>";
                }
            }
            amministrazione += prenotazione1;
            amministrazione += prenotazione2;
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("IN BUILD: " + e.getMessage());
        }
    }

    public void visualizzaAggiungiPizza() {
        
        amministrazione = "<form name=\"aggiungipizza\" action=\"Gestore\" onSubmit=\"return verificaInput(this)\" method=\"post\"><table>";
        amministrazione += "<tr><td>COD:</td><td><input type=\"text\" name=\"COD\" value=\"\" size=\"14\"></td></tr>";
        amministrazione += "<tr><td>Nome:</td><td><input type=\"text\" name=\"NOME\" value=\"\" size=\"30\"></td></tr>";
        amministrazione += "<tr><td>Descrizione:</td><td><input type=\"text\" name=\"DESCRIZIONE\" value=\"\"></td></tr>";
        amministrazione += "<tr><td>Immagine(nome):</td><td><input type=\"text\" name=\"IMMAGINE\" value=\".jpg\"></td></tr>";
        amministrazione += "<tr><td>Prezzo:</td><td><input type=\"text\" name=\"PREZZO\" value=\"0.0\" size=\"5\"></td></tr>";
        amministrazione += "</table>";
        amministrazione += "<input type=\"hidden\" name=\"action\" value=\"aggiungiPizza\">";
        amministrazione += "<input type=\"submit\" value=\"Aggiungi\"></form>";        
        
    }

    public boolean aggiungiPizza(String COD, String NOME, String DESCRIZIONE, double PREZZO, String IMMAGINE) {
        System.out.println(COD+IMMAGINE+DESCRIZIONE+PREZZO);
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO PIZZE (COD, NOME,  DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('" + COD + "', '" + NOME + "',' Ingredienti:" + DESCRIZIONE + "' , " + PREZZO + " , '"+IMMAGINE+"')");
            st.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean rimuoviPizza(String COD) {

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            String url = "jdbc:derby://localhost:1527/Pizze";
            String user = "fabio";
            String pass = "fabio";
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM PIZZE WHERE COD='" + COD + "'");
            st.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
