package HighScore;
import java.sql.*;

public class SqlReadWrite {
	
	public static void Write(){
		try{			
			String url = "jdbc:msql://localhost/highscores";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url, "group6", "tjjlgroup6");
			Statement st = conn.createStatement();
			st.executeUpdate("INSERT INTO highscores" + "VALUES(1, 'tom', 200, 2)");
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
