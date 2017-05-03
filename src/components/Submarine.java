package components;

import com.jogamp.opengl.GL2;

import utils.*;
import submarineParts.*;

/**
 * Submarine class. Extends TreeNode. Can be considered the root node for the
 * submarine.
 * 
 * @author Aritra Das
 *
 */
public class Submarine extends TreeNode {

	double length = 0.7;
	double width = 0.2;
	double height = 0.2;
	public double x = 0, y = 0, z = 0;
	private Body body;
	public PropellerAxle axle;
	private PropellerBlade[] blades;
	private Sail sail;
	private Fin fin;
	public double angularspeed = 2;

	public enum Yaw {
		LEFT, RIGHT, CENTER
	};

	public Yaw yaw;

	/** The speed. */
	public double speed = 0;

	public double posx = 0, posy = 0, posz = 0;

	public double angle = 0;

	private double sealevel, bedlevel;

	/**
	 * Constructor for submarine.
	 *
	 * @param sealevel
	 *            y coordinate indicating sealevel
	 * @param bedlevel
	 *            y coordinate indicating seabed
	 */
	public Submarine(double sealevel, double bedlevel) {
		this.sealevel = sealevel;
		this.bedlevel = bedlevel;
		this.body = new Body(length, height, width);
		this.addChild(body);
		this.axle = new PropellerAxle();
		body.addChild(this.axle);
		blades = new PropellerBlade[4];
		for (int i = 0; i < blades.length; i++) {
			blades[i] = new PropellerBlade(i + 1, height);
			axle.addChild(blades[i]);
		}
		axle.setTranslation(length, 0, 0);

		sail = new Sail(length, height, width);
		sail.setTranslation(0, height, 0);
		body.addChild(sail);

		fin = new Fin(length / 7, height, 0.2);
		fin.setTranslation(length * 0.80, 0, 0);
		body.addChild(fin);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#transformNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void transformNode(GL2 gl) {
		propel();
		gl.glTranslated(x, y, z);
		gl.glRotated(angle, 0, 1, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#drawNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void drawNode(GL2 gl) {

	}

	/**
	 * Sets Translation
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Rotates
	 * 
	 * @param angle
	 *            angle to rotate
	 */
	public void setRotation(double angle) {
		this.angle = angle % 360;

	}

	/**
	 * High level function for steering the submarine
	 * 
	 * @param dir
	 *            Direction to steer
	 */
	public void steer(Yaw dir) {
		this.yaw = dir;
		switch (this.yaw) {
		case LEFT: {
			fin.setRotation(25);
			angle += angularspeed;
			break;
		}
		case RIGHT: {
			fin.setRotation(-25);
			angle -= angularspeed;
			break;
		}
		case CENTER: {
			fin.setRotation(0);
			break;
		}
		}
	}

	/**Dives or Surfaces submarine
	 * @param distance distance to dive
	 */
	public void dive(double distance) {
		if (this.y + distance > bedlevel + height && this.y + distance < sealevel) {
			this.y += distance;
			posy = y;

		}
	}

	/**Propels submarine
	 * 
	 */
	public void propel() {
		if (speed != 0) {
			double xtrans = speed * Math.cos(Math.toRadians(angle));
			double ztrans = -speed * Math.sin(Math.toRadians(angle));

			posx += xtrans;
			posz += ztrans;
			this.setTranslation(posx, posy, posz);
			this.axle.rotate(speed);
		}
	}

	/**
	 * Resets submarine position
	 */
	public void reset() {
		posx = 0;
		posy = bedlevel + height + 0.065;
		x = 0;
		y = posy;
		z = 0;
		posz = 0;
		speed = 0;
		angle = 0;

	}

}
