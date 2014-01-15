package TestCase;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import HighScore.Score;
import HighScore.SqlReadWrite;

public class SqlReadWriteTest {
	
	Score testScore;
	Score testScore1;
	Score mostRecentScoreTest;
	ArrayList<Score> scoreList = new ArrayList<Score>();
	ArrayList<Score> saveHighscores = new ArrayList<Score>();


	@Before
	public void setup(){
		testScore = new Score("joost", 200);
		testScore1 = new Score("kasper", 80);
		mostRecentScoreTest = testScore1;
		scoreList.add(testScore);
		scoreList.add(testScore1);
		SqlReadWrite.Read();
		saveHighscores = SqlReadWrite.getHighscores();
		SqlReadWrite.startConnection();

	}
	@Test
	public void testConnection() {
		try {
			assertEquals(SqlReadWrite.getConnection().isClosed(), false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadWrite(){
		for(Score s: scoreList){
			SqlReadWrite.Write(s);	
		}
		SqlReadWrite.Read();
		scoreList = SqlReadWrite.getHighscores();
		for(Score s: scoreList){
			if (s.toString().equals(testScore.toString())){
				assertEquals(s.toString(), testScore.toString());
			}
		}
		assertEquals(SqlReadWrite.getMostRecentScore().toString(), mostRecentScoreTest.toString());
	}
	
	@Test
	public void testDelete(){
		SqlReadWrite.DeleteHigHscores();
		assertNull(SqlReadWrite.getMostRecentScore());
		try {
			assertEquals(SqlReadWrite.getConnection().isClosed(), true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@After
	public void after(){
		for(Score s: saveHighscores){
			SqlReadWrite.Write(s);	
		}
	}
	
	
	

}
