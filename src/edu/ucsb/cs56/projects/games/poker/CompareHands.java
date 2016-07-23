package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;

/**
 * Class that Compares Hands between Players
 */
public class CompareHands implements Serializable{
    
    private ArrayList<Card> cardHand1;
    private ArrayList<Card> cardHand2;
    
    private int player1Value = 0;
    private int player2Value = 0;    
    
    public CompareHands(Player player1, Player player2, TableCards table) {
	cardHand1 = new ArrayList<Card>();
	cardHand1.addAll(player1.getHand());
	cardHand1.addAll(table.getFlopCards());
	cardHand1.add(table.getTurnCard());
	cardHand1.add(table.getRiverCard());

	cardHand2 = new ArrayList<Card>();
	cardHand2.addAll(player2.getHand());
	cardHand2.addAll(table.getFlopCards());
	cardHand2.add(table.getTurnCard());
	cardHand2.add(table.getRiverCard());
    }

    /**
     * Returns 1 if "Player 1" hand is better than "Player 2" hand
     * Returns 0 if the opposite.
     * Returns 2 if exact tie
     * @param otherHand hand to be compared
     */
    public int compareHands(){
	player1Value = calculateValue(cardHand1);
	player2Value = calculateValue(cardHand2);
	
	if(player1Value>player2Value)
	    return 1;
	else if(player1Value<player2Value)
	    return 0;
	else {
	    int yourHandValue = sameHandUpdated(cardHand1, 1);
	    int otherHandValue = sameHandUpdated(cardHand2, 1);
	    if(yourHandValue>otherHandValue)
		return 1;
	    else if (yourHandValue<otherHandValue)
		return 0;
	    else {
		Card yourmaxCard = cardHand1.get(0);
		Card othermaxCard = cardHand2.get(0);
		if (yourmaxCard.getValue()<cardHand1.get(1).getValue())
		    yourmaxCard = cardHand1.get(1);
		if (othermaxCard.getValue()<cardHand2.get(1).getValue())
		    othermaxCard = cardHand2.get(1);

		if (yourmaxCard.getValue()>othermaxCard.getValue())
		    return 1;
		else if (yourmaxCard.getValue()<othermaxCard.getValue())
		    return 0;
		
		else {
		    yourHandValue = sameHandUpdated(cardHand1, 2);
		    otherHandValue = sameHandUpdated(cardHand2, 2);
		    if(yourHandValue>otherHandValue)
			return 1;
		    else if (yourHandValue<otherHandValue)
			return 0;
		    else {
			Card yourminCard = cardHand1.get(0);
			Card otherminCard = cardHand2.get(0);
			if (yourminCard.getValue()>cardHand1.get(1).getValue())
			    yourminCard = cardHand1.get(1);
			if (otherminCard.getValue()>cardHand2.get(1).getValue())
			    otherminCard = cardHand2.get(1);
			
			if (yourminCard.getValue()>otherminCard.getValue())
			    return 1;
			else if (yourminCard.getValue()<otherminCard.getValue())
			    return 0;
			else
			    return 2;// Only arrives here if cards are exactly the same		
		    }
		}
	    }		
	}
    }

    /**
     * This method is used if both hands are the same type
     * @param Hand: hand is either this or the otherhand
     * @param recursion: Either 1 if called 1st, 2 if called 2nd
     * func won't work as intended if given a different number
     */
    private int sameHandUpdated(ArrayList<Card> c, int recursion) {
	ArrayList<Card> cards_tmp = new ArrayList<Card>();
	Card yourCard = c.get(0);
	if (recursion==1) {
	    if (c.get(1).getValue()>yourCard.getValue())
	     	yourCard = c.get(1);
	    cards_tmp.add(yourCard);
	    for (int i=2; i<c.size(); i++)
		cards_tmp.add(c.get(i));
	    return calculateValue(cards_tmp);
	}
	if (recursion==2) {
	    if (c.get(1).getValue()<yourCard.getValue())
		yourCard = c.get(1);
	    cards_tmp.add(yourCard);
	    for(int i=2; i<c.size(); i++)
		cards_tmp.add(c.get(i));
	    return calculateValue(cards_tmp);
	}
	return -1; // Should only pass in 1 or 2 in parameter
    }
    
    /**
       Method used to determine which hand has the higher high card. If high
       cards are the same, continuously checks for next high card until a 
       higher card is found.
       @param otherHand hand to be compared
    */
    /*
    public int sameHandMethod(Hand otherHand){	
	if(cardHand1.isEmpty())
	    return 2;
	else if(cardHand1.getHighCardValue()>otherHand.getHighCardValue())
	    return 1;
	else if(cardHand1.getHighCardValue()<otherHand.getHighCardValue())
	    return 0;
	else{
	    cardHand1.remove(this.getHighCard()); 
	    otherHand.remove(otherHand.getHighCard());
	    return sameHandMethod(otherHand);
	}
    }
    */

    public int calculateValue(ArrayList<Card> player) {
	if(isStraightFlush(player))
	    return 8;
	else if(isFourOfAKind(player))
	    return 7;
	else if(isFullHouse(player))
	    return 6;
	else if(isFlush(player))
	    return 5;
	else if(isStraight(player))
	    return 4;
	else if(isThreeOfAKind(player))
	    return 3;
	else if(isTwoPair(player))
	    return 2;
	else if(isOnePair(player))
	    return 1;
	else
	    return 0;
    }


    private ArrayList<Integer> sortHand(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	for(int i=0;i<player.size();i++) {
	    sortedHand.add(player.get(i).getValue());
	}
	Collections.sort(sortedHand);
	return sortedHand;
    }

    /**
       Returns boolean for if the hand is a straight flush
    */
    private boolean isStraightFlush(ArrayList<Card> player){
	int straightFlushCounter=0;
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	ArrayList<Integer> spades=new ArrayList<Integer>();
	ArrayList<Integer> clovers=new ArrayList<Integer>();
	ArrayList<Integer> diamonds=new ArrayList<Integer>();
	ArrayList<Integer> hearts=new ArrayList<Integer>();
	for(Card c: player){
	    if(c.getSuit()=="S"){
		spadeCounter++;
		spades.add(c.getValue());
	    }
	    else if(c.getSuit()=="C"){
		cloverCounter++;
		clovers.add(c.getValue());
	    }
	    else if(c.getSuit()=="D"){
		diamondCounter++;
		diamonds.add(c.getValue());
	    }
	    else if(c.getSuit()=="H"){
		heartCounter++;
		hearts.add(c.getValue());
	    }
	}
	int i=0;
	if(spadeCounter>=5){
	    Collections.sort(spades);
	    if(spades.get(i)==(spades.get(i+1)-1) && 
	       spades.get(i)==(spades.get(i+2)-2) &&
	       spades.get(i)==(spades.get(i+3)-3) &&
	       spades.get(i)==(spades.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(cloverCounter>=5){
	    Collections.sort(clovers);
	    if(clovers.get(i)==(clovers.get(i+1)-1) && 
	       clovers.get(i)==(clovers.get(i+2)-2) &&
	       clovers.get(i)==(clovers.get(i+3)-3) &&
	       clovers.get(i)==(clovers.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(heartCounter>=5){
	    Collections.sort(hearts);
	    if(spades.get(i)==(hearts.get(i+1)-1) && 
	       hearts.get(i)==(hearts.get(i+2)-2) &&
	       hearts.get(i)==(hearts.get(i+3)-3) &&
	       hearts.get(i)==(hearts.get(i+4)-4))
		straightFlushCounter=4;
	}
	else if(diamondCounter>=5){
	    Collections.sort(diamonds);
	    if(diamonds.get(i)==(diamonds.get(i+1)-1) && 
	       diamonds.get(i)==(diamonds.get(i+2)-2) &&
	       diamonds.get(i)==(diamonds.get(i+3)-3) &&
	       diamonds.get(i)==(diamonds.get(i+4)-4))
		straightFlushCounter=4;
	}
	else
	    return false;
	    
	if(straightFlushCounter==4)
	    return true;
	else
	    return false;
    }

    /**
       Returns boolean for if the hand has a four of a kind.
    */
    private boolean isFourOfAKind(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int quadCounter=0;
	for(int i=0;i<player.size()-3;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i+1)
	       ==sortedHand.get(i+2) && sortedHand.get(i+2)==sortedHand.get(i+3)) {
		quadCounter=3;
	    }
	}
	if(quadCounter==3)
	    return true;
	else
	    return false;
    }

    /**
       Returns boolean for if the hand is a full house.
    */
    private boolean isFullHouse(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int doubleCounter=0;
	int tripleCounter=0;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1)) {
		if(tripleCounter==1) {
		    sortedHand.remove(i+1);
		    sortedHand.remove(i);
		    tripleCounter++;
		    break;
		}
		else {
		    if(i==1)
			tripleCounter=0;
		    else
			tripleCounter++;
		}
	    }
	    else
		tripleCounter=0;
	}
	
	if(tripleCounter==2) {
	    sortedHand.trimToSize();
	    int size=sortedHand.size();
	    for(int i=0;i<(size-1);i++) {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    doubleCounter++;
	    }
	}
	else
	    return false;
	if(doubleCounter>=1)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand is a flush.
    */
    private boolean isFlush(ArrayList<Card> player) {
	int spadeCounter=0;
	int cloverCounter=0;
	int heartCounter=0;
	int diamondCounter=0;
	for(Card c: player){
	    if(c.getSuit()=="S")
		spadeCounter++;
	    else if(c.getSuit()=="C")
		cloverCounter++;
	    else if(c.getSuit()=="D")
		diamondCounter++;
	    else
		heartCounter++;
	}
	if(spadeCounter>=5 || cloverCounter>=5 || diamondCounter>=5 
	   || heartCounter>=5)
	    return true;
	else
	    return false;
    }
    
    /**
       Returns boolean for if the hand is a straight.
    */
    private boolean isStraight(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=sortHand(player);
	int straightCounter=0;
	for(int i=0;i<player.size()-4;i++) {
		if(sortedHand.get(i)==(sortedHand.get(i+1)-1) && 
		   sortedHand.get(i)==(sortedHand.get(i+2)-2) &&
		   sortedHand.get(i)==(sortedHand.get(i+3)-3) &&
		   sortedHand.get(i)==(sortedHand.get(i+4)-4))
		    {
			straightCounter=4;
		    }
	}
	return(straightCounter==4);
    }
    
    /**
       Returns boolean for if the hand has three of a kind.
    */
    private boolean isThreeOfAKind(ArrayList<Card> player) {
	if(isFullHouse(player)){
	    return false;
	}
	
	ArrayList<Integer> sortedHand= sortHand(player);
	int tripleCounter=0;
	for(int i=0;i<player.size()-1;i++) {
		if(sortedHand.get(i)==sortedHand.get(i+1))
		    tripleCounter++;
		else {
		    if(tripleCounter==1)
			tripleCounter=0;
		}
	}
	if(tripleCounter==2)
	    return true;
	else
	    return false;		
    }
    
    /**
       Returns boolean for if the hand has two pairs.
    */
    private boolean isTwoPair(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=sortHand(player);
	int pair1Counter=0;
	int pair2Counter=0;
	int pair1Int=100;
	int pair2Int=100;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1)){
		if(sortedHand.get(i)==pair1Int || sortedHand.get(i)==pair2Int){
		    return false;
		}
		else if(pair1Counter==1){
		    pair2Counter++;
		    pair2Int=sortedHand.get(i);
		}
		else{
		    pair1Counter++;
		    pair1Int=sortedHand.get(i);
		}
	    }
	}
	return(pair1Counter==1 && pair2Counter>=1);
    }
    
    /**
       Returns boolean for if the hand has only one pair.
    */
    private boolean isOnePair(ArrayList<Card> player) {
	ArrayList<Integer> sortedHand=new ArrayList<Integer>();
	sortedHand=sortHand(player);
	int pairCounter=0;
	for(int i=0;i<player.size()-1;i++) {
	    if(sortedHand.get(i)==sortedHand.get(i+1))
		pairCounter++;
	}
	if(pairCounter==1)
	    return true;
	else
	    return false;
    }

}