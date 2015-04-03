package be.nabu.glue.images;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
public class ImageConfiguration {
	
	private List<ImageZone> inclusionZones = new ArrayList<ImageZone>(), exclusionZones = new ArrayList<ImageZone>();
	private String algorithm;

	public List<ImageZone> getInclusionZones() {
		return inclusionZones;
	}

	public void setInclusionZones(List<ImageZone> inclusionZones) {
		this.inclusionZones = inclusionZones;
	}

	public List<ImageZone> getExclusionZones() {
		return exclusionZones;
	}

	public void setExclusionZones(List<ImageZone> exclusionZones) {
		this.exclusionZones = exclusionZones;
	}
	
	@XmlAttribute
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public static ImageConfiguration parse(InputStream input) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ImageConfiguration.class);
		return (ImageConfiguration) context.createUnmarshaller().unmarshal(input);
	}
	
	public void write(OutputStream output) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ImageConfiguration.class);
		context.createMarshaller().marshal(this, output);
	}
}
