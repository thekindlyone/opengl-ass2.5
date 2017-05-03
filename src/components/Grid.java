package components;

import com.jogamp.opengl.GL2;
import utils.*;

/**
 * Class that draws Grid on x-z plane
 * 
 * @author Aritra Das
 *
 */
public class Grid {

	private double y;
	private double size;
	private float[] color = new float[4];
	private int displayList = -1;
	private double tmin, tmax;
	private double worldsize = 100;

	/**
	 * Constructor of grid
	 * 
	 * @param y
	 *            y coordinate of grid on x-z plane
	 * @param size
	 *            size of grid
	 * @param color
	 *            color of grid
	 * @param tmin
	 *            minimum x and z of grid
	 * @param tmax
	 *            maximum x and z of grid
	 */
	public Grid(double y, double size, float[] color, double tmin, double tmax) {
		this.y = y;
		this.color = color;
		this.size = size;
		this.tmax = tmax;
		this.tmin = tmin;
	}

	/**
	 * Stores figure as displaylist
	 * 
	 * @param gl
	 *            opengl drawable
	 */
	private void initialiseDisplayList(GL2 gl) {
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL2.GL_COMPILE);
		drawGrid(gl);
		gl.glEndList();
	}

	/**
	 * Draws the grid
	 * 
	 * @param gl
	 *            opengl drawable
	 */
	public void drawGrid(GL2 gl) {

		gl.glColor4fv(color, 0);
		double imax = (tmax - tmin);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		for (double z = 0; z <= imax; z += size / 2) {
			for (double x = 0; x <= imax; x += size / 2) {

				double scaledx = Misc.scale(x, 0, imax, -worldsize / 2, worldsize / 2);
				double scaledz = Misc.scale(z, 0, imax, -worldsize / 2, worldsize / 2);

				drawCell(gl, scaledx, y, scaledz, size);

			}
		}
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_BLEND);
	}

	/**
	 * Draws cell at specified coordinates
	 * 
	 * @param gl
	 *            opengl drawable
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param size
	 *            size of cell
	 */
	public void drawCell(GL2 gl, double x, double y, double z, double size) {
		gl.glBegin(GL2.GL_QUADS);
		gl.glNormal3d(0, 1, 0);
		gl.glVertex3d(x, y, z);
		gl.glNormal3d(0, 1, 0);
		gl.glVertex3d(x, y, z + size);
		gl.glNormal3d(0, 1l, 0);
		gl.glVertex3d(x - size, y, z + size);
		gl.glNormal3d(0, 1, 0);
		gl.glVertex3d(x - size, y, z);
		gl.glEnd();
	}

	/**
	 * Draws stored displaylist
	 * 
	 * @param gl
	 *            opengl drawable
	 */
	public void draw(GL2 gl) {
		if (displayList < 0) {
			// if not initialised, do it now
			initialiseDisplayList(gl);
		}
		gl.glPushMatrix();
		gl.glCallList(displayList);
		gl.glPopMatrix();

	}

}
