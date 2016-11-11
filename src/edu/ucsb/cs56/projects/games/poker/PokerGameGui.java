package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import java.util.ArrayList;

public class PokerGameGui extends PokerGame{
    
    
    
    protected JFrame
	mainFrame, gameOverFrame;
    
    protected JTextField
        betTextField;
    
    protected JButton
        foldButton, betButton, checkButton, callButton, showdownButton, rulesButton,  // mainFrame
	overviewRulesButton, gameplayRulesButton1, gameplayRulesButton2, exampleRulesButton, //Rules Panel
	gameOverButton; // gameOverFrame
    
    protected JLabel
	rulesExampleLabel, rulesOverviewLabel,
	rulesGameplay1Label, rulesGameplay2Label,
        playerWinsLabel, opponentWinsLabel, // Possibly don't need these
	playerChipsLabel, opponentChipsLabel, 
	potLabel, gameMessage, playerPrompt,  // Possibly don't need these
	backCardLabel1, backCardLabel2, // Not sure what these are used for
	gameOverLabel; // gameOverFrame
    
    protected JPanel
	rulesPanel, rulesNextPageButtons, 
	opponentPanel, playerPanel,
	centerPanel, messagePanel, optionArea,
	oSubPane1, oSubPane2, oSubPane3,
	pSubPane1, pSubPane2, pSubPane3,
        flopPane, turnPane, riverPane, betPane,
	gameOverMessage, gameOverPanel, gameOverButtonPanel; // gameOverFrame
    
    public PokerGameGui(){
	super();		 
    }
    
    public void layoutSubViews() {
        if (!gameOver) {
            Color pokerGreen = new Color(83, 157, 89);
	    
            foldButton = new JButton("FOLD");
            foldButton.setEnabled(false);
            foldButton.addActionListener(new foldButtonHandler());
	    betButton = new JButton("BET");
            betButton.setEnabled(false);
	    betButton.addActionListener(new betButtonHandler());
            betTextField = new JTextField(4);
            checkButton = new JButton("CHECK");
            checkButton.setEnabled(false);
	    checkButton.addActionListener(new checkButtonHandler());
            callButton = new JButton("CALL");
            callButton.setEnabled(false);
	    callButton.addActionListener(new callButtonHandler());
            showdownButton = new JButton("SHOWDOWN");
	    showdownButton.addActionListener(new showdownButtonHandler());
	    
	    /*putting the rules pictures into the game without adding a new window */
	    
	    rulesButton = new JButton("RULES");
	    rulesButton.setEnabled(true);
	    rulesButton.addActionListener(new rulesButtonHandler());
	    rulesPanel = new JPanel();
	    
	    rulesPanel.setLayout(new BorderLayout());
	    rulesOverviewLabel = new JLabel();
	    rulesGameplay1Label = new JLabel();
	    rulesGameplay2Label = new JLabel();
	    rulesExampleLabel = new JLabel();
	    rulesOverviewLabel.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesOverview.png"));
	    rulesGameplay1Label.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesGamePlay1.png"));
	    rulesGameplay2Label.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesGamePlay2.png"));
	    rulesExampleLabel.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesExamples.png"));
	    
	    rulesPanel.add(BorderLayout.CENTER, rulesOverviewLabel);
	    
	    overviewRulesButton = new JButton("Overview");
	    overviewRulesButton.setEnabled(true);
	    overviewRulesButton.addActionListener(new overviewButtonHandler() );
	    gameplayRulesButton1 = new JButton("Rules");
	    gameplayRulesButton1.setEnabled(true);
	    gameplayRulesButton1.addActionListener(new gameplayButton1Handler() );
	    gameplayRulesButton2 = new JButton("Rules(cont.)");
	    gameplayRulesButton2.setEnabled(true);
	    gameplayRulesButton2.addActionListener(new gameplayButton2Handler() );
	    exampleRulesButton = new JButton("Example Hands");
	    exampleRulesButton.setEnabled(true);
	    exampleRulesButton.addActionListener(new exampleButtonHandler() );
	    
	    rulesNextPageButtons = new JPanel();
	    rulesNextPageButtons.setLayout(new BoxLayout(rulesNextPageButtons, BoxLayout.X_AXIS));
	    rulesNextPageButtons.add(overviewRulesButton);
	    rulesNextPageButtons.add(gameplayRulesButton1);
	    rulesNextPageButtons.add(gameplayRulesButton2);
	    rulesNextPageButtons.add(exampleRulesButton);
	    rulesNextPageButtons.setBackground(pokerGreen);
	    
	    rulesPanel.add(BorderLayout.SOUTH, rulesNextPageButtons);
	    rulesPanel.setBackground(pokerGreen);
	    rulesPanel.setVisible(false);
	    
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
	    optionArea.add(rulesButton);

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
                pSubPane2.add(new JLabel(getCardImage((player.getHand()).get(i))));
            }

	    centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
            flopPane = new JPanel();
            flopPane.add(new JLabel("Flop:"));

	    for (int i = 0; i < 3; i++) {
                flopPane.add(new JLabel(getCardImage((table.getFlopCards()).get(i))));
            }
            flopPane.setVisible(false);
            
	    turnPane = new JPanel();
            turnPane.add(new JLabel("Turn:"));
            turnPane.add(new JLabel(getCardImage(table.getTurnCard())));
            turnPane.setVisible(false);
            riverPane = new JPanel();
            riverPane.add(new JLabel("River:"));
            riverPane.add(new JLabel(getCardImage(table.getRiverCard())));
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
            mainFrame.setSize(new Dimension(1000, 600));
	    mainFrame.setLayout(new BorderLayout() );
            mainFrame.setResizable(false);
            mainFrame.setLocation(250, 250);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().add(BorderLayout.NORTH, opponentPanel);
            mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
            mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
            mainFrame.getContentPane().add(BorderLayout.EAST, messagePanel);
	    mainFrame.getContentPane().add(BorderLayout.WEST, rulesPanel);
	    mainFrame.setVisible(true);
	}
    }

    /**
     * Method that updates the panels in the frame based on step
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
     * Enables and disables buttons on the screen depending on turn and step
     */
    protected void controlButtons() {
        if (step == Step.SHOWDOWN) {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(false);
            foldButton.setEnabled(false);
            showdownButton.setVisible(true);
	    rulesButton.setEnabled(true);
        }
        else if (turn == Turn.PLAYER && responding) {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(true);
            foldButton.setEnabled(true);
	    rulesButton.setEnabled(true);
        } else if (turn == Turn.PLAYER) {
            betButton.setEnabled(true);
            betTextField.setEnabled(true);
            checkButton.setEnabled(true);
            callButton.setEnabled(false);
            foldButton.setEnabled(true);
	    rulesButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(false);
            foldButton.setEnabled(false);
	    rulesButton.setEnabled(true);
        }
	updateFrame();
    }


    /**
     * Inner class that handles the betButton using ActionListener
     */
    protected class betButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    String inputText = betTextField.getText();
	    if (!inputText.equals("")) {
		bet = Integer.parseInt(inputText);
		if (bet<=0) {
		    prompt = "Enter a valid bet!";
		    updateFrame();
		}
		else if ((player.getChips()-bet>=0) && (opponent.getChips()-bet>=0)) {
		    betTextField.setText("");
		    pot += bet;
		    player.bet(bet);
		    message = "Opponent waiting for turn.";
		    prompt = "Player bets " + bet + ".";
		    betButton.setEnabled(false);
		    betTextField.setEnabled(false);
		    checkButton.setEnabled(false);
		    callButton.setEnabled(false);
		    foldButton.setEnabled(false);
		    responding = true;
		    checkPassTurnUpdate();
		    updateFrame();
		}
		else {
		    prompt = "Not enough chips!";
		    updateFrame();
		}
	    }
	    else {
		prompt = "Enter a number of chips to bet!";
		updateFrame();
	    }
	}
    }

    /**
     * Inner class that handles the checkButton using ActionListener
     */
    protected class checkButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
	    bet = 0;
	    message = "Opponent waiting to deal.";
	    prompt = "Player checks.";
	    betButton.setEnabled(false);
	    betTextField.setEnabled(false);
	    checkButton.setEnabled(false);
	    callButton.setEnabled(false);
	    foldButton.setEnabled(false);
	    rulesButton.setEnabled(true);
	    checkPassTurnUpdate();
	    updateFrame();
	}
    }

    /**
     * Inner class that handles the foldButton using ActionListener
     */
    protected class foldButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
	    message = "Opponent waiting for turn.";
	    prompt = "You fold.";
	    player.foldHand();
	}
    }

    /**
     * Inner class that handles the callButton using ActionListener
     */
    protected class callButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
	    pot += bet;
	    player.bet(bet);
	    message = "You call.";
	    prompt = "Next turn: ";
	    responding = false;
	    callButton.setEnabled(false);
	    foldButton.setEnabled(false);
	    changeTurn();
	    updateFrame();
	}
    }

    /**
     * Inner class that handles the showdownButton using ActionListener
     */
    protected class showdownButtonHandler implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    determineWinner();
	    collectPot();
	    showWinnerAlert();
	}
    }

    protected class rulesButtonHandler implements ActionListener {// rules
	public void actionPerformed(ActionEvent e) {
	    rulesPanel.setVisible(!rulesPanel.isVisible() );
	}
    }

    protected class overviewButtonHandler implements ActionListener {// rules
	public void actionPerformed(ActionEvent e) {
	 
	    rulesPanel.remove(rulesGameplay1Label);
	    rulesPanel.remove(rulesGameplay2Label);
	    rulesPanel.remove(rulesOverviewLabel);
	    rulesPanel.remove(rulesExampleLabel);
	    rulesPanel.add(BorderLayout.CENTER, rulesOverviewLabel);
	    rulesPanel.revalidate();
	    rulesPanel.repaint();
	}
    }

      protected class gameplayButton1Handler implements ActionListener {// rules
	public void actionPerformed(ActionEvent e) {	    
	 
	    rulesPanel.remove(rulesGameplay1Label);
	    rulesPanel.remove(rulesGameplay2Label);
	    rulesPanel.remove(rulesOverviewLabel);
	    rulesPanel.remove(rulesExampleLabel);
	    rulesPanel.add(BorderLayout.CENTER, rulesGameplay1Label);
	    rulesPanel.revalidate();
	    rulesPanel.repaint();
	}
    }
    protected class gameplayButton2Handler implements ActionListener {// rules
	public void actionPerformed(ActionEvent e) {	    
	 
	    rulesPanel.remove(rulesGameplay1Label);
	    rulesPanel.remove(rulesGameplay2Label);
	    rulesPanel.remove(rulesOverviewLabel);
	    rulesPanel.remove(rulesExampleLabel);
	    rulesPanel.add(BorderLayout.CENTER, rulesGameplay2Label);
	    rulesPanel.revalidate();
	    rulesPanel.repaint();
	}
    }

      protected class exampleButtonHandler implements ActionListener {// rules
	public void actionPerformed(ActionEvent e) {	 
	    
	    rulesPanel.remove(rulesGameplay1Label);
	    rulesPanel.remove(rulesGameplay2Label);
	    rulesPanel.remove(rulesOverviewLabel);
	    rulesPanel.remove(rulesExampleLabel);
	    rulesPanel.add(BorderLayout.CENTER, rulesExampleLabel);
	    rulesPanel.revalidate();
	    rulesPanel.repaint();
	}
    }

    /**
     * Function that puts up a Game Over Frame that can take us back to the Main Screen
     */
    protected void gameOver(String label) {
        gameOverFrame = new JFrame();
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setBackground(Color.red);

        gameOverMessage = new JPanel();
        gameOverMessage.setBackground(Color.red);

        gameOverButtonPanel = new JPanel();
        gameOverButtonPanel.setBackground(Color.red);

        gameOverLabel = new JLabel(label);
        gameOverButton = new JButton("Back to Main Menu");
        gameOverButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    gameOverFrame.setVisible(false);
                    PokerMain restart = new PokerMain();
                    restart.go();
                }
            });


        gameOverMessage.add(gameOverLabel);
        gameOverButtonPanel.add(gameOverButton);

        gameOverFrame.setSize(300, 200);
        gameOverFrame.setResizable(false);
        gameOverFrame.setLocation(250, 250);
        gameOverFrame.getContentPane().add(BorderLayout.NORTH, gameOverMessage);
        gameOverFrame.getContentPane().add(BorderLayout.SOUTH, gameOverButtonPanel);
        gameOverFrame.pack();
        gameOverFrame.setVisible(true);
        mainFrame.dispose();
    }
}
