package it.abo.models;

import java.awt.Point;

public class PointsBlock extends Block {

	public PointsBlock(Point coordinates) {
		super(coordinates);
		
		setBlockType(BlockType.POINTS);
	}

}
