package edu.ucsb.cs56.projects.games.poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

final class PokerSinglePlayer extends PokerGame {
    public void go() {
	pot = 0;
	layoutSubViews();
	step = Step.BLIND;
	turn = Turn.DEALER;
	dealerAI();
    }

    /**
     *  Overwritten method to activate the dealer AI on turn change.
    */
    public void changeTurn() {
	if(turn == Turn.PLAYER){
	    if(responding == true){
		turn = Turn.DEALER;
		controlButtons();
		dealerAI();
	    }
	    else{
		prompt = "Dealer turn.";
		updateFrame();
		nextStep();
		turn = Turn.DEALER;
		controlButtons();
		dealerAI();
	    }
	}
	else if(turn == Turn.DEALER){
	    message = "Dealer checks.";
	    prompt = "What will you do?";	
	    turn = Turn.PLAYER;
	    controlButtons();
	    updateFrame();
	}
    }

    /**
     *  Simple method called in single player to play the dealer's turn.
     *  Currently the dealer will only check or call.
     */
    public void dealerAI () {
	if(responding == true){
	    message = "Dealer calls.";
	    pot += bet;
	    dealer.setChips(dealer.getChips() - dealer.bet(bet));
	    bet = 0;
	    responding = false;
	    updateFrame();
	    nextStep();
	    dealerAI();
	}
	else {
	    updateFrame();
	    changeTurn();
	}
    }

    /**
     * Method overridden to allow for a new single player game to start.
     */
    
    public void showWinnerAlert() {
	
        String message = "";
	dSubPane2.remove(backCardLabel1);
	dSubPane2.remove(backCardLabel2);
	for(int i=0;i<2;i++){
	    dSubPane2.add(new JLabel(getCardImage(dealer.getCardFromHand(i))));
	}
	updateFrame();
        if (winnerType == Winner.PLAYER) {
            System.out.println("player");
            message = "You won! \n\n Next round?";
        } else if (winnerType == Winner.DEALER) {
            System.out.println("dealer");
            message = "Dealer won. \n\n Next round?";
        } else if (winnerType == Winner.TIE){
            System.out.println("tie");
            message = "Tie \n\n Next round?";
        }
	
        int option = JOptionPane.showConfirmDialog(null, message, "Winner", JOptionPane.YES_NO_OPTION);
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

    
    
}
