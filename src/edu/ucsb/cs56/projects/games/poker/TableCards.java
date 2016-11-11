package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;

/**
 * Class that represents the 5 Cards on the Table
 */
public class TableCards implements Serializable {

    private ArrayList<Card> flopCards;
    private Card turnCard;
    private Card riverCard;
    
	/** Constructor that creates a pre-defined table */
	public TableCards(Card flop1, Card flop2, Card flop3, 
		              Card turn, Card river){
		flopCards = new ArrayList<Card>();
		flopCards.add(flop1);
		flopCards.add(flop2);
		flopCards.add(flop3);
		turnCard = turn;
		riverCard = river;
		
	}
    /**
     * Constructor that creates the Table Cards by calling Deck them to give us
     * Creates a 5 Card Table that represents the cards on the table
     * @param Deck
     */
    public TableCards(Deck deck) {
	flopCards = new ArrayList<Card>();
	for (int i=0; i<3; i++)
	    flopCards.add(deck.obtainRandomCardFromDeck());
	turnCard = deck.obtainRandomCardFromDeck();
	riverCard = deck.obtainRandomCardFromDeck();
    }
	

    /**
     * Gives us the first 3 Cards in the Table Cards which represent the Flop state
     * Returns an ArrayList<Card>
     */
    public ArrayList<Card> getFlopCards() {
	return flopCards;
    }

    /**
     * Gives us third Card in the Table Cards which represent the Turn state
     * Returns a Card object
     */
    public Card getTurnCard() {
	return turnCard;
    }

    /**
     * Gives us third Card in the Table Cards which represent the River state
     * Returns a Card object
     */
    public Card getRiverCard() {
	return riverCard;
    }
}