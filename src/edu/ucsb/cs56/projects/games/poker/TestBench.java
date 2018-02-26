package edu.ucsb.cs56.projects.games.poker;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
/**
 * This class holds all data structures and functions
 * relating to our DIY branch coverage tracker
 * @author danhemgren
 *
 */

public class TestBench {
	public static HashMap<String, Boolean> coverage;
	
	/**
	 * Initialize the data structure used for 
	 * DIY branch coverage. All branches needs
	 * to be registered here as false to provide
	 * correct ratio in AnalyzeCoverage
	 */
	public static void SetupCoverageTracking() {
		coverage = new HashMap<String, Boolean>();
		coverage.put("compareHands1", false);
		coverage.put("compareHands2", false);
		coverage.put("compareHands3", false);
		coverage.put("compareHands4", false);
		coverage.put("compareHands5", false);
		coverage.put("compareHands6", false);
		coverage.put("compareHands7", false);
		coverage.put("compareHands8", false);
		coverage.put("compareHands9", false);
		coverage.put("compareHands10", false);
		coverage.put("compareHands11", false);
    
		coverage.put("getMostCommonSuit1", false);
		coverage.put("getMostCommonSuit2", false);
		coverage.put("getMostCommonSuit3", false);
		coverage.put("getMostCommonSuit4", false);
		coverage.put("getMostCommonSuit5", false);
		coverage.put("getMostCommonSuit6", false);
		coverage.put("getMostCommonSuit7", false);
		coverage.put("getMostCommonSuit8", false);
		coverage.put("getMostCommonSuit9", false);
		coverage.put("getMostCommonSuit10", false);
		coverage.put("getMostCommonSuit11", false);
    
		coverage.put("straightTie1", false);
		coverage.put("straightTie2", false);
		coverage.put("straightTie3", false);
		coverage.put("straightTie4", false);
		coverage.put("straightTie5", false);
		coverage.put("straightTie6", false);
		coverage.put("straightTie7", false);
		coverage.put("straightTie8", false);
		coverage.put("straightTie9", false);

		for (int i = 0; i <= 13; i++) {
			coverage.put("showWinnerAlert" + i, false);
		}
	}
	
	/**
	 * Iterate through registered branches 
	 * and calculate ratio of traversed branches. 
	 */
	public static void AnalyzeCoverage() {
		Collection<Boolean> values = coverage.values();
		int totalNumberOfBranches = values.size();
		if(totalNumberOfBranches < 1) {
			System.out.println("No branches have been registered, coverage ratio calculations skipped.");
			return;
		}
		int traversedBranches = 0;
		Iterator it = values.iterator();
		while(it.hasNext()) {
			Boolean val = (Boolean) it.next();
			if(val == true) {
				traversedBranches++;
			}
		}
		double res = ((double)traversedBranches / (double)totalNumberOfBranches)*100;
		System.out.println("Total branch coverage is " + res + "%");
	}
	
	/**
	 * Flag branch @branchID as traversed.
	 * @param branchID
	 */
	public static void BranchReached(String branchID) {
		if(coverage == null) {
			SetupCoverageTracking();
		}
		coverage.put(branchID, true);
	}
}
