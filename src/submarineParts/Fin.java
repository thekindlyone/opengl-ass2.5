
package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.*;

public class Fin extends TreeNode {
	double length, height, width;
	boolean isWireframe = true;
	private int displayList = -1;
	double x, y, z;
	double angle = 0;

	public Fin(double length, double height, double width) {
		this.length = length;
		this.width = width;
		this.height = height;
	}

	private void initialiseDisplayList(GL2 gl, boolean isWireframe) {
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		glu.gluQuadricDrawStyle(quadric, (isWireframe) ? GLU.GLU_LINE : GLU.GLU_FILL);
		gl.glPushMatrix();
		gl.glScaled(1, 1, width);
		gl.glRotated(-90, 1, 0, 0);
		glu.gluCylinder(quadric, length, length / 2, height, 20, 20);
		gl.glPopMatrix();
		gl.glEndList();
	}

	@Override
	public void transformNode(GL2 gl) {
		// System.out.println("transformnode called");
		gl.glTranslated(x, y, z);
		gl.glRotated(angle, 0, 1, 0);

	}

	@Override
	public void drawNode(GL2 gl, boolean isWireframe) {
		// System.out.println("drawNode called");
		if (displayList < 0 || isWireframe != this.isWireframe) {
			// if not initialised, do it now
			initialiseDisplayList(gl, isWireframe);
		}
		this.isWireframe = isWireframe;
		gl.glPushMatrix();
		gl.glCallList(displayList);
		gl.glPopMatrix();

	}

	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setRotation(double angle){
		this.angle = angle;
	}

}
