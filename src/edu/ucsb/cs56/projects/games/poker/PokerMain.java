package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PokerMain {

    JFrame playButtonFrame;
    JButton singlePlayerButton, serverButton, clientButton;
    JPanel panel;

    public static void main (String [] args) {
	PokerMain start = new PokerMain();
	start.go();
	
    }

    public void go() {
	playButtonFrame = new JFrame();
	playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	PlayButtonLister listener = new PlayButtonListener();
	panel=new JPanel();
	panel.setBackground(Color.darkGray);
	
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	singlePlayerButton = new JButton("Play Single Player");
	singlePlayerButton.addActionListener(listener);
	panel.add(singlePlayerButton);

	serverButton = new JButton("Create Server");
	serverButton.addActionListener(listener);
	panel.add(serverButton);

	clientButton = new JButton("Connect to Server");
	clientButton.addActionListener(listener);
	panel.add(clientButton);
	
	
        playButtonFrame.add(BorderLayout.CENTER, panel);
	playButtonFrame.setSize(200,200);
	playButtonFrame.setLocation(250, 250);
	
	playButtonFrame.setVisible(true);
    }

    private class PlayButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Object src = e.getSource();
	    if(src == singlePlayerButton) {
		PokerSinglePlayer singlePlayer = new PokerSinglePlayer(500,500);
		singlePlayer.go();
		playButtonFrame.setVisible(false);	
	    }
	    else if (src == serverButton){
		PokerServer server = new PokerServer();
		server.go();
	    }
	    else if(src == clientButton) {
		PokerClient client = new PokerClient();
		client.go();
	    }
	}
    }
}
