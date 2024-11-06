/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
