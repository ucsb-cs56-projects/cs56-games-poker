cs56-games-poker
================
GUI application that simulates a Texas Holdem style Poker Game using 2 decks.

project history
===============
```
 W14 | TBD | TBD | (pconrad) Poker Game
```

Playing the Game
================
To start the game, type the command
```
ant run
```
Game Options :

* Play Single Player : Play versus the computer

* Create Poker Server : Host a socket server on the local machine for a poker game and tells you the IP Address needed for other players to join the server. In order to personally join the server you just created, open a new terminal window, type the command 'ant run' again, and then click "Connect to Poker Server." Further instructions below.

* Connect to Poker Server : Connect to an open poker server. Enter "localhost" or "127.0.0.1" into the prompt textbox to join your own server. Otherwise, enter the IP address of the host you want to connect to (IP address given to the host after creating the server). 

* Create Poker Chat Server : Host a socket server for the chat program. 

* Connect to Poker Chat Server : Connect to an open poker chat server. Connecting to the chat server works the exact same way as the above 'Poker Server'.
 
Screenshots
===========
![Main Menu](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/menu.png)

![Single Player](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/singleplayer.png)

![Multiplayer](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/multiplayer.png)

![Chat](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/Chat.png)

Betting
=======
* If you choose to bet, enter the amount in the betting field and press bet or enter
* As of now, raising is not implemented as a response to a bet.

Folding
=======
* If you choose to fold, press the fold button; be aware you will lose any chips you placed in the pot
* Also, if you choose not to call the dealer's bet, you will fold

Winning
=======
* In order to win, either recieve a better poker hand than the dealer or try to make him fold.

End Of Game
===========
* At the end of every hand the winner will appear in an alert
* If you wish to continue playing, press Yes
* If you wish to quit playing press No

JavaDoc
=======
Note that ant javadoc works but links do not!


M16 final remarks
=================
Poker Single Player begins to follow a "Factory Design" pattern for PokerGame. Deck is in charge of the deck, Hand is in charge of the player's hand, Player has a hand, TableCard holds the table cards, Poker Game holds Players, a Deck, and TableCards. When we rewrote the Poker Game just for a single player, we completely got rid of our multiplayer version in favor of an understandable design. This is where you come in, we left a good basic heirarchy of the single player game and we want you to extend our PokerGame class to create the multiplayer aspect of the game through the server. See the many issues we created last year and see which ones you can tackle. Feel free to restart the idea of the Chat Button, we kept it there because it had no influence with the Single Player game when we rewrote the code. A good idea is that you should develop a better server with a hierarchal structure to make the PokerServer (aka MultiPlayer Server). This game actually has tons of room for improvement and we can't wait to see what you come up with!
