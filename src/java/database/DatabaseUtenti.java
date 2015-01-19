package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseUtenti {
	public static void main(String [] args){
		String url = "jdbc:derby://localhost:1527/Pizze";
		String user="fabio";
		String pwd="fabio";
		String name="UTENTI";
		
		
		try{
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			Connection conn = DriverManager.getConnection(url,user,pwd);
		
			creaTabella(name, conn, url, user, pwd);
			caricaDati(name, conn, url, user, pwd);
			mostraTabella(conn, name);
			
			conn.close();
		} catch (SQLException e) {System.out.println(e.getMessage());}
		
		
	}
	
	public static void mostraTabella(Connection conn, String name) throws SQLException{
		String out="";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM "+name);
		
		while (rs.next())
			out += rs.getString("USERNAME") + " - "+ rs.getString("PASSWORD") +", "+ rs.getString("PERMESSO")+"\n"; 
		System.out.println(out);
		rs.close();
		st.close();
	
	}
	
	public static void caricaDati(String name, Connection conn, String url, String user, String pwd) throws SQLException{
		Statement st = conn.createStatement();
		st.executeUpdate("INSERT INTO "+name+" (USERNAME, PASSWORD, PERMESSO) VALUES ('Fabio', 'fabio', 'admin')");
		st.executeUpdate("INSERT INTO "+name+" (USERNAME, PASSWORD, PERMESSO) VALUES ('Dario', 'dario', 'admin')");
		st.executeUpdate("INSERT INTO "+name+" (USERNAME, PASSWORD, PERMESSO) VALUES ('Mauro', 'mauro', 'user')");
		st.executeUpdate("INSERT INTO "+name+" (USERNAME, PASSWORD, PERMESSO) VALUES ('Darione', 'darione', 'user')");
		st.close();
	}
	
	public static String creaTabella (String name, Connection conn, String url, String user, String pwd) throws SQLException{
		Statement st = conn.createStatement();
		try{st.execute("DROP TABLE "+name);}catch(SQLException e){}
		st.executeUpdate("CREATE TABLE "+name+" (USERNAME VARCHAR(20) PRIMARY KEY, PASSWORD VARCHAR(20) NOT NULL, PERMESSO VARCHAR(10))");
		st.close();
		return name;
	}
}
