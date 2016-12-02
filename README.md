cs56-games-poker
================
GUI application that simulates a Texas Holdem style Poker Game using 2 decks.

project history
===============
```
 (pconrad) Poker Game | W14 | W16 | M16
```

Playing the Game
================
To start the game, type the command
```
ant run
```
Game Options :

* Play Single Player : Play versus the computer

* Create Poker Server : Host a socket server on the local machine for a poker game and tells you the IP Address needed for other players to join the server. In order to personally join the server you just created, open a new terminal window, type the command 'ant run' again, and then click "Connect to Poker Server." Further instructions below. (This has temporarily been removed in favor of creating a better PokerGame)

* Connect to Poker Server : Connect to an open poker server. Enter "localhost" or "127.0.0.1" into the prompt textbox to join your own server. Otherwise, enter the IP address of the host you want to connect to (IP address given to the host after creating the server). (This has temporarily been removed in favor of creating a better Poker Game)

* Create Poker Chat Server : Host a socket server for the chat program. 

* Connect to Poker Chat Server : Connect to an open poker chat server. Connecting to the chat server works the exact same way as the above 'Poker Server'.

Screenshots
===========

![Main Menu](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/pictures/menu.png)

![Rules](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/src/edu/ucsb/cs56/projects/games/poker/rules.png)

![Single Player](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/pictures/singleplayer.png)

![Creating Chat Server](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/pictures/createchatserver.png)

![Enter Username for Chat](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/pictures/chatusername.png)

![Chat](https://raw.githubusercontent.com/dvanmali/cs56-games-poker/master/pictures/chat.png)

Rules
=====
* If you forget any of these rules, don't worry just click the rules button in the Poker Game!

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
To see JavaDoc and overall structure of the game, type the command:
```
ant javadoc
```

Code Assessment
===============
The code is seperated into a multitude of classes.
* The PokerMain class sets up the menu that you first see when the game starts up. It creates three buttons and does the event handling for them as well.
* One of the buttons creates a PokerServer, which is defined in the PokerChatServer class. This class tries to create a server socket at port 14040. Then, after finding out your IP address using a website (line 45), it prints out your IP address to the stream. The socket continues running indefinitely. Should anything fail, the location of the failure in the code will be printed. 
* Another button creates the PokerChatClient. BEFORE the PokerChatClient is created, PokerMain asks for the IP address of the server youre try to connect to. THEN is creates PokerChatClient. This class then sets up a GUI that asks for a username. Then, it tries to create a socket at port 14040. once it is Setup, the ChatBox GUI is set up. The methods inside deal with receiving and sending message as well. Unlike PokerChatServer, PokerChatClient can be exited.
* The last button starts up a new PokerSinglePlayer.
* PokerSinglePlayer is a subclass of PokerGame. The GUI for PokerSinglePlayer is kept in the PokerGameGUI class. this class is also a subclass of PokerGame. 
* When the "Play Single Player" button is pushed in PokerMain, first, PokerSinglePlayer sets the chips for the player and the computer opponent. Next, inside the go method, PokerGame's setup method is called. This method makes the player and opponent bet 5 chips to set up the Ante of 5 chips. this "bet" method is located inside of the Player class. Now that the setup method has been called, layoutSubviews and controlButtons is called. both of these methods are located inside of PokerGameGUI.
* The card class creates all 52 cards, assigning them each a suit and a number. Aces have the value "14" as opposed to "1". The deck class puts all 52 cards into an array, and handles shuffling and drawing cards from the deck.

M16 final remarks
=================
Poker Single Player begins to follow a "Factory Design" pattern for PokerGame. Deck is in charge of the deck, Hand is in charge of the player's hand, Player has a hand, TableCard holds the table cards, Poker Game holds Players, a Deck, and TableCards. When we rewrote the Poker Game just for a single player, we completely got rid of our multiplayer version in favor of an understandable design. This is where you come in, we left a good basic heirarchy of the single player game and we want you to extend our PokerGame class to create the multiplayer aspect of the game through the server. See the many issues we created last year and see which ones you can tackle. Feel free to restart the idea of the Chat Button, we kept it there because it had no influence with the Single Player game when we rewrote the code. A good idea is that you should develop a better server with a hierarchal structure to make the PokerServer (aka MultiPlayer Server). Future improvements and ideas that you can do are improving the GUI layout and creating a new class to take on GUI responsibilities of the PokerGame. You can add better animations to the cards (and maybe even poker chips)! The rules sheet opens in a new window, rather, you can add it directly within the PokerGame. You can also integrate chat with the multiplayer game and disable it from the singleplayer mode. This game actually has tons of room for improvement and we can't wait to see what you come up with!

F16 final remarks
==================
To learn about the code, please refer to the code assessment section above. Overall, we have tried to move the code towards setting up a good base for future features. We separated the PokerSinglePlayer into two classes: one that contains the logic, and one that contains the GUI. The PokerSinglePlayer extends GUI, and GUI extends PokerGame. We also made some minor improvements to the gameplay, such as randomizing who gets to bet first and adding the rules image inside of the game itself. Originally, we wanted to add a multiplayer. However, as it stands, PokerServer and PokerClient need a major revamp before that can be done. Please refer to those issues for more information. The first thing we recommend you tackle is improving the CompareHands class. After we created a test class, we discovered that it contains some bugs. We got rid of some major bugs but there’s still a lot of room for improvement.  Please read the issue we left for more information on that. From then on, it is up to you whether you want to continue improving single player, or to begin adding more features. Should you choose to improve single player, consider taking the AI out of Poker Single Player and making it its own class. Our dream for this game was to create a multiplayer that allowed you to chat with the players while you played with them. There’s so many different directions you can take with this game, but remember that it is more pertinent to fix present bugs and design issues than it is to add features.