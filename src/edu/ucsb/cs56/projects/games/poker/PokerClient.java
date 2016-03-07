package edu.ucsb.cs56.projects.games.poker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.ucsb.cs56.projects.games.poker.PokerGame.Turn;
import edu.ucsb.cs56.projects.games.poker.PokerGame.Winner;

public class PokerClient extends PokerGame {
	private Socket sock;
	ObjectOutputStream clientOutput;
	ObjectInputStream clientInput;
	PokerGameState state;
	String address;
	Thread listener;
	int playerNumber;
	boolean clientRoundOver = false;

	public static void main(String[] args) {
		PokerClient client = new PokerClient();
		try {
			client.go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setAddress(String address){
	this.address = address;
	}
	public void go() throws IOException {
		setUpNetworking();
		try {
			System.out.println("You connected.");
			state = new PokerGameState();
			clientInput = new ObjectInputStream(sock.getInputStream());
			clientOutput = new ObjectOutputStream(sock.getOutputStream());
			
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
			sock = new Socket(address, 15000);
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
	
	public void changeTurn() {
		if(playerNumber == 1){
			if(turn == Turn.PLAYER){
				turn = Turn.OPPONENT;
				controlButtons();
			}
			else {
				turn = Turn.PLAYER;
				controlButtons();
			}
		}
		else if(playerNumber == 2){
			if (turn == Turn.PLAYER) {
				if (responding == true) {
					turn = Turn.OPPONENT;
					controlButtons();
				} else {
					nextStep();
					turn = Turn.OPPONENT;
					controlButtons();
				}
			} else if (turn == Turn.OPPONENT) {
				turn = Turn.PLAYER;
				controlButtons();
			}
		}
		
	}
	
	public void showWinnerAlert() {
		if(!gameOver){
		    String message = "";
		    oSubPane2.remove(backCardLabel1);
		    oSubPane2.remove(backCardLabel2);
		    for(int i=0;i<2;i++){
			oSubPane2.add(new JLabel(getCardImage(opponent.getCardFromHand(i))));
		    }
		    updateFrame();
		    if (winnerType == Winner.PLAYER) {
	            System.out.println("player");
	            message = "You won! \n\n Next round?";
		    } else if (winnerType == Winner.OPPONENT) {
			System.out.println("opponent");
			message = "opponent won. \n\n Next round?";
		    } else if (winnerType == Winner.TIE){
	            System.out.println("tie");
	            message = "Tie \n\n Next round?";
		    }
		    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
							       JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				try {
					clientOutput.writeObject("continue");
					clientOutput.reset();
					System.out.println("Waiting on opponent to continue");
					state = (PokerGameState) clientInput.readObject();
					System.out.println("Creating new round.");
					mainFrame.dispose();
					playerSetUp();
					layoutSubViews();
					if(playerNumber == 1)
						turn = Turn.PLAYER;
					else
						turn = Turn.OPPONENT;
					step = Step.BLIND;
					controlButtons();
					updateFrame();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			} else {
				// Quit
				System.exit(1);
			}
		}		
	}
	
	public class InputReader implements Runnable {
		
		public void run () {
			try {
				while (true) {
					if (playerNumber == 1 && state.getStep() == 4)
						break;
					System.out.println("waiting");
					state = (PokerGameState) clientInput.readObject();
					System.out.println("Read from server.");
					System.out.println("New turn: " + state.getTurn());
					System.out.println("New step: " + state.getStep());
					if (state != null) {
						int stateStep = state.getStep();
						int stateTurn = state.getTurn();
						responding = state.getRespond();
						if (!state.getRoundOver()) {
							if (playerNumber == 1) {
								if (stateStep == 0 && stateTurn == 1 && !responding) {
									System.out.println("Starting game");
									playerSetUp();
									layoutSubViews();
									if (!gameOver) {
										turn = Turn.PLAYER;
										step = Step.BLIND;
										controlButtons();
										updateFrame();
									}
								} else if (stateStep == 4) {
									step = Step.SHOWDOWN;
									controlButtons();
									updateFrame();
								} else {
									switch (stateStep) {
									case 1:
										step = Step.FLOP;
										break;
									case 2:
										step = Step.TURN;
										break;
									case 3:
										step = Step.RIVER;
										break;

									}
									player.setChips(state.getPlayer1Chips());
									opponent.setChips(state.getPlayer2Chips());
									bet = state.getBet();
									pot = state.getPot();
									if (!state.getJustUpdate()) {
										changeTurn();
									}
									updateFrame();
									state.setJustUpdate(false);
								}
							} else if (playerNumber == 2) {
								if (stateStep == 0 && stateTurn == 1) {
									System.out.println("Starting game");
									playerSetUp();
									layoutSubViews();
									if (!gameOver) {
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
									System.out.println("Just update: " + state.getJustUpdate());
									switch (stateStep) {
									case 1:
										step = Step.FLOP;
										break;
									case 2:
										step = Step.TURN;
										break;
									case 3:
										step = Step.RIVER;
										break;

									}
									player.setChips(state.getPlayer2Chips());
									opponent.setChips(state.getPlayer1Chips());
									bet = state.getBet();
									pot = state.getPot();
									if (!state.getJustUpdate()) {
										System.out.println("hehe");
										changeTurn();
									}
									updateFrame();
									state.setJustUpdate(false);
								}
							}
						} else {
							if (playerNumber == 1) {
								if (state.getWinner() == 1)
									winnerType = Winner.PLAYER;
								else if (state.getWinner() == 2)
									winnerType = Winner.OPPONENT;
								else
									winnerType = Winner.TIE;
								player.setChips(state.getPlayer1Chips());
								opponent.setChips(state.getPlayer2Chips());
								showWinnerAlert();
							} else {
								if (state.getWinner() == 2)
									winnerType = Winner.PLAYER;
								else if (state.getWinner() == 1)
									winnerType = Winner.OPPONENT;
								else
									winnerType = Winner.TIE;
								player.setChips(state.getPlayer2Chips());
								opponent.setChips(state.getPlayer1Chips());
								showWinnerAlert();
							}
						}
					}

						
					
					clientOutput.reset();
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
				state.setJustUpdate(false);
				System.out.println("Are we just updating? : " + state.getJustUpdate());
				if(!state.getJustUpdate()){
					changeTurn();
					state.changeTurn();
				}
				state.setRespond(responding);
				if(playerNumber == 1){
					state.setPlayer1Chips(player.getChips());
					state.setPlayer2Chips(opponent.getChips());
				}
				else{
					state.setPlayer1Chips(opponent.getChips());
					state.setPlayer2Chips(player.getChips());
					if(!responding){
						state.nextStep();
						updateFrame();
					}
				}
				state.setBet(bet);
				state.setPot(pot);
				try {
					System.out.println("Writing Turn: " + state.getTurn());
					System.out.println("Writing Step: " + state.getStep());
					clientOutput.writeObject(state);
					clientOutput.reset();
					System.out.println("Wrote to server.");
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
				if (playerNumber == 1) {
					nextStep();
					updateFrame();
					controlButtons();
					state.nextStep();
					state.setRespond(responding);
					state.setPlayer1Chips(player.getChips());
					state.setPlayer2Chips(opponent.getChips());
					state.setBet(bet);
					state.setPot(pot);
					state.setJustUpdate(true);
					try {
						System.out.println("Writing Turn: " + state.getTurn());
						System.out.println("Writing Step: " + state.getStep());
						clientOutput.writeObject(state);
						System.out.println("Wrote to server. (call)");
						clientOutput.reset();
					} catch (IOException e) {
						e.printStackTrace();
					}
					state.setJustUpdate(false);
				}
				else 
					passTurnButton.setEnabled(true);
			}
			else if(src == showdownButton){
				determineWinner();
				collectPot();
				if(winnerType == Winner.PLAYER) { 
					state.setWinner(1);
				}
				else if (winnerType == Winner.TIE){
					state.setWinner(0);
				}
				else
					state.setWinner(2);
				state.setRoundOver(true);
				clientRoundOver = true;
				state.setPlayer1Chips(player.getChips());
				state.setPlayer2Chips(opponent.getChips());
				state.setBet(bet);
				state.setPot(pot);
				state.setJustUpdate(true);
				try {
					System.out.println("Winner: " + state.getWinner());
					System.out.println("Round over: " + state.getRoundOver());
					clientOutput.writeObject(state);
					System.out.println("Wrote to server. (over)");
					clientOutput.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}
				showWinnerAlert();
			}
		}
	}
}
