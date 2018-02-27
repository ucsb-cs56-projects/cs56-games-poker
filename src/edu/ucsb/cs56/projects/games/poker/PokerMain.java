package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * The Poker Game Main Page (Start-Up) screen
 */
public class PokerMain {

    /**
     * Frame holding buttons representing initial options
     */
    JFrame playButtonFrame;

    /**
     * Button to start a single player game
     */
    JButton singlePlayerButton;

    /**
     * Currently unused
     */
    JButton serverButton, clientButton;

    /**
     * Button to open a chat client
     */
    JButton clientChatButton;

    /**
     * Button to start chat server
     */
    JButton serverChatButton;

    /**
     * Panel to hold buttons representing initial options
     */
    JPanel panel;

    /**
     * Chat server address
     */
    String address;

    /**
     * Runs the application
     */
    public static void main(String[] args) {
    	TestBench.SetupCoverageTracking();
        PokerMain start = new PokerMain();
        start.go();
        TestBench.AnalyzeCoverage();
    }    

    /**
     * Brings up option window to start game.
     */
    public void go() {
        playButtonFrame = new JFrame();
        playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayButtonHandler listener = new PlayButtonHandler();
        panel = new JPanel();
        panel.setBackground(Color.darkGray);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        singlePlayerButton = new JButton("Play Single Player");
        singlePlayerButton.addActionListener(listener);
        panel.add(singlePlayerButton);

        /*
        serverButton = new JButton("Create Poker Server");
        serverButton.addActionListener(listener);
        panel.add(serverButton);

        clientButton = new JButton("Connect to Poker Server");
        clientButton.addActionListener(listener);
        panel.add(clientButton);
        */
        
        serverChatButton = new JButton("Create Poker Chat Server");
        serverChatButton.addActionListener(listener);
        panel.add(serverChatButton);
            
        clientChatButton = new JButton("Connect to Poker Chat Server");
        clientChatButton.addActionListener(listener);
        panel.add(clientChatButton);
        

        playButtonFrame.add(BorderLayout.CENTER, panel);
        playButtonFrame.setSize(300, 200);
        playButtonFrame.setResizable(false);
        playButtonFrame.setLocation(250, 250);
        
        playButtonFrame.setVisible(true);
    }

    /**
     * Handles the buttons in the option window.
     */
    private class PlayButtonHandler implements ActionListener {
        /**
         * listener for play, serverChat, and clientChat buttons
         * @param e action event
         */
        public void actionPerformed(ActionEvent event) {
            Object src = event.getSource();
            if (src == singlePlayerButton) {
                PokerSinglePlayer singlePlayer = new PokerSinglePlayer(500, 500);
                singlePlayer.go();
            }
            /*
            else if (src == serverButton) {
            PokerServer server = new PokerServer();
            server.go();
            } 
            else if (src == clientButton) {
            PokerClient client = new PokerClient();
            address = JOptionPane.showInputDialog(playButtonFrame, "What IP Address are you connecting to?");
            if(address != null){
                client.setAddress(address);	
                try{
                client.go();
                } catch (IOException ex){ex.printStackTrace();
                }
            }
            }*/
            
            else if(src == serverChatButton){
                PokerChatServer server2 = new PokerChatServer();
                server2.go();
            }
            else if(src == clientChatButton){
                address = JOptionPane.showInputDialog(playButtonFrame, "What IP Address are you connecting to?");
                PokerChatClient client2 = new PokerChatClient();
                if(address != null){
                    client2.setAddress(address);
                    client2.go();
                }
            }
            
            playButtonFrame.setVisible(false);
        }
    }	
}
