package networkMessages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import frames.StartWindowGUI;
import game_logic.GameData;
import other_gui.TeamGUIComponents;

public class ChatServer extends Thread{
	private Vector<ServerThread> serverThreads;
	private List<ServerThread> hasAnswered;
	private List<String> hasAnsweredString;
	private List<String> teams;
	private int playersNeeded;
	private int totalPlayers;
	private GameData gameData;
	private ServerSocket ss;
	//private int firstPlayer;

	
	//CONSTRUCTOR
	public ChatServer(int port, int playersNeeded, GameData gameData){
		
		this.gameData = gameData;
		gameData.setServer(this);
		teams = new ArrayList<String>();
		hasAnswered = new ArrayList<ServerThread>();
		hasAnsweredString = new ArrayList<String>();
		totalPlayers = playersNeeded;
		this.playersNeeded = playersNeeded;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverThreads = new Vector<ServerThread>();
		start();
	}
	
	
	//SEND MESSAGES
	public void sendMessageToAllClients(ChatMessage message){
		//System.out.println(message.getMessage());
		if(message.getGameData() != null){
			//System.out.println(gameData.getNumberChosen());
			gameData = message.getGameData();
			message.setGameData(gameData);
		}
		for(ServerThread st : serverThreads){
			st.sendMessage(message);
		}
	}
	
	public void sendMessageToAllButOne(ChatMessage message, ServerThread serverT){
		//System.out.println(message.getMessage());
		if(message.getGameData() != null){
			//System.out.println(gameData.getNumberChosen());
			gameData = message.getGameData();
			message.setGameData(gameData);
		}
		for(ServerThread st : serverThreads){
			if(st.equals(serverT)){
				continue;
			}
			else{
				st.sendMessage(message);
			}
		}
	}
	public void sendMessageToOne(ChatMessage cm, ServerThread st){
		System.out.println(cm.getMessage());
		if(cm.getGameData() != null){
			gameData = cm.getGameData();
			cm.setGameData(gameData);
		}
		st.sendMessage(cm);
	}
	
	//FUNCTIONS CALLED BY SERVER THREAD
	public void disableTurn(){
		String whoseTurn = gameData.getStringWhoseTurn();
		for(int i = 0; i < teams.size(); i++){
			if(whoseTurn.equals(teams.get(i))){
				serverThreads.get(i).sendMessage(new ChatMessage("enable","enable", gameData));
			}
			else{
				serverThreads.get(i).sendMessage(new ChatMessage("disable","disable", gameData));
			}
		}
		System.out.println("Disabled all but " + whoseTurn);
	}
	public void disableTurn(String whoseTurn){
		for(int i = 0; i < teams.size(); i++){
			if(whoseTurn.equals(teams.get(i))){
				serverThreads.get(i).sendMessage(new ChatMessage("enable","enable", gameData));
			}
			else{
				serverThreads.get(i).sendMessage(new ChatMessage("disable","disable", gameData));
			}
		}
		System.out.println("Disabled all but " + whoseTurn);
	}
	
	public void addUpdate(ChatMessage cm){
		//gameData = cm.getGameData();
		//cm.setGameData(gameData);
		sendMessageToAllClients(cm);
	}
	//SEPARATE THESE
	public void updateScorePanel(ChatMessage cm){
		//gameData = cm.getGameData();
		String teamName = cm.getTeamGUI().getTeamName();
		boolean posNeg = cm.getPosNeg();
		int pointVal = cm.getPointVal();
		
		for(int i = 0; i < gameData.getTeamDataList().size(); i++){
			if(gameData.getTeamDataList().get(i).getTeamName().equals(teamName)){
				if(posNeg == true){
					gameData.getTeamDataList().get(i).addPoints(pointVal);
					//System.out.print("Server:" + Integer.toString(gameData.getTeamDataList().get(i).getPoints()));
				}
				else{
					gameData.getTeamDataList().get(i).deductPoints(pointVal);
				}
			}
		}
		cm.setGameData(gameData);
		sendMessageToAllClients(cm);
	}
	public void goToMain(ChatMessage cm){
		gameData = cm.getGameData();
		cm.setGameData(gameData);
		hasAnswered.clear();
	}
	
	public void timeToBuzz(ChatMessage cm, ServerThread serverT){
		hasAnswered.add(serverT);
		hasAnsweredString.add(cm.getGameData().getStringWhoseTurn());
		//System.out.println(hasAnswered.get(hasAnswered.size() - 1));
		gameData = cm.getGameData();
		cm.setGameData(gameData);
		//check if all have answered
		if(hasAnswered.size() == serverThreads.size()){
			gameData.updateNumberOfChosenQuestions();
			gameData.setWhoseTurn(hasAnsweredString.get(0));
			gameData.nextTurn();
			sendMessageToAllClients(new ChatMessage("all answered","all answered",gameData));
			hasAnswered.clear();
			return;
		}
		else{
			for(ServerThread st : serverThreads){
				if(hasAnswered.get(0) != null){
					if(st.equals(hasAnswered.get(0))){
						sendMessageToOne(new ChatMessage("is original", "is original", gameData), st);
					}
					else{
						for(ServerThread st1 : hasAnswered){
							if(st1.equals(st)){
								sendMessageToOne(new ChatMessage("has buzzed", "has buzzed", gameData), st);
							}
							else{
								sendMessageToOne(new ChatMessage("buzz", "buzz", gameData), st);
							}
						}
						
					}
				}
				else{
					sendMessageToOne(new ChatMessage("buzz", "buzz", gameData), st);
				}
			}
		}
	}
	
	public void setTeamName(String name){
		teams.add(name);
		System.out.println(name);
		if(playersNeeded == 0){
			startMainForAll();
		}
	}
	
	public void joinLeft(){
		playersNeeded++;
		ChatMessage msg = new ChatMessage("playersRemaining", Integer.toString(playersNeeded));
		sendMessageToAllClients(msg);
	}
	
	public void hostLeft(){
		ChatMessage msg = new ChatMessage("host left", "host left");
		sendMessageToAllClients(msg);
	}
	
	public void changePanel(ChatMessage cm, ServerThread st){
		System.out.println("in change panel method in server");
		gameData = cm.getGameData();
		cm.setGameData(gameData);
		sendMessageToAllButOne(cm, st);
	}
	
	public void startMainForAll(){
		//System.out.println("blah");
		gameData.setTeams(teams, totalPlayers);
		
		sendMessageToAllClients(new ChatMessage("start", "start", gameData));
		disableTurn();
	}
	public void disableQ(ChatMessage cm, ServerThread t){
		gameData = cm.getGameData();
		cm.setGameData(gameData);
		sendMessageToAllButOne(cm, t);
	}
	public void enableQ(ChatMessage cm, ServerThread t){
		gameData = cm.getGameData();
		cm.setGameData(gameData);
		sendMessageToOne(cm, t);
	}
	public void gameButton(ChatMessage cm){
		gameData.addButton(cm.getX(), cm.getY());
		cm.setGameData(gameData);
		sendMessageToAllClients(cm);
	}
	public void finalJP(ChatMessage cm){
		gameData.disableRemainingButtons();
		// figure out the teams that qualified for final jeopardy
		gameData.determineFinalists();
		
		sendMessageToAllClients(new ChatMessage("finalJP", "finalJP", gameData));
	}
	public void addChosen(ChatMessage cm){
		if(cm.getGameData().getNumberChosen() > gameData.getNumberChosen())
			gameData.setNumberOfChosenQuestions(cm.getGameData().getNumberChosen());
		else{
			gameData.updateNumberOfChosenQuestions();
		}
		cm.setGameData(gameData);
	}
	public void exitNetworked(ChatMessage cm, ServerThread t){
		sendMessageToAllButOne(cm, t);
	}
	public void restart(){
		gameData.restartGame();
		sendMessageToAllClients(new ChatMessage("restart","restart",gameData));
	}
	
	//ONLY READ UNTIL ALL CONNECTIONS MADE
	public void run(){
		try{
			//how the fuck to get the client on its own server
			while(true){
				Thread.yield();
				//System.out.println("checkpoint");	
				Socket s = ss.accept();
				//System.out.println("Connection Made");
				playersNeeded--;
				ServerThread st = new ServerThread(s,this);
				serverThreads.add(st);
				if(playersNeeded == 0){
					break;
				}
				ChatMessage msg = new ChatMessage("playersRemaining", Integer.toString(playersNeeded));
				sendMessageToAllClients(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
