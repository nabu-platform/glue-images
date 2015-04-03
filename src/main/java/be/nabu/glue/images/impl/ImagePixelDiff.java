package be.nabu.glue.images.impl;

import java.util.List;

import be.nabu.glue.images.Pixel;
import be.nabu.glue.images.api.ImageDiffAlgorithm;

/**
 * Keeps track of the difference between pixels, any change in the pixel and the pixel is flagged
 */
public class ImagePixelDiff implements ImageDiffAlgorithm {
	@Override
	public double diff(List<Pixel> pixels) {
		long amount = 0;
		for (Pixel pixel : pixels) {
			if (pixel.getSourceRGB() != pixel.getTargetRGB()) {
				amount++;
			}
		}
		return (double) amount / (double) pixels.size();
	}
}
