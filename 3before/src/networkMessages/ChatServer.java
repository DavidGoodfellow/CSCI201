package networkMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import frames.StartWindowGUI;
import game_logic.GameData;

public class ChatServer {
	private Vector<ServerThread> serverThreads;
	private int playersNeeded;
	private GameData gameData;

	public ChatServer(int port, int playersNeeded, GameData gameData){
		
		this.gameData = gameData;
		this.playersNeeded = playersNeeded;
		ServerSocket ss = null;
		serverThreads = new Vector<ServerThread>();
		
		try{
			//how the fuck to get the client on its own server
			ss = new ServerSocket(port);
			
			while(true){
				Thread.yield();
				System.out.println("checkpoint");	
				Socket s = ss.accept();
				System.out.println("Connection Made");
				playersNeeded--;
				ServerThread st = new ServerThread(s,this);
				serverThreads.add(st);
				RemainingPlayersMessage msg = new RemainingPlayersMessage("playersRemaining", Integer.toString(playersNeeded));
				sendMessageToAllClients(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendMessageToAllClients(ChatMessage message){
		System.out.println(message.getMessage());
		for(ServerThread st : serverThreads){
			st.sendMessage(message);
		}
	}
	
	
}
