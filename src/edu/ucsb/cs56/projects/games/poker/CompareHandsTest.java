package edu.ucsb.cs56.projects.games.poker;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;


import org.junit.Test;

/**
 * Test class for CompareHands, to test if winning hands are judged correctly
 *
 *@author Gregory Whiter
 *@version CS56, Fall 2016
 *@see CompareHands
 */


 public class CompareHandsTest{
	Card twoDiamond = new Card(2,"D");
	Card twoHeart = new Card(2,"H");
	Card twoClub = new Card(2,"C");
	Card twoSpade = new Card(2,"S");
	Card threeDiamond = new Card(3,"D");
	Card threeHeart = new Card(3,"H");
	Card threeClub = new Card(3,"C");
	Card threeSpade = new Card(3,"S");
	Card fourDiamond = new Card(4,"D");
	Card fourHeart = new Card(4,"H");
	Card fourClub = new Card(4,"C");
	Card fourSpade = new Card(4,"S");
	Card fiveDiamond = new Card(5,"D");
	Card fiveHeart = new Card(5,"H");
	Card fiveClub = new Card(5,"C");
	Card fiveSpade = new Card(5,"S");
	Card sixDiamond = new Card(6,"D");
	Card sixHeart = new Card(6,"H");
	Card sixClub = new Card(6,"C");
	Card sixSpade = new Card(6,"S");
	Card sevenDiamond = new Card(7,"D");
	Card sevenHeart = new Card(7,"H");
	Card sevenClub = new Card(7,"C");
	Card sevenSpade = new Card(7,"S");
	Card eightDiamond = new Card(8,"D");
	Card eightHeart = new Card(8,"H");
	Card eightClub = new Card(8,"C");
	Card eightSpade = new Card(8,"S");
	Card nineDiamond = new Card(9,"D");
	Card nineHeart = new Card(9,"H");
	Card nineClub = new Card(9,"C");
	Card nineSpade = new Card(9,"S");
	Card tenDiamond = new Card(10,"D");
	Card tenHeart = new Card(10,"H");
	Card tenClub = new Card(10,"C");
	Card tenSpade = new Card(10,"S");
	Card jackDiamond = new Card(11,"D");
	Card jackHeart = new Card(11,"H");
	Card jackClub = new Card(11,"C");
	Card jackSpade = new Card(11,"S");   // JACK
	Card queenDiamond = new Card(12,"D");
	Card queenHeart = new Card(12,"H");
	Card queenClub = new Card(12,"C");
	Card queenSpade = new Card(12,"S");   // QUEEN
	Card kingDiamond = new Card(13,"D");
	Card kingHeart = new Card(13,"H");
	Card kingClub = new Card(13,"C");
	Card kingSpade = new Card(13,"S");   // KING
	Card aceDiamond = new Card(14,"D");
	Card aceHeart = new Card(14,"H");
	Card aceClub = new Card(14,"C");
	Card aceSpade = new Card(14,"S"); // ACE

	Player player1, player2;
  ArrayList<Player> players = new ArrayList<Player>(2);

	TableCards table;
	Hand hand1, hand2;
	CompareHands comparingHands;
	ArrayList<Card> cardHand = new ArrayList<Card>();	 //Player cards + table cards


	 /** test hand with one pair wins over ace high hand */
	 @Test
	 public void testisOnePairWins(){
		table = new TableCards(fiveSpade, twoHeart, kingClub, nineDiamond, jackHeart);
		hand1 = new Hand(fiveDiamond, sixSpade);
		player1 = new User(hand1);
		hand2 = new Hand(aceSpade, sixHeart);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }
	 /** test hand with one pair loses to hand with straight */
	 @Test
	 public void testisOnePairLoses(){
		table = new TableCards(fiveSpade, twoHeart, kingClub, nineDiamond, jackHeart);
		hand1 = new Hand(fiveDiamond, sixSpade);
		player1 = new User(hand1);
		hand2 = new Hand(queenSpade, tenHeart);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test two pair wins against one pair */
	 @Test
	 public void testisTwoPairWins(){
		table = new TableCards(threeSpade, threeDiamond, sevenHeart, queenClub, fiveClub);
		hand1 = new Hand(fiveDiamond, jackSpade);
		player1 = new User(hand1);
		hand2 = new Hand(aceHeart, tenSpade);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
		assertEquals("Two Pair beats Pair", comparingHands.compareMessage());
	 }
	 /** test two pair loses against straight */
	 @Test
	 public void testisTwoPairLoses(){
		table = new TableCards(threeSpade, threeDiamond, sevenHeart, queenClub, fiveClub);
		hand1 = new Hand(fiveDiamond, jackSpade);
		player1 = new User(hand1);
		hand2 = new Hand(fourHeart, sixSpade);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
		assertEquals("Straight beats Two Pair", comparingHands.compareMessage());
	 }

	 /** test three of a kind wins against one pair */
	 @Test
	 public void testisThreeOfAKindWins(){
		table = new TableCards(fiveSpade, twoSpade, kingSpade, fiveClub, jackSpade);
		hand1 = new Hand(fiveDiamond, eightHeart);
		player1 = new User(hand1);
		hand2 = new Hand(fourHeart, sixClub);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
		assertEquals("Three of a Kind beats Pair", comparingHands.compareMessage());
	 }

	 /** test three of a kind loses against flush */
	 @Test
	 public void testisThreeOfAKindLoses(){
		table = new TableCards(fiveSpade, twoSpade, kingSpade, fiveClub, jackSpade);
		hand1 = new Hand(fiveDiamond, eightHeart);
		player1 = new User(hand1);
		hand2 = new Hand(fourSpade, sixClub);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test straight wins against three of a kind */
	 @Test
	 public void testisStraightWins(){
		table = new TableCards(aceSpade, twoHeart, sevenHeart, threeDiamond, jackHeart);
		hand1 = new Hand(fourSpade, fiveSpade);
		player1 = new User(hand1);
		hand2 = new Hand(sevenDiamond, sevenClub);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }

	 /** test straight loses against flush */
	 @Test
	 public void testisStraightLoses(){
		 table = new TableCards(fiveSpade, twoHeart, sevenHeart, threeDiamond, jackHeart);
		hand1 = new Hand(fourDiamond, aceSpade);
		player1 = new User(hand1);
		hand2 = new Hand(tenHeart, kingHeart);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test flush wins against straight */
	 @Test
	 public void testisFlushWins(){
		table = new TableCards(fiveDiamond, twoHeart, eightSpade, nineSpade, jackSpade);
		hand1 = new Hand(fourSpade, kingSpade);
		player1 = new User(hand1);
		hand2 = new Hand(tenSpade, queenDiamond);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }

	 /** test flush loses to straight flush */
	 @Test
	 public void testisFlushLoses(){
		table = new TableCards(twoDiamond, fiveSpade, threeSpade, fourSpade, jackSpade);
		hand1 = new Hand(fourSpade, kingSpade);
		player1 = new User(hand1);
		hand2 = new Hand(aceSpade, twoSpade);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test full house wins against flush */
	 @Test
	 public void testisFullHouseWins(){
		table = new TableCards(twoSpade,twoHeart,eightHeart,nineDiamond,jackHeart);
		hand1 = new Hand(twoDiamond, eightClub);
		player1 = new User(hand1);
		hand2 = new Hand(tenHeart, fiveHeart);
		player2 = new User(hand2);
    players.add(player1);
    players.add(player2);
		comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }

	 /** test full house loses against four of a kind */
	 @Test
	 public void testisFullHouseLoses(){
		table = new TableCards(twoSpade,twoHeart,eightHeart,eightClub,jackHeart);
		hand1 = new Hand(twoDiamond, jackSpade);
		player1 = new User(hand1);
		hand2 = new Hand(eightSpade, eightDiamond);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test four of a kind wins against three of a kind */
	 @Test
	 public void testisFourOfAKindWins(){
		table = new TableCards(aceSpade,aceHeart,tenHeart,threeClub,jackHeart);
		hand1 = new Hand(aceDiamond, aceClub);
		player1 = new User(hand1);
		hand2 = new Hand(threeSpade, threeDiamond);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }

	 /** test four of a kind loses to straight flush */
	 @Test
	 public void testisFourOfAKindLoses(){
		table = new TableCards(aceSpade, aceHeart, threeSpade, fourSpade, jackDiamond);
		hand1 = new Hand(aceDiamond, aceClub);
		player1 = new User(hand1);
		hand2 = new Hand(twoSpade, fiveSpade);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test straight flush wins against full house */
	 @Test
	 public void testisStraightFlushWins(){
		table = new TableCards(jackClub, threeDiamond, fourDiamond, fiveDiamond, jackHeart);
		hand1 = new Hand(aceClub, twoClub);
		player1 = new User(hand1);
		hand2 = new Hand(fourDiamond, jackSpade);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(0, comparingHands.compareHands());
	 }

	 /** test close case of two pair of 3's and 10's beats two pair of 3's and 5's */
	 @Test
	 public void testLowerTwoPairLoses() {
		table = new TableCards(threeSpade, threeDiamond, sevenHeart, queenClub, jackClub);
		hand1 = new Hand(tenDiamond, tenSpade);
		player1 = new User(hand1);
		hand2 = new Hand(fiveHeart, fiveSpade);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(1, comparingHands.compareHands());
	 }

	 /** test case where players have the same hand */
	 @Test
	 public void testTie(){
		table = new TableCards(aceSpade, threeDiamond, sevenHeart, queenClub, jackClub);
		hand1 = new Hand(tenDiamond, tenSpade);
		player1 = new User(hand1);
		hand2 = new Hand(tenHeart, tenClub);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

		assertEquals(2, comparingHands.compareHands());
	 }


    /** test lowest ranking straightflush wins against straight */
    @Test
    public void testStraightFlushWinsAgain(){
        table = new TableCards(aceSpade, twoSpade, threeSpade, fourSpade, jackHeart);
        hand1 = new Hand(fiveSpade, kingHeart);
        player1 = new User(hand1);
        hand2 = new Hand(fiveHeart, kingDiamond);
        player2 = new User(hand2);
        players.add(player1);
        players.add(player2);
        comparingHands = new CompareHands(players, table);

        assertEquals(1, comparingHands.compareHands());
    }


     /** test straight wins against two pairs */
    @Test
    public void testStraightWinsAgain(){
        table = new TableCards(fiveSpade, fourSpade, threeSpade, twoSpade, jackHeart);
        hand1 = new Hand(aceHeart, kingHeart);
        player1 = new User(hand1);
        hand2 = new Hand(fourHeart, jackDiamond);
        player2 = new User(hand2);
        players.add(player1);
        players.add(player2);
        comparingHands = new CompareHands(players, table);

        assertEquals(1, comparingHands.compareHands());
    }

     /** test straight wins against pair when table has duplicate values */
    @Test
    public void testStraightDuplicateWins() {
        table = new TableCards(aceSpade, fourSpade, threeSpade, twoSpade, jackHeart);
        hand1 = new Hand(aceHeart, kingHeart);
        player1 = new User(hand1);
        hand2 = new Hand(fourHeart, sevenDiamond);
        player2 = new User(hand2);
        players.add(player1);
        players.add(player2);
        comparingHands = new CompareHands(players, table);

        assertEquals(1, comparingHands.compareHands());

    }

    /** test straight flush wins against lower straight flush */
    @Test
    public void testStraightFlushBeatsStraightFlush() {
        table = new TableCards(eightHeart, nineHeart, tenHeart, fiveClub, twoSpade);
        hand1 = new Hand(jackHeart, queenHeart);
        player1 = new User(hand1);
        hand2 = new Hand(sevenHeart, jackHeart);
        player2 = new User(hand2);
        players.add(player1);
        players.add(player2);
        comparingHands = new CompareHands(players, table);

        assertEquals(1, comparingHands.compareHands());
    }

    /** test four of a kind wins against lower four of a kind */
    @Test
    public void testFourOfAKBeatsFourOfAK() {
        table = new TableCards(eightHeart, nineHeart, eightDiamond, nineClub, twoSpade);
        hand1 = new Hand(eightSpade, eightClub);
        player1 = new User(hand1);
        hand2 = new Hand(nineDiamond, nineHeart);
        player2 = new User(hand2);
        players.add(player1);
        players.add(player2);
        comparingHands = new CompareHands(players, table);

        assertEquals(0, comparingHands.compareHands());
    }

    /** test full house wins against lower full house */
    @Test
    public void testFullHouseBeatsFullHouse() {
		table = new TableCards(twoSpade, twoHeart, eightHeart, eightDiamond, jackHeart);
		hand1 = new Hand(twoDiamond, jackClub);
		player1 = new User(hand1);
		hand2 = new Hand(eightClub, jackSpade);
		player2 = new User(hand2);
		players.add(player1);
    players.add(player2);
    comparingHands = new CompareHands(players, table);

    		assertEquals(0, comparingHands.compareHands());
        }

    	/** test flush wins against lower flush */
    	@Test
        public void testFlushBeatsFlush() {
            table = new TableCards(twoSpade,twoHeart,eightHeart,nineDiamond,jackHeart);
            hand1 = new Hand(threeHeart, tenHeart);
            player1 = new User(hand1);
            hand2 = new Hand(queenHeart, fiveHeart);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
    comparingHands = new CompareHands(players, table);

            assertEquals(0, comparingHands.compareHands());
        }

        /** test straight wins against lower straight */
        @Test
        public void testStraightBeatsStraight() {
            table = new TableCards(fourHeart, fiveSpade, sixClub, eightSpade, twoDiamond);
            hand1 = new Hand(sevenHeart, nineHeart);
            player1 = new User(hand1);
            hand2 = new Hand(threeClub, sevenClub);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
            comparingHands = new CompareHands(players, table);

            assertEquals(1, comparingHands.compareHands());
        }

        /** test three of a kind wins against lower three of a kind */
        @Test
        public void testThreeOfAKBeatsThreeOfAK() {
            table = new TableCards(fourSpade, tenDiamond, threeHeart, sixDiamond, fourClub);
            hand1 = new Hand(tenHeart, tenSpade);
            player1 = new User(hand1);
            hand2 = new Hand(fourHeart, sevenDiamond);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
            comparingHands = new CompareHands(players, table);

            assertEquals(1, comparingHands.compareHands());
        }

        /** Test two pair wins against lower two pair */
        @Test
        public void testTwoPairBeatsTwoPair() {
            table = new TableCards(fourSpade, tenDiamond, threeHeart, sixDiamond, fourClub);
            hand1 = new Hand(aceHeart, tenSpade);
            player1 = new User(hand1);
            hand2 = new Hand(sixHeart, sevenDiamond);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
            comparingHands = new CompareHands(players, table);

            assertEquals(1, comparingHands.compareHands());
        }

        /** Test pair wins against lower pair */
        @Test
        public void pairBeatsPair() {
            table = new TableCards(sevenClub, tenDiamond, threeHeart, sixDiamond, fourClub);
            hand1 = new Hand(aceHeart, tenSpade);
            player1 = new User(hand1);
            hand2 = new Hand(twoHeart, sevenDiamond);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
            comparingHands = new CompareHands(players, table);

            assertEquals(1, comparingHands.compareHands());
        }

        /** Test player with higher card wins when no actual hands are able to be made */
        @Test
        public void testHighCardBeatsLowerCard() {
            table = new TableCards(sevenClub, tenDiamond, threeHeart, sixDiamond, fourClub);
            hand1 = new Hand(aceHeart, twoSpade);
            player1 = new User(hand1);
            hand2 = new Hand(nineHeart, jackDiamond);
            player2 = new User(hand2);
            players.add(player1);
            players.add(player2);
            comparingHands = new CompareHands(players, table);

            assertEquals(1, comparingHands.compareHands());
        }
    }
