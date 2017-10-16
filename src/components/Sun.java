package components;

import com.jogamp.opengl.GL2;

import utils.ColorPalette;

/**
 * The Class Sun. Represents the sun
 */
public class Sun {

	/** The time. */
	public double time = 0;
	
	/** The moving. */
	public boolean moving = false;
	
	/** The speed. */
	private double speed = 1;

	/**
	 * Draws the sun
	 *
	 * @param gl the drawable
	 */
	public void draw(GL2 gl) {

		double theta = time % 360;
		float sunZ = 10 * (float) Math.cos(Math.toRadians(theta));
		float sunX = 10 * (float) Math.sin(Math.toRadians(theta));
		float sunY = 10 * (float) Math.cos(Math.toRadians(60));
		float position2[] = { sunX, sunY, sunZ, 0 };
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, position2, 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, ColorPalette.withalpha(ColorPalette.Black, 0.2f), 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, ColorPalette.withalpha(ColorPalette.light1, 1f), 0);
		gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, ColorPalette.withalpha(ColorPalette.white2, 1f), 0);

		// enable lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT2);

		// normalize the normals
		gl.glEnable(GL2.GL_NORMALIZE);
		if (moving) {
			time += speed;
			if (time >= 360) {
				time = 0;
			}
		}
	}

}
