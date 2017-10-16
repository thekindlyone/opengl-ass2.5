package utils;

import java.util.Random;

/**Misc functions
 * @author Aritra Das
 *
 */
public class Misc {

	/**scales value in input range to target range 
	 * @param input value to scale
	 * @param inputMin lower limit of input
	 * @param inputMax upper limit of input
	 * @param targetMin lower limit of target
	 * @param targetMax upper limit of target
	 * @return
	 */
	public static double scale(double input, double inputMin, double inputMax, double targetMin, double targetMax) {
		return targetMin + ( ((targetMax-targetMin)*(input-inputMin))/(inputMax-inputMin) );

	}
	
	private static Random randomno = new Random();

	/**Returns random value between a given range
	 * @param min lower limit of range
	 * @param max upper limit of range
	 * @return random number such that min<random number<max 
	 */
	public static float get_rand(float min, float max) {

		return ((max - min) * randomno.nextFloat() + min);

	}
	
	public static double get_rand(double min, double max) {

		return ((max - min) * randomno.nextFloat() + min);

	}
	
	

}
