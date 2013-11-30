package Drawing;


import java.io.InputStream;

import javax.media.opengl.GL;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

//import GameObjects.Editor;

public class MenuDrawing {
	
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
	
	}
	
	public static Texture initTexture(GL gl, String textureName)
	{
		textureName = "/Textures/" + textureName + ".jpg";
		Texture res = null;
		try{
			InputStream stream = MenuDrawing.class.getResourceAsStream(textureName);
			TextureData data = TextureIO.newTextureData(stream, false, "jpg");
			res = TextureIO.newTexture(data);
			stream.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Help method that uses GL calls to draw a square
	 */
	public static void boxOnScreen(GL gl, float x, float y, float sizeX, float sizeY)
	{
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x, y);
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(x + sizeX, y);
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(x + sizeX, y + sizeY);
		gl.glTexCoord2f(0, 0);
		gl.glVertex2f(x, y + sizeY);
		gl.glTexCoord2f(0, 1);
		gl.glEnd();
	}
	
}
