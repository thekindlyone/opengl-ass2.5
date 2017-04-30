package scene;


/**

 * Simple camera
 * @author Jacqueline Whalley
 */


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;



public class Camera {


    public void draw(GL2 gl) {
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
  }

 
  


   
}
