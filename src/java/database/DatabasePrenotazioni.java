package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabasePrenotazioni {
	public static void main(String [] args){
		String url = "jdbc:derby://localhost:1527/Pizze";
		String user="fabio";
		String pwd="fabio";
		String name="PRENOTAZIONI";
		
		// EVASO=1 
		//NON EVASO=0
		try{
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			Connection conn = DriverManager.getConnection(url,user,pwd);
		
			Statement st = conn.createStatement();
			try{st.execute("DROP TABLE "+name);}catch(SQLException e){}
			st.executeUpdate("CREATE TABLE "+name+" (COD INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, USERNAME VARCHAR(20) NOT NULL, DATA VARCHAR(20), TOTALE FLOAT, EVASO INT)");
			st.close();
			System.out.println("Refresh\n");
			conn.close();
		} catch (SQLException e) {System.out.println(e.getMessage());}
		
		
	}
}
