package other_gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;

import frames.MainGUI;
import frames.MainGUINetworked;
import game_logic.GameData;
import game_logic.Timer20;
import messages.BuzzInMessage;
import messages.QuestionAnsweredMessage;
import messages.QuestionClickedMessage;
import server.Client;

//inherits from QuestionGUIElement
public class QuestionGUIElementNetworked extends QuestionGUIElement {

	private Client client;
	//very similar variables as in the AnsweringLogic class
	public Boolean hadSecondChance;
	private TeamGUIComponents currentTeam;
	private TeamGUIComponents originalTeam;
	int teamIndex;
	int numTeams;
	
	//stores team index as the key to a Boolean indicating whether they have gotten a chance to answer the question
	private HashMap<Integer, Boolean> teamHasAnswered;
	
	public QuestionGUIElementNetworked(String question, String answer, String category, int pointValue, int indexX, int indexY) {
		super(question, answer, category, pointValue, indexX, indexY);
		//timer = new Timer20(this);
	}
	
	public void questionHitZero(){
		//System.out.println(teamLabel.getText());
		if(teamIndex != client.getCurrentQuestion().getCurrentTeam().getTeamIndex()){
			return;
		}	
		
		if(timerLabel.getText().equals(":1")){
			if(teamLabel.getIcon() != null){
				//nobody buzzed in
				client.sendMessage(new QuestionAnsweredMessage("nobody"));
			}
			else{
				System.out.println("BUZZ");
				//setBuzzInPeriod();
				client.sendMessage(new QuestionAnsweredMessage(""));
			}
		}
	
	}
	
	//set the client and also set the map with the booleans to all have false
	public void setClient(Client client, int numTeams){
		this.client = client;
		this.numTeams = numTeams;
		teamIndex = client.getTeamIndex();
		teamHasAnswered = new HashMap<>();
		for (int i = 0; i< numTeams; i++) teamHasAnswered.put(i, false);
	}
	
	//returns whether every team has had a chance at answering this question
	public Boolean questionDone(){
		Boolean questionDone = true;
		for (Boolean currentTeam : teamHasAnswered.values()) questionDone = questionDone && currentTeam;
		return questionDone;
	}
	
	public void startTimer(){
		//setTimerLabel(0);
		//if(timer.isAlive())
			//timer.interrupt();
		timer = new Timer20(this);
		timer.start();
	}
	public void interruptTimer(){
		timer.interrupt();
	}
	
	//to avoid null pointer exception
	public void interruptAndStartTimer(){
		//setTimerLabel(0);
		timer.interrupt();
		//timer.stop();
		timer = new Timer20(this);
		timer.start();
	}
	
	public Client getClient(){
		return client;
	}
	
	//overrides the addActionListeners method in super class
	@Override
	public void addActionListeners(MainGUI mainGUI, GameData gameData){
		passButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//send a buzz in message
				client.sendMessage(new BuzzInMessage(teamIndex, QuestionGUIElementNetworked.this));
			}
			
		});
		
		gameBoardButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//send a question clicked message
				client.sendMessage(new QuestionClickedMessage(getX(), getY()));
				//QuestionGUIElementNetworked.this.startTimer();
			}
		});
		
		submitAnswerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//send question answered message
				client.sendMessage(new QuestionAnsweredMessage(answerField.getText()));
			}
			
		});
	}
	
	//override the resetQuestion method
	@Override
	public void resetQuestion(){
		super.resetQuestion();
		hadSecondChance = false;
		currentTeam = null;
		originalTeam = null;
		teamHasAnswered.clear();
		//reset teamHasAnswered map so all teams get a chance to anaswer again
		for (int i = 0; i< numTeams; i++) teamHasAnswered.put(i, false);
	}
	
	@Override
	public void populate(){
		super.populate();
		passButton.setText("Buzz In!");
		//client.noClock();
	}
	
	public int getOriginalTeam(){
		return originalTeam.getTeamIndex();
	}

	public void updateAnnouncements(String announcement){
		announcementsLabel.setText(announcement);
	}
	
	public void setOriginalTeam(int team, GameData gameData){
		originalTeam = gameData.getTeamDataList().get(team);
		fishThread = new Thread(announcementIcon);
		fishThread.start();
		updateTeam(team, gameData);
	}
	
	public boolean getFirstTimer(){
		return firstTimer;
	}
	
	public void startFishThread(){
		if(fishThread.isAlive()){
			return;
		}
		fishThread = new Thread(announcementIcon);
		fishThread.start();
	}
	public void endFishThread(){
		if(fishThread.isAlive()){
			fishThread.interrupt();
		}	
	}
	
	public void removeIcon(){
		teamLabel.setIcon(null);
	}
	
	//update the current team of this question
	public void updateTeam(int team, GameData gameData){
		currentTeam = gameData.getTeamDataList().get(team);
		System.out.println(currentTeam.getTeamName());
		passButton.setVisible(false);
		
		//thread stuff
		if(t.isAlive()){
			t.interrupt();
		}
		teamLabel.setIcon(null);
		
		answerField.setText("");
		//if the current team is this client
		if (team == teamIndex){
			AppearanceSettings.setEnabled(true, submitAnswerButton, answerField);
			announcementsLabel.setText("Answer within 20 seconds!");
			//teamLabel.setIcon(null);
			endFishThread();
			announcementIcon.setVisible(false);
		}
		//if the current team is an opponent
		else{
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
			announcementsLabel.setText("It's "+currentTeam.getTeamName()+"'s turn to try to answer the question.");
			
			startFishThread();
			announcementIcon.setVisible(true);
		}
		//mark down that this team has had a chance to answer
		teamHasAnswered.replace(team, true);
		hadSecondChance = false;
		teamLabel.setText(currentTeam.getTeamName());
		System.out.println(currentTeam.getTeamName());
		
		//reset timer and clock animation
		client.setClock(team);
		client.setCurrentTeamLabel(currentTeam.getTeamName());
		
		if(firstTimer == true){
			firstTimer = false;
			startTimer();
		}
		else{
			System.out.println("INTERRUPT");
			interruptAndStartTimer();
		}
	}
	
	//called from QuestionAnswerAction when there is an illformated answer
	public void illFormattedAnswer(){
		
		if (currentTeam.getTeamIndex() == teamIndex){
			announcementsLabel.setText("You had an illformatted answer. Please try again");
		}
		else{
			announcementsLabel.setText(currentTeam.getTeamName()+" had an illformatted answer. They get to answer again.");
		}
	}
	
	//set the gui to be a buzz in period, also called from QuestionAnswerAction
	public void setBuzzInPeriod(){
		endFishThread();
		announcementIcon.setVisible(false);
		passButton.setVisible(true);
		teamLabel.setText("");
		if(t.isAlive() == false)
			t.start();
		//teamLabel.setIcon(new ImageIcon(new ImageIcon("images/clock.jpg").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
		client.noClock();
		interruptAndStartTimer();
		
		if (teamHasAnswered.get(teamIndex)){
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField, passButton);
			announcementsLabel.setText("It's time to buzz in! But you've already had your chance..");
		}
		else{
			announcementsLabel.setText("Buzz in to answer the question!");
			passButton.setEnabled(true);
			AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
		}
	}
	
	
	public TeamGUIComponents getCurrentTeam(){
		return currentTeam;
	}
}
