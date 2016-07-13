package edu.ucsb.cs56.projects.games.poker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class PokerChatClient {
	
    JTextArea chatText;
    JTextField outgoingText;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String address;
	
    public static void main(String[] args){
	PokerChatClient client = new PokerChatClient();
	client.go();
    }
    public void setAddress(String address){
	this.address = address;
    }

    public void go(){		
	JFrame frame = new JFrame("Poker Chat Client");
	JPanel mainPanel = new JPanel();
	chatText = new JTextArea(15,50);
	chatText.setLineWrap(true);
	chatText.setWrapStyleWord(true);
	chatText.setEditable(false);

	JScrollPane qScroller = new JScrollPane(chatText);
	qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
	
	outgoingText = new JTextField(20);
	outgoingText.addActionListener(new SendListener());

	JButton sendButton = new JButton("Send");
	sendButton.addActionListener(new SendListener());
	mainPanel.add(qScroller);
	mainPanel.add(outgoingText);
	mainPanel.add(sendButton);
	setUpNetworking();
	Thread readerThread = new Thread(new IncomingReader());
	readerThread.start();
	frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
	frame.setSize(600,400);
	frame.setVisible(true);
    }
	
    private void setUpNetworking(){
	try {
	    sock = new Socket(address, 14040);
	    InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
	    reader = new BufferedReader(streamReader);
	    writer = new PrintWriter(sock.getOutputStream());
	    System.out.println("connected to network chat server");
	}catch(IOException ex){
	    ex.printStackTrace();
	}
    }
	
    public class SendListener implements ActionListener{
	public void actionPerformed(ActionEvent e){
	    try{
		writer.println(outgoingText.getText());
		writer.flush();
	    }
	    catch(Exception ex){
		ex.printStackTrace();
	    }
	    outgoingText.setText("");
	    outgoingText.requestFocus();
	}
    }
    public class IncomingReader implements Runnable{
	public void run(){
	    String message;
	    try{
		while((message = reader.readLine()) != null){
		    System.out.println("read " + message);
		    chatText.append(message + "\n");
		}
	    }
	    catch(Exception ex){ex.printStackTrace();}
	}
    }
		
}
