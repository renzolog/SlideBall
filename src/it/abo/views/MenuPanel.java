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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import it.abo.actions.InstructionsListener;
import it.abo.actions.NewGameListener;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	JLabel[] menuLabels;
	NewGameListener newGameListener;
	InstructionsListener instructionsListener;
	
	MenuPanel() {
		
		setLayout(new GridBagLayout());
		setBackground(Color.BLUE);
		
		GridBagConstraints cons = new GridBagConstraints();
		
		menuLabels = new JLabel[4];
		
		menuLabels[0] = new JLabel("SlideBall", JLabel.CENTER);
		menuLabels[0].setFont(new Font("Comic Sans MS", Font.PLAIN, 100));
		menuLabels[0].setForeground(Color.YELLOW);
		
		menuLabels[1] = new JLabel("NEW GAME", JLabel.CENTER);
		menuLabels[1].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		menuLabels[1].setFont(new Font("Comic Sans MS", Font.BOLD, 29));
		menuLabels[1].setForeground(Color.BLACK);
		menuLabels[1].setBackground(new Color(0, 102, 0));
		menuLabels[1].setOpaque(true);
		menuLabels[1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		menuLabels[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				menuLabels[1].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				menuLabels[1].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				newGameListener.newGame();
			}
		});

		menuLabels[2] = new JLabel("INSTRUCTIONS", JLabel.CENTER);
		menuLabels[2].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		menuLabels[2].setFont(new Font("Comic Sans MS", Font.BOLD, 27));
		menuLabels[2].setForeground(Color.BLACK);
		menuLabels[2].setBackground(new Color(204, 0, 0));
		menuLabels[2].setOpaque(true);
		menuLabels[2].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		menuLabels[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				menuLabels[2].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				menuLabels[2].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				instructionsListener.instructions();
			}
		});
		
		menuLabels[3] = new JLabel("EXIT", JLabel.CENTER);
		menuLabels[3].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		menuLabels[3].setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		menuLabels[3].setForeground(Color.BLACK);
		menuLabels[3].setBackground(Color.GRAY);
		menuLabels[3].setOpaque(true);
		menuLabels[3].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		menuLabels[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				menuLabels[3].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				menuLabels[3].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				int opt = JOptionPane.showConfirmDialog(MenuPanel.this, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
				
				switch(opt) {
				case JOptionPane.YES_OPTION :
					System.exit(1);
				case JOptionPane.NO_OPTION :
					break;
				}
			}
		});
		
		/* Title label */
		cons.gridx = 0;  
		cons.gridy = 0;
		cons.ipady = 50;
		cons.ipadx = 100;
		cons.insets = new Insets(10, 0, 0, 0);
		add(menuLabels[0], cons);
		
		/* Empty space */
		cons.gridy = 1;
		cons.insets = new Insets(10, 0, 0, 0);
		add(new JLabel(), cons);
		
		/* New Game label */
		cons.gridy = 2;
		add(menuLabels[1], cons);
		
		/* Instructions label */
		cons.gridy = 3;
		cons.ipadx = 44;
		add(menuLabels[2], cons);
		
		/* Exit label */
		cons.gridy = 4;
		cons.ipadx = 194;
		add(menuLabels[3], cons);
		
	}
	
	/* Makes gradient background */
	
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
        g2d.fillOval(0, 0, w, h);
    }
	
	public void setNewGameListener(NewGameListener newGameListener) {
		this.newGameListener = newGameListener;
	}
	
	public void setInstructionsListener(InstructionsListener instructionsListener) {
		this.instructionsListener = instructionsListener;
	}
}
