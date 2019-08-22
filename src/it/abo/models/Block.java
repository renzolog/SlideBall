package it.abo.models;

import java.awt.Point;

public abstract class Block {
	
	Point coordinates;
	boolean isActive;
	BlockType blockType;
	
	public Block(Point coordinates) {
		
		setCoordinates(coordinates);
		setActive(true);
	}
	
	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public BlockType getBlockType() {

		 return blockType;
	}

	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	
}
