package edu.ucsb.cs56.projects.games.poker;



public class PokerClient extends PokerGame {
    private Socket sock;
    InputStreamReader inputReader;
    BufferedReader reader;
    String message;
    
    public static void main (String[] args){
	PokerClient client =  new PokerClient();
	client.go();
    }

    public void go() {
	setUpNetworking();
	inputReader = new InputStreamReader(sock.getInputStream());
	reader = new BufferedReader(inputReader);
	message = reader.readLine();
	System.out.println(message);
	
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
