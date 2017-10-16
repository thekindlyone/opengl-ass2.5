package components;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import utils.*;

public class Bulb {
	double x,y,z,radius;
	MaterialProfile material;
	
	public Bulb(double x, double y, double z, double radius, MaterialProfile material){
		this.x=x;
		this.y=y;
		this.z=z;
		this.radius=radius;
		this.material=material;
	}
	
	public void draw(GL2 gl){
//		gl.glDisable(GL2.GL_COLOR_MATERIAL);
		GLU glu = new GLU();
		GLUquadric quadric = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
		
		
		gl.glPushAttrib(GL2.GL_LIGHTING_BIT);
		gl.glPushMatrix();
		material.apply(gl);
		gl.glTranslated(x, y, z);
		gl.glScaled(radius, radius, radius);
		glu.gluSphere(quadric, 1, 25, 25);
		gl.glPopAttrib();
		gl.glPopMatrix();
//		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}

}
