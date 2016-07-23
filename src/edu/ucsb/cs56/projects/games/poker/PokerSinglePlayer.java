package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

final class PokerSinglePlayer extends PokerGame {

    Timer timer;
    int timer_value = 1500; // milliseconds
    boolean yourTurnToBet = true;

    /**
     * Constructor to set the player and opponent's initial chips. 
     * This is used when we continue playing the game
     * @param dChips
     * @param pChips
     */
    public PokerSinglePlayer(int pChips, int oChips){
	player.setChips(pChips);
	opponent.setChips(oChips);
    }
    
    /**
     * Starts between you game and AI
     */
    public void go() {
	super.setUp();
	layoutSubViews();
	controlButtons();

	pot = 0;
	if(!gameOver){
	    step = Step.BLIND;
	    turn = Turn.OPPONENT;

	    timer = new Timer(timer_value, e -> opponentAI()); // Java 8 Lambda function Cool!!!
	    timer.setRepeats(false);
	    prompt = "opponent goes first!";
	    message = "opponent is thinking...";
	    timer.restart();
	}
    }

    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */
    public void changeTurn() {
	if (turn == Turn.PLAYER) {
	    if (responding == true) {
		turn = Turn.OPPONENT;
		controlButtons();
		message = "opponent is thinking...";
		updateFrame();		
		timer.restart();
	    } else {
		updateFrame();
		nextStep();
		if (step != Step.SHOWDOWN) {
		    turn = Turn.OPPONENT;
		    controlButtons();
		    prompt = "opponent Turn.";
		    message = "opponent is thinking...";
		    updateFrame();
		    timer.restart();
		}
	    }
	} else if (turn == Turn.OPPONENT) {
	    if (responding == true) {
		turn = Turn.PLAYER;
		controlButtons();
		responding = false;
		prompt = "What will you do?";
		updateFrame();
	    } else {
		prompt = "What will you do?";
		turn = Turn.PLAYER;
		controlButtons();
		updateFrame();
	    }
	}
    }



    /**
     *  Simple AI for the opponent in single player.
     */
    // TODO: Make AI much more complex and less predictable, rewrite existing later
    public void opponentAI() {
	boolean shouldBet = false;
	boolean shouldCall = true;
    }
	

    /**
     * Method overridden to allow for a new single player game to start.
     */    
    public void showWinnerAlert() {
    	if(!gameOver){
    	    String message = "";
    	    oSubPane2.remove(backCardLabel1);
    	    oSubPane2.remove(backCardLabel2);
    	    for(int i=0;i<2;i++){
    		oSubPane2.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
    	    }
    	    updateFrame();
    	    if (winnerType == Winner.PLAYER) {
                System.out.println("player");
                message = "You won! \n\n Next round?";
    	    } else if (winnerType == Winner.OPPONENT) {
    		System.out.println("opponent");
    		message = "opponent won. \n\n Next round?";
    	    } else if (winnerType == Winner.TIE){
                System.out.println("tie");
                message = "Tie \n\n Next round?";
    	    }
    	    
    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);
    	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		
		// First check if players have enough chips
		if(opponent.getChips() < 5) {
		    gameOver("GAME OVER!\n\n opponent has run out of chips!");
		} else if (player.getChips() < 5) {
		    gameOver("GAME OVER!\n\n you have run out of chips!");
		}
		
		// Create new game
		PokerSinglePlayer singlePlayerReplay = new PokerSinglePlayer(player.getChips(),
									     opponent.getChips());
    		singlePlayerReplay.go();
	    } else if (option == JOptionPane.NO_OPTION) {
		gameOver("");
		mainFrame.dispose();
	    } else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }
	
    /**
     * Function that puts up a Game Over Frame that can take us back to the Main Screen
     */
    
    private void gameOver(String label) {
	gameOverFrame = new JFrame();
	gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	gameOverFrame.setBackground(Color.red);

	gameOverMessage = new JPanel();
        gameOverMessage.setBackground(Color.red);

	gameOverButtonPanel = new JPanel();
	gameOverButtonPanel.setBackground(Color.red);

	gameOverLabel = new JLabel(label);
	gameOverButton = new JButton("Back to Main Menu");
	gameOverButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){
		    gameOverFrame.setVisible(false);
		    PokerMain restart = new PokerMain();
		    restart.go();
		}
	    });
	
	
	gameOverMessage.add(gameOverLabel);
	gameOverButtonPanel.add(gameOverButton);

	gameOverFrame.setSize(300, 200);
	gameOverFrame.setResizable(false);
	gameOverFrame.setLocation(250, 250);
	gameOverFrame.getContentPane().add(BorderLayout.NORTH, gameOverMessage);
	gameOverFrame.getContentPane().add(BorderLayout.SOUTH, gameOverButtonPanel);
	gameOverFrame.pack();
	gameOverFrame.setVisible(true);
	mainFrame.dispose();
    }    
}