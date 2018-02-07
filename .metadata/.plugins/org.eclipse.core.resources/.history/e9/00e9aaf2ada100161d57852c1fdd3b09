package frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import game_logic.GameData;
import game_logic.User;
import listeners.ExitWindowListener;
import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;
import networkMessages.ChatClient;
import networkMessages.ChatServer;

public class StartWindowGUI extends JFrame {

	// main panel for whole system
	// north, center, south
	private JPanel mainPanel;

	// set up client and server so they are not local
	private ChatClient client;
	private ChatServer server;

	JPanel welcomePanel;

	// CardPanel System
	private JPanel cardPanel;
	private CardLayout cl;
	private JPanel notPanel;
	private JPanel hostPanel;
	private JPanel joinPanel;

	// set up radio buttons
	private JPanel radioPanel;
	private ButtonGroup radioGroup;
	private JRadioButton notRadio;
	private JRadioButton hostRadio;
	private JRadioButton joinRadio;

	// fileChooser not
	private JFileChooser fileChooser;
	private JButton fileChooserButton;
	private JLabel fileNameLabel;
	private Boolean haveValidFile;

	// only for not networked
	private List<JTextField> teamNameTextFields;
	private List<JLabel> teamNameLabels;
	private static final int MAX_NUMBER_OF_TEAMS = 4;
	private int numberOfTeams;

	private JLabel numberOfTeamsLabel;
	private JLabel chooseGameFileLabel;

	// for host
	private JTextField portField;
	private JTextField networkedTeamField;
	private JLabel networkedNameLabel;
	private JSlider sliderHost;
	private JLabel hnumberOfTeamsLabel;
	private JLabel hChooseGameFileLabel;
	private JLabel hGameFileLabel;
	private JButton hFileButton;
	private JLabel nwStatusLabel;
	private int playersNeeded;

	private boolean hostHasValidFile;

	// for join
	private JTextField jportField;
	private JTextField ipField;
	private JTextField jnetworkedTeamField;
	private JLabel jnetworkedNameLabel;
	private JLabel jnwStatusLabel;

	// private JPanel hostMain;
	private JPanel hostP1;
	private JPanel hostP2;
	private JPanel hostP3;

	// private JPanel joinMain;
	private JPanel joinP1;
	private JPanel joinP2;

	// bottom four buttons -- consistent
	private JButton startGameButton;
	private JButton clearDataButton;
	private JButton exitButton;
	private JButton logoutButton;

	// Slider just for the non networke 1-4
	private JSlider sliderNon;

	// for north panel -- set invisible for join**
	private JCheckBox quickPlay;

	private Boolean haveNames;

	private GameData gameData;
	// logged in user
	private User loggedInUser;

	public StartWindowGUI(User user) {

		super("Jeopardy Menu");
		loggedInUser = user;
		numberOfTeams = -1;
		haveNames = false;
		haveValidFile = false;

		initializeComponents();
		createGUI();
		addListeners();
	}

	// private methods
	private void initializeComponents() {
		mainPanel = new JPanel(new BorderLayout());

		// create card panels
		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout());
		cl = (CardLayout) (cardPanel.getLayout());

		// adding cards
		notPanel = new JPanel();
		notPanel.setLayout(new GridLayout(2, 1));
		hostPanel = new JPanel();
		hostPanel.setLayout(new GridLayout(4, 1, 0, 10));
		joinPanel = new JPanel();
		joinPanel.setLayout(new GridLayout(3, 1, 0, 10));

		// create radio buttons
		radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1, 3));
		radioGroup = new ButtonGroup();
		notRadio = new JRadioButton("Not Networked");
		hostRadio = new JRadioButton("Host Game");
		joinRadio = new JRadioButton("Join Game");
		radioGroup.add(notRadio);
		radioGroup.add(hostRadio);
		radioGroup.add(joinRadio);
		notRadio.setSelected(true);
		radioPanel.add(notRadio);
		radioPanel.add(hostRadio);
		radioPanel.add(joinRadio);

		fileChooser = new JFileChooser();
		teamNameTextFields = new ArrayList<>(4);
		teamNameLabels = new ArrayList<>(4);
		fileNameLabel = new JLabel("");
		logoutButton = new JButton("Logout");
		gameData = new GameData();
		// fileRating = new JLabel("");
		quickPlay = new JCheckBox("Quick Play?");

		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			teamNameTextFields.add(new JTextField());
			teamNameLabels.add(new JLabel("Please name Team " + (i + 1)));
		}

		startGameButton = new JButton("Start Jeopardy");
		clearDataButton = new JButton("Clear Choices");
		exitButton = new JButton("Exit");
		fileChooserButton = new JButton("Choose File");
		sliderNon = new JSlider();

	}

	private void createGUI() {

		// setting appearance of member variable gui components
		// setting background colors
		AppearanceSettings.setBackground(Color.darkGray, exitButton, logoutButton, clearDataButton, startGameButton,
				sliderNon, teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, teamNameTextFields.get(0),
				teamNameTextFields.get(1), teamNameTextFields.get(2), teamNameTextFields.get(3), radioPanel);

		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, fileNameLabel, mainPanel, cardPanel);

		// setting fonts
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, teamNameTextFields.get(0), teamNameTextFields.get(1),
				teamNameTextFields.get(2), teamNameTextFields.get(3), teamNameLabels.get(0), teamNameLabels.get(1),
				teamNameLabels.get(2), teamNameLabels.get(3), fileChooserButton, fileNameLabel, exitButton,
				clearDataButton, logoutButton, startGameButton);

		// other
		AppearanceSettings.setForeground(Color.lightGray, exitButton, logoutButton, clearDataButton, startGameButton,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton, fileNameLabel, sliderNon);

		AppearanceSettings.setOpaque(exitButton, clearDataButton, logoutButton, startGameButton, sliderNon,
				teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2), teamNameLabels.get(3),
				fileChooserButton);

		AppearanceSettings.setSize(180, 70, exitButton, clearDataButton, startGameButton, logoutButton);
		AppearanceSettings.setSize(150, 80, teamNameTextFields.get(0), teamNameTextFields.get(1),
				teamNameTextFields.get(2), teamNameTextFields.get(3));

		AppearanceSettings.setSize(250, 100, teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2),
				teamNameLabels.get(3));

		AppearanceSettings.unSetBorderOnButtons(exitButton, logoutButton, clearDataButton, startGameButton,
				fileChooserButton);

		AppearanceSettings.setTextAlignment(teamNameLabels.get(0), teamNameLabels.get(1), teamNameLabels.get(2),
				teamNameLabels.get(3), fileNameLabel);

		setAllInvisible(teamNameTextFields, teamNameLabels);
		// check box settings
		quickPlay.setFont(AppearanceConstants.fontSmallest);
		quickPlay.setHorizontalTextPosition(SwingConstants.LEFT);
		quickPlay.setPreferredSize(new Dimension(200, 30));

		// file chooser settings
		fileChooser.setPreferredSize(new Dimension(400, 500));
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));

		// sliderNon settings
		AppearanceSettings.setSliders(1, MAX_NUMBER_OF_TEAMS, 1, 1, AppearanceConstants.fontSmallest, sliderNon);
		sliderNon.setSnapToTicks(true);
		sliderNon.setPreferredSize(new Dimension(500, 50));
		startGameButton.setEnabled(false);

		createMainPanel();

		setBackground(AppearanceConstants.darkBlue);
		add(mainPanel, BorderLayout.CENTER);
		setSize(800, 825);
	}

	public void createHostCardGUI() {
		// create four panels
		hostP1 = new JPanel();
		hostP2 = new JPanel();
		hostP3 = new JPanel();
		hnumberOfTeamsLabel = new JLabel("Please choose the number of teams that will be playing on the slider below.");
		hChooseGameFileLabel = new JLabel("Please choose a game file.");
		hGameFileLabel = new JLabel("");
		hFileButton = new JButton("Choose File");
		nwStatusLabel = new JLabel("");
		
		nwStatusLabel.setForeground(Color.WHITE);
		AppearanceSettings.setTextAlignment(hnumberOfTeamsLabel, nwStatusLabel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, hostP1, hostP2, hostP3, hostPanel);
		sliderHost = new JSlider();

		// p1
		portField = new JTextField("port");
		AppearanceSettings.setForeground(Color.lightGray, portField, hFileButton, hGameFileLabel, hChooseGameFileLabel,
				hnumberOfTeamsLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, portField, hFileButton, hGameFileLabel,
				hChooseGameFileLabel);
		AppearanceSettings.setSize(150, 80, portField);

		hostP1.add(portField, hostP1.CENTER_ALIGNMENT);

		// p2
		hostP2.setLayout(new GridLayout(3, 1));
		hostP2.add(hnumberOfTeamsLabel);

		AppearanceSettings.setBackground(Color.darkGray, sliderHost, hFileButton);
		AppearanceSettings.setOpaque(sliderHost, hFileButton);
		AppearanceSettings.unSetBorderOnButtons(hFileButton);
		AppearanceSettings.setSliders(2, MAX_NUMBER_OF_TEAMS, 1, 1, AppearanceConstants.fontSmallest, sliderHost);

		sliderHost.setSnapToTicks(true);
		sliderHost.setPreferredSize(new Dimension(375, 50));
		hostP2.add(sliderHost);

		JPanel hostP2P3 = new JPanel(new GridLayout(1, 3));
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, hostP2P3);

		hostP2P3.setBorder(new MatteBorder(3, 0, 0, 0, AppearanceConstants.darkBlue));

		hostP2P3.add(hChooseGameFileLabel);
		hostP2P3.add(hFileButton);
		hostP2P3.add(hGameFileLabel);

		hostP2.add(hostP2P3);

		// p3
		hostP3.setLayout(new GridLayout(2, 1, 0, 2));

		networkedTeamField = new JTextField();
		networkedTeamField.setText(loggedInUser.getUsername());
		networkedNameLabel = new JLabel("Please choose a team name");
		AppearanceSettings.setBackground(Color.darkGray, networkedNameLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, networkedTeamField, networkedNameLabel);
		AppearanceSettings.setForeground(Color.lightGray, networkedNameLabel);
		AppearanceSettings.setOpaque(networkedNameLabel);
		AppearanceSettings.setSize(150, 80, networkedTeamField);
		AppearanceSettings.setSize(250, 100, networkedNameLabel);
		AppearanceSettings.setTextAlignment(networkedNameLabel);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, networkedTeamField);

		hostP3.add(networkedNameLabel);
		hostP3.add(networkedTeamField);

		// add border then add all together
		hostPanel.setBorder(new MatteBorder(0, 50, 0, 50, AppearanceConstants.darkBlue));
		hostPanel.add(hostP1);
		hostPanel.add(hostP2);
		hostPanel.add(hostP3);
		hostPanel.add(nwStatusLabel);
		// hostPanel.add(hostP4);

	}

	public void createJoinCardGUI() {
		// dont need to recreate port or the name entrance
		// also, not here, but in the action listener, just set the file window
		// invisible
		// and just make it visible again upon the switch --> or is this harder?
		// Later decide
		joinP1 = new JPanel();
		joinP2 = new JPanel();
		jportField = new JTextField("port");
		ipField = new JTextField("ip");
		jnetworkedTeamField = new JTextField(loggedInUser.getUsername());
		jnetworkedNameLabel = new JLabel("Please choose a team name");
		jnwStatusLabel = new JLabel("");

		jnwStatusLabel.setForeground(Color.WHITE);
		AppearanceSettings.setTextAlignment(jnwStatusLabel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, joinP1, joinP2, joinPanel);

		AppearanceSettings.setForeground(Color.lightGray, ipField, jportField, jnetworkedNameLabel);

		AppearanceSettings.setFont(AppearanceConstants.fontSmall, ipField, jportField, jnetworkedTeamField,
				jnetworkedNameLabel);
		AppearanceSettings.setSize(150, 80, ipField, jportField);
		AppearanceSettings.setBackground(Color.darkGray, jnetworkedNameLabel);
		AppearanceSettings.setOpaque(jnetworkedNameLabel);
		AppearanceSettings.setTextAlignment(jnetworkedNameLabel);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, jnetworkedTeamField);

		joinP1.setLayout(new GridLayout(1, 2));
		joinP2.setLayout(new GridLayout(2, 1));

		joinP1.add(jportField, joinP1.CENTER_ALIGNMENT);
		joinP1.add(ipField, joinP1.CENTER_ALIGNMENT);

		joinP2.setLayout(new GridLayout(2, 1, 0, 2));
		joinP2.add(jnetworkedNameLabel);
		joinP2.add(jnetworkedTeamField);

		// add border then add all together
		joinPanel.setBorder(new MatteBorder(20, 200, 125, 200, AppearanceConstants.darkBlue));
		joinPanel.add(joinP1);
		joinPanel.add(joinP2);
		joinPanel.add(jnwStatusLabel);
	}

	// sets the label and textField visible again
	private void setVisible(JLabel label, JTextField textField) {
		// the first text field is always shown so we can use their border
		Border border = teamNameTextFields.get(0).getBorder();

		textField.setBackground(AppearanceConstants.lightBlue);
		textField.setForeground(Color.black);
		textField.setBorder(border);

		label.setBackground(Color.darkGray);
		label.setForeground(Color.lightGray);
	}

	// I wanted to user BoxLayout for resizability but if you simply set a
	// components invisble with
	// setVisible(false), it changes the size of the components that are
	// visible. This is my way aroung that
	private void setInvisible(JLabel label, JTextField textField) {
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, textField, label);
		AppearanceSettings.setForeground(AppearanceConstants.darkBlue, textField, label);
		textField.setBorder(AppearanceConstants.blueLineBorder);
	}

	// used in the constructor to set everything invisible (except the first
	// label and text field)
	private void setAllInvisible(List<JTextField> teamNameTextFields, List<JLabel> teamNameLabels) {

		for (int i = 1; i < 4; i++)
			setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
	}

	public void setjnwStatusLabel(String txt) {
		jnwStatusLabel.setText(txt);
	}

	public void setnwStatusLabel(String txt) {
		nwStatusLabel.setText(txt);
	}

	private void createMainPanel() {
		// initialize local panels
		JPanel teamNamesPanel = new JPanel();
		JPanel teamLabelsPanel1 = new JPanel();
		JPanel teamLabelsPanel2 = new JPanel();
		JPanel teamTextFieldsPanel1 = new JPanel();
		JPanel teamTextFieldsPanel2 = new JPanel();
		JPanel teamsAndFilePanel = new JPanel();
		JPanel numberOfTeamsPanel = new JPanel();

		// NORTH & SOUTH
		JPanel northPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		// will go into NORTH
		welcomePanel = new JPanel(new BorderLayout());
		JPanel titlePanel = new JPanel(new GridLayout(3, 1));
		// include the radioPanel down below

		JPanel fileChooserPanel = new JPanel();

		// initialize labels
		JLabel welcomeLabel = new JLabel("Welcome to Jeopardy!");
		JLabel explainContentLabel = new JLabel("Choose to join or host a game or play not networked.");
		numberOfTeamsLabel = new JLabel("Please choose the number of teams that will be playing on the slider below.");
		chooseGameFileLabel = new JLabel("Please choose a game file.");

		// set appearances on local variables
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, welcomeLabel, explainContentLabel, welcomePanel,
				titlePanel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, explainContentLabel, chooseGameFileLabel,
				numberOfTeamsLabel);
		AppearanceSettings.setTextAlignment(welcomeLabel, explainContentLabel, chooseGameFileLabel, numberOfTeamsLabel);

		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, chooseGameFileLabel, numberOfTeamsLabel,
				numberOfTeamsPanel, fileChooserPanel, teamsAndFilePanel, buttonPanel, teamNamesPanel, teamLabelsPanel1,
				teamLabelsPanel2, teamTextFieldsPanel1, teamTextFieldsPanel2);
		AppearanceSettings.setForeground(Color.lightGray, chooseGameFileLabel, numberOfTeamsLabel);

		AppearanceSettings.setSize(800, 60, welcomePanel, explainContentLabel);
		AppearanceSettings.setSize(800, 100, buttonPanel, numberOfTeamsPanel);
		AppearanceSettings.setSize(800, 80, fileChooserPanel);

		welcomeLabel.setFont(AppearanceConstants.fontLarge);
		numberOfTeamsLabel.setSize(700, 60);

		// setting box layouts of panels
		AppearanceSettings.setBoxLayout(BoxLayout.LINE_AXIS, buttonPanel, fileChooserPanel, teamLabelsPanel1,
				teamLabelsPanel2, teamTextFieldsPanel1, teamTextFieldsPanel2);
		AppearanceSettings.setBoxLayout(BoxLayout.PAGE_AXIS, northPanel, teamNamesPanel, teamsAndFilePanel,
				numberOfTeamsPanel);

		// method iterates through components and add glue after each one is
		// added, bool indicates whether glue should be added at the initially
		// as well
		AppearanceSettings.addGlue(teamLabelsPanel1, BoxLayout.LINE_AXIS, true, teamNameLabels.get(0),
				teamNameLabels.get(1));
		AppearanceSettings.addGlue(teamLabelsPanel2, BoxLayout.LINE_AXIS, true, teamNameLabels.get(2),
				teamNameLabels.get(3));
		AppearanceSettings.addGlue(teamTextFieldsPanel1, BoxLayout.LINE_AXIS, true, teamNameTextFields.get(0),
				teamNameTextFields.get(1));
		AppearanceSettings.addGlue(teamTextFieldsPanel2, BoxLayout.LINE_AXIS, true, teamNameTextFields.get(2),
				teamNameTextFields.get(3));
		AppearanceSettings.addGlue(teamNamesPanel, BoxLayout.PAGE_AXIS, true, teamLabelsPanel1, teamTextFieldsPanel1,
				teamLabelsPanel2, teamTextFieldsPanel2);

		// don't want to pass in fileNameLabel since I don't want glue after it
		AppearanceSettings.addGlue(fileChooserPanel, BoxLayout.LINE_AXIS, true, chooseGameFileLabel, fileChooserButton);
		fileChooserPanel.add(fileNameLabel);

		// don't want to pass in fileChooserPanel since I don't want glue after
		// it
		AppearanceSettings.addGlue(teamsAndFilePanel, BoxLayout.PAGE_AXIS, true, numberOfTeamsPanel);
		teamsAndFilePanel.add(fileChooserPanel);

		AppearanceSettings.addGlue(buttonPanel, BoxLayout.LINE_AXIS, true, startGameButton, clearDataButton,
				logoutButton, exitButton);

		// add other components to other containers
		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
		welcomePanel.add(quickPlay, BorderLayout.EAST);

		titlePanel.add(welcomePanel);
		titlePanel.add(explainContentLabel);
		titlePanel.add(radioPanel);

		northPanel.add(titlePanel);

		numberOfTeamsPanel.add(numberOfTeamsLabel);
		numberOfTeamsPanel.add(sliderNon);

		notPanel.add(teamsAndFilePanel);
		notPanel.add(teamNamesPanel);

		// creating the two panels not shown for inclusion in the Appearance
		// Settings
		createHostCardGUI();
		createJoinCardGUI();

		cardPanel.add(notPanel, "not");
		cardPanel.add(hostPanel, "host");
		cardPanel.add(joinPanel, "join");

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(cardPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	// determines whether the chosen file is valid
	private void setHaveValidFile(File file) {

		// if they had already chosen a valid file, but want to replace it, need
		// to clear stored data
		if (haveValidFile)
			gameData.clearData();

		try {
			// try parsing this file; the parseFile method could throw an
			// exception here, in which case we know it was invalid
			gameData.parseFile(file.getAbsolutePath());
			haveValidFile = true;

			if (gameData.getGameFile().getNumberOfRatings() == -1)
				fileNameLabel.setText(file.getName() + "    no rating");

			else
				fileNameLabel.setText(
						file.getName() + "    average rating: " + gameData.getGameFile().getAverageRating() + "/5");
			// check if the user can start the game
			startGameButton.setEnabled(haveValidFile && haveNames());
		}

		catch (Exception e) {
			haveValidFile = false;
			startGameButton.setEnabled(false);
			fileNameLabel.setText("");
			// pop up with error message
			JOptionPane.showMessageDialog(this, e.getMessage(), "File Reading Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// determines whether the chosen file is valid
	private void setHostHaveValidFile(File file) {

		// if they had already chosen a valid file, but want to replace it, need
		// to clear stored data
		if (hostHasValidFile)
			gameData.clearData();

		try {
			// try parsing this file; the parseFile method could throw an
			// exception here, in which case we know it was invalid
			gameData.parseFile(file.getAbsolutePath());
			hostHasValidFile = true;

			if (gameData.getGameFile().getNumberOfRatings() == -1)
				hGameFileLabel.setText(file.getName() + "    no rating");

			else
				hGameFileLabel.setText(
						file.getName() + "    average rating: " + gameData.getGameFile().getAverageRating() + "/5");
			// check if the user can start the game
			startGameButton.setEnabled(hostHasValidFile);
		}

		catch (Exception e) {
			hostHasValidFile = false;
			startGameButton.setEnabled(false);
			hGameFileLabel.setText("");
			// pop up with error message
			JOptionPane.showMessageDialog(this, e.getMessage(), "File Reading Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void addListeners() {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));

		// adding a document listener to each text field. This will allow us to
		// determine if the user has entered team names
		for (int i = 0; i < MAX_NUMBER_OF_TEAMS; i++) {
			teamNameTextFields.get(i).getDocument().addDocumentListener(new MyDocumentListener());
		}

		// add document listeners to host
		portField.getDocument().addDocumentListener(new MyDocumentListener());
		networkedTeamField.getDocument().addDocumentListener(new MyDocumentListener());

		// add document listeners to join
		jportField.getDocument().addDocumentListener(new MyDocumentListener());
		ipField.getDocument().addDocumentListener(new MyDocumentListener());
		jnetworkedTeamField.getDocument().addDocumentListener(new MyDocumentListener());

		notRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				hostRadio.setSelected(false);
				joinRadio.setSelected(false);
				
				startGameButton.setEnabled(false);

				cl.show(cardPanel, "not");
				clearDataButton.doClick();
				// make checkbox visible
				quickPlay.setVisible(true);
			}

		});
		hostRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				notRadio.setSelected(false);
				joinRadio.setSelected(false);
				
				startGameButton.setEnabled(false);

				// make checkbox visible
				quickPlay.setVisible(true);

				cl.show(cardPanel, "host");
				startGameButton.setEnabled(false);
			}

		});
		joinRadio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				notRadio.setSelected(false);
				hostRadio.setSelected(false);
				
				startGameButton.setEnabled(false);

				cl.show(cardPanel, "join");
				startGameButton.setText("Join Game");

				// make checkbox invisible
				quickPlay.setVisible(false);
			}

		});

		fileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(StartWindowGUI.this);
				File file = fileChooser.getSelectedFile();

				if (file != null)
					setHaveValidFile(file);
			}

		});

		hFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.showOpenDialog(StartWindowGUI.this);
				File file = fileChooser.getSelectedFile();

				if (file != null)
					setHostHaveValidFile(file);
			}

		});

		sliderNon.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// sets appropriate text fields and labels invisible
				for (int i = sliderNon.getValue(); i < MAX_NUMBER_OF_TEAMS; i++) {
					setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
				}
				// sets appropriate text fields and labels visible
				for (int i = 0; i < sliderNon.getValue(); i++) {
					setVisible(teamNameLabels.get(i), teamNameTextFields.get(i));
				}
				// check if the user can start the game
				startGameButton.setEnabled(haveNames() && haveValidFile);
			}

		});

		startGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (notRadio.isSelected()) {
					numberOfTeams = sliderNon.getValue();
					List<String> teamNames = new ArrayList<>(numberOfTeams);
					// getting the text in each of the visible text fields and
					// storing them in a list
					for (int i = 0; i < numberOfTeams; i++) {
						teamNames.add(teamNameTextFields.get(i).getText());
					}
					// initializing TeamGUIComponents objects
					gameData.setTeams(teamNames, numberOfTeams);
					gameData.setNumberOfQuestions(quickPlay.isSelected() ? 5 : 25);

					new MainGUI(gameData, loggedInUser).setVisible(true);
					dispose();
				} else {
					startGameButton.setEnabled(false);
					clearDataButton.setEnabled(false);
					if (hostRadio.isSelected()) {
						playersNeeded = sliderHost.getValue() - 1;//because one will get deducted with own client
						int port = Integer.parseInt(portField.getText());
						server = new ChatServer(port, playersNeeded, gameData);
						
						client = new ChatClient(StartWindowGUI.this, "localhost", Integer.parseInt(jportField.getText()));
						//nwStatusLabel
						//.setText("Waiting for " + Integer.toString(playersNeeded) + " other players to join..");
					} else if (joinRadio.isSelected()) {
						// send request to that ip
						// if successful, send message to wait or direct to
						// otherwise send error message
						// client = new ChatClient();
						createClient();
						//jnwStatusLabel.setText("Waiting for " + Integer.toString(playersNeeded) + " other players to join..");
					}

				}
			}

		});

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}

		});

		clearDataButton.addActionListener(new ActionListener() {

			// reseting all data
			@Override
			public void actionPerformed(ActionEvent e) {
				haveNames = false;
				haveValidFile = false;
				gameData.clearData();
				quickPlay.setSelected(false);
				// start index at 1, we still was to show the 0th elements (team
				// 1)
				for (int i = 1; i < MAX_NUMBER_OF_TEAMS; i++) {
					setInvisible(teamNameLabels.get(i), teamNameTextFields.get(i));
					teamNameTextFields.get(i).setText("");
				}
				notRadio.doClick();

				startGameButton.setEnabled(false);
				teamNameTextFields.get(0).setText("");
				sliderNon.setValue(1);
				fileNameLabel.setText("");
			}

		});

		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginGUI().setVisible(true);
				dispose();
			}

		});

	}

	// updates and returns the haveNames member variable of whether all teams
	// have been named
	private boolean haveNames() {

		haveNames = true;
		// check to see if all relevant text fields have text in them
		for (int i = 0; i < sliderNon.getValue(); i++) {

			if (teamNameTextFields.get(i).getText().trim().equals(""))
				haveNames = false;
		}

		return haveNames;
	}

	// document listener; in each method, simply checking whether the user can
	// start the game
	private class MyDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			if (notRadio.isSelected()) {
				startGameButton.setEnabled(haveNames() && haveValidFile);
			} else if (hostRadio.isSelected()) {
				boolean b = true;
				if(portField.getText().equals("")){
					b = false;
				}
				else if(portField.getText().equals("port")){
					b = false;
				}
				boolean b2 = true;
				if(networkedTeamField.getText().equals("")){
					b2 = false;
				}
				startGameButton.setEnabled(b && b2 && hostHasValidFile);
			} else {// join game
				boolean b1 = true;
				if(jportField.getText().equals("")){
					b1 = false;
				}
				else if(jportField.getText().equals("port")){
					b1 = false;
				}
				boolean b2 = true;
				if(ipField.getText().equals("")){
					b2 = false;
				}
				else if(ipField.getText().equals("ip")){
					b2 = false;
				}
				boolean b3 = true;
				if(jnetworkedTeamField.getText().equals("")){
					b3 = false;
				}
				startGameButton.setEnabled((b1 && b2 && b3));
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if (notRadio.isEnabled()) {
				startGameButton.setEnabled(haveNames() && haveValidFile);
			} else if (hostRadio.isSelected()) {
				boolean b = true;
				if(portField.getText().equals("")){
					b = false;
				}
				else if(portField.getText().equals("port")){
					b = false;
				}
				boolean b2 = true;
				if(networkedTeamField.getText().equals("")){
					b2 = false;
				}
				startGameButton.setEnabled(b && b2 && hostHasValidFile);
			} else {// join game
				boolean b1 = true;
				if(jportField.getText().equals("")){
					b1 = false;
				}
				else if(jportField.getText().equals("port")){
					b1 = false;
				}
				boolean b2 = true;
				if(ipField.getText().equals("")){
					b2 = false;
				}
				else if(ipField.getText().equals("ip")){
					b2 = false;
				}
				boolean b3 = true;
				if(jnetworkedTeamField.getText().equals("")){
					b3 = false;
				}
				startGameButton.setEnabled((b1 && b2 && b3));
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if (notRadio.isEnabled()) {
				startGameButton.setEnabled(haveNames() && haveValidFile);
			} else if (hostRadio.isSelected()) {
				boolean b = true;
				if(portField.getText().equals("")){
					b = false;
				}
				else if(portField.getText().equals("port")){
					b = false;
				}
				boolean b2 = true;
				if(networkedTeamField.getText().equals("")){
					b2 = false;
				}
				startGameButton.setEnabled(b && b2 && hostHasValidFile);
			} else {// join game
				boolean b1 = true;
				if(jportField.getText().equals("")){
					b1 = false;
				}
				else if(jportField.getText().equals("port")){
					b1 = false;
				}
				boolean b2 = true;
				if(ipField.getText().equals("")){
					b2 = false;
				}
				else if(ipField.getText().equals("ip")){
					b2 = false;
				}
				boolean b3 = true;
				if(jnetworkedTeamField.getText().equals("")){
					b3 = false;
				}
				startGameButton.setEnabled(b1 && b2 && b3);
			}
		}
	}

	// functions below used for server/client communication

	// construct server and client
	private void createClient() {
		System.out.println("Creating external client");
		client = new ChatClient(this, ipField.getText(), Integer.parseInt(jportField.getText()));
	}
	/*private void createClient(String hostname, int port){
		System.out.println("Creating server's client");
		client = new ChatClient(this, hostname, port);
	}*/

	public void changeServerWaitingLabel() {
		playersNeeded--;
		nwStatusLabel.setText("Waiting for " + playersNeeded + " other players to join..");
	}

	public void changeClientWaitingLabel(int i) {
		nwStatusLabel.setText("Waiting for " + i + " other players to join..");
	}

	public boolean isHostRadio() {
		return hostRadio.isSelected();
	}

	

	public boolean isJoinRadio() {
		return joinRadio.isSelected();
	}

}
