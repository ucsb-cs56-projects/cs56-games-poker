package edu.ucsb.cs56.projects.games.poker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import java.util.ArrayList;
import static java.lang.Math.*;


/**
  Enum for dealing with winner
*/



/**
	Class that represents a Texas Holdem' Style Poker Game.
*/

public class PokerGame {
  public enum Winner { PLAYER, DEALER, TIE, NEW_GAME };
	private JPanel panel;
	private JFrame mainFrame,mainFrame2;
	private JFrame playButtonFrame;
  private JTextField betTextField;
	private JButton playButton, playAgainButton, turnButton, riverButton, foldButton, betButton;
	private JLabel winnerLabel, playerWinsLabel, dealerWinsLabel, playerChipsLabel, dealerChipsLabel;
	private JPanel dealerPanel, playerPanel, centerPanel, revealPanel;
	private Hand dealerHand, playerHand, flop;
	private Deck deck;
	private Card turnCard, riverCard, backCard;
	private ImageIcon backCardImage;
  private static int playerWins = 0;
  private static int dealerWins = 0;
  private Winner winnerType = Winner.NEW_GAME;
  private static int playerChips = 500;
  private static int dealerChips = 500;
  private int pot;

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

		playerWins = 0;
    dealerWins = 0;

		panel=new JPanel();
		
		playButton=new JButton("Click to Play");
		playButton.addActionListener(new playButtonListener());
		panel.add(playButton,BorderLayout.CENTER);
		panel.setBackground(Color.darkGray);
		
		playButtonFrame.add(BorderLayout.CENTER, panel);
		playButtonFrame.setSize(200,200);
		
		playButtonFrame.setVisible(true);
	}
	
/**
	Sets up the player's and dealer's hand.
*/	
	public void playerSetUp(){
		deck=new Deck();
		playerHand=new Hand(deck);
		dealerHand=new Hand(deck);
		flop=deck.showFlop();
		turnCard=deck.returnCard();
		riverCard=deck.returnCard();
		backCard=new Card(100,"B");
		//playerHand.include(flop);
		for(Card c: flop){
			playerHand.add(c);
			dealerHand.add(c);
		}
		playerHand.add(turnCard);
		playerHand.add(riverCard);
		//dealerHand.include(flop);
		dealerHand.add(turnCard);
		dealerHand.add(riverCard);
		
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
  Method for when user folds
*/

  public void userFold()
  {
    dealerWins++;
    if (winnerType == Winner.PLAYER) {
      playerWins--;
    }
    collectPot();
    // Reset player win flag
    deck.reShuffle();
    winnerType = Winner.NEW_GAME;
    mainFrame.dispose();
    PokerGame gui2=new PokerGame();
    gui2.replay();
  }

/**
  Method for when dealer folds
*/

  public void dealerFold()
  {
    playerWins++;
    if (winnerType == Winner.DEALER) {
      dealerWins--;
    }
    collectPot();
    // Reset player win flag
    deck.reShuffle();
    winnerType = Winner.NEW_GAME;
    mainFrame.dispose();
    PokerGame gui2=new PokerGame();
    gui2.replay();
  }

/**
  Method for betting
*/

public void bet(int chips, boolean isPlayer)
{
  int randomNum = 1 + (int)(Math.random() * 10);
  if (isPlayer) {
    // Deal with user having no chips
    if (chips > playerChips) {
       System.out.println(String.format("random: %d", randomNum));
      if (randomNum >= 3) {
        if (dealerChips <= playerChips) {
          pot += (dealerChips * 2);
          playerChips -= dealerChips;
          dealerChips = 0;
        } else {
          dealerChips -= playerChips;
          pot += (playerChips * 2);
          playerChips = 0;
        }
      } else {
        winnerType = Winner.DEALER;
        dealerFold();
      }
    } else {
      
      System.out.println(String.format("random: %d", randomNum));
      if (randomNum >= 3) {
        if (dealerChips <= chips) {
          playerChips -= dealerChips;
          pot += (dealerChips * 2);
          dealerChips = 0;
        } else {
          pot += (chips * 2);
          playerChips -= chips;
          dealerChips -= chips; 
        }

      } else {
        winnerType = Winner.DEALER;
        dealerFold();
      }
    }

  } else {
    // Check to see if user wants to call
    String message = String.format("Would you like to call: %d?", chips);
    int option = JOptionPane.showConfirmDialog(null, message, "Betting", JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
      if (playerChips <= chips) {
        pot += (2 * playerChips);
        playerChips = 0;
        dealerChips -= playerChips;
      } else {
        playerChips -= chips;
        dealerChips -= chips;
        pot += (chips * 2);
      }
    } else {
      winnerType = Winner.DEALER;
      userFold();
    }
  }
}

/**
  Determine if dealer should bet
*/

  public boolean dealerShouldBet(int max)
  {
      int shouldBet = 1 + (int)(Math.random() * max);
      int odds = 1 + (int)(Math.random() * max * 1.56);
      if (shouldBet >= odds) {
        return true;
      } else {
        return false;
      }
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
      playerChips += pot;
    } else if (winnerType == Winner.DEALER) {
      // Player lost
          System.out.println("DEALER");
          System.out.println(String.format("%d", pot));
      dealerChips += pot;
    } else if (winnerType == Winner.TIE){
      // Tie, split pot
          System.out.println("Tie");
          System.out.println(String.format("%d", pot));
      dealerChips += (pot/2);
      pot -= (pot/2);
      playerChips += pot;
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
      dealerPanel=new JPanel();
      playerPanel=new JPanel();
      centerPanel=new JPanel();
      revealPanel=new JPanel();
      dealerWinsLabel = new JLabel();
      playerWinsLabel = new JLabel();
      dealerChipsLabel = new JLabel(String.format("Chips: %d", dealerChips));
      playerChipsLabel = new JLabel(String.format("Chips: %d", playerChips));
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

      turnButton=new JButton("TURN");
      foldButton = new JButton("FOLD");
      foldButton.addActionListener(new foldButtonListener());
      turnButton.addActionListener(new turnButtonListener());
      for(int i=0;i<2;i++){
        dealerPanel.add(new JLabel(backCardImage));
        playerPanel.add(new JLabel(getCardImage(playerHand.get(i))));
      }
      centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
      for(int i=0;i<3;i++){
        centerPanel.add(BorderLayout.CENTER, new 
        JLabel(getCardImage(flop.get(i))));
      }
        
      dealerPanel.add(new JLabel("DEALER"));
      playerPanel.add(new JLabel("PLAYER"));
      playerPanel.add(foldButton);
      centerPanel.add(BorderLayout.SOUTH,turnButton);

      playerPanel.add(playerChipsLabel);
      betArea.add(BorderLayout.NORTH, betButton);
      betArea.add(BorderLayout.SOUTH, betTextField);


      playerWinsLabel.setText(String.format("Player wins: %d", playerWins));
      dealerWinsLabel.setText(String.format("Dealer wins: %d", dealerWins));
      winCount.add(BorderLayout.NORTH, playerWinsLabel);
      winCount.add(BorderLayout.SOUTH, dealerWinsLabel);

      if(playerHand.compareHands(dealerHand) == 1) {
        playerWins = playerWins + 1;
        winnerType = Winner.PLAYER;
        winnerLabel=new JLabel("YOU WON!");
        System.out.println("I win");
      } else if(playerHand.compareHands(dealerHand) == 2) {
        winnerType = Winner.TIE;
        winnerLabel=new JLabel("TIED");
        System.out.println("No one wins");
      } else {
        winnerType = Winner.DEALER;
        dealerWins = dealerWins + 1;
        winnerLabel=new JLabel("Dealer won");
        System.out.println("You win");
      }

      deck.reShuffle();
        
      mainFrame=new JFrame();
      mainFrame.setSize(600,600);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.getContentPane().add(BorderLayout.NORTH, dealerPanel);
      mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
      mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
      mainFrame.setVisible(true);

      if (dealerShouldBet(20)) {
        int randomBet = 1 + (int)(Math.random() * dealerChips);
        bet(randomBet ,false);
      }
  }
	
/**
	Method that sets up a new Poker game with new hands.
*/
	public void replay()
  {  	
    layoutSubViews();
  }

/**
	Sets up the Poker game when the client clicks the Play button.
*/
	class playButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
	
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
  		riverButton=new JButton("RIVER");
  		riverButton.addActionListener(new riverButtonListener());
  		centerPanel.add(riverButton);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);

      if (dealerShouldBet(10)) {
        int randomBet = 1 + (int)(Math.random() * dealerChips);
        bet(randomBet ,false);
      }
      
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
  	
  		deck.reShuffle();	
  		centerPanel.add(winnerLabel);
  		centerPanel.add(playAgainButton);
  		
  		for(int i=0;i<2;i++) {
  			revealPanel.add(new JLabel(getCardImage(dealerHand.get(i))));
      }
  		revealPanel.add(new JLabel("DEALER"));

      collectPot();
  		
  		mainFrame.remove(dealerPanel);
  		mainFrame.add(revealPanel);
  		mainFrame.setVisible(false);
  		mainFrame.setVisible(true);
  	}
  }

/**
  Fold button actions
*/
  class foldButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      userFold();
    }
  }
/**
	Sets up the Poker game when the Client clicks the PlayAgain button.
*/
  class playAgainListener implements ActionListener{
  	public void actionPerformed(ActionEvent event){
      winnerType = Winner.NEW_GAME;
  		mainFrame.dispose();
  		PokerGame gui2=new PokerGame();
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
        bet(amount, true);
      }

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
        bet(amount, true);
      }

    }
  }

}
















