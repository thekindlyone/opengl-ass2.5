package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import utils.*;

/**Creates and draws submarine body
 * @author Aritra Das
 *
 */
public class Body extends TreeNode {

	double length ;
	double width ;
	double height ;
	int displayList = -1;
	// translation parameters
	private double x, y, z;

	/**Constructor for submarine body
	 * @param length length of sub
	 * @param height height of sub
	 * @param width width of sub
	 */
	public Body(double length, double height, double width) {
		this.length = length;
		this.width = width;
		this.height = height;
	}


	/**Creates and stores displaylist
	 * @param gl opengl drawable
	 */
	private void initialiseDisplayList(GL2 gl) {
		// create the quadric
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glColor4d(1, 1, 1, 1);
		gl.glLineWidth(1.0f);
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		gl.glScaled(length, height, width);
		gl.glColor3fv(ColorPalette.Gray,0);
		glu.gluSphere(quadric, 1, 25, 25);
		gl.glEndList();
	}

	/**Sets Translation
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see utils.TreeNode#transformNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void transformNode(GL2 gl) {
		gl.glTranslated(x, y, z);
	}

	/* (non-Javadoc)
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

}
