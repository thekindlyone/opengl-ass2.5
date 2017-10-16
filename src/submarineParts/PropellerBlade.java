package submarineParts;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.*;

/**Class to create and draw Propeller Blades
 * @author Aritra Das
 *
 */
public class PropellerBlade extends TreeNode {

	private int displayList = -1;
	private double x, y, z;
	double bladelength = 1.6;
	double bladewidth = 0.5;
	double bladethickness = 0.01;
	double subheight;
	int bladeno;

	/** Constructor for Propeller Blades
	 * @param bladeno The index number of blade
	 * @param subheight height of submarine
	 */
	public PropellerBlade(int bladeno, double subheight) {
		this.bladeno = bladeno;
		this.subheight = subheight;
	}
	/**Create and store displaylist
	 * @param gl opengl drawable
	 */
	private void initialiseDisplayList(GL2 gl) {
		GLUT glut = new GLUT();
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glColor3fv(ColorPalette.Blue, 0);
		gl.glLineWidth(1.0f);

		gl.glPushMatrix();
		gl.glRotated((360 / 4) * bladeno, 1, 0, 0);
		gl.glRotated(45, 0, 0, 1);
		gl.glScaled(1, subheight / bladelength, subheight / bladelength);
		gl.glScaled(bladethickness, 1, 1);
		gl.glPushMatrix();
		
		

		gl.glRotated(180, 1, 0, 0);
		gl.glTranslated(0, 0, -bladelength);
		glut.glutSolidCone(bladewidth, bladelength, 20, 20);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0, 0, bladelength);
		glut.glutSolidSphere(bladewidth, 20, 20);
		gl.glPopMatrix();

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
	/**Sets translation
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
