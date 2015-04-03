package be.nabu.glue.images;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import be.nabu.glue.images.api.ImageDiffAlgorithm;
import be.nabu.glue.images.impl.ImagePixelDiff;
import be.nabu.glue.images.impl.ImageRGBDiff;
import be.nabu.glue.impl.methods.ScriptMethods;

public class ImageDiff {
	
	private BufferedImage image1;
	private BufferedImage image2;
	private List<Pixel> diffs = new ArrayList<Pixel>();
	private double diff;
	private BufferedImage diffImage;
	private ImageConfiguration configuration;

	/**
	 * A helper method
	 */
	public static BufferedImage parse(InputStream content) throws IOException {
		return ImageIO.read(content);
	}

	/**
	 * Does some preliminary checks before returning an actual diff object
	 * Can throw an IllegalArgumentException if the two images do not qualify for diffing due to structural differences (like width/height)
	 */
	public static ImageDiff diff(BufferedImage image1, BufferedImage image2, ImageConfiguration configuration) {
		if (image1.getWidth() != image2.getWidth()) {
			throw new IllegalArgumentException("The images are not of the same width: " + image1.getWidth() + " != " + image2.getWidth()); 
		}
		if (image1.getHeight() != image2.getHeight()) {
			throw new IllegalArgumentException("The images are not of the same height: " + image1.getHeight() + " != " + image2.getHeight()); 
		}
		return new ImageDiff(image1, image2, configuration);
	}

	protected ImageDiff(BufferedImage image1, BufferedImage image2, ImageConfiguration configuration) {
		this.image1 = image1;
		this.image2 = image2;
		this.configuration = configuration;
		diff();
	}
	
	private void diff() {
		String algorithmName = configuration == null ? null : configuration.getAlgorithm();
		if (algorithmName == null) {
			algorithmName = ScriptMethods.environment("images.algorithm");
		}
		if (algorithmName == null) {
			algorithmName = ImagePixelDiff.class.getName();
		}
		ImageDiffAlgorithm algorithm; 
		try {
			algorithm = (ImageDiffAlgorithm) Thread.currentThread().getContextClassLoader().loadClass(algorithmName).newInstance();
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		diffs.clear();
		for (int y = 0; y < image1.getHeight(); y++) {
			for (int x = 0; x < image1.getWidth(); x++) {
				// if there is a configuration and it has inclusion zones, only diff if the pixel is in it
				if (configuration != null && !configuration.getInclusionZones().isEmpty()) {
					boolean isIncluded = false;
					for (ImageZone inclusionZone : configuration.getInclusionZones()) {
						if (inclusionZone.contains(x, y)) {
							isIncluded = true;
							break;
						}
					}
					if (!isIncluded) {
						continue;
					}
				}
				// if we have exclusion zones, don't check the pixel if it is in there
				if (configuration != null && !configuration.getExclusionZones().isEmpty()) {
					boolean isExcluded = false;
					for (ImageZone exclusionZone : configuration.getExclusionZones()) {
						if (exclusionZone.contains(x, y)) {
							isExcluded = true;
							break;
						}
					}
					if (isExcluded) {
						continue;
					}
				}
				Pixel diff = new Pixel(x, y, image1.getRGB(x, y), image2.getRGB(x, y));
				diffs.add(diff);
			}
		}
		if (diffs.isEmpty()) {
			throw new RuntimeException("No pixels found for comparison, please update your inclusion/exclusion zones");
		}
		this.diff = algorithm.diff(diffs);
	}
	
	public double getDiff() {
		return diff;
	}
	
	public BufferedImage getDiffAsImage() {
		if (diffImage == null) {
			synchronized (this) {
				if (diffImage == null) {
					diffImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
					for (Pixel diff : diffs) {
						diffImage.setRGB(diff.getX(), diff.getY(), ImageRGBDiff.getDiffRGB(diff.getSourceRGB(), diff.getTargetRGB()));
					}
				}
				// increase contrast
				String contrast = ScriptMethods.environment("images.contrast");
				contrast1(diffImage, contrast == null ? 5f : Float.parseFloat(contrast));
			}
		}
		return diffImage;
	}
	
	public static void contrast2(BufferedImage image, float contrast) {
		RescaleOp op = new RescaleOp(contrast, 0f, null);
		op.filter(image, image);
	}
	
	public static void contrast1(BufferedImage image, float contrast) {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				int r = Pixel.getR(rgb);
				int g = Pixel.getG(rgb);
				int b = Pixel.getB(rgb);
				int max = Math.max(Math.max(r, g), b);
				if (max > 0) {
					// add a bit to the value, depending on the contrast
					max += (255 - max) / Math.max(10 - contrast, 2);
					int contrastedRGB = Pixel.getRGB(max, max, max);
					image.setRGB(x, y, contrastedRGB);
				}
			}
		}
	}
}
