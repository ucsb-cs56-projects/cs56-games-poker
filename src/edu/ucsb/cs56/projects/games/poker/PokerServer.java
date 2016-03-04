package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;

public class PokerServer {
	ServerSocket serverSock;
	Socket player1Sock, player2Sock;
	ObjectInputStream player1Input, player2Input;
	ObjectOutputStream player1Output, player2Output;
	String player1Message, player2Message;
	Thread player1Listener, player2Listener;

	public static void main(String[] args) {
		PokerServer server = new PokerServer();
		server.go();
	}

	public void go() {
		try {
			serverSock = new ServerSocket(15000);
			System.out.println("Poker server is running.\n Waiting for players to connect.");
			
			player1Sock = serverSock.accept();
			System.out.println("Player 1 connected. Waiting on player 2.");
			player1Output = new ObjectOutputStream(player1Sock.getOutputStream());
			player1Input = new ObjectInputStream(player1Sock.getInputStream());
			player1Listener = new Thread(new InputReader());
			player1Listener.start();
			
			player2Sock = serverSock.accept();
			System.out.println("Player 2 connected. Starting game...");
			player2Output = new ObjectOutputStream(player2Sock.getOutputStream());
			player2Input = new ObjectInputStream(player2Sock.getInputStream());
			player2Listener = new Thread(new InputReader());
			player2Listener.start();
			
			player1Output.writeObject("Start");
			player2Output.writeObject("Start");
			
			//player1Output.close();
			//player1Input.close();
			//player2Output.close();
			//player2Input.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public class InputReader implements Runnable {

		public void run() {
			try {
				while (true) {
					player1Message = (String)player1Input.readObject();
					player2Message = (String)player2Input.readObject();
					if(player1Message.equals("Starting game") && player2Message.equals("Starting game")){
						player1Output.writeObject("Good");
						player2Output.writeObject("Good");
						System.out.println(player1Message);
						System.out.println(player2Message);
					}
						
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
