# Images

This plugin provides the ability to diff images. It calculates a diff percentage and creates a visual diff result. The visual diff uses one of two contrast methods to provide a more visible result.

The actual algorithm used to diff the images is configurable by setting ``images.algorithm`` which should point to a class that implements ``be.nabu.glue.images.api.ImageDiffAlgorithm``.

Two implementations are provided out of the box:

- **Pixel diff**: checks the amount of pixels that are different between two images (the default) ``be.nabu.glue.images.impl.ImagePixelDiff``
- **RGB diff**: checks the amount of RGB difference between corresponding pixels: ``be.nabu.glue.images.impl.ImageRGBDiff``. This is a reimplementation of [this algorithm](http://rosettacode.org/wiki/Percentage_difference_between_images#Java)

Which algorithm you use depends on your needs, if you are reproducing pixel perfect screenshots for example the pixel diff algorithm is better. However the example found by following the link in the RGB description will be better served with that algorithm.

For example the left two images are diffed and the resulting diff is visualized in the third image:

![Pug 1](https://github.com/nablex/glue-images/blob/master/pug1.png)

![Pug 2](https://github.com/nablex/glue-images/blob/master/pug2.png)

![Pug Diff](https://github.com/nablex/glue-images/blob/master/pug-diff.png)

According to the pixel algorithm, there is a ``1.53%`` difference between the two images while the RGB algorithm says ``0.20%``. **Note**: the default threshold for "accepted" is ``0%`` so the test will fail. You can update the threshold to allow such small changes.

The visual diff result is available in the context of the script run as ``imageDiffs`` and is also injected into the pipeline as variable ``$diffImage``.

## Properties

Apart from the ``images.algorithm`` property mentioned above, you can set:

- **images.threshold**: the default threshold to allow, if none is set and none is passed in as parameter at runtime, the default is ``0%``
- **images.contrast**: you can set a level (0-10) of contrast to use when generating the contrasted image, the default is ``5``

## Example

The above pug diff was generating using:

```python
validateImage("Checking the pugs!", "pug1.png", "pug2.png")
write("diff.png", $diffImage)
```

If you want more control over the algorithm and threshold at runtime you can do:

```glue
validateImage("Checking the pugs!", "pug1.png", "pug2.png", configureImageDiff("be.nabu.glue.images.impl.ImageRGBDiff"), 0.03)
write("diff.png", $diffImage)
```

## Zones

You can configure inclusion zones and exclusion for images to check.

For example in the above image, if I want to ignore the top 100 pixels, I can create a resource, let's call it ``pug.xml`` that contains the following:

```xml
<configuration>
	  <inclusionZones fromY="100"/>
</configuration>
```

You can then use this configuration like this:

```python
validateImage("Checking the pugs!", "pug1.png", "pug2.png", "pug.xml")
```

In this case it will find ``0.00%`` difference as the only difference lies above this position.

In this XML you can also set the algorithm:

```xml
<configuration algorithm="be.nabu.glue.images.impl.ImageRGBDiff">
	  <inclusionZones fromY="100" />
	  <exclusionZones fromY="200" />
</configuration>
```
