package be.nabu.glue.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;

import be.nabu.glue.ScriptRuntime;
import be.nabu.glue.annotations.GlueMethod;
import be.nabu.glue.annotations.GlueParam;
import be.nabu.glue.impl.methods.ScriptMethods;
import be.nabu.glue.impl.methods.TestMethods;
import be.nabu.libs.evaluator.annotations.MethodProviderClass;

@MethodProviderClass(namespace = "image")
public class ImageMethods {
	
	@GlueMethod(description = "Create a new configuration for an algorithm")
	public static ImageConfiguration configureImageDiff(@GlueParam(name = "algorithm") String algorithm) {
		ImageConfiguration configuration = new ImageConfiguration();
		configuration.setAlgorithm(algorithm);
		return configuration;
	}
	
	@GlueMethod(description = "Validate an image against an expected image")
	public static boolean validateImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual) throws IOException, JAXBException {
		return validateImage(message, expected, actual, null, null);
	}
	
	@GlueMethod(description = "Validate an image against an expected image with a specific configuration")
	public static boolean validateImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual, @GlueParam(name = "configuration") Object configuration) throws IOException, JAXBException {
		return validateImage(message, expected, actual, configuration, null);
	}
	
	@GlueMethod(description = "Validate an image against an expected image with a specific configuration and a certain threshold")
	public static boolean validateImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual, @GlueParam(name = "configuration") Object configuration, @GlueParam(name = "threshold") Double threshold) throws IOException, JAXBException {
		return checkImage(message, expected, actual, configuration, threshold, false);
	}

	@GlueMethod(description = "Confirm an image against an expected image")
	public static boolean confirmImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual) throws IOException, JAXBException {
		return confirmImage(message, expected, actual, null, null);
	}

	@GlueMethod(description = "Confirm an image against an expected image with a specific configuration")
	public static boolean confirmImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual, @GlueParam(name = "configuration") Object configuration) throws IOException, JAXBException {
		return confirmImage(message, expected, actual, configuration, null);
	}
	
	@GlueMethod(description = "Confirm an image against an expected image with a specific configuration and a certain threshold")
	public static boolean confirmImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual, @GlueParam(name = "configuration") Object configuration, @GlueParam(name = "threshold") Double threshold) throws IOException, JAXBException {
		return checkImage(message, expected, actual, configuration, threshold, true);
	}
	
	@GlueMethod(description = "Check an image against an expected image with a specific configuration and a certain threshold with variable failure")
	@SuppressWarnings("unchecked")
	public static boolean checkImage(@GlueParam(name = "message") String message, @GlueParam(name = "expected") Object expected, @GlueParam(name = "actual") Object actual, @GlueParam(name = "configuration") Object configurationContent, @GlueParam(name = "threshold") Double threshold, @GlueParam(name = "fail") boolean fail) throws IOException, JAXBException {
		// defaults to 0% if nothing is set
		if (threshold == null) {
			String configured = ScriptMethods.environment("images.threshold");
			threshold = configured == null ? 0 : Double.parseDouble(configured);
		}
		BufferedImage expectedImage = expected instanceof BufferedImage ? (BufferedImage) expected : ImageDiff.parse(new ByteArrayInputStream(ScriptMethods.bytes(expected)));
		BufferedImage actualImage = expected instanceof BufferedImage ? (BufferedImage) actual : ImageDiff.parse(new ByteArrayInputStream(ScriptMethods.bytes(actual)));
		ImageConfiguration configuration = null;
		if (configurationContent instanceof ImageConfiguration) {
			configuration = (ImageConfiguration) configurationContent;
		}
		else if (configurationContent != null) {
			configuration = ImageConfiguration.parse(new ByteArrayInputStream(ScriptMethods.bytes(configurationContent)));
		}
		try {
			ImageDiff diff = ImageDiff.diff(
				expectedImage,
				actualImage,
				configuration
			);
			double diffPercentage = diff.getDiff();
			boolean result = TestMethods.check(message, diffPercentage <= threshold, String.format("%.2f", diffPercentage * 100) + "% <= " + String.format("%.2f", threshold * 100) + "%", fail);
			// add it to a central map that can be used later
			if (!ScriptRuntime.getRuntime().getContext().containsKey("imageDiffs")) {
				ScriptRuntime.getRuntime().getContext().put("imageDiffs", new LinkedHashMap<String, byte[]>());
			}
			Map<String, byte[]> imageDiffs = (Map<String, byte[]>) ScriptRuntime.getRuntime().getContext().get("imageDiffs");
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(diff.getDiffAsImage(), "png", output);
			imageDiffs.put(message.replaceAll("[^\\w]+", "_") + ".png", output.toByteArray());
			// add it to the current pipeline for immediate use
			ScriptRuntime.getRuntime().getExecutionContext().getPipeline().put("$diffImage", output.toByteArray());
			return result;
		}
		catch (IllegalArgumentException e) {
			return TestMethods.check(message, false, "Can not compare: " + e.getMessage(), fail);
		}
	}
	
}
