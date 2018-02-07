package action_factory;

import frames.MainGUINetworked;
import frames.WinnersAndRatingGUI;
import game_logic.QuestionAnswer;
import game_logic.ServerGameData;
import messages.Message;
import messages.QuestionAnsweredMessage;
import other_gui.FinalJeopardyGUINetworked;
import other_gui.QuestionGUIElementNetworked;
import other_gui.TeamGUIComponents;
import server.Client;

public class QuestionAnsweredAction extends Action{

	private ServerGameData gameData;
	private MainGUINetworked mainGUI;
	private QuestionGUIElementNetworked currentQuestion;
	private TeamGUIComponents currentTeam;
	private String answer;
	private Client client;

	public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData, Message message,
			Client client) {
		//set variables
		QuestionAnsweredMessage qAnsweredMessage = (QuestionAnsweredMessage) message;
		this.gameData = gameData;
		this.client = client;
		this.mainGUI = mainGUI;
		answer = qAnsweredMessage.getAnswer();
		currentQuestion = client.getCurrentQuestion();
		currentTeam = currentQuestion.getCurrentTeam();
		//if the answer had valid format
		if(answer.equals("")){
			timeRanOut();
		}
		else if(answer.equals("nobody")){
			mainGUI.addUpdate("Nobody buzzed in on time. The correct answer was: "+currentQuestion.getAnswer() + ". Moving on to next question");
			currentQuestion.interruptAndStartTimer();
			checkReadyForFJ();
		}
		else if (gameData.validAnswerFormat(answer)){
			validFormat();
		}
		//if invalid format of answer
		else{
			invalidFormat();
		}

	}
	
	private void timeRanOut(){
		//System.out.println("TIME RAN OUT");
		currentTeam.deductPoints(currentQuestion.getPointValue());
		mainGUI.addUpdate(currentTeam.getTeamName()+" did not answer in time. $"+currentQuestion.getPointValue()+" will be deducted from their total. ");
		//check if all teams have gotten a chance to answer
		questionDone();
	}
	
	private void validFormat(){
		//was the question correct?
		Boolean correct = QuestionAnswer.correctAnswer(answer, currentQuestion.getAnswer());
		//if answer is correct
		if (correct){
			//add points, add update on Game Progress, set the next turn to the current team
			currentTeam.addPoints(currentQuestion.getPointValue());
			mainGUI.addUpdate(currentTeam.getTeamName()+" answered correctly. $"+currentQuestion.getPointValue()+" will be added to their total.");
			gameData.setNextTurn(currentTeam.getTeamIndex());
			//check if its time for FJ
			checkReadyForFJ();
		}
		//if answer is wrong
		else{
			//deduct points, add update in Game Progress
			currentTeam.deductPoints(currentQuestion.getPointValue());
			mainGUI.addUpdate(currentTeam.getTeamName()+" answered incorrectly. $"+currentQuestion.getPointValue()+" will be deducted from their total. ");
			//check if all teams have gotten a chance to answer
			questionDone();
		}
	}
	
	private void checkReadyForFJ(){
		currentQuestion.interruptTimer();
		//check with the game data if we are ready for FJ, if so have the mainGUI set it up
		if (gameData.readyForFinalJeoaprdy()){
			mainGUI.startFinalJeopardy();
		}
		//if not ready, move on to another question
		else{
			
			//add update to Game Progress, determine whether the game board buttons should be enabled or disabled, and change the main panel
			mainGUI.addUpdate("It is "+gameData.getCurrentTeam().getTeamName()+"'s turn to choose a question.");
			mainGUI.setCurrentTeamLabel(gameData.getCurrentTeam().getTeamName());
			if (gameData.getCurrentTeam().getTeamIndex() != client.getTeamIndex()) mainGUI.disableAllButtons();
			
			System.out.println("going to main panel");
			
			
			mainGUI.showMainPanel();
		}
	}
	
	private void questionDone(){
		//check the current question to see if all teams have had a chance, and if so
		if (currentQuestion.questionDone()){
			//set the next turn to be the team in clockwise order from original team, add an update, and see whether it is time for FJ
			gameData.setNextTurn(gameData.nextTurn(currentQuestion.getOriginalTeam()));
			mainGUI.addUpdate("No one answered the question correctly. The correct answer was: "+currentQuestion.getAnswer());
			checkReadyForFJ();
		}
		//if question has not been attempted by everyone, go to a buzz in period
		else{
			mainGUI.addUpdate("Another team can buzz in within the next 20 seconds to answer the question!");	
			currentQuestion.interruptAndStartTimer();
			currentQuestion.setBuzzInPeriod();
		}
	}
	
	private void invalidFormat(){
		//if the current team has already had a second chance, deduct points, sent an update to the Game Progress
		//then check whether all teams have had a chance to answer
		if (currentQuestion.hadSecondChance){
			currentQuestion.getCurrentTeam().deductPoints(currentQuestion.getPointValue());
			mainGUI.addUpdate(currentTeam.getTeamName()+" had an illformated answer for their second try. $"+currentQuestion.getPointValue()+" will be deducted from their total.");
			questionDone();
		}
		//if they have not had a second chance yet, stay on the same team, just send an update to the Game Progress
		else{
			//this method changes the annnouncements label on the question panel to say they had illformatted answer
			currentQuestion.illFormattedAnswer();
			mainGUI.addUpdate(currentTeam.getTeamName()+" had an illformated answer. They get to try to answer again");
			//need to set hadSecondChance to true now
			currentQuestion.hadSecondChance = true;
		}
	}
	
}
