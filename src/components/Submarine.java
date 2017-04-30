package components;

import com.jogamp.opengl.GL2;

import utils.*;
import submarineParts.*;

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

	public enum Yaw {
		LEFT, RIGHT, CENTER
	};

	public Yaw yaw;
	// public enum Direction{FORWARD,REVERSE}

	public double speed = 0;

	public double posx = 0, posy = 0, posz = 0;

	public double angle = 0;

	// private double depth = 0;
	private double sealevel;

	public Submarine(double sealevel) {
		this.sealevel = sealevel;
		this.body = new Body(length, height, width);
		this.addChild(body);
		this.axle = new PropellerAxle();
		body.addChild(this.axle);
		blades = new PropellerBlade[4];
		for (int i = 0; i < blades.length; i++) {
			blades[i] = new PropellerBlade(i + 1, height);
			// blades[i].setTranslation(length, 0, 0);
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

	@Override
	public void transformNode(GL2 gl) {
		propel();
		// gl.glTranslated(0, depth, 0);
		gl.glTranslated(x, y, z);
		gl.glRotated(angle, 0, 1, 0);

	}

	@Override
	public void drawNode(GL2 gl, boolean isWireframe) {

	}

	public void setTranslation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		// depth=y;
	}

	public void setRotation(double angle) {
		this.angle = angle;

	}

	public void steer(Yaw dir) {
		this.yaw = dir;
		switch (this.yaw) {
		case LEFT: {
			fin.setRotation(25);
			angle += 2;
			break;
		}
		case RIGHT: {
			fin.setRotation(-25);
			angle -= 2;
			break;
		}
		case CENTER: {
			fin.setRotation(0);
			break;
		}
		}
		// if(this.yaw==dir){return;}
		// if(this.yaw == Yaw.CENTER){this.yaw = dir;}
		// else{this.yaw = Yaw.CENTER;}
	}

	public void dive(double distance) {
		// y=depth;
		if (this.y + distance > 0 && this.y + distance < sealevel) {
			this.y += distance;

		}
	}

	public void propel() {
		if (speed != 0) {
			double xtrans = speed * Math.cos(Math.toRadians(angle));
			double ztrans = -speed * Math.sin(Math.toRadians(angle));
			// System.out.println("xtrans = "+xtrans+", ztrans = "+ztrans+",
			// angle = "+angle);

			posx += xtrans;
			posz += ztrans;
			this.setTranslation(posx, y, posz);
			this.axle.rotate(speed);
		}
	}

	public void reset() {
		posx = 0;
		posy = 0;
		y = 0;
		posz = 0;

	}

}
