package Drawing;

import java.awt.Font;

import javax.media.opengl.GL;

import GameObjects.Editor;

import com.sun.opengl.util.j2d.TextRenderer;

public class MenuDrawing {
	
	private static TextRenderer renderer;
	private static TextRenderer Trenderer;
	private static int titleScale = 10;
	private static int textScale = 18;
	private static int screenWidth;
	private static int screenHeight;
	private static float buttonSize;
	
	public static void init(int sw, int sh){
		screenWidth = sw;
		screenHeight = sh;
		buttonSize = screenWidth / 11.0f < screenHeight / 10.f ? screenWidth / 11.0f : screenHeight / 10.f;
		Trenderer = new TextRenderer(new Font("Impact", Font.PLAIN, (screenWidth)/titleScale));
		renderer = new TextRenderer(new Font("Arial", Font.BOLD, (screenWidth)/textScale));
		Editor.setButtonSize(buttonSize);
	}
	
	public static void drawPlane(GL gl){
		//Draws a background-plane with a texture
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(0, 0);
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(screenWidth,0);
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(screenWidth,screenHeight);
		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(0,screenHeight);
		gl.glTexCoord2f(0, 1);
		gl.glEnd();
	}

	public static void drawMenu(GL gl){
		//Teken nu het menu over de achtergrond heen
		
		//Draw a nice transparent surface over the background
		drawTrans(gl,0,0,screenWidth,screenHeight,0.1f,0.1f,0.1f,0.4f);
		
		//Draw the epic title
		drawTitle("MadBalls", 0.9f, 0.4f, 0.4f, 1f, (int)(screenWidth*0.315),(int)(screenHeight*0.8));
			
		// De vier menu texts "New game (of play ofzo" "Load level" "options" "quit"
		drawText("Play", 1f, 1f, 1f, 1f,(int)(screenWidth*0.445),
				(int)(screenHeight*0.625));
		
		drawText("Load", 1f, 1f, 1f, 1f,(int)(screenWidth*0.432),
				(int)(screenHeight*0.48));
		
		drawText("Editor", 1f, 1f, 1f, 1f,(int)(screenWidth*0.42),
				(int)(screenHeight*0.33));
	
		drawText("Quit", 1f, 1f, 1f, 1f,(int)(screenWidth*0.442),
				(int)(screenHeight*0.18));
	}
	
	public static void drawTrans(GL gl, float x, float y, float width, float height
			,float r, float g, float b, float a){
		//De onderstaande functies
		//zorgen voor de doorzichtigheid van de menu
		//elementen, tesamen met kleur etc.
		
		gl.glColor4f(r,g,b,a);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		//draw the actual surface
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x,y);
		gl.glVertex2f(x + width, y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
		gl.glEnd();
		
		// Disable alle crap voordat 
		//de volgende flush plaats vindt en 
		//de settings doorgegeven worden aan
		//de achtergrond
		gl.glDisable(GL.GL_COLOR_MATERIAL);
		gl.glDisable(GL.GL_BLEND);
	}
	
	public static void drawText(String text, float r, float g, float b, float a, int x, int y){
		//Renderer alvast in init gemaakt, anders wordt ie na elke glFlush() opnieuw gemaakt!
		
		renderer.beginRendering(screenWidth, screenHeight);
		renderer.setColor(r, g, b, a);
		renderer.draw(text, x, y);
		renderer.flush();
		renderer.endRendering();
	}
	
	public static void drawTitle(String text, float r, float g, float b, float a, int x, int y){
		//Renderer alvast in init gemaakt, anders wordt ie na elke glFlush() opnieuw gemaakt!
		
		Trenderer.beginRendering(screenWidth, screenHeight);
		Trenderer.setColor(r, g, b, a);
		Trenderer.draw(text, x, y);
		Trenderer.flush();
		Trenderer.endRendering();
	}

	public static void drawPauseMenu(GL gl, float x, float y, float width, float height
			,float r, float g, float b, float a){
		//De onderstaande functies
		//zorgen voor de doorzichtigheid van de menu
		//elementen, tesamen met kleur etc.
		
		gl.glColor4f(r,g,b,a);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		//draw the actual surface
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x,y);
		gl.glVertex2f(x + width, y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
		gl.glEnd();
		
		// Disable alle crap voordat 
		//de volgende flush plaats vindt en 
		//de settings doorgegeven worden aan
		//de achtergrond
		gl.glDisable(GL.GL_COLOR_MATERIAL);
		gl.glDisable(GL.GL_BLEND);
		
		//Draw all the appropriate text
		drawTitle("Pause", 0.9f, 0.4f, 0.4f, 1f, (int)(screenWidth*0.380),(int)(screenHeight*0.8));
		
		drawText("Resume", 1f, 1f, 1f, 1f,(int)(screenWidth*0.395),
				(int)(screenHeight*0.625));
		
		drawText("Main Menu", 1f, 1f, 1f, 1f,(int)(screenWidth*0.360),
				(int)(screenHeight*0.48));
	
		drawText("Quit", 1f, 1f, 1f, 1f,(int)(screenWidth*0.442),
				(int)(screenHeight*0.33));
	}
	
	//Button Section for Maze-Editor
	public static void drawButtons(GL gl) {
		// Draw a puls on top of the first box
		gl.glLineWidth(4);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		lineOnScreen(gl, buttonSize / 10.0f, screenHeight - buttonSize / 2.0f,
				buttonSize * 9.0f / 10.0f, screenHeight - buttonSize / 2.0f);
		lineOnScreen(gl, buttonSize / 2.0f, screenHeight - buttonSize / 10.0f,
				buttonSize / 2.0f, screenHeight - buttonSize * 9.0f / 10.0f);

		// Draw a minus on top of the second box.
		lineOnScreen(gl, buttonSize * 1.10f, screenHeight - buttonSize / 2.0f,
				buttonSize * 19.0f / 10.0f, screenHeight - buttonSize / 2.0f);

		// Draw a cube on the fourth box.
		boxOnScreen(gl, buttonSize * 3.10f, screenHeight - buttonSize* 0.9f, buttonSize * 0.8f);

		// Draw a slope on the seventh box.
		slopeOnScreen(gl, buttonSize * 6.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.8f);

		// Draw a slope on the eighth box.
		slopeOnScreen(gl, buttonSize * 7.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.4f);

		//Draw a rectangle on the ninth box.
		rectangleOnScreen(gl, buttonSize * 8.10f, screenHeight - buttonSize * 0.9f, buttonSize * 0.8f, buttonSize * 0.4f);

		// Draw the background boxes
		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 0.0f, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 2*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0.5f, 0);
		boxOnScreen(gl, 3*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 4*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, 5*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 6*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0.5f, 0);
		boxOnScreen(gl, 7*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0.5f, 0f);
		boxOnScreen(gl, 8*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0, 0, 0.5f);
		boxOnScreen(gl, 9*buttonSize, screenHeight - buttonSize, buttonSize);

		gl.glColor3f(0.5f, 0, 0);
		boxOnScreen(gl, 10*buttonSize, screenHeight - buttonSize, buttonSize);

		// Draw line around buttons
		gl.glColor3f(0.8f,  0.8f,  0.8f);
		rectangleOnScreen(gl, 0, screenHeight - buttonSize * 1.1f, buttonSize * 11, buttonSize * 0.1f);
		rectangleOnScreen(gl, buttonSize*11, screenHeight - buttonSize * 1.1f, buttonSize * 0.1f, buttonSize * 1.1f);
	}

	/**
	 * Help method that uses GL calls to draw a line.
	 */
	public static void lineOnScreen(GL gl, float x1, float y1, float x2, float y2) {
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a square
	 */
	public static void boxOnScreen(GL gl, float x, float y, float size)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + size, y);
		gl.glVertex2f(x + size, y + size);
		gl.glVertex2f(x, y + size);
		gl.glEnd();
	}
	/**
	 * Help method that uses GL calls to draw a slope
	 */
	public static void slopeOnScreen(GL gl, float x, float y, float xSize, float ySize)
	{
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + xSize, y);
		gl.glVertex2f(x, y + ySize);
		gl.glEnd();
	}

	/**
	 * Help method that uses GL calls to draw a rectangle
	 */
	public static void rectangleOnScreen(GL gl, float x, float y, float xSize, float ySize)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + xSize, y);
		gl.glVertex2f(x + xSize, y + ySize);
		gl.glVertex2f(x, y + ySize);
		gl.glEnd();
	}
}
