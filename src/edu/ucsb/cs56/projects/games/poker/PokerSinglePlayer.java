package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

final class PokerSinglePlayer extends PokerGameGui {

    /**
     * Timer for calling turnDecider()
     */
    Timer timer;

    /**
     * The milliseconds for timer
     */
    int timer_value = 2000; // milliseconds

    /**
     * Whether or not it's your turn to bet
     */
    boolean yourTurnToBet = true;


    /**
     * No arg constructor to create instance of PokerSinglePlayer to begin game
     */
    public PokerSinglePlayer(){
    }
    
    /**
     * Constructor to set the player and opponent's initial chips. 
     * This is used when we continue playing the game
     * @param pChips the player's chips
     * @param oChips the opponent's chips
     */
    public PokerSinglePlayer(int pChips, int oChips){
        player.setChips(pChips);
        opponent.setChips(oChips);
    }
    
    /**
     * Starts game between you and AI
     */
    public void go() {
        super.setUp();
        layoutSubViews(); //sets up all of the buttons and panels and GUI
        controlButtons(); //this function is in PokerGameGui.

        if(!gameOver){ 
            step = Step.BLIND; //This is the first step in the game.
            turn = Turn.OPPONENT;
            prompt = "opponent goes first!";
            
            int rng = (int) (Math.random()*2); //generate a random 0 or 1 
            if (rng == 1) { //1 = player 1 goes first.
                turn = Turn.PLAYER;
                message = "player goes first!";
                prompt = "what will you do?";
              
            }
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider() );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider () {
        if (turn == Turn.PLAYER) {
            playerTurn();
        }
        else {
            opponentAI();
        }
    }

    
    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AI
     */
    public void changeTurn() {
        if (turn == Turn.PLAYER) {
            if (responding == true) {
                turn = Turn.OPPONENT;
                controlButtons();
                message = "opponent is thinking...";
                updateFrame();		
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
     * Updates GUI based on the player's decision
     */
    public void playerTurn() {
        controlButtons();
        updateFrame();
    }

    /**
     * Method that moves the game to the next phase
     */
	public void nextStep() {
        if (step == Step.BLIND) { // Most like able to skip/remove this step
            step = Step.FLOP;
        } else if (step == Step.FLOP) {
            step = Step.TURN;
        } else if (step == Step.TURN) {
            step = Step.RIVER;
        } else {
            step = Step.SHOWDOWN;
            message = "All bets are in.";
            prompt = "Determine Winner: ";
	    controlButtons();
        }
    }



    /**
     *  Simple AI for the opponent in single player.
     */
    // TODO: Make AI much more complex and less predictable
    //Move Opponent AI into its own class
    private void opponentAI() {
        boolean shouldBet = false;
        boolean shouldCall = true;
        int dValue = 0;
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
                if (allIn) {
                    message = "opponent goes all in, no more bets will be allowed";
                    bet = opponent.getChips();
                }
                else {
                    message = "opponent calls.";
                }
                pot += bet;
                opponent.bet(bet);
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
                opponent.bet(bet);
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
    		oSubPane2.add(new JLabel(getCardImage((opponent.getHand()).get(i))));
    	    }
    	    updateFrame();

	    message = winningHandMessage();

    	    if (winnerType == Winner.PLAYER) {
                System.out.println("player");
                message = message + ("\n\nYou win!\n\nNext round?");
    	    } else if (winnerType == Winner.OPPONENT) {
    		System.out.println("opponent");
    		message = message + ("\n\nOpponent wins.\n\nNext round?");
    	    } else if (winnerType == Winner.TIE){
                System.out.println("tie");
                message = message + ("\n\nTie \n\nNext round?");
    	    }
    	    
    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);
    	    
	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		PokerSinglePlayer singlePlayerReplay;

		// Check if players have enough chips.	
		// Create new game.
		
		if(player.getChips() < 5 || opponent.getChips() < 5){
		    JOptionPane.showMessageDialog(null, "Resetting chips...");
		    singlePlayerReplay = new PokerSinglePlayer();
		    singlePlayerReplay.go();
		}
		else {
		    singlePlayerReplay = new PokerSinglePlayer(player.getChips(),opponent.getChips());
		    singlePlayerReplay.go();
		}
	       
	    } else if (option == JOptionPane.NO_OPTION) {
		if(player.getChips() < 5 || opponent.getChips() < 5) {
		    gameOver("GAME OVER! No chips left!");
		}
		gameOver("GAME OVER! Thanks for playing.\n\n");
	    } 
	    
	    else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }
	
}
