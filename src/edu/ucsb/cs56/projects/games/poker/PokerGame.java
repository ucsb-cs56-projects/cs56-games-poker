package edu.ucsb.cs56.projects.games.poker;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private JPanel panel;
	private JFrame mainFrame,mainFrame2;
	private JFrame playButtonFrame;
    private JTextField betTextField;
	private JButton playButton, playAgainButton, turnButton, riverButton, foldButton, betButton;
	private JLabel playerWinsLabel, dealerWinsLabel, playerChipsLabel, dealerChipsLabel, potLabel;
	private JPanel dealerPanel, playerPanel, centerPanel, revealPanel;
	private Hand dealerHand, playerHand, flop;
	private Deck deck;
	private Card turnCard, riverCard, backCard;
	private ImageIcon backCardImage;
    private Winner winnerType = Winner.NEW_GAME;
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
		player.setHand(new Hand(deck));
		dealer.setHand(new Hand(deck));
		flop = deck.showFlop();
		turnCard = deck.returnCard();
		riverCard = deck.returnCard();
		backCard = new Card(100,"B");
		//playerHand.include(flop);
		for(Card c: flop){
			player.addCardToHand(c);
			dealer.addCardToHand(c);
		}
		player.addCardToHand(turnCard);
		player.addCardToHand(riverCard);
		//dealerHand.include(flop);
		dealer.addCardToHand(turnCard);
		dealer.addCardToHand(riverCard);
		
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
        if (winnerType == Winner.PLAYER) {
            dealer.win();
        } else if (winnerType == Winner.DEALER) {
            player.win();
        }
        collectPot();
        showWinnerAlert();
        // Reset player win flag
        deck.reShuffle();
        winnerType = Winner.NEW_GAME;
    }

  /**
    Determine if dealer should bet
    @param max the odds the dealer should bet
  */

  public boolean dealerShouldBet(int max)
  {
      int shouldBet = 1 + (int)(Math.random() * max);
      int odds = 1 + (int)(Math.random() * max * 1.56);
      if (shouldBet >= odds)
        return true;
      else
        return false;

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

      pot = 0;
      dealerPanel = new JPanel();
      playerPanel = new JPanel();
      centerPanel = new JPanel();
      revealPanel = new JPanel();
      dealerWinsLabel = new JLabel();
      playerWinsLabel = new JLabel();
      potLabel = new JLabel();
      dealerChipsLabel = new JLabel(String.format("Chips: %d", dealer.getChips()));
      playerChipsLabel = new JLabel(String.format("Chips: %d", player.getChips()));
      JPanel betArea = new JPanel();
      JPanel winCount = new JPanel();
      winCount.setLayout(new BorderLayout());
      betArea.setLayout(new BorderLayout());
      playerPanel.add(betArea);
      dealerPanel.add(winCount);
      dealerPanel.add(dealerChipsLabel);
      betTextField = new JTextField(4);
      betTextField.addActionListener(new betTextFieldListener());
      betButton = new JButton("BET");
      betButton.addActionListener(new betButtonListener());

      turnButton = new JButton("TURN");
      foldButton = new JButton("FOLD");
      foldButton.addActionListener(new foldButtonListener());
      turnButton.addActionListener(new turnButtonListener());
      for(int i=0;i<2;i++){
        dealerPanel.add(new JLabel(backCardImage));
        playerPanel.add(new JLabel(getCardImage(player.getCardFromHand(i))));
      }
      centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
      for(int i=0;i<3;i++){
        centerPanel.add(BorderLayout.CENTER, new
                JLabel(getCardImage(flop.get(i))));
      }
        
      dealerPanel.add(new JLabel("DEALER"));
      playerPanel.add(new JLabel("PLAYER"));
      playerPanel.add(foldButton);
      centerPanel.add(BorderLayout.SOUTH, turnButton);

      playerPanel.add(playerChipsLabel);
      betArea.add(BorderLayout.NORTH, betButton);
      betArea.add(BorderLayout.SOUTH, betTextField);


      playerWinsLabel.setText(String.format("Player wins: %d", player.getWins()));
      dealerWinsLabel.setText(String.format("Dealer wins: %d", dealer.getWins()));
      winCount.add(BorderLayout.NORTH, playerWinsLabel);
      winCount.add(BorderLayout.SOUTH, dealerWinsLabel);

      deck.reShuffle();

      mainFrame = new JFrame();
      mainFrame.setSize(600,600);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.getContentPane().add(BorderLayout.NORTH, dealerPanel);
      mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
      mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
      mainFrame.setVisible(true);

      if (dealerShouldBet(20)) {
          int dealerBet = 1 + (int)(Math.random() * dealer.getChips());
          showPlayerCallBetAlert(dealerBet);
      }
      potLabel.setText(String.format("Pot: %d", pot));
      centerPanel.add(potLabel, BorderLayout.SOUTH);
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
     * Method that sets up a new Poker game with new hands.
     */
    public void replay()
    {
        pot = 0;
        layoutSubViews();

    }

   /**
	 Sets up the Poker game when the client clicks the Play button.
   */
	class playButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
  			layoutSubViews();
  	}
  }
/**
	Displays the turn card when the user clicks the button.
*/
  class turnButtonListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
  		centerPanel.remove(turnButton);
  		centerPanel.add(new JLabel(getCardImage(turnCard)));
  		riverButton = new JButton("RIVER");
  		riverButton.addActionListener(new riverButtonListener());
  		centerPanel.add(riverButton);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);

        if (dealerShouldBet(10)) {
            int dealerBet = 1 + (int)(Math.random() * dealer.getChips());
            showPlayerCallBetAlert(dealerBet);
        }
        potLabel.setText(String.format("Pot: %d", pot));
        centerPanel.add(potLabel, BorderLayout.SOUTH);
      
  	}
  }

/**
	Displays the river card when the user clicks the button.
*/  
  class riverButtonListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
  		centerPanel.remove(riverButton);
  		centerPanel.add(new
  		JLabel(getCardImage(riverCard)));
  		
  		playAgainButton=new JButton("Play Again");
        playAgainButton.addActionListener(new playAgainListener());

        determineWinner();
  	
  		deck.reShuffle();
  		centerPanel.add(playAgainButton);
        potLabel.setText(String.format("Pot: %d", pot));
        centerPanel.add(potLabel);

  		for(int i=0;i<2;i++) {
  			revealPanel.add(new JLabel(getCardImage(dealer.getCardFromHand(i))));
        }
  		revealPanel.add(new JLabel("DEALER"));

        collectPot();
  		
  		mainFrame.remove(dealerPanel);
  		mainFrame.add(revealPanel);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);

        showWinnerAlert();
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
	Sets up the Poker game when the Client clicks the PlayAgain button.
*/
  class playAgainListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
        winnerType = Winner.NEW_GAME;
  		mainFrame.dispose();

        // Create new game
  		PokerGame gui2 = new PokerGame();
  		gui2.replay();
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
          int amount = Integer.parseInt(inputText);
          betTextField.setText("");
          // Dealer calls
          if (dealerShouldBet(10)) {
              pot += player.bet(amount);
              pot += dealer.bet(amount);
          } else {
              winnerType = Winner.PLAYER;
              dealer.foldHand();
          }

      }
        potLabel.setText(String.format("Pot: %d", pot));
        centerPanel.add(potLabel, BorderLayout.SOUTH);

    }
  }

/**
  Betting field actions
*/

  class betTextFieldListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      // Handle user pressing enter key to place bet
      String inputText = betTextField.getText();
      if (!inputText.equals("")) {
          int amount = Integer.parseInt(inputText);
          betTextField.setText("");
          // Dealer calls
          if (dealerShouldBet(10)) {
              pot += player.bet(amount);
              pot += dealer.bet(amount);
          } else {
              System.out.println("BETTING");
              winnerType = Winner.PLAYER;
              dealer.foldHand();
          }
      }
        potLabel.setText(String.format("Pot: %d", pot));
        centerPanel.add(potLabel, BorderLayout.SOUTH);
    }
  }

    /**
     * Method to show determine of player calls bet
     */

    public void showPlayerCallBetAlert(int amount) {
        String message = String.format("Would you like to call: %d?", amount);
        int option = JOptionPane.showConfirmDialog(null, message, "Betting", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            pot += dealer.bet(amount);
            pot += player.bet(amount);
        } else {
            // Player folds
            winnerType = Winner.DEALER;
            player.foldHand();
        }
    }

    /**
     * Method shows winner alertview
     */

    public void showWinnerAlert() {

        String message = "";
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
            gui2.replay();
        } else {
            // Quit
            System.exit(1);
        }
    }

}
















