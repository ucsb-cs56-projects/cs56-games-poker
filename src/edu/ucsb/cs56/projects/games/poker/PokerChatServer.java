package edu.ucsb.cs56.projects.games.poker;

import java.io.*;
import java.net.*;
import java.util.*;

public class PokerChatServer {

    ArrayList clientStreams;
    public class ClientHandler implements Runnable{

	BufferedReader reader;
	Socket sock;
	private String IP;
	public ClientHandler(Socket clientSocket){
	    try{
		sock = clientSocket;
		IP = sock.getRemoteSocketAddress().toString();
		InputStreamReader inputReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(inputReader);

	    } catch(Exception ex){ex.printStackTrace();}
	}
	public void run(){
	    String message;
	    try{
		while((message = reader.readLine()) != null){
		    System.out.println("read " + message);
		    sendOut(message,IP);
		}
	    }catch(Exception ex){ex.printStackTrace();}
	}
    }

    public static void main(String[] args){
	new PokerChatServer().go();
    }
    public void go(){
	clientStreams = new ArrayList();
	System.out.println("Poker chat server created");
	try{
	    ServerSocket serverSock = new ServerSocket(14040);

	    // Print out your IP address; needed to connect with you 
	    URL whatismyip = new URL("http://checkip.amazonaws.com");
	    BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
	    String ip = in.readLine();
	    System.out.println("Your IP Address is: " + ip);
	    //
    
	    while(true){
		Socket clientSocket = serverSock.accept();
		PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
		clientStreams.add(writer);
		Thread t1 = new Thread(new ClientHandler(clientSocket));
		t1.start();
		System.out.println("connection established");
	    }
	}catch (Exception ex){
	    ex.printStackTrace();
	}
    }
    public void sendOut(String message, String IP){
	Iterator iter = clientStreams.iterator();
	while(iter.hasNext()){
	    try{
		PrintWriter writer = (PrintWriter) iter.next();
		writer.println(message);
		writer.flush();
	    }catch(Exception ex){ex.printStackTrace();}
	}
    }

}
