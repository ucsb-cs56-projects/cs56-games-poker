package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import java.util.*;
import static java.lang.Math.*;


/**
	Class that represents a Texas Holdem' Style Poker Game.
*/

public class PokerGame implements PlayerDelegate {
    public enum Winner { PLAYER, DEALER, TIE, NEW_GAME };
    public enum Step {BLIND, FLOP, TURN, RIVER, SHOWDOWN};
    public enum Turn {PLAYER, DEALER};
    private JPanel panel;
    private JFrame mainFrame, mainFrame2, playButtonFrame;
    private JTextField betTextField;
    private JButton playButton, playAgainButton, foldButton, betButton, checkButton,
	showdownButton, passTurnButton, callButton;
    private JLabel playerWinsLabel, dealerWinsLabel, playerChipsLabel, dealerChipsLabel, potLabel;
    private JLabel gameMessage, playerPrompt, backCardLabel1, backCardLabel2;
    private JPanel dealerPanel, playerPanel, centerPanel, revealPanel, messagePanel;
    private JPanel dSubPane1, dSubPane2, dSubPane3, pSubPane1, pSubPane2, pSubPane3, optionArea;
    private JPanel flopPane, turnPane, riverPane, betPane, respondPane;
    private String message, prompt;
    private Hand dealerHand, playerHand, flop;
    private Deck deck;
    private Card turnCard, riverCard, backCard;
    private ImageIcon backCardImage;
    private Winner winnerType = Winner.NEW_GAME;
    private Step step;
    private Turn turn;
    private int bet = 0;
    public boolean responding = false;
    private static int pot = 0;
    private static Player player = new Player(500);
    private static Player dealer = new Player(500);


    /**
       No arg constructor that initializes a new deck.
    */
    public PokerGame(){
	//deck=new Deck();
    }
    
    /**
       Main method of PokerGame class.
    */
    public static void main(String[] args)
    {
	PokerGame gui=new PokerGame();
	gui.go();
    }
    
    /**
       Creates a window with a Play button.
    */
    public void go(){
	playButtonFrame = new JFrame();
	playButtonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel=new JPanel();
		
	playButton = new JButton("Click to Play");
	playButton.addActionListener(new ButtonHandler());
	panel.add(playButton, BorderLayout.CENTER);
	panel.setBackground(Color.darkGray);
	
	playButtonFrame.add(BorderLayout.CENTER, panel);
	playButtonFrame.setSize(200,200);
	
	playButtonFrame.setVisible(true);
    }
    
    /**
       Sets up the player's and dealer's hand.
    */	
    public void playerSetUp(){
        player.delegate = this;
        dealer.delegate = this;
	deck = new Deck();
	player.setHand(deck.dealCards());
	dealer.setHand(deck.dealCards());
	flop = deck.showFlop();
	turnCard = deck.returnCard();
	riverCard = deck.returnCard();
	for(Card c: flop){
	    player.addCardToHand(c);
	    dealer.addCardToHand(c);
	}
	player.addCardToHand(turnCard);
	player.addCardToHand(riverCard);
	dealer.addCardToHand(turnCard);
	dealer.addCardToHand(riverCard);
	backCard = new Card(100,"B");	
	String dir="Cards/";
	String cardFile="B.png";
	URL url=getClass().getResource(dir+cardFile);
	backCardImage=new ImageIcon(url);
	bet = 10;
	player.setChips(player.getChips() - 5);
	dealer.setChips(dealer.getChips() -5);
	pot += bet;
	message = "Ante of 5 chips set.";
	
    }
    
    /**
       Returns an ImageIcon by using the URL class in order to make the 
       ImageIcon web compatible.
       @param c card whose image is to be retrieved.
    */
    public ImageIcon getCardImage(Card c){
	String dir="Cards/";
	String cardFile=c.toString()+".png";
	URL url=getClass().getResource(dir+cardFile);
	return new ImageIcon(url);
	
    }
    
    /**
     *  PlayerDelegate
     */
    
    public void fold() {
	if(turn == Turn.PLAYER){
	    winnerType = Winner.DEALER;
	    dealer.win();
	}
	else{
	    winnerType = Winner.PLAYER;
	    player.win();
	}
        collectPot();
        showWinnerAlert();
        // Reset player win flag
        deck.reShuffle();
        winnerType = Winner.NEW_GAME;
    }

    
    /**
       Method to transfer winnings
    */
    
    public void collectPot()
    {
	
	if (winnerType == Winner.PLAYER) {
	    // Player won
          System.out.println("Player");
          System.out.println(String.format("%d", pot));
	  int playerChips = player.getChips();
	  playerChips += pot;
	  player.setChips(playerChips);
	} else if (winnerType == Winner.DEALER) {
	    // Player lost
	    System.out.println("DEALER");
	    System.out.println(String.format("%d", pot));
	    int dealerChips = dealer.getChips();
	    dealerChips += pot;
	    dealer.setChips(dealerChips);
	} else if (winnerType == Winner.TIE){
	    // Tie, split pot
	    System.out.println("Tie");
	    System.out.println(String.format("%d", pot));
	    int dealerChips = dealer.getChips();
	    dealerChips += (pot/2);
	    dealer.setChips(dealerChips);
	    pot -= (pot/2);
	    int playerChips = player.getChips();
	    playerChips += pot;
	    player.setChips(playerChips);
	} else {
	    // New game, should never be called
	    pot = 0;
	}
    }
    
    /**
       Method lays out GUI elements
    */
    
    public void layoutSubViews()
    {
	playerSetUp();
	Color pokerGreen = new Color(83,157,89);
	
	dealerPanel = new JPanel();
	dealerPanel.setLayout(new BorderLayout());
	dSubPane1 = new JPanel();
	dSubPane1.setLayout(new BoxLayout(dSubPane1, BoxLayout.Y_AXIS));
	dSubPane2 = new JPanel();
	dSubPane3 = new JPanel();
	dSubPane3.setLayout(new BorderLayout());
	
	dealerChipsLabel = new JLabel(String.format("Chips: %d", dealer.getChips()));
	dealerWinsLabel = new JLabel();
	dealerWinsLabel.setText(String.format("Dealer wins: %d", dealer.getWins()));
	playerWinsLabel = new JLabel();
	playerWinsLabel.setText(String.format("Player wins: %d", player.getWins()));
	dSubPane1.add(new JLabel("DEALER"));
	dSubPane1.add(dealerChipsLabel);
	dSubPane3.add(BorderLayout.NORTH, playerWinsLabel);
	dSubPane3.add(BorderLayout.SOUTH, dealerWinsLabel);
	dealerPanel.add(BorderLayout.WEST, dSubPane1);
	dealerPanel.add(BorderLayout.CENTER, dSubPane2);
	dealerPanel.add(BorderLayout.EAST, dSubPane3);

	ButtonHandler b = new ButtonHandler();
	
	betTextField = new JTextField(4);
	betButton = new JButton("BET");
	betButton.addActionListener(b);
	checkButton = new JButton("CHECK");
	checkButton.addActionListener(b);
	foldButton = new JButton("FOLD");
	foldButton.addActionListener(b);
	callButton = new JButton("CALL");
	callButton.addActionListener(b);

	optionArea = new JPanel();
	optionArea.setLayout(new BoxLayout(optionArea, BoxLayout.Y_AXIS));
	optionArea.add(betButton);
	optionArea.add(betTextField);
	optionArea.add(callButton);
	optionArea.add(checkButton);
	optionArea.add(foldButton);
	
	playerPanel = new JPanel();
	playerPanel.setLayout(new BorderLayout());
	pSubPane1 = new JPanel();
	pSubPane1.setLayout(new BoxLayout(pSubPane1, BoxLayout.Y_AXIS));
	pSubPane2 = new JPanel();
	pSubPane3 = new JPanel();

	playerChipsLabel = new JLabel(String.format("Chips: %d", player.getChips()));
	pSubPane1.add(new JLabel("PLAYER"));
	pSubPane1.add(playerChipsLabel);
	pSubPane3.add(optionArea);
	playerPanel.add(BorderLayout.WEST, pSubPane1);
	playerPanel.add(BorderLayout.CENTER, pSubPane2);
	playerPanel.add(BorderLayout.EAST, pSubPane3);

	backCardLabel1 = new JLabel(backCardImage);
	backCardLabel2 = new JLabel(backCardImage);
	dSubPane2.add(backCardLabel1);
	dSubPane2.add(backCardLabel2);
	for(int i=0;i<2;i++){
	    pSubPane2.add(new JLabel(getCardImage(player.getCardFromHand(i))));
	}

	
	centerPanel = new JPanel();
	centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
	flopPane = new JPanel();
	flopPane.add(new JLabel("Flop:"));
	for(int i=0;i<3;i++){
	    flopPane.add(new JLabel(getCardImage(flop.get(i))));
	}
	flopPane.setVisible(false);
	turnPane = new JPanel();
	turnPane.add(new JLabel("Turn:"));
	turnPane.add(new JLabel(getCardImage(turnCard)));
	turnPane.setVisible(false);
	riverPane = new JPanel();
	riverPane.add(new JLabel("River:"));
	riverPane.add(new JLabel(getCardImage(riverCard)));
	riverPane.setVisible(false);
	centerPanel.add(flopPane);
	centerPanel.add(turnPane);
	centerPanel.add(riverPane);

	messagePanel = new JPanel();
	messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
	messagePanel.add(Box.createRigidArea(new Dimension(0,20)));
	potLabel = new JLabel();
	potLabel.setText(String.format("Pot: %d", pot));
	messagePanel.add(potLabel);
	messagePanel.add(Box.createRigidArea(new Dimension(10,20)));

	gameMessage = new JLabel(message);
	messagePanel.add(Box.createRigidArea(new Dimension(10,20)));
	messagePanel.add(gameMessage);
	playerPrompt = new JLabel(prompt);
	messagePanel.add(playerPrompt);
	messagePanel.add(Box.createRigidArea(new Dimension(10,0)));

	passTurnButton = new JButton("END TURN");
	passTurnButton.addActionListener(b);
	showdownButton = new JButton("SHOWDOWN");
	showdownButton.addActionListener(b);
	showdownButton.setVisible(false);
	messagePanel.add(showdownButton);
	messagePanel.add(Box.createRigidArea(new Dimension(0,20)));
	messagePanel.add(passTurnButton);
	messagePanel.add(Box.createRigidArea(new Dimension(0,20)));
	
	revealPanel = new JPanel();

	dSubPane1.setBackground(pokerGreen);
	dSubPane2.setBackground(pokerGreen);
	dSubPane3.setBackground(pokerGreen);
	pSubPane1.setBackground(pokerGreen);
	pSubPane2.setBackground(pokerGreen);
	pSubPane3.setBackground(pokerGreen);
	messagePanel.setBackground(pokerGreen);
	centerPanel.setBackground(pokerGreen);
	optionArea.setBackground(pokerGreen);

	mainFrame = new JFrame("Poker Game");
	mainFrame.setSize(700,500);
	mainFrame.setResizable(false);
	mainFrame.setLocation(250, 250);
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.getContentPane().add(BorderLayout.NORTH, dealerPanel);
	mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
	mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
	mainFrame.getContentPane().add(BorderLayout.EAST, messagePanel);
	mainFrame.setVisible(true);
    }
    
    /**
     * Method to determine the winner of the game
     */
    
    public void determineWinner() {
        if(player.getHand().compareHands(dealer.getHand()) == 1) {
            player.win();
            winnerType = Winner.PLAYER;
        } else if(player.getHand().compareHands(dealer.getHand()) == 2) {
            winnerType = Winner.TIE;
        } else {
            winnerType = Winner.DEALER;
            dealer.win();
        }
    }

    /**
     * Method that updates the panels in the frame.
     */
    public void updateFrame () {
	gameMessage.setText(message);
	playerPrompt.setText(prompt);
	potLabel.setText(String.format("Pot: %d", pot));
	dealerChipsLabel.setText(String.format("Chips: %d", dealer.getChips()));
	playerChipsLabel.setText(String.format("Chips: %d", player.getChips()));
	dealerPanel.revalidate();
	playerPanel.revalidate();
	centerPanel.revalidate();
    }

    public void controlButtons(){
	if(step == Step.SHOWDOWN){
	    passTurnButton.setVisible(false);
	    betButton.setEnabled(false);
	    betTextField.setEnabled(false);
	    checkButton.setEnabled(false);
	    callButton.setEnabled(false);
	    foldButton.setEnabled(false);
	    showdownButton.setVisible(true);
	}
	else if(turn == Turn.PLAYER && responding) {
	    passTurnButton.setEnabled(false);
	    betButton.setEnabled(true);
	    betTextField.setEnabled(true);
	    checkButton.setEnabled(false);
	    callButton.setEnabled(true);
	    foldButton.setEnabled(true);
	}
        else if(turn == Turn.PLAYER){
	    passTurnButton.setEnabled(false);
	    betButton.setEnabled(true);
	    betTextField.setEnabled(true);
	    checkButton.setEnabled(true);
	    callButton.setEnabled(false);
	    foldButton.setEnabled(true);
	}
	else {
	    passTurnButton.setEnabled(false);
	    betButton.setEnabled(false);
	    betTextField.setEnabled(false);
	    checkButton.setEnabled(false);
	    callButton.setEnabled(false);
	    foldButton.setEnabled(false);
	}
    }

    /**
     * Method to pass the turn from dealer to player, or player to dealer.
     */
    
    public void changeTurn() {
	if(turn == Turn.PLAYER){
	    if(responding == true){
		turn = Turn.DEALER;
		controlButtons();
		dealerAI();
	    }
	    else{
		prompt = "Dealer turn.";
		updateFrame();
		nextStep();
		turn = Turn.DEALER;
		controlButtons();
		dealerAI();
	    }
	}
	else if(turn == Turn.DEALER){
	    message = "Dealer checks.";
	    prompt = "What will you do?";	
	    turn = Turn.PLAYER;
	    controlButtons();
	    updateFrame();
	}
    }

    /**
     *  Simple method called in single player to play the dealer's turn.
     *  Currently the dealer will only check or call.
     */
    public void dealerAI () {
	if(responding == true){
	    message = "Dealer calls.";
	    pot += bet;
	    dealer.setChips(dealer.getChips() - dealer.bet(bet));
	    bet = 0;
	    responding = false;
	    updateFrame();
	    nextStep();
	    dealerAI();
	}
	else {
	    updateFrame();
	    changeTurn();
	}
    }

    /**
     *  Method used to advance the game from each step to the next.
     *  Specifically, Blind-Flop-Turn-River-Showdown in order.
     */
    
    public void nextStep() {
	if(step == Step.BLIND){
	    message = "Dealer has dealt the flop.";
	    flopPane.setVisible(true);
	    step = Step.FLOP;
	}
	else if(step == Step.FLOP){
	    turnPane.setVisible(true);
	    step = Step.TURN;
	}
	else if (step == Step.TURN){
	    riverPane.setVisible(true);
	    step = Step.RIVER;
	}
	else if (step == Step.RIVER){
	    step = Step.SHOWDOWN;
	    controlButtons();
	}
	else {
	    message = "All bets are in.";
	    prompt = "";
	    updateFrame();
	    controlButtons();
	}
    }

    /**
     *  Starts the single player version of the game.
     */
    public void playSinglePlayer () {
	pot = 0;
	layoutSubViews();
	step = Step.BLIND;
	turn = Turn.DEALER;
	dealerAI();
    }

    private class ButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    Object src = e.getSource();
	    if(src == playButton) {
		playSinglePlayer();
		playButtonFrame.setVisible(false);
	    }
	    else if (src == passTurnButton) {
		changeTurn();
		passTurnButton.setEnabled(false);
		updateFrame();
	    }
	    else if (src == playAgainButton){ 
		winnerType = Winner.NEW_GAME;
		mainFrame.dispose();
	    
		// Create new game
		PokerGame gui2 = new PokerGame();
		gui2.playSinglePlayer();
	    }
	    else if(src == betButton) {
		// Place Bet
		String inputText = betTextField.getText();
		if (!inputText.equals("")) {
		    bet = Integer.parseInt(inputText);
		    if((player.getChips() - bet > 0)){
			betTextField.setText("");
			pot += bet;
			player.setChips(player.getChips() - player.bet(bet));
			prompt = "Player bets " + bet + ".";
			passTurnButton.setEnabled(true);
			betButton.setEnabled(false);
			betTextField.setEnabled(false);
			checkButton.setEnabled(false);
			callButton.setEnabled(false);
			foldButton.setEnabled(false);
			responding = true;
			updateFrame();
		    }
		    else {
			prompt = "Not enough chips!";
			updateFrame();
		    }
		}
		else{
		    prompt = "Enter a number of chips to bet!";
		    updateFrame();
		}
	    }
	    else if(src == checkButton) {
		prompt = "Player checks.";
	        passTurnButton.setEnabled(true);
		betButton.setEnabled(false);
		betTextField.setEnabled(false);
		checkButton.setEnabled(false);
		callButton.setEnabled(false);
		foldButton.setEnabled(false);
		updateFrame();
	    }
	    else if (src == foldButton) {
		player.foldHand();
	    }
	    else if (src == callButton) {
		pot += bet;
		player.setChips(player.getChips() - player.bet(bet));
	    }
	    else if (src == showdownButton) {
		determineWinner();
		deck.reShuffle();
		collectPot();
		showWinnerAlert();
	    }
	}
    }
    
    
    /**
     * Method shows winner alertview
     */
    
    public void showWinnerAlert() {
	
        String message = "";
	dSubPane2.remove(backCardLabel1);
	dSubPane2.remove(backCardLabel2);
	for(int i=0;i<2;i++){
	    dSubPane2.add(new JLabel(getCardImage(dealer.getCardFromHand(i))));
	}
	updateFrame();
        if (winnerType == Winner.PLAYER) {
            System.out.println("player");
            message = "You won! \n\n Play Again?";
        } else if (winnerType == Winner.DEALER) {
            System.out.println("dealer");
            message = "Dealer won. \n\n Play Again?";
        } else if (winnerType == Winner.TIE){
            System.out.println("tie");
            message = "Tie \n\n Play Again?";
        }
	
        int option = JOptionPane.showConfirmDialog(null, message, "Winner", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Restart
            mainFrame.dispose();
            // Create new game
            PokerGame gui2 = new PokerGame();
            gui2.playSinglePlayer();
        } else {
            // Quit
            System.exit(1);
        }
    } 
}
















