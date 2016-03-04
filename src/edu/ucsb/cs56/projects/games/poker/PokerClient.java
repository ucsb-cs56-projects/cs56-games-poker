package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;

public class PokerClient extends PokerGame {
	private Socket sock;
	ObjectOutputStream clientOutput;
	ObjectInputStream clientInput;
	String message;
	Thread listener;

	public static void main(String[] args) {
		PokerClient client = new PokerClient();
		try {
			client.go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() throws IOException {
		setUpNetworking();
		try {
			System.out.println("You connected.");
			clientInput = new ObjectInputStream(sock.getInputStream());
			clientOutput = new ObjectOutputStream(sock.getOutputStream());
			
			listener = new Thread(new InputReader());
			listener.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUpNetworking() {

		try {
			sock = new Socket("127.0.0.1", 15000);
			System.out.println("Player connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class InputReader implements Runnable {
		
		public void run () {
			try{
				while(true){
					message = (String)clientInput.readObject();
					if (message.equals("Start")) {
						PokerSinglePlayer game = new PokerSinglePlayer();
						game.go();
						System.out.println(message);
						clientOutput.writeObject("Starting game");
					}
					if (message.equals("Good")) {
						System.out.println(message);
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	
	}
}
