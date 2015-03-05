package edu.ucsb.cs56.projects.games.poker;

/**
	Class that represents a poker player
*/


public class Player {
    private int chips;
    private int wins;
    private Hand pokerHand;
    public PokerGame delegate;



    public Hand getPokerHand() {
        return pokerHand;
    }

    public void setPokerHand(Hand pokerHand) {
        this.pokerHand = pokerHand;
    }

    public Player(int chips) {
        this.chips = chips;
        this.wins = 0;
    }

    public Player() {
        this.chips = 0;
        this.wins = 0;
    }

    public Hand getHand() {
        return pokerHand;
    }

    public void setHand(Hand hand) {
        this.pokerHand = hand;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public int bet(int _chips) {
        if (chips == 0) return 0;
        if (chips <= _chips) {
            int returnValue = _chips;
            chips = 0;
            return returnValue;
        } else {
            chips -= _chips;
            return _chips;
        }
    }

    public void addCardToHand(Card card) {
        pokerHand.add(card);
    }

    public Card getCardFromHand(int index) {
        return pokerHand.get(index);
    }

    public void win() {
        wins += 1;
    }

    public void foldHand() {
        delegate.fold();
    }


}