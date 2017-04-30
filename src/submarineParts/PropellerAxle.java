package submarineParts;

import com.jogamp.opengl.GL2;

import utils.*;

public class PropellerAxle extends TreeNode {
	double angle = 0;
	double speed;
	boolean clockwise;
	int speedcoeff = 600;

	private double x, y, z;

	@Override
	public void transformNode(GL2 gl) {
//		if (speed != 0) {
//			rotate();
//			
//		}
		gl.glRotated(angle, 1, 0, 0);
		gl.glTranslated(x, y, z);

	}

	@Override
	public void drawNode(GL2 gl, boolean isWireframe) {
		// NOTHING TO DRAW

	}

	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void rotate(double speed) {
        
		this.angle += speed*speedcoeff;

	}
	


}
