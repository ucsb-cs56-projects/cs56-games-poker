package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;

public class PokerGameState implements Serializable {

	private int pot, bet, player1Chips, player2Chips, step, turn;
	private boolean respond = false;
	private Deck deck;
	private Hand player1Hand, player2Hand, flop;
	private Card turnCard, riverCard;
	
	public PokerGameState(int pot, int p1Chips, int p2Chips){
		this.pot = pot;
		this.player1Chips = p1Chips;
		this.player2Chips = p2Chips;
		this.step = 0;
		this.turn = 1;
		
		deck = new Deck();
		player1Hand = deck.dealCards();
		player2Hand = deck.dealCards();
		flop = deck.showFlop();
		turnCard = deck.returnCard();
		riverCard = deck.returnCard();
	}
	
	public void setBet(int bet){
		this.bet = bet;
	}
	
	public void setPot(int pot){
		this.pot = pot;
	}
	
	public int getBet(){
		return this.bet;
	}
	
	public int getPot(){
		return this.pot;
	}
	
	public void setPlayer1Chips(int chips){
		this.player1Chips = chips;
	}
	
	public void setPlayer2Chips(int chips){
		this.player2Chips = chips;
	}
	
	public int getPlayer1Chips(){
		return this.player1Chips;
	}
	
	public int getPlayer2Chips(){
		return this.player2Chips;
	}
	
	public int getTurn() {
		return this.turn;
	}
	
	public int getStep() {
		return this.step;
	}	
	
	public boolean mustRespond () {
		return this.respond;
	}
	
	public void requestRespond (boolean responding) {
		respond = responding;
	}
	
	
	public void nextStep(){
		if(step < 4)
			step++;
	}
	
	public void changeTurn() {
		if(this.turn == 0)
			this.turn = 1;
		else
			this.turn = 0;
	}
	
	public Hand getFlop (){
		return flop;
	}
	
	public Hand getPlayer1Hand () {
		return player1Hand;
	}
	
	public Hand getPlayer2Hand () {
		return player2Hand;
	}
	
	public Card getTurnCard() {
		return turnCard;
	}
	
	public Card getRiverCard() {
		return riverCard;
	}
	
}