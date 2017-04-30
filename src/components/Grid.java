package components;

import com.jogamp.opengl.GL2;
import utils.*;

public class Grid {

	private double y;
	private double size;
	private float[] color = new float[4];
	private int displayList = -1;
	private boolean isWireframe = true;
	private double tmin, tmax;
	private double worldsize = 100;

	public Grid(double y, double size, float[] color, double tmin, double tmax) {
		this.y = y;
		this.color = color;
		this.size = size;
		this.tmax = tmax;
		this.tmin = tmin;
	}

	private void initialiseDisplayList(GL2 gl, boolean isWireframe) {
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL2.GL_COMPILE);
		drawGrid(gl,isWireframe);
		gl.glEndList();
	}

	public void drawGrid(GL2 gl,  boolean isWireframe) {

		gl.glColor4fv(color, 0);
		double imax = (tmax - tmin);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		for (double z = 0; z <= imax; z += size / 2) {
			for (double x = 0; x <= imax; x += size / 2) {

				double scaledx = Misc.scale(x, 0, imax, -worldsize/2, worldsize/2);
				double scaledz = Misc.scale(z, 0, imax, -worldsize/2, worldsize/2);

				drawCell(gl, scaledx, y, scaledz, size, isWireframe);

			}
		}
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_BLEND);
	}

	public void drawCell(GL2 gl, double x, double y, double z, double size, boolean isWireframe) {
		gl.glBegin((isWireframe) ? GL2.GL_LINE_LOOP : GL2.GL_QUADS);
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

	public void draw(GL2 gl,boolean isWireframe) {
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
