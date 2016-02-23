package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PokerMain {

    JFrame playButtonFrame;
    JButton singlePlayerButton;
    JPanel panel;

    public static void main (String [] args) {
	PokerMain start = new PokerMain();
	start.go();
	
    }

    public void go() {
	playButtonFrame = new JFrame();
	playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel=new JPanel();
	singlePlayerButton = new JButton("Play Single Player");

	singlePlayerButton.addActionListener(new PlayButtonHandler());
	panel.add(singlePlayerButton, BorderLayout.CENTER);
	panel.setBackground(Color.darkGray);
	
	playButtonFrame.add(BorderLayout.CENTER, panel);
	playButtonFrame.setSize(200,200);
	playButtonFrame.setLocation(250, 250);
	
	playButtonFrame.setVisible(true);
    }

    private class PlayButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Object src = e.getSource();
	    if(src == singlePlayerButton) {
		PokerSinglePlayer singlePlayer = new PokerSinglePlayer();
		singlePlayer.go();
		playButtonFrame.setVisible(false);	
	    }
	}
    }
}
