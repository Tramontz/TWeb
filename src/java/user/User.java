package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	
	public User(){}
	
	
	public static String checkLogin(String user_name, String user_pass){
		try{
			DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			String permesso="not_found";
		    String url = "jdbc:derby://localhost:1527/Pizze";
		    String user="fabio";
		    String pass="fabio";
		    Connection conn = DriverManager.getConnection(url, user, pass);
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery("SELECT * FROM UTENTI WHERE USERNAME = '"+user_name+"' AND PASSWORD = '"+user_pass+"'");
		    while(rs.next())
		    	permesso = rs.getString("PERMESSO");
		    
		    st.close(); conn.close();
		    return permesso;
		}catch(SQLException e){ System.out.println(e.getMessage());}
		return "not_found";
	}
	
}
