/**
 A class which represents a card from a standard 52 card deck
 @author Joey Dewan
 @version 5/29/12 CS56 S12
  */
public class Card{
	private int value;
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
		Returns the value of the card
	*/
	public int getValue(){
		return this.value;
	}
	
	/**
		Returns the suit of the card
	*/
	public String getSuit(){
		return this.suit;
	}
	
	/**
		Returns the string representation of the card
	*/
	public String toString(){
		String s=""+value+suit;
		return s;
	}
}