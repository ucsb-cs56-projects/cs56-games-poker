package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;

/**
 * Class that represents a human poker player
 */
public class OpponentAI extends Player implements Serializable {

    /**
     *Creates a human poker player with a set hand
     * @param hand the hand you have
     */
    public OpponentAI(Hand hand){
        super(hand);
	this.type = 0;
    }

    /**
     * Creates a human poker player with a designated number of chips
     * @param chips number of chips
     * @param deck of Cards
     */
    public OpponentAI(int chips, Deck deck) {
        super(chips, deck);
	this.type = 0;
    }
    

    /**
     * Instructs the program to wait for user input and update the
     * GUI and game based on the user's input
     */
    public void takeTurn() {
        boolean shouldBet = false;
        boolean shouldCall = true;
        int dValue = 0;
        int betAmount = 5 * dValue;
        int bet = delegate.getCurrentBet();

        if (delegate.getStep() == PokerGameMult.Step.BLIND) {
            if (dValue >= 1) {
                shouldBet = true;
            }
        } else if (delegate.getStep() == PokerGameMult.Step.FLOP) {
            if (dValue >= 3) {
                shouldBet = true;
            }
            if ((dValue == 0 && delegate.getCurrentBet() >= 20)) {
                shouldCall = false;
            }
        } else if (delegate.getStep() == PokerGameMult.Step.TURN) {
            if (dValue >= 4) {
                shouldBet = true;
            }
            if ((dValue < 2 && delegate.getCurrentBet() > 20)) {
                shouldCall = false;
            }
        } else if (delegate.getStep() == PokerGameMult.Step.RIVER) {
            if (dValue >= 4) {
                shouldBet = true;
            }
            if ((dValue < 2 && bet > 20))
                shouldCall = false;
        }

        if (delegate.isResponding()) {
            if (shouldCall) {
                if (delegate.isAllIn()) {
                    delegate.setMessage("opponent goes all in, no more bets will be allowed");
                    delegate.setBet(this.getChips());
                }
                else {
                    delegate.setMessage("opponent " + index +" calls.");
                }
                delegate.addToPot(bet);
                this.bet(bet);
                delegate.setBet(0);
                delegate.setResponding(false);
                delegate.updateFrame();

                delegate.restartTimer();
            } else {
                delegate.setMessage("opponent folds.");
                this.foldHand();
            }
        } else if (shouldBet && delegate.getStep() != PokerGameMult.Step.SHOWDOWN) {
            if ((this.getChips() - betAmount >= 0) && (delegate.players.get(0).getChips() - betAmount >= 0)) {
                delegate.setBet(betAmount);
                bet = betAmount;
                delegate.addToPot(bet);
                this.bet(bet);
                delegate.setResponding(true);
                delegate.setMessage("opponent bets " + bet + " chips.");
                delegate.updateFrame();
                delegate.changeTurn();
            } else {
                delegate.setMessage("opponent " + index + " checks.");
                delegate.updateFrame();
                delegate.changeTurn();
            }
        } else if (delegate.getStep() != PokerGameMult.Step.SHOWDOWN) {
            delegate.setMessage("opponent " + index + " checks.");
            delegate.updateFrame();
            System.out.println("OPP 3");
            delegate.changeTurn();
        }
    }
}
