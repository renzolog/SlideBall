package it.abo.models;

import java.awt.Point;

public class HardBlock extends Block {
	
	public HardBlock(Point coordinates) {
		super(coordinates);
		
		setBlockType(BlockType.HARD);
	}

}
