package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;

/**
 * Class that represents a human poker player
 */
public class User extends Player implements Serializable {

    /**
     *Creates a human poker player with a set hand
     * @param hand the hand you have
     */
    public User(Hand hand){
        super(hand);
	this.type = 1;
    }

    /**
     * Creates a human poker player with a designated number of chips
     * @param chips number of chips
     * @param deck of Cards
     */
    public User(int chips, Deck deck) {
        super(chips, deck);
	this.type = 1;
    }

    /**
     * Instructs the program to wait for user input and update the
     * GUI and game based on the user's input
     */
    public void takeTurn() {
        delegate.userTurn();
    }
}
