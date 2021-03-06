package dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

//used to create an encapsulated way to connect to our db
public class Database {
	public static Connection getConnection() throws Exception{
		
		//connecting to any database could throw a SQL Exception so use a try
		Connection conn = null;
		//Statement st = null;
		ResultSet rs = null;
		try{
			//need to tell it what driver we want to use
			//we included a driver into the class path but you still need to know 
			//which one is used currently because your program could connect to multiple
			Class.forName("com.mysql.jdbc.Driver").newInstance();//dynamically loads a class into your program on runtime
			
			//normally in a config file that is encrypted
			String connectionURL = "jdbc:mysql://localhost/ProjectDB?user=root&password=Zxcasdqwe123&useSSL=false";
			conn = (Connection) DriverManager.getConnection(connectionURL);
			/*st = (Statement) conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Categories;");
			rs.next();
			String id = rs.getString("id");
			String name = rs.getString("name");
			System.out.println(id + " " + name);*/
			return conn;
			
			
		} catch(SQLException sqle){
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe){
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try{
				//make sure to close them in reverse order!!!
				if(rs!=null){
					rs.close();
				}
				/*if(st!=null){
					st.close();
				}*/
				if(conn!=null){
					conn.close();
				}
			}catch (SQLException sqle){
				System.out.println("sqle: " + sqle.getMessage());		
			}
		}
		return conn;
}
	
	/*public static void main(String [] args){
		try {
			getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
