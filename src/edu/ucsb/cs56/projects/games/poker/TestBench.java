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
	}
	
	public static void AnalyzeCoverage() {
		Collection<Boolean> values = coverage.values();
		int totalNumberOfBranches = values.size();
		if(totalNumberOfBranches < 1) {
			System.out.println("No branches have been registered, aborting...");
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
}
