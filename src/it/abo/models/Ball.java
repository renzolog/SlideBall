package it.abo.models;

import java.awt.Point;

public class Ball extends Block {
	
	Point previousCoordinates;
	int ballPoints;
	
	public Ball(Point coordinates) {
		super(coordinates);
		
		setPreviousCoordinates(coordinates);
		setBallPoints(0);
		setBlockType(BlockType.BALL);
	}
	
	public Point getPreviousCoordinates() {
		return previousCoordinates;
	}

	public void setPreviousCoordinates(Point previousCoordinates) {
		this.previousCoordinates = previousCoordinates;
	}

	public int getBallPoints() {
		return ballPoints;
	}
	
	public void setBallPoints(int ballPoints) {
		this.ballPoints = ballPoints;
	}

	public void moveDown() {
		if(getCoordinates().y+1 < 15) {
			++getCoordinates().y;
		}
	}
}
