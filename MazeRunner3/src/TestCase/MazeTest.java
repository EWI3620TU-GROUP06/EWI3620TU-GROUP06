package TestCase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
		testMaze.set(Maze.standards.get(ObjectMode.ADD_LOW_BOX), 2, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_BOX), 2, 2);
		testMaze.set(Maze.standards.get(ObjectMode.ADD_FINISH), 2, 2);
		tunnel = CustomMazeObject.readFromOBJ(new File("src/Objects/tunnel_01.obj"));
		roundRamp = CustomMazeObject.readFromOBJ(new File("src/Objects/round_ramp_02.obj"));
		Maze.customs.add(tunnel);
		Maze.customs.add(roundRamp);
		testMaze.set(tunnel, 2, 1);
		testMaze.set(roundRamp, 2, 1);
	}

	/**
	 * First test to determine whether the initiated maze is of the expected form.
	 * <p>
	 * Note that the equals methods for the MazeObjects are not conclusive on their own for these tests: 
	 * the position needs to be taken into consideration as well;
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
	 * Tests adding to a new maze using the selected flag like in the editor.
	 */

	@Test
	public void testAddRemoveNew()
	{
		Maze newMaze = new Maze();
		assertTrue(newMaze.getSizeX() == 3*Maze.SQUARE_SIZE);
		newMaze.removeTop(2, 1);
		newMaze.clearSelected();
		newMaze.select(0, 0);
		newMaze.addBlock(ObjectMode.ADD_START, 0);
		newMaze.clearSelected();
		newMaze.select(2, 0);
		newMaze.addBlock(ObjectMode.ADD_EMPTY, 0);
		newMaze.addBlock(ObjectMode.ADD_BOX, 0);
		newMaze.clearSelected();
		newMaze.select(0, 1);
		newMaze.addBlock(ObjectMode.ADD_LOW_RAMP,0);
		newMaze.clearSelected();
		newMaze.select(2, 1);
		newMaze.addBlock((byte)(-Maze.customs.indexOf(tunnel) - 1), 0);
		newMaze.addBlock((byte)(-Maze.customs.indexOf(roundRamp) - 1), 0);
		newMaze.clearSelected();
		newMaze.select(0, 2);
		newMaze.select(1, 2);
		newMaze.select(2, 2);
		newMaze.addBlock(ObjectMode.ADD_LOW_BOX, 0);
		newMaze.clearSelected();
		newMaze.select(1, 2);
		newMaze.addBlock(ObjectMode.ADD_RAMP, 0);
		newMaze.clearSelected();
		newMaze.select(2, 2);
		newMaze.addBlock(ObjectMode.ADD_BOX, 0);
		newMaze.addBlock(ObjectMode.ADD_FINISH, 0);
		newMaze.clearSelected();

		newMaze.removeTop(1, 1);
		newMaze.removeTop(1, 1);
		newMaze.removeTop(1, 1);

		checkMazes(testMaze, newMaze);
	}

	/**
	 * Test the getStart and isFinish methods, and the effect of rotateTop on the Start position.
	 */

	@Test
	public void testStartFinish()
	{
		// test isFinish
		assertFalse(testMaze.isFinish(5, 5, 5));
		assertFalse(testMaze.isFinish(12.5, -0.5, 12.5));
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

		testMaze.rotateTop(0, 0, 90, false, true, false);
		startPos = testMaze.getStart();
		assertTrue(startPos[3] == 90);
	}

	@Test
	public void testReadWrite()
	{
		File testFile = new File("testCase.mz");
		try{
			PrintWriter wr =  new PrintWriter(testFile);
			testMaze.write(wr);
			wr.flush();
			wr.close();
			Scanner sc = new Scanner(testFile);
			Maze readMaze = Maze.read(sc);
			checkMazes(testMaze, readMaze);
			sc.close();
			
			Maze noStartMaze = new Maze();
			wr = new PrintWriter(testFile);
			assertFalse(noStartMaze.write(wr));
			wr.flush();
			wr.close();
			
			Maze noFinishMaze = new Maze();
			noFinishMaze.set(Maze.standards.get(ObjectMode.ADD_START), 0, 0);
			wr = new PrintWriter(testFile);
			assertFalse(noFinishMaze.write(wr));
			wr.flush();
			wr.close();
			
			sc = new Scanner(testFile);
			Maze faultMaze = Maze.read(sc);
			checkMazes(faultMaze, new Maze());
			sc.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	@Test
	public void testResize()
	{
		testMaze.setSize(4, 5);
		MazeObject obj242 = testMaze.get(2, 4, 2);
		MazeObject obj322 = testMaze.get(3, 2, 2);
		assertEquals(obj242, Maze.standards.get(ObjectMode.ADD_FINISH));
		assertEquals(obj322, Maze.standards.get(ObjectMode.ADD_FLOOR));
		assertTrue(testMaze.getSizeX() == 4 * Maze.SQUARE_SIZE);
		assertTrue(testMaze.getSizeZ() == 5 * Maze.SQUARE_SIZE);
		
		testMaze.setSize(1, 2);
		obj242 = testMaze.get(2, 4, 2);
		assertEquals(obj242, null);
		assertTrue(testMaze.getSizeX() == 1 * Maze.SQUARE_SIZE);
		assertTrue(testMaze.getSizeZ() == 2 * Maze.SQUARE_SIZE);
		
		testMaze.setSize(0, 3);
		assertTrue(testMaze.getSizeX() == 1 * Maze.SQUARE_SIZE);
		assertTrue(testMaze.getSizeZ() == 2 * Maze.SQUARE_SIZE);
	}
	
	@Test
	public void testHeight()
	{
		assertTrue(testMaze.getHeight(2, 2) == 12.5);
		assertTrue(testMaze.getHeight(0,-1) == Integer.MIN_VALUE);
		
		assertTrue(testMaze.getFloorHeight(2, 2) == -1);
		assertTrue(Math.abs(testMaze.getFloorHeight(1, 0) - 0) < 0.00001);
		assertTrue(testMaze.getFloorHeight(-1,  0) == -1);
	}
	
	@Test
	public void testRemoveRedundantVertices()
	{
		MazeObject obj022 = testMaze.get(0, 2, 2);
		assertTrue(obj022.getNumFaces() == 6);
		int numTunnelFaces = testMaze.get(2, 2, 1).getNumFaces();
		testMaze.removeRedundantFaces();
		obj022 = testMaze.get(0, 2, 2);
		assertTrue(obj022.getNumFaces() == 5);
		assertTrue(testMaze.get(2, 2, 1).getNumFaces() == numTunnelFaces - 2);
	}
	
	@Test
	public void testNeighbours()
	{
		MazeObject[] objArray = testMaze.getNeighbourTiles(0, 2, 2.5f);
		assertEquals(objArray[0], null);
		assertEquals(objArray[1], Maze.standards.get(ObjectMode.ADD_LOW_RAMP));
		assertEquals(objArray[2], Maze.standards.get(ObjectMode.ADD_LOW_BOX));
		assertEquals(objArray[0], null);
		
		MazeObject[] faultArray = testMaze.getNeighbourTiles(0, -1, 0);
		assertTrue(faultArray == null);
	}

	@After
	public void clearTestMaze()
	{
		testMaze = null;
	}

	private void checkMazes(Maze maze1, Maze maze2)
	{
		for(int i = 0; i < maze1.getSizeX() && i < maze2.getSizeX(); i++){
			for(int j = 0; j < maze1.getSizeZ() && j < maze2.getSizeZ(); j++){
				ArrayList<MazeObject> stack1 = maze1.get(i, j);
				ArrayList<MazeObject> stack2 = maze2.get(i, j);
				for(int k = 0; k < stack1.size() && k < stack2.size(); k++){
					assertEquals(stack1.get(k), stack2.get(k));
					checkVectors(stack1.get(k).getPos(), stack2.get(k).getPos());
				}
			}

		}

	}

	private void checkVectors(Vector3f vector1, Vector3f vector2)
	{
		Vector3f dif = new Vector3f();
		dif.sub(vector1, vector2);
		assertTrue(Math.abs(dif.length()) < 0.00001);
	}

}
