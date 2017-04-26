package scene;


/**

 * Simple camera
 * @author Jacqueline Whalley
 */


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;



public class Camera {

    // some hard limitations to camera values
   
    private static final double MIN_FOV = 1;
    private static final double MAX_FOV = 80;
    
    // the point to look at
    private double lookAt[] = {0, 0, 0};
    private double eye[] = {0, 5, 10};

   
    // camera parameters
    double fieldOfView      = 45;
    double distanceToOrigin = 5;
    double windowWidth      = 1;
    double windowHeight     = 1;

    // GLU context
    GLU glu = new GLU();

  
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
    public void draw(GL2 gl) {
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        // setting up perspective projection
        // far distance is hardcoded to 3*cameraDistance. If your scene is bigger,
        // you might need to adapt this
        glu.gluPerspective(fieldOfView, windowWidth / windowHeight, 0.1, 150);

        // then set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(
            eye[0], eye[1], eye[2],                // eye
            lookAt[0], lookAt[1], lookAt[2], // center
            0, 1, 0);                        // up
    }

 
    /**
     * Gets the field of view angle of the camera
     * @return the field of view of the camera in degrees
     */
    public double getFieldOfView() {
        return fieldOfView;
    }

    /**
     * Sets the field of view angle of the camera.
     * @param fov the new field of view angle of the camera in degrees
     */
    public void setFieldOfView(double fov) {
        fieldOfView = fov;
        limitFieldOfView();
    }

    /**
     * Limits the field of view angle to a valid range.
     */
    private void limitFieldOfView() {
        if (fieldOfView < MIN_FOV) {
            fieldOfView = MIN_FOV;
        }
        if (fieldOfView > MAX_FOV) {
            fieldOfView = MAX_FOV;
        }
    }

    /**
     * Sets up the lookAt point
     * @param x X coordinate of the lookAt point
     * @param y Y coordinate of the lookAt point
     * @param z Z coordinate of the lookAt point
     */
    public void setLookAt(double x, double y, double z) {
        lookAt = new double[]{x, y, z};
    }
    
    public void setEye(double x, double y, double z){
    	eye = new double[]{x, y, z};
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
