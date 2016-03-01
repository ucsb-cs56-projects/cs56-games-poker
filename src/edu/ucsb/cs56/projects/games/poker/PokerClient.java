package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;

public class PokerClient extends PokerGame {
    private Socket sock;
    InputStreamReader inputReader;
    BufferedReader reader;
    String message;
    
    public static void main (String[] args){
	PokerClient client =  new PokerClient();
	try {
		client.go();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

    public void go() throws IOException{
	setUpNetworking();
	System.out.println("we connected");
		inputReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(inputReader);
		while((message = reader.readLine()) != null){
			if(message.equals("start")){
				PokerSinglePlayer game = new PokerSinglePlayer();
				game.go();
				System.out.println(message);
			}
			}
		}
		
	

	
    


    public void setUpNetworking() {

	try {
	    sock = new Socket("127.0.0.1", 15000);
	    System.out.println("Player connected");
	}
	catch(IOException ex) {
	    ex.printStackTrace();
	}
    }
}
