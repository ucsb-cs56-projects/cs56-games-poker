cs56-games-poker
================
Original Programmer: Joey Dewan
        
GUI application that simulates a Texas Holdem style Poker Game using 2 decks.

// Links not working anymore

See: https://foo.cs.ucsb.edu/56mantis/view.php?id=786
https://foo.cs.ucsb.edu/cs56/issues/0000786/

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
![alt tag](https://raw.githubusercontent.com/username/projectname/branch/path/to/img.png)
![Single Player](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/blob/master/Single%20Player.png)
![Main Menu](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/blob/master/Main%20Menu.png)
![Chat](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/blob/master/Chat.png)
![Multiplayer](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/blob/master/Multiplayer%20Up%20and%20Running.png)


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

W16 final remarks
=================
When following the game logic for the multiplayer portion of the game, note that each client for player 1 and 2 is managing their own instance variables inherited from PokerGame while sharing its information with the server and the other player by updating and sending a serializable game state through the socket connection. One major improvement that can be made to the game would be refactoring it to follow a model, view, controller design pattern which would allow for easier future changes to the GUI and game separately(see issue #18). This would open up possibilities like having more than two player multiplayer, which would better simulate a real game of poker. For other bugfixes and improvements see issue #19 and #20. 
