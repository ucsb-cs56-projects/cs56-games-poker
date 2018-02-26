package edu.ucsb.cs56.projects.games.poker;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class PokerSinglePlayerNoTest extends PokerSinglePlayer {
	@Override
	public int showWinnerConfirmDialog(String message) {
		return 1;
	}
	
	@Test
	public void testShowWinnerAlertWinNoMoreThan5Chips() {
		// Streams results is to be written to
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		
		// Connect stdout and stderr to these
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));

	    PokerSinglePlayerNoTest sp = new PokerSinglePlayerNoTest();
	    sp.layoutSubViews();
	    sp.gameOver = false;
	    sp.winnerType = Winner.PLAYER;
	    sp.showWinnerAlert();
	    
	    assertEquals("player\n", outContent.toString());
	    assertEquals("GAME OVER! Thanks for playing.\n\n", sp.gameOverLabel.getText());

		// Reconnect stdout and stderr to its original streams
		System.setOut(System.out);	
		System.setErr(System.err);
	}

}
