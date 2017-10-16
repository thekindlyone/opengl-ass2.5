package utils;

import com.jogamp.opengl.GL2;

/**
 * @author Aritra Das
 *
 */
public class MaterialProfile {
	/**
	 * Defines a material
	 * 
	 */
	private float[] ambient, diffuse, specular, shininess, emissive;

	/**
	 * @param ambient color
	 * @param diffuse color
	 * @param specular color
	 * @param shininess color
	 */
	public MaterialProfile(float[] ambient, float[] diffuse, float[] specular, float[] shininess) {

		this(ambient, diffuse, specular, shininess, new float[] { 0, 0, 0 });
	}

	/**
	 * @param ambient color
	 * @param diffuse color
	 * @param specular color
	 * @param shininess color
	 * @param emissive color
	 */
	public MaterialProfile(float[] ambient, float[] diffuse, float[] specular, float[] shininess, float[] emissive) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shininess = shininess;
		this.emissive = emissive;
	}

	/** applies material
	 * @param gl drawable
	 */
	public void apply(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emissive, 0);
	}

	/**resets material state
	 * @param gl drawable
	 */
	public static void reset(GL2 gl) {
		float[] ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
		float[] diffuse = { 0.8f, 0.8f, 0.8f, 1.0f };
		float[] specular = { 0f, 0f, 0f, 1.0f };
		float[] emissive = { 0f, 0f, 0f, 1.0f };
		float[] shininess = { 0f };

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emissive, 0);

	}
}
