package edu.ucsb.cs56.projects.games.poker;

public class PokerServer {    
    ServerSocket serverSock;
    Socket player1Sock, player2Sock;
    
    public static void main(String[] args) {
	PokerServer server =  new PokerServer();
	server.go();
    }

    public void go () {
	try{
	    serverSock = new ServerSocket(15000);
	    System.out.println("Poker game is running.\n Waiting for players to connect.");
	    while (true) {
		player1Sock = serverSock.accept();
		System.out.println("Player 1 connected. Waiting on player 2.");
		player2Sock = serverSock.accept();
		PrintWriter w1 = new PrintWriter(player1Sock.getOutputStream());
		PrintWriter w2 = new PrintWriter(player2Sock.getOutputStream());
		w1.println("start");
		w2.println("start1");
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }
    
}
