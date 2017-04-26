package scene;

//public class RenderScene {
//
//}

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
import com.jogamp.opengl.util.gl2.GLUT;

import components.*;
import utils.*;

/**
 * Starting class for Assignment 2
 * 
 * Provides a <code>light</code> method to set up lighting and calls that method
 * in the <code>display</code> method.
 * 
 * @author jwhalley
 *
 */

public class RenderScene implements GLEventListener, KeyListener {

	private Camera camera;
	private TrackballCamera camera2;
	private static GLCanvas canvas;
	private boolean track = true;
	Grid seabed, watersurface;
	private LinkedBlockingQueue<KeyEvent> eventQ = new LinkedBlockingQueue<KeyEvent>();
	private boolean isWireframe = true;
	private boolean showOrigin = true;

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();

		// clear the depth and color buffers
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		// using default camera & projection
		if (!track) {
			camera.draw(gl);
		} else {
			camera2.draw(gl);
		}
		// set up lights
		this.lights(gl);
		
		while (this.handleNextKeyEvent(gl))
			;

		if (showOrigin) {
			Origin.drawAxes(gl);
		}

		seabed.draw(gl,isWireframe);
		watersurface.draw(gl,isWireframe);

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
		if (!track) {
			camera = new Camera();
		} else {
			camera2 = new TrackballCamera(canvas);
			camera2.setLookAt(0, 1, 0);
			camera2.setDistance(10);
			camera2.setFieldOfView(40);
		}
		// double y, double size, float[] color, double tmin, double tmax
		seabed = new Grid(0, 3, ColorPalette.withalpha(ColorPalette.floor, 1), -100, 100);
		watersurface = new Grid(1.5, 3, ColorPalette.withalpha(ColorPalette.Blue, 0.1f), -100, 100);

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
		if (!track) {
			camera.newWindowSize(width, height);
		} else {
			camera2.newWindowSize(width, height);
		}

	}

	public static void main(String[] args) {
		Frame frame = new Frame("A2 Starting Code");
		canvas = new GLCanvas();
		RenderScene app = new RenderScene();
		canvas.addGLEventListener(app);
		canvas.addKeyListener(app);
		frame.add(canvas);
		frame.setSize(800, 800);
		final FPSAnimator animator = new FPSAnimator(canvas, 60);
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
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

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

	private void handleKeyEvent(KeyEvent e, GL2 gl) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_L: {
			System.out.println("detected");
			isWireframe = !isWireframe;
			break;
		}
		case KeyEvent.VK_O: {
			showOrigin = !showOrigin;
			break;
		}
		}

	}

}