package other_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import frames.LoginGUI;
import frames.MainGUI;
import game_logic.GameData;
import game_logic.User;

public class FinalNetworkedGUI extends FinalJeopardyGUI{

	public FinalNetworkedGUI(GameData gameData, MainGUI mainGUI2) {
		super(gameData, mainGUI2);
		// TODO Auto-generated constructor stub
		createGUI();
	}
	
	public void createGUI(){
		setLayout(new GridLayout(7,1));
		add(titlePanel);
		
		JPanel teamAnswerPanel = new JPanel(new BorderLayout());
		JPanel teamBetPanel = new JPanel(new BorderLayout());

		JPanel betLabelAndButtonPanel = new JPanel(new GridLayout(1, 2));
		JPanel sliderPanel = new JPanel(new GridLayout(1, 2));

		TeamGUIComponents team = gameData.getFinalistsAndEliminatedTeams().getFinalists().get(0);
		// initialize the team's slider based on their total, and add action
		// listeners to its buttons
		team.prepareForFinalJeopardy(this, gameData);
		
		sliderPanel.setPreferredSize(new Dimension(800, 100));
		teamAnswerPanel.setPreferredSize(new Dimension(500, 60));
		AppearanceSettings.setBackground(Color.darkGray, betLabelAndButtonPanel, sliderPanel, teamAnswerPanel,
				teamBetPanel);

		// create/add this team's answer panel
		teamAnswerPanel.add(team.getFJAnswerTextField(), BorderLayout.CENTER);
		teamAnswerPanel.add(team.getFJAnswerButton(), BorderLayout.EAST);

		sliderPanel.add(team.getBetSlider());
		sliderPanel.add(betLabelAndButtonPanel);

		betLabelAndButtonPanel.add(team.getBetLabel());
		betLabelAndButtonPanel.add(team.getBetButton());

		teamBetPanel.add(team.getBetSlider(), BorderLayout.CENTER);
		teamBetPanel.add(betLabelAndButtonPanel, BorderLayout.EAST);
		teamBetPanel.add(team.getFJTeamNameLabel(), BorderLayout.WEST);

		answerPanel.add(teamAnswerPanel);
		// add this team's bet panel
		add(teamBetPanel);
	}
	
	public static void main (String [] args){

		GameData gd = new GameData();
		User user = new User("blah","blah");
		MainGUI main = new MainGUI(gd, user);
		new FinalNetworkedGUI(gd, main).setVisible(true);
	}
}

