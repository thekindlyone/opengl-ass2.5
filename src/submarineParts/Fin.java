
package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import utils.*;

/**Creates and draws submarine fin
 * @author Aritra Das
 *
 */
public class Fin extends TreeNode {
	double length, height, width;
	private int displayList = -1;
	double x, y, z;
	double angle = 0;

	/**Constructor for Fin
	 * @param length length of fin
	 * @param height height of fin
	 * @param width width of fin
	 */
	public Fin(double length, double height, double width) {
		this.length = length;
		this.width = width;
		this.height = height;
	}

	/**Create and store displaylist
	 * @param gl opengl drawable
	 */
	private void initialiseDisplayList(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glPushMatrix();
		gl.glScaled(1, 1, width);
		gl.glRotated(-90, 1, 0, 0);
		glu.gluCylinder(quadric, length, length / 2, height, 20, 20);
		gl.glPopMatrix();
		gl.glEndList();
	}

	/* (non-Javadoc)
	 * @see utils.TreeNode#transformNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void transformNode(GL2 gl) {
		// System.out.println("transformnode called");
		gl.glTranslated(x, y, z);
		gl.glRotated(angle, 0, 1, 0);

	}

	/* (non-Javadoc)
	 * @see utils.TreeNode#drawNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void drawNode(GL2 gl) {
		// System.out.println("drawNode called");
		if (displayList < 0) {
			// if not initialised, do it now
			initialiseDisplayList(gl);
		}
		gl.glPushMatrix();
		gl.glCallList(displayList);
		gl.glPopMatrix();

	}

	/**Sets tranlation
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**Rotates
	 * @param angle angle to rotate
	 */
	public void setRotation(double angle) {
		this.angle = angle;
	}

}
