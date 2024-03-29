package utils;

/**
 * @author Aritra Das Class for storing known colors
 */
public class ColorPalette {
	public static float[] Black = { 0.000f, 0.000f, 0.000f };
	public static float[] White = { 1.000f, 1.000f, 1.000f };
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
	public static float[] floor = { 0.3f, 0.12f, 0.08f };

	public static float[] white1 = { 245 / 255f, 255 / 255f, 250 / 255f };
	public static float[] white2 = { 240 / 255f, 255 / 255f, 255 / 255f };
	public static float[] white3 = { 255 / 255f, 250 / 255f, 240 / 255f };
	public static float[] light1 = { 0.02f, 0.02f, 0.02f};
	public static float[] Fog = { 0.000f, 0.000f, 0.400f };
	public static float[] ambient = { 0.2f, 0.2f, 0.200f };
	public static float[] Fog2={220f/255f, 240f/255f,247f/255f};
	public static float[] Ocean = {0.01f, 0.12f, 0.29f};

	/**
	 * takes alpha and color and returns color with alpha
	 * 
	 * @param color
	 *            double[3] color
	 * @param alpha
	 *            transparency
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
	
	public static float[] randomcolor(){
		float r= Misc.get_rand(0f, 1f);
		float g= Misc.get_rand(0f, 1f);
		float b= Misc.get_rand(0f, 1f);
		
		return new float[]{r,g,b};
	}

}
