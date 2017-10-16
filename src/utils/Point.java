package utils;




/**
 * @author Aritra Das Point primitive class that denotes a 3D point
 */
public class Point implements Comparable<Point> {
	public double x, y, z;

	/**
	 * @param X
	 *            x coordinate of point
	 * @param Y
	 *            y coordinate of point
	 */
	public Point(double X, double Y, double Z) {
		x = X;
		y = Y;
		z = Z;

	}

	/**
	 * Returns a translated point by given distance and angle.
	 * 
	 * @param angle
	 *            angle to translate
	 * @param distance
	 *            distance to translate
	 * @return Point that is translated
	 */
	public Point get_translated(double anglex, double angley, double distance) {
//		double radx = Math.toRadians(anglex);
//		double rady = Math.toRadians(angley);
//		double radz = Math.toRadians(anglez);
		
        double r = distance * Math.cos(Math.toRadians(angley));
        double pZ = r * Math.cos(Math.toRadians(anglex));
        double pX = r * Math.sin(Math.toRadians(anglex));
        double pY = distance * Math.sin(Math.toRadians(angley));
		
		Point p = new Point(
				x + (pX), 
				y + (pY),
				z + (pZ));

		return p;

	}

	@Override
	public int compareTo(Point other) {
		if (this.x > other.x) {
			return 1;
		} else {
			return -1;
		}

	}
	
	public double distancefrom(Point other){
		return Math.sqrt(
				Math.pow(x-other.x, 2)+
				Math.pow(y-other.y, 2)+
				Math.pow(z-other.z, 2)
				);
	}
}
