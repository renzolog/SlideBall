package it.abo.models;

import java.awt.Point;

public class EmptyBlock extends Block {

	public EmptyBlock(Point coordinates) {
		super(coordinates);
		
		setBlockType(BlockType.EMPTY);
	}
	
}
