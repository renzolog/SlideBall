package it.abo.models;

import java.awt.Point;

public class Player extends Block {
	
	PlayerColor playerColor;
	Point previousCoordinates;
	int totalPoints;
	boolean hasMovesLeft;

	public Player(Point coordinates, PlayerColor playerColor) {
		super(coordinates);
		
		setHasMovesLeft(true);
		setPlayerColor(playerColor);
		setTotalPoints(0);
		setBlockType(BlockType.PLAYER);
	}
	
	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	public void setPlayerColor(PlayerColor playerColor) {
		this.playerColor = playerColor;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public void moveLeft() {
		if(getCoordinates().x > 0) {
			previousCoordinates = this.getCoordinates();
			setCoordinates(new Point(getCoordinates().x - 1, getCoordinates().y));
		} else {
			previousCoordinates = this.getCoordinates();
		}
	}
	
	public void moveRight() {
		if(getCoordinates().x < 28) {
			previousCoordinates = this.getCoordinates();
			setCoordinates(new Point(getCoordinates().x + 1, getCoordinates().y));
		} else {
			previousCoordinates = this.getCoordinates();
		}
	}
	
	public void move(int x, int y) {
		if(getCoordinates().x >= 0 && getCoordinates().x < 29) {
			previousCoordinates = this.getCoordinates();
			setCoordinates(new Point(x, y));
		} else {
			previousCoordinates = this.getCoordinates();
		}
	}

	public Point getPreviousCoordinates() {
		return previousCoordinates;
	}
	
	public boolean getHasMovesLeft() {
		return hasMovesLeft;
	}
	
	public void setHasMovesLeft(boolean hasMovesLeft) {
		this.hasMovesLeft = hasMovesLeft;
	}
}
