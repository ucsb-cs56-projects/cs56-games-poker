package edu.ucsb.cs56.projects.games.poker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.ImageIcon;

public class PokerClient extends PokerGame {
	private Socket sock;
	ObjectOutputStream clientOutput;
	ObjectInputStream clientInput;
	PokerGameState state;
	Thread listener;
	int playerNumber;

	public static void main(String[] args) {
		PokerClient client = new PokerClient();
		try {
			client.go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() throws IOException {
		setUpNetworking();
		try {
			System.out.println("You connected.");
			clientInput = new ObjectInputStream(sock.getInputStream());
			clientOutput = new ObjectOutputStream(sock.getOutputStream());
			
			PokerGameState test = new PokerGameState(0,0,0);
			clientOutput.writeObject(test);
			clientOutput.flush();
			
			playerNumber = (int) clientInput.readObject();
			System.out.println("You are Player " + playerNumber);
			listener = new Thread(new InputReader());
			listener.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUpNetworking() {

		try {
			sock = new Socket("127.0.0.1", 15000);
			System.out.println("Player connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void playerSetUp() {
		player.delegate = this;
		opponent.delegate = this;
		if(player.getChips() > 5 && opponent.getChips() > 5){
			pot += 10;
			player.setChips(player.getChips() - 5);
			opponent.setChips(opponent.getChips() - 5);
			message = "Ante of 5 chips set.";
		} else {
			gameOver = true;
			showWinnerAlert();
		}
		flop = state.getFlop();
		turnCard = state.getTurnCard();
		riverCard = state.getRiverCard();
		
		if(playerNumber == 1){
			player.setHand(state.getPlayer1Hand());
			opponent.setHand(state.getPlayer2Hand());
		} else {
			player.setHand(state.getPlayer2Hand());
			opponent.setHand(state.getPlayer1Hand());
		}
		for(Card c : flop) {
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
	
	public void addActionListeners() {
		ClientButtonHandler b = new ClientButtonHandler();
		foldButton.addActionListener(b);
		betButton.addActionListener(b);
		checkButton.addActionListener(b);
		callButton.addActionListener(b);
		showdownButton.addActionListener(b);
		passTurnButton.addActionListener(b);
	}
	
	public class InputReader implements Runnable {
		
		public void run () {
			try {
				while (true) {
					state = (PokerGameState) clientInput.readObject();
					if(state.getPlayer1Chips() == 1){
						System.out.println("read");
					}
					if (state != null) {
						int stateStep = state.getStep();
						int stateTurn = state.getTurn();
						if (playerNumber == 1) {
							if (stateStep == 0 && stateTurn == 1) {
								System.out.println("Starting game");
								playerSetUp();
								layoutSubViews();
								if (!gameOver) {
									if (state.mustRespond()) {
										responding = true;
										controlButtons();
										updateFrame();
									} else {
										turn = Turn.PLAYER;
										step = Step.BLIND;
										controlButtons();
										updateFrame();
									}
								}
							} else if (stateStep == 4) {
								step = Step.SHOWDOWN;
								controlButtons();
								updateFrame();
							} else {
								if (state.mustRespond()) {
									responding = true;
									nextStep();
									player.setChips(state.getPlayer1Chips());
									opponent.setChips(state.getPlayer2Chips());
									bet = state.getBet();
									pot = state.getPot();
									changeTurn();
									updateFrame();
								} else {
									player.setChips(state.getPlayer1Chips());
									opponent.setChips(state.getPlayer2Chips());
									bet = state.getBet();
									pot = state.getPot();
									changeTurn();
									updateFrame();
								}
							}
						} else if (playerNumber == 2) {
							if (stateStep == 0 && stateTurn == 1) {
								System.out.println("Starting game");
								playerSetUp();
								layoutSubViews();
								if (!gameOver) {
									if (state.mustRespond()) {
										responding = true;
										controlButtons();
										updateFrame();
									} else {
										turn = Turn.OPPONENT;
										step = Step.BLIND;
										controlButtons();
										updateFrame();
									}
								} else if (stateStep == 4) {
									step = Step.SHOWDOWN;
									controlButtons();
									showdownButton.setEnabled(false);
									updateFrame();
								} else {
									if (state.mustRespond()) {
										responding = true;
										nextStep();
										player.setChips(state.getPlayer2Chips());
										opponent.setChips(state.getPlayer1Chips());
										bet = state.getBet();
										pot = state.getPot();
										changeTurn();
										updateFrame();
									} else {
										nextStep();
										player.setChips(state.getPlayer2Chips());
										opponent.setChips(state.getPlayer1Chips());
										bet = state.getBet();
										pot = state.getPot();
										changeTurn();
										checkButton.setEnabled(true);
										updateFrame();
									}
								}
							}
						}
					}

					clientOutput.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
	public class ClientButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object src = event.getSource();
			if(src == passTurnButton){
				changeTurn();
				state.changeTurn();
				state.requestRespond(responding);
				if(playerNumber == 1){
					state.setPlayer1Chips(player.getChips());
					state.setPlayer2Chips(opponent.getChips());
				}
				else{
					state.setPlayer1Chips(opponent.getChips());
					state.setPlayer2Chips(player.getChips());
				}
				state.setBet(bet);
				state.setPot(pot);
				try {
					clientOutput.writeObject(state);
					clientOutput.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(src == betButton) {
				String inputText = betTextField.getText();
				if(!inputText.equals("")){
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
				}
			else if(src == checkButton) {
				bet = 0;
				passTurnButton.setEnabled(true);
				betButton.setEnabled(false);
				betTextField.setEnabled(false);
				checkButton.setEnabled(false);
				callButton.setEnabled(false);
				foldButton.setEnabled(false);
				updateFrame();
			}
			else if(src == foldButton){
				player.foldHand();
			}
			else if(src == callButton){
				pot += bet;
				player.setChips(player.getChips() - player.bet(bet));
				responding = false;
				callButton.setEnabled(false);
				foldButton.setEnabled(false);
				updateFrame();
				changeTurn();
				state.changeTurn();
				state.requestRespond(responding);
				if(playerNumber == 1){
					state.setPlayer1Chips(player.getChips());
					state.setPlayer2Chips(opponent.getChips());
				}
				else{
					state.setPlayer1Chips(opponent.getChips());
					state.setPlayer2Chips(player.getChips());
				}
				state.setBet(bet);
				state.setPot(pot);
				try {
					clientOutput.writeObject(state);
					clientOutput.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(src == showdownButton){
				determineWinner();
				collectPot();
				showWinnerAlert();
			}
		}
	}
}
