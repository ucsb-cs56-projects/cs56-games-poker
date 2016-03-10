package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * Class that represents a Texas Holdem' Style Poker Game.
 */

public class PokerGame implements PlayerDelegate {
	public enum Winner {
		PLAYER, OPPONENT, TIE, NEW_GAME
	};

	public enum Step {
		BLIND, FLOP, TURN, RIVER, SHOWDOWN
	};

	public enum Turn {
		PLAYER, OPPONENT
	};

	protected JFrame mainFrame, mainFrame2, gameOverFrame;
	protected JTextField betTextField;
	protected JButton foldButton, betButton, checkButton, showdownButton, passTurnButton, callButton;
	protected JButton gameOverButton;
	protected JLabel playerWinsLabel, opponentWinsLabel, playerChipsLabel, opponentChipsLabel, potLabel;
	protected JLabel gameMessage, playerPrompt, backCardLabel1, backCardLabel2, gameOverLabel;
	protected JPanel opponentPanel, playerPanel, centerPanel, messagePanel, gameOverPanel;
	protected JPanel oSubPane1, oSubPane2, oSubPane3, pSubPane1, pSubPane2, pSubPane3, optionArea;
	protected JPanel flopPane, turnPane, riverPane, betPane, respondPane;
	protected String message, prompt;
	protected Hand opponentHand, playerHand, flop;
	protected Card turnCard, riverCard, backCard;
	protected ImageIcon backCardImage;
	protected Winner winnerType = Winner.NEW_GAME;
	protected Step step;
	protected Turn turn;
	protected Deck deck;
	protected int bet = 0;
	protected boolean responding = false;
	protected boolean gameOver = false;
	protected boolean youFold = false;
	protected boolean opponentFold = false;
	protected static int pot = 0;
	protected static Player player = new Player(500);
	protected static Player opponent = new Player(500);

	/**
	 * No arg constructor that initializes a new deck.
	 */
	public PokerGame() {
	}

	/**
	 * Sets up the player's and opponent's hand.
	 */
	public void playerSetUp() {
		player.delegate = this;
		opponent.delegate = this;
		if (player.getChips() > 5 && opponent.getChips() > 5) {
			bet = 10;
			player.setChips(player.getChips() - 5);
			opponent.setChips(opponent.getChips() - 5);
			pot += bet;
			message = "Ante of 5 chips set.";
		} else {
			gameOver = true;
			showWinnerAlert();
		}

		deck = new Deck();
		player.setHand(deck.dealCards());
		opponent.setHand(deck.dealCards());

		flop = deck.showFlop();
		turnCard = deck.returnCard();
		riverCard = deck.returnCard();
		for (Card c : flop) {
			player.addCardToHand(c);
			opponent.addCardToHand(c);
			
		}
		player.addCardToHand(turnCard);
		player.addCardToHand(riverCard);
		opponent.addCardToHand(turnCard);
		opponent.addCardToHand(riverCard);
		backCard = new Card(100, "B");
		String dir = "Cards/";
		String cardFile = "B.png";
		URL url = getClass().getResource(dir + cardFile);
		backCardImage = new ImageIcon(url);

	}

	/**
	 * Returns an ImageIcon by using the URL class in order to make the
	 * ImageIcon web compatible.
	 * 
	 * @param c
	 *            card whose image is to be retrieved.
	 */
	public ImageIcon getCardImage(Card c) {
		String dir = "Cards/";
		String cardFile = c.toString() + ".png";
		URL url = getClass().getResource(dir + cardFile);
		return new ImageIcon(url);

	}

	/**
	 * PlayerDelegate method to handle folding.
	 */

	public void fold() {
		if (turn == Turn.PLAYER) {
			winnerType = Winner.OPPONENT;
			opponent.win();
		} else {
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
	 * Method to transfer winnings.
	 */

	public void collectPot() {

		if (winnerType == Winner.PLAYER) {
			// Player won
			System.out.println("Player");
			System.out.println(String.format("%d", pot));
			int playerChips = player.getChips();
			playerChips += pot;
			player.setChips(playerChips);
		} else if (winnerType == Winner.OPPONENT) {
			// Player lost
			System.out.println("OPPONENT");
			System.out.println(String.format("%d", pot));
			int opponentChips = opponent.getChips();
			opponentChips += pot;
			opponent.setChips(opponentChips);
		} else if (winnerType == Winner.TIE) {
			// Tie, split pot
			System.out.println("Tie");
			System.out.println(String.format("%d", pot));
			int opponentChips = opponent.getChips();
			opponentChips += (pot / 2);
			opponent.setChips(opponentChips);
			pot -= (pot / 2);
			int playerChips = player.getChips();
			playerChips += pot;
			player.setChips(playerChips);
		} else {
			// New game, should never be called
			pot = 0;
		}
	}

	/**
	 * Method lays out GUI elements
	 */

	public void layoutSubViews() {
		if (!gameOver) {
			Color pokerGreen = new Color(83, 157, 89);

			foldButton = new JButton("FOLD");
			foldButton.setEnabled(false);
			betButton = new JButton("BET");
			betButton.setEnabled(false);
			betTextField = new JTextField(4);
			checkButton = new JButton("CHECK");
			checkButton.setEnabled(false);
			callButton = new JButton("CALL");
			callButton.setEnabled(false);
			showdownButton = new JButton("SHOWDOWN");
			passTurnButton = new JButton("PASS TURN");
			passTurnButton.setEnabled(false);
			addActionListeners();

			opponentPanel = new JPanel();
			opponentPanel.setLayout(new BorderLayout());
			oSubPane1 = new JPanel();
			oSubPane1.setLayout(new BoxLayout(oSubPane1, BoxLayout.Y_AXIS));
			oSubPane2 = new JPanel();
			oSubPane3 = new JPanel();
			oSubPane3.setLayout(new BorderLayout());

			opponentChipsLabel = new JLabel(String.format("Chips: %d", opponent.getChips()));
			opponentWinsLabel = new JLabel();
			opponentWinsLabel.setText(String.format("Opponent wins: %d", opponent.getWins()));
			playerWinsLabel = new JLabel();
			playerWinsLabel.setText(String.format("Player wins: %d", player.getWins()));
			oSubPane1.add(new JLabel("OPPONENT"));
			oSubPane1.add(opponentChipsLabel);
			oSubPane3.add(BorderLayout.NORTH, playerWinsLabel);
			oSubPane3.add(BorderLayout.SOUTH, opponentWinsLabel);
			opponentPanel.add(BorderLayout.WEST, oSubPane1);
			opponentPanel.add(BorderLayout.CENTER, oSubPane2);
			opponentPanel.add(BorderLayout.EAST, oSubPane3);

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
			oSubPane2.add(backCardLabel1);
			oSubPane2.add(backCardLabel2);
			for (int i = 0; i < 2; i++) {
				pSubPane2.add(new JLabel(getCardImage(player.getCardFromHand(i))));
			}

			centerPanel = new JPanel();
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
			flopPane = new JPanel();
			flopPane.add(new JLabel("Flop:"));
			for (int i = 0; i < 3; i++) {
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
			messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
			potLabel = new JLabel();
			potLabel.setText(String.format("Pot: %d", pot));
			messagePanel.add(potLabel);
			messagePanel.add(Box.createRigidArea(new Dimension(10, 20)));

			gameMessage = new JLabel(message);
			messagePanel.add(Box.createRigidArea(new Dimension(10, 20)));
			messagePanel.add(gameMessage);
			playerPrompt = new JLabel(prompt);
			messagePanel.add(playerPrompt);
			messagePanel.add(Box.createRigidArea(new Dimension(10, 0)));

			showdownButton.setVisible(false);
			messagePanel.add(showdownButton);
			messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
			messagePanel.add(passTurnButton);
			messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));

			oSubPane1.setBackground(pokerGreen);
			oSubPane2.setBackground(pokerGreen);
			oSubPane3.setBackground(pokerGreen);
			pSubPane1.setBackground(pokerGreen);
			pSubPane2.setBackground(pokerGreen);
			pSubPane3.setBackground(pokerGreen);
			messagePanel.setBackground(pokerGreen);
			centerPanel.setBackground(pokerGreen);
			optionArea.setBackground(pokerGreen);
			flopPane.setBackground(pokerGreen);
			turnPane.setBackground(pokerGreen);
			riverPane.setBackground(pokerGreen);

			mainFrame = new JFrame("Poker Game");
			mainFrame.setSize(800, 500);
			mainFrame.setResizable(false);
			mainFrame.setLocation(250, 250);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.getContentPane().add(BorderLayout.NORTH, opponentPanel);
			mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
			mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
			mainFrame.getContentPane().add(BorderLayout.EAST, messagePanel);
			mainFrame.setVisible(true);
		}
	}
	
	/**
	 * Adds action listeners to all buttons in the main GUI.
	 */
	public void addActionListeners() {
		ButtonHandler b = new ButtonHandler();
		foldButton.addActionListener(b);
		betButton.addActionListener(b);
		checkButton.addActionListener(b);
		callButton.addActionListener(b);
		showdownButton.addActionListener(b);
		passTurnButton.addActionListener(b);
	}
    
	/**
	 * Method to determine the winner of the game
	 */

	public void determineWinner() {
		if (player.getHand().compareHands(opponent.getHand()) == 1) {
			player.win();
			winnerType = Winner.PLAYER;
		} else if (player.getHand().compareHands(opponent.getHand()) == 2) {
			winnerType = Winner.TIE;
		} else {
			winnerType = Winner.OPPONENT;
			opponent.win();
		}
	}

	/**
	 * Method that updates the panels in the frame.
	 */
	public void updateFrame() {
		if (step == Step.FLOP) {
			flopPane.setVisible(true);
		} else if (step == Step.TURN) {
			turnPane.setVisible(true);
		} else if (step == Step.RIVER) {
			riverPane.setVisible(true);
		}
		gameMessage.setText(message);
		playerPrompt.setText(prompt);
		potLabel.setText(String.format("Pot: %d", pot));
		opponentChipsLabel.setText(String.format("Chips: %d", opponent.getChips()));
		playerChipsLabel.setText(String.format("Chips: %d", player.getChips()));
		opponentPanel.revalidate();
		playerPanel.revalidate();
		centerPanel.revalidate();
	}
	
	/**
	 *  Enables and disables buttons one the screen, depending
	 *  on the turn and step.
	 */
	public void controlButtons() {
		if (step == Step.SHOWDOWN) {
			passTurnButton.setVisible(false);
			betButton.setEnabled(false);
			betTextField.setEnabled(false);
			checkButton.setEnabled(false);
			callButton.setEnabled(false);
			foldButton.setEnabled(false);
			showdownButton.setVisible(true);
		} else if (turn == Turn.PLAYER && responding) {
			passTurnButton.setEnabled(false);
			betButton.setEnabled(false);
			betTextField.setEnabled(false);
			checkButton.setEnabled(false);
			callButton.setEnabled(true);
			foldButton.setEnabled(true);
		} else if (turn == Turn.PLAYER) {
			passTurnButton.setEnabled(false);
			betButton.setEnabled(true);
			betTextField.setEnabled(true);
			checkButton.setEnabled(true);
			callButton.setEnabled(false);
			foldButton.setEnabled(true);
		} else {
			passTurnButton.setEnabled(false);
			betButton.setEnabled(false);
			betTextField.setEnabled(false);
			checkButton.setEnabled(false);
			callButton.setEnabled(false);
			foldButton.setEnabled(false);
		}
	}

	/**
	 * Method to pass the turn from opponent to player, or player to opponent.
	 */

	public void changeTurn() {
		if (turn == Turn.PLAYER) {
			if (responding == true) {
				turn = Turn.OPPONENT;
				controlButtons();
			} else {
				prompt = "Opponent turn.";
				updateFrame();
				nextStep();
				turn = Turn.OPPONENT;
				controlButtons();
			}
		} else if (turn == Turn.OPPONENT) {
			prompt = "What will you do?";
			turn = Turn.PLAYER;
			controlButtons();
			updateFrame();
		}
	}

	/**
	 * Method used to advance the game from each step to the next. Specifically,
	 * Blind-Flop-Turn-River-Showdown in order.
	 */

	public void nextStep() {
		if (step == Step.BLIND) {
			step = Step.FLOP;
		} else if (step == Step.FLOP) {
			step = Step.TURN;
		} else if (step == Step.TURN) {
			step = Step.RIVER;
		} else if (step == Step.RIVER) {
			step = Step.SHOWDOWN;
			controlButtons();
			message = "All bets are in.";
			prompt = "Determine Winner: ";
		} else {
			message = "All bets are in.";
			prompt = "Determine Winner: ";
			updateFrame();
			controlButtons();
		}
	}
	
	/**
	 * Inner class that handles all buttons in the GUI.
	 */
	protected class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			if (src == passTurnButton) {
				changeTurn();
				passTurnButton.setEnabled(false);
				updateFrame();
			} else if (src == betButton) {
				// Place Bet
				String inputText = betTextField.getText();
				if (!inputText.equals("")) {
					bet = Integer.parseInt(inputText);
					if ((player.getChips() - bet >= 0) && (opponent.getChips() - bet >= 0)) {
						betTextField.setText("");
						pot += bet;
						player.setChips(player.getChips() - player.bet(bet));
						message = "Opponent waiting for turn.";
						prompt = "Player bets " + bet + ".";
						passTurnButton.setEnabled(true);
						betButton.setEnabled(false);
						betTextField.setEnabled(false);
						checkButton.setEnabled(false);
						callButton.setEnabled(false);
						foldButton.setEnabled(false);
						responding = true;
						updateFrame();
					} else {
						prompt = "Not enough chips!";
						updateFrame();
					}
				} else {
					prompt = "Enter a number of chips to bet!";
					updateFrame();
				}
			} else if (src == checkButton) {
				message = "Opponent waiting to deal.";
				prompt = "Player checks.";
				passTurnButton.setEnabled(true);
				betButton.setEnabled(false);
				betTextField.setEnabled(false);
				checkButton.setEnabled(false);
				callButton.setEnabled(false);
				foldButton.setEnabled(false);
				updateFrame();
			} else if (src == foldButton) {
				message = "Opponent waiting for turn.";
				prompt = "You fold.";
				player.foldHand();
			} else if (src == callButton) {
				pot += bet;
				player.setChips(player.getChips() - player.bet(bet));
				prompt = "You call.";
				responding = false;
				callButton.setEnabled(false);
				foldButton.setEnabled(false);
				updateFrame();
				changeTurn();
			} else if (src == showdownButton) {
				determineWinner();
				deck.reShuffle();
				collectPot();
				showWinnerAlert();
			}
		}
	}

	/**
	 * Method shows winner and exits game.
	 */

	public void showWinnerAlert() {
		if (!gameOver) {
			message = "";
			oSubPane2.remove(backCardLabel1);
			oSubPane2.remove(backCardLabel2);
			for (int i = 0; i < 2; i++) {
				oSubPane2.add(new JLabel(getCardImage(opponent.getCardFromHand(i))));
			}
			if (winnerType == Winner.PLAYER) {
				System.out.println("player");
				prompt = "You won!";
			} else if (winnerType == Winner.OPPONENT) {
				System.out.println("opponent");
				prompt = "Opponent won.";
			} else if (winnerType == Winner.TIE) {
				System.out.println("tie");
				prompt = "Tie!";
			}
			updateFrame();
			System.exit(1);
		} else
			System.exit(1);
	}

}
