package it.abo.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.abo.actions.ResetListener;
import it.abo.actions.TurnEndListener;
import it.abo.models.Ball;
import it.abo.models.Block;
import it.abo.models.BlockType;
import it.abo.models.EmptyBlock;
import it.abo.models.HardBlock;
import it.abo.models.LeftDirectionBlock;
import it.abo.models.Player;
import it.abo.models.PlayerColor;
import it.abo.models.PointsBlock;
import it.abo.models.RightDirectionBlock;
import it.abo.models.WarpBlock;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	int turn;
	boolean freezed; /*
						 * utile per impedire le mosse del giocatore mentre le palline si muovono sullo
						 * schermo
						 */

	final static int rows = 15;
	final static int columns = 29;

	JPanel[][] panelHolder;

	Block[][] blocksMatrix;
	Block[][] ballMatrix;
	WarpBlock[] warpArr;

	Player player1;
	Player player2;

	Player[] playerArr;

	TurnEndListener turnEndListener;
	ResetListener resetListener;
	
//	static final ImageIcon greenPlayerIcon = new ImageIcon(BoloBallFrame.basePath + "green-player.png");
//	static final ImageIcon redPlayerIcon = new ImageIcon(BoloBallFrame.basePath + "red-player.png");
//	static final ImageIcon greenBallIcon = new ImageIcon(BoloBallFrame.basePath + "green-ball.png");
//	static final ImageIcon redBallIcon = new ImageIcon(BoloBallFrame.basePath + "red-ball.png");
//	static final ImageIcon pointsIcon = new ImageIcon(BoloBallFrame.basePath + "points.png");
//	static final ImageIcon hardBlockIcon = new ImageIcon(BoloBallFrame.basePath + "hardBlock.jpg");
//	static final ImageIcon leftArrowIcon = new ImageIcon(BoloBallFrame.basePath + "leftArrow.png");
//	static final ImageIcon rightArrowIcon = new ImageIcon(BoloBallFrame.basePath + "rightArrow.png");
//	static final ImageIcon warpIcon = new ImageIcon(BoloBallFrame.basePath + "warp.png");
//	static final ImageIcon inactiveWarpIcon = new ImageIcon(BoloBallFrame.basePath + "inactiveWarp.png");
        
	final ClassLoader cl = GamePanel.class.getClassLoader();
	
	final ImageIcon greenPlayerIcon = new ImageIcon(cl.getResource("green-player.png"));
	final ImageIcon redPlayerIcon = new ImageIcon(cl.getResource("red-player.png"));
	final ImageIcon greenBallIcon = new ImageIcon(cl.getResource("green-ball.png"));
	final ImageIcon redBallIcon = new ImageIcon(cl.getResource("red-ball.png"));
	final ImageIcon pointsIcon = new ImageIcon(cl.getResource("points.png"));
	final ImageIcon hardBlockIcon = new ImageIcon(cl.getResource("hardBlock.jpg"));
	final ImageIcon leftArrowIcon = new ImageIcon(cl.getResource("leftArrow.png"));
	final ImageIcon rightArrowIcon = new ImageIcon(cl.getResource("rightArrow.png"));
	final ImageIcon warpIcon = new ImageIcon(cl.getResource("warp.png"));
	final ImageIcon inactiveWarpIcon = new ImageIcon(cl.getResource("inactiveWarp.png"));

	public GamePanel() {

		setLayout(new GridLayout(rows, columns));
		setup();
	}

	/* Sets up the grid for a new game */
	
	public void setup() { 

		player1 = new Player(new Point(0, 0), PlayerColor.GREEN);
		player2 = new Player(new Point(0, 0), PlayerColor.RED);
		playerArr = new Player[] { player1, player2 };

		turn = 0;

		blocksMatrix = new Block[rows][columns];
		ballMatrix = new Block[2][columns];

		for (int y = 0; y < rows; ++y) {
			for (int x = 0; x < columns; ++x)

				if (y == 1) {
					ballMatrix[0][x] = new Ball(new Point(x, y));
					ballMatrix[1][x] = new Ball(new Point(x, y));
				} else
					blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));
		}

		blocksMatrix[1] = getBallRow();

		blocksMatrix[player1.getCoordinates().y][player1.getCoordinates().x] = player1;

		/* Here to change the number of blocks for each type */
		
		int hardBlockCount = 20;
		int pointsBlockCount = 10;
		int leftDirectionBlockCount = 35;
		int rightDirectionBlockCount = 35;
		int warpBlockCount = 6;

		warpArr = new WarpBlock[warpBlockCount];

		Random random = new Random();

		/* Build the matrix that represent our game grid */
		
		while (hardBlockCount > 0 || pointsBlockCount > 0 || leftDirectionBlockCount > 0 || rightDirectionBlockCount > 0
				|| warpBlockCount > 0) {

			int x = random.nextInt(columns);
			int y = random.nextInt(rows - 4) + 3;

			while (blocksMatrix[y][x].getBlockType() != BlockType.EMPTY
					|| blocksMatrix[y + 1][x].getBlockType() != BlockType.EMPTY) {
				x = random.nextInt(columns);
				y = random.nextInt(rows - 4) + 3;
			}

			if (hardBlockCount > 0) {
				blocksMatrix[y][x] = new HardBlock(new Point(x, y));
				--hardBlockCount;
				continue;
			}

			if (leftDirectionBlockCount > 0) {
				blocksMatrix[y][x] = new LeftDirectionBlock(new Point(x, y));
				--leftDirectionBlockCount;
				continue;
			}

			if (rightDirectionBlockCount > 0) {
				blocksMatrix[y][x] = new RightDirectionBlock(new Point(x, y));
				--rightDirectionBlockCount;
				continue;
			}

			if (warpBlockCount > 0) {
				blocksMatrix[y][x] = new WarpBlock(new Point(x, y));
				warpArr[--warpBlockCount] = (WarpBlock) blocksMatrix[y][x];
				continue;
			}

			if (pointsBlockCount > 0) {
				blocksMatrix[y][x] = new PointsBlock(new Point(x, y));
				--pointsBlockCount;
				continue;
			}
		}
		
		/* For each warp block, a twin block is assigned (balls entering one of 
		 * them will come out from the other one). Even indexes are coupled with
		 * the following even index, same story with odd indexes. */
		
		for (int i = 0; i < warpArr.length; ++i) { 

			warpArr[i].setTwin(warpArr[(i % 2 == 0) ? i + 1 : i - 1]);
		}
		
		/* Build JPanel matrix for visual representation of game grid (empty cells for now) */
		
		panelHolder = new JPanel[rows][columns];

		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				
				panelHolder[i][j] = new JPanel();
				panelHolder[i][j].setLayout(new BorderLayout());

				if (i == 0)
					panelHolder[i][j].setBackground(Color.BLACK);
				else {
					panelHolder[i][j].setBackground(Color.GRAY);
					panelHolder[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				}

				add(panelHolder[i][j]);
			}
		}
	}

	Player getPlayer() {
		return playerArr[turn % 2];
	}

	/* Each player has its own array of Ball (child class of Block), switching turn by turn */
	
	Block[] getBallRow() {
		return ballMatrix[turn % 2];
	}

	public void setTurnEndListener(TurnEndListener turnEndListener) {
		this.turnEndListener = turnEndListener;
	}

	public void setResetListener(ResetListener resetListener) {
		this.resetListener = resetListener;
	}

	/* DEBUG ONLY - prints on console the blocks matrix */
	
	public void printMatrix() {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {

				switch (blocksMatrix[i][j].getBlockType()) {

				case BALL:
					System.out.print("O|");
					break;
				case EMPTY:
					System.out.print(" |");
					break;
				case HARD:
					System.out.print("#|");
					break;
				case LEFTDIRECTION:
					System.out.print("<|");
					break;
				case PLAYER:
					System.out.print("P|");
					break;
				case POINTS:
					System.out.print(".|");
					break;
				case RIGHTDIRECTION:
					System.out.print(">|");
					break;
				case WARP:
					System.out.print("@|");
				}
			}
			System.out.println();
		}
	}

	/* Adds JLabels with proper icons for each type of block in blocks matrix to JPanel matrix */
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for (int y = 0; y < rows; ++y) {
			for (int x = 0; x < columns; ++x) {

				switch (blocksMatrix[y][x].getBlockType()) {

				case PLAYER:
					panelHolder[y][x].add(new JLabel(greenPlayerIcon));
					break;
				case BALL:
					panelHolder[y][x].add(new JLabel(greenBallIcon));
					break;
				case HARD:
					panelHolder[y][x].add(new JLabel(hardBlockIcon));
					break;
				case LEFTDIRECTION:
					panelHolder[y][x].add(new JLabel(leftArrowIcon));
					break;
				case RIGHTDIRECTION:
					panelHolder[y][x].add(new JLabel(rightArrowIcon));
					break;
				case POINTS:
					panelHolder[y][x].add(new JLabel(pointsIcon));
					break;
				case WARP:
					panelHolder[y][x].add(new JLabel(warpIcon));
					break;
				case EMPTY:
				}

			}
		}

	}

	/* For each player's turn, this method draws the player in the last registered position */
	
	public void repositionPlayer() {

		Player inactivePlayer = (turn % 2 == 0) ? player2 : player1;
		Player nextPlayer = (turn % 2 == 0) ? player1 : player2;

		ImageIcon icon = (turn % 2 == 0) ? greenPlayerIcon : redPlayerIcon;

		blocksMatrix[0][inactivePlayer.getCoordinates().x] = new EmptyBlock(
				new Point(inactivePlayer.getCoordinates().x, inactivePlayer.getCoordinates().y));

		panelHolder[0][inactivePlayer.getCoordinates().x].removeAll();
		panelHolder[0][inactivePlayer.getCoordinates().x].repaint();

		blocksMatrix[0][nextPlayer.getCoordinates().x] = nextPlayer;
		panelHolder[0][nextPlayer.getCoordinates().x].add(new JLabel(icon));
		panelHolder[0][nextPlayer.getCoordinates().x].revalidate();
	}

	/* Updates the player position for each movement and redraws it consequently */
	
	public void updatePlayer(Player player) {

//		int coercedMax = Math.max(Math.min(player.getCoordinates().x-1, columns-1), 0);
//		int coercedMin = Math.max(Math.min(player.getCoordinates().x+1, columns-1), 0);

		if (player.getCoordinates().x >= 0 && player.getCoordinates().x < columns) {

			ImageIcon icon = (turn % 2 == 0) ? greenPlayerIcon : redPlayerIcon;

			panelHolder[player.getPreviousCoordinates().y][player.getPreviousCoordinates().x].removeAll();
			panelHolder[player.getPreviousCoordinates().y][player.getPreviousCoordinates().x].repaint();

			panelHolder[player.getCoordinates().y][player.getCoordinates().x].add(new JLabel(icon));
			panelHolder[player.getCoordinates().y][player.getCoordinates().x].revalidate();
		}
	}

	/* Redraws each player's set of balls at the beginning of each turn */
	
	public void updatePlayerBalls(Block[] ballsRow, int turn) {

		ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

		for (int i = 0; i < columns; ++i) {
			panelHolder[1][i].removeAll();
			panelHolder[1][i].repaint();

			if (ballsRow[i].getBlockType() == BlockType.BALL) {
				panelHolder[1][i].add(new JLabel(icon));
				panelHolder[1][i].revalidate();
				;
			}
		}
	}

	/* Checks if the current player still has moves available */
	
	public boolean stillMovesLeft(Player player) {

		int y = (player.getPlayerColor() == PlayerColor.GREEN) ? 0 : 1;

		for (int j = 0; j < columns; ++j) {
			if (ballMatrix[y][j].getBlockType() == BlockType.BALL
					&& blocksMatrix[2][j].getBlockType() == BlockType.EMPTY)
				return true;
		}
		return false;
	}
	

	class BallThrow extends Thread {

		Ball ball;

		BallThrow(Ball ball) {

			this.ball = ball;
		}
		
		/* Describes balls' interaction while moving down depending on the underlying block type.
		 * Player's icon is missing because it doesn't appear in the blocks'grid 
		 * (that explains the following SuppressWarnings)
		 */
		
		@SuppressWarnings("incomplete-switch")
		public void ballMove(Ball ball) throws InterruptedException {

			int x, y, previousX, previousY;

			freezed = true; /* prevents the player to move while the ball 
							is moving (see key listener in BoloBall frame */

			while (ball.isActive() && ball.getCoordinates().y < rows - 1) {

				y = ball.getCoordinates().y;
				x = ball.getCoordinates().x;

				previousY = ball.getPreviousCoordinates().y;
				previousX = ball.getPreviousCoordinates().x;

				ball.setPreviousCoordinates(ball.getCoordinates());

				switch (blocksMatrix[y + 1][x].getBlockType()) {

				case BALL:

					if (x == 0 || x == columns - 1)
						ball.setActive(false);

					else if (blocksMatrix[previousY + 1][previousX].getBlockType() == BlockType.RIGHTDIRECTION
							&& blocksMatrix[y][x - 1].getBlockType() == BlockType.EMPTY
							|| blocksMatrix[y][x - 1].getBlockType() == BlockType.POINTS)
						slideLeft(ball);

					else if (blocksMatrix[previousY + 1][previousX].getBlockType() == BlockType.LEFTDIRECTION
							&& blocksMatrix[y][x + 1].getBlockType() == BlockType.EMPTY
							|| blocksMatrix[y][x + 1].getBlockType() == BlockType.POINTS)
						slideRight(ball);

					else
						ball.setActive(false);
					break;

				case EMPTY:
					moveDown(ball);
					break;

				case HARD:
					ball.setActive(false);
					break;

				case LEFTDIRECTION:

					if (x == 0)
						ball.setActive(false);
					else if (blocksMatrix[y][x - 1].getBlockType() == BlockType.EMPTY)
						slideLeft(ball);
					else if (blocksMatrix[y][x - 1].getBlockType() == BlockType.POINTS)
						getPoints(ball, (PointsBlock) blocksMatrix[y][x - 1]);
					else if (blocksMatrix[y][x - 1].getBlockType() == BlockType.WARP)
						teleport(ball, (WarpBlock) blocksMatrix[y][x - 1]);
					else
						ball.setActive(false);
					break;

				case POINTS:
					getPoints(ball, (PointsBlock) blocksMatrix[y + 1][x]);
					break;

				case RIGHTDIRECTION:

					if (x == columns - 1)
						ball.setActive(false);
					else if (blocksMatrix[y][x + 1].getBlockType() == BlockType.EMPTY)
						slideRight(ball);
					else if (blocksMatrix[y][x + 1].getBlockType() == BlockType.POINTS)
						getPoints(ball, (PointsBlock) blocksMatrix[y][x + 1]);
					else if (blocksMatrix[y][x + 1].getBlockType() == BlockType.WARP)
						teleport(ball, (WarpBlock) blocksMatrix[y][x + 1]);
					else
						ball.setActive(false);

					break;

				case WARP:
					teleport(ball, (WarpBlock) blocksMatrix[y + 1][x]);
				}

				y = ball.getCoordinates().y;
				x = ball.getCoordinates().x;

				Thread.sleep(130);
			}
			
			deactivate(ball);
			
			if (ball.getCoordinates().y == rows - 1) { /* If the ball reaches the last row, it slides away from the screen */
				
				int count = ball.getCoordinates().x;
				
				if (getPlayer().getPlayerColor() == PlayerColor.RED)
					count = columns - count;
				
				while (count >= 0) {
					slideAway(ball);
					Thread.sleep(90);
					--count;
				}
			}
			
			/* If the ball stops right under a warp and it's not the last row, it deactivates the twin warp */
			
			if(blocksMatrix[ball.getCoordinates().y - 1][ball.getCoordinates().x].getBlockType() == BlockType.WARP) {
				
				WarpBlock overBlock = (WarpBlock) blocksMatrix[ball.getCoordinates().y - 1][ball.getCoordinates().x];
				closeTwinWarp(overBlock);
			}
			
			pointsAssignment(getPlayer(), ball);

			/* End of turn, updates scores */
			
			if (turnEndListener != null)
				turnEndListener.turnEnd(getPlayer());

			player1.setHasMovesLeft(stillMovesLeft(player1));
			player2.setHasMovesLeft(stillMovesLeft(player2));

			/* If no moves left, end of the game */
			
			if (!player1.getHasMovesLeft() && !player2.getHasMovesLeft()) {

				Player winner = (player1.getTotalPoints() > player2.getTotalPoints()) ? player1 : player2;

				int opt = JOptionPane.showConfirmDialog(getParent(),
						winner.getPlayerColor() + " player won! Play again?", "Victory", JOptionPane.YES_NO_OPTION);

				switch (opt) {
				case JOptionPane.YES_OPTION:

					resetListener.reset();
					return;

				case JOptionPane.NO_OPTION:
					System.exit(0);
				}
			}

			/* If there are still moves left, turn is passed and player and balls are switched */
			
			++turn;
			
			if(!getPlayer().getHasMovesLeft()) {
				
				blocksMatrix[1] = getBallRow();
				repositionPlayer();
				updatePlayerBalls(blocksMatrix[1], turn);
				Thread.sleep(1500);
				
				++turn;
			}
			
			blocksMatrix[1] = getBallRow();
			repositionPlayer();
			updatePlayerBalls(blocksMatrix[1], turn);
			
			freezed = false;
		}

		public void moveDown(Ball ball) {

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			ball.setCoordinates(new Point(x, y + 1));

			blocksMatrix[y + 1][x] = ball;

			panelHolder[y + 1][x].add(new JLabel(icon));
			panelHolder[y + 1][x].revalidate();

			blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));

			panelHolder[y][x].removeAll();
			panelHolder[y][x].repaint();

			ball.setBallPoints(ball.getBallPoints() + 2);
		}

		public void slideLeft(Ball ball) {

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			ball.setCoordinates(new Point(x - 1, y));

			blocksMatrix[y][x - 1] = ball;

			panelHolder[y][x - 1].add(new JLabel(icon));
			panelHolder[y][x - 1].revalidate();

			blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));

			panelHolder[y][x].removeAll();
			panelHolder[y][x].repaint();

			if (blocksMatrix[ball.getPreviousCoordinates().y + 1][ball.getPreviousCoordinates().x]
					.getBlockType() == BlockType.LEFTDIRECTION) {

				blocksMatrix[y + 1][x] = new RightDirectionBlock(new Point(x, y + 1));

				panelHolder[y + 1][x].removeAll();
				panelHolder[y + 1][x].add(new JLabel(rightArrowIcon));
				panelHolder[y + 1][x].revalidate();
			}
		}

		public void slideRight(Ball ball) {

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			ball.setCoordinates(new Point(x + 1, y));

			blocksMatrix[y][x + 1] = ball;

			panelHolder[y][x + 1].add(new JLabel(icon));
			panelHolder[y][x + 1].revalidate();

			blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));

			panelHolder[y][x].removeAll();
			panelHolder[y][x].repaint();

			if (blocksMatrix[ball.getPreviousCoordinates().y + 1][ball.getPreviousCoordinates().x]
					.getBlockType() == BlockType.RIGHTDIRECTION) {

				blocksMatrix[y + 1][x] = new LeftDirectionBlock(new Point(x, y + 1));

				panelHolder[y + 1][x].removeAll();
				panelHolder[y + 1][x].add(new JLabel(leftArrowIcon));
				panelHolder[y + 1][x].revalidate();
			}
		}

		public void teleport(Ball ball, WarpBlock warpBlock) {

			WarpBlock twin = warpBlock.getTwin();
			int twinY = twin.getCoordinates().y;
			int twinX = twin.getCoordinates().x;

			if (blocksMatrix[twinY + 1][twinX].getBlockType() != BlockType.EMPTY
					&& blocksMatrix[twinY + 1][twinX].getBlockType() != BlockType.POINTS
					&& ball.getCoordinates().y < rows) {
				deactivate(ball);
				
			} else {

				ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

				int y = ball.getCoordinates().y;
				int x = ball.getCoordinates().x;

				int newY = warpBlock.getTwin().getCoordinates().y + 1;
				int newX = warpBlock.getTwin().getCoordinates().x;

				panelHolder[y][x].removeAll();
				panelHolder[y][x].repaint();

				blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));

				panelHolder[newY][newX].add(new JLabel(icon));
				panelHolder[newY][newX].revalidate();

				blocksMatrix[newY][newX] = ball;
				ball.setCoordinates(new Point(newX, newY));
				
			}
		}
		
		public void closeTwinWarp(WarpBlock warpBlock) {
			
			WarpBlock twin = warpBlock.getTwin();
			int twinY = twin.getCoordinates().y;
			int twinX = twin.getCoordinates().x;
			
			panelHolder[twinY][twinX].removeAll();
			panelHolder[twinY][twinX].repaint();
			panelHolder[twinY][twinX].add(new JLabel(inactiveWarpIcon));
			panelHolder[twinY][twinX].revalidate();
		}

		/* Adds 10 points to the ball's score when capturing a point block */
		
		public void getPoints(Ball ball, PointsBlock pointsBlock) { 

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;

			int ballY = ball.getCoordinates().y;
			int ballX = ball.getCoordinates().x;

			int pointsBlockY = pointsBlock.getCoordinates().y;
			int pointsBlockX = pointsBlock.getCoordinates().x;

			ball.setCoordinates(new Point(pointsBlockX, pointsBlockY));

			blocksMatrix[pointsBlockY][pointsBlockX] = ball;

			panelHolder[pointsBlockY][pointsBlockX].add(new JLabel(icon));
			panelHolder[pointsBlockY][pointsBlockX].revalidate();

			blocksMatrix[ballY][ballX] = new EmptyBlock(new Point(ballY, ballX));
			panelHolder[ballY][ballX].removeAll();
			panelHolder[ballY][ballX].repaint();

			ball.setBallPoints(ball.getBallPoints() + 10);
		}

		public void pointsAssignment(Player player, Ball ball) {

			player.setTotalPoints(player.getTotalPoints() + ball.getBallPoints());
		}

		/* Called when the ball has stopped. Displays the ball's score on the ball itself */
		
		public void deactivate(Ball ball) { 

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;
			Color pointsColor = (getPlayer().getPlayerColor() == PlayerColor.GREEN) ? Color.BLACK : Color.YELLOW;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			panelHolder[y][x].removeAll();
			panelHolder[y][x].repaint();

			JLabel label = new JLabel(icon, SwingConstants.CENTER);
			
			label.setText(String.valueOf(ball.getBallPoints()));
			label.setFont(new Font("Comic Sans", Font.BOLD, 18));
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.CENTER);
			label.setForeground(pointsColor);

			panelHolder[y][x].add(label);
			panelHolder[y][x].revalidate();

			ball.setActive(false);
		}

		/* Called when the ball reaches the last row. It slides away from the screen */
		
		public void slideAway(Ball ball) {

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;
			Color pointsColor = (getPlayer().getPlayerColor() == PlayerColor.GREEN) ? Color.BLACK : Color.YELLOW;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			int destinationX = (getPlayer().getPlayerColor() == PlayerColor.GREEN) ? x - 1 : x + 1;

			JLabel label = new JLabel();

			label.setIcon(icon);
			label.setText(String.valueOf(ball.getBallPoints()));
			label.setFont(new Font("Comic Sans", Font.BOLD, 18));
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.CENTER);
			label.setForeground(pointsColor);

			if ((getPlayer().getPlayerColor() == PlayerColor.GREEN) ? x > 0 : x < columns - 1) {

				ball.setCoordinates(new Point(destinationX, y));

				blocksMatrix[y][destinationX] = ball;
				blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));

				panelHolder[y][x].removeAll();
				panelHolder[y][x].repaint();

				panelHolder[y][destinationX].add(label);
				panelHolder[y][destinationX].revalidate();

				y = ball.getCoordinates().y;
				x = ball.getCoordinates().x;

			} else {

				blocksMatrix[y][x] = new EmptyBlock(new Point(x, y));
				panelHolder[y][x].removeAll();
				panelHolder[y][x].repaint();
			}
		}

		@Override
		public void run() {
			try {
				ballMove(ball);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
