package it.abo.views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import it.abo.actions.BackToMainMenuListener;
import it.abo.actions.InstructionsListener;

public class InstructionsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	InstructionsListener instructionsListener;
	BackToMainMenuListener backToMainMenuListener;
	
	public static int page = 1;
		
	final static ImageIcon fieldInstructions = new ImageIcon(BoloBallFrame.basePath + "fieldInstructions.png");
	final static ImageIcon scoreInstructions = new ImageIcon(BoloBallFrame.basePath + "scoreInstructions.png");
	final static ImageIcon nextPage = new ImageIcon(BoloBallFrame.basePath + "nextPage.png");
	final static ImageIcon previousPage = new ImageIcon(BoloBallFrame.basePath + "previousPage.png");
	final static ImageIcon arrowBlocks = new ImageIcon(BoloBallFrame.basePath + "arrowBlocksInstructions.png");
	final static ImageIcon hardBlock = new ImageIcon(BoloBallFrame.basePath + "hardBlockInstructions.png");
	final static ImageIcon pointsBlock = new ImageIcon(BoloBallFrame.basePath + "pointsBlockInstructions.png");
	final static ImageIcon warpBlock = new ImageIcon(BoloBallFrame.basePath + "warpBlockInstructions.png");
	final static ImageIcon slideInstructions = new ImageIcon(BoloBallFrame.basePath + "slideInstructions.png");
	final static ImageIcon blockedInstructions = new ImageIcon(BoloBallFrame.basePath + "blockedInstructions.png");
	final static ImageIcon keysInstructions = new ImageIcon(BoloBallFrame.basePath + "keysInstructions.png");
	final static ImageIcon mainMenu = new ImageIcon(BoloBallFrame.basePath + "mainMenu.png");

	InstructionsPanel() {
		
		switch(page) {
		case 1:
			page1();
			break;
		case 2:
			page2();
			break;
		case 3:
			page3();
			break;
		case 4:
			page4();
		}
	}
	
	public void page1() {
		
		setLayout(new GridBagLayout());

		JLabel title1 = new JLabel("General Rules", SwingConstants.CENTER);
		title1.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		title1.setForeground(Color.YELLOW);

		JLabel pic1 = new JLabel(fieldInstructions);
		
		JLabel textPic1 = new JLabel("<html>SlideBall is a two players game where<br/>the main goal is to push balls down a grid<br/>and make them run the longest distance<br/>before they stop.</html>");
		textPic1.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		textPic1.setForeground(Color.YELLOW);
		
		JLabel pic2 = new JLabel(scoreInstructions);
		
		JLabel textPic2 = new JLabel("<html>For each row the ball rolls in,<br/>the player earns 2 points (only vertical<br/>movements make points grow)<br/><br/>When no more balls are available for<br/>either player, that’s the end of the game,<br/>and the player with the highest score wins.</html>");
		textPic2.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		textPic2.setForeground(Color.YELLOW);
		
		JLabel pageCount = new JLabel("1/4", SwingConstants.RIGHT);
		pageCount.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		pageCount.setForeground(Color.YELLOW);
		
		JLabel nextPageLabel = new JLabel(nextPage, SwingConstants.LEFT);
		nextPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				++page;
				instructionsListener.instructions();
			}
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		pan.setOpaque(false);
		pan.add(pageCount);
		pan.add(nextPageLabel);
		
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.gridx = 0;
		cons.gridy = 0;
		cons.ipady = 50;
		cons.gridwidth = 2;
		cons.fill = GridBagConstraints.HORIZONTAL;
		add(title1, cons);

		cons.fill = GridBagConstraints.NONE;
		cons.gridwidth = 1;
		cons.gridy = 1;
		cons.weightx = 100;
		cons.insets = new Insets(0, 0, 50, 0);
		add(pic1, cons);

		cons.weightx = 0.0;
		cons.ipadx = 400;
		cons.ipady = 0;
		cons.gridx = 1;
		add(textPic1, cons);

		cons.gridy = 2;
		cons.gridx = 0;
		add(pic2, cons);

		cons.gridx = 1;
		cons.insets = new Insets(0, 30, 20, 0);
		add(textPic2, cons);
		
		cons.gridy = 3;
		cons.gridx = 0;
		cons.gridwidth = 2;
		cons.insets = new Insets(0, 454, 48, 0);
		add(pan, cons);
	}
	
	public void page2() {
	
		setLayout(new GridBagLayout());

		JLabel title2 = new JLabel("The Blocks", SwingConstants.CENTER);
		title2.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		title2.setForeground(Color.YELLOW);

		JLabel description = new JLabel("The grid is filled with several types of blocks, each one making the ball do different things");
		description.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		description.setForeground(Color.YELLOW);
		
		JLabel pic3 = new JLabel(arrowBlocks, SwingConstants.RIGHT);
		
		JLabel namePic3 = new JLabel("Directional blocks", SwingConstants.RIGHT);
		namePic3.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		namePic3.setForeground(Color.YELLOW);
		
		JLabel textPic3 = new JLabel("<html>They make the ball slide in the indicated direction,<br/>then the arrow is inverted.</html>", SwingConstants.RIGHT);
		textPic3.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textPic3.setForeground(Color.YELLOW);
		
		JLabel pic4 = new JLabel(hardBlock, SwingConstants.RIGHT);
		
		JLabel namePic4 = new JLabel("Hard block", SwingConstants.CENTER);
		namePic4.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		namePic4.setForeground(Color.YELLOW);
		
		JLabel textPic4 = new JLabel("<html>It makes the ball stop instantly.</html>", SwingConstants.RIGHT);
		textPic4.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textPic4.setForeground(Color.YELLOW);
		
		JLabel pic5 = new JLabel(pointsBlock, SwingConstants.RIGHT);
		
		JLabel namePic5 = new JLabel("Points block", SwingConstants.CENTER);
		namePic5.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		namePic5.setForeground(Color.YELLOW);
		
		JLabel textPic5 = new JLabel("<html>It makes the player earn 10 points and it disappears<br/>when the ball rolls over it, allowing it to keep moving.</html>", SwingConstants.RIGHT);
		textPic5.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textPic5.setForeground(Color.YELLOW);
		
		JLabel pic6 = new JLabel(warpBlock, SwingConstants.RIGHT);
		
		JLabel namePic6 = new JLabel("Warp block", SwingConstants.CENTER);
		namePic6.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		namePic6.setForeground(Color.YELLOW);
		
		JLabel textPic6 = new JLabel("<html><br/>It teleports the ball to another teleport block.<br/> If the cell underlying the destination block is occupied,<br/>the warp will close, stopping any other incoming ball.</html>", SwingConstants.RIGHT);
		textPic6.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		textPic6.setForeground(Color.YELLOW);
		
		JLabel pageCount = new JLabel("2/4", SwingConstants.CENTER);
		pageCount.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		pageCount.setForeground(Color.YELLOW);
		
		JLabel previousPageLabel = new JLabel(previousPage, SwingConstants.RIGHT);
		previousPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previousPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				--page;
				instructionsListener.instructions();
			}
		});
		
		JLabel nextPageLabel = new JLabel(nextPage, SwingConstants.LEFT);
		nextPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				++page;
				instructionsListener.instructions();
			}
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		pan.setOpaque(false);
		pan.add(previousPageLabel);
		pan.add(pageCount);
		pan.add(nextPageLabel);
		
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.gridy = 0;
		cons.gridx = 0;
		cons.gridwidth = 3;
		cons.insets = new Insets(0, 0, 30, 0);
		cons.fill = GridBagConstraints.NONE;
		add(title2, cons);
		
		cons.gridy = 1;
		cons.insets = new Insets(0, 0, 0, 0);
		add(description, cons);

		cons.gridy = 2;
		cons.gridwidth = 1;
		cons.insets = new Insets(40, 0, 0, 0);
		add(pic3, cons);

		cons.gridx = 1;
		cons.insets = new Insets(40, 20, 0, 0);
		add(namePic3, cons);

		cons.gridx = 2;
		cons.insets = new Insets(40, 0, 0, 0);
		add(textPic3, cons);
		
		cons.gridy = 3;
		cons.gridx = 0;
		cons.insets = new Insets(20, 0, 0, 0);
		add(pic4, cons);

		cons.gridx = 1;
		add(namePic4, cons);

		cons.gridx = 2;
		cons.insets = new Insets(20, 0, 0, 170);
		add(textPic4, cons);
		
		cons.gridy = 4;
		cons.gridx = 0;
		cons.insets = new Insets(20, 0, 0, 0);
		add(pic5, cons);

		cons.gridx = 1;
		add(namePic5, cons);

		cons.gridx = 2;
		cons.insets = new Insets(20, 30, 0, 0);
		add(textPic5, cons);
		
		cons.gridy = 5;
		cons.gridx = 0;
		cons.insets = new Insets(20, 0, 10, 0);
		add(pic6, cons);

		cons.gridx = 1;
		add(namePic6, cons);

		cons.gridx = 2;
		cons.insets = new Insets(0, 50, 0, 0);
		add(textPic6, cons);
		
		cons.gridy = 6;
		cons.gridx = 0;
		cons.gridwidth = 3;
		cons.insets = new Insets(50, 0, 0, 0);
		add(pan, cons);
	}
	
	public void page3() {
		
		setLayout(new GridBagLayout());
		
		JLabel title3 = new JLabel("Tactics");
		title3.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		title3.setForeground(Color.YELLOW);
		
		JLabel pic7 = new JLabel(slideInstructions, SwingConstants.CENTER);
		
		JLabel textPic7 = new JLabel("<html>When a ball slides on top of another one,<br/>it won't stop, but will keep sliding.</html>", SwingConstants.CENTER);
		textPic7.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		textPic7.setForeground(Color.YELLOW);
		
		JLabel pic8 = new JLabel(blockedInstructions, SwingConstants.CENTER);
		
		JLabel textPic8 = new JLabel("<html>You can prevent the opponent from playing<br/>a ball by putting one of yours in the<br/>underlying cell as shown in the picture.</html>", SwingConstants.CENTER);
		textPic8.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		textPic8.setForeground(Color.YELLOW);
		
		JLabel pageCount = new JLabel("3/4", SwingConstants.CENTER);
		pageCount.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		pageCount.setForeground(Color.YELLOW);
		
		JLabel previousPageLabel = new JLabel(previousPage, SwingConstants.RIGHT);
		previousPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previousPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				--page;
				instructionsListener.instructions();
			}
		});
		
		JLabel nextPageLabel = new JLabel(nextPage, SwingConstants.LEFT);
		nextPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				++page;
				instructionsListener.instructions();
			}
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		pan.setOpaque(false);
		pan.add(previousPageLabel);
		pan.add(pageCount);
		pan.add(nextPageLabel);
		
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.gridy = 0;
		cons.gridx = 0;
		cons.gridwidth = 3;
		cons.insets = new Insets(0, 0, 20, 0);
		add(title3, cons);
		
		cons.gridy = 1;
		cons.gridx = 0;
		cons.gridwidth = 2;
		add(pic7, cons);
		
		cons.gridx = 2;
		cons.gridwidth = 1;
		cons.insets = new Insets(0, 20, 0, 0);
		add(textPic7, cons);
		
		cons.gridy = 2;
		cons.gridx = 0;
		cons.gridwidth = 2;
		cons.insets = new Insets(50, 0, 0, 0);
		add(pic8, cons);
		
		cons.gridx = 2;
		cons.gridwidth = 1;
		cons.insets = new Insets(0, 27, 0, 0);
		add(textPic8, cons);
		
		cons.gridy = 3;
		cons.gridx = 2;
		cons.insets = new Insets(60, 8, 3, 190);
		add(pan, cons);
	}
	
	public void page4() {
		
		setLayout(new GridBagLayout());
		
		JLabel title4 = new JLabel("Keys", SwingConstants.CENTER);
		title4.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		title4.setForeground(Color.YELLOW);
		
		JLabel pic9 = new JLabel(keysInstructions);
		
		JLabel pageCount = new JLabel("4/4", SwingConstants.CENTER);
		pageCount.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		pageCount.setForeground(Color.YELLOW);
		
		JLabel previousPageLabel = new JLabel(previousPage, SwingConstants.CENTER);
		previousPageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previousPageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				--page;
				instructionsListener.instructions();
			}
		});
		
		JLabel mainMenuLabel = new JLabel(mainMenu, SwingConstants.LEFT);
		mainMenuLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mainMenuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				page = 1;
				backToMainMenuListener.backToMainMenu();
			}
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
		pan.setOpaque(false);
		pan.add(previousPageLabel);
		pan.add(pageCount);
		pan.add(mainMenuLabel);
		
		GridBagConstraints cons = new GridBagConstraints();
		
		cons.gridy = 0;
		cons.gridx = 0;
		cons.insets = new Insets(0, 0, 150, 0);
		add(title4, cons);
		
		cons.gridy = 1;
		add(pic9, cons);
		
		cons.insets = new Insets(30, 7, 0, 0);
		cons.gridy = 2;
		add(pan, cons);
		
	}
	
	public void setInstructionsListener(InstructionsListener instructionsListener) {
		this.instructionsListener = instructionsListener;
	}
	
	public void setBackToMainMenuListener(BackToMainMenuListener backToMainMenuListener) {
		this.backToMainMenuListener = backToMainMenuListener;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int w = getWidth();
		int h = getHeight();
		Color color1 = Color.BLUE;
		Color color2 = Color.BLACK;
		GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
	}
}
