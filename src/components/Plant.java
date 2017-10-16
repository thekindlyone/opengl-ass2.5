package components;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

import utils.*;

/**
 * @author Aritra Das
 * 
 * Plant class. Created and draws a fractal 3D plant
 *
 */
public class Plant {
	public double branchangle;
	int displayList = -1;
	Point startpoint;
	double anglex, angley, length;
	int depth, density;
	double anglefuzz;
	Texture texture;
	MaterialProfile bulbmaterial;
	
	public Point bboxmin,bboxmax;

	/**
	 * @param p start point
	 * @param anglex start x angle
	 * @param angley start y angle 
	 * @param length branch length
	 * @param branchangle branch angle
	 * @param density density of tree
	 * @param anglefuzz angle variation of branches
	 * @param depth depth of tree
	 * @param texture texture for branches
	 */
	public Plant(Point p, double anglex, double angley, double length, double branchangle, int density,
			double anglefuzz, int depth, Texture texture) {
		startpoint = p;
		this.anglex = anglex;
		this.angley = angley;
		this.length = length;
		this.density = density;
		this.anglefuzz = anglefuzz;
		this.depth = depth;
		this.branchangle = branchangle;
		this.texture = texture;
		this.bulbmaterial = new MaterialProfile(ColorPalette.withalpha(ColorPalette.Black, 1), // ambient
				ColorPalette.withalpha(ColorPalette.Black, 1f), // diffuse
				ColorPalette.withalpha(ColorPalette.Black, 1), // specular
				new float[] { 2.0f }, // shininess
				ColorPalette.withalpha(ColorPalette.randomcolor(), 1) // emissive
		);
		
		this.bboxmin=new Point(2000,2000,2000);
		this.bboxmax=new Point(-2000,-2000,-2000);


	}

	public void create(GL2 gl, Point p, double anglex, double angley, double length, int depth) {
		boolean end_node = false;
		if (depth == 0)
			return;
		if (depth == 1)
			end_node = true;
		Point next = p.get_translated(anglex, angley, length);
		if (next.x<=bboxmin.x){bboxmin.x=next.x;}
		if (next.y<=bboxmin.y){bboxmin.y=next.y;}
		if (next.z<=bboxmin.z){bboxmin.z=next.z;}
		if (next.x>=bboxmax.x){bboxmax.x=next.x;}
		if (next.y>=bboxmax.y){bboxmax.y=next.y;}
		if (next.z>=bboxmax.z){bboxmax.z=next.z;}
		
		draw_branch(gl, p, next, anglex, angley, length, end_node);
		for (int i = 1; i <= 2; i++) {
			for (int j = 1; j <= 2; j++) {
				if (Misc.get_rand(1, 100) < density) {
					create(gl, next, anglex + Math.pow(-1, i) * branchangle * j * Misc.get_rand(1, anglefuzz),
							angley - Math.pow(-1, i) * branchangle * j * Misc.get_rand(1, anglefuzz), length / 2,
							depth - 1);
				}
				if (Misc.get_rand(1, 100) < density) {
					create(gl, next, anglex + Math.pow(-1, i) * branchangle * j * Misc.get_rand(1, anglefuzz),
							angley + Math.pow(-1, i) * branchangle * j * Misc.get_rand(1, anglefuzz), length / 2,
							depth - 1);
				}
			}
		}

	}

	public void draw_branch(GL2 gl, Point p, Point q, double anglex, double angley, double length, boolean end_node) {

		GLU glu;
		GLUquadric quadric;
		glu = new GLU();
		quadric = glu.gluNewQuadric();
		glu.gluQuadricTexture(quadric, true);
		gl.glColor3d(1, 1, 1);
		texture.bind(gl);
		texture.enable(gl);
		// set clamping parameters
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

		gl.glPushMatrix();
		gl.glTranslated(p.x, p.y, p.z);
		gl.glRotated(anglex, 0, 1, 0);
		gl.glRotated(-angley, 1, 0, 0);
	
		glu.gluCylinder(quadric, 0.01, 0.01, length, 5, 5);
		gl.glPopMatrix();
		texture.disable(gl);

		if (end_node) {

			Bulb bulb = new Bulb(q.x, q.y, q.z, 0.02, bulbmaterial);
			bulb.draw(gl);
		}

	}

	// old
	public void draw_branch2(GL2 gl, Point p, Point q, double anglex, double angley, double length, boolean end_node) {
		gl.glBegin(GL2.GL_LINES);

		gl.glColor3d(1, 1, 1);
		gl.glLineWidth(3f);

		gl.glVertex3d(p.x, p.y, p.z);
		gl.glVertex3d(q.x, q.y, q.z);
		gl.glEnd();
	}

	private void initialiseDisplayList(GL2 gl) {
		// create the display list
		displayList = gl.glGenLists(1);
		// compile data in the display list
		gl.glNewList(displayList, GL2.GL_COMPILE);
		create(gl, startpoint, anglex, angley, length, depth);
		gl.glEndList();
	}

	public void diagnose(Point p, double ax, double ay) {

		Point another = p.get_translated(ax, ay, 10);

		System.out.println(another.x + "," + another.y + "," + another.z);

	}

	public void drawPlant(GL2 gl) {
//		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColor3fv(ColorPalette.White, 0);
		if (displayList < 0) {
			// if not initialised, do it now
			initialiseDisplayList(gl);
		}
		gl.glPushMatrix();
		gl.glCallList(displayList);
		gl.glPopMatrix();
//		gl.glDisable(GL2.GL_COLOR_MATERIAL);
	}

}
