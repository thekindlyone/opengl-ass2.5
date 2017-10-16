package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.*;

/**
 * The propeller axle
 * 
 * @author Aritra Das
 *
 */
public class PropellerAxle extends TreeNode {
	double angle = 0;
	double speed;
	boolean clockwise;
	int speedcoeff = 600;

	private double x, y, z;

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#transformNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void transformNode(GL2 gl) {
		gl.glRotated(angle, 1, 0, 0);
		gl.glTranslated(x, y, z);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#drawNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void drawNode(GL2 gl) {
		GLUT glut = new GLUT();
		gl.glColor3fv(ColorPalette.Yellow, 0);
		gl.glPushMatrix();
		gl.glRotated(90, 0, 1, 0);
		glut.glutSolidCone(0.03, 0.03, 10, 10);
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

	/**
	 * Rotates
	 * 
	 * @param angle
	 *            angle to rotate
	 */
	public void rotate(double speed) {

		this.angle += speed * speedcoeff;

	}

}
