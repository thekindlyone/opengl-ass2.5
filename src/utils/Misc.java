package utils;

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

}
