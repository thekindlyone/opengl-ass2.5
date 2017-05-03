package scene;

import com.jogamp.opengl.GL2;

/**Plain Camera
 * @author Jacqueline Whalley
 *
 */
public class Camera {

	/**Sets up the Plain camera
	 * @param gl opengl drawable
	 */
	public void draw(GL2 gl) {
		// set up projection first
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

}
