package edu.ucsb.cs56.projects.games.poker;

import java.util.*;

import java.io.Serializable;
import java.math.*;

/**
   Class that represents a 52 card deck
   Every Card obtained from the deck must pass through this
*/
public class Deck implements Serializable{

    private ArrayList<Card> activeDeck;
    private ArrayList<Card> discardPile;
       
    /**
     * No arg constructor that initialized the cards in activeDeck,
     * creates standard deck of 52 Card as activeDeck
     * creates an empty ArrayList for the discardPile
     * the number of cards left in the activeDeck to 52, and creates a new
     arraylist of Cards called discardPile.
    */
    public Deck(){
	activeDeck=new ArrayList(Arrays.asList(new Card(2,"D"), new Card(2,"H"),
					       new Card(2,"C"), new Card(2,"S"),
					       new Card(3,"D"), new Card(3,"H"),
					       new Card(3,"C"), new Card(3,"S"),
					       new Card(4,"D"), new Card(4,"H"),
					       new Card(4,"C"), new Card(4,"S"),
					       new Card(5,"D"), new Card(5,"H"),
					       new Card(5,"C"), new Card(5,"S"),
					       new Card(6,"D"), new Card(6,"H"),
					       new Card(6,"C"), new Card(6,"S"),
					       new Card(7,"D"), new Card(7,"H"),
					       new Card(7,"C"), new Card(7,"S"),
					       new Card(8,"D"), new Card(8,"H"),
					       new Card(8,"C"), new Card(8,"S"),
					       new Card(9,"D"), new Card(9,"H"),
					       new Card(9,"C"), new Card(9,"S"),
					       new Card(10,"D"), new Card(10,"H"),
					       new Card(10,"C"), new Card(10,"S"),
					       new Card(11,"D"), new Card(11,"H"),
					       new Card(11,"C"), new Card(11,"S"),   // JACK
					       new Card(12,"D"), new Card(12,"H"),
					       new Card(12,"C"), new Card(12,"S"),   // QUEEN
					       new Card(13,"D"), new Card(13,"H"),
					       new Card(13,"C"), new Card(13,"S"),   // KING
					       new Card(14,"D"), new Card(14,"H"),
					       new Card(14,"C"), new Card(14,"S"))); // ACE
	discardPile = new ArrayList<Card>();
    }
    
    /**
     * Moves the Card you input from the activeDeck into the discardDeck
     * @param Card
     */
    private void moveToDiscardPile(Card card) {
	discardPile.add(card);
	activeDeck.remove(card);
    }

    /**
     * Returns a Random Card from activeDeck
     * Transfers the Card from activeDeck to the discardPile
     * Only public function! Every card obtained from the deck MUST go through this function
     * @return Card 
     */
    public Card obtainRandomCardFromDeck() {
	int randomNum = (int)(Math.random() * activeDeck.size());
	Card card = activeDeck.get(randomNum);
	moveToDiscardPile(card);
	return card;
    }
    
}
