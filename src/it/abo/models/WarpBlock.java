package it.abo.models;

import java.awt.Point;

public class WarpBlock extends Block {
	
	WarpBlock twin;
	
	public WarpBlock(Point coordinates) {
		super(coordinates);
		
		this.setBlockType(BlockType.WARP);
	}
	
	public WarpBlock getTwin() {
		return twin;
	}
	
	public void setTwin(WarpBlock twin) {
		this.twin = twin;
	}

}
