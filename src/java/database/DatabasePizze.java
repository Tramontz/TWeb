package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePizze {

    public static void main(String[] args) {
        String url = "jdbc:derby://localhost:1527/Pizze";
        String user = "fabio";
        String pwd = "fabio";
        String name = "Pizze";

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            Connection conn = DriverManager.getConnection(url, user, pwd);

            creaTabella(name, conn, url, user, pwd);
            caricaDati(name, conn, url, user, pwd);
            mostraTabella(conn, name);

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void mostraTabella(Connection conn, String name) throws SQLException {
        String out = "";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + name);

        while (rs.next()) {
            out += "(" + rs.getString("COD") + ") " + rs.getString("NOME") +": "+ rs.getString("DESCRIZIONE") +"  €"+ rs.getDouble("PREZZO") + "\n";
        }
        System.out.println(out);
        rs.close();
        st.close();

    }

    public static void caricaDati(String name, Connection conn, String url, String user, String pwd) throws SQLException {
        try {
            Statement st = conn.createStatement();

            String Bianca = "Ingredienti: mozzarella, olio, origano.";
            String Margherita = "Ingredienti: pomodoro, mozzarella, basilico.";
            String Napoletana = "Ingredienti: pomodoro, mozzarella, basilico, acciughe, olive.";
            String Diavola = "Ingredienti: pomodoro, mozzarella, basilico, salme piccante, olive, peperoncino.";
            String Valdostana = "Ingredienti: pomodoro, mozzarella, basilico, fontina, prosciutto cotto.";
            String Americana = "Ingredienti: pomodoro, mozzarella, basilico, peperoni, patatine fritte, wurstel, uova.";

            st.executeUpdate("INSERT INTO " + name + " (COD, NOME, DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('P_100', 'Bianca', '"+Bianca+"', 4.00, 'Bianca.jpg')");
            st.executeUpdate("INSERT INTO " + name + " (COD, NOME, DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('P_101', 'Margherita', '"+Margherita+"', 5.00, 'Margherita.jpg')");
            st.executeUpdate("INSERT INTO "+name+" (COD, NOME, DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('P_105', 'Napoletana', '"+Napoletana+"', 6.70, 'Napoletana.jpg')");
            st.executeUpdate("INSERT INTO "+name+" (COD, NOME, DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('P_108', 'Valdostana','"+Valdostana+"', 7.30, 'Valdostana.jpg')");
            st.executeUpdate("INSERT INTO "+name+" (COD, NOME, DESCRIZIONE, PREZZO, IMMAGINE) VALUES ('P_112', 'Americana', '"+Americana+"', 9.00, 'Americana.jpg')");

            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String creaTabella(String name, Connection conn, String url, String user, String pwd) throws SQLException {
        Statement st = conn.createStatement();

        try {
            st.execute("DROP TABLE " + name);
        } catch (SQLException e) {
            System.out.println(e);
        }

        st.executeUpdate("CREATE TABLE " + name + "(COD VARCHAR(14) PRIMARY KEY, NOME VARCHAR(40) NOT NULL, DESCRIZIONE VARCHAR(1500) NOT NULL, PREZZO FLOAT NOT NULL, IMMAGINE VARCHAR(40) NOT NULL)");
        st.close();
        return name;
    }
}
