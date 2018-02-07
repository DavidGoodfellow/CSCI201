package networkMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import frames.MainGUI;
import frames.WinnersAndRatingGUI;
import frames.StartWindowGUI;
import frames.WinnersAndRatingGUI;
import other_gui.FinalNetworkedGUI;

public class ChatClient extends Thread{
	private Socket s;
	private StartWindowGUI swGUI;
	private MainGUI mainGUI;
	private WinnersAndRatingGUI ratingGUI;
	private FinalNetworkedGUI finalGUI;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ChatClient(StartWindowGUI sw, String hostname, int port){
		s = null;
		try{
			System.out.println("in ChatClient try");
			s = new Socket(hostname, port);
			System.out.println("after socket");
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			swGUI = sw;
			System.out.println("starting client thread");
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){
		System.out.println("client run");
		try{
			while(true){
				parseMessage((ChatMessage)ois.readObject());
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
		if(cm == null){
			return;
		}
		else if(cm.getName().equals("playersRemaining")){
			//only sends a string integer of the remaining teams needed
			if(swGUI.isJoinRadio()){
				swGUI.setjnwStatusLabel("Waiting for " + ((RemainingPlayersMessage) cm).getMessage() + " other player to join..");
			}
			else if(swGUI.isHostRadio()){
				swGUI.setnwStatusLabel("Waiting for " + ((RemainingPlayersMessage) cm).getMessage() + " other player to join..");
			}
		}
	}
}
