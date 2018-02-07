package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game_logic.Category;
import game_logic.GameData;
import game_logic.User;
import listeners.ExitWindowListener;
import networkMessages.ChatClient;
import networkMessages.ChatMessage;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import other_gui.FinalNetworkedGUI;
import other_gui.QuestionGUIElement;
import other_gui.TeamGUIComponents;

public class MainGUI extends JFrame {

	private JPanel mainPanel, pointsPanel, eastPanel, southEastPanel;
	private JPanel currentPanel;

	private ChatClient client;

	private JPanel questionsPanel;
	private GameData gameData;
	private JButton[][] questionButtons;

	private static final int QUESTIONS_LENGTH_AND_WIDTH = 5;

	private JTextArea updatesTextArea;
	private JMenuBar menuBar;
	private JMenu menu;
	
	private JLabel[] scoreLabels;

	//JPanel[][] panelHolder;

	private JMenuItem logoutButton;
	private JMenuItem exitButton;
	private JMenuItem restartThisGameButton;
	private JMenuItem chooseNewGameFileButton;
	// in case we need to know which user is logged in
	private User loggedInUser;

	public MainGUI(GameData gameData, User user) {
		super("Jeopardy!!");
		
		client = null;
		this.gameData = gameData;
		loggedInUser = user;
		initializeComponents();
		createGUI();
		addListeners();
		//gameData.attachMainToQ(this);
	}

	// for networked games
	public MainGUI(GameData gameData, User user, ChatClient client) {
		super("Jeopardy!!");

		this.client = client;
		this.gameData = gameData;
		loggedInUser = user;
		System.out.println(this.gameData.getNumberOfQuestions());
		initializeComponents();
		createGUI();
		addListeners();
		//gameData.attachMainToQ(this);
	}
	
	// for networked games
	public MainGUI(GameData gameData, User user, ChatClient client, boolean b) {
		super("Jeopardy!!");

		this.client = client;
		this.gameData = gameData;
		loggedInUser = user;
		System.out.println(this.gameData.getNumberOfQuestions());
		initializeComponents();
		createGUI();
		addListeners();
		updatesTextArea.setText("Game has been restarted by the host. \n " + "The team to go first will be "
				+ gameData.getCurrentTeam().getTeamName());
		// repaint the board to show updated data
		//showMainPanel();
		gameData.attachMainToQ(this);
	}

	public boolean isNetworked() {
		if (client == null)
			return false;
		else
			return true;
	}

	public ChatClient getClient() {
		return client;
	}
	
	public void rate(){
		WinnersAndRatingGUI ratingGUI = new WinnersAndRatingGUI(gameData);
		ratingGUI.setVisible(true);
		this.setVisible(false);
	}

	public void restart() {
		gameData.restartGame();
		new MainGUI(gameData, loggedInUser, client, true).setVisible(true);
		
		dispose();
	}
	
	public String getUsername(){
		return loggedInUser.getUsername();
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}
	
	public void setMainPanel(JPanel panel){
		mainPanel = panel;
		
	}
	public JPanel getMainPanel(){
		return mainPanel;
	}

	public void someoneBuzzedUpdate1() {
		System.out.println((gameData == null) ? "NULL" : "NOT NULL");
		if (client != null)
			client.someoneBuzzedUpdate(gameData, loggedInUser.getUsername());
	}

	// public methods
	public void addUpdate(String update) {
		if (client != null) {
			if(update.equals("It's time for Final Jeopardy!")){
				System.out.println("It's time for Final Jeopardy!");
			}
			client.sendMessage(new ChatMessage("add update", update));
		} else {
			updatesTextArea.append("\n" + update);
		}
	}

	public void addUpdate(String update, boolean b) {
		updatesTextArea.append("\n" + update);
	}

	// only show buzz to those who didnt answer so get the list that did
	public void showBuzz() {
		if (client != null) {
			addUpdate("Another team can buzz in");
			client.sendMessage(new ChatMessage("buzz", "buzz", gameData));
		}
	}

	public void blankTeamLabel() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).removeTeamLabel();
		}
	}

	public void setTeamLabel(String un) {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).setTeamLabel(un);
		}
	}
	
	public GameData getGameData() {
		return gameData;
	}

	public void sendMessage(String str) {
		client.sendMessage(new ChatMessage(str, str, gameData));
	}
	public void disableQButton(){
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).disableQButton();
		}
	}

	// for updating the actual panel
	//there is a member variable for this!!
	public void updateScorePanel(boolean b) {
		System.out.println("in update score panel main");
		for(int i = 0; i < gameData.getTeamDataList().size(); i++){
			System.out.println(gameData.getTeamDataList().get(i).getPoints());
			
			scoreLabels[i].setText("$"+ gameData.getTeamDataList().get(i).getPoints());
			
		}
	}
	public void updateTeamGUIScore(TeamGUIComponents tgui, int pointVal, boolean posNeg){
		client.sendMessage(new ChatMessage("score panel","score panel", tgui, pointVal, posNeg));
	}
	

	// this method changes the current panel to the provided panel
	public void changePanel(JPanel panel) {
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel, BorderLayout.CENTER);

		// send to the server if networked
		if (client != null) {
			System.out.println("sending change panel message to serverthread");
			if(currentPanel.equals(mainPanel)){
				List<Integer> x = gameData.getXValues();
				List<Integer> y = gameData.getYValues();
				//Set<QuestionGUIElement> questions = gameData.getQuestions();
				for(int i = 0; i < x.size(); i++){
					gameData.getQGUI(x.get(i), y.get(i)).disableGameButton();
				}
				
				client.sendMessage(new ChatMessage("change panel", "main panel", gameData, panel));
			}
			else{
				client.sendMessage(new ChatMessage("change panel", "change panel", gameData, panel));
			}
		}

		if (panel instanceof QuestionGUIElement) {
			((QuestionGUIElement) panel).setAnnouncement("It is your turn to answer");
		}

		// must repaint or the change won't show
		repaint();
		revalidate();
	}

	// used for buzzing in
	public void buzzIn() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).setMainGUI(this);
			((QuestionGUIElement) currentPanel).buzzButton();
		}
	}

	// show exit Option Pane
	public void exitOptionPane() {
		int input = JOptionPane.showOptionDialog(null, "A player exited the game", "Player Left",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (input == JOptionPane.OK_OPTION) {
			dispose();
			new StartWindowGUI(loggedInUser).setVisible(true);
		}
	}

	// used if it was the original
	public void isOriginal() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).isOriginalQ();
		}
	}

	// for if they have already buzzed before
	public void hasBuzzed() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).hasBuzzedQ();
		}
	}

	// just to overload the method to not loop forever
	public void changePanel(JPanel panel, boolean b) {
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel, BorderLayout.CENTER);

		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).disableQQ();
		}

		// must repaint or the change won't show
		repaint();
		revalidate();
	}

	public void disableQ(String namee) {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).disableQQ();
			String use = namee + " buzzed in to answer.";
			((QuestionGUIElement) currentPanel).setAnnouncement(use);
			
		}
	}
	public void disableQ() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).disableQQ();
		}
	}


	public void enableQ() {
		if (currentPanel instanceof QuestionGUIElement) {
			((QuestionGUIElement) currentPanel).enableQQ(this, gameData);
		}
	}

	public void showMainPanel() {
		//iterate through and only use the ones found from gamedata
		List<Integer> xVal = gameData.getXValues();
		List<Integer> yVal = gameData.getYValues();
		
		for(int i = 0; i < xVal.size(); i++){
			questionButtons[xVal.get(i)][yVal.get(i)].setEnabled(false);
		}
		
		changePanel(mainPanel);
	}
	public void showMainPanel(boolean b){
		//iterate throuhg and use 
		List<Integer> xVal = gameData.getXValues();
		List<Integer> yVal = gameData.getYValues();
		
		for(int i = 0; i < xVal.size(); i++){
			questionButtons[xVal.get(i)][yVal.get(i)].setEnabled(false);
		}
		
		changePanel(mainPanel, true);
	}
	
	public void disableGameButton(int x, int y){
		questionButtons[x][y].setEnabled(false);
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
	}

	private void createGUI() {

		createMenu();
		createMainPanel();

		add(mainPanel, BorderLayout.CENTER);
		add(createProgressPanel(), BorderLayout.EAST);
		setSize(1500, 825);
	}

	// creating the JMenuBar
	private void createMenu() {
		if (client != null) {
			if (client.isHost() == true) {
				menu.add(restartThisGameButton);
			}
		} else {
			menu.add(restartThisGameButton);
		}
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

	// creates the panel with the jeopardy label
	private JPanel createJeopardyPanel() {
		JPanel jeopardyPanel = new JPanel();
		JLabel jeopardyLabel = new JLabel("Jeopardy");
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, jeopardyPanel, jeopardyLabel);
		jeopardyLabel.setFont(AppearanceConstants.fontLarge);
		jeopardyPanel.add(jeopardyLabel);

		return jeopardyPanel;
	}

	// creates both the categories panel and the questions panel
	private JPanel createCategoriesAndQuestionsPanels() {
		JPanel categoriesPanel = new JPanel(new GridLayout(1, QUESTIONS_LENGTH_AND_WIDTH));
		AppearanceSettings.setBackground(Color.darkGray, categoriesPanel, questionsPanel);

		Map<String, Category> categories = gameData.getCategories();
		JLabel[] categoryLabels = new JLabel[QUESTIONS_LENGTH_AND_WIDTH];

		// iterate through the map of categories, and place them in the correct
		// index
		for (Map.Entry<String, Category> category : categories.entrySet()) {
			categoryLabels[category.getValue().getIndex()] = category.getValue().getCategoryLabel();
		}

		// place the question buttons in the proper indices in 'questionButtons'
		for (QuestionGUIElement question : gameData.getQuestions()) {

			// adding action listeners to the question
			question.addActionListeners(this, gameData);
			questionButtons[question.getX()][question.getY()] = question.getGameBoardButton();
		}

		// actually adding the categories and questions into their respective
		// panels
		for (int i = 0; i < QUESTIONS_LENGTH_AND_WIDTH; i++) {

			categoryLabels[i].setPreferredSize(new Dimension(100, 70));
			categoryLabels[i].setIcon(Category.getIcon());
			categoriesPanel.add(categoryLabels[i]);

			for (int j = 0; j < QUESTIONS_LENGTH_AND_WIDTH; j++) {
				// have to use opposite indices because of how GridLayout adds
				// components
				questionsPanel.add(questionButtons[j][i]);
			}
		}

		return categoriesPanel;
	}

	// creates the panel with the team points, and the Game Progress area
	private JPanel createProgressPanel() {

		
		pointsPanel = new JPanel(new GridLayout(gameData.getNumberOfTeams(), 2));
		scoreLabels = new JLabel[gameData.getNumberOfTeams()];
		// create panels
		southEastPanel = new JPanel(new BorderLayout());
		eastPanel = new JPanel();
		// other local variables
		JLabel updatesLabel = new JLabel("Game Progress");
		JScrollPane updatesScrollPane = new JScrollPane(updatesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// setting appearances
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, southEastPanel, updatesLabel, updatesScrollPane,
				updatesTextArea);
		AppearanceSettings.setSize(400, 400, pointsPanel, updatesScrollPane);
		AppearanceSettings.setTextComponents(updatesTextArea);

		updatesLabel.setFont(AppearanceConstants.fontLarge);
		pointsPanel.setBackground(Color.darkGray);
		updatesLabel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		updatesScrollPane.setBorder(null);

		updatesTextArea.setText("Welcome to Jeopardy!");
		updatesTextArea.setFont(AppearanceConstants.fontSmall);
		updatesTextArea.append("The team to go first will be " + gameData.getCurrentTeam().getTeamName());

		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		// adding components/containers
		southEastPanel.add(updatesLabel, BorderLayout.NORTH);
		southEastPanel.add(updatesScrollPane, BorderLayout.CENTER);

		// adding team labels, which are stored in the TeamGUIComponents class,
		// to the appropriate panel
		
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			TeamGUIComponents team = gameData.getTeamDataList().get(i);
			pointsPanel.add(team.getMainTeamNameLabel());
			scoreLabels[i] = team.getTotalPointsLabel();
			//scoreLabels[i].setBackground(Color.darkGray);
			pointsPanel.add(scoreLabels[i]);
		}

		eastPanel.add(pointsPanel);
		eastPanel.add(southEastPanel);

		return eastPanel;
	}

	private void exitNetworked() {
		if (client != null) {
			client.sendMessage(new ChatMessage("exit networked", "exit networked"));
		}
	}

	// adding even listeners
	private void addListeners() {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// add window listener
		addWindowListener(new ExitWindowListener(this));
		// add action listeners
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitNetworked();
				dispose();
				System.exit(0);
			}

		});

		restartThisGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isNetworked()) {
					client.sendMessage(new ChatMessage("restart", "restart"));
				} else {
					updatesTextArea.setText("Game has been restarted.");
					// reset all data
					gameData.restartGame();
					// repaint the board to show updated data
					showMainPanel();
					addUpdate("The team to go first will be " + gameData.getCurrentTeam().getTeamName());
				}
			}

		});

		chooseNewGameFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitNetworked();
				dispose();
				new StartWindowGUI(loggedInUser).setVisible(true);
			}

		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				exitNetworked();
				dispose();
				new LoginGUI().setVisible(true);
			}

		});
	}
	
}
