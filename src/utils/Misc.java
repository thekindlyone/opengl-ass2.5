package utils;

public class Misc {

	public static double scale(double input, double inputMin, double inputMax, double targetMin, double targetMax) {
		return targetMin + ( ((targetMax-targetMin)*(input-inputMin))/(inputMax-inputMin) );

	}

}
