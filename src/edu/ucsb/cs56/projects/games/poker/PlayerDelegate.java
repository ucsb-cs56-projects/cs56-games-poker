package edu.ucsb.cs56.projects.games.poker;

/**
 * Delegate for player folding
 */
public interface PlayerDelegate {
    /**
     * Fold delegate method, define in controller to deal with chip winnings
     */
    public void fold();
}