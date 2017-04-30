package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.*;

public class PropellerBlade extends TreeNode {

	private int displayList = -1;
	boolean isWireframe = true;
	private double x, y, z;
	double bladelength = 1.6;
	double bladewidth = 0.5;
	double bladethickness = 0.01;
	double subheight;
	int bladeno;

	public PropellerBlade(int bladeno, double subheight) {
		this.bladeno = bladeno;
		this.subheight = subheight;
	}

	private void initialiseDisplayList(GL2 gl, boolean isWireframe) {
		GLUT glut = new GLUT();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glColor3fv(ColorPalette.Blue, 0);
		gl.glLineWidth(1.0f);

		gl.glPushMatrix();
		gl.glRotated((360 / 4) * bladeno, 1, 0, 0);
		gl.glScaled(1, subheight / bladelength, subheight / bladelength);
		gl.glScaled(bladethickness, 1, 1);
		gl.glPushMatrix();
		gl.glRotated(180, 1, 0, 0);
		gl.glTranslated(0, 0, -bladelength);
		if (isWireframe) {
			glut.glutWireCone(bladewidth, bladelength, 20, 20);
		} else {
			glut.glutSolidCone(bladewidth, bladelength, 20, 20);
		}
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0, 0, bladelength);
		if (isWireframe) {
			glut.glutWireSphere(bladewidth, 20, 20);
		} else {
			glut.glutSolidSphere(bladewidth, 20, 20);
		}
		gl.glPopMatrix();

		gl.glPopMatrix();

		gl.glEndList();
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

	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
