package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;

public class PokerServer {
	ServerSocket serverSock;
	Socket player1Sock, player2Sock;
	ObjectInputStream player1Input, player2Input;
	ObjectOutputStream player1Output, player2Output;
	Thread playerListener;
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
			
			state = new PokerGameState();
			
			player1Sock = serverSock.accept();
			System.out.println("Player 1 connected. Waiting on player 2.");
			player1Output = new ObjectOutputStream(player1Sock.getOutputStream());
			player1Input = new ObjectInputStream(player1Sock.getInputStream());
			player1Output.writeObject(1);
			player1Output.reset();
			
			
			player2Sock = serverSock.accept();
			System.out.println("Player 2 connected. Starting game...");
			player2Output = new ObjectOutputStream(player2Sock.getOutputStream());
			player2Input = new ObjectInputStream(player2Sock.getInputStream());
			player2Output.writeObject(2);
			player2Output.reset();
			
			
			playerListener = new Thread(new ClientHandler());
			playerListener.start();
			
			player1Output.writeObject(state);
			player2Output.writeObject(state);
			player1Output.reset();
			player2Output.reset();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public class ClientHandler implements Runnable {
		
		public void run() {
			while (!state.getRoundOver()) {
				try {
					System.out.println("Restarting loop\nTurn: " + state.getTurn());
					System.out.println("Step: " + state.getStep());
					if (state.getTurn() == 1) {
						System.out.println("Read from p1 write to p2");
						state = (PokerGameState) player1Input.readObject();
						System.out.println("Read from p1");
						System.out.println("New turn: " + state.getTurn());
						System.out.println("New step: " + state.getStep());
						System.out.println("Are we just updating? : " + state.getJustUpdate());
						player2Output.writeObject(state);
						System.out.println("Wrote to p2");
					} else {
						System.out.println("Read from p2 write to p1");
						state = (PokerGameState) player2Input.readObject();
						System.out.println("Read from p2");
						System.out.println("New turn: " + state.getTurn());
						System.out.println("New step: " + state.getStep());
						player1Output.writeObject(state);
						System.out.println("Wrote to p1");
					}
					state.setJustUpdate(false);
					player1Output.reset();
					player2Output.reset();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(state.getRoundOver()){
				try {
					System.out.println("Round over.\n Waiting for players to continue");
					
					String p1Cont = (String) player1Input.readObject();
					String p2Cont = (String) player2Input.readObject();
					if (p1Cont.equals("continue") && p2Cont.equals("continue")) {
						state = new PokerGameState();
						player1Output.writeObject(state);
						player2Output.writeObject(state);
						player1Output.reset();
						player2Output.reset();
						playerListener = new Thread(new ClientHandler());
						playerListener.start();
						
					}
					else
						System.exit(1);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				
			}
		}

	}

}
