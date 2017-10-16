package components;

import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

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
	private int displayList = -1;
	int dim;
	boolean terrain;
	Texture texture;
	double[][] heightmap;
	public Point min;
	public Point max;
	public double maxy = -2000;
	public double miny = 2000;
	public double hrange;
	int texsize;
	float[] color;
	Vector[][][] data;

	public Grid(double y, double size, int dim, float[] color, Texture texture, int texsize, boolean terrain) {
		this.y = y;
		this.texture = texture;
		this.terrain = terrain;
		this.dim = (int) Math.pow(2, (int) (Math.log10(dim) / Math.log10(2))) + 1; // force
																					// 2^n+1
																					// form
																					// of
																					// dim
		this.color = color;
		this.texsize = texsize / 100;
		this.size = size;
		double min = -(this.dim - 1) * size / 2.0;
		this.min = new Point(min,-2000,min);
		double max = +(this.dim - 1) * size / 2.0;
		this.max = new Point(max,2000,max);
		this.hrange = (this.dim*1.5)/33;
		if (terrain) {
			data = new Vector[dim][dim][2];
			heightmap = FractalSurface.generate(y, (this.dim), hrange);
			for (int i = 0; i < this.dim; i++) {
				for (int j = 0; j < this.dim; j++) {
					if (heightmap[i][j] >= maxy) {
						maxy = heightmap[i][j];
					}
					if (heightmap[i][j] <= miny) {
						miny = heightmap[i][j];
					}
				}
			}
		}

	}

	public void drawGrid(GL2 gl) {
		if (!terrain) {
			draw_plain(gl);
			return;
		}
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		// gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColor4fv(color, 0);
		
		MaterialProfile terrainmaterial = new MaterialProfile(
				new float[]{1,1,1,1}, //ambient, 
				new float[]{1,1,1,1},// diffuse, 
				new float[]{0.4f,0.4f,0.4f,1}, //specular 
				new float[]{5} //shininess
				);
		
		terrainmaterial.apply(gl);
		
		texture.isUsingAutoMipmapGeneration();
		// Use linear filter for texture if image is larger than the original
		// texture
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		// Use linear filter for texture if image is smaller than the original
		// texture
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		texture.bind(gl);
		texture.enable(gl);

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				double x = Misc.scale(i, 0, dim - 1, -(dim - 1) * size / 2.0, +(dim - 1) * size / 2.0);
				double z = Misc.scale(j, 0, dim - 1, -(dim - 1) * size / 2.0, +(dim - 1) * size / 2.0);

				double ha = getheight(i, j);
				double hb = getheight(i + 1, j);
				double hd = getheight(i, j + 1);
				double hf = getheight(i - 1, j);
				double hh = getheight(i, j - 1);
				Vector a = new Vector(x, ha, z);
				Vector b = new Vector(x + size, hb, z);
				Vector d = new Vector(x, hd, z + size);
				Vector f = new Vector(x - size, hf, z);
				Vector h = new Vector(x, hh, z - size);

				Vector normal = Vector.get_average_4((d.minus(a).cross(b.minus(a))).normalized(),
						(b.minus(a).cross(h.minus(a))).normalized(), (h.minus(a).cross(f.minus(a))).normalized(),
						(f.minus(a).cross(d.minus(a))).normalized());


				data[i][j][0] = a;
				data[i][j][1] = normal;
			}
		}

		double textquad = 0;
		double pieces = 12;
		for (int i = 0; i < dim - 1; i++) {
			for (int j = 0; j < dim - 1; j++) {

				Vector a = getpoint(i, j);
				Vector na = getnormal(i, j);

				Vector b = getpoint(i, j + 1);
				Vector nb = getnormal(i, j + 1);

				Vector c = getpoint(i + 1, j + 1);
				Vector nc = getnormal(i + 1, j + 1);

				Vector d = getpoint(i + 1, j);
				Vector nd = getnormal(i + 1, j);
				gl.glBegin(GL2.GL_QUADS);

				gl.glNormal3d(na.x, na.y, na.z);
				gl.glTexCoord2d(textquad * (1 / pieces), textquad * (1 / pieces));
				gl.glVertex3d(a.x, a.y, a.z);

				gl.glNormal3d(nb.x, nb.y, nb.z);
				gl.glTexCoord2d(textquad * (1 / pieces), (textquad + 1) * (1 / pieces));
				gl.glVertex3d(b.x, b.y, b.z);

				gl.glNormal3d(nc.x, nc.y, nc.z);
				gl.glTexCoord2d((textquad + 1) * (1 / pieces), (textquad + 1) * (1 / pieces));
				gl.glVertex3d(c.x, c.y, c.z);

				gl.glNormal3d(nd.x, nd.y, nd.z);
				gl.glTexCoord2d((textquad + 1) * (1 / pieces), textquad * (1 / pieces));
				gl.glVertex3d(d.x, d.y, d.z);
				gl.glEnd();
				textquad++;
				textquad = textquad % (pieces);

				// displays normals. Not deleted on purpose

				// if(shownormals){
				// gl.glBegin(GL2.GL_LINES);
				//
				// double len = 0.5;
				// Vector normalend = a.plus(na.scalarprod(len));
				// gl.glVertex3d(a.x,a.y,a.z);
				// gl.glVertex3d(normalend.x, normalend.y, normalend.z);
				//
				//
				// normalend = b.plus(nb.scalarprod(len));
				// gl.glVertex3d(b.x,b.y,b.z);
				// gl.glVertex3d(normalend.x, normalend.y, normalend.z);
				//
				//
				// normalend = c.plus(nc.scalarprod(len));
				// gl.glVertex3d(c.x,c.y,c.z);
				// gl.glVertex3d(normalend.x, normalend.y, normalend.z);
				//
				// normalend = d.plus(nd.scalarprod(len));
				// gl.glVertex3d(d.x,d.y,d.z);
				// gl.glVertex3d(normalend.x, normalend.y, normalend.z);
				//
				//
				//
				// gl.glEnd();}

			}
		}

		texture.disable(gl);
		gl.glDisable(GL2.GL_BLEND);
	}

	public void draw_plain(GL2 gl) {
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4fv(color, 0);
		// Use linear filter for texture if image is larger than the original
		// texture
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		// Use linear filter for texture if image is smaller than the original
		// texture
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		texture.bind(gl);
		texture.enable(gl);
		gl.glBegin(GL2.GL_QUADS);
		double textquad = 0;
		double pieces = 12;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				double x = Misc.scale(i, 0, dim - 1, -(dim - 1) * size / 2.0, +(dim - 1) * size / 2.0);
				double z = Misc.scale(j, 0, dim - 1, -(dim - 1) * size / 2.0, +(dim - 1) * size / 2.0);
				Vector a = new Vector(x, y, z);
				Vector b = new Vector(x, y, z + size);
				Vector c = new Vector(x + size, y, z + size);
				Vector d = new Vector(x + size, y, z);

				gl.glNormal3d(0, 1, 0);
				gl.glTexCoord2d(textquad * (1 / pieces), textquad * (1 / pieces));
				gl.glVertex3d(a.x, a.y, a.z);

				gl.glTexCoord2d(textquad * (1 / pieces), (textquad + 1) * (1 / pieces));
				gl.glVertex3d(b.x, b.y, b.z);

				gl.glTexCoord2d((textquad + 1) * (1 / pieces), (textquad + 1) * (1 / pieces));
				gl.glVertex3d(c.x, c.y, c.z);

				gl.glTexCoord2d((textquad + 1) * (1 / pieces), textquad * (1 / pieces));
				gl.glVertex3d(d.x, d.y, d.z);

				textquad++;
				textquad = textquad % (pieces);
			}
		}
		gl.glEnd();

		texture.disable(gl);
		gl.glDisable(GL2.GL_BLEND);
	}

	private void initialiseDisplayList(GL2 gl) {
		displayList = gl.glGenLists(1);
		gl.glNewList(displayList, GL2.GL_COMPILE);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		drawGrid(gl);
		gl.glDisable(GL2.GL_COLOR_MATERIAL);
		gl.glEndList();
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

	public double getheight(int i, int j) {
		return heightmap[Math.abs(i % dim)][Math.abs(j % dim)];
	}

	public Vector getpoint(int i, int j) {
		return data[Math.abs(i % dim)][Math.abs(j % dim)][0];
	}

	public Vector getnormal(int i, int j) {
		return data[Math.abs(i % dim)][Math.abs(j % dim)][1];
	}

}
