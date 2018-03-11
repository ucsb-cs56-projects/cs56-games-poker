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
    	for(Player player:players) {
    	    player.setDelegate(this);
        }

    }

    /**
     * Constructor to set the player and opponent's initial chips.
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
     // Change to players and equal chips?
     public PokerSinglePlayer(int pChips, int oChips){
	super(4);
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
    	if (players.get(turn).status == 0) {
    		changeTurn();
      }
    	else {
    		players.get(turn).takeTurn();
      }
    }


    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */

    public void changeTurn() {
      Player current = players.get(turn);
      if (turn < players.size() - 1) {
            if (responding == true) {
                turn++;
                controlButtons();
                message = "opponent " + (turn) + " is thinking...";
                updateFrame();
                timer.restart();
                }
                else {
                  updateFrame();
                  nextStep();
                  if (step != Step.SHOWDOWN) {
                      System.out.println(turn + " HERE 3" );
                      turn++;
                      controlButtons();
                      System.out.println(turn + " HERE 4" );
                      prompt = "opponent " + (turn) + "'s Turn.";
                      //message = "opponent " + (turn) + " is thinking...";
                      updateFrame();
                      timer.restart();
                  }
                }
        }
        else {
            if (responding == true) {
                turn = lowestTurn;
                controlButtons();
                responding = false;
                if (lowestTurn == 0)
                {
                  prompt = "Player's turn. What will you do?";
                }
                else
                {
                    prompt = "opponent " + (turn) + "'s Turn.";

                }

                updateFrame();
            } else {
                //updateFrame();
                //nextStep();

                if (lowestTurn == 0)
                {
                  prompt = "Player's turn. What will you do?";
                }
                turn = lowestTurn;
                if (lowestTurn != 0)
                {
                    prompt = "opponent " + (turn) + "'s Turn.";
                }

                controlButtons();
                updateFrame();
                timer.restart();

            }
        }
    }
    public void userTurn()
    {
      controlButtons();
      updateFrame();
    }

    /**
     * Restarts the timer controlling turnDecider()
     */
    public void restartTimer() {
        timer.restart();
    }
}
