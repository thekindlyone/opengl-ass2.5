package components;

import utils.Misc;

public class FractalSurface {

	/** Class to generate a heightmap using diamon square algorithm
	 * @param cornervalue the y coordinate of the corners
	 * @param dim dimension
	 * @param hrange the range of random fluctuation
	 * @return heightmap 2D array
	 */
	public static double[][] generate(double cornervalue, int dim, double hrange) {
		
		double[][] map = new double[dim][dim];
		map[0][0] = cornervalue;
		map[0][dim - 1] = cornervalue;
		map[dim - 1][0] = cornervalue;
		map[dim - 1][dim - 1] = cornervalue;

		for (int side = dim - 1; side >= 2; side /= 2, hrange /= 2.0) {
			int half = side / 2;

			// square
			for (int x = 0; x < dim - 1; x += side) {
				for (int y = 0; y < dim - 1; y += side) {
					double avg = (map[x][y] + map[x + side][y] + map[x][y + side] + map[x + side][y + side]) / 4.0;
					map[x + half][y + half] = avg + (Misc.get_rand(-hrange, hrange));
				}
			}

			// diamond
			for (int x = 0; x < dim - 1; x += half) {
				for (int y = (x + half) % side; y < dim - 1; y += side) {
					double avg = (map[(x - half + dim - 1) % (dim - 1)][y] + map[(x + half) % (dim - 1)][y]
							+ map[x][(y + half) % (dim - 1)] + map[x][(y - half + dim - 1) % (dim - 1)]) / 4.0;

					avg = avg + (Misc.get_rand(-hrange, hrange));
					map[x][y] = avg;

					// wrap
					map[dim - 1][y] = avg;
					if (y == 0)
						map[x][dim - 1] = avg;
				}
			}
		}


		return map;
	}

}
