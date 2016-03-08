package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

final class PokerSinglePlayer extends PokerGame {

    Timer timer;

    /**
     * Default no-arg constructor;
     */
    public PokerSinglePlayer() {
    }
    
    /**
     * Constructor to set the player and opponent's initial chips. 
     * @param dChips
     * @param pChips
     */
    public PokerSinglePlayer(int oChips, int pChips){
	opponent.setChips(oChips);
	player.setChips(pChips);
    }
    
    /**
     * Starts the game.
     */
    public void go() {
	pot = 0;
	playerSetUp();
	layoutSubViews();
	if(!gameOver){
	    step = Step.BLIND;
	    turn = Turn.OPPONENT;
	    timer = new Timer(2000, new ActionListener() {
		    public void actionPerformed(ActionEvent e){
			opponentAI();
		    }
		});
	    timer.setRepeats(false);
	    message = "opponent is thinking...";
	    prompt = "opponent goes first!";
	    timer.restart();
	}
    }

    /**
     * Method to activate the opponent AI on turn change.
    */
	public void changeTurn() {
		if (turn == Turn.PLAYER) {
			if (responding == true) {
				turn = Turn.OPPONENT;
				controlButtons();
				updateFrame();
				message = "opponent is thinking...";
				timer.restart();
			} else {
				updateFrame();
				nextStep();
				if (step != Step.SHOWDOWN) {
					turn = Turn.OPPONENT;
					controlButtons();
					prompt = "opponent Turn.";
					message = "opponent is thinking...";
					updateFrame();
					timer.restart();
				}
			}
		} else if (turn == Turn.OPPONENT) {
			if (responding == true) {
				turn = Turn.PLAYER;
				controlButtons();
				responding = false;
				prompt = "What will you do?";
				updateFrame();
			} else {
				prompt = "What will you do?";
				turn = Turn.PLAYER;
				controlButtons();
				updateFrame();
			}
		}
	}

    /**
     *  Simple AI for the opponent in single player.
     */
	public void opponentAI() {
		Hand opponentHand = new Hand();
		if (step == Step.BLIND) {
			if (opponentHand.size() != 2) {
				for (int i = 0; i < 2; i++) {
					opponentHand.add(opponent.getHand().get(i));
				}
			}
		} else if (step == Step.FLOP) {
			if (opponentHand.size() < 5) {
				for (Card c : flop) {
					opponentHand.add(c);
				}
			}
		} else if (step == Step.TURN) {
			if (opponentHand.size() < 6) {
				opponentHand.add(turnCard);
			}
		} else if (step == Step.RIVER) {
			if (opponentHand.size() < 7) {
				opponentHand.add(riverCard);
			}
		} else {
		}

		boolean shouldBet = false;
		boolean shouldCall = true;
		int dValue = opponentHand.calculateValue();
		int betAmount = 5 * dValue;
		if (step == Step.BLIND) {
			if (dValue >= 1) {
				shouldBet = true;
			}
		} else if (step == Step.FLOP) {
			if (dValue >= 3) {
				shouldBet = true;
			}
			if ((dValue == 0 && bet >= 20)) {
				shouldCall = false;
			}
		} else if (step == Step.TURN) {
			if (dValue >= 4) {
				shouldBet = true;
			}
			if ((dValue < 2 && bet > 20)) {
				shouldCall = false;
			}
		} else if (step == Step.RIVER) {
			if (dValue >= 4) {
				shouldBet = true;
			}
			if ((dValue < 2 && bet > 20))
				shouldCall = false;
		}

		if (responding == true) {
			if (shouldCall) {
				message = "opponent calls.";
				pot += bet;
				opponent.setChips(opponent.getChips() - opponent.bet(bet));
				bet = 0;
				responding = false;
				nextStep();
				updateFrame();
				timer.restart();
			} else {
				message = "opponent folds.";
				opponent.foldHand();
			}
		} else if (shouldBet && step != Step.SHOWDOWN) {
			if ((opponent.getChips() - betAmount >= 0) && (player.getChips() - betAmount >= 0)) {
				bet = betAmount;
				pot += bet;
				opponent.setChips(opponent.getChips() - opponent.bet(bet));
				responding = true;
				message = "opponent bets " + bet + " chips.";
				updateFrame();
				changeTurn();
			} else {
				message = "opponent checks.";
				updateFrame();
				changeTurn();
			}
		} else if (step != Step.SHOWDOWN) {
			message = "opponent checks.";
			updateFrame();
			changeTurn();
		}
	}

    /**
     * Method overridden to allow for a new single player game to start.
     */
    
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
    		// Restart
    		mainFrame.dispose();
    		// Create new game
    		PokerSinglePlayer singlePlayerReplay = new PokerSinglePlayer();
    		singlePlayerReplay.go();
    	    } else {
    		// Quit
    		System.exit(1);
    	    }
    	}
	else{
	    gameOverFrame = new JFrame();
	    gameOverFrame.setLayout(new FlowLayout());
	    gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    
	    gameOverButton = new JButton("Back to Main Menu");
	    
	    gameOverButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
			gameOverFrame.setVisible(false);
			PokerMain restart = new PokerMain();
			restart.go();
		    }
		});
	    if(opponent.getChips() < 5) {
		gameOverLabel = new JLabel("GAME OVER!\n\n opponent has run of of chips!");
	    }
	    else
		gameOverLabel = new JLabel("GAME OVER!\n\n You have run of of chips!");
	    gameOverFrame.getContentPane().add(gameOverLabel);
	    gameOverFrame.getContentPane().add(gameOverButton);
	    gameOverFrame.setSize(400,200);
	    gameOverFrame.setLocation(250, 250);
	    
	    gameOverFrame.setVisible(true);
	}
    }
    
}
