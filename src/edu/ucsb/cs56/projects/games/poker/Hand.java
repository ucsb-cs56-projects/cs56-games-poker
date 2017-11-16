package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;


/**
 * Class that represents a hand of 2 Cards.
 * The ONLY object that should have a Hand object is Player
 */
public class Hand extends ArrayList<Card> implements Serializable{

    private int handValue;
    /** 
     * Constructor to set specific hands 
     * @param card1 first set of cards
     * @param card2 second set of cards
     */
    public Hand(Card card1, Card card2){
		this.add(card1);
		this.add(card2);
	}
    /**
     * Constructor creates a Hand of Cards by calling the Deck to give Hand these cards
     * Creates a 2 Card Hand for the player
     * @param deck a deck of cards
     */
    public Hand(Deck deck) {
	handValue = 0;
	for (int i=0; i<2; i++)
	    this.add(deck.obtainRandomCardFromDeck());
    }
    
    /**
     * Sets the Hand value.
     * NOTE: we may never need this for now, good to have it here for accessibility
     * @param handValue int that represents the value of the hand
     */	
    public void setHandValue(int handValue){
	this.handValue=handValue;
    }
    
    /**
     * Gets the Hand value.
     * NOTE: we may never need this for now, good to have it here for accessibility
     * @return int
     */
    public int getHandValue(){
	return handValue;
    }
    
    /**
     * Returns the Card in your Hand with the higher value.
     * @return Card
     */
    public Card getHighCard(){
	Card max=this.get(0);
	for(Card c:this) {
	    if(c.getValue()>max.getValue())
		max=c;
	}
	return max;
    }
    
    /**
     *  Returns the int value of the higher Card.
     * @return int
     */
    public int getHighCardValue(){
	int max=0;
	for(Card c:this) {
	    if(c.getValue()>max)
		max=c.getValue();
	}
	return max;
    }

    /**
     * Returns the Card in your Hand with the lower value.
     * @return Card
     */
    public Card getLowCard(){
	Card min=this.get(0);
	for(Card c:this) {
	    if(c.getValue()<min.getValue())
		min=c;
	}
	return min;
    }
    
    /**
     *  Returns the int value of the lower Card.
     * @return int 
     */
    public int getLowCardValue(){
	int min=0;
	for(Card c:this) {
	    if(c.getValue()<min)
		min=c.getValue();
	}
	return min;
    }   
	
}
