package Game_Layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FileChooserUI;

import game_logic.GamePlay;

public class WelcomeLayout extends JFrame {
	
	
	//layout the variables
	private Color lightBlue = new Color(42,182,203);
	
	private JButton exitButton, sureButton;
	private JButton clearChoices;
	private JButton startJeopardy;
	
	private InnerBox t1;
	private InnerBox t2;
	private InnerBox t3;
	private InnerBox t4;
	
	private JButton fileButton;
	private JLabel fileName;
	
	private GamePlay gp;
	
	private JSlider slider;
	
	private JFileChooser jfc;
	private JDialog sureD;
	
	//start jeopardy flags
	private boolean fileChosen;
	private boolean teamsChosen;
	
	private JCheckBox checkBox;
	
	//constructor
	public WelcomeLayout(GamePlay gp){//, GamePlay gp){
		super();
		this.gp = gp;
		setSize(1000,600);
		
		fileChosen = false;
		teamsChosen = false;
		
		//CLOSE OPERATION
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		sureButton = new JButton("Exit");
		
		//--------------------------------------
		//NORTH Border Layout
		//--------------------------------------
		JPanel northP = new JPanel();
		northP.setLayout(new BoxLayout(northP, BoxLayout.Y_AXIS));
		northP.setBackground(lightBlue);
		
		//creating the components for north
		JLabel welcomeMessage1 = new JLabel("Welcome to Jeopardy!");
		//need to center it!!!!!!!
		welcomeMessage1.setFont(new Font("Times New Roman",Font.BOLD, 30));
		welcomeMessage1.setBorder(new EmptyBorder(0,0,10,0));
		
		JLabel welcomeMessage2 = new JLabel("Choose the game file, number or teams, "
				+ "and team names before starting the game.");
		welcomeMessage2.setFont(new Font("Times New Roman",Font.BOLD, 10));
		
		//adding the components to north
		northP.add(welcomeMessage1);
		northP.add(welcomeMessage2);
		
		//--------------------------------------
		//CENTER Border Layout
		//--------------------------------------
		
		//main panel
		JPanel centerP = new JPanel();
		centerP.setLayout(new BoxLayout(centerP, BoxLayout.Y_AXIS));
		centerP.setBackground(Color.BLUE);
		
		//text file part
		JPanel fileP = new JPanel();
		fileP.setLayout(new BoxLayout(fileP, BoxLayout.X_AXIS));
		fileP.setBackground(Color.BLUE);
	
		JLabel gFile = new JLabel("Please choose a game file.");
		gFile.setBorder(new EmptyBorder(0,10,10,15));
		gFile.setFont(new Font("Times New Roman",Font.BOLD, 10));
		gFile.setForeground(Color.WHITE);
		
		fileButton = new JButton("Choose File");
		fileButton.setBackground(Color.DARK_GRAY);
		fileButton.setOpaque(true);
		fileButton.setBorderPainted(false);
		fileButton.setForeground(Color.WHITE);
		
		fileName = new JLabel("");
		fileName.setBorder(new EmptyBorder(0,10,10,15));
		fileName.setFont(new Font("Times New Roman",Font.BOLD, 10));
		fileName.setForeground(Color.WHITE);
		
		
		fileP.add(gFile);
		fileP.add(fileButton);
		fileP.add(fileName);
		
		//Please Choose # teams text
		JLabel numberTeamsText = new JLabel("Please choose the number of teams that will be "
				+ "playing on the slider below.");
		numberTeamsText.setBorder(new EmptyBorder(10,10,10,15));
		numberTeamsText.setForeground(Color.WHITE);
		
		//Slider
		JPanel sliderP = new JPanel(new BorderLayout());
		sliderP.setBackground(Color.DARK_GRAY);
		
		slider = new JSlider(JSlider.HORIZONTAL);
		slider.setMaximum(4);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
		slider.setBackground(Color.DARK_GRAY);
		slider.setForeground(Color.WHITE);
		slider.setMinimum(1);
		slider.setValue(1);
		
		sliderP.add(slider);
		
		//enter team names
		JPanel grid = new JPanel(new GridLayout(2,2,1,1));
		grid.setBackground(Color.BLUE);
		grid.setBorder(new EmptyBorder(10,20,10,20));
		
		t1 = new InnerBox(1);
		t2 = new InnerBox(2);
		t3 = new InnerBox(3);
		t4 = new InnerBox(4);
		
		grid.add(t1);
		grid.add(t2);
		grid.add(t3);
		grid.add(t4);
		
		t1.unhide();
		t2.hide();
		t3.hide();
		t4.hide();
		
		//adding the parts to completed center
		centerP.add(fileP);
		centerP.add(numberTeamsText);
		
		JPanel sliderGrid = new JPanel(new BorderLayout());
		sliderGrid.add(sliderP, BorderLayout.NORTH);
		sliderGrid.add(grid, BorderLayout.CENTER);
		
		centerP.add(sliderGrid);
		
		//--------------------------------------
		//SOUTH Border Layout
		//--------------------------------------
		JPanel southP1 = new JPanel();
		southP1.setLayout(new BoxLayout(southP1, BoxLayout.Y_AXIS));
		southP1.setBackground(Color.BLUE);
		
		JPanel southP = new JPanel();
		southP.setLayout(new BoxLayout(southP, BoxLayout.X_AXIS));
		southP.setBackground(Color.BLUE);
		
		startJeopardy = new JButton("Start Jeopardy");
		startJeopardy.setBackground(Color.DARK_GRAY);
		startJeopardy.setOpaque(true);
		startJeopardy.setBorderPainted(false);
		startJeopardy.setForeground(Color.WHITE);
		//startJeopardy.setEnabled(false);
		//startJeopardy.setBorder(new EmptyBorder(0,10,10,10));
		
		clearChoices = new JButton("Clear Data");
		clearChoices.setBackground(Color.DARK_GRAY);
		clearChoices.setOpaque(true);
		clearChoices.setBorderPainted(false);
		clearChoices.setForeground(Color.WHITE);
		//clearChoices.setBorder(new EmptyBorder(0,10,10,10));
		
		exitButton = new JButton("Exit");
		exitButton.setBackground(Color.DARK_GRAY);
		exitButton.setOpaque(true);
		exitButton.setBorderPainted(false);
		exitButton.setForeground(Color.WHITE);
		//exitButton.setBorder(new EmptyBorder(0,10,10,10));
		
		southP.add(startJeopardy);
		southP.add(clearChoices);
		southP.add(exitButton);
		
		JPanel southP2 = new JPanel();
		southP2.setLayout(new BoxLayout(southP2, BoxLayout.X_AXIS));
		southP2.setBackground(Color.BLUE);
		
		JLabel quickMode = new JLabel("Quick Mode :");
		quickMode.setForeground(Color.WHITE);
		quickMode.setBackground(Color.BLUE);
		checkBox = new JCheckBox();
		
		southP2.add(quickMode);
		southP2.add(checkBox);
		
		southP1.add(southP);
		southP1.add(southP2);
		
		//-------------------------------------------
		//adding the formed panels to the border Layout
		//-------------------------------------------
		add(northP, BorderLayout.NORTH);
		add(southP1, BorderLayout.SOUTH);
		add(centerP, BorderLayout.CENTER);
		addActions();
		setSize(1000,600);
		setVisible(true);
		
	}
	
	public void addActions(){
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				System.exit(0);
			}
		});
		
		sureButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				System.exit(0);
			}
		});
		
		startJeopardy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				String chooseTeamString = "not pass";
				//String[] teams = new String[4];
				//MainScreen main = new MainScreen(teams, gp);
				//startJeopardy.setBackground(Color.RED);
				//setVisibile(false);
				if(fileChosen == false){
					JDialog errorBox = new JDialog();
					errorBox.setTitle("Error");
					JLabel errorMsg = new JLabel("You must select a file to proceed");
					errorBox.add(errorMsg);
					errorBox.setSize(new Dimension(250,100));
					errorBox.setModal(true);
					//errorBox.setForeground(lightBlue);
					errorBox.setVisible(true);
					return;
				}
				//stream in the teams
				int sliderVal = slider.getValue();
				//System.out.println(sliderVal);
				
				//processes the teams into the game logic
				chooseTeamString = processTeams(sliderVal);
				
				if(chooseTeamString.equals("pass")){
					teamsChosen = true;
				}
				//------------------
				if(teamsChosen == false){
					JDialog errorBox = new JDialog();
					errorBox.setTitle("Error");
					JLabel errorMsg = new JLabel(chooseTeamString);
					errorBox.add(errorMsg);
					errorBox.setSize(new Dimension(250,100));
					errorBox.setModal(true);
					//errorBox.setForeground(lightBlue);
					errorBox.setVisible(true);
					return;
				}
				else{
					//System.out.println("going to main");
					setVisible(false);
					if(checkBox.isSelected()){
						MainScreen main = new MainScreen(gp, true);
					}else{
						MainScreen main = new MainScreen(gp, false);
					}
					//System.out.println("going to main");
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		        sureD = new JDialog();
		        JPanel sureP = new JPanel();
		        sureP.setLayout(new BoxLayout(sureP, BoxLayout.Y_AXIS));
		        sureP.setBackground(lightBlue);
		        JLabel sureL = new JLabel("Are you sure you want to exit?");
		        sureP.add(sureL);
		        sureP.add(sureButton);
		        sureD.add(sureP);
		        sureD.setSize(new Dimension(200,100));
		        sureD.setVisible(true);
		    }
		});
		
		
		
		clearChoices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				t1.clearData();
				t2.clearData();
				t3.clearData();
				t4.clearData();
			}
		});
		
		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				jfc = new JFileChooser();
				int val = jfc.showOpenDialog(fileButton);
				if(val == JFileChooser.APPROVE_OPTION){
					String file = jfc.getSelectedFile().getAbsolutePath();
					//System.out.println(file);
					if(file.endsWith(".txt")){
						//check if good through parser in gamedata of gameplay
						
						String parse = gp.fileReader(jfc.getSelectedFile());
						//System.out.println(parse);
						if(parse.equals("pass")){
							fileName.setText(file);
							fileChosen = true;
						}
						else{
							JDialog errorBox = new JDialog();
							errorBox.setTitle("Error");
							JLabel errorMsg = new JLabel(parse);
							errorBox.add(errorMsg);
							errorBox.setSize(new Dimension(250,100));
							errorBox.setModal(true);
							//errorBox.setForeground(lightBlue);
							errorBox.setVisible(true);
							return;
						}
					}
					//not ending with .txt
					else{
						JDialog errorBox = new JDialog();
						errorBox.setTitle("Error");
						JLabel errorMsg = new JLabel("Please select a .txt file");
						errorBox.add(errorMsg);
						errorBox.setSize(new Dimension(250,100));
						errorBox.setModal(true);
						//errorBox.setForeground(lightBlue);
						errorBox.setVisible(true);
						return;
					}
				}
				else if(val == JFileChooser.CANCEL_OPTION){
					
				}		
			}
		});
		
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				int i = slider.getValue();
				
					if(i == 1){
						t2.hide();
						t3.hide();
						t4.hide();
						
					}
					else if(i == 2){
						t2.unhide();
						t3.hide();
						t4.hide();
					}
					else if(i == 3){
						t2.unhide();
						t3.unhide();
						t4.hide();
					}
					else{
						t2.unhide();
						t3.unhide();
						t4.unhide();
					}
					t1.clearData();
					t2.clearData();
					t3.clearData();
					t4.clearData();
				
			}
		});
		
		
		
		
	}
	
	//helper classes
	public String processTeams(int sliderVal){
		if(sliderVal == 1){
			String[] names = new String[1];
			names[0] = t1.getText();
			return gp.chooseTeams(1,names);
		}
		else if(sliderVal == 2){
			String[] names = new String[2];
			names[0] = t1.getText();
			names[1] = t2.getText();
			return gp.chooseTeams(2,names);
		}
		else if(sliderVal == 3){
			String[] names = new String[3];
			names[0] = t1.getText();
			names[1] = t2.getText();
			names[2] = t3.getText();
			return gp.chooseTeams(3,names);
			
		}
		else{
			String[] names = new String[4];
			names[0] = t1.getText();
			names[1] = t2.getText();
			names[2] = t3.getText();
			names[3] = t4.getText();
			return gp.chooseTeams(4,names);
		}
	}
	
	
	//no need for a main because these actions 
	//will be performed inside the game logic
	//JUST DOING IT FOR THIS TESTING
	/*public static void main(String args[]){
		//GamePlay gp = new GamePlay();
		WelcomeLayout wl = new WelcomeLayout();
	}
	*/
	
	//---------------
	//inner class
	//---------------
	class InnerBox extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private JTextField team;
		private JPanel textP, teamP;
		private JLabel text;
		
		public InnerBox(int num){
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBackground(Color.BLUE);
			setBorder(new EmptyBorder(10,40,10,40));
			
			//Text part
			textP = new JPanel(new BorderLayout());
			textP.setBackground(Color.DARK_GRAY);
			
			text = new JLabel("Please name Team " + num);
			text.setForeground(Color.WHITE);
			
			textP.add(text);
			
			//Text Field Part
			teamP = new JPanel(new BorderLayout());
			teamP.setBackground(lightBlue);
			team = new JTextField("");
			team.setBackground(lightBlue);
			team.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			teamP.add(team);
			
			//Adding in
			add(textP);
			add(teamP);
			
		}
		
		public String getText(){
			return team.getText();
		}
		
		public void clearData(){
			team.setText("");
		}
		public void hide(){
			team.setVisible(false);
			textP.setVisible(false);
			teamP.setVisible(false);;
			text.setVisible(false);;
		}
		
		public void unhide(){
			team.setVisible(true);
			textP.setVisible(true);
			teamP.setVisible(true);;
			text.setVisible(true);;
		}
	
	}
	
	
}
