package HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SqlReadWrite {

	private static Connection connect = null;
	public static ArrayList<Score> highscores = new ArrayList<Score>();
	public static Score mostRecentScore = null;

	public static void startConnection()
	{
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connect = DriverManager
					.getConnection("jdbc:mysql://sql.ewi.tudelft.nl:3306/WDtech", "haantjes", "wdtech123");
			Statement stat = connect.createStatement();
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS highscores("
					+ "num INT PRIMARY KEY AUTO_INCREMENT,"
					+ "name CHARACTER( 10 ) NOT NULL,"
					+ "score INT NOT NULL,"
					+ "LEVEL CHARACTER( 10 ) NOT NULL,"
					+ "TIME TIMESTAMP NOT NULL)");
			stat.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void Write(Score score){
		try{
			startConnection();
			PreparedStatement preparedStatement = connect
					.prepareStatement("insert into  highscores values (default, ?, ?, ?, ?)");
			preparedStatement.setString(1,  score.getName());
			preparedStatement.setInt(2, score.getScr());
			preparedStatement.setString(3,  "Level 1");
			Calendar calendar =	Calendar.getInstance();
			Date date = calendar.getTime();
			preparedStatement.setTimestamp(4, new Timestamp(date.getTime()));
			preparedStatement.executeUpdate();
			
			connect.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void Read()
	{
		highscores = new ArrayList<Score>();
		try{
			Timestamp mostRecentTime = new Timestamp(0);
			startConnection();
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT name, score, time FROM highscores ORDER BY score DESC;");
			while(resultSet.next())
			{
				String name = resultSet.getString("name");
				int scr = resultSet.getInt("score");
				Timestamp timestamp = resultSet.getTimestamp("time");
				Score newScore = new Score(name, scr);
				if(timestamp.after(mostRecentTime))
					mostRecentScore = newScore;
				highscores.add(newScore);
			}
			connect.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void DeleteHigHscores(){
		try {
			startConnection();
			Statement stat = connect.createStatement();
			stat.executeUpdate("DROP TABLE IF EXISTS highscores;");
			highscores = new ArrayList<Score>();
			mostRecentScore = null;
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
