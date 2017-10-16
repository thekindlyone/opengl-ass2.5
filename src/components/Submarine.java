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

	public double length = 0.4;
	double width = 0.15;
	double height = 0.15;
	public double x = 0, y = 0, z = 0;
	private Body body;
	public PropellerAxle axle;
	private PropellerBlade[] blades;
	private Spotlight spotlight;
	private Sail sail;
	private Fin fin;
	public double angularspeed = 2;
	public double xangle = 0;
	public double zangle = 0;
	private int numplants;

	public enum Yaw {
		LEFT, RIGHT, CENTER
	};

	public Yaw yaw;

	/** The speed. */
	public double speed = 0;

	public double posx = 0, posy = 0, posz = 0;

	public double angle = 0;

	private double sealevel, bedlevel;
	private static double roll = 10;
	private static double pitch = 10;

	// public boolean diving = false;
	public enum DiveState {
		UP, DOWN, NONE
	};

	public DiveState dstate = DiveState.NONE;
	private MaterialProfile submaterial;
	private Plant[] plants;
	Point worldmax,worldmin;

	/**
	 * Constructor for submarine.
	 *
	 * @param sealevel
	 *            y coordinate indicating sealevel
	 * @param bedlevel
	 *            y coordinate indicating seabed
	 */
	public Submarine(MaterialProfile submaterial, double sealevel, double bedlevel, Point worldmin,Point worldmax,int numplants, Plant[] plants) {
		this.plants = plants;
		this.numplants = numplants;
		this.submaterial = submaterial;
		this.sealevel = sealevel;
		this.bedlevel = bedlevel;
		this.worldmax=worldmax;
		this.worldmin=worldmin;
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

		this.spotlight = new Spotlight(new float[] { -1.5f, 0, 0, 1 });
		body.addChild(spotlight);

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

		switch (dstate) {
		case NONE: {
			break;
		}
		case UP: {
			gl.glRotated(-pitch, 0, 0, 1);
			break;
		}
		case DOWN: {
			gl.glRotated(pitch, 0, 0, 1);
			break;
		}
		}

		gl.glRotated(xangle, 1, 0, 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.TreeNode#drawNode(com.jogamp.opengl.GL2)
	 */
	@Override
	public void drawNode(GL2 gl) {
		submaterial.apply(gl);
		gl.glColor3fv(ColorPalette.White, 0);

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
			xangle = roll;
			break;
		}
		case RIGHT: {
			fin.setRotation(-25);
			angle -= angularspeed;
			xangle = -roll;
			break;
		}
		case CENTER: {
			fin.setRotation(0);
			xangle = 0;
			break;
		}
		}
	}

	/**
	 * Dives or Surfaces submarine
	 * 
	 * @param distance
	 *            distance to dive
	 */
	public void dive(double distance) {
		if (this.y + distance > bedlevel + height && this.y + distance < sealevel
				&& !check_collission(x, y + distance, z)) {
			this.y += distance;
			posy = y;

		}
	}

	/**
	 * Propels submarine
	 * 
	 */
	public void propel() {
		if (speed != 0) {
			double xtrans = speed * Math.cos(Math.toRadians(angle));
			double ztrans = -speed * Math.sin(Math.toRadians(angle));
			if (!check_collission(posx + xtrans, posy, posz + ztrans) && Collission.check_inside(worldmax.x, new Point(posx + xtrans, posy, posz + ztrans), length/2, 0.02)) {
				posx += xtrans;
				posz += ztrans;
				this.setTranslation(posx, posy, posz);
				this.axle.rotate(speed);
			}
		}
	}

	/**
	 * Resets submarine position
	 */
	public void reset() {
		posx = 0;
		posy = 0;
		x = 0;
		y = posy;
		z = 0;
		posz = 0;
		speed = 0;
		angle = 0;

	}

	public void showpos() {
		System.out.println("SUB POS: " + this.x + "," + this.y + "," + this.z);
	}

	public boolean check_collission(double x, double y, double z) {
		for (int i = 0; i < numplants; i++) {
			if (Collission.check(plants[i].bboxmin, // bounding box minimum
					plants[i].bboxmax, // bounding box maximum
					new Point(x, y, z), // center
										// of
										// sphere
					this.length / 2, // radius of sphere
					0.02 // tolerence
			)) {
				return true;

			}
		}
		return false;
	}

}
