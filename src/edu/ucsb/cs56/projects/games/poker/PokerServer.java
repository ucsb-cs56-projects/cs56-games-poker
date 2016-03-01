package edu.ucsb.cs56.projects.games.poker;

import java.io.PrintWriter;
import java.net.*;

public class PokerServer {    
    ServerSocket serverSock;
    Socket player1Sock, player2Sock;
    PrintWriter w1,w2;
    
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
		w1 = new PrintWriter(player1Sock.getOutputStream());
		player2Sock = serverSock.accept();
		System.out.println("Second player connected");
		w2 = new PrintWriter(player2Sock.getOutputStream());
		//w1.println("start");
		w2.println("start");
		w1.println("start");
		w2.close();
		w1.close();
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

    }
    
}
