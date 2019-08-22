package it.abo.models;

import java.awt.Point;

public class LeftDirectionBlock extends Block{

	public LeftDirectionBlock(Point coordinates) {
		super(coordinates);
		
		this.setBlockType(BlockType.LEFTDIRECTION);
	}

}
