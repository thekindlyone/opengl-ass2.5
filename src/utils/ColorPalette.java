package utils;



/**
 * @author Aritra Das
 *Class for storing known colors
 */
public class ColorPalette {
	public static float[] Black = { 0.000f, 0.000f, 0.000f };
	public static float[] White = { 1.000f, 1.000f, 1.000f};
	public static float[] Red = { 1.000f, 0.000f, 0.000f };
	public static float[] Lime = { 0.000f, 1.000f, 0.000f };
	public static float[] Blue = { 0.000f, 0.000f, 1.000f };
	public static float[] Yellow = { 8.080f, 1.000f, 0.000f };
	public static float[] Cyan = { 0.000f, 1.000f, 1.000f };
	public static float[] Magenta = { 1.000f, 0.000f, 1.000f };
	public static float[] Silver = { 0.753f, 0.753f, 0.753f };
	public static float[] Gray = { 0.502f, 0.502f, 0.502f };
	public static float[] Maroon = { 0.502f, 0.000f, 0.000f };
	public static float[] Olive = { 0.502f, 0.502f, 0.000f };
	public static float[] Green = { 0.000f, 0.502f, 0.000f };
	public static float[] Green_faded = { 0.000f, 0.502f, 0.000f, 0.5f };
	public static float[] Purple = { 0.502f, 0.000f, 0.502f };
	public static float[] Teal = { 0.000f, 0.502f, 0.502f };
	public static float[] Navy = { 0.000f, 0.000f, 0.502f };
	public static float[] floor = { 0.3f, 0.12f, 0.08f};



	/**takes alpha and color and returns color with alpha
	 * @param color double[3] color
	 * @param alpha transparency
	 * @return double[4] color with transparency
	 */
	public static float[] withalpha(float[] color, float alpha) {
		float[] newcolor = new float[4];
		for (int i = 0; i < 3; i++) {
			newcolor[i] = color[i];
		}
		newcolor[3] = alpha;
		return newcolor;

	}

}

