package TestCase;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import EditorModes.ObjectMode;
import LevelHandling.Maze;
import MazeObjects.CustomMazeObject;
import MazeObjects.MazeObject;

public class MazeTest {
	
	private Maze testMaze;
	private CustomMazeObject tunnel;
	private CustomMazeObject roundRamp;
	
	/**
	 * Before testing create a small maze that contains all standard maze objects and two custom ones.
	 * This maze will be modified and checked during all tests.
	 */
	
	@Before
	public void initTestMaze()
	{
		testMaze = new Maze(3, 3);
				
		testMaze.set(Maze.standards.get(ObjectMode.ADD_START), 0, 0);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_FLOOR), 1, 0);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_FLOOR), 2, 0);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_EMPTY), 2, 0);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_BOX), 2, 0);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_LOW_RAMP), 0, 1);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_LOW_BOX), 0, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_LOW_BOX), 1, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_RAMP), 1, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_BOX), 2, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_LOW_BOX), 2, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_FINISH), 2, 2);
		tunnel = CustomMazeObject.readFromOBJ(new File("src/Objects/tunnel_01.obj"));
		roundRamp = CustomMazeObject.readFromOBJ(new File("src/Objects/round_ramp_02.obj"));
		testMaze.set(tunnel, 2, 1);
		testMaze.set(roundRamp, 2, 1);
	}
	
	/**
	 * First test to determine whether the initiated maze is of the expected form.
	 * <p>
	 * Note that the equals methods for the MazeObjects are not conclusive on their own for these tests: 
	 * the position and rotation needs to be taken into consideration as well;
	 */

	@Test
	public void testInit() {
		//Test the stack getter
		ArrayList<MazeObject> stack10 =  testMaze.get(1, 0);
		assertEquals(stack10.size(), 3);
		assertEquals(stack10.get(0), Maze.standards.get(ObjectMode.ADD_BOTTOM));
		assertEquals(stack10.get(0).getPos(), new Vector3f(Maze.SQUARE_SIZE, -20, 0));
		assertEquals(stack10.get(1), Maze.standards.get(ObjectMode.ADD_PIT));
		assertEquals(stack10.get(1).getPos(), new Vector3f(Maze.SQUARE_SIZE, -20, 0));
		assertEquals(stack10.get(2), Maze.standards.get(ObjectMode.ADD_FLOOR));
		assertEquals(stack10.get(2).getPos(), new Vector3f(Maze.SQUARE_SIZE, 0, 0));
		ArrayList<MazeObject> faultStack = testMaze.get(3, 0);
		assertEquals(faultStack.size(), 0);
		faultStack = testMaze.get(0, -1);
		assertEquals(faultStack.size(), 0);
		
		
		//test the single object getter
		MazeObject obj240 = testMaze.get(2, 4, 0);
		assertEquals(obj240, Maze.standards.get(ObjectMode.ADD_BOX));
		assertEquals(obj240.getPos(), new Vector3f(2*Maze.SQUARE_SIZE, Maze.SQUARE_SIZE, 0));
		MazeObject faultObj = testMaze.get(-1, 0, 0);
		assertEquals(faultObj, null);
		faultObj = testMaze.get(0, 4, 0);
		assertEquals(faultObj, null);
	}
	
	/**
	 * Test the getStart and isFinish methods, and the effect of rotateTop on the Start position.
	 */
	
	@Test
	public void testStartFinish()
	{
		// test isFinish
		assertFalse(testMaze.isFinish(5, 5, 5));
		assertFalse(testMaze.isFinish(12.5, -7, 12.5));
		assertTrue(testMaze.isFinish(12.5, 7, 12.5));
		
		//test getStart
		double[] startPos = testMaze.getStart();
		assertTrue(startPos[0] == 2.5);
		assertTrue(startPos[1] == 2.5);
		assertTrue(startPos[2] == 2.5);
		assertTrue(startPos[3] == 0);
		
		testMaze.rotateTop(0, 0, 90, true, false, true);
		startPos = testMaze.getStart();
		assertTrue(startPos[3] == 0);
		
		testMaze.rotateTop(0, 0, 180, false, true, false);
		startPos = testMaze.getStart();
		assertTrue(startPos[3] == 180);
		
	}
	
	@After
	public void clearTestMaze()
	{
		testMaze = null;
	}

}
