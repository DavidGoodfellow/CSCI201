import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import java.util.List;

public class Main {
	
	//VARIABLES
	private GameSystem game;
	private List<Player> players;
	
	//CONSTRUCTOR
	public Main(){
		game = new GameSystem();
		players = new ArrayList<Player>();
	}
	
	//GAMEPLAY
	public void play(){
		
		//scanner isn't closing where I put it?
		Scanner s = new Scanner(System.in);
		
		//PLAYERS HAVE BEEN INITIALIZED
		System.out.println("Setting up the game for you...");
		System.out.println("Ready to Play!");
		
		//----------------------------------------
		// CLEARING PREVIOUS DATA
		//----------------------------------------
		game.restart();
		for(int i = 0; i < players.size(); i++){
			players.get(i).setPoints(0);
		}
		
		//------------------------------------------
		//           REGULAR  TURNS
		//------------------------------------------
		
		int playerNum = 0;
		while(game.isComplete() == false){
			System.out.println("It is " + players.get(playerNum).getName() + " turn to choose a question!");
			System.out.println("Please choose a category.");
			
			String str1 = s.nextLine();
			if(str1 == "exit")
				return;
			if(str1 == "restart"){
				play();
				return;
			}	
			
			int c = game.chooseCategory(str1);
			while(c == -1){
				System.out.println("This category does not exist, please choose another.");
				String str2 = s.nextLine();
				if(str2 == "exit")
					return;
				if(str2 == "restart"){
					play();
					return;
				}
				c = game.chooseCategory(str2);
			}
			System.out.println("Please choose a dollar value for the question.");
			String str3 = s.nextLine();
			if(str3 == "exit")
				return;
			if(str3 == "restart"){
				play();
				return;
			}
			int r = game.chooseValue(Integer.parseInt(str3));
			while(r == -1){
				System.out.println("This dollar value does not exist, please choose another.");
				String str4 = s.nextLine();
				if(str4 == "exit")
					return;
				if(str4 == "restart"){
					play();
					return;
				}
				r = game.chooseValue(Integer.parseInt(str4));
			}
			String rc = Integer.toString(r) + Integer.toString(c);
			if(game.isFlipped(rc) == true){
				System.out.println("Tile is already played, restarting your turn...");
				continue;
			}
			//question is valid, running it now
			game.question(rc);
			String strA = s.nextLine();
			if(strA == "exit")
				return;
			if(strA == "restart"){
				play();
				return;
			}
			if(game.answer(rc, strA)){//is the answer correct?
				players.get(playerNum).addPoints(r);
			}
			else{
				players.get(playerNum).minusPoints(r);
			}
			
			//outputting the updated point values of all players after turn
			System.out.println("----------PLAYER SCORES----------");
			for(int k = 0; k < players.size(); k++){
				System.out.println(players.get(k).getName() + ": " + players.get(k).getPoints());
			}
			System.out.println("---------------------------------");
			
			//if last player, go bak to the first team's turn
			playerNum++;
			if(playerNum == players.size()){
				playerNum = 0;
			}
		}
		
		//-----------------------------------------
		// FINAL Q & END OF GAME
		//-----------------------------------------
		
		//getting all of the bets
		System.out.println("Now that all the questions have been chosen, it's time for Final Jeopardy!");

		for(int x = 0; x < players.size(); x++){
			System.out.println("Team " + players.get(x).getName() + ", please give a dollar amount from your total that would like to bet");
			boolean okay = false;
			while(okay != true){
				String str = s.nextLine();
				if(str == "exit")
					return;
				if(str == "restart"){
					play();
					return;
				}
				int bet = Integer.parseInt(str);
				if(players.get(x).getPoints() < 1){
					System.out.println("Since you have no money, your bet will be 0.");
					players.get(x).finalBet = 0;
				}
				
				else if(bet < 1){
					System.out.println("You must place a bet of at least $1. Please enter your new bet.");
					continue;
				}
				else if(bet > players.get(x).getPoints()){
					System.out.println("Your bet cannot be more than what money you have available. Please enter your new bet.");
					continue;
				}
				else{
					players.get(x).finalBet = bet;
				}
			}
			
		}//bets recorded
		
		//answering the question
		System.out.println("The question is: ");
		game.finalQ();
		List<Integer> correctAnswers = new ArrayList<Integer>();
		for(int x = 0; x < players.size(); x++){
			System.out.println("Team " + players.get(x).getName() + ", please enter your answer.");
			
			String str2 = s.nextLine();
			if(str2 == "exit")
				return;
			if(str2 == "restart"){
				play();
				return;
			}
			
			if(game.answer("FJ",str2) == true){
				players.get(x).addPoints(players.get(x).finalBet);
				correctAnswers.add(x);
			}
			else{
				players.get(x).minusPoints(players.get(x).finalBet);
			}
		}
		//showing who got the question right
		System.out.println("The following players got the final question correct: ");
		for(int y = 0; y < correctAnswers.size(); y++){
			System.out.println(players.get(correctAnswers.get(y)).getName());
		}
		//announcing the winner of the game
		int pointsMax = players.get(0).getPoints();
		int winner = 0;
		for(int x=1; x < players.size(); x++){
			if(players.get(x).getPoints() > pointsMax){
				pointsMax = players.get(x).getPoints();
				winner = x;
			}
		}
		System.out.println("The winning team is Team " + players.get(winner).getName());
		
		s.close();
	}

	//MAIN METHOD USED FOR STREAMING
	public void main(String []args){
		//have to do something about the location of the game file as an argument
		
		//--------------------------------------------
		//    streaming into game play object
		//--------------------------------------------
		
		try {
			//opening file and instantiating the game
			
			//where to close it?
			System.out.println("here");
			BufferedReader br = new BufferedReader(new FileReader("gameFile.txt"));
			System.out.println("here");
			//starting with categories
			String str = br.readLine();
			String categories[] = str.split("::");
			if(categories.length != 5){
				System.out.println("Error: Wrong amount of categories.");
				throw new IOException();
			}
			for(int i = 0; i < categories.length; i++){
				System.out.println(categories[i]);
				if(game.addCategory(categories[i], i+1) == false){
					System.out.println("Error: Duplicate categories.");
					throw new IOException();
				}
			}
			
			//streaming in values
			str = br.readLine();
			String values[] = str.split("::");
			if(values.length != 5){
				System.out.println("Error: Wrong amount of values.");
				throw new IOException();
			}
			for(int i = 0; i < values.length; i++){
				if(game.addValue(Integer.parseInt(values[i]), i+1) == false){
					System.out.println("Error: Duplicate Values.");
					throw new IOException();
				}
			}
			
			//deal with all the questions
			while(br.readLine() != null){
				//List<String> entry = new ArrayList<String>();
				//HOW TO READ IN FULL LINES
				
				if(game.questionInput(br.readLine()) == false){
					throw new IOException();
				}
			}
			
			if(game.getQCount() != 26){
				System.out.println("Error: Not exactly 26 questions.");
			}
			else if(game.inlcudedFQ() == false){
				System.out.println("Error: No final question is included");
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//don't print stack trace just overall fail the program!!
			e.printStackTrace();
		}
		
		//----------------------------------------------------
		//STREAMING COMPLETE, STARTING TO PLAY
		//----------------------------------------------------
		//starting sequence
		System.out.println("Welcome to Jeopardy!");
		System.out.println("Please enter the number of teams that will play");
		Scanner s = new Scanner(System.in);
		int numberOfTeams = Integer.parseInt(s.nextLine());
		while(numberOfTeams < 1 || numberOfTeams > 4){
			System.out.println("Invalid entry; Please try again.");
			numberOfTeams = Integer.parseInt(s.nextLine());
		}
				
		//naming teams
		System.out.println("Please choose a name for Team " + 1);
		String str = s.nextLine();
		Player p1 = new Player(str);
		players.add(p1);
				
		int i = 1; 
		while(i < numberOfTeams){
			System.out.println("Please choose a name for Team " + (i + 1));
			//how to name them all differently in a while loop?????
			str = s.nextLine();
			for(int j = 0; j < players.size(); i++){
				if(str == players.get(j).getName()){
					System.out.println("Please choose a different name from all other teams.");
					continue;
				}
			}
			Player p2 = new Player(str);
			players.add(p2);
			i++;
		}
		s.close();
		
		play();
		
		
		
	}
	
}
