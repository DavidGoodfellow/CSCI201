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

import other_gui.TeamGUIComponents;

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
			oos.reset();
			//System.out.println("test");
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
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
		else if(cm.getName().equals("name")){
			cs.setTeamName(cm.getMessage());
		}
		else if(cm.getName().equals("join left")){
			cs.joinLeft();
		}
		else if(cm.getName().equals("host left")){
			cs.hostLeft();
		}
		else if(cm.getName().equals("change panel")){
			//System.out.println("sending from thread to server");
			cs.changePanel(cm, this);
		}
		else if(cm.getName().equals("add update")){
			cs.addUpdate(cm);
		}
		else if(cm.getName().equals("score panel")){
			cs.updateScorePanel(cm);
		}
		else if(cm.getName().equals("show main")){
			cs.goToMain(cm);
		}
		else if(cm.getName().equals("buzz")){
			//System.out.println("in server thread");
			cs.timeToBuzz(cm, this);
		}
		else if(cm.getName().equals("disableQ")){
			cs.disableQ(cm, this);
		}
		else if(cm.getName().equals("enableQ")){
			cs.enableQ(cm, this);
		}
		else if(cm.getName().equals("disable QButton")){
			cs.sendMessageToAllClients(cm);
		}
		else if(cm.getName().equals("exit networked")){
			cs.exitNetworked(cm, this);
		}
		else if(cm.getName().equals("game button")){
			cs.gameButton(cm);
		}
		else if(cm.getName().equals("add chosen")){
			cs.addChosen(cm);
		}
		else if(cm.getName().equals("finalJP")){
			cs.finalJP(cm);
			
		}
		else if(cm.getName().equals("restart")){
			cs.restart();
		}
	}
	
	
}
