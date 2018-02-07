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
import game_logic.GameData;
import frames.StartWindowGUI;
import frames.WinnersAndRatingGUI;
import other_gui.FinalJeopardyGUI;
import other_gui.FinalNetworkedGUI;
import other_gui.TeamGUIComponents;

public class ChatClient extends Thread{
	private Socket s;
	private String username;
	private StartWindowGUI swGUI;
	private MainGUI mainGUI;
	private WinnersAndRatingGUI ratingGUI;
	private FinalNetworkedGUI finalGUI;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean isHost;
	private GameData gameData;
	
	public ChatClient(StartWindowGUI sw, String hostname, int port){
		s = null;
		try{
			//System.out.println("in ChatClient try");
			s = new Socket(hostname, port);
			//System.out.println("after socket");
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			swGUI = sw;
			gameData = null;
			//setting the username
			if(swGUI.isHostRadio()){
				username = swGUI.networkedTeamField.getText();
				isHost = true;
			}
			else{
				username = swGUI.jnetworkedTeamField.getText();
				isHost = false;
			}
			//sending message of username to serverthread, eventually to server
			sendMessage(new ChatMessage("name", username));
			//System.out.println("starting client thread");
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isHost(){
		return isHost;
	}
	
	
	public void run(){
		//System.out.println("client run");
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
		System.out.println(cm.getName());
		
		if(cm == null)
			return;
		
		else if(cm.getName().equals("start")){
			
			mainGUI = new MainGUI(cm.getGameData(), swGUI.getUser(), this);
			mainGUI.setVisible(true);
			swGUI.dispose();
		}
		else if(cm.getName().equals("playersRemaining")){
			//only sends a string integer of the remaining teams needed
			if(swGUI.isHostRadio()){
				swGUI.setnwStatusLabel("Waiting for " + cm.getMessage() + " other player to join..");
			}
			else{
				swGUI.setjnwStatusLabel("Waiting for " + cm.getMessage() + " other player to join..");
			}
		}
		else if(cm.getName().equals("host left")){
			swGUI.hostLeft();
		}
		else if(cm.getName().equals("change panel")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			if(cm.getMessage().equals("main panel")){
				mainGUI.showMainPanel(true);
				//mainGUI.changePanel(mainGUI.getMainPanel(), true);
			}
			else{
				mainGUI.changePanel(cm.getPanel(), true);
			}
		}
		else if(cm.getName().equals("game button")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.disableGameButton(cm.getX(),cm.getY());
		}
		else if(cm.getName().equals("disable")){
			mainGUI.setGameData(cm.getGameData());
			mainGUI.setEnabled(false);
			//if on a question
			mainGUI.setTeamLabel(cm.getUsername());
			mainGUI.disableQ();
		}
		else if(cm.getName().equals("enable")){
			mainGUI.setGameData(cm.getGameData());
			mainGUI.setEnabled(true);
			//if on a question
			mainGUI.setTeamLabel(cm.getUsername());
			mainGUI.enableQ();
		}
		else if(cm.getName().equals("add update")){
			//gameData = cm.getGameData();
			//mainGUI.setGameData(gameData);
			mainGUI.addUpdate(cm.getMessage(), true);
		}
		else if(cm.getName().equals("buzz")){
			
			mainGUI.setEnabled(true);
			mainGUI.blankTeamLabel();
			mainGUI.buzzIn();
		}
		else if(cm.getName().equals("is original")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.setEnabled(false);
			mainGUI.blankTeamLabel();
			mainGUI.isOriginal();
		}
		else if(cm.getName().equals("has buzzed")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.setEnabled(false);
			mainGUI.blankTeamLabel();
			mainGUI.hasBuzzed();
		}
		else if(cm.getName().equals("disableQ")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.setEnabled(false);
			mainGUI.setTeamLabel(cm.getUsername());
			mainGUI.disableQ(cm.getUsername());
		}
		else if(cm.getName().equals("enableQ")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.setEnabled(true);
			mainGUI.setTeamLabel(cm.getUsername());
			mainGUI.enableQ();
		}
		else if(cm.getName().equals("disableQButton")){
			mainGUI.disableQButton();
		}
		//work on this!!!!!!!
		else if(cm.getName().equals("score panel")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			mainGUI.updateScorePanel(true);
		}
		else if(cm.getName().equals("add chosen")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
		}
		else if(cm.getName().equals("all answered")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			
			mainGUI.addUpdate("Everyone answered incorrectly.", true);
		}
		else if(cm.getName().equals("exit networked")){
			mainGUI.exitOptionPane();
		}
		else if(cm.getName().equals("finalJP")){
			gameData = cm.getGameData();
			mainGUI.setGameData(gameData);
			for(TeamGUIComponents team1 : gameData.getFinalistsAndEliminatedTeams().getFinalists()){
				if(team1.getTeamName().equals(mainGUI.getUsername())){
					mainGUI.changePanel(new FinalNetworkedGUI(gameData, mainGUI), true);
				}
			}
			for(TeamGUIComponents team1 : gameData.getFinalistsAndEliminatedTeams().getEliminatedTeams()){
				if(team1.getTeamName().equals(mainGUI.getUsername())){
					mainGUI.rate();
					//mainGUI.dispose();	
				}
			}
		}
		else if(cm.getName().equals("restart")){
			mainGUI.setGameData(cm.getGameData());
			mainGUI.restart();
		}
	}
	
	//SENDING MESSAGES
	//methods called by the GUIs
	
	public void sendMessage(ChatMessage message){
		try{
			if(message.getGameData() != null)
				gameData = message.getGameData();
			oos.writeObject(message);
			oos.flush();
			oos.reset();
		} catch (IOException ioe){
			System.out.println("ioe" + ioe.getMessage());
		}
	}
	
	//sending message helper classes
	public void sendBuzz(ChatMessage cm){
		cm.setGameData(gameData);
		cm.setUsername(username);
		sendMessage(cm);
	}
	
	public void someoneBuzzedUpdate(GameData gd, String userN){
		gameData = gd;
		gameData.setWhoseTurn(userN);
		String update = username + " buzzed in to answer.";
		sendMessage(new ChatMessage("add update",update, gameData));
	}
	 
}
