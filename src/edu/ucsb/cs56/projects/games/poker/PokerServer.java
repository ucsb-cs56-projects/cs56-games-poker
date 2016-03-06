package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;

public class PokerServer {
	ServerSocket serverSock;
	Socket player1Sock, player2Sock;
	ObjectInputStream player1Input, player2Input;
	ObjectOutputStream player1Output, player2Output;
	Thread player1Listener, player2Listener;
	PokerGameState state;
	Deck deck;
	Hand player1Hand, player2Hand, flop;
	Card turn, river;

	public static void main(String[] args) {
		PokerServer server = new PokerServer();
		server.go();
	}

	public void go() {
		try {
			serverSock = new ServerSocket(15000);
			System.out.println("Poker server is running.\n Waiting for players to connect.");
			
			state = new PokerGameState(1,1,1);
			
			player1Sock = serverSock.accept();
			System.out.println("Player 1 connected. Waiting on player 2.");
			player1Output = new ObjectOutputStream(player1Sock.getOutputStream());
			player1Input = new ObjectInputStream(player1Sock.getInputStream());
			player1Output.writeObject(1);
			player1Output.flush();
			
			state = (PokerGameState) player1Input.readObject();
			if(state.getPlayer1Chips() == 0){
				System.out.println("Success");
				
			}
			
			
			player2Sock = serverSock.accept();
			System.out.println("Player 2 connected. Starting game...");
			player2Output = new ObjectOutputStream(player2Sock.getOutputStream());
			player2Input = new ObjectInputStream(player2Sock.getInputStream());
			player2Output.writeObject(2);
			player2Output.flush();
			
			
			player1Listener = new Thread(new ClientHandler());
			player1Listener.start();
			player2Listener = new Thread(new ClientHandler());
			player2Listener.start();
			
			player1Output.writeObject(state);
			player2Output.writeObject(state);
			player1Output.flush();
			player2Output.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public class ClientHandler implements Runnable {
		
		public void run() {
			try {
				if(state.getTurn() == 1){
					state = (PokerGameState) player1Input.readObject();
					System.out.println("we made it");
					//player2Output.writeObject(state);
				}
				else {
					state = (PokerGameState) player2Input.readObject();
					player1Output.writeObject(state);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
