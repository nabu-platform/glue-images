package be.nabu.glue.images;

public class Pixel {
	private int x, y;
	private int sourceRGB;
	private int targetRGB;
	
	public Pixel(int x, int y, int sourceRGB, int targetRGB) {
		this.x = x;
		this.y = y;
		this.sourceRGB = sourceRGB;
		this.targetRGB = targetRGB;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getSourceRGB() {
		return sourceRGB;
	}

	public int getTargetRGB() {
		return targetRGB;
	}
	
	public static int getR(int rgb) {
		return (rgb >> 16) & 0xff;
	}
	
	public static int getG(int rgb) {
		return (rgb >> 8) & 0xff;
	}
	
	public static int getB(int rgb) {
		return rgb & 0xff;
	}
	
	public static int getRGB(int r, int g, int b) {
		return (r << 16) + (g << 8) + b;
	}
}
