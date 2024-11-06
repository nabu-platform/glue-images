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
