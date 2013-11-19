package MainGame;

import java.io.InputStream;
import java.awt.Font;
import com.sun.opengl.util.j2d.TextRenderer;

import javax.media.opengl.*;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

import Main.Game;

public class MainMenu implements GLEventListener {
	
	private int screenWidth, screenHeight;				// Screen size to handle reshaping
	private Texture backgroundTexture;
	private TextRenderer renderer;
	
	public MainMenu(Game game) {
		this.screenWidth = game.getScreenWidth();
		this.screenHeight = game.getScreenHeight();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1); //2D by making a -1 to 1 z around the z = 0 plane
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		//To render texts
		//Set the font type shizzle here
		renderer = new TextRenderer(new Font("ARIAL", Font.BOLD, 40)); 
		
		// Preload the texture we want to use!
		try{
		InputStream stream = getClass().getResourceAsStream("mainmenu.jpg");
        TextureData data = TextureIO.newTextureData(stream, false, "jpg");
        this.backgroundTexture = TextureIO.newTexture(data);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
		
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		// set the 'clear screen color' not really necessary but useful to make all the clear colors the same!
		gl.glClearColor(1f,1f,1f,1);
		
		//The ambient color is white light
        float[] lightColor = {1f, 1f, 1f, 1f};

        // The Ambient light is created here.
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColor, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, lightColor, 0);
        
        // Enable lighting in GL.
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        
		
		float[] rgba = {1f, 1f, 1f}; //Sets the material color
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
		backgroundTexture.enable(); // Enable the background texture
		backgroundTexture.bind(); // Bind the background texture to the next object
		drawPlane(gl); // draw the background plane
		backgroundTexture.disable(); // Disable the background texture again, such that the next object is textureless
		
		drawMenu(gl, drawable); // draw the menu buttons with text and stuff.
		
		gl.glFlush();
	}
	
	private void drawPlane(GL gl){
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

	private void drawMenu(GL gl, GLAutoDrawable drawable){
		//Teken nu het menu over de achtergrond heen
		
		//De onderstaande functies
		//zorgen voor de doorzichtigheid van de menu
		//elementen, tesamen met kleur etc.
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL.GL_COLOR_MATERIAL);
			
		// De vier menu texts "New game (of play ofzo" "Load level" "options" "quit"
		gl.glColor4f(0.3f,0.3f,0.3f,0.75f);
		drawTextBox(gl,(screenWidth/2.0f) - 0.15f*screenWidth, 
				(screenHeight*0.60f) - 0.05f*screenHeight, 0.3f*screenWidth, 0.1f*screenHeight);
		drawText("Text", 1f, 1f, 1f, 1f,(int)( (screenWidth/2.0) - 0.10*screenWidth),(int)( (screenHeight*0.60) - 0.0*screenHeight), drawable);
		gl.glColor4f(0.3f,0.3f,0.3f,0.75f);
		drawTextBox(gl,(screenWidth/2.0f) - 0.15f*screenWidth, 
				(screenHeight*0.45f) - 0.05f*screenHeight, 0.3f*screenWidth, 0.1f*screenHeight);
		gl.glColor4f(0.3f,0.3f,0.3f,0.75f);
		drawTextBox(gl,(screenWidth/2.0f) - 0.15f*screenWidth, 
				(screenHeight*0.30f) - 0.05f*screenHeight, 0.3f*screenWidth, 0.1f*screenHeight);
		gl.glColor4f(0.3f,0.3f,0.3f,0.75f);
		drawTextBox(gl,(screenWidth/2.0f) - 0.15f*screenWidth, 
				(screenHeight*0.15f) - 0.05f*screenHeight, 0.3f*screenWidth, 0.1f*screenHeight);
		
		// Disable alle crap voordat 
		//de volgende flush plaats vindt en 
		//de settings doorgegeven worden aan
		//de achtergrond
		gl.glDisable(GL.GL_COLOR_MATERIAL);
		gl.glDisable(GL.GL_BLEND);
	}
	
	private void drawTextBox(GL gl, float x, float y, float width, float height){
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x,y);
		gl.glVertex2f(x + width, y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
		gl.glEnd();
	}
	
	private void drawText(String text, float r, float g, float b, float a, int x, int y, GLAutoDrawable drawable){
		//Renderer alvast in init gemaakt, anders wordt ie na elke glFlush() opnieuw gemaakt!
		
		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(r, g, b, a);
		renderer.draw(text, x, y);
		renderer.flush();
		renderer.endRendering();
	}
	
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// Hopefully not needed
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		
		// Setting the new screen size and adjusting the viewport.
		screenWidth = width;
		screenHeight = height;
		gl.glViewport( 0, 0, screenWidth, screenHeight );
		
		// Set the new projection matrix.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0,screenWidth,0,screenHeight,-1,1); //2D by making a -1 to 1 z around the z = 0 plane
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glDisable(GL.GL_DEPTH_TEST);
	}

}
