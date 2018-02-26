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
	private static HashMap<String, Boolean> coverage;

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
                String branch = "pairTie";
                for (int i = 1; i < 9; i++) {
                    coverage.put(branch+i, false);
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
