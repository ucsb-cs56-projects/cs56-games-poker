package edu.ucsb.cs56.projects.games.poker;

/**
	Class that represents a poker player
*/
interface PlayerDelegate {
    public void fold();
}

class Player {
    private int chips;
    private int wins;
    private Hand pokerHand;
    PokerGame delegate;



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
        Player(0);
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

    public int bet(int chips) {
        if (this.chips <= chips) {
            int returnValue = this.chips;
            this.chips = 0;
            return returnValue;
        } else {
            this.chips -= chips;
            return chips;
        }
    }

    public void addCardToHand(Card card) {
        this.pokerHand.add(card);
    }

    public Card getCardFromHand(int index) {
        return this.hand.get(index);
    }

    public void win() {
        this.wins += 1;
    }

    public void foldHand() {
        this.delegate.fold();
    }


}