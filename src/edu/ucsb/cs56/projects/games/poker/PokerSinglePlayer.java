package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

final class PokerSinglePlayer extends PokerGameGui {

    /**
     * Timer for calling turnDecider()
     */
    Timer timer;

    /**
     * The milliseconds for timer
     */
    int timer_value = 2000; // milliseconds

    /**
     * Whether or not it's your turn to bet
     */
    boolean yourTurnToBet = true;


    /**
     * No arg constructor to create instance of PokerSinglePlayer to begin game
     */
    public PokerSinglePlayer(){
        player.setDelegate(this);
        opponent.setDelegate(this);
    }

    /**
     * Constructor to set the player and opponent's initial chips.
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
    public PokerSinglePlayer(int pChips, int oChips){
        player.setChips(pChips);
        opponent.setChips(oChips);
        player.setDelegate(this);
        opponent.setDelegate(this);
    }

    /**
     * Starts game between you and AI
     */
    public void go() {
        super.setUp();
        layoutSubViews(); //sets up all of the buttons and panels and GUI
        controlButtons(); //this function is in PokerGameGui.

        if(!gameOver){
            step = Step.BLIND; //This is the first step in the game.
            turn = Turn.OPPONENT;
            prompt = "opponent goes first!";

            int rng = (int) (Math.random()*2); //generate a random 0 or 1
            if (rng == 1) { //1 = player 1 goes first.
                turn = Turn.PLAYER;
                message = "player goes first!";
                prompt = "what will you do?";

            }
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider() );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider () {
        if (turn == Turn.PLAYER) {
            player.takeTurn();
        }
        else {
            opponent.takeTurn();
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
     * Updates GUI based on the player's decision
     */
    public void userTurn() {
        controlButtons();
        updateFrame();
    }

    /**
     * Method that moves the game to the next phase
     */
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
     * Method overridden to allow for a new single player game to start.
     */
     // BUG: must refactor accordingly to fit GUI
    public void showWinnerAlert() {
    	if(!gameOver){
    	    String message = "";
          oSubPane1.remove(backCardLabel1);
          oSubPane1.remove(backCardLabel2);
    	    oSubPane2.remove(backCardLabel3);
    	    oSubPane2.remove(backCardLabel4);
          oSubPane3.remove(backCardLabel5);
          oSubPane3.remove(backCardLabel6);
          // BUG: chips not added or decreased properly
          oSubPane2.remove(opponent2ChipsLabel);
          // change opponent.getHand
    	    for(int i=0;i<2;i++){
    		      oSubPane1.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
              oSubPane2.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
    	        oSubPane3.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
    	    }
          oSubPane2.add(opponent2ChipsLabel);
    	    updateFrame();
    	    if (!Fold) {
    		message = winningHandMessage();
    	    }
    	    else {
    		message = ("Folded!");
    	    }

    	    if (winnerType == Winner.PLAYER) {
                System.out.println("player");
                message = message + ("\n\nYou win!\n\nNext round?");
    	    } else if (winnerType == Winner.OPPONENT) {
    		System.out.println("opponent");
    		message = message + ("\n\nOpponent wins.\n\nNext round?");
    	    } else if (winnerType == Winner.TIE){
                System.out.println("tie");
                message = message + ("\n\nTie \n\nNext round?");
    	    }

    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);

	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		PokerSinglePlayer singlePlayerReplay;

		// Check if players have enough chips.
		// Create new game.

		if(player.getChips() < 5 || opponent.getChips() < 5){
		    JOptionPane.showMessageDialog(null, "Resetting chips...");
		    singlePlayerReplay = new PokerSinglePlayer();
		    singlePlayerReplay.go();
		}
		else {
		    singlePlayerReplay = new PokerSinglePlayer(player.getChips(),opponent.getChips());
		    singlePlayerReplay.go();
		}

	    } else if (option == JOptionPane.NO_OPTION) {
		if(player.getChips() < 5 || opponent.getChips() < 5) {
		    gameOver("GAME OVER! No chips left!");
		}
		gameOver("GAME OVER! Thanks for playing.\n\n");
	    }

	    else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }

    /**
     * Restarts the timer controlling turnDecider()
     */
    public void restartTimer() {
        timer.restart();
    }
}
