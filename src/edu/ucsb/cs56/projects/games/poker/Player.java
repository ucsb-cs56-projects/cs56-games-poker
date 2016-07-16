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

    /**
     * Sets the player's poker hand
     * @param pokerHand the hand the player will have
     */
    public void setPokerHand(Hand pokerHand) {
        this.pokerHand = pokerHand;
    }

    /**
     * One arg constructor
     * @param chips amount of chips player starts with
     */
    public Player(int chips) {
        this.chips = chips;
        this.wins = 0;
    }

    /**
     * No arg constructor
     */
    public Player() {
        this.chips = 0;
        this.wins = 0;
    }

    /**
     * Get player's poker hand
     * @return Hand object
     */
    public Hand getHand() {
        return pokerHand;
    }

    /**
     * Set player's poker hand
     * @param hand
     */
    public void setHand(Hand hand) {
        this.pokerHand = hand;
    }

    /**
     * Get number of player wins
     * @return int type
     */
    public int getWins() {
        return wins;
    }

    /**
     * Set player wins
     * @param wins number of wins to give to player
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Get number of chips player has
     * @return int type
     */
    public int getChips() {
        return chips;
    }

    /**
     * Set number of chips player has
     * @param chips number of chips to give to player
     */
    public void setChips(int chips) {
        this.chips = chips;
    }

    /**
     * Bet chips
     * @param _chips number of chips to bet
     * @return int type: number of chips bet
     */
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

    /**
     * Add Card obejct to poker hand
     * @param card card to add to hand
     */
    public void addCardToHand(Card card) {
        pokerHand.add(card);
    }

    /**
     * Get card from Hand
     * @param index index of card into Hand ArrayList
     * @return Card object of the card at the specified index
     */
    public Card getCardFromHand(int index) {
        return pokerHand.get(index);
    }

    /**
     * Increment player's wins by one
     */
    public void win() {
        wins += 1;
    }

    /**
     * Fold hand, calls delegate fold method
     */
    public void foldHand() {
        delegate.fold();
    }


}