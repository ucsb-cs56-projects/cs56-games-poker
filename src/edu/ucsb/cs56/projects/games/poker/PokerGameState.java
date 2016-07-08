package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;

/**
 * Class that represents the state of a poker game played 
 * over a socket connection. Used to pass important information
 * between games played on different computers.
 */

public class PokerGameState implements Serializable {

	private static final long serialVersionUID = 1L;
	private int pot, bet, player1Chips, player2Chips, step, turn, winner;
	private String serverMessage;
	private boolean respond = false;
	private boolean justUpdate = false;
	private boolean roundOver = false;
	private Deck deck;
	private Hand player1Hand, player2Hand, flop;
	private Card turnCard, riverCard;
	
	/**
	 * No-arg constructor initializes important instance
	 * variables and deals hands for the server.
	 */
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
	
	/**
	 * Set the bet state.
	 * @param bet
	 */
	public void setBet(int bet){
	    this.bet = bet;
	}
	
	/**
	 * Set the pot state.
	 * @param pot
	 */
	public void setPot(int pot){
		this.pot = pot;
	}
	
	/**
	 * Get the bet state.
	 * @return int
	 */
	public int getBet(){
		return this.bet;
	}
	
	/**
	 * Get the pot state.
	 * @return int
	 */
	public int getPot(){
		return this.pot;
	}
	
	/**
	 * Set the state for player 1's chips.
	 * @param chips
	 */
	public void setPlayer1Chips(int chips){
		this.player1Chips = chips;
	}
	
	/**
	 * Set the state for player 2's chips.
	 * @param chips
	 */
	public void setPlayer2Chips(int chips){
		this.player2Chips = chips;
	}
	
	/**
	 * Get the state for player 1's chips.
	 * @return int
	 */
	public int getPlayer1Chips(){
		return this.player1Chips;
	}
	
	/**
	 * Get the state for player 2's chips
	 * @return int
	 */
	public int getPlayer2Chips(){
		return this.player2Chips;
	}
	
	/**
	 * Get the number of the current player's turn.
	 * A return of 1 means player 1's turn, a return
	 * of 2 means player 2's turn.
	 * @return int
	 */
	public int getTurn() {
		return this.turn;
	}
	
	/**
	 * Get the current step of the game. Numbers correspond
	 * to the steps in order. 0 = blind, 1 = flop, 2 = turn,
	 * 3 = river, 4 = show down
	 * @return int
	 */
	public int getStep() {
		return this.step;
	}	

	/**
	 * Set the boolean respond. Respond indicates that the 
	 * previous turn's player has made a bet, and so the next turn's
	 * player must respond.
	 * @param responding
	 */
	public void setRespond (boolean responding) {
		respond = responding;
	}
	
	/**
	 * Get the boolean respond. Respond indicates that the 
	 * previous turn's player has made a bet, and so the next turn's
	 * player must respond.
	 * @return boolean
	 */
	public boolean getRespond () {
		return respond;
	}
	
	/**
	 * Increment the state of the step for the game.
	 */
	public void nextStep(){
		if(step < 4)
			step++;
	}
	
	/**
	 * Change from player 1 to player 2 turn, or vice versa.
	 */
	public void changeTurn() {
		if(this.turn == 1)
			this.turn = 2;
		else
			this.turn = 1;
	}
	
	/**
	 * Get the flop dealt by the server's state.
	 * @return Hand
	 */
	public Hand getFlop (){
		return flop;
	}
	
	/**
	 * Get player 1's hand dealt by the server's state.
	 * @return Hand
	 */
	public Hand getPlayer1Hand () {
		return player1Hand;
	}
	
	/**
	 * Get player 2's hand dealt by the server's state.
	 * @return Hand
	 */
	public Hand getPlayer2Hand () {
		return player2Hand;
	}
	
	/**
	 * Get the turn card dealt by the state.
	 * @return Card
	 */
	public Card getTurnCard() {
		return turnCard;
	}
	
	/**
	 * Get the river card dealt by the state.
	 * @return Card
	 */
	public Card getRiverCard() {
		return riverCard;
	}
	
	/**
	 * Get the boolean justUpdate. JustUpdate indicates
	 * that the player receiving the state should simply
	 * update instance variables to match with the server's
	 * state and not change turns. Used for passing information
	 * without advancing the game.
	 * @return boolean
	 */
	public boolean getJustUpdate() {
		return justUpdate;
	}
	
	/**
	 * Set the boolean justUpdate. JustUpdate indicates
	 * that the player receiving the state should simply
	 * update instance variables to match with the server's
	 * state and not change turns. Used for passing information
	 * without advancing the game.
	 * @return boolean
	 */
	public void setJustUpdate(boolean update) {
		justUpdate = update;
	}
	
	/**
	 * Get the boolean roundOver. RoundOver indicates that
	 * the a winner for the current round of play has
	 * been decided.
	 * @return boolean
	 */
	public boolean getRoundOver() {
		return roundOver;
	}
	
	/**
	 * Set the boolean roundOver. RoundOver indicates that
	 * the a winner for the current round of play has
	 * been decided.
	 * @param over
	 */
	public void setRoundOver(boolean over) {
		roundOver = over;
	}
	
	/**
	 * Get the winner of the game, represented as an int.
	 * 1 refers to player 1, 2 refers to player 2.
	 * @return int
	 */
	public int getWinner() {
		return winner;
	}
	
	/**
	 * Set the winner of the game, represented as an int.
	 * 1 refers to player 1, 2 refers to player 2.
	 * @param wnr
	 */
	public void setWinner (int wnr) {
		winner = wnr;
	}
	
	/**
	 * Get the message from the server. Messages from the server
	 * will indicate what the opposing player has done.
	 * @return String
	 */
	public String getServerMessage () {
		return serverMessage;
	}
	
	/**
	 * Get the message from the server. Messages from the server
	 * will indicate what the opposing player has done.
	 * @param msg
	 */
	public void setServerMessage (String msg){
		serverMessage = msg;
	}
	
	
}
