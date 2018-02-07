package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Animation.ClockAnimation;
import game_logic.Category;
import game_logic.GameData;
import game_logic.Timer15;
import game_logic.User;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import other_gui.QuestionGUIElement;
import other_gui.TeamGUIComponents;

public class MainGUI extends JFrame {

	protected JPanel mainPanel;
	protected JPanel currentPanel;

	private JPanel questionsPanel;
	protected GameData gameData;
	protected JButton[][] questionButtons;

	protected static final int QUESTIONS_LENGTH_AND_WIDTH = 5;

	private JTextArea updatesTextArea;
	protected JMenuBar menuBar;
	protected JMenu menu;

	protected JMenuItem logoutButton;
	protected JMenuItem exitButton;
	protected JMenuItem restartThisGameButton;
	protected JMenuItem chooseNewGameFileButton;
	
	//public MyTimer timer;
	
	protected JLabel currentTeamLabel;
	protected JPanel progressPanel;
	protected List<JLabel> clockLabels;
	protected JLabel jeopardyLabel;
	//in case we need to know which user is logged in
	protected User loggedInUser;

	public MainGUI(GameData gameData) {
		super("Jeopardy!!");
		make(gameData);
		
	}
	//called from constructor of MainGUINetworked
	public MainGUI(User loggedInUser){
		super("Jeopardy!!  "+loggedInUser.getUsername());
		clockLabels = new ArrayList<JLabel>();
	}
	
	public void setCurrentTeamLabel(String txt){
		currentTeamLabel.setText(txt);
	}
	
	public void setClock(){
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			if (i == gameData.getCurrentTeam().getTeamIndex())
				clockLabels.get(i).setVisible(true);
			else
				clockLabels.get(i).setVisible(false);
		}
	}
	public void setClock(int j){
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			if (i == j)
				clockLabels.get(i).setVisible(true);
			else
				clockLabels.get(i).setVisible(false);
		}
	}
	
	public void noClock(){
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			clockLabels.get(i).setVisible(false);
		}
	}
	
	//move constructor logic to a protected method so MainGUINetworked can call it
	protected void make(GameData gameData){
		this.gameData = gameData;
		initializeComponents();
		createGUI();
		addListeners();
	}

	// public methods
	public void addUpdate(String update) {
		updatesTextArea.append("\n" + update);
	}

	// this method changes the current panel to the provided panel
	public void changePanel(JPanel panel) {
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel, BorderLayout.CENTER);
		// must repaint or the change won't show
		repaint();
		revalidate();
	}
	
	public JPanel getCurrentPanel(){
		return currentPanel;
	}

	public void showMainPanel() {
		changePanel(mainPanel);
		if(this instanceof MainGUINetworked){
			((MainGUINetworked) this).reset15();
		}
	}

	// private methods
	private void initializeComponents() {
		mainPanel = new JPanel();
		currentPanel = mainPanel;
		exitButton = new JMenuItem("Exit Game");
		restartThisGameButton = new JMenuItem("Restart This Game");
		chooseNewGameFileButton = new JMenuItem("Choose New Game File");
		logoutButton = new JMenuItem("Logout");
		updatesTextArea = new JTextArea("Welcome to Jeopardy!");
		menu = new JMenu("Menu");
		questionButtons = new JButton[QUESTIONS_LENGTH_AND_WIDTH][QUESTIONS_LENGTH_AND_WIDTH];
		menuBar = new JMenuBar();
		questionsPanel = new JPanel(new GridLayout(QUESTIONS_LENGTH_AND_WIDTH, QUESTIONS_LENGTH_AND_WIDTH));
		clockLabels = new ArrayList<JLabel>(gameData.getNumberOfTeams());
	}
	
	private void createGUI() {

		createMenu();
		createMainPanel();

		add(mainPanel, BorderLayout.CENTER);
		add(createProgressPanel(), BorderLayout.EAST);
		setSize(1500, 825);
	}

	// creating the JMenuBar
	protected void createMenu() {

		menu.add(restartThisGameButton);
		menu.add(chooseNewGameFileButton);
		menu.add(logoutButton);
		menu.add(exitButton);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	// creating the main panel (the game board)
	private void createMainPanel() {
		mainPanel.setLayout(new BorderLayout());

		// getting the panel that holds the 'jeopardy' label
		JPanel jeopardyPanel = createJeopardyPanel();
		// getting the cateogries panel
		JPanel categoriesPanel = createCategoriesAndQuestionsPanels();
		JPanel northPanel = new JPanel();

		northPanel.setLayout(new BorderLayout());
		northPanel.add(jeopardyPanel, BorderLayout.NORTH);
		northPanel.add(categoriesPanel, BorderLayout.SOUTH);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(questionsPanel, BorderLayout.CENTER);
	}
	
	public void setJeopardyLabel(int i){
		jeopardyLabel.setText("Jeopardy :" + i);
	}

	// creates the panel with the jeopardy label
	private JPanel createJeopardyPanel() {
		JPanel jeopardyPanel = new JPanel();
		jeopardyLabel = new JLabel("Jeopardy");
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, jeopardyPanel, jeopardyLabel);
		jeopardyLabel.setFont(AppearanceConstants.fontLarge);
		jeopardyPanel.add(jeopardyLabel);

		return jeopardyPanel;
	}
	
	//we want to override this in MainGUINetworked which is why I pulled the logic out into its own method
	protected void populateQuestionButtons(){
		// place the question buttons in the proper indices in 'questionButtons'
		for (QuestionGUIElement question : gameData.getQuestions()) {

			// adding action listeners to the question
			question.addActionListeners(this, gameData);
			questionButtons[question.getX()][question.getY()] = question.getGameBoardButton();
		}

	}

	// creates both the categories panel and the questions panel
	private JPanel createCategoriesAndQuestionsPanels() {
		JPanel categoriesPanel = new JPanel(new GridLayout(1, QUESTIONS_LENGTH_AND_WIDTH));
		AppearanceSettings.setBackground(Color.darkGray, categoriesPanel, questionsPanel);

		Map<String, Category> categories = gameData.getCategories();
		JLabel[] categoryLabels = new JLabel[QUESTIONS_LENGTH_AND_WIDTH];

		// iterate through the map of categories, and place them in the correct index
		for (Map.Entry<String, Category> category : categories.entrySet()) {
			categoryLabels[category.getValue().getIndex()] = category.getValue().getCategoryLabel();
		}

		populateQuestionButtons();
		// actually adding the categories and questions into their respective panels
		for (int i = 0; i < QUESTIONS_LENGTH_AND_WIDTH; i++) {

			categoryLabels[i].setPreferredSize(new Dimension(100, 70));
			categoryLabels[i].setIcon(Category.getIcon());
			categoriesPanel.add(categoryLabels[i]);

			for (int j = 0; j < QUESTIONS_LENGTH_AND_WIDTH; j++) {
				// have to use opposite indices because of how GridLayout adds components
				questionsPanel.add(questionButtons[j][i]);
			}
		}

		return categoriesPanel;
	}

	// creates the panel with the team points, and the Game Progress area
	private JPanel createProgressPanel() {
		// create panels
		GridLayout gl = new GridLayout(1,3);
		gl.setHgap(40);
		
		progressPanel = new JPanel(gl);
		
		JPanel pointsPanel = new JPanel(new GridLayout(gameData.getNumberOfTeams(), 3));//3 for clock image
		JPanel southEastPanel = new JPanel(new BorderLayout());
		JPanel eastPanel = new JPanel();
		// other local variables
		JLabel updatesLabel = new JLabel("Game Progress");
		//JPanel middleTemp = new JPanel();
		currentTeamLabel = new JLabel("temp");
		
		JScrollPane updatesScrollPane = new JScrollPane(updatesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// setting appearances
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, southEastPanel, updatesLabel, progressPanel, currentTeamLabel, updatesScrollPane,
				updatesTextArea);
		AppearanceSettings.setSize(400, 400, pointsPanel, updatesScrollPane);
		AppearanceSettings.setTextComponents(updatesTextArea);

		updatesLabel.setFont(AppearanceConstants.fontLarge);
		pointsPanel.setBackground(Color.darkGray);
		
		progressPanel.add(updatesLabel);
		//progressPanel.add(middleTemp);
		progressPanel.add(currentTeamLabel);
		
		progressPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		updatesScrollPane.setBorder(null);

		updatesTextArea.setText("Welcome to Jeopardy!");
		updatesTextArea.setFont(AppearanceConstants.fontSmall);
		updatesTextArea.append("The team to go first will be " + gameData.getCurrentTeam().getTeamName());
		setCurrentTeamLabel(gameData.getCurrentTeam().getTeamName());
		
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		// adding components/containers
		southEastPanel.add(progressPanel, BorderLayout.NORTH);
		southEastPanel.add(updatesScrollPane, BorderLayout.CENTER);

		// adding team labels, which are stored in the TeamGUIComponents class,
		// to the appropriate panel
		
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			TeamGUIComponents team = gameData.getTeamDataList().get(i);
			pointsPanel.add(team.getMainTeamNameLabel());
			
			ClockAnimation clockA = new ClockAnimation();
			Thread t = new Thread(clockA);
			clockLabels.add(clockA);
			pointsPanel.add(clockA);
			clockA.setVisible(false);
			t.start();
			pointsPanel.add(team.getTotalPointsLabel());
		}
		setClock();

		eastPanel.add(pointsPanel);
		eastPanel.add(southEastPanel);
		
		//eastPanel.setPreferredSize(new Dimension());
		return eastPanel;
	}
	

	public void resetGame(){
		updatesTextArea.setText("Game has been restarted.");
		gameData.restartGame();
		// reset all data
		// repaint the board to show updated data
		showMainPanel();
		addUpdate("The team to go first will be " + gameData.getCurrentTeam().getTeamName());
		setCurrentTeamLabel(gameData.getCurrentTeam().getTeamName());
		setClock();
		if(this instanceof MainGUINetworked){
			Timer15 t15 = new Timer15((MainGUINetworked) this);
			t15.start();
		}
	}
	// adding even listeners, made this protected so MainGUINetworked can override it
	protected void addListeners() {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//add window listener
		addWindowListener(new ExitWindowListener(this));
		//add action listeners
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}

		});

		restartThisGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetGame();
			}

		});

		chooseNewGameFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new StartWindowGUI(loggedInUser).setVisible(true);
			}

			
		});
		
		logoutButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginGUI().setVisible(true);
			}
			
		});
	}

}
