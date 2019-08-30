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

	static final ImageIcon greenPlayerIcon = new ImageIcon(BoloBallFrame.basePath + "green-player.png");
	static final ImageIcon redPlayerIcon = new ImageIcon(BoloBallFrame.basePath + "red-player.png");
	static final ImageIcon greenBallIcon = new ImageIcon(BoloBallFrame.basePath + "green-ball.png");
	static final ImageIcon redBallIcon = new ImageIcon(BoloBallFrame.basePath + "red-ball.png");
	static final ImageIcon pointsIcon = new ImageIcon(BoloBallFrame.basePath + "points.png");
	static final ImageIcon hardBlockIcon = new ImageIcon(BoloBallFrame.basePath + "hardBlock.jpg");
	static final ImageIcon leftArrowIcon = new ImageIcon(BoloBallFrame.basePath + "leftArrow.png");
	static final ImageIcon rightArrowIcon = new ImageIcon(BoloBallFrame.basePath + "rightArrow.png");
	static final ImageIcon warpIcon = new ImageIcon(BoloBallFrame.basePath + "warp.png");
	static final ImageIcon inactiveWarpIcon = new ImageIcon(BoloBallFrame.basePath + "inactiveWarp.png");

	public GamePanel() {

		setLayout(new GridLayout(rows, columns));
		setup();
	}

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

		int hardBlockCount = 20;
		int pointsBlockCount = 10;
		int leftDirectionBlockCount = 35;
		int rightDirectionBlockCount = 35;
		int warpBlockCount = 6;

		warpArr = new WarpBlock[warpBlockCount];

		Random random = new Random();

		while (hardBlockCount > 0 || pointsBlockCount > 0 || leftDirectionBlockCount > 0 || rightDirectionBlockCount > 0
				|| warpBlockCount > 0) { /* costruisco la matrice di blocchi */

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

		for (int i = 0; i < warpArr.length; ++i) { /*
													 * per ogni blocco di teletrasporto, assegno un blocco gemello dove
													 * verrà teletrasportata la pallina (quelli di indice pari
													 * accoppiati coi dispari immediatamente successivi)
													 */

			warpArr[i].setTwin(warpArr[(i % 2 == 0) ? i + 1 : i - 1]);
		}

		panelHolder = new JPanel[rows][columns];

		for (int i = 0; i < rows; ++i) { /* disegno la griglia */
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

	Block[] getBallRow() {
		return ballMatrix[turn % 2];
	}

	public void setTurnEndListener(TurnEndListener turnEndListener) {
		this.turnEndListener = turnEndListener;
	}

	public void setResetListener(ResetListener resetListener) {
		this.resetListener = resetListener;
	}

	public void printMatrix() { /* debug only */
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

	public void repositionPlayer() { /*
										 * ridisegna il giocatore successivo alla fine del turno nell'ultima posizione
										 * registrata
										 */

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

	public void updatePlayer(Player player) { /* ridisegna il giocatore quando si muove */

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

	public void updatePlayerBalls(Block[] ballsRow, int turn) { /*
																 * ridisegna le palline a disposizione di ogni giocatore
																 * all'inizio di ogni turno
																 */

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

		@SuppressWarnings("incomplete-switch") /*
												 * Nei case labels manca il player, che però non si incontra mai con la
												 * ball nella griglia
												 */
		public void ballMove(Ball ball) throws InterruptedException {

			int x, y, previousX, previousY;

			freezed = true;

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

			pointsAssignment(getPlayer(), ball);

			if (ball.getCoordinates().y == rows - 1) { /* Se la pallina è all'ultimo row scivola via dallo schermo */

				int count = ball.getCoordinates().x;

				if (getPlayer().getPlayerColor() == PlayerColor.RED)
					count = columns - count;

				while (count >= 0) {
					slideAway(ball);
					Thread.sleep(90);
					--count;
				}
			}

			if (turnEndListener != null) /* aggiorna i punteggi */
				turnEndListener.turnEnd(getPlayer());

			player1.setHasMovesLeft(stillMovesLeft(player1));
			player2.setHasMovesLeft(stillMovesLeft(player2));

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
					&& blocksMatrix[twinY + 1][twinX].getBlockType() != BlockType.POINTS) {
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
				
				if (blocksMatrix[twinY + 2][twinX].getBlockType() != BlockType.EMPTY
						&& blocksMatrix[twinY + 2][twinX].getBlockType() != BlockType.POINTS 
						&& blocksMatrix[warpBlock.getCoordinates().y - 1][warpBlock.getCoordinates().x].getBlockType() != BlockType.BALL) {
					
					int warpX = warpBlock.getCoordinates().x;
					int warpY = warpBlock.getCoordinates().y;
					
					panelHolder[warpY][warpX].removeAll();
					panelHolder[warpY][warpX].repaint();
					panelHolder[warpY][warpX].add(new JLabel(inactiveWarpIcon));
					panelHolder[warpY][warpX].revalidate();
				}
				
			}
		}

		public void getPoints(Ball ball,
				PointsBlock pointsBlock) { /*
											 * assegna il punteggio per la pallina appena lanciata alla pallina stessa
											 */

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

		public void deactivate(Ball ball) { /*
											 * viene chiamato quando la pallina ha terminato il movimento (ma non si
											 * trova nell'ultimo row)
											 */

			ImageIcon icon = (turn % 2 == 0) ? greenBallIcon : redBallIcon;
			Color pointsColor = (getPlayer().getPlayerColor() == PlayerColor.GREEN) ? Color.BLACK : Color.YELLOW;

			int y = ball.getCoordinates().y;
			int x = ball.getCoordinates().x;

			panelHolder[y][x].removeAll();
			panelHolder[y][x].repaint();

			JLabel label = new JLabel();

			label.setIcon(icon);
			label.setText(String.valueOf(ball.getBallPoints()));
			label.setFont(new Font("Comic Sans", Font.BOLD, 18));
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.CENTER);
			label.setForeground(pointsColor);

			panelHolder[y][x].add(label);
			panelHolder[y][x].revalidate();

			ball.setActive(false);
		}

		public void slideAway(Ball ball) { /* viene chiamato quando la pallina arriva all'ultimo row */

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
