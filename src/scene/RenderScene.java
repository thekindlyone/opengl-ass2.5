package scene;

import java.awt.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import components.*;
import utils.*;

public class RenderScene implements GLEventListener, KeyListener {

	private FollowCamera followcamera;
	private TrackballCamera customcamera;
	private static GLCanvas canvas;
	Grid seabed, watersurface;
	Submarine submarine;
	private LinkedBlockingQueue<KeyEvent> eventQ = new LinkedBlockingQueue<KeyEvent>();
	private LinkedBlockingQueue<KeyEvent> eventQ2 = new LinkedBlockingQueue<KeyEvent>();

	private boolean isWireframe = true;
	private boolean showOrigin = false;

	private double divespeed = 0.02;

	MaterialProfile bulbmaterial;

	Sun sun;

	double sealevel = 3;
	double bedlevel = -2;
	boolean follow = true;
	Plant[] plants;
	MaterialProfile submaterial;

	Texture texture_floor, texture_water, texture_plant;
	int numplants = 5;
	double seagrid_size = 0.5;
	int seagrid_dim = 50;
	boolean underwater = true;

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		// handle keypresses
		while (this.handleNextKeyEvent(gl))
			;

		while (this.handleNextKeyReleaseEvent(gl))
			;

		// clear the depth and color buffers
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		if (follow) {
			followcamera.draw(gl, submarine);
		} else {
			customcamera.draw(gl);
		}

		// set up lights
		this.lights(gl);


		setfogmode(gl, followcamera.camY <= sealevel);

		sun.draw(gl);

		setDrawMode(gl);

		seabed.draw(gl);

		if (showOrigin) {
			Origin.drawAxes(gl);
		}
		// gl.glEnable(GL2.GL_BLEND);
		submarine.draw(gl);
		MaterialProfile.reset(gl);
		for (int i = 0; i < numplants; i++) {
			plants[i].drawPlant(gl);
			MaterialProfile.reset(gl);
		}

		watersurface.draw(gl);
		MaterialProfile.reset(gl);

		gl.glFlush();

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.setSwapInterval(1);

		// enable depth test and set shading mode
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_BLEND);
		gl.glClearColor(ColorPalette.Ocean[0], ColorPalette.Ocean[1], ColorPalette.Ocean[2], 0.5f);

		setfog(gl);

		followcamera = new FollowCamera(canvas);
		customcamera = new TrackballCamera(canvas);

		try {
			texture_floor = TextureIO.newTexture(new File("./textures/ground_moss.jpg"), true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			texture_water = TextureIO.newTexture(new File("./textures/sea.jpg"), true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			texture_plant = TextureIO.newTexture(new File("./textures/plant.png"), true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		submaterial = new MaterialProfile(ColorPalette.withalpha(ColorPalette.Black, 1), // ambient
				ColorPalette.withalpha(ColorPalette.Silver, 1f), // diffuse
				ColorPalette.withalpha(ColorPalette.White, 1), // specular
				new float[] { 10.0f } // shininess
		);

		// double min_world = (-(seagrid_dim - 1) * seagrid_size / 2.0) +
		// seagrid_size;
		// double max_world = ((seagrid_dim - 1) * seagrid_size / 2.0) -
		// seagrid_size;

		seabed = new Grid(bedlevel, seagrid_size, seagrid_dim, ColorPalette.withalpha(ColorPalette.White, 1),
				texture_floor, 1000 - 1, true);
		watersurface = new Grid(sealevel, seagrid_size, seagrid_dim, ColorPalette.withalpha(ColorPalette.White, 0.5f),
				texture_water, 512 - 1, false);

		plants = new Plant[numplants];
		for (int i = 0; i < numplants; i++) {
			Point random_point = new Point(Misc.get_rand(seabed.min.x, seabed.max.x), seabed.miny,
					Misc.get_rand(seabed.min.z, seabed.max.z));

			plants[i] = new Plant(random_point, // start point
					0, // start angle x
					90, // start angle y
					1.5, // start branch length
					20, // branch angle
					(int) Misc.get_rand(40, 65), // density (1-100), higher
													// means more dense tree
					1.5, // angle fuzz factor
					5, // recursion depth
					texture_plant // texture
			// bulbmaterial // bulb material
			);

			submarine = new Submarine(submaterial, sealevel, seabed.maxy, seabed.min,seabed.max, numplants, plants);

		}

		sun = new Sun();

		System.out.println("Key Mapping:\n--------------------------\n"
				+ "Up/Down Arrows : Increase depth(dive) or decrease depth (surface)\n"
				+ "W/S: Move forward or backward\nA/D: Yaw(turn left or right)\n"
				+ "O: Show/Hide Origin and Axes\nL: Toggle Filled/Wireframe\n"
				+ "T: toggle time (day night cycle)\n");

	}

	private void lights(GL2 gl) {

		float position0[] = { 0, 5, 2, 0 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ColorPalette.withalpha(ColorPalette.Black, 0.2f), 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, ColorPalette.withalpha(ColorPalette.light1, 0.5f), 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, ColorPalette.withalpha(ColorPalette.white2, 0.5f), 0);

		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_POSITION, followcamera.getpos(), 0);
		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_AMBIENT, ColorPalette.withalpha(ColorPalette.Black, 0.2f), 0);
		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_DIFFUSE, ColorPalette.withalpha(ColorPalette.light1, 0.1f), 0);
		gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPECULAR, ColorPalette.withalpha(ColorPalette.white2, 0.5f), 0);

		// enable lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT3);

		// normalize the normals
		gl.glEnable(GL2.GL_NORMALIZE);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		height = (height == 0) ? 1 : height; // prevent divide by zero

		// Set the viewport to cover the new window
		gl.glViewport(0, 0, width, height);
		followcamera.newWindowSize(width, height);
		customcamera.newWindowSize(width, height);

	}

	public static void main(String[] args) {
		Frame frame = new Frame("Submarine Scene - Aritra Das");
		canvas = new GLCanvas();
		RenderScene app = new RenderScene();
		canvas.addGLEventListener(app);
		canvas.addKeyListener(app);
		frame.add(canvas);
		frame.setSize(800, 800);
		final FPSAnimator animator = new FPSAnimator(canvas, 80);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {

					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		// Center frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.setFocusable(true);
		canvas.requestFocus();
		animator.start();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		try {
			this.eventQ.put(e);
		} catch (InterruptedException e1) {
			// Thread interrupted while waiting to add to queue - ignore this
			// event.
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

		try {
			this.eventQ2.put(e);
		} catch (InterruptedException e1) {
			// Thread interrupted while waiting to add to queue - ignore this
			// event.
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handles Keystrokes Queue
	 * 
	 * @param gl
	 * @return true if there are events waiting in queue
	 */
	private boolean handleNextKeyEvent(GL2 gl) {
		boolean eventsToHandle = false;

		if (this.eventQ.size() > 0) {
			KeyEvent nextEvent;
			try {
				nextEvent = this.eventQ.take();
				this.handleKeyEvent(nextEvent, gl);
			} catch (InterruptedException e) {
			}
			eventsToHandle = true;
		}

		return eventsToHandle;

	}

	/**
	 * Handles Keystroke Release Queue
	 * 
	 * @param gl
	 * @return true if there are events waiting in queue
	 */
	private boolean handleNextKeyReleaseEvent(GL2 gl) {
		boolean eventsToHandle = false;

		if (this.eventQ2.size() > 0) {
			KeyEvent nextEvent;
			try {
				nextEvent = this.eventQ2.take();
				this.handleKeyReleaseEvent(nextEvent, gl);
			} catch (InterruptedException e) {
			}
			eventsToHandle = true;
		}

		return eventsToHandle;

	}

	/**
	 * handles key press action
	 * 
	 * @param e
	 *            Keyevent triggered
	 * @param gl
	 */
	private void handleKeyEvent(KeyEvent e, GL2 gl) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_L: {
			isWireframe = !isWireframe;
			break;
		}
		case KeyEvent.VK_O: {
			showOrigin = !showOrigin;
			break;
		}
		case KeyEvent.VK_A: {
			submarine.steer(Submarine.Yaw.LEFT);
			break;
		}
		case KeyEvent.VK_D: {
			submarine.steer(Submarine.Yaw.RIGHT);
			break;
		}
		case KeyEvent.VK_UP: {
			submarine.dive(divespeed);
			submarine.dstate = Submarine.DiveState.UP;
			break;
		}
		case KeyEvent.VK_DOWN: {
			submarine.dive(-divespeed);
			submarine.dstate = Submarine.DiveState.DOWN;
			break;
		}
		case KeyEvent.VK_W: {
			submarine.speed = -0.01;
			break;
		}
		case KeyEvent.VK_S: {
			submarine.speed = +0.01;
			break;
		}
		case KeyEvent.VK_C: {
			follow = !follow;
			break;
		}
		case KeyEvent.VK_T: {
			sun.moving = !sun.moving;
			break;
		}

		}

	}

	/**
	 * handles key release action
	 * 
	 * @param e
	 *            Keyevent triggered
	 * @param gl
	 *            Drawable gl object
	 */
	private void handleKeyReleaseEvent(KeyEvent e, GL2 gl) {

		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_A: {
			submarine.steer(Submarine.Yaw.CENTER);
			break;
		}
		case KeyEvent.VK_D: {
			submarine.steer(Submarine.Yaw.CENTER);
			break;
		}

		case KeyEvent.VK_W: {
			submarine.speed = 0;
			break;
		}

		case KeyEvent.VK_S: {
			submarine.speed = 0;
			break;
		}
		case KeyEvent.VK_UP: {
			submarine.dstate = Submarine.DiveState.NONE;
			break;
		}

		case KeyEvent.VK_DOWN: {
			submarine.dstate = Submarine.DiveState.NONE;
			break;
		}
		}

	}

	/**
	 * sets draw mode fill or wireframe
	 * 
	 * @param gl
	 *            drawable gl object
	 */
	public void setDrawMode(GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, (isWireframe) ? GL2.GL_LINE : GL2.GL_FILL);
	}

	public void setfogmode(GL2 gl, boolean newstate) {
		if (underwater != newstate) {
			underwater = newstate;
			setfog(gl);
		}

	}

	public void setfog(GL2 gl) {
		gl.glEnable(GL2.GL_FOG);
		if (underwater) {
			gl.glFogfv(GL2.GL_FOG_COLOR, ColorPalette.withalpha(ColorPalette.Ocean, 1f), 0);

			gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.7f);
			gl.glFogf(GL2.GL_FOG_START, 1.2f);
			gl.glFogf(GL2.GL_FOG_END, 15.0f);
		} else {
			gl.glFogfv(GL2.GL_FOG_COLOR, ColorPalette.withalpha(ColorPalette.White, 0.2f), 0);

			gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
			gl.glFogf(GL2.GL_FOG_DENSITY, 0.5f);
			gl.glFogf(GL2.GL_FOG_START, 1.0f);
			gl.glFogf(GL2.GL_FOG_END, 20.0f);
		}

	}

}