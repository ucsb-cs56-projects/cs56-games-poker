package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import java.util.ArrayList;

public class PokerGameGui extends PokerGameMult{

    /**
     * The main frame for running the game
     */
    protected JFrame mainFrame;

    /**
     * The frame used for when the game is over
     */
    protected JFrame gameOverFrame;

    // mainFrame

    /**
     * Text field for entering bet
     */
    protected JTextField betTextField;

    /**
     * Button to fold
     */
    protected JButton foldButton;

    /**
     * Button to bet
     */
    protected JButton betButton;

    /**
     * Button to check
     */
    protected JButton checkButton;

    /**
     * Button to call
     */
    protected JButton callButton;

    /**
     * Button for showdown
     */
    protected JButton showdownButton;

    /**
     * Button to show rules
     */
    protected JButton rulesButton;

    // Rules Panel

    /**
     * Button to view general overview of Texas Holdem
     */
	  protected JButton overviewRulesButton;

    /**
     * Button to view information about basic gameplay in each round
     * Ex: bet, call raise, etc
     */
    protected JButton gameplayRulesButton1;

    /**
     * Button to view information about each round of the game (flop, turn, showdown)
     */
    protected JButton gameplayRulesButton2;

    /**
     * Button to view example hands
     */
    protected JButton exampleRulesButton;

    // gameOverFrame

    /**
     * Button to return to main menu when game is over
     */
	  protected JButton gameOverButton; // gameOverFrame

    /**
     * Image of example poker hands
     */
    protected JLabel rulesExampleLabel;

    /**
     * Image of general overview of Texas Holdem
     */
    protected JLabel rulesOverviewLabel;

    /**
     * Image with information about basic gameplay in each round
     */
    protected JLabel rulesGameplay1Label;

    /**
     * Image with information about each round of the game
     */
    protected JLabel rulesGameplay2Label;

    /**
     * Displays player's winnings if player wins
     */
    protected JLabel playerWinsLabel;

    /**
     * Displays opponent's winnings if opponent wins
     */
    protected JLabel opponent1WinsLabel;
    protected JLabel opponent2WinsLabel;
    protected JLabel opponent3WinsLabel;

    /**
     * Displays player's chips
     */
    protected JLabel playerChipsLabel;

    /**
     * Displays opponent's chips
     */
    protected JLabel opponent1ChipsLabel;
    protected JLabel opponent2ChipsLabel;
    protected JLabel opponent3ChipsLabel;

    /**
     * Displays current pot
     */
    protected JLabel potLabel;

    /**
     * Displays message about current game state
     */
    protected JLabel gameMessage;

    /**
     * Displays instructions for player
     */
    protected JLabel playerPrompt;

    /**
     * Image of back of card
     */
    protected JLabel backCardLabel1, backCardLabel2, backCardLabel3, backCardLabel4, backCardLabel5, backCardLabel6;

    /**
     * Displays message about who lost the game
     */
    protected JLabel gameOverLabel; // gameOverFrame

    /**
     * Panel for displaying rules
     */
    protected JOptionPane Rules;
    protected JPanel rulesPanel;

    /**
     * Panel for buttons for navigating rules
     */
    protected JPanel rulesNextPageButtons;

    /**
     * Panel for opponent game information
     */
	protected JPanel opponentPanel;

    /**
     * Panel for player game information
     */
    protected JPanel playerPanel;

    /**
     * Panel for displaying cards
     */
	protected JPanel centerPanel;

    /**
     * Panel for displaying game message, current pot
     */
    protected JPanel messagePanel;

    protected JPanel winnerPanel;

    protected JPanel wSubPanel;
    /**
     * Panel holding the various game action buttons (bet, check, fold, etc)
     */
    protected JPanel optionArea;

    protected JPanel oSubPane1;
    protected JPanel oSubPane2;
    protected JPanel oSubPane3;

    /**
     * Panel displaying player's chips
     */
	  protected JPanel pSubPane1;

    /**
     * Panel where the player's cards are displayed
     */
    protected JPanel pSubPane2;

    /**
     * Panel where possible game actions are displayed (holds JPanel optionArea)
     */
    protected JPanel pSubPane3;

    /**
     * Panel displaying flop cards
     */
    protected JPanel flopPane;

    /**
     * Panel displaying turn card
     */
    protected JPanel turnPane;

    /**
     * Panel displaying river card
     */
    protected JPanel riverPane;

    // gameOverFrame

    /**
     * Panel displaying gameover message
     */
	protected JPanel gameOverMessage;

    /**
     * Panel displaying "Back to Main Menu" button
     */
    protected JPanel gameOverButtonPanel;

    protected boolean multiPlayer = false;

    /**
     * No arg constructor for PokerGameGui that simply calls the superclass constructor
     */
     // Default: Single Player
    public PokerGameGui(){
        super();
    }
    // MultiPlayer
    // if mode == true, singlePlayer
    // if mode == false, multiPlayer
    public PokerGameGui(boolean mode) {
      super(mode);
      multiPlayer = !mode;
    }

    public PokerGameGui(int totalplayers) {
	    super(totalplayers);
    }

    /**
     * Initial GUI setup
     * Instantiates the relevant JComponent objects (which is most of them) to their initial states
     */
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


            rulesButton = new JButton("RULES");
            rulesButton.setEnabled(true);
            rulesButton.addActionListener(new rulesButtonHandler());
            /**
            * SWAPPED OUT FOR POPUP WINDOW OPTION
            * maybe better as a popup scroll window

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
            */


            opponentPanel = new JPanel();
            opponentPanel.setLayout(new BorderLayout());

            oSubPane1 = new JPanel();
            oSubPane2 = new JPanel();
            oSubPane3 = new JPanel();

            opponentPanel.add(BorderLayout.WEST, oSubPane1);
            opponentPanel.add(BorderLayout.CENTER, oSubPane2);
            opponentPanel.add(BorderLayout.EAST,oSubPane3);


            opponent1ChipsLabel = new JLabel(String.format("Chips: %d", players.get(1).getChips()));
            opponent1WinsLabel = new JLabel();
            opponent1WinsLabel.setText(String.format("Opponent 1 wins: %d", players.get(1).getWins()));

            if (multiPlayer == true)
            {
              opponent2ChipsLabel = new JLabel(String.format("Chips: %d", players.get(2).getChips()));
              opponent2WinsLabel = new JLabel();
              opponent2WinsLabel.setText(String.format("Opponent 2 wins: %d", players.get(2).getWins()));
              opponent3ChipsLabel = new JLabel(String.format("Chips: %d", players.get(3).getChips()));
              opponent3WinsLabel = new JLabel();
              opponent3WinsLabel.setText(String.format("Opponent 3 wins: %d", players.get(3).getWins()));

            }

            playerWinsLabel = new JLabel();
            playerWinsLabel.setText(String.format("Player wins: %d", players.get(0).getWins()));

            JLabel newline = new JLabel(" ");
            JLabel newline1 = new JLabel(" ");
            JLabel newline2 = new JLabel(" ");
            JLabel newline3 = new JLabel(" ");

            winnerPanel = new JPanel();
            wSubPanel = new JPanel();
            winnerPanel.setLayout(new BorderLayout());
            wSubPanel.setLayout(new BoxLayout(wSubPanel, BoxLayout.Y_AXIS));

            wSubPanel.add(playerWinsLabel);
            wSubPanel.add(newline1);
            wSubPanel.add(opponent1WinsLabel);
            wSubPanel.add(newline2);

            if (multiPlayer == true)
            {
              wSubPanel.add(opponent2WinsLabel);
              wSubPanel.add(newline3);
              wSubPanel.add(opponent3WinsLabel);
              wSubPanel.add(newline);
            }

            wSubPanel.setBackground(pokerGreen);

            winnerPanel.add(BorderLayout.SOUTH, wSubPanel);


            optionArea = new JPanel();
            optionArea.setLayout(new BoxLayout(optionArea, BoxLayout.Y_AXIS));
            optionArea.add(betButton);
            optionArea.add(betTextField);
            optionArea.add(callButton);
            optionArea.add(checkButton);
            optionArea.add(foldButton);
            optionArea.add(rulesButton);

            backCardLabel1 = new JLabel(backCardImage);
            backCardLabel2 = new JLabel(backCardImage);
            backCardLabel3 = new JLabel(backCardImage);
            backCardLabel4 = new JLabel(backCardImage);
            backCardLabel5 = new JLabel(backCardImage);
            backCardLabel6 = new JLabel(backCardImage);

            playerPanel = new JPanel();
            playerPanel.setLayout(new BorderLayout());
            pSubPane1 = new JPanel();
            pSubPane1.setLayout(new BoxLayout(pSubPane1, BoxLayout.Y_AXIS));
            pSubPane2 = new JPanel();
            pSubPane3 = new JPanel();

            JLabel playername = new JLabel("PLAYER");
            playerChipsLabel = new JLabel(String.format("Chips: %d", players.get(0).getChips()));


            pSubPane3.add(optionArea);
            // message area
            playerPanel.add(BorderLayout.WEST, pSubPane1);
            playerPanel.add(BorderLayout.CENTER, pSubPane2);
            playerPanel.add(BorderLayout.EAST, pSubPane3);

            JLabel opponent2name = new JLabel("OPPONENT 2");

            if (multiPlayer == true)
            {
              JLabel opponent1name = new JLabel("OPPONENT 1");
              JLabel opponent3name = new JLabel("OPPONENT 3");
              oSubPane1.add(backCardLabel1);
              oSubPane1.add(backCardLabel2);
              oSubPane1.add(opponent1name);
              oSubPane1.add(opponent1ChipsLabel);

              oSubPane2.add(backCardLabel3);
              oSubPane2.add(backCardLabel4);
              oSubPane2.add(opponent2name);
              oSubPane2.add(opponent2ChipsLabel);

              oSubPane3.add(backCardLabel5);
              oSubPane3.add(backCardLabel6);
              oSubPane3.add(opponent3name);
              oSubPane3.add(opponent3ChipsLabel);
            }
            else if (multiPlayer == false)
            {
              JLabel opponentname = new JLabel("OPPONENT 1");
              oSubPane2.add(backCardLabel3);
              oSubPane2.add(backCardLabel4);
              oSubPane2.add(opponentname);
              oSubPane2.add(opponent1ChipsLabel);

            }


            for (int i = 0; i < 2; i++) {
                pSubPane2.add(new JLabel(getCardImage((players.get(0).getHand()).get(i))));
            }
            pSubPane2.add(playername);
            pSubPane2.add(playerChipsLabel);


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

            // adding into mainFrame Panels
            pSubPane1.add(messagePanel);


            oSubPane1.setBackground(pokerGreen);
            oSubPane2.setBackground(pokerGreen);
            oSubPane3.setBackground(pokerGreen);
            pSubPane1.setBackground(pokerGreen);
            pSubPane2.setBackground(pokerGreen);
            pSubPane3.setBackground(pokerGreen);
            messagePanel.setBackground(pokerGreen);
            centerPanel.setBackground(pokerGreen);
            optionArea.setBackground(pokerGreen);
            winnerPanel.setBackground(pokerGreen);
            flopPane.setBackground(pokerGreen);
            turnPane.setBackground(pokerGreen);
            riverPane.setBackground(pokerGreen);



            mainFrame = new JFrame("Poker Game");
            mainFrame.setSize(new Dimension(1100, 700));
            mainFrame.setLayout(new BorderLayout() );
            mainFrame.setResizable(false);
            mainFrame.setLocation(250, 250);
            mainFrame.getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, pokerGreen));
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.getContentPane().add(BorderLayout.NORTH, opponentPanel);
            mainFrame.getContentPane().add(BorderLayout.SOUTH, playerPanel);
            mainFrame.getContentPane().add(BorderLayout.CENTER, centerPanel);
            mainFrame.getContentPane().add(BorderLayout.EAST, winnerPanel);
            //mainFrame.getContentPane().add(BorderLayout.WEST, rulesPanel);
            mainFrame.setVisible(true);
        }
    }

    /**
     * Method that updates the panels in the frame based on the current game state
     */
    public void updateFrame() {
        if (step == Step.FLOP) {
            flopPane.setVisible(true);
        } else if (step == Step.TURN) {
            turnPane.setVisible(true);
        } else if (step == Step.RIVER) {
            riverPane.setVisible(true);
        }
        if (allIn) {
            message = "All in, no more bets allowed";
        }
        playerPrompt.setText(prompt);
        gameMessage.setText(message);

        potLabel.setText(String.format("Pot: %d", pot));
        opponent1ChipsLabel.setText(String.format("Chips: %d", players.get(1).getChips()));
        if (multiPlayer == true)
        {
          opponent2ChipsLabel.setText(String.format("Chips: %d", players.get(2).getChips()));
          opponent3ChipsLabel.setText(String.format("Chips: %d", players.get(3).getChips()));
        }

        playerChipsLabel.setText(String.format("Chips: %d", players.get(0).getChips()));
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
        }
        else if (allIn) {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(true);
            callButton.setEnabled(true);
            foldButton.setEnabled(true);
        }
        else if (turn == 0 && responding) {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(true);
            foldButton.setEnabled(true);
        } else if (turn == 0) {
            betButton.setEnabled(true);
            betTextField.setEnabled(true);
            checkButton.setEnabled(true);
            callButton.setEnabled(false);
            foldButton.setEnabled(true);
        } else {
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(false);
            foldButton.setEnabled(false);
        }
        rulesButton.setEnabled(true);
        updateFrame();
    }


    /**
     * Inner class that handles the betButton using ActionListener
     */
    protected class betButtonHandler implements ActionListener {
        /**
         * @param e action event
         */
        public void actionPerformed(ActionEvent e) {
            String inputText = betTextField.getText();
	    int placeHolder = 0;
            if (!inputText.equals("")) {
                bet = Integer.parseInt(inputText);
		for (Player player:players) {
		    if(player.getChips() - bet >=0) {
			placeHolder++;
		    }
		}
		if (bet<=0) {
                    prompt = "Enter a valid bet!";
                }
                else if ((players.get(0).getChips() - bet >= 0) && (players.get(1).getChips() - bet >= 0)) {
                    pot += bet;
                    players.get(0).bet(bet);
                    prompt = "Player bets " + bet + ".";
                    updateBetGUIElements();
                    checkPassTurnUpdate();
                }
                else if (((turn == 0) && (players.get(0).getChips() < bet)) || ((turn != 0) && (players.get(turn).getChips() < bet))) {
                    prompt = "Not enough chips!";
                }
                else {
                    allIn = true;
                    allInBet();
                    updateBetGUIElements();
                    checkPassTurnUpdate();
                }
                updateFrame();
            }
            else {
                prompt = "Enter a number of chips to bet!";
                updateFrame();
            }
        }

        /**
         * Handles the betting functionality for an all-in scenario
         */
        private void allInBet() {
/*
  	    int min = (players.get(0)).getChips();
	    int temp;
	    for (Player player:players) {
		temp = player.getChips();
		if(temp < min)
		   min = temp;
	    }
            prompt = "Opponent only has " + min + " chips. Your bet is limited to ";
            prompt += min + " chips.If this bet is called, it will be an all in.";
            pot += min;
            player.bet(min);
*/
          // bet limit needs readjusting
            prompt = "Opponent only has " + players.get(1).getChips() + " chips. Your bet is limited to ";
            prompt += players.get(1).getChips() + " chips.If this bet is called, it will be an all in.";
            pot += players.get(1).getChips();
            players.get(0).bet(players.get(1).getChips());
        }

        /**
         * Updates the GUI elements that are affected by the
         * bet action.
        */
        private void updateBetGUIElements() {
            betTextField.setText("");
            message = "Opponents waiting for turn.";
            betButton.setEnabled(false);
            betTextField.setEnabled(false);
            checkButton.setEnabled(false);
            callButton.setEnabled(false);
            foldButton.setEnabled(false);
            responding = true;
        }
    }

    /**
     * Inner class that handles the checkButton using ActionListener
     */
    protected class checkButtonHandler implements ActionListener {
        /**
         * @param e action event
         */
        public void actionPerformed(ActionEvent e) {
            bet = 0;
            message = "Opponents waiting to deal.";
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
        /**
         * @param e action event
         */
        public void actionPerformed(ActionEvent e) {
            message = "Opponents waiting for turn.";
            prompt = "You fold.";
            System.out.println("You Folded. ");
            players.get(0).foldHand();
            int active = 0;
            for (Player player: players)
            {
              if(player.status == 1)
              {
                active ++;
              }
            }
        }
    }

    /**
     * Inner class that handles the callButton using ActionListener
     */
    protected class callButtonHandler implements ActionListener {
	/**
         * @param e action event
         */
	public void actionPerformed(ActionEvent e) {
	    pot += bet;
	    players.get(0).bet(bet);
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
        /**
         * @param e action event
         */
        public void actionPerformed(ActionEvent e) {
            determineWinner();
            collectPot();
            showWinnerAlert();
        }
    }

    /**
     * Inner class that handles the rulesButton using ActionListener
     */
    protected class rulesButtonHandler implements ActionListener {// rules
        /**
         * @param e action event
         */
        public void actionPerformed(ActionEvent e) {
            //rulesPanel.setVisible(!rulesPanel.isVisible() );
            //rulesOverviewLabel.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesOverview.png"));
            //rulesGameplay1Label.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesGamePlay1.png"));
            //rulesGameplay2Label.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesGamePlay2.png"));
            //rulesExampleLabel.setIcon(new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesExamples.png"));
            ImageIcon icon = new ImageIcon("src/edu/ucsb/cs56/projects/games/poker/rules/rulesOverview.png");
                  JOptionPane.showMessageDialog(
                          null,
                          "",
                          "Rules", JOptionPane.INFORMATION_MESSAGE,
                          icon);

        }
    }

    /**
     * Inner class that handles the overviewButton using ActionListener
     */
    protected class overviewButtonHandler implements ActionListener {// rules
        /**
         * @param e action event
         */
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

    /**
     * Inner class that handles gamePlayButton1 using ActionListener
     */
    protected class gameplayButton1Handler implements ActionListener {// rules
        /**
         * @param e action event
         */
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

    /**
     * Inner class that handles gamePlayButton2 using ActionListener
     */
    protected class gameplayButton2Handler implements ActionListener {// rules
        /**
         * @param e action event
         */
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

    /**
     * Inner class that handles exampleButton using ActionListener
     */
    protected class exampleButtonHandler implements ActionListener {// rules
        /**
         * @param e action event
         */
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
     * @param label a label on the GUI
     */
    protected void gameOver(String label) {
        gameOverFrame = new JFrame();
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Color gameOverGreen = new Color(0,153,76);
	gameOverFrame.setBackground(gameOverGreen);

        gameOverLabel = new JLabel(label);
	gameOverMessage = new JPanel();
        gameOverMessage.setBackground(gameOverGreen);

	gameOverButtonPanel = new JPanel();
        gameOverButtonPanel.setBackground(gameOverGreen);
        gameOverButton = new JButton("Return to Main Menu");

        gameOverButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    gameOverFrame.setVisible(false);
                    PokerMain restart = new PokerMain();
                    restart.go();
                }
            });

        gameOverMessage.add(gameOverLabel);
        gameOverButtonPanel.add(gameOverButton);

        gameOverFrame.setSize(new Dimension(300, 200));
        gameOverFrame.setResizable(false);
        gameOverFrame.setLocation(250, 250);

        gameOverFrame.add(BorderLayout.NORTH, gameOverMessage);
        gameOverFrame.add(BorderLayout.SOUTH, gameOverButtonPanel);
        gameOverFrame.pack();
        gameOverFrame.setVisible(true);
        mainFrame.dispose();
    }
}
