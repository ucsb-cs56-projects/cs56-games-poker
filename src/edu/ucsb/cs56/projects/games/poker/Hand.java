package edu.ucsb.cs56.projects.games.poker;

import java.util.*;

/**
	Class that represents a hand of 5 Cards.
*/
public class Hand extends ArrayList<Card>{

    private int handValue;

/**
	No arg constructor for Hand
*/
	public Hand(){
		super(1);
	}

/**
	Constructor that sets a hand given 7 Cards as arguments.
	@param a Card a
	@param b Card b
	@param c Card c
	@param d Card d
	@param e Card e
	@param f Card f
	@param g Card g
*/	
	public Hand(Card a, Card b, Card c, Card d, Card e, Card f, Card g){
		super(7);
		this.add(a); this.add(b); this.add(c); this.add(d); this.add(e);
		this.add(f); this.add(g);
	}
	
/**
	Constructor that sets a hand given 5 Cards as arguments.
	@param a Card a
	@param b Card b
	@param c Card c
	@param d Card d
	@param e Card e
*/	
	public Hand(Card a, Card b, Card c, Card d, Card e){
		super(5);
		this.add(a); this.add(b); this.add(c); this.add(d); this.add(e);
	}

/**
	Constructor that sets a 5 Card hand based on the deck passed in.
	@param deck deck of Cards
*/
	public Hand(Deck deck){
		super(7);
		for(Card c:deck.dealCards())
			this.add(c);
		handValue=100;
	}
/**
	Sets the Hand value.
*/	
	public void setHandValue(int handValue){
		this.handValue=handValue;
	}
/**
	Gets the Hand value.
*/
	public int getHandValue(){
		return handValue;
	}


/**
	Discards the hand into the discardPile of the deck.
	@param deck deck of Cards
*/	
	public void discardHand(Deck deck){
		deck.addToDiscardPile(this);
	}
	
/**
	Adds Cards from the deck to the Hand.
	@param deck deck of Cards
*/	
	public void draw(Deck deck){
		for(Card c:deck.dealCards())
			this.add(c);
	}

/**
	Returns the Card with the highest value.
*/
	public Card getHighCard(){
		Card max=this.get(0);
		for(Card c:this){
			if(c.getValue()>max.getValue())
				max=c;
		}
		return max; 
	}

/**
	Returns the int value of the highest Card.
*/
	public int getHighCardValue(){
		int max=0;
		for(Card c:this)
		{
			if(c.getValue()>max)
				max=c.getValue();
		}
		return max;
	}

/**
	Returns an ArrayList of Card int values in a numerical order.
*/
	public ArrayList<Integer> sortHand(){
		ArrayList<Integer> sortedHand=new ArrayList<Integer>();
		for(int i=0;i<this.size();i++)
		{
			sortedHand.add(this.get(i).getValue());
		}
		Collections.sort(sortedHand);
		return sortedHand;
	}
	
/**
	Returns boolean for if the hand is a straight flush
*/	
	public boolean isStraightFlush(){
		int straightFlushCounter=0;
			int spadeCounter=0;
			int cloverCounter=0;
			int heartCounter=0;
			int diamondCounter=0;
			ArrayList<Integer> spades=new ArrayList<Integer>();
			ArrayList<Integer> clovers=new ArrayList<Integer>();
			ArrayList<Integer> diamonds=new ArrayList<Integer>();
			ArrayList<Integer> hearts=new ArrayList<Integer>();
			for(Card c: this){
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
				else{
					heartCounter++;
					hearts.add(c.getValue());
					}
				}
			int i=0;
			if(spadeCounter>=5){
				Collections.sort(spades);
				if(spades.get(i)==(spades.get(i+1)-1) && 
				spades.get(i)==(spades.get(i+2)-2) &&
				spades.get(i)==(spades.get(i+3)-3)	&&
				spades.get(i)==(spades.get(i+4)-4))
					straightFlushCounter=4;
				}
			else if(cloverCounter>=5){
				Collections.sort(clovers);
				if(clovers.get(i)==(clovers.get(i+1)-1) && 
				clovers.get(i)==(clovers.get(i+2)-2) &&
				clovers.get(i)==(clovers.get(i+3)-3)	&&
				clovers.get(i)==(clovers.get(i+4)-4))
					straightFlushCounter=4;
				}
			else if(heartCounter>=5){
				Collections.sort(hearts);
				if(spades.get(i)==(hearts.get(i+1)-1) && 
				hearts.get(i)==(hearts.get(i+2)-2) &&
				hearts.get(i)==(hearts.get(i+3)-3)	&&
				hearts.get(i)==(hearts.get(i+4)-4))
					straightFlushCounter=4;
				}
			else if(diamondCounter>=5){
				Collections.sort(diamonds);
				if(diamonds.get(i)==(diamonds.get(i+1)-1) && 
				diamonds.get(i)==(diamonds.get(i+2)-2) &&
				diamonds.get(i)==(diamonds.get(i+3)-3)	&&
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
	public boolean isFourOfAKind(){
		ArrayList<Integer> sortedHand=this.sortHand();
		int quadCounter=0;
		for(int i=0;i<4;i++)
		{
			if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i+1)
			==sortedHand.get(i+2) && sortedHand.get(i+2)==sortedHand.get(i+3))
			{
				quadCounter=3;
			}
		}
		int i=0;
		if(quadCounter==3)
			return true;
		else
			return false;
	}

/**
	Returns boolean for if the hand is a full house.
*/			
	public boolean isFullHouse(){
		ArrayList<Integer> sortedHand=this.sortHand();
		int doubleCounter=0;
		int tripleCounter=0;
		for(int i=0;i<6;i++)
		{
			if(sortedHand.get(i)==sortedHand.get(i+1))
			{
				if(tripleCounter==1)
				{
					sortedHand.remove(i+1);
					sortedHand.remove(i);
					tripleCounter++;
					break;
				}
				else
				{
					if(i==1)
						tripleCounter=0;
					else
						tripleCounter++;
				}
					
			}
			else
				tripleCounter=0;
				
			
		}
		if(tripleCounter==2)
		{
			sortedHand.trimToSize();
			int size=sortedHand.size();
			for(int i=0;i<(size-1);i++)
			{
				if(sortedHand.get(i)==sortedHand.get(i+1))
				{
					doubleCounter++;
				}
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
	public boolean isFlush(){
	
		int spadeCounter=0;
		int cloverCounter=0;
		int heartCounter=0;
		int diamondCounter=0;
		for(Card c: this){
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
	public boolean isStraight(){
		ArrayList<Integer> sortedHand=this.sortHand();
		int straightCounter=0;
		for(int i=0;i<3;i++)
		{
			if(sortedHand.get(i)==(sortedHand.get(i+1)-1) && 
				sortedHand.get(i)==(sortedHand.get(i+2)-2) &&
				sortedHand.get(i)==(sortedHand.get(i+3)-3)	&&
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
	public boolean isThreeOfAKind(){
		if(this.isFullHouse()){
			return false;
		}
		
		ArrayList<Integer> sortedHand=this.sortHand();
		int tripleCounter=0;
		for(int i=0;i<6;i++)
		{
			if(sortedHand.get(i)==sortedHand.get(i+1))
				tripleCounter++;
			else
			{
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
	public boolean isTwoPair(){
		ArrayList<Integer> sortedHand=new ArrayList<Integer>();
		sortedHand=this.sortHand();
		int pair1Counter=0;
		int pair2Counter=0;
		int pair1Int=100;
		int pair2Int=100;
		for(int i=0;i<6;i++)
		{
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
	public boolean isOnePair(){
		ArrayList<Integer> sortedHand=new ArrayList<Integer>();
		sortedHand=this.sortHand();
		int pairCounter=0;
		for(int i=0;i<6;i++)
		{
			if(sortedHand.get(i)==sortedHand.get(i+1))
				pairCounter++;
		}
		if(pairCounter==1)
			return true;
		else
			return false;
	}
	
/**
	Returns 1 if "this" hand is better than "otherHand" or returns 0
	if the opposite.
	@param otherHand hand to be compared
*/
	public int compareHands(Hand otherHand){
		int player1Value=0;
		int player2Value=0;
		
		if(this.isStraightFlush())
			player1Value=8;
		else if(this.isFourOfAKind())
			player1Value=7;
		else if(this.isFullHouse())
			player1Value=6;
		else if(this.isFlush())
			player1Value=5;
		else if(this.isStraight())
			player1Value=4;
		else if(this.isThreeOfAKind())
			player1Value=3;
		else if(this.isTwoPair())
			player1Value=2;
		else if(this.isOnePair())	
			player1Value=1;
		else
			player1Value=0;
		handValue=player1Value;
		
		if(otherHand.isStraightFlush())
			player2Value=8;
		else if(otherHand.isFourOfAKind())
			player2Value=7;
		else if(otherHand.isFullHouse())
			player2Value=6;
		else if(otherHand.isFlush())
			player2Value=5;
		else if(otherHand.isStraight())
			player2Value=4;
		else if(otherHand.isThreeOfAKind())
			player2Value=3;
		else if(otherHand.isTwoPair())
			player2Value=2;
		else if(otherHand.isOnePair())	
			player2Value=1;
		else
			player2Value=0;
		otherHand.setHandValue(player2Value);
		
		if(player1Value>player2Value)
			return 1;
		else if(player2Value>player1Value)
			return 0;
		else
			return this.sameHand(otherHand);
}

/**
	This method is used if both hands are of the same type(two pairs,
	full house, etc.) Returns 1 if "this" hand is better or 0 if otherwise.
	@param otherHand hand to be compared
*/	
public int sameHand(Hand otherHand){
		ArrayList<Integer> sortedHand=new ArrayList<Integer>();
		sortedHand=this.sortHand();
		ArrayList<Integer> otherSortedHand=new ArrayList<Integer>();
		otherSortedHand=otherHand.sortHand();
		
		int handValue=0;
		int otherHandValue=0;
		int handPairIndex=0;
		int otherHandPairIndex=0;
		
		if(isOnePair())
		{
			
			for(int i=0;i<6;i++)
			{
				if(sortedHand.get(i)==sortedHand.get(i+1)){
					handValue=sortedHand.get(i);
					handPairIndex=i;
					}
				if(otherSortedHand.get(i)==otherSortedHand.get(i+1)){
					otherHandValue=otherSortedHand.get(i);
					otherHandPairIndex=i; 
					}
			}
			
			if(handValue>otherHandValue)
				return 1;
			else if(handValue<otherHandValue)
				return 0;
			else{
				sortedHand.remove(handPairIndex+1);
				sortedHand.remove(handPairIndex);
				otherSortedHand.remove(otherHandPairIndex+1);
				otherSortedHand.remove(otherHandPairIndex);
				int index=4; 
				while(sortedHand.get(index)==otherSortedHand.get(index)){
					if(index==0)
						break;
					else
						index--;
				}
				if(index==0)
					return 2;
				else{
					int winner = (sortedHand.get(index) > otherSortedHand.get(index)) ? 1 : 0;
					return winner;
				}
			}
	
		}
		
		else if(isTwoPair())
		{
			Integer handCard=0; Integer otherHandCard=0;
			int index=1; int index2=1;
			for(int i=0;i<6;i++)
			{
				if(sortedHand.get(i)==sortedHand.get(index))
					{
						if(sortedHand.get(i)>handValue){
							handValue=sortedHand.get(i);
							handCard=handValue;
						}
					}
				index++;
			}
			remove(handCard);
			
			for(int i=0;i<6;i++){
				if(otherSortedHand.get(i)==otherSortedHand.get(index2))
					{
						if(otherSortedHand.get(i)>otherHandValue){
							otherHandValue=otherSortedHand.get(i);
							otherHandCard=otherHandValue;
						}
					}
				index2++;
			}
			remove(otherHandCard);
			if(handValue>otherHandValue)
				return 1;
			else if(handValue<otherHandValue) 
				return 0;
			else 
				return 2;
				
		}
		
		else if(isStraight() || isStraightFlush())
		{
			int index=0;
			int index2=0;
			for(int i=0;i<4;i++){
				if(sortedHand.get(i)==(sortedHand.get(i+1)-1) &&
					sortedHand.get(i)==(sortedHand.get(i+3)-3))
					index=i+3;
			}
			for(int i=0;i<4;i++){
				if(otherSortedHand.get(i)==(otherSortedHand.get(i+1)-1) &&
					otherSortedHand.get(i)==(otherSortedHand.get(i+3)-3))
					index2=i+3;
			}
			if(sortedHand.get(index)>otherSortedHand.get(index2))
				return 1;
			else if(sortedHand.get(index)<otherSortedHand.get(index2))
				return 0;
			else
				return 2;
		}
		else if(isFullHouse() || isThreeOfAKind())
		{
			for(int i=0;i<5;i++)
			{
				if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i)==sortedHand.get(i+2))
					{
						if(sortedHand.get(i)>handValue)
							handValue=sortedHand.get(i);
					}	
				if(otherSortedHand.get(i)==otherSortedHand.get(i+1) && otherSortedHand.get(i)==otherSortedHand.get(i+2))
					{
						if(otherSortedHand.get(i)>otherHandValue)
							otherHandValue=otherSortedHand.get(i);	
					}
			}
			if(handValue>otherHandValue)
				return 1;
			else if(otherHandValue>handValue)
				return 0;
			else
				return 2;
		}
		
		else if(isFourOfAKind())
		{
			for(int i=0;i<4;i++)
			{
				if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i)==sortedHand.get(i+3))
					{
						if(sortedHand.get(i)>handValue)
							handValue=sortedHand.get(i);
					}	
				if(otherSortedHand.get(i)==otherSortedHand.get(i+1) && otherSortedHand.get(i)==otherSortedHand.get(i+3))
					{
						if(otherSortedHand.get(i)>otherHandValue)
							otherHandValue=otherSortedHand.get(i);	
					}
			}
			if(handValue>otherHandValue)
				return 1;
			else if(handValue<otherHandValue)
				return 0;
			else
				return 2;
		}
		else
		{
			return sameHandMethod(otherHand);
		}
	}

/**
	Method used to determine which hand has the higher high card. If high
	cards are the same, continuously checks for next high card until a 
	higher card is found.
	@param otherHand hand to be compared
*/
	public int sameHandMethod(Hand otherHand){
	
		if(this.isEmpty())
			return 0;
		else if(this.getHighCardValue()>otherHand.getHighCardValue())
			return 1;
		else if(this.getHighCardValue()<otherHand.getHighCardValue())//made changes
			return 0;
		else{
			this.remove(this.getHighCard()); 
			otherHand.remove(otherHand.getHighCard());
			return sameHandMethod(otherHand);
		}
}


}
	
	
	


















