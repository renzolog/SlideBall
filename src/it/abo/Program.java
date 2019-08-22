package it.abo;

import javax.swing.SwingUtilities;

import it.abo.views.BoloBallFrame;

public class Program {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(BoloBallFrame::new);
	}

}
