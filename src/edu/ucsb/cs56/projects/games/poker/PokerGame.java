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
import java.util.ArrayList;
import static java.lang.Math.*;


/**
	Class that represents a Texas Holdem' Style Poker Game.
*/

public class PokerGame implements PlayerDelegate {
    public enum Winner { PLAYER, DEALER, TIE, NEW_GAME };
    public enum Step {BLIND, FLOP, TURN, RIVER, SHOWDOWN;}
    public enum Turn {PLAYER, DEALER};
    private JPanel panel;
    private JFrame mainFrame, mainFrame2, playButtonFrame;
    private JTextField betTextField;
    private JButton playButton, playAgainButton, foldButton1, foldButton2, betButton, checkButton,
	showdownButton, passTurnButton, callButton;
    private JLabel playerWinsLabel, dealerWinsLabel, playerChipsLabel, dealerChipsLabel, potLabel;
    private JLabel gameMessage, playerPrompt, backCardLabel1, backCardLabel2;
    private JPanel dealerPanel, playerPanel, centerPanel, revealPanel;
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
	playButton.addActionListener(new playButtonListener());
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

	betTextField = new JTextField(4);
	betButton = new JButton("BET");
	betButton.addActionListener(new betButtonListener());
	checkButton = new JButton("CHECK");
	checkButton.addActionListener(new checkButtonListener());
	foldButton1 = new JButton("FOLD");
	foldButton1.addActionListener(new foldButtonListener());
	foldButton2 = new JButton("FOLD");
	foldButton2.addActionListener(new foldButtonListener());
	callButton = new JButton("CALL");
	callButton.addActionListener(new callButtonListener());

	optionArea = new JPanel();
	CardLayout cardlayout = new CardLayout();
	optionArea.setLayout(cardlayout);
	
	betPane = new JPanel();
	betPane.setLayout(new BoxLayout(betPane, BoxLayout.Y_AXIS));
	betPane.add(betButton);
	betPane.add(betTextField);
	betPane.add(checkButton);
	betPane.add(foldButton1);
	
	respondPane = new JPanel();
	respondPane.setLayout(new BoxLayout(respondPane, BoxLayout.Y_AXIS));
	respondPane.add(callButton);
	respondPane.add(foldButton2);

	optionArea.add(betPane);
	optionArea.add(respondPane);
	
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
	potLabel = new JLabel();
	potLabel.setText(String.format("Pot: %d", pot));
	centerPanel.add(potLabel);
	centerPanel.add(Box.createRigidArea(new Dimension(50,0)));

	gameMessage = new JLabel(message);
	centerPanel.add(gameMessage);
	playerPrompt = new JLabel(prompt);
	centerPanel.add(playerPrompt);

	passTurnButton = new JButton("END TURN");
	passTurnButton.addActionListener(new passTurnButtonListener());
	passTurnButton.setVisible(false);
	showdownButton = new JButton("SHOWDOWN");
	showdownButton.addActionListener(new showdownButtonListener());
	showdownButton.setVisible(false);
	centerPanel.add(showdownButton);
	centerPanel.add(passTurnButton);
	
	revealPanel = new JPanel();

	mainFrame = new JFrame("Poker Game");
	mainFrame.setSize(900,600);
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.setVisible(false);
	mainFrame.getContentPane().add(BorderLayout.NORTH, dealerPanel);
	mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
	mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
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

    /**
     * Method to pass the turn from dealer to player, or player to dealer.
     */
    
    public void changeTurn() {
	if(turn == Turn.PLAYER){
	    if(responding == true){
		turn = Turn.DEALER;
		dealerAI();
	    }
	    else{
		prompt = "Dealer turn.";
		updateFrame();
		nextStep();
		turn = Turn.DEALER;
		dealerAI();
	    }
	}
	else if(turn == Turn.DEALER){
	    message = "Dealer checks.";
	    prompt = "What will you do?";
	    if(step !=Step.SHOWDOWN)
		optionArea.setVisible(true);
	    updateFrame();
	    turn = Turn.PLAYER;
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
	    optionArea.setVisible(false);
	    showdownButton.setVisible(true);
	    updateFrame();
	    step = Step.SHOWDOWN;
	}
	else {
	    optionArea.setVisible(false);
	    showdownButton.setVisible(true);
	    message = "All bets are in.";
	    prompt = "";
	    updateFrame();
	}
    }

    /**
       Sets up the Poker game when the client clicks the Play button.
    */
    class playButtonListener implements ActionListener{
	public void actionPerformed(ActionEvent event) {
	    playSinglePlayer();
	    playButtonFrame.setVisible(false);
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
    
    
    /**
       Sets up the Poker game when the Client clicks the PlayAgain button.
    */
    class playAgainListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
	    winnerType = Winner.NEW_GAME;
	    mainFrame.dispose();
	    
	    // Create new game
	    PokerGame gui2 = new PokerGame();
	    gui2.playSinglePlayer();
  	}
  	
    }
    
    
    /**
       Bet button actions
    */
    
    class betButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    // Place Bet
	    String inputText = betTextField.getText();
	    if (!inputText.equals("")) {
		bet = Integer.parseInt(inputText);
		betTextField.setText("");
		pot += bet;
		player.setChips(player.getChips() - player.bet(bet));
	    }
	    prompt = "Player bets " + bet + ".";
	    optionArea.setVisible(false);
	    passTurnButton.setVisible(true);
	    responding = true;
	    updateFrame();
	}
    }
    
    /**
       Betting field actions
    */
    
   
    class checkButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    prompt = "Player checks.";
	    passTurnButton.setVisible(true);
	    optionArea.setVisible(false);
	    updateFrame();
	}
    }

    /**
       Fold button actions
    */
    class foldButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    player.foldHand();
	    
	}
    }

    /**
       Call button actions
     */
    class callButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    pot += bet;
	    player.setChips(player.getChips() - player.bet(bet));
	}
    }

    /**
       Showdown button actions
    */

    class showdownButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e){
	    determineWinner();
	    deck.reShuffle();
	    collectPot();
	    showWinnerAlert();
	}
    }

    /**
       Pass turn button actions.
     */

    class passTurnButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    changeTurn();
	    passTurnButton.setVisible(false);
	    updateFrame();
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
















