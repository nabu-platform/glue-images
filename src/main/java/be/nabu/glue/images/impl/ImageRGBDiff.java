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

package be.nabu.glue.images.impl;

import java.util.List;

import be.nabu.glue.images.Pixel;
import be.nabu.glue.images.api.ImageDiffAlgorithm;

/**
 * Keeps track of the difference between RGB values
 */
public class ImageRGBDiff implements ImageDiffAlgorithm {

	@Override
	public double diff(List<Pixel> pixels) {
		long amount = 0;
		for (Pixel pixel : pixels) {
			amount += getDiffRGB(pixel.getSourceRGB(), pixel.getTargetRGB());
		}
		return (double) amount / (255.0 * pixels.size() * 3);
	}
	
	public static int getDiffRGB(int sourceRGB, int targetRGB) {
		int diff = Math.abs(Pixel.getR(sourceRGB) - Pixel.getR(targetRGB));
		diff += Math.abs(Pixel.getG(sourceRGB) - Pixel.getG(targetRGB));
		diff += Math.abs(Pixel.getB(sourceRGB) - Pixel.getB(targetRGB));
		return diff;
	}

}
