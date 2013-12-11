package HighScore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/highscores?"
							+ "user=sqluser&password=sqluserpw");
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
					.prepareStatement("insert into  HIGHSCORES.HIGHSCORE values (default, ?, ?, ?, ?)");
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
		try{
			Timestamp mostRecentTime = new Timestamp(0);
			startConnection();
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT name, score, time FROM highscores.highscore ORDER BY score DESC;");
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
}
