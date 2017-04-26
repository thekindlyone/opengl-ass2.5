package components;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import utils.*;

public class Origin {

	public static void drawAxes(GL2 gl) {
		GLUT glut = new GLUT();	
		//axes
		gl.glLineWidth(2.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3fv(ColorPalette.Red, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(2, 0, 0);
		gl.glColor3fv(ColorPalette.Blue, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 2, 0);
		gl.glColor3fv(ColorPalette.Green, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, 2);
		gl.glEnd();
		//sphere at origin
		gl.glColor3d(0, 1, 0);
		glut.glutSolidSphere(0.05f, 50, 50);
	}

}
