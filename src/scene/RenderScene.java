package scene;

import java.awt.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import components.*;
import utils.*;

public class RenderScene implements GLEventListener, KeyListener {

	private Camera camera;
	private TrackballCamera camera2;
	private FollowCamera camera3;
	private static GLCanvas canvas;
	Grid seabed, watersurface;
	Submarine submarine;
	private LinkedBlockingQueue<KeyEvent> eventQ = new LinkedBlockingQueue<KeyEvent>();
	private LinkedBlockingQueue<KeyEvent> eventQ2 = new LinkedBlockingQueue<KeyEvent>();

	private boolean isWireframe = true;
	private boolean showOrigin = true;

	private double divespeed = 0.01;

	double sealevel = 3;
	double bedlevel = 0;

	private enum CameraMode {
		CUSTOM, FOLLOW, PLAIN;
		public CameraMode getNext() {
			return values()[(ordinal() + 1) % values().length];
		}
	}

	CameraMode camMode = CameraMode.FOLLOW;

	/**Draws the Camera based on camera mode set. Also resets Submarine Position in case its the Plain Camera
	 * @param gl GL2.gl drawable object
	 */
	public void drawcamera(GL2 gl) {
		switch (camMode) {
		case PLAIN: {
			submarine.reset();
			camera.draw(gl);
			break;
		}
		case CUSTOM: {
			camera2.draw(gl);
			break;
		}
		case FOLLOW: {
			camera3.draw(gl, submarine);
			break;
		}
		}

	}

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

		drawcamera(gl);

		// set up lights
		this.lights(gl);

		setDrawMode(gl);

		seabed.draw(gl);
		watersurface.draw(gl);
		if (showOrigin) {
			Origin.drawAxes(gl);
		}

		submarine.draw(gl);

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

		camera = new Camera();

		camera2 = new TrackballCamera(canvas);
		camera2.setLookAt(0, 1, 0);
		camera2.setDistance(10);
		camera2.setFieldOfView(40);

		camera3 = new FollowCamera(canvas);

		seabed = new Grid(bedlevel, 3, ColorPalette.withalpha(ColorPalette.floor, 1), -100, 100);
		watersurface = new Grid(sealevel, 3, ColorPalette.withalpha(ColorPalette.Blue, 0.1f), -100, 100);
		submarine = new Submarine(sealevel, bedlevel);
		submarine.dive(0.28);

		System.out.println("Key Mapping:\n--------------------------\n"
				+ "Up/Down Arrows : Increase depth(dive) or decrease depth (surface)\n"
				+ "W/S: Move forward or backward\nA/D: Yaw(turn left or right)\n"
				+ "O: Show/Hide Origin and Axes\nL: Toggle Filled/Wireframe\nC: Toggle Camera Mode");

	}

	private void lights(GL2 gl) {

		// lighting stuff
		float ambient[] = { 0, 0, 0, 1 };
		float diffuse[] = { 1, 1, 1, 1 };
		float specular[] = { 1, 1, 1, 1 };
		float position0[] = { 1, 1, 1, 0 };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);

		// enable lights
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		// draw using standard glColor
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		height = (height == 0) ? 1 : height; // prevent divide by zero

		// Set the viewport to cover the new window
		gl.glViewport(0, 0, width, height);
		camera2.newWindowSize(width, height);
		camera3.newWindowSize(width, height);

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
	 * @param e Keyevent triggered
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
			break;
		}
		case KeyEvent.VK_DOWN: {
			submarine.dive(-divespeed);
			break;
		}
		case KeyEvent.VK_W: {
			submarine.speed = -0.005;
			break;
		}
		case KeyEvent.VK_S: {
			submarine.speed = +0.005;
			break;
		}

		case KeyEvent.VK_C: {
			camMode = camMode.getNext();
			break;
		}
		}

	}
	
	/**
	 * handles key release action
	 * @param e Keyevent triggered
	 * @param gl Drawable gl object
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
		}

	}

	/**
	 * sets draw mode fill or wireframe
	 * @param gl drawable gl object
	 */
	public void setDrawMode(GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, (isWireframe) ? GL2.GL_LINE : GL2.GL_FILL);
	}

}