package scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import components.Submarine;

public class FollowCamera {

	// the camera rotation angles
	private double angle = 0;

	// camera parameters
	double fieldOfView = 45;
	double distanceToSub = 4;
	double windowWidth = 1;
	double windowHeight = 1;
	double elevation = 20; // degrees
	double lookat[] = { 0, 0, 0 };

	public double camX, camZ, camY;

	// GLU context
	GLU glu = new GLU();

	/**
	 * Constructor of the Follow camera
	 * 
	 * @param drawable
	 *            the GL drawable context to register this camera with
	 */
	public FollowCamera(GLCanvas canvas) {
	}

	/**
	 * "Draws" the camera. This sets up the projection matrix and the camera
	 * position and orientation. This method has to be called first thing in the
	 * <code>display()</code> method of the main program
	 *
	 * @param gl
	 *            then OpenGL context to draw the camera in
	 */
	public void draw(GL2 gl, Submarine submarine) {
		// set up projection first
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// setting up perspective projection

		glu.gluPerspective(fieldOfView, windowWidth / windowHeight, 0.1, 20);

		// then set up the camera position and orientation
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		angle = submarine.angle + 90;
		camZ = submarine.posz + distanceToSub * Math.cos(Math.toRadians(angle));
		camX = submarine.posx + distanceToSub * Math.sin(Math.toRadians(angle));
		camY = submarine.posy + distanceToSub * Math.cos(Math.toRadians(90 - elevation));
		lookat[0] = submarine.posx + submarine.length * Math.sin(Math.toRadians(angle));
		lookat[2] = submarine.posz + submarine.length * Math.cos(Math.toRadians(angle));
		lookat[1] = submarine.posy;
		glu.gluLookAt(camX, camY, camZ, // eye
				lookat[0], lookat[1], lookat[2], // look at
				0, 1, 0); // up
	}

	/**
	 * Passes a new window size to the camera. This method should be called from
	 * the <code>reshape()</code> method of the main program.
	 *
	 * @param width
	 *            the new window width in pixels
	 * @param height
	 *            the new window height in pixels
	 */
	public void newWindowSize(int width, int height) {
		windowWidth = Math.max(1.0, width);
		windowHeight = Math.max(1.0, height);
	}
	
	
	public float[] getpos(){
		return new float[]{(float)this.camX,(float)this.camY,(float)this.camZ,0};
	}

}
