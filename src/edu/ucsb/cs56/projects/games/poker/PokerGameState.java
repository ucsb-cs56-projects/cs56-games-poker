package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;

public class PokerGameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private int pot, bet, player1Chips, player2Chips, step, turn, winner;
	private boolean respond = false;
	private boolean justUpdate = false;
	private boolean roundOver = false;
	private boolean player1Continue;
	private boolean player2Continue;
	private Deck deck;
	private Hand player1Hand, player2Hand, flop;
	private Card turnCard, riverCard;
	
	public PokerGameState(){
		this.pot = 0;
		this.player1Chips = 500;
		this.player2Chips = 500;
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

	
	public void setRespond (boolean responding) {
		respond = responding;
	}
	
	public boolean getRespond () {
		return respond;
	}
	
	public void nextStep(){
		if(step < 4)
			step++;
	}
	
	public void changeTurn() {
		if(this.turn == 1)
			this.turn = 2;
		else
			this.turn = 1;
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
	
	public boolean getJustUpdate() {
		return justUpdate;
	}
	
	public void setJustUpdate(boolean update) {
		justUpdate = update;
	}
	
	public boolean getRoundOver() {
		return roundOver;
	}
	
	public void setRoundOver(boolean over) {
		roundOver = over;
	}
	
	public int getWinner() {
		return winner;
	}
	
	public void setWinner (int wnr) {
		winner = wnr;
	}
	
	public boolean getPlayer1Continue() {
		return player1Continue;
	}
	public boolean getPlayer2Continue() {
		return player2Continue;
	}
	public void setPlayer1Continue(boolean cont) {
		player1Continue = cont;
	}
	public void setPlayer2Continue(boolean cont) {
		player2Continue = cont;
	}
}