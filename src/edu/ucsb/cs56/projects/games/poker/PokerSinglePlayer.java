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
	super();
	for(Player player:players)
	    player.setDelegate(this);
/*
        players.get(0).setDelegate(this);
        players.get(1).setDelegate(this);
*/
    }

    /**
     * Constructor to set the player and opponent's initial chips.
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
     public PokerSinglePlayer(int pChips, int oChips){
	super();
	for(Player player:players) {
        player.setChips(pChips);
        player.setDelegate(this);
       }
       Player opponent = players.get(1);
       opponent.setChips(oChips);
     }

    public PokerSinglePlayer(int pChips, int o1Chips, int o2Chips, int o3Chips){
        super();
	players.get(0).setChips(pChips);
        players.get(1).setChips(o1Chips);
	players.get(2).setChips(o2Chips);
	players.get(3).setChips(o3Chips);
        for(Player player:players)
	    player.setDelegate(this);
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
	   /* int currentPlayers = players.size();
            int rng = (int) (Math.random()*currentPlayers); //generate random num 0-4 
	    Player first = players.get(rng);
	    first.turn = true;
	    turn = rng;
	    rng++;
            if (turn == 0) { //1 = player 1 goes first.
		message = "player 1 goes first!";
                prompt = "what will you do?";
            }
	    else 
		prompt = "player " + rng + " goes first!";
		*/
	    players.get(0).turn = true;
	    turn = 0;
	    message = "player 1 goes first!";
	    prompt = "what will you do?";
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider() );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
	    players.get(0).turn = false;
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider () {
	players.get(turn).takeTurn();
/*
        if (turn == 0) {
            players.get(0).takeTurn();
        }
        else {
            players.get(1).takeTurn();
        }
*/
    }


    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */
    public void changeTurn() {/*
	if (turn < 3) 
	   turn++;
	else
	   turn = 0; */
	Player current = players.get(turn);
        if (turn < players.size()-1) {
           if (responding == true) {
		    /*
        if (turn == 0) {
            if (responding == true) {
                turn = 1;
*/
                turn++;
		controlButtons();
                message = "computer " + (turn) + " is thinking...";
                updateFrame();		
                timer.restart();
                } else {
                    updateFrame();
		    /*if (turn == (players.size() - 1)) {
			nextStep();
		    } */
                    if (step != Step.SHOWDOWN) {
			turn++;
                        controlButtons();
                        prompt = "computer " + (turn) + "'s Turn.";
                        message = "computer " + (turn) + " is thinking...";
                        updateFrame();
                        timer.restart();
                    }
                }
        } else {
            if (responding == true) {
		turn = 0;
		controlButtons();
		responding = false;
		prompt = "Player 1's turn. What will you do?";
		updateFrame();
            } else {
		    updateFrame();
		    nextStep();
		    prompt = "Player 1's turn. What will you do?";
    		    turn = 0;
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
    		      oSubPane1.add(new JLabel(getCardImage((players.get(1).getHand()).get(i))));
              oSubPane2.add(new JLabel(getCardImage((players.get(2).getHand()).get(i))));
    	        oSubPane3.add(new JLabel(getCardImage((players.get(3).getHand()).get(i))));
    	    }
          oSubPane2.add(opponent2ChipsLabel);
    	    updateFrame();

	    message = winningHandMessage();
	    
	    int winner = 0;

	    for (Player player:players) {
		if (player.winStatus == true) {
		    int index = players.indexOf(player);
		    index++;
		    if (player.type == 1) {
			System.out.println("player");
			message = message + ("\n\nPlayer " + index + " wins!\n\nNext round?");
		    } else {
			index++;
			System.out.println("computer");
			message = message + ("\n\nComputer " + index + " wins.\n\nNext round?");
		    }
		    winner++;
		}
	    }
	    if (winner == 0){
	/*
    	    if (!Fold) {
    		message = winningHandMessage();
    	    }
    	    else {
    		message = ("Folded!");
    	    }

    	    if (winnerIdx == 0) {
                System.out.println("player");
                message = message + ("\n\nYou win!\n\nNext round?");
    	    } else if (winnerIdx != 0) {
    		System.out.println("opponent");
    		message = message + ("\n\nOpponent wins.\n\nNext round?");
      } else if (winnerIdx < 0){
*/
                System.out.println("tie");
                message = message + ("\n\nTie \n\nNext round?");
    	    }

    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);

	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		PokerSinglePlayer singlePlayerReplay;
		int Continue = 0;

		// Check if players have enough chips.
		// Create new game.
		for (Player player:players) {
		    if (player.getChips() < 5){
		    	JOptionPane.showMessageDialog(null, "Resetting chips...");
		    	singlePlayerReplay = new PokerSinglePlayer();
		    	singlePlayerReplay.go();
			Continue++;
		    }
		}
		if (Continue == 0) {
		    singlePlayerReplay = new PokerSinglePlayer(players.get(0).getChips(),players.get(1).getChips(),players.get(2).getChips(),players.get(3).getChips());
		    singlePlayerReplay.go();
		}
	    }
	    else if (option == JOptionPane.NO_OPTION) {
		for (Player player:players) {
		    if(player.getChips() < 5) {
		         gameOver("GAME OVER! No chips left!");
		   }
/*
		if(players.get(0).getChips() < 5 || players.get(1).getChips() < 5){
		    JOptionPane.showMessageDialog(null, "Resetting chips...");
		    singlePlayerReplay = new PokerSinglePlayer();
		    singlePlayerReplay.go();
		}
		else {
		    singlePlayerReplay = new PokerSinglePlayer(players.get(0).getChips(),players.get(1).getChips());
		    singlePlayerReplay.go();
		}

	    } else if (option == JOptionPane.NO_OPTION) {
		if(players.get(0).getChips() < 5 || players.get(0).getChips() < 5) {
		    gameOver("GAME OVER! No chips left!");
*/
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
