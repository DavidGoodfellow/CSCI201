package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import frames.MainGUI;
import frames.WinnersGUI;
import game_logic.GameData;
import game_logic.QuestionAnswer;
import game_logic.TeamData;
import listeners.TextFieldFocusListener;

public class QuestionGUIElement extends QuestionAnswer {

	//the JButton displayed on the game board grid on MainGUI associated with this question
	private JButton gameBoardButton, passButton;
	//the panel that will be switched out with the main panel when the gameBoardButton is clicked
	private JPanel questionPanel;
	//the label that would show whether there was a format problem with the submitted answer
	private JLabel announcementsLabel;
	//components for the questionPanel
	private JLabel categoryLabel;
	private JLabel pointLabel;
	private JLabel teamLabel;
	private JTextPane questionLabel;
	private JTextField answerField;
	private JButton submitAnswerButton;
	private int originalWhoseTurn;
	private String enabledImage;
	
	private Boolean hadSecondChance;
	
	public QuestionGUIElement(String question, String answer, String category, int pointValue, int indexX, int indexY, String enabledImage) {
		super(question, answer, category, pointValue, indexX, indexY);
		hadSecondChance = false;
		this.enabledImage = enabledImage;
		
		initializeComponents();
		createGUI();
		addActionListeners();
	}
	
	//public methods
	public JButton getGameBoardButton(){
		return gameBoardButton;
	}
	//this method is called from the MainGUI; cannot add the action listeners until then
	public void addActionListeners(MainGUI mainGUI, GameData gameData){
		submitAnswerButton.addActionListener(new SubmitAnswerActionListener(mainGUI, gameData));
		gameBoardButton.addActionListener(new GameBoardActionListener(mainGUI, gameData));
		
		//if the pass button is clicked, perform a operation to pass through the submit button with key to inform them
		passButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				answerField.setText("pass button was just clicked!!!");
				SubmitAnswerActionListener sa = new SubmitAnswerActionListener(mainGUI, gameData);
				sa.actionPerformed(e);
			}

		});
		
	}
	
	//method used to reset the data of this question to it's default
	//called from MainGUI when user chooses 'Restart This Game' option on the menu
	public void resetQuestion(String imageIcon){
		submitAnswerButton.setEnabled(true);
		asked = false;
		teamLabel.setText("");
		gameBoardButton.setEnabled(true);
		gameBoardButton.setIcon(new ImageIcon(imageIcon));
		answerField.setText("");
	}
	
	//private methods
	
	//initialize member variables
	private void initializeComponents(){
		
		questionPanel = new JPanel();
		gameBoardButton = new JButton("$"+pointValue, new ImageIcon(enabledImage));
		gameBoardButton.setHorizontalTextPosition(JButton.CENTER);
		gameBoardButton.setVerticalTextPosition(JButton.CENTER);
		gameBoardButton.setForeground(Color.BLACK);
		pointLabel = new JLabel("$"+ pointValue);
		categoryLabel = new JLabel(category);
		questionLabel = new JTextPane();
		announcementsLabel = new JLabel("");
		answerField = new JTextField("Enter your answer.");
		submitAnswerButton = new JButton("Submit Answer");
		teamLabel = new JLabel("");
		passButton = new JButton("Pass");
	}
	
	private void createGUI(){
	
		//local variables
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel answerPanel = new JPanel(new BorderLayout());
		JPanel formatErrorPanel = new JPanel();
		JPanel northPanel = new JPanel();
		//appearance settings
		AppearanceSettings.setBackground(Color.darkGray, gameBoardButton, questionPanel, announcementsLabel, answerPanel, formatErrorPanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabel, pointLabel, categoryLabel, infoPanel);
		AppearanceSettings.setForeground(Color.lightGray, teamLabel, pointLabel, categoryLabel, announcementsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, questionLabel, teamLabel, pointLabel, categoryLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, gameBoardButton, announcementsLabel, submitAnswerButton, answerField);
		AppearanceSettings.setTextAlignment(teamLabel, pointLabel, categoryLabel, announcementsLabel);
		
		questionLabel.setText(question);
		questionLabel.setEditable(false);
		// sourced from: http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
		//centers the text in the question pane
		StyledDocument doc = questionLabel.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		gameBoardButton.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
		gameBoardButton.setOpaque(true);
		answerField.setForeground(Color.gray);
		questionLabel.setBackground(AppearanceConstants.lightBlue);
		
		//components that need their size set
		questionLabel.setPreferredSize(new Dimension(800, 400));
		answerField.setPreferredSize(new Dimension(600, 100));
		infoPanel.setPreferredSize(new Dimension(900, 80));
		formatErrorPanel.setPreferredSize(new Dimension(800, 100));
		
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		//add components to the panels
		infoPanel.add(teamLabel, BorderLayout.EAST);
		infoPanel.add(categoryLabel, BorderLayout.CENTER);
		infoPanel.add(pointLabel, BorderLayout.WEST);

		
		JPanel answerX = new JPanel();
		answerX.setLayout(new BorderLayout());
		answerX.add(answerField, BorderLayout.CENTER);
		answerX.add(submitAnswerButton, BorderLayout.EAST);
		answerX.setBackground(Color.darkGray);
		passButton.setVisible(false);
		answerPanel.add(passButton, BorderLayout.SOUTH);
		answerPanel.add(answerX, BorderLayout.CENTER);
		
		formatErrorPanel.add(announcementsLabel);
		
		northPanel.add(infoPanel);
		northPanel.add(formatErrorPanel);
		
		questionPanel.add(northPanel, BorderLayout.NORTH);
		questionPanel.add(questionLabel, BorderLayout.CENTER);
		questionPanel.add(answerPanel, BorderLayout.SOUTH);

	}
	
	//add focus listener to answer text field, and the rest of the action listeners will be added later from a call from MainGUI
	private void addActionListeners(){
		answerField.addFocusListener(new TextFieldFocusListener("Enter your answer", answerField));
		
		 answerField.addKeyListener(new KeyAdapter() {
	            public void keyReleased(KeyEvent e) {
	                
	            }

	            public void keyTyped(KeyEvent e) {
	                // TODO: Do something for the keyTyped event
	            	passButton.setEnabled(false);
	            }

	            public void keyPressed(KeyEvent e) {
	                // TODO: Do something for the keyPressed event
	            }
	        });
	}
	
	//private listener classes
	//action listener for gameBoardButton
	private class GameBoardActionListener implements ActionListener{

		private MainGUI mainGUI;
		private GameData gameData;
		
		public GameBoardActionListener(MainGUI mainGUI, GameData gameData){
			this.mainGUI = mainGUI;
			this.gameData = gameData;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//change panel to the question panel
			mainGUI.changePanel(questionPanel);
			//set the label of which team chose the question
			teamLabel.setText(gameData.getCurrentTeam().getTeamName());
			originalWhoseTurn = gameData.getCurrentTeamIndex();
		}
		
	}
	
	//action listener for submitAnswerButton
	private class SubmitAnswerActionListener implements ActionListener{

		private MainGUI mainGUI;
		private GameData gameData;
		private String update;
		private Boolean answered;
		
		public SubmitAnswerActionListener(MainGUI mainGUI, GameData gameData){
			this.mainGUI = mainGUI;
			this.gameData = gameData;
			answered = false;
			update = "";
		}
		
		//checks the answer taken from answerField and determines whether the player gets a second chance to answer
		private boolean checkAnswer(){
			
			String givenAnswer = answerField.getText();
			TeamData team = gameData.getCurrentTeam();
			//valid format
			if (gameData.validAnswerFormat(givenAnswer)){
				//team got the answer right
				if (givenAnswer.trim().toLowerCase().endsWith(answer.toLowerCase())){
					team.addPoints(pointValue);
					update = team.getTeamName()+" got the answer right! $"+pointValue+" will be added to their total. ";
					//answer is true, meaning no second chance
					answered = true;
					return true;
				}
				//team got the answer wrong
				else{
					team.deductPoints(pointValue);
					update = team.getTeamName()+" got the answer wrong! $"+pointValue+" will be deducted from their total. ";
					return false;
				}
				
			}
			//valid format
			else{
				passButton.setEnabled(false);
				//if the user already had a second chance, deduct points
				if (hadSecondChance){
					announcementsLabel.setText("Your answer is still formatted incorrect. $"+pointValue+" will be deducted from your total.");
					team.deductPoints(pointValue);
					//answered = true;
					update = team.getTeamName()+", your answer is still formatted incorrect. $"+pointValue+" will be deducted from their total. ";
					return false;
				}
				//if user has not had second chance yet, so answered = false
				else{
					announcementsLabel.setText("Invalid format of your answer. Remember to pose it as a question");
					answerField.setText("");
					hadSecondChance = true;
					update = team.getTeamName()+" had a badly formatted answer. They will get a second chance to answer";
					return true;
				}
			}
		}
		
		//functionality for when the question has been answered
		private void questionHasBeenAnswered(){
			
			passButton.setVisible(false);
			passButton.setEnabled(true);
			gameBoardButton.setEnabled(false);
			gameBoardButton.setIcon(new ImageIcon(gameData.disabledImage));;
			//increment the number of chosen questions
			gameData.updateNumberOfChosenQuestions();
			//have all the questions been asked? if so, time for final jeopardy
			if (gameData.readyForFinalJeoaprdy()){
				//in case we are playing a Quick Play game, disable remaining buttons
				gameData.disableRemainingButtons();
				playFinalJeopardy();
			}
			//not time for final jeopardy
			else{
				//change who's turn it is
				gameData.chooseTurn(originalWhoseTurn);
				gameData.nextTurn();
				mainGUI.addUpdate("It's "+gameData.getCurrentTeam().getTeamName()+" turn to choose!");
				//go back to main screen
				mainGUI.showMainPanel();
			}
			
		}
		//functionality to determine if the teams are eligible to play final jeopardy
		private void playFinalJeopardy(){
			mainGUI.addUpdate("It's time for Final Jeopardy!");
			//figure out the teams that qualified for final jeopardy
			gameData.determineFinalists();
			//if there are no qualifying teams, pop up a WinnersGUI (showing no one won)
			if (gameData.getFinalistsAndEliminatedTeams().getFinalists().size() == 0){
				mainGUI.showMainPanel();
				new WinnersGUI(gameData).setVisible(true);
			}
			//if there are final teams, change the current panel to the final jeopardy view
			else{
				mainGUI.changePanel(new FinalJeopardyGUI(gameData, mainGUI));
			}
		
		}
		
		public void next(){
			gameData.nextTurn();
			passButton.setVisible(true);
			passButton.setEnabled(true);
			if(gameData.getCurrentTeamIndex() == originalWhoseTurn){
				mainGUI.addUpdate("None of the teams were able to get the question right! The answer was " + answer.toLowerCase());
				answered = true;
			}
			else{
				mainGUI.addUpdate("It's " + gameData.getCurrentTeam().getTeamName() + "'s turn to answer the question.");
				announcementsLabel.setText("It's " + gameData.getCurrentTeam().getTeamName() + "'s turn to answer the question.");
				teamLabel.setText(gameData.getCurrentTeam().getTeamName());
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//check whether their answer was correct, and whether we should navigate back to the main screen
			boolean answeredCorrectly = false;
			//this is where you need to iterate through updating. once 
			if(answerField.getText().equals("pass button was just clicked!!!"))
				update = gameData.getCurrentTeam().getTeamName() + " decided to pass.";
			else
				answeredCorrectly = checkAnswer();
			mainGUI.addUpdate(update);
			//update team point label on MainGUI
			gameData.getCurrentTeam().updatePointsLabel();
			
			if(answeredCorrectly == false){
				next();
			}
			answerField.setText("");
			//if it has been answered, check if the game has finished
			if (answered) questionHasBeenAnswered();
		}
		
	}
}
