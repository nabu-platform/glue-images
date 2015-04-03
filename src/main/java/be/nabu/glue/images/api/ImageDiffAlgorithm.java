package be.nabu.glue.images.api;

import java.util.List;

import be.nabu.glue.images.Pixel;

public interface ImageDiffAlgorithm {
	public double diff(List<Pixel> pixels);
}
