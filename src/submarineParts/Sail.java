package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.*;

/**
 * Class for creating and drawing the submarine sail
 * 
 * @author Aritra Das
 *
 */
public class Sail extends TreeNode {
	double length, height, width;
	private int displayList = -1;
	double x, y, z;

	/**
	 * Constructor for Sail
	 * 
	 * @param length
	 *            length of sail
	 * @param height
	 *            height of sail
	 * @param width
	 *            width of sail
	 */
	public Sail(double length, double height, double width) {
		this.length = length;
		this.width = width;
		this.height = height;
	}

	/**
	 * Create and store displaylist
	 * 
	 * @param gl
	 *            opengl drawable
	 */
	private void initialiseDisplayList(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		GLUT glut = new GLUT();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glColor4d(1, 1, 1, 1);
		gl.glLineWidth(1.0f);
		gl.glPushMatrix();
		gl.glScaled(length / 4, height / 2, width / 4);
		gl.glColor3fv(ColorPalette.Teal, 0);
		glut.glutSolidCube(1);
		gl.glPopMatrix();
		// gl.glColor3fv(ColorPalette.white2,0);
		gl.glPushMatrix();
		gl.glRotated(20, 0, 1, 0);
		gl.glTranslated(0, 0, (width / 4));
		gl.glScaled(length / 8, height / 6, width / 1.5);
		glut.glutSolidCube(1);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glRotated(-20, 0, 1, 0);
		gl.glTranslated(0, 0, -(width / 4));
		gl.glScaled(length / 8, height / 6, width / 1.5);
		glut.glutSolidCube(1);
		gl.glPopMatrix();

		gl.glColor3fv(ColorPalette.Gray, 0);
		gl.glPushMatrix();
		gl.glTranslated(0, height, 0);
		gl.glScaled(0.01, 0.2, 0.01);
		gl.glRotated(90, 1, 0, 0);
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		glu.gluCylinder(quadric, 1, 1, 1, 20, 20);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(0.05, 1.5 * height, 0);
		gl.glScaled(0.01, 0.25, 0.01);
		gl.glRotated(90, 1, 0, 0);
		glu.gluCylinder(quadric, 1, 1, 1, 20, 20);
		gl.glPopMatrix();
		gl.glEndList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#transformNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void transformNode(GL2 gl) {
		gl.glTranslated(x, y, z);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#drawNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void drawNode(GL2 gl) {
		if (displayList < 0) {
			// if not initialised, do it now
			initialiseDisplayList(gl);
		}
		gl.glPushMatrix();
		gl.glCallList(displayList);
		gl.glPopMatrix();

	}

	/**
	 * Sets translation
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
