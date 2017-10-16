package scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import components.Submarine;

/**Plain Camera
 * @author Jacqueline Whalley
 *
 */
public class Camera {

	/**Sets up the Plain camera
	 * @param gl opengl drawable
	 */
	
	double distance = 5;
	GLU glu = new GLU();
	public void draw(GL2 gl,Submarine submarine) {
		// set up projection first
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(submarine.posx, submarine.posy, submarine.posz-distance, // eye
				submarine.posx, submarine.posy , submarine.posz, // look
																					// at
				0, 1, 0); // up
		
	}

}
