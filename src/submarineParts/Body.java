package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import utils.*;

public class Body extends TreeNode {

	double length ;
	double width ;
	double height ;
	int displayList = -1;
	boolean isWireframe = true;
	// translation parameters
	private double x, y, z;

	public Body(double length, double height, double width) {
		this.length = length;
		this.width = width;
		this.height = height;
	}


	private void initialiseDisplayList(GL2 gl, boolean isWireframe) {
		// System.out.println("displaylist initialized");
		// create the quadric
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glColor4d(1, 1, 1, 1);
		gl.glLineWidth(1.0f);
		glu.gluQuadricDrawStyle(quadric, (isWireframe) ? GLU.GLU_LINE : GLU.GLU_FILL);
		gl.glScaled(length, height, width);
		gl.glColor3fv(ColorPalette.Gray,0);
		glu.gluSphere(quadric, 1, 25, 25);
		gl.glEndList();
	}

	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void transformNode(GL2 gl) {
		// System.out.println("transformnode called");
		gl.glTranslated(x, y, z);
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

}
