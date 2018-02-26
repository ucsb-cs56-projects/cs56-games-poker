package edu.ucsb.cs56.projects.games.poker;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import org.junit.Test;

public class PokerSinglePlayerYesTest extends PokerSinglePlayer {	
	@Override
	public int showWinnerConfirmDialog(String message) {
		return 0;
	}
	
	@Test
	public void testShowWinnerAlertWinYesMoreThan5Chips() {
		// Streams results is to be written to
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		// Connect stdout to these
	    System.setOut(new PrintStream(outContent));

	    PokerSinglePlayerYesTest sp = new PokerSinglePlayerYesTest();
	    sp.layoutSubViews();
	    sp.gameOver = false;
	    sp.winnerType = Winner.PLAYER;
	    sp.showWinnerAlert();
	    
	    assertEquals("player\n", outContent.toString());
	    assertFalse(sp.mainFrame.isVisible());

		// Reconnect stdout to its original streams
	    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

}
