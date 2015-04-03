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
