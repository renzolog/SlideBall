package it.abo.models;

import java.awt.Point;

public class RightDirectionBlock extends Block {

	public RightDirectionBlock(Point coordinates) {
		super(coordinates);
		
		this.setBlockType(BlockType.RIGHTDIRECTION);
	}
	

}
