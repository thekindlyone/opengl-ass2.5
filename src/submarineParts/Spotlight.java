package submarineParts;

import com.jogamp.opengl.GL2;

import utils.ColorPalette;
import utils.*;






public class Spotlight extends TreeNode {
	
	float[] pos;
	
	
	public Spotlight(float[]pos){
		this.pos=pos;
	}


	@Override
	public void transformNode(GL2 gl) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void drawNode(GL2 gl) {
		// TODO Auto-generated method stub
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, pos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ColorPalette.withalpha(ColorPalette.Black, 1), 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, ColorPalette.withalpha(ColorPalette.white2, 0.5f), 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, ColorPalette.withalpha(ColorPalette.White, 1f), 0);
		gl.glLightfv(GL2.GL_LIGHT1,GL2.GL_SPOT_DIRECTION, new float[]{0,-1,0},0);
		gl.glLightf(GL2.GL_LIGHT1,GL2.GL_SPOT_EXPONENT, 20.0f);
		gl.glLightf(GL2.GL_LIGHT1,GL2.GL_SPOT_CUTOFF,60.0f);
	}
	
	

}
