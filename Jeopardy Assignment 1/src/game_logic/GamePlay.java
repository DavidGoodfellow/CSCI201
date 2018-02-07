package game_logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;

import Game_Layout.MainScreen;

public class GamePlay extends GameData{
	public int numberOfTeams;
	public int whoseTurn;
	
	//how many questions have been chosen
	private int numberOfChosenQuestions;
	//total points for each team, each TeamPoints object holds team index, team points, and team name
	public List<TeamData> teamData;
	
	private static Set<String> unmodifiableSetAnswerVerbs;
	private static Set<String> unmodifiableSetAnswerNouns;
	private static final String EXIT = "exit";
	private static final String RESTART = "restart";
	
	//DOES NOT IMPLEMENT EXIT AND REPLAY LOGIC
	public GamePlay() {
		super();
		//br = new BufferedReader(new InputStreamReader(System.in));
		//initialize private variables
		numberOfChosenQuestions = 0;
		teamData = new ArrayList<>();
		
		initializeAnswerFormatSet();
		//chooseTeams();
		//playGame();
	}
	
	public void initializeAnswerFormatSet(){
		Set<String> nounsModifiableSet = new HashSet<>();
		Set<String> verbsModifiableSet = new HashSet<>();
		nounsModifiableSet.add("who");
		nounsModifiableSet.add("where");
		nounsModifiableSet.add("when");
		nounsModifiableSet.add("what");
		verbsModifiableSet.add("is");
		verbsModifiableSet.add("are");
		
		unmodifiableSetAnswerNouns = Collections.unmodifiableSet(nounsModifiableSet);
		unmodifiableSetAnswerVerbs = Collections.unmodifiableSet(verbsModifiableSet);
	}
	
	//increment whose turn it is
	private int nextTurn(int currentTurn){

		return (currentTurn + 1) == numberOfTeams ? 0 : currentTurn + 1;
	}
	
	//check whether the answer is in the format of a question
	private boolean validAnswerFormat(String answer){
		
		if (answer.length() < 1) return false;
		
		String[] splitAnswer = answer.trim().split("\\s+");
		
		if (splitAnswer.length < 2) return false;
		
		return unmodifiableSetAnswerVerbs.contains(splitAnswer[1].toLowerCase()) && unmodifiableSetAnswerNouns.contains(splitAnswer[0].toLowerCase());
	}
	
	public String chooseTeams(int numberOfTeams, String [] names){
		 
		//System.out.println("Welcome to Jeopardy!");
		//System.out.println("Please enter the number of teams that will be playing in this game");
		
		//determines whether the user gave a valid input
		/*boolean successful = false;
		
		while (!successful){
			
			try{
				String input = br.readLine();
				checkForRestartOrExit(input);
				numberOfTeams = Integer.parseInt(input);
				//this syntax is used throughout the code, so if you don't know how it works, look at the following comment
				// conditional ? true condition action : false condition action
				successful = (numberOfTeams>0) && (numberOfTeams<5) ? true : false;
			}
			catch(IOException ioe){}
			catch(NumberFormatException nfe){}
			finally{
				
				if (!successful){
					System.out.println("Invalid entry; Please try again.");
				}		
			}
		
		}*/
		this.numberOfTeams = numberOfTeams;
		teamData = new ArrayList<>(numberOfTeams);
		Set<String> duplicateTeamNamesCheck = new HashSet<>();

		//choose team names
		for (int i = 1; i <= numberOfTeams; i++){
			
				//System.out.println("Please choose a name for Team "+i);
				
				
					String input = names[i-1];
					
					if (duplicateTeamNamesCheck.contains(input.toLowerCase())){
						String c = "This name has already been chosen by another team. Please choose something else.";
						return c;
					}
					else if (input.trim().equals("")){
						String c = "Your team name cannot be white space.";
						return c;
					}
					else{
						teamData.add(new TeamData(i-1, 0L, input));
						duplicateTeamNamesCheck.add(input.toLowerCase());
						
					}
		
		}
		String c = "pass";
		return c;
		
	}
	
	public Long dataGetPoints(int i){
		return teamData.get(i).getPoints();
	}
	public String dataGetTeam(int i){
		return teamData.get(i).getTeamName();
	}
	
	public void startGame()
	{
		
		//System.out.println("Ready to Play!");
		Random rand = new Random();
		//System.out.println(numberOfTeams);
		int firstTeam = (rand.nextInt(numberOfTeams) < 0) ? -rand.nextInt(numberOfTeams) : rand.nextInt(numberOfTeams);
		//System.out.println("The team to go first will be "+teamData.get(firstTeam).getTeamName());
		
		whoseTurn = firstTeam;
	}
	
	public void checkForRestartOrExit(String line, boolean quick){
		
		line = line.trim().toLowerCase();
		
		if (line.equals(EXIT)){
			System.exit(0);
		}
		
		else if (line.equals(RESTART)){
			resetData();
			//playGame();
			MainScreen msNext = new MainScreen(this, quick);
		}
	}
	
	private void resetData(){
		whoseTurn = -1;
		numberOfChosenQuestions = 0;
		//total points for each team, each TeamPoints object holds team index, team points, and team name
		for (TeamData team : teamData){
			team.setPoints(0);
		}
		
		for (int i = 0; i<5; i++){
			
			for (int j = 0; j<5; j++){
				questions[i][j].resetHasBeenAsked();
			}
		}
		
	}
	
	/*private void askQuestions(){
		
		//while (numberOfChosenQuestions < 25){
			
			//System.out.println("Please choose a Category");
			
			//String category = "";
			//Integer pointValue = -1;
			//int xIndex = -1;
			//int yIndex = -1;
			//boolean successfulChoice = false;
			
			//must loop until valid category and pointValue are chosen
			//while (!successfulChoice){
				//boolean validInputs= false;
				//loops until input for category from user is valid
				//while (!validInputs){
					
					//try {
						
						//String input = br.readLine();
						//checkForRestartOrExit(input);
						//category = input.toLowerCase();
						//validInputs = categoriesMap.containsKey(category.toLowerCase()) ? true : false;
						
					//} 
					//catch (IOException e) {}
					//finally{
						
						//if (!validInputs){
							//System.out.println("Invalid Category choice; Please try again.");
						//}
					//}
				//}
				
				//System.out.println("Please enter the dollar value of the question you wish to answer.");
				//validInputs = false;
				
				//loops until input for point value from user is valid
				while (!validInputs){
					
					try {
						String input = br.readLine();
						checkForRestartOrExit(input);
						pointValue = Integer.parseInt(input);
						validInputs = pointValuesMapToIndex.containsKey(pointValue) ? true : false;
		
					}
					catch (NumberFormatException e) {}
					catch (IOException e) {}
					finally{
						
						if (!validInputs){
							System.out.println("Invalid dollar value; Please try again.");
						}
					}
					
				}
				
				xIndex = categoriesMap.get(category.trim()).getIndex();
				yIndex = pointValuesMapToIndex.get(pointValue);
				
				//check whether this question has been asked already
				successfulChoice = !questions[xIndex][yIndex].hasBeenAsked();
				
				//if (!successfulChoice){
					//System.out.println("This question has already been chosen; Please try again.");
				//}
			}
			
			answerQuestion(xIndex, yIndex, pointValue);
			//if (numberOfChosenQuestions != 25) System.out.println("It is "+teamData.get(whoseTurn).getTeamName()+"'s turn to choose a question!");
		//}

	}*/
	
	public String answerQuestion(int xIndex, int yIndex, int pointValue, String givenAnswer){
		
		questions[xIndex][yIndex].setHasBeenAsked();
		//System.out.println(questions[xIndex][yIndex].getQuestion());
		//System.out.println("Please enter your answer. Remember to pose it as a question.");
		
		//String givenAnswer = "";
		String expectedAnswer = questions[xIndex][yIndex].getAnswer();
		
			//if the answer is not in a question format, give them another chance
			if (!validAnswerFormat(givenAnswer)){
				return "format";
			}
				
			
			else if (!givenAnswer.toLowerCase().endsWith(expectedAnswer.toLowerCase())){
				teamData.get(whoseTurn).deductPoints(pointValue);
				whoseTurn = nextTurn(whoseTurn);
				return "fail";
			}
			
			else{
				teamData.get(whoseTurn).addPoints(pointValue);
				whoseTurn = nextTurn(whoseTurn);
				return "pass";
			}	
		
		
		//Output.printScores(numberOfTeams, teamData);
		
		
	}
	
	public void makeBet(int i, int bet){
		//boolean successfulBet = false;
		
		//let each team make a bet for final jeopardy
		//for (int i = 0; i<finalists.size(); i++){
			TeamData currentTeam = teamData.get(i);
			
			//while (!successfulBet){
				//System.out.println("Team "+currentTeam.getTeamName()+", please give a dollar amount from your total that you would like to bet");
				
				//try {
					//String input = br.readLine();
					//checkForRestartOrExit(input);
					//long tempBet = Integer.parseInt(bet);
					//successfulBet = (tempBet>0) && (tempBet < (currentTeam.getPoints() + 1)) ? true : false;
					currentTeam.setBet(bet);
				//} 
				//catch (NumberFormatException e) {} 
				//catch (IOException e) {}
				//finally{
					
					//if (!successfulBet){
						//System.out.println("Invalid bet; Please try again.");
					//}
				//}
			//}
			
			//successfulBet = false;
		//}

	}
	
	public List<Integer> getNonFinalists(){
		List<Integer> finalTeams = new ArrayList<>();
		
		for (int i = 0; i<teamData.size(); i++){
			
			Integer team = teamData.get(i).getTeam();
			
			if (teamData.get(i).getPoints() <= 0){
				finalTeams.add(team);
			}
			
			//else{
				//System.out.println("Sorry, "+team.getTeamName()+", you have been eliminated from the game!");
			//}
		}
		
		return finalTeams;
	}
	
	public List<Integer> getFinalists(){
		List<Integer> finalTeams = new ArrayList<>();
		
		for (int i = 0; i<teamData.size(); i++){
			
			Integer team = teamData.get(i).getTeam();
			
			if (teamData.get(i).getPoints() > 0){
				finalTeams.add(team);
			}
			
			//else{
				//System.out.println("Sorry, "+team.getTeamName()+", you have been eliminated from the game!");
			//}
		}
		
		return finalTeams;
	}
	
	public void playFinalJeopardy(int index, String answer){
		
		//System.out.println("Welcome to Final Jeopardy!");
		//get the finalists for the round
		//List<TeamData> finalTeams = getFinalists();
		
		//if (finalTeams.isEmpty()){
			//exit ("None of the teams made it to the Final Jeopardy round! Nobody wins, GAME OVER!");
		//}
		
		//have all the teams make a bet
		//makeBets(finalTeams);
		
		//System.out.println("The question is: ");
		//System.out.println(finalJeopardyQuestion);
		
		//have each question provide an answer and calculate their new score
		//for (int i = 0; i<finalTeams.size(); i++){
			TeamData currentTeam = teamData.get(index);
			//System.out.println("Team "+currentTeam.getTeamName()+", please enter your answer.");
			//String answer = "";
			
			//try {
				//answer = br.readLine();
				//checkForRestartOrExit(answer);
				
				if (!validAnswerFormat(answer.trim())){
					//System.out.println("Invalid question format. Your answer will be marked as incorrect");
					currentTeam.deductPoints(currentTeam.getBet());
				}
				
				else{
					
					if (answer.toLowerCase().endsWith(finalJeopardyAnswer.toLowerCase())){
						currentTeam.addPoints(currentTeam.getBet()); 
					}
					
					else{
						currentTeam.deductPoints(currentTeam.getBet());
					}
				}
				
			 
			//catch (IOException e) {
			//	System.out.println("Something went wrong!");
			//}
			
			//Output.printScores(finalTeams.size(), finalTeams);
		//}
		
		//ArrayList<Integer> winners = getWinners(finalTeams);
		/*
		if (winners.size() == 0){
			System.out.println("There were no winners!");
		}
		
		else{
			String toPrint = winners.size() > 1 ? "And the winners are " : "And the winner is ";
			System.out.print(toPrint + finalTeams.get(winners.get(0)).getTeamName());
			
			if (winners.size() > 1){
				
				for (int i = 1; i<winners.size(); i++){
					System.out.print(", " + finalTeams.get(winners.get(i)).getTeamName());
				}
			}
		}*/
	}
	
	
	public ArrayList<Integer> getWinners(){
		
		ArrayList<Integer> winners = new ArrayList<>();
		int max = 0;
		
		for(int i = 0; i < numberOfTeams; i++){
			if(teamData.get(i).getPoints() > max){
				winners.clear();
				winners.add(i);
			}
			else if(teamData.get(i).getPoints() == max){
				winners.add(i);
			}
		}
		
		return winners;
	}

}
