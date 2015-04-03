package be.nabu.glue.images;

import javax.xml.bind.annotation.XmlAttribute;

public class ImageZone {
	
	private Integer fromX, fromY, toX, toY;

	public ImageZone(Integer fromX, Integer fromY, Integer toX, Integer toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	public ImageZone() {
		// auto construct
	}

	@XmlAttribute
	public Integer getFromX() {
		return fromX;
	}
	@XmlAttribute
	public Integer getFromY() {
		return fromY;
	}
	@XmlAttribute
	public Integer getToX() {
		return toX;
	}
	@XmlAttribute
	public Integer getToY() {
		return toY;
	}

	public void setFromX(Integer fromX) {
		this.fromX = fromX;
	}

	public void setFromY(Integer fromY) {
		this.fromY = fromY;
	}

	public void setToX(Integer toX) {
		this.toX = toX;
	}

	public void setToY(Integer toY) {
		this.toY = toY;
	}
	
	public boolean contains(int x, int y) {
		return (fromX == null || x >= fromX)
			&& (toX == null || x < toX)
			&& (fromY == null || y >= fromY)
			&& (toY == null || y < toY);
	}
}
