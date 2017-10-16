package utils;

public class Collission {

	/**
	 * Check.
	 *
	 * @param bboxmin
	 *            the bboxmin
	 * @param bboxmax
	 *            the bboxmax
	 * @param center
	 *            the center
	 * @param radius
	 *            the radius
	 * @param tolerence
	 *            the tolerence
	 * @return true, if collission
	 */
	public static boolean check(Point bboxmin, Point bboxmax, Point center, double radius, double tolerence) {
		double dis = 0;

		if (center.x < bboxmin.x) {
			dis += Math.pow(center.x - bboxmin.x, 2);
		} else if (center.x > bboxmax.x) {
			dis += Math.pow(center.x - bboxmax.x, 2);
		}

		if (center.y < bboxmin.y) {
			dis += Math.pow(center.y - bboxmin.y, 2);
		} else if (center.y > bboxmax.y) {
			dis += Math.pow(center.y - bboxmax.y, 2);
		}

		if (center.z < bboxmin.z) {
			dis += Math.pow(center.z - bboxmin.z, 2);
		} else if (center.z > bboxmax.z) {
			dis += Math.pow(center.z - bboxmax.z, 2);
		}

		return dis <= Math.pow(radius, 2) - tolerence;
	}

	public static boolean check_inside(double outer, Point center, double radius, double tolerence) {
		double dis = center.distancefrom(new Point(0, 0, 0));

		return (dis - radius) <= outer;
	}

}
