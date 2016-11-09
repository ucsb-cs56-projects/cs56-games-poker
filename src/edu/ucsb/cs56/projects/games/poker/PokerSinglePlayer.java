package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

final class PokerSinglePlayer extends PokerGameGui {

    Timer timer;
    int timer_value = 1500; // milliseconds
    boolean yourTurnToBet = true;

    /**
     * Constructor to set the player and opponent's initial chips. 
     * This is used when we continue playing the game
     * @param dChips
     * @param pChips
     */

    public PokerSinglePlayer(){

    }
    
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
	public void nextStep() {
        if (step == Step.BLIND) { // Most like able to skip/remove this step
            step = Step.FLOP;
        } else if (step == Step.FLOP) {
            step = Step.TURN;
        } else if (step == Step.TURN) {
            step = Step.RIVER;
        } else {
            step = Step.SHOWDOWN;
            message = "All bets are in.";
            prompt = "Determine Winner: ";
	    controlButtons();
	}
    }



    /**
     *  Simple AI for the opponent in single player.
     */
    // TODO: Make AI much more complex and less predictable, rewrite existing later
    private void opponentAI() {
	boolean shouldBet = false;
	boolean shouldCall = true;
	int dValue = 0;
	int betAmount = 5 * dValue;
	if (step == Step.BLIND) {
	    if (dValue >= 1) {
		shouldBet = true;
	    }
	} else if (step == Step.FLOP) {
	    if (dValue >= 3) {
		shouldBet = true;
	    }
	    if ((dValue == 0 && bet >= 20)) {
		shouldCall = false;
	    }
	} else if (step == Step.TURN) {
	    if (dValue >= 4) {
		shouldBet = true;
	    }
	    if ((dValue < 2 && bet > 20)) {
		shouldCall = false;
	    }
	} else if (step == Step.RIVER) {
	    if (dValue >= 4) {
		shouldBet = true;
	    }
	    if ((dValue < 2 && bet > 20))
		shouldCall = false;
	}

	if (responding == true) {
	    if (shouldCall) {
		message = "opponent calls.";
		pot += bet;
		opponent.bet(bet);
		bet = 0;
		responding = false;
		nextStep();
		updateFrame();
		timer.restart();
	    } else {
		message = "opponent folds.";
		opponent.foldHand();
	    }
	} else if (shouldBet && step != Step.SHOWDOWN) {
	    if ((opponent.getChips() - betAmount >= 0) && (player.getChips() - betAmount >= 0)) {
		bet = betAmount;
		pot += bet;
		opponent.bet(bet);
		responding = true;
		message = "opponent bets " + bet + " chips.";
		updateFrame();
		changeTurn();
	    } else {
		message = "opponent checks.";
		updateFrame();
		changeTurn();
	    }
	} else if (step != Step.SHOWDOWN) {
	    message = "opponent checks.";
	    updateFrame();
	    changeTurn();
	}
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
		
		// Create new game
		PokerSinglePlayer singlePlayerReplay = new PokerSinglePlayer();

		singlePlayerReplay.go();
	    } else if (option == JOptionPane.NO_OPTION) {
		if(opponent.getChips() < 5) {
		    gameOver("GAME OVER!\n\n opponent has run out of chips!");
		} else if (player.getChips() < 5) {
		    gameOver("GAME OVER!\n\n you have run out of chips!");
		}

		gameOver("");
		
	    } else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }
	
}
