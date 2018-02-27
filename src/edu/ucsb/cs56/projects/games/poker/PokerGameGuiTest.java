package edu.ucsb.cs56.projects.games.poker;

import static org.junit.Assert.*;
import org.junit.Test;


public class PokerGameGuiTest extends PokerGameGui {
    
        /**
        * No arg constructor for PokerGameGui that simply calls the superclass constructor
        */
        public PokerGameGuiTest(){
            super();		 
        }
	
        /** test that only valid strings can be used to place bets*/
        @Test
        public void testValidBets() {
        	PokerGameGuiTest pg = new PokerGameGuiTest();
            pg.layoutSubViews();
            
            pg.betTextField.setEnabled(true);
            pg.betTextField.setText("");
            pg.betButton.setEnabled(true);
            pg.betButton.doClick();
            assertEquals("Enter a number of chips to bet!", pg.prompt);
            
            pg.betTextField.setEnabled(true);
            pg.betTextField.setText("-1");
            pg.betButton.setEnabled(true);
            pg.betButton.doClick();
            assertEquals("Enter a valid bet!", pg.prompt);
            
             
	}
        
        /** Test that a player can successfully place bets*/
        @Test
        public void testSuccessfulBets() {
            PokerGameGuiTest pg = new PokerGameGuiTest();
            pg.layoutSubViews();
            pg.step = Step.TURN;
            pg.turn = Turn.PLAYER;
            pg.responding = false;
            pg.allIn = false;
            pg.controlButtons();
            
            pg.betTextField.setText("10");
            pg.betButton.doClick();
            
            assertEquals("Player bets 10.", pg.prompt);
        }
        
        /** Test that a player cannot bet more than their chip's value*/
        @Test
        public void testBetMoreThanPlayersValue() {
            PokerGameGuiTest pg = new PokerGameGuiTest();
            pg.layoutSubViews();
            pg.step = Step.TURN;
            pg.turn = Turn.PLAYER;
            pg.responding = false;
            pg.allIn = false;
            pg.controlButtons();
            
            pg.betTextField.setText("10000");
            pg.betButton.doClick();
            
            assertEquals("Not enough chips!", pg.prompt);
            
            assertTrue(true);
        }

}
