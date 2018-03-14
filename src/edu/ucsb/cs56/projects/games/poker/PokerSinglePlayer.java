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
     * Default No arg constructor to create instance of PokerSinglePlayer to begin game
     * If no arguments, assume SinglePlayer mode
     */
     public PokerSinglePlayer() {
       super();
     }

     /**
      *  Constructor that determines singlePlayer or multiPlayer mode
      *  Mode = true, singlePlayer Mode
      *  Mode = false, multiPlayer Mode
     **/
    public PokerSinglePlayer(boolean mode){
        super(mode);

    	 for(Player player:players) {
    	    player.setDelegate(this);
        }

    }

    // not currently in use
    // In the future, more flexibility with user input on player numbers
    public PokerSinglePlayer(int totalplayers){
            super(totalplayers);
        for(Player player:players) {
            player.setDelegate(this);
        }

    }


     /* MultiPlayer and SinglePlayer Continue Constructor
      * totalPlayers not in use, but in future can easily replace MAXPLAYERS
      * and extend this so it can handle possibly any arbitrary number that a
      * user inputs
     **/
    public PokerSinglePlayer(int [] Chips, int [] Wins, int totalPlayers, boolean mode){
        super(mode);
	for (Player player:players) {
	   int index = players.indexOf(player);
	   player.setChips(Chips[index]);
	   player.setWins(Wins[index]);
	}
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

      	    players.get(0).turn = true;
      	    turn = 0;
      	    message = "player 1 goes first!";
      	    prompt = "what will you do?";
            //Here, we are using a timer to control how the turns work
            //The timer comes from the swing class if you want to read up on it
            //Another thing to note is we used a lambda function deal with the thread in timer.
            timer = new Timer(timer_value, e -> turnDecider() );
            timer.setRepeats(false); //We want the timer to go off once. We will restart it as needed instead.
            updateFrame(); //function is inside of PokerGameGui
            timer.restart();
	           players.get(0).turn = false;
        }
    }

    /**
     * Method that directs the turn to who it needs to go to
     */
    public void turnDecider () {
    	if (players.get(turn).status == 0) {
    		changeTurn();
      }
    	else {
    		players.get(turn).takeTurn();
      }
    }


    /**
     * Method to activate the opponent AI on turn change.
     * Changes between you and the AIs
     */

    public void changeTurn() {
      Player current = players.get(turn);
      if (turn < (players.size() - 1)) {
            if (responding == true) {
                turn++;
                controlButtons();
                message = "opponent " + (turn) + " is thinking...";
                if (!Fold)
                {
                  timer.restart();
                }
                updateFrame();
                }
            else {
                  //updateFrame();
                  //nextStep();
                  if (step != Step.SHOWDOWN) {
                      turn++;
                      controlButtons();
                      System.out.println("Opponent " + turn + "'s turn.");
                      prompt = "opponent " + (turn) + "'s Turn.";
                      updateFrame();
                  }
                  // if activePlayers > 1, turns keep going
                  if (!Fold)
                  {
                    timer.restart();
                  }
                }
        }
        else {
            if (responding == true) {
                turn = lowestTurn;
                controlButtons();
                responding = false;
                // if player is still in game
                if (lowestTurn == 0)
                {
                  System.out.println("Player's turn.");
                  prompt = "Player's turn. What will you do?";
                }
                // if player is not in game
                else
                {
                    System.out.println("Opponent " + turn + "'s turn.");
                    prompt = "opponent " + (turn) + "'s Turn.";
                    timer.restart();

                }
                updateFrame();
            }
            else {
                turn = lowestTurn;
                updateFrame();
                nextStep();
                if (step != Step.SHOWDOWN) {
                  // if player is still in game
                  if (lowestTurn == 0)
                  {
                    System.out.println("Player's Turn.");
                    prompt = "Player's turn. What will you do?";

                  }
                  // if player is not in game, skips their turn and calls
                  // turnDecider w/ timer.restart to make AI take its turn
                  else if (lowestTurn != 0)
                  {
                    System.out.println("Opponent " + turn + "'s turn.");
                    prompt = "opponent " + (turn) + "'s Turn.";

                    // if activePlayers > 1, turns keep going
                    if (!Fold)
                    {
                      timer.restart();
                    }
                  }
                }
                controlButtons();
                updateFrame();

            }
        }
    }

    public void nextStep() {
      System.out.println("Next Step");
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
    public void showWinnerAlert() {
      if(!gameOver){
    	    String message = "";
          oSubPane1.remove(backCardLabel1);
          oSubPane1.remove(backCardLabel2);
    	    oSubPane2.remove(backCardLabel3);
    	    oSubPane2.remove(backCardLabel4);
          oSubPane3.remove(backCardLabel5);
          oSubPane3.remove(backCardLabel6);

    	    for(int i=0;i<2;i++){
            if (multiPlayer == true)
            {
              oSubPane1.add(new JLabel(getCardImage((players.get(1).getHand()).get(i))));
              oSubPane2.add(new JLabel(getCardImage((players.get(2).getHand()).get(i))));
    	        oSubPane3.add(new JLabel(getCardImage((players.get(3).getHand()).get(i))));
             }
            else {
              oSubPane2.add(new JLabel(getCardImage((players.get(1).getHand()).get(i))));
            }

    	    }
          if (multiPlayer == true)
          {
            oSubPane1.remove(opponent1ChipsLabel);
            oSubPane1.add(opponent1ChipsLabel);
            oSubPane2.remove(opponent2ChipsLabel);
            oSubPane2.add(opponent2ChipsLabel);
            oSubPane3.remove(opponent3ChipsLabel);
            oSubPane3.add(opponent3ChipsLabel);

          }
          else {
            oSubPane2.remove(opponent1ChipsLabel);
            oSubPane2.add(opponent1ChipsLabel);

          }

    	    updateFrame();
    	    if (!Fold) {
    		message = winningHandMessage();
    	    }
    	    else {
    		message = ("Folded!");
    	    }

    	    if (winnerIdx == 0) {
                System.out.println("player");
                message = message + ("\n\nYou win!\n\nNext round?");
    	    } else if (winnerIdx != 0) {
    		System.out.println("opponent");
    		message = message + ("\n\nOpponent " + winnerIdx + " wins.\n\nNext round?");
      } else if (winnerIdx < 0){
                System.out.println("tie");
                message = message + ("\n\nTie \n\nNext round?");
    	    }

    	    int option = JOptionPane.showConfirmDialog(null, message, "Winner",
    						       JOptionPane.YES_NO_OPTION);

	    if (option == JOptionPane.YES_OPTION) {
    		// Restart
		mainFrame.dispose();
		PokerSinglePlayer singlePlayerReplay;
    PokerSinglePlayer multiPlayerReplay;

		// Check if players have enough chips.
		// Create new game.

    for (Player player: players)
    {
      if (player.getChips() < 5)
      {
        JOptionPane.showMessageDialog(null, "Resetting chips...");
        if (multiPlayer == true)
        {
          multiPlayerReplay = new PokerSinglePlayer(false);
        }
        else
        {
          singlePlayerReplay = new PokerSinglePlayer(true);
        }

      }
    }
        int [] chips = new int [MAXPLAYERS];
	int [] totalWins = new int [MAXPLAYERS];
        for (Player player:players) {
          int index = players.indexOf(player);
          chips [index] = player.getChips();
	  totalWins [index] = player.getWins();
        }

        if (multiPlayer == true)
        {

          multiPlayerReplay = new PokerSinglePlayer(chips, totalWins, totalPlayers, false);
          multiPlayerReplay.go();
        }
        else
        {
          singlePlayerReplay = new PokerSinglePlayer(chips,totalWins, totalPlayers, true);
          singlePlayerReplay.go();
        }


	    }

      else if (option == JOptionPane.NO_OPTION) {
        for (Player player: players)
        {
          if (player.getChips() < 5)
          {
            gameOver("GAME OVER! No chips left!");
          }
        }
		      gameOver("GAME OVER! Thanks for playing.\n\n");
	    }

	    else {
    		// Quit
    		System.exit(1);
    	    }
    	}
    }
    public void userTurn()
    {
      controlButtons();
      updateFrame();
    }
    /**
     * Restarts the timer controlling turnDecider()
     */
    public void restartTimer() {
        timer.restart();
    }
}
