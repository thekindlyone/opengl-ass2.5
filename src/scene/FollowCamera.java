package scene;



import java.awt.Point;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;


public class FollowCamera  {


    

    // the camera rotation angles
    private double angleX = 0;
    private double angleY = 0;

    // old mouse position for dragging

    // camera parameters
    double fieldOfView      = 45;
    double distanceToSub = 5;
    double windowWidth      = 1;
    double windowHeight     = 1;

    // GLU context
    GLU glu = new GLU();
    

    /**
     * Constructor of the trackball camera
     * @param drawable the GL drawable context to register this camera with
     */
    public FollowCamera(GLCanvas canvas) {
    }

    /**
     * "Draws" the camera.
     * This sets up the projection matrix and
     * the camera position and orientation.
     * This method has to be called first thing
     * in the <code>display()</code> method
     * of the main program
     *
     * @param gl then OpenGL context to draw the camera in
     */
    public void draw(GL2 gl,double submarine_angle, double submarineX, double submarineY, double submarineZ) {
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // setting up perspective projection
        // far distance is hardcoded to 3*cameraDistance. If your scene is bigger,
        // you might need to adapt this
        glu.gluPerspective(fieldOfView, windowWidth / windowHeight, 0.1, 15);

        // then set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        angleX = submarine_angle+90;
        double r = distanceToSub * Math.cos(Math.toRadians(angleY));
        double camZ = submarineZ+ r * Math.cos(Math.toRadians(angleX));
        double camX = submarineX+ r * Math.sin(Math.toRadians(angleX));
        double camY = submarineY+distanceToSub * Math.sin(Math.toRadians(angleY));
        glu.gluLookAt(
            camX, submarineY+1, camZ,                // eye
            submarineX,submarineY, submarineZ, // look at
            0, 1, 0);                        // up
    }

    /**
     * Passes a new window size to the camera.
     * This method should be called from the <code>reshape()</code> method
     * of the main program.
     *
     * @param width the new window width in pixels
     * @param height the new window height in pixels
     */
    public void newWindowSize(int width, int height) {
        windowWidth = Math.max(1.0, width);
        windowHeight = Math.max(1.0, height);
    }

  
}
