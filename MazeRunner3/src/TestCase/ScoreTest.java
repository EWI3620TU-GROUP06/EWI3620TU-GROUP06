package TestCase;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import HighScore.Score;

public class ScoreTest {
	
	Score testScore;
	@Before
	public void setup(){
		testScore = new Score("joost", 200);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(testScore.toString(), "joost 200");
	}
	
	@Test
	public void testGetScr(){
		assertEquals(testScore.getScr(), 200);
	}
	
	@Test
	public void testGetName(){
		assertEquals(testScore.getName(), "joost");
	}

}
