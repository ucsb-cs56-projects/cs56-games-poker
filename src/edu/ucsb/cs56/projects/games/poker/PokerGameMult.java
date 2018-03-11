package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class that represents a Texas Holdem' Style Poker Game.
 */
public class PokerGameMult {
    // PokerGame States

    /**
     * enum representing the game winner
     */
     // if OPPONENT, loop through
    public enum Result {
        PLAYER, OPPONENT, TIE, NEW_GAME
            };

    public boolean Fold = false;

    /**
     * enum representing the game step
     */
    public enum Step {
        BLIND, FLOP, TURN, RIVER, SHOWDOWN
            };

    public enum Type {
	PLAYER, COMPUTER
    };

    static int MAXPLAYERS = 4;

    // PokerGame GUI


    // Properties of Poker Game

    /**
     * ArrayList holding the game's current players
     */
    protected ArrayList<Player> players;
    protected Player winner;
    protected int winnerIdx;


    /**
     * The deck used for the game
     */
    protected Deck deck;

    /**
     * The cards currently on the table
     */
    protected TableCards table;

    /**
     * The current pot
     */
    protected int pot;

    // Variables maybe used ---- Eventually this should not be in existence

    /**
     * The current bet
     */
    protected int bet = 0;

    /**
     * Information/instructions displayed to the user
     */
    protected String message, prompt;

    /**
     * Image of the back of the card
     */
    protected ImageIcon backCardImage;

    /**
     * The round winner
     */
    protected Result resultType = Result.NEW_GAME;
    /**
     * Variable representing the game winner
     * winner = 0 : players[0] is the winner
     * winner = 1 : players[1] is the winner
     * etc.
     */


    /**
     * The current game step
     */
    protected Step step;

    /**
     * Which player's turn it is
     */
    protected int turn;
    /**
     * Variable representing which player's turn it is
     * turn = 0 : players[0] turn
     * turn = 1 : players[1] turn
     * etc
     */
     protected int activePlayers;
    //int turn;

    /**
     * The back of a card
     */
    protected Card backCard;

    // Booleans variable

    /**
     * Whether or not one player is responding to the other player's move
     */
    protected boolean responding = false;

    /**
     * The game status
     */
    protected boolean gameOver = false;

    /**
     * Whether or not someone has gone all in
     */
    protected boolean allIn = false;

     /*
     * No arg constructor that initializes a new deck.
     */

    // Single Player
    public PokerGameMult() {
      this.deck = new Deck();
      players = new ArrayList<Player>();
       players.add(new User(500,deck));
	    players.add(new OpponentAI(500, deck));
      for (int i = 0; i < players.size(); i++) {
        players.get(i).setIndex(i);
      }

	     this.table = new TableCards(deck);
	      pot = 0;


    }
    // Multiplayer
    // Current Functionality Supports One User and (MAXPLAYER - 1) AIs
   public PokerGameMult(int player) {
	    this.deck = new Deck();
      players = new ArrayList<Player>();
	    players.add(new User(500, deck));
	    for (int i = 0; i < MAXPLAYERS-1; i++) {
         players.add(new OpponentAI(500, deck));
      }
	     this.table = new TableCards(deck);
	      pot = 0;

        for (int i = 0; i < players.size(); i++) {
          players.get(i).setIndex(i);
        }

    }

    // Getters and setters for various members

    /**
     * Returns the current pot
     * @return the current pot
     */
    public int getPot() {
        return pot;
    }

    /**
     * Adds a bet to the current pot
     * @param pot the pot's new value
     */
    public void addToPot(int bet) {
        this.pot += bet;
    }

    /**
     * Returns the current bet
     * @return the current bet
     */
    public int getCurrentBet() {
        return bet;
    }

    /**
     * Sets the current bet
     * @param bet the number to set the current bet to
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Returns the game's current step
     * @return the current step
     */
     public Step getStep() {
        return step;
    }

    /**
     * Returns if the player is responding to the move
     * @return whether or not the player is responding
     */
    public boolean isResponding() {
        return responding;
    }

    /**
     * Sets responding to true or false
     * @param responding responding's new value
     */
    public void setResponding(boolean responding) {
        this.responding = responding;
    }

    /**
     * Returns whether or not it is all-in in the current game
     * @return the boolean value of allIn
     */
    public boolean isAllIn() {
        return allIn;
    }

    /**
     * Sets the current message being displayed
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /** TODO: Change for multiplayer
     * Sets up the player's and opponent's hand.
     */
    public void setUp() {
      for(Player player:players) {
          player.setDelegate(this);
	      if (player.getChips() >= 5) {
          	player.bet(5);
           	pot += 5;
        }
        else {
                  gameOver = true;
                  showWinnerAlert();
	     }
	   }
	      message = "Ante of 5 chips set.";
        backCard = new Card(100, "B");
        String dir = "Cards/";
        String cardFile = "B.png";
        URL url = getClass().getResource(dir + cardFile);
        backCardImage = new ImageIcon(url);
    }

    /**
     * Returns an ImageIcon by using the URL class in order to make the
     * ImageIcon web compatible
     * @param c whose image is to be retrieved
     * @return ImageIcon
     */
    public ImageIcon getCardImage(Card c) {
        String dir = "Cards/";
        String cardFile = c.toString() + ".png";
        URL url = getClass().getResource(dir + cardFile);
        return new ImageIcon(url);
    }

    /** TODO: Change for multiplayer
     * PlayerDelegate method handles folding
     */
    // COULD IMPROVE IMPLEMENTATION
    protected int lowestTurn = 0;

    public void fold() {
        // temp iterator
        lowestTurn++;
        activePlayers = 0;
        // after a player folds, check for all activePlayers in game
      	for (Player player:players) {
      	    if (player.status == 1) {
          		activePlayers +=1;
          		//player.winStatus = true;
      	    }
      	}
        if (turn != 0)
        {
          System.out.println("Opponent " + (turn) + " folds.");
        }

        System.out.println(activePlayers + " active players remaining.");
        // if activePlayers = 1, they win.
      	if (activePlayers == 1) {
          for (Player player: players)
          {
              if (player.status == 1)
              {
                winnerIdx = players.indexOf(player);
              }
          }
                Fold = true;
              	collectPot();
              	showWinnerAlert();

      	// Reset player win flag

              // deck.reShuffle();
          	    resultType = Result.NEW_GAME;
      	}

      	if (activePlayers > 1) {
      		for (Player player:players) {
      			player.winStatus = false;
          }
          // after a player folds, prompt the next active player to take their turn
          while (players.get(turn).status == 0)
          {
            turn++;
          }
          players.get(turn).takeTurn();
      	}



    }

    /** TODO: Change for multiplayer
     * Function that transfers winnings to a player
     */
    protected void collectPot() {
        for (Player player:players) {
    	    if (player.winStatus == true) {
        		if (player.getType() == 1) {
        	    	    System.out.println("Player " + players.indexOf(player));
        		    System.out.println(String.format("%d", pot));
        		}
        		else if (player.getType() == 0) {
        		     System.out.println("Opponent");
                             System.out.println(String.format("%d", pot));
        		}
        		int playerChips = player.getChips();
        		playerChips += pot;
        		player.setChips(playerChips);
        		player.win();
        		return;
        }
	}
        System.out.println("Tie");
        System.out.println(String.format("%d", pot));
	int chipsGained = pot/MAXPLAYERS;
        for (Player player:players) {
	    int playerChips = player.getChips();
            playerChips += chipsGained;
            player.setChips(playerChips);
        }
    }

    /** TODO: Change for multiplayer
     * Method to determine the winner of the game
     */
    // Possibly fix this

    public void determineWinner() {
        CompareHands comparison = new CompareHands(players, table);
        int win = comparison.compareHands();
        //compareHands() returns player index of winning player
	if(win < 0)
	     return;
	else {
	     Player winner = players.get(win);
      	     players.get(win).win();
      	     players.get(win).winStatus = true;

	}
    }

    public String winningHandMessage(){
    	CompareHands comparison = new CompareHands(players, table);
    	return comparison.compareMessage();
    }
    /**
     * Method to pass the turn from opponent to player, or player to opponent
     * Must be overridden!!!
     */
    public void changeTurn() { }

    /**
     * Method used to advance the game from each step to the next.
     * Specifically, in the order Blind-Flop-Turn-River-Showdown
     */
    public void nextStep() {
	       //Now implemented in PokerSinglePlayer
    }

    /**
     * This state is for the turn
     * Purpose is to be overriden in PokerClient
     */
    protected void checkPassTurnUpdate() {
        changeTurn();
    }

    /**
     * Method shows winner and exits game.
     * Overridden to actually do stuff in children
     */
    public void showWinnerAlert() { }

    /**
     * Method that updates the panels in the GUI based on the current game state
     * Must be overriden (preferrably in the GUI class)
     */
    public void updateFrame() { }

    /**
     * Called when it is a user's turn
     * Must be overridden
     */
    public void userTurn() { }

    /**
     * Restarts the timer
     * Must be overridden
     */
    public void restartTimer() { }

}
