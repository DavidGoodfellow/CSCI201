package jdbcTest;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class JDBCTest {
	public static void main(String [] args){
		//connecting to any database could throw a SQL Exception so use a try
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try{
			//need to tell it what driver we want to use
			//we included a driver into the class path but you still need to know 
			//which one is used currently because your program could connect to multiple
			Class.forName("com.mysql.jdbc.Driver");//dynamically loads a class into your program on runtime
			//normally in a config file that is encrypted
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/StudentGrades?user=root&password=Zxcasdqwe123&useSSL=false");
			st = (Statement) conn.createStatement();
			rs = st.executeQuery("SELECT fname, lname FROM Student;");
			//String firstName = "Sheldon";
			//rs = st.executeQuery("SELECT fname, lname FROM Student WHERE fname='"+ firstName + "';");
			//^^this could be an issue through SQL injection!!
			//example: if String firstName = "Sheldon'; DROP DATABASE StudentGrades;"
			//can be prevented through a PREPARE STATEMENT in java
			while(rs.next()){
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				System.out.println(fname + " " + lname);
			}
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
				if(st!=null){
					st.close();
				}
				if(conn!=null){
					conn.close();
				}
			}catch (SQLException sqle){
				System.out.println("sqle: " + sqle.getMessage());		
			}
		}
	}
}
