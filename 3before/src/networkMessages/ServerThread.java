package networkMessages;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread{
	ChatServer cs;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public ServerThread(Socket s, ChatServer cs){
		this.cs = cs;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.start();
	}
	
	public void sendMessage(ChatMessage message){
		try{
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe){
			System.out.println("ioe" + ioe.getMessage());
		}
	}
	
	public void run(){
		try{
			while(true){
				ChatMessage message = (ChatMessage)ois.readObject();
				
				//parse will do instance of checks, works both ways
				parseMessage(message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseMessage(ChatMessage cm){
		/*if(msg.getMessage().equals("playersNeeded")){
			sendPlayersNeeded(this);
		}
		else{//used mostly for everyone but not always
			cs.sendMessageToAllClients(message);
			//have some other classes that alter the message
		}*/
		if(cm == null){
			return;
		}
		
	}
	
	
}
