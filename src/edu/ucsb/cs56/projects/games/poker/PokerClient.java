package edu.ucsb.cs56.projects.games.poker;

//
import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Random;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.ucsb.cs56.projects.games.poker.PokerGame.Turn;
import edu.ucsb.cs56.projects.games.poker.PokerGame.Winner;

public class PokerClient extends PokerGame {
    protected JButton chatButton;

    private Socket sock;
    ObjectOutputStream clientOutput;
    ObjectInputStream clientInput;
    PokerGameState state;
    String address;
    Thread listener;
    int playerNumber;
    boolean clientRoundOver = false;
    String serverMessage;
    String [] descriptions = {"chilling", "sweating", "cool", "emotionless", 
			      "panicking", "clearly worried", "not even worried",
			      "smiling", "waiting"};

    public static void main(String[] args) {
	PokerClient client = new PokerClient();
	try {
	    client.go();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
	
    /**
     * Set the address of the server.
     * @param address 
     */
    public void setAddress(String address){
	this.address = address;
    }

    /**
     * Set up connection with the server and initialize thread
     * to read and write to server.
     * @throws IOException
     */
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

    /**
     * Attempt to connect to the server.
     */
    public void setUpNetworking() {
	try {
	    sock = new Socket(address, 15000);
	    System.out.println("Player connected");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
	
    /**
     * Receive cards dealt from the server's states and
     * prepare variables for game start.
     */
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
	    System.exit(1);
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
	
    /**
     * Adds action listeners to the buttons on the GUI.
     * Overridden to allow for new ActionListener class for client.
     */
    public void addActionListeners() {
	ClientButtonHandler b = new ClientButtonHandler();
	foldButton.addActionListener(b);
	betButton.addActionListener(b);
	checkButton.addActionListener(b);
	callButton.addActionListener(b);
	showdownButton.addActionListener(b);
	chatButton.addActionListener(b);
    }
	
    /**
     * Change turns from player to opponent, or vice versa.
     * Activates/deactivates buttons appropriate to the 
     * correct turn and step.
     */
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
	
    /**
     * Fold the round and prompt for next round.
     */
    public void fold () {
	if(playerNumber == 1){
	    state.setWinner(2);
	} else {
	    state.setWinner(1);
	}
	winnerType = Winner.OPPONENT;
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
	collectPot();
	showWinnerAlert();
    }
	
    /**
     * Reveal opponent's cards, alert user to the winner and
     * prompt for next round.
     */
    public void showWinnerAlert() {
	if(!gameOver){
	    message = "";
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
		    mainFrame.dispose();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		// Quit
		System.exit(1);
	    }
	}		
    }
	
    /**
     * Runnable class for client's thread to continuously read from the server
     * and update appropriately based on the state read in.
     */
    public class InputReader implements Runnable {
	
	public void run () {
	    try {
		while (true) {
		    if (state != null) {
			System.out.println("Before read: Turn : " + state.getTurn());
			System.out.println("Step: " + state.getStep());
			
			System.out.println("waiting");
			state = (PokerGameState) clientInput.readObject();
			System.out.println("Read from server.");
			System.out.println("New turn: " + state.getTurn());
			System.out.println("New step: " + state.getStep());
			
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
					message = "Player 2 is " + getRandomDescription() + ".";
					prompt = "You go first.\n What will you do?";
					controlButtons();
					updateFrame();
				    }
				} else if (stateStep == 4) {
				    step = Step.SHOWDOWN;
				    message = "Player 2 is " + getRandomDescription() + ".";
				    prompt = "Determine winner: ";
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
					message = state.getServerMessage();
					prompt = "Your turn. What will you do?";
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
				    updateFrame();
				    state.changeTurn();
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
				    message = state.getServerMessage();
				    if (!state.getJustUpdate()) {
					prompt = "Your turn. What will you do?";
					changeTurn();
				    }
				    updateFrame();
				    showdownButton.setEnabled(false);
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
			clientOutput.reset();
		    }
		    
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
    }
    
    /**
     * Inner class for handling all buttons on the GUI.
     * Certain buttons write back to the server to update
     * the server on what the user has changed.
     */
    public class ClientButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    Object src = event.getSource();
	    if(src == chatButton){ // FIXED TO CREATE CHAT SERVER SCREEN USING SAME IP ADDRESS
		System.out.println("We're Chit Chatting");
		PokerChatClient chitchat = new PokerChatClient();
		if (address != null) {
		    chitchat.setAddress(address);
		    chitchat.go();
		    chatButton.setEnabled(false);
		}
	    }
	    else if(src == betButton) {
		String inputText = betTextField.getText();
		if(!inputText.equals("")){
		    bet = Integer.parseInt(inputText);
		    if ((player.getChips()-bet>=0) && (opponent.getChips()-bet>=0) && (bet>0)) {
			betTextField.setText("");
			pot += bet;
			player.setChips(player.getChips() - player.bet(bet));
			chatButton.setEnabled(true);
			betButton.setEnabled(false);
			betTextField.setEnabled(false);
			checkButton.setEnabled(false);
			callButton.setEnabled(false);
			foldButton.setEnabled(false);
			responding = true;
			prompt = "Allow response: ";
			message = "You bet " + bet + " chips.";
			serverMessage = "Player " + playerNumber + " bets " + bet + " chips.";
			updateFrame();
		    } else {
			prompt = "Not enough chips!";
			updateFrame();
		    }
		} else {
		    prompt = "Enter a number of chips to bet!";
		    updateFrame();
		}
		
		// This state is for passing the turn
		state.setJustUpdate(false);
		if(!state.getJustUpdate()){
		    changeTurn();
		    state.changeTurn();
		}
		state.setRespond(responding);
		if(playerNumber == 1){
		    state.setPlayer1Chips(player.getChips());
		    state.setPlayer2Chips(opponent.getChips());
		    prompt = "Player 2 turn.";
		    message = "Player 2 is " + getRandomDescription() + ".";
		    updateFrame();
		}
		else{
		    state.setPlayer1Chips(opponent.getChips());
		    state.setPlayer2Chips(player.getChips());
		    if(!responding){
			state.nextStep();
			updateFrame();
		    }
		    if( !(state.getStep() == 4) ) {
			prompt = "Player 1 turn.";
			message = "Player 1 is " + getRandomDescription() + ".";
			updateFrame();
		    }	
		}
		state.setBet(bet);
		state.setPot(pot);
		state.setServerMessage(serverMessage);
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
	    else if(src == checkButton) {
		bet = 0;
		chatButton.setEnabled(true);
		betButton.setEnabled(false);
		betTextField.setEnabled(false);
		checkButton.setEnabled(false);
		callButton.setEnabled(false);
		foldButton.setEnabled(false);
		message = "You check.";
		if(playerNumber == 1)
		    prompt = "Pass turn: ";
		else
		    prompt = "Deal cards: ";
		serverMessage = "Player " + playerNumber + " checks."; 
		updateFrame();
		
		//This state is for passing the turn
		state.setJustUpdate(false);
		if(!state.getJustUpdate()){
		    changeTurn();
		    state.changeTurn();
		}
		state.setRespond(responding);
		if(playerNumber == 1){
		    state.setPlayer1Chips(player.getChips());
		    state.setPlayer2Chips(opponent.getChips());
		    prompt = "Player 2 turn.";
		    message = "Player 2 is " + getRandomDescription() + ".";
		    updateFrame();
		}
		else{
		    state.setPlayer1Chips(opponent.getChips());
		    state.setPlayer2Chips(player.getChips());
		    if(!responding){
			state.nextStep();
			updateFrame();
		    }
		    if( !(state.getStep() == 4) ) {
			prompt = "Player 1 turn.";
			message = "Player 1 is " + getRandomDescription() + ".";
			updateFrame();
		    }	
		}
		state.setBet(bet);
		state.setPot(pot);
		state.setServerMessage(serverMessage);
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
	    else if(src == foldButton){
		player.foldHand();
	    }
	    else if(src == callButton){
		pot += bet;
		player.setChips(player.getChips() - player.bet(bet));
		responding = false;
		callButton.setEnabled(false);
		foldButton.setEnabled(false);
		message = "You call.";
		prompt = "Next turn: ";
		serverMessage = "Player" + playerNumber + " calls.";
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
		    state.setServerMessage(serverMessage);
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
		    chatButton.setEnabled(true);
		
		// This state is for passing on the turn
		state.setJustUpdate(false);
		if(!state.getJustUpdate()){
		    changeTurn();
		    state.changeTurn();
		}
		state.setRespond(responding);
		if(playerNumber == 1){
		    state.setPlayer1Chips(player.getChips());
		    state.setPlayer2Chips(opponent.getChips());
		    prompt = "Player 2 turn.";
		    message = "Player 2 is " + getRandomDescription() + ".";
		    updateFrame();
		}
		else{
		    state.setPlayer1Chips(opponent.getChips());
		    state.setPlayer2Chips(player.getChips());
		    if(!responding){
			state.nextStep();
			updateFrame();
		    }
		    if( !(state.getStep() == 4) ) {
			prompt = "Player 1 turn.";
			message = "Player 1 is " + getRandomDescription() + ".";
			updateFrame();
		    }	
		}
		state.setBet(bet);
		state.setPot(pot);
		state.setServerMessage(serverMessage);
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
    
    /**
     * Creates random descriptions for interesting server messages.
     * @return String
     */
    public String getRandomDescription (){
	int index = new Random().nextInt(descriptions.length);
	return (descriptions[index]);
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
	    // addActionListeners();

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

//////////////////////////////////////////////////////
	    
	    chatButton = new JButton("CHAT");
            chatButton.setEnabled(true);
            addActionListeners();

	    messagePanel.add(chatButton);
            messagePanel.add(Box.createRigidArea(new Dimension(0, 20)));
	    
	    mainFrame.validate();
	    mainFrame.repaint();
	}
    }

    /**
     *  Enables and disables buttons one the screen, depending
     *  on the turn and step.
     */
    public void controlButtons() {
	chatButton.setEnabled(true);
	super.controlButtons();
    }
}
