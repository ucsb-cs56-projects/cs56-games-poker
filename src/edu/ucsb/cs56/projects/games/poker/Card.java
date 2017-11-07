package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;

/**
   A class which represents a card from a standard 52 card deck
   @author Joey Dewan
   @version 5/29/12 CS56 S12
*/
public class Card implements Serializable {
    /**
     * The card's numeric value
     */
    private int value;

    /*
     * The card's suit
     */
    private String suit;
	
    /** 2 argument constructor to initialize the value and the suit
	@param value value of the card ranging from 2-14, 2 being 2 and 14 being ace
	@param suit suit of the card(hearts,dimaonds,etc.)
    */
    public Card(int value, String suit){
        this.value=value;
        this.suit=suit;
    }
	
    /**
     * Returns the Value of the Card
     * @return int int value of Card
     */
    public int getValue(){
        return this.value;
    }
	
    /**
     * Returns the suit of the card
     * @return String The Card's Suit
     */
    public String getSuit(){
        return this.suit;
    }
	
    /**
     * Returns the string representation of the card
     * @return String of the Card as valuesuit
    */
    public String toString(){
        String s=""+value+suit;
        return s;
    }
}
