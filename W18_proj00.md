Annie Bai, Hyewon Seong

# CS56 Winter 2018 P. Conrad

### Description:
The project allows you to simulate a Texas Hold'em poker game, either against a computer in single-player.

### User Stories:
* As a user, I can choose to play a single-player poker game against a computer
* As a user, I can bet, call, check, or fold during a hand, or check the rules at any time.

### Assessment of Project:
* The single player mode runs to completion.
* The option for creating a poker chat causes the screen to freeze.
* The option of joining a poker chat brings up a chat screen, but does not send any messages.

### User Stories: Prospective Options
* As a user, I can play against multiple players in a game, instead of just one.
* As a user, I can customize the name that is displayed on the screen.

### Assessment of the README
The README is mostly comprehensive. A better explanation of how to win the game might be necessary for people who don't know the game.

### Assessment of build.xml file
The file is currently based on ant, but has relatively few descriptions about what the flie itself is doing.

### Assessment of Issues
There are enough issues to gain more than 1000 points, and they are clear in what is expected to gain those points.

### Additional Issues
*Something we may try to implement is a more visually pleasing interface, for both choosing the game and displaying the win conditions, as well as an easier to read Rules explanation.

### Assessment of code
The code is currently organized explicity for a two-player game, and therefore relies on such.
The way each individual file is organized, but together the code gets a little messier.
* The Deck code describes how the cards are moved from the active deck to the discarded one.
* The Hand code seems to describe how to return values in the hand.
* The Player class gives it a hand of cards, keeps track of its wins, and the amount of chips it holds
* One of the classes holds code for restarting a timer, but a timer isn't implemented within the game.
* The CompareHands code compares the values of two different cards in order to determine the winner - it is currently highly dependent on the fact that there are only two players

### Assessment of Test Code
There are several test codes for win conditions for different match-ups. It doesn't cover every case, however, and could be improved in including cases for empty hands.  It also does not have any test cases for the bet implementation or other features, which could be added to make it more thorough.
