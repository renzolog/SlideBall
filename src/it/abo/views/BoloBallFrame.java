package it.abo.views;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.abo.actions.ResetListener;
import it.abo.actions.TurnEndListener;
import it.abo.models.Ball;
import it.abo.models.BlockType;
import it.abo.models.Player;
import it.abo.models.PlayerColor;
import it.abo.views.GamePanel.BallThrow;


public class BoloBallFrame extends JFrame implements TurnEndListener, ResetListener {

	private static final long serialVersionUID = 1L;
	
	GamePanel gamePanel;
	BallThrow ballThrow;
	JPanel pointsPanel;
	static JLabel player1Points;
	static JLabel player2Points;
	JLabel gameName;
	
	public BoloBallFrame() {
		
		gamePanel = new GamePanel();
		gamePanel.setTurnEndListener(this);
		gamePanel.setResetListener(this);
		
		setResizable(false);
		
		pointsPanel = new JPanel();
		pointsPanel.setLayout(new BorderLayout());
		
		player1Points = new JLabel("Green: " + gamePanel.player1.getTotalPoints());
		player1Points.setForeground(Color.YELLOW);
		player1Points.setFont(new Font("Comic Sans", Font.BOLD, 18));
		player1Points.setForeground(Color.GREEN);
		
		gameName = new JLabel("Zio Pippo Ball", JLabel.CENTER);
		gameName.setForeground(Color.YELLOW);
		gameName.setFont(new Font("Comic Sans", Font.BOLD, 20));
		
		player2Points = new JLabel("Red: " + gamePanel.player2.getTotalPoints());
		player2Points.setForeground(Color.YELLOW);
		player2Points.setFont(new Font("Comic Sans", Font.BOLD, 18));
		player2Points.setForeground(Color.RED);
		
		this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) 
            {   
            	if(gamePanel.freezed)
            		return;
            	
            	Player currPlayer = gamePanel.getPlayer();
            	int playerX = currPlayer.getCoordinates().x;
            	int playerY = currPlayer.getCoordinates().y;
            	
                int keyCode = e.getKeyCode();
                
                switch(keyCode)
                {
                case 39:
                	currPlayer.moveRight();
                    gamePanel.updatePlayer(currPlayer);
                    break;
                case 37:
                	currPlayer.moveLeft();
                    gamePanel.updatePlayer(currPlayer);
                    break;
                case 40:
                	
                	for(int i = 0; i < - 1; ++i) {
                		
                	}
                	
                	if(gamePanel.getBallRow()[playerX].getBlockType() == BlockType.BALL 
                	&& gamePanel.blocksMatrix
                	[playerY + 2][playerX].getBlockType() == BlockType.EMPTY) {
                		Ball ball = (Ball) gamePanel.getBallRow()[playerX];
                		ballThrow = gamePanel.new BallThrow(ball);
                		ballThrow.start();	
                	}
                    break;
                case 38:
                    break;
                }                    
            }
        }); 
		
		pointsPanel.setBackground(Color.BLUE);
		pointsPanel.add(player1Points, BorderLayout.WEST);
		pointsPanel.add(gameName, BorderLayout.CENTER);
		pointsPanel.add(player2Points, BorderLayout.EAST);
		
		add(pointsPanel, BorderLayout.SOUTH);
		add(gamePanel, BorderLayout.CENTER);
		setSize(1080, 650);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void turnEnd(Player player) {
		
		if(player.getPlayerColor() == PlayerColor.GREEN) {
			player1Points.setText("Green: " + player.getTotalPoints());
			player1Points.repaint();
			player1Points.revalidate();
			
		} else {
			player2Points.setText("Red: " + player.getTotalPoints());
			player2Points.repaint();
			player2Points.revalidate();
		}
	}

	@Override
	public void reset() {
		this.remove(gamePanel);
		this.repaint();
		
		gamePanel = new GamePanel();
		
		this.add(gamePanel, BorderLayout.CENTER);
		this.revalidate();
		
		player1Points.setText("Green: " + gamePanel.player1.getTotalPoints());
		player1Points.repaint();
		player2Points.revalidate();
		
		player2Points.setText("Red: " + gamePanel.player2.getTotalPoints());
		player1Points.repaint();
		player2Points.revalidate();
		
		gamePanel.setTurnEndListener(this);
		gamePanel.setResetListener(this);
		
	}
	
	
}
