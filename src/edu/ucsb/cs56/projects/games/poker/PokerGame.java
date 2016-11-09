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
public class PokerGame {
    // PokerGame States
    public enum Winner {
        PLAYER, OPPONENT, TIE, NEW_GAME
            };

    public enum Step {
        BLIND, FLOP, TURN, RIVER, SHOWDOWN
            };

    public enum Turn {
        PLAYER, OPPONENT
            };

    // PokerGame GUI
    

    // Properties of Poker Game
    protected Player player;
    protected Player opponent;
    protected Deck deck;
    protected TableCards table;
    protected int pot;

    // Variables maybe used ---- Eventually this should not be in existence
    protected int bet = 0;
    protected String message, prompt;
    protected ImageIcon backCardImage;
    protected Winner winnerType = Winner.NEW_GAME;
    protected Step step;
    protected Turn turn;
    protected Card backCard;

    // Booleans variable
    protected boolean responding = false;
    protected boolean gameOver = false;
    protected boolean youFold = false;
    protected boolean opponentFold = false;
    
    /**
     * No arg constructor that initializes a new deck.
     */
    // FIX THIS OR FIX setUp() METHOD
    public PokerGame() {
	this.deck = new Deck();
	this.player = new Player(500, deck);
	this.opponent = new Player(500, deck);
	this.table = new TableCards(deck);
	pot = 0;
    }

    /**
     * Sets up the player's and opponent's hand.
     */
    public void setUp() {
        player.delegate = this;
        opponent.delegate = this;
	if (player.getChips() > 5 && opponent.getChips() > 5) {
	    player.bet(5);
	    opponent.bet(5);
            pot += 10;
            message = "Ante of 5 chips set.";
        }
	else {
            gameOver = true;
            showWinnerAlert();
        }

        backCard = new Card(100, "B");
        String dir = "Cards/";
        String cardFile = "B.png";
        URL url = getClass().getResource(dir + cardFile);
        backCardImage = new ImageIcon(url);
    }

    /**
     * Returns an ImageIcon by using the URL class in order to make the
     * ImageIcon web compatible
     * @param Card whose image is to be retrieved
     * @return ImageIcon
     */
    public ImageIcon getCardImage(Card c) {
        String dir = "Cards/";
        String cardFile = c.toString() + ".png";
        URL url = getClass().getResource(dir + cardFile);
        return new ImageIcon(url);
    }

    /**
     * PlayerDelegate method handles folding
     */
    // COULD IMPROVE IMPLEMENTATION
    public void fold() {
        if (turn == Turn.PLAYER) {
            winnerType = Winner.OPPONENT;
            opponent.win();
        } else {
            winnerType = Winner.PLAYER;
            player.win();
        }
        collectPot();
        showWinnerAlert();
	// Reset player win flag

        // deck.reShuffle();
        winnerType = Winner.NEW_GAME;
    }

    /**
     * Function that transfers winnings to a player
     */
    protected void collectPot() {
        if (winnerType == Winner.PLAYER) {
	    // Player won
            System.out.println("Player");
            System.out.println(String.format("%d", pot));
            int playerChips = player.getChips();
            playerChips += pot;
            player.setChips(playerChips);
	    player.win();
        } else if (winnerType == Winner.OPPONENT) {
            // Player lost
            System.out.println("OPPONENT");
            System.out.println(String.format("%d", pot));
            int opponentChips = opponent.getChips();
            opponentChips += pot;
            opponent.setChips(opponentChips);
	    opponent.win();
	} else if (winnerType == Winner.TIE) {
            // Tie, split pot, should be extremely rare
            System.out.println("Tie");
            System.out.println(String.format("%d", pot));
            int opponentChips = opponent.getChips();
            opponentChips += (pot / 2);
            opponent.setChips(opponentChips);
            pot -= (pot / 2);
            int playerChips = player.getChips();
            playerChips += pot;
            player.setChips(playerChips);
        } else {
            // New game, should never be called
            pot = 0;
        }
    }
	
    /**
     * Method to determine the winner of the game
     */
    // Possibly fix this
    public void determineWinner() {
	CompareHands comparison = new CompareHands(player, opponent, table);
	int winner = comparison.compareHands();
	if (winner == 1) {
	    player.win();
	    winnerType = Winner.PLAYER;
        } else if (winner == 0) {
            winnerType = Winner.OPPONENT;
        } else {
            winnerType = Winner.TIE;
            opponent.win();
        }
    }

    /**
     * Method to pass the turn from opponent to player, or player to opponent
     * Must be overridden!!!
     */
    public void changeTurn() {
    }

    /**
     * Method used to advance the game from each step to the next.
     * Specifically, in the order Blind-Flop-Turn-River-Showdown
     */
    public void nextStep() {
     /*   if (step == Step.BLIND) { // Most like able to skip/remove this step
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
	}*/
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
    public void showWinnerAlert() {
    }













    // ****************************** GUI STUFF IN OTHER CLASS *****************************

}