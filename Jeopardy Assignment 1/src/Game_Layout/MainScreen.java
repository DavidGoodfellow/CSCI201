package Game_Layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.toIntExact;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game_logic.GamePlay;

public class MainScreen extends JFrame {
	
	private Color lightBlue = new Color(42,182,203);
	
	private JMenuBar jmb;
	private JMenu menu;
	private JMenuItem exitItem;
	private JMenuItem restartItem;
	private JMenuItem newFileItem;
	
	private JPanel eastP, topEastP, centerP, gameBoard, centerGrid, questionBoard;
	private JPanel updatesP;
	private JPanel questionWorthP, questionCatP, teamNameP, finalJP;  
	private JLabel questionWorthL, questionCatL, teamNameL, poseReminder, questionL; 
	private JLabel col5L;
	private JTextField enterA;
	private JButton submitB, sureButton;
	private BetTicker bt1,bt2,bt3,bt4;
	private BetArea ba1, ba2, ba3, ba4;
	private GamePlay gp;
	private JButtonCat j;
	private int questionsAsked;
	private boolean quickPlay;
	private JDialog sureD;
	private JLabel teamL;
	
	public int indexX, indexY;
	private int pointQ, currentTurn;
	private JPanelSide s1,s2,s3,s4;
	
	
	public MainScreen(GamePlay gp, boolean qp){
		super("Jeopardy!!");
		setSize(1000,600);
		
		//CLOSE OPERATION
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		sureButton = new JButton("Exit");
				
		this.gp = gp;
		quickPlay = qp;
		questionsAsked = 0;
		
		gp.startGame();
		
		//set up menu
		menuSetUp();
		
		//east set up
		eastSetUp();
		
		//CARD LAYOUT
		cardSetUp();
		
		//setting up game layout
		gameCard1();
		
		//setting up question card
		gameCard2();
		
		//setting up final jeopardy card
		gameCard3();

		//--------------------
		//ADDING EVERYTHING
		//--------------------
		centerP.add(gameBoard, "game");	
		centerP.add(questionBoard, "question");
		centerP.add(finalJP, "final");
		
		add(eastP, BorderLayout.EAST);
		add(centerP, BorderLayout.CENTER);
		pack();
		addActions();
		//gp.startGame(updatesP);
		setVisible(true);
		
	}
	
	public void addActions(){
		
		//MENU ACTIONS
		newFileItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setVisible(false);
				GamePlay gpNext = new GamePlay();
				WelcomeLayout wl = new WelcomeLayout(gpNext);
			}
		});
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				System.exit(0);
			}
		});
		restartItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setVisible(false);
				gp.checkForRestartOrExit("restart", quickPlay);
			}
		});
		
		sureButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				System.exit(0);
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
		
		submitB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				//System.out.println("here");
				currentTurn = gp.whoseTurn;
				teamL = new JLabel("It is " + gp.teamData.get(currentTurn).getTeamName() + "'s turn");
				teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
				updatesP.add(teamL);
				
				String c = gp.answerQuestion(indexX, indexY, pointQ, enterA.getText());
				//System.out.println(c);
				if(c.equals("format")){
					if(poseReminder.getText().equals("")){
						//System.out.println(1);
						poseReminder.setText("Remember to pose your "
								+ "answer as a question.");
						poseReminder.setVisible(true);
					}
					else{
						//System.out.println(2);
						gp.answerQuestion(indexX, indexY, pointQ, "what is kjfhlsdhfkja");
						JLabel failL = new JLabel(gp.teamData.get(currentTurn).getTeamName() + 
								", you got the answer wrong! $" + pointQ + " will be subtracted from your score.");
						failL.setFont(new Font("Times New Roman",Font.BOLD, 10));
						updatesP.add(failL);
						menuSwitch();
						poseReminder.setText("");
						
						if(currentTurn == 0){
							s1.setScore(gp.teamData.get(currentTurn).getPoints());
						}
						else if(currentTurn == 1){
							s2.setScore(gp.teamData.get(currentTurn).getPoints());
						}
						else if(currentTurn == 2){
							s3.setScore(gp.teamData.get(currentTurn).getPoints());
						}
						else if(currentTurn == 3){
							s4.setScore(gp.teamData.get(currentTurn).getPoints());
						}
					}
				}
				else if(c.equals("fail")){
					//System.out.println(3);
					JLabel failL = new JLabel(gp.teamData.get(currentTurn).getTeamName() + 
							", you got the answer wrong! $" + pointQ + " will be subtracted from your score.");
					failL.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(failL);
					menuSwitch();
					poseReminder.setText("");
					if(currentTurn == 0){
						//System.out.println(gp.teamData.get(currentTurn).getPoints());
						s1.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 1){
						s2.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 2){
						s3.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 3){
						s4.setScore(gp.teamData.get(currentTurn).getPoints());
					}
				}
				else if(c.equals("pass")){
					//System.out.println(4);
					JLabel passL = new JLabel(gp.teamData.get(currentTurn).getTeamName() + 
							", you got the answer right! $" + pointQ + " will be added to your score.");
					passL.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(passL);
					menuSwitch();
					poseReminder.setText("");
					
					if(currentTurn == 0){
						s1.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 1){
						s2.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 2){
						s3.setScore(gp.teamData.get(currentTurn).getPoints());
					}
					else if(currentTurn == 3){
						s4.setScore(gp.teamData.get(currentTurn).getPoints());
					}
				}
				/*else{
					poseReminder.setText("");
				}*/
			}
		});
		
		
	}
	
	public void menuSwitch(){
		//System.out.println(currentTurn);
		
		
		if(quickPlay == true){
			if(questionsAsked < 5){
				((CardLayout)centerP.getLayout()).show(centerP, "game");
			}
			else{
				if(someonePos()){
					((CardLayout)centerP.getLayout()).show(centerP, "final");
					
					JLabel uP = new JLabel("Welcome to Final Jeopardy!");
					uP.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(uP);
					
					//disable non-finalists
					List<Integer> nonfinalTeams = gp.getNonFinalists();
					for(int i = 0; i < nonfinalTeams.size(); i++){
						int curr = nonfinalTeams.get(i);
						if(curr == 0){
							bt1.notFinalist();
							ba1.notFinalist();
						}
						else if(curr == 1){
							bt2.notFinalist();
							ba2.notFinalist();
						}
						else if(curr == 2){
							bt3.notFinalist();
							ba3.notFinalist();
						}
						else if(curr == 3){
							bt4.notFinalist();
							ba4.notFinalist();
						}
					}
					
					//update finalists
					List<Integer> finalTeams = gp.getFinalists();
					for(int i = 0; i < finalTeams.size(); i++){
						int curr = finalTeams.get(i);
						if(curr == 0){
							bt1.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 1){
							bt2.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 2){
							bt3.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 3){
							bt2.updateInfo(gp.teamData.get(curr).getPoints());
						}
					}
				}
				else{
					JLabel uP = new JLabel("Everyone was eliminated from the game");
					uP.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(uP);
					FinalDialog fd = new FinalDialog(false);
					((CardLayout)centerP.getLayout()).show(centerP, "game");
					setEnabled(false);
				}
			}
		}
		else if(quickPlay == false){
			if(questionsAsked < 25){
				((CardLayout)centerP.getLayout()).show(centerP, "game");
			}
			else{
				if(someonePos()){
					((CardLayout)centerP.getLayout()).show(centerP, "final");
					JLabel uP = new JLabel("Welcome to Final Jeopardy!");
					uP.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(uP);
					
					//disable non-finalists
					List<Integer> nonfinalTeams = gp.getNonFinalists();
					for(int i = 0; i < nonfinalTeams.size(); i++){
						int curr = nonfinalTeams.get(i);
						if(curr == 0){
							bt1.notFinalist();
							ba1.notFinalist();
						}
						else if(curr == 1){
							bt2.notFinalist();
							ba2.notFinalist();
						}
						else if(curr == 2){
							bt3.notFinalist();
							ba3.notFinalist();
						}
						else if(curr == 3){
							bt4.notFinalist();
							ba4.notFinalist();
						}
					}
					
					//update finalists
					List<Integer> finalTeams = gp.getFinalists();
					for(int i = 0; i < finalTeams.size(); i++){
						int curr = finalTeams.get(i);
						if(curr == 0){
							bt1.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 1){
							bt2.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 2){
							bt3.updateInfo(gp.teamData.get(curr).getPoints());
						}
						else if(curr == 3){
							bt2.updateInfo(gp.teamData.get(curr).getPoints());
						}
					}
				}
				else{
					JLabel uP = new JLabel("Everyone was eliminated from the game");
					uP.setFont(new Font("Times New Roman",Font.BOLD, 10));
					updatesP.add(uP);
					FinalDialog fd = new FinalDialog(false);
					((CardLayout)centerP.getLayout()).show(centerP, "game");
					setEnabled(false);
				}
			}
		}
	}
	
	public boolean someonePos(){
		for(int i = 0; i < gp.numberOfTeams; i++){
			if(gp.teamData.get(i).getPoints() > 0)
				return true;
		}
		return false;
	}
	
	
	//no need for a main because these actions 
		//will be performed inside the game logic
		//JUST DOING IT FOR THIS TESTING
		
		
		public void menuSetUp(){
			//---------------------
			// MENU
			//--------------------
			
			
			jmb = new JMenuBar();
			this.setJMenuBar(jmb);
			menu = new JMenu("Menu");
			jmb.add(menu);
			exitItem = new JMenuItem("Exit");
			menu.add(exitItem);
			restartItem = new JMenuItem("Restart this Game");
			menu.add(restartItem);
			newFileItem = new JMenuItem("Choose New Game File");
			menu.add(newFileItem);
		}
		
		public void eastSetUp(){
			//---------------------
			// EAST SIDE
			//--------------------

			eastP = new JPanel();
			eastP.setBackground(lightBlue);
			eastP.setLayout(new BoxLayout(eastP, BoxLayout.Y_AXIS));
			
			topEastP = new JPanel();
			topEastP.setBackground(lightBlue);
			topEastP.setLayout(new GridLayout(gp.numberOfTeams + 1,1));
			
			if(gp.numberOfTeams == 1){
				s1 = new JPanelSide(0);
				topEastP.add(s1);
			}
			else if(gp.numberOfTeams == 2){
				s1 = new JPanelSide(0);
				s2 = new JPanelSide(1);
				topEastP.add(s1);
				topEastP.add(s2);
			}
			else if(gp.numberOfTeams == 3){
				s1 = new JPanelSide(0);
				s2 = new JPanelSide(1);
				s3 = new JPanelSide(2);
				topEastP.add(s1);
				topEastP.add(s2);
				topEastP.add(s3);
			}
			else if(gp.numberOfTeams == 4){
				s1 = new JPanelSide(0);
				s2 = new JPanelSide(1);
				s3 = new JPanelSide(2);
				s4 = new JPanelSide(3);
				topEastP.add(s1);
				topEastP.add(s2);
				topEastP.add(s3);
				topEastP.add(s4);
			}
			
			JPanel progressP = new JPanel();
			progressP.setBackground(lightBlue);
			progressP.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
			JLabel progressL = new JLabel("Game Progress");
			progressL.setFont(new Font("Times New Roman",Font.BOLD, 23));
			progressL.setBorder(new EmptyBorder(10,10,10,10));
			progressP.add(progressL);
			topEastP.add(progressP);
			
			updatesP = new JPanel();
			
			updatesP.setBackground(new Color(42,182,203));
			updatesP.setLayout(new FlowLayout(FlowLayout.LEFT));
			updatesP.setAlignmentX(Component.LEFT_ALIGNMENT);
			updatesP.setPreferredSize(new Dimension(150,200));
			JLabel welcome = new JLabel("Welcome to Jeopardy!");
			JLabel welcome2 = new JLabel("The team to go first will be "+ gp.teamData.get(gp.whoseTurn).getTeamName());
			welcome.setFont(new Font("Times New Roman",Font.BOLD, 10));
			welcome.setHorizontalAlignment(JLabel.LEFT);
			welcome2.setFont(new Font("Times New Roman",Font.BOLD, 10));
			welcome.setHorizontalAlignment(JLabel.LEFT);
			updatesP.add(welcome);
			updatesP.add(welcome2);
			//way to add updates
			//take updatesP outside as member variable and
			//then create listener that can add to it
			
			
			eastP.add(topEastP);
			eastP.add(updatesP);
		
		}
		
		public void cardSetUp(){
			centerP = new JPanel();
			centerP.setPreferredSize(new Dimension(650,300));
			centerP.setBackground(Color.BLUE);
			centerP.setLayout(new CardLayout());
		}
		
		public void gameCard1(){
			//----------------
			// GAME BOARD
			//----------------
			
			gameBoard = new JPanel();
			gameBoard.setLayout(new BorderLayout());
			gameBoard.setBackground(Color.GRAY);
			
			
			//NORTH PART OF GAME BOARD
			JPanel northP = new JPanel();
			northP.setLayout(new BoxLayout(northP, BoxLayout.Y_AXIS));
					
			JPanel jeopardyP = new JPanel(new BorderLayout());
			jeopardyP.setBackground(lightBlue);
			JLabel jeopardyL = new JLabel("Jeopardy");
			jeopardyL.setFont(new Font("Times New Roman",Font.BOLD, 30));
			jeopardyP.add(jeopardyL);
			jeopardyP.setVisible(true);
			northP.add(jeopardyP);
					
			JPanel catP = new JPanel();
			catP.setLayout(new BoxLayout(catP, BoxLayout.X_AXIS));
			catP.setBackground(Color.DARK_GRAY);
			catP.setBorder(new EmptyBorder(0,2,0,2));
			JPanel tempP;
			JLabel tempL;
			for(int i = 1; i < 6; i++){
				tempP = new JPanel(new BorderLayout());
				tempP.setBackground(Color.DARK_GRAY);
				tempL = new JLabel(gp.parsedCategories[i-1]);
				tempL.setForeground(Color.WHITE);
				tempL.setBorder(new EmptyBorder(10,10,10,10));
				//tempP.setBorder(new EmptyBorder(0,10,0,10));
				tempP.add(tempL);	
				catP.add(tempP);
			}
			northP.add(catP);
			add(northP, BorderLayout.NORTH);
			
			//CENTER PART OF GAMEBOARD
			centerGrid = new JPanel();
			centerGrid.setLayout(new GridLayout(5,5));
			//centerGrid.setBorder(new EmptyBorder(100,100,100,100));
			centerGrid.setBackground(Color.BLUE);
			for(int i = 0; i < 25; i++){
				int point = 0;
				String cat = "";
				//point value
				if(i < 5){
					point = Integer.parseInt(gp.parsedPointValues[0]);
				}
				else if(i < 10){
					point = Integer.parseInt(gp.parsedPointValues[1]);
				}
				else if(i < 15){
					point = Integer.parseInt(gp.parsedPointValues[2]);
				}
				else if(i < 20){
					point = Integer.parseInt(gp.parsedPointValues[3]);
				}
				else if(i < 25){
					point = Integer.parseInt(gp.parsedPointValues[4]);
				}
				//cat value
				if(i % 5 == 0){
					cat = gp.parsedCategories[0];
				}
				else if(i % 5 == 1){
					cat = gp.parsedCategories[1];
				}
				else if(i % 5 == 2){
					cat = gp.parsedCategories[2];
				}
				else if(i % 5 == 3){
					cat = gp.parsedCategories[3];
				}
				else if(i % 5 == 4){
					cat = gp.parsedCategories[4];
				}
				j = new JButtonCat(point,cat);
				
				centerGrid.add(j);
			}
			
			
			
			gameBoard.setBorder(new EmptyBorder(3,10,20,10));
			gameBoard.add(northP,BorderLayout.NORTH);
			gameBoard.add(centerGrid, BorderLayout.CENTER);
			
			
		}
		
		public void gameCard2(){
			//----------------
			// QUESTION BOARD
			//----------------
			questionBoard = new JPanel(new BorderLayout());
			
			//NORTH PART OF QUESTION BOARD
			JPanel northQ = new JPanel();
			northQ.setLayout(new BoxLayout(northQ, BoxLayout.Y_AXIS));
			northQ.setBackground(Color.DARK_GRAY);
			
			JPanel northInfo = new JPanel();
			northInfo.setBackground(Color.BLUE);
			northInfo.setLayout(new BoxLayout(northInfo, BoxLayout.X_AXIS));
			
			teamNameP = new JPanel(new BorderLayout());
			teamNameP.setBackground(Color.BLUE);
			//somehow get the team name
			teamNameL = new JLabel("Team 1");
			teamNameL.setBorder(new EmptyBorder(10,10,10,10));
			teamNameL.setForeground(Color.WHITE);
			teamNameP.add(teamNameL);
			northInfo.add(teamNameP);
			
			questionCatP = new JPanel(new BorderLayout());
			questionCatP.setBackground(Color.BLUE);
			//somehow get the team name
			questionCatL = new JLabel("Category 1");
			questionCatL.setBorder(new EmptyBorder(10,10,10,10));
			questionCatL.setForeground(Color.WHITE);
			questionCatP.add(questionCatL);
			northInfo.add(questionCatP);
			
			questionWorthP = new JPanel(new BorderLayout());
			questionWorthP.setBackground(Color.BLUE);
			//somehow get the team name
			questionWorthL = new JLabel("$100");
			questionWorthL.setBorder(new EmptyBorder(10,10,10,10));
			questionWorthL.setForeground(Color.WHITE);
			questionWorthP.add(questionWorthL);
			northInfo.add(questionWorthP);
			
			JPanel grayMessage = new JPanel(new BorderLayout());
			grayMessage.setBackground(Color.DARK_GRAY);
			poseReminder = new JLabel("");
			poseReminder.setForeground(Color.WHITE);
			poseReminder.setFont(new Font("Times New Roman",Font.BOLD, 10));
			poseReminder.setBorder(new EmptyBorder(20,10,20,10));
			grayMessage.add(poseReminder);
			//grayMessage.remove(poseReminder);
			
			//CENTER AREA FOR QUESTION CARD
			
			JPanel questionC = new JPanel();
			questionC.setLayout(new BoxLayout(questionC, BoxLayout.Y_AXIS));
			questionC.setBackground(Color.DARK_GRAY);
			questionC.setBorder(new EmptyBorder(0,15,10,15));
			
			
			//QUESTION BOX
			JPanel questionBox = new JPanel();
			questionBox.setBackground(lightBlue);
			questionBox.setPreferredSize(new Dimension(250,250));
			questionL = new JLabel("Test");
			questionL.setFont(new Font("Times New Roman",Font.BOLD, 30));
			questionBox.add(questionL);
			//questionL.setVisible(false);
			//questionBox.remove(questionL);
			
			//ANSWER MODULE
			JPanel answerModule = new JPanel();
			answerModule.setBackground(Color.DARK_GRAY);
			answerModule.setBorder(new EmptyBorder(10,0,10,0));
			answerModule.setLayout(new BorderLayout());
			
			enterA = new JTextField();
			enterA.setBackground(Color.WHITE);
			enterA.setForeground(Color.LIGHT_GRAY);
			//enterA.setPreferredSize(new Dimension());
			answerModule.add(enterA, BorderLayout.CENTER);
			
			submitB = new JButton("Submit Answer");
			submitB.setBackground(Color.WHITE);
			submitB.setForeground(Color.BLACK);
			answerModule.add(submitB, BorderLayout.EAST);
			
			
			//ADDING TOGETHER QUESTION BOARD
			northQ.add(northInfo);
			northQ.add(grayMessage);
			questionC.add(questionBox);
			questionC.add(answerModule);
			
			questionBoard.add(northQ, BorderLayout.NORTH);
			questionBoard.add(questionC, BorderLayout.CENTER);
		}
		
		public void gameCard3(){
			//----------------
			// FINAL JEOPARDY
			//----------------
			//MAIN PANEL
			finalJP = new JPanel();
			finalJP.setBackground(Color.DARK_GRAY);
			finalJP.setLayout(new GridLayout(8,1));
			finalJP.setBorder(new EmptyBorder(3,10,20,10));
			
			//1/8
			JPanel col1 = new JPanel(new BorderLayout());
			col1.setBackground(Color.BLUE);
			JLabel col1L = new JLabel("Final Jeopardy Round");
			col1L.setFont(new Font("Times New Roman",Font.BOLD, 30));
			col1L.setForeground(Color.WHITE);
			col1.add(col1L);
			finalJP.add(col1);
			
			//2-5
			if(gp.numberOfTeams == 1){
				bt1 = new BetTicker(0, 0);
				finalJP.add(bt1);
			}
			else if(gp.numberOfTeams == 2){
				bt1 = new BetTicker(0, 0);
				bt2 = new BetTicker(1, 0);
				finalJP.add(bt1);
				finalJP.add(bt2);
			}
			else if(gp.numberOfTeams == 3){
				bt1 = new BetTicker(0, 0);
				bt2 = new BetTicker(1, 0);
				bt3 = new BetTicker(2, 0);
				finalJP.add(bt1);
				finalJP.add(bt2);
				finalJP.add(bt3);
			}
			else{
				bt1 = new BetTicker(0, 0);
				bt2 = new BetTicker(1, 0);
				bt3 = new BetTicker(2, 0);
				bt4 = new BetTicker(3, 0);
				finalJP.add(bt1);
				finalJP.add(bt2);
				finalJP.add(bt3);
				finalJP.add(bt4);
			}
			
			//5/8
			JPanel col5 = new JPanel(new BorderLayout());
			col5.setBackground(lightBlue);
			col5L = new JLabel("And the question is..");
			col5L.setFont(new Font("Times New Roman",Font.BOLD, 20));
			col5.add(col5L);
			
			//7/8
			JPanel col6 = new JPanel();
			col6.setLayout(new BoxLayout(col6, BoxLayout.X_AXIS));
			col6.setBackground(Color.DARK_GRAY);
			//final jeopardy answers
			ba1 = new BetArea(0);
			col6.add(ba1);
			if(gp.numberOfTeams > 1){
				ba2 = new BetArea(1);
				col6.add(ba2);
			}
			
			
			//8/8
			JPanel col7 = new JPanel();
			col7.setLayout(new BoxLayout(col7, BoxLayout.X_AXIS));
			col7.setBackground(Color.DARK_GRAY);
			if(gp.numberOfTeams > 2){
				ba3 = new BetArea(2);
				col7.add(ba3);
			}
			if(gp.numberOfTeams > 3){
				ba4 = new BetArea(3);
				col7.add(ba4);
			}
		
			
			//adding all the cols together
			finalJP.add(col5);
			finalJP.add(col6);
			finalJP.add(col7);
		}
		
		//---------------------------
		//INNER CLASSES
		//----------------------------
		
		public class BetTicker extends JPanel{
			
			private JSlider slider;
			private JLabel bet;
			private JButton setBet;
			private int index;
			
			public BetTicker(int index, int money){
				setLayout(new BorderLayout());
				setBackground(Color.DARK_GRAY);
				this.index = index;
				//WEST SIDE
				JLabel teamName = new JLabel(gp.teamData.get(index).getTeamName() + "'s bet");
				teamName.setForeground(Color.WHITE);
				teamName.setBorder(new EmptyBorder(10,10,10,10));
				
				//CENTER
				JPanel cp = new JPanel();
				cp.setLayout(new BorderLayout());
				cp.setBackground(Color.DARK_GRAY);
				
				slider = new JSlider(JSlider.HORIZONTAL);
				slider.setMaximum(money);
				slider.setPaintLabels(true);
				slider.setPaintTicks(true);
				slider.setMajorTickSpacing(money/2);
				slider.setBackground(Color.DARK_GRAY);
				slider.setForeground(Color.WHITE);
				slider.setMinimum(0);
				slider.setValue(0);
				
				bet = new JLabel("$0");
				bet.setBackground(Color.DARK_GRAY);
				bet.setForeground(Color.WHITE);
				bet.setBorder(new EmptyBorder(10,10,10,10));
				
				cp.add(slider, BorderLayout.CENTER);
				cp.add(bet,BorderLayout.EAST);
				
				//EAST
				setBet = new JButton("Set Bet");
				setBet.setBackground(Color.WHITE);
				
				setBorder(new EmptyBorder(7,10,7,10));
				add(teamName, BorderLayout.WEST);
				add(cp, BorderLayout.CENTER);
				add(setBet, BorderLayout.EAST);
				
				addActions();
			}
			
			public void notFinalist(){
				slider.setEnabled(false);
				setBet.setEnabled(false);
			}
			
			public void updateInfo(Long point){
				int bar = toIntExact(point);
				slider.setMaximum(bar);
				slider.setPaintLabels(true);
				slider.setPaintTicks(true);
				slider.setMajorTickSpacing(bar/10);
			}
			/*public boolean isEnab(){
				return setBet.isEnabled();
			}
			*/
			public void addActions(){
				setBet.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						setBet.setEnabled(false);
						slider.setEnabled(false);
						
						if(gp.numberOfTeams == 1){
							col5L.setText(gp.finalJeopardyQuestion);
							teamL = new JLabel("Final Question is now displayed");
							teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
							updatesP.add(teamL);
						}
						else if(gp.numberOfTeams == 2){
							if(bt1.isActive() == false && bt2.isActive() == false){
								col5L.setText(gp.finalJeopardyQuestion);
								teamL = new JLabel("Final Question is now displayed");
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
							}
						}
						else if(gp.numberOfTeams == 3){
							if(bt1.isActive() == false && bt2.isActive() == false &&
									bt3.isActive() == false){
								col5L.setText(gp.finalJeopardyQuestion);
								teamL = new JLabel("Final Question is now displayed");
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
							}
						}
						else{
							if(bt1.isActive() == false && bt2.isActive() == false &&
									bt3.isActive() == false && bt4.isActive() == false){
								col5L.setText(gp.finalJeopardyQuestion);
								teamL = new JLabel("Final Question is now displayed");
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
							}
						}
						//setBet for each
						gp.makeBet(index,slider.getValue());
						
						teamL = new JLabel(gp.teamData.get(index).getTeamName() + " bet $"
								+ slider.getValue());
						teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
						updatesP.add(teamL);
					}
				});
				
				slider.addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent e){
						int value = slider.getValue();
						bet.setText("$"+value);
					}
				});
			}
			
			public boolean isActive(){
				return setBet.isEnabled();
			}
		}
		
		public class BetArea extends JPanel{
			
			
			private JTextField answer;
			private JButton answerB;
			private int index;
			
			public BetArea(int index){
				
				setBackground(Color.DARK_GRAY);
				setLayout(new BorderLayout());
				this.index = index;
				//Center
				answer = new JTextField(gp.teamData.get(index).getTeamName() + ", enter your answer.");
				answer.setBackground(Color.WHITE);
				answer.setForeground(Color.LIGHT_GRAY);
				add(answer, BorderLayout.CENTER);
				
				//east
				answerB = new JButton("Submit Answer");
				answerB.setBackground(Color.WHITE);
				//answerB.setBorder(new EmptyBorder(10,10,10,10));
				
				setBorder(new EmptyBorder(8,10,15,10));
				add(answerB, BorderLayout.EAST);
				addActions();
			}
			
			public boolean isActive(){
				return answerB.isEnabled();
			}
			public void notFinalist(){
				answerB.setEnabled(false);
				answer.setEnabled(false);
			}
			
			public void addActions(){
				answerB.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						answerB.setEnabled(false);
						answer.setEnabled(false);
						
						//integration for playing final card
						gp.playFinalJeopardy(index, answer.getText());
						
						if(gp.numberOfTeams == 1){
							//enter
							teamL = new JLabel("The answer is: " + gp.finalJeopardyAnswer);
							teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
							updatesP.add(teamL);
							FinalDialog fd = new FinalDialog(true);
						}
						else if(gp.numberOfTeams == 2){
							if(ba1.isActive() == false && ba2.isActive() == false){
								//enter
								teamL = new JLabel("The answer is: " + gp.finalJeopardyAnswer);
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
								FinalDialog fd = new FinalDialog(true);
							}
						}
						else if(gp.numberOfTeams == 3){
							if(ba1.isActive() == false && ba2.isActive() == false &&
									ba3.isActive() == false){
								//enter
								teamL = new JLabel("The answer is: " + gp.finalJeopardyAnswer);
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
								FinalDialog fd = new FinalDialog(true);
							}
						}
						else{
							if(ba1.isActive() == false && ba2.isActive() == false &&
									ba3.isActive() == false && ba4.isActive() == false){
								//enter
								teamL = new JLabel("The answer is: " + gp.finalJeopardyAnswer);
								teamL.setFont(new Font("Times New Roman",Font.BOLD, 10));
								updatesP.add(teamL);
								FinalDialog fd = new FinalDialog(true);
							}
						}
					
					}
				});
				
				answer.addMouseListener(new MouseAdapter(){
		            @Override
		            public void mouseClicked(MouseEvent e){
		                answer.setText("");
		                answer.setForeground(Color.BLACK);
		            }
		        });
				
				
			}
		}
		
		public class JButtonCat extends JButton implements ActionListener{
			
			private String cat;
			private int points;
			private JLabel teamsTurn;
			
			public JButtonCat(int point, String cat){
				super(Integer.toString(point));
				points = point;
				this.cat = cat;
				setBackground(Color.DARK_GRAY);
				setOpaque(true);
				setBorder(new LineBorder(Color.BLUE));
				setBorderPainted(true);
				setForeground(Color.WHITE);
				indexX = gp.categoriesMap.get(cat.toLowerCase().trim()).getIndex();
				indexY = gp.pointValuesMapToIndex.get(point);
				//System.out.println(indexX + " : " + indexY);
				addActionListener(this);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setEnabled(false);
			
				//set up question frame
				
				((CardLayout)centerP.getLayout()).show(centerP, "question");
				
				questionL.setText(gp.questions[indexX][indexY].getQuestion());
				questionWorthL.setText("$" + Integer.toString(pointQ));
				questionCatL.setText(cat);
				teamNameL.setText(gp.teamData.get(gp.whoseTurn).getTeamName());
				questionsAsked++;
				pointQ = points;
				
				
			}
		}
		
		public class FinalDialog extends JDialog{
			//enter data for the message and find winners and display
			
			//okay and exit button
			private JButton okay, exit;
			private JLabel finalL1;
			
			public FinalDialog(boolean winner){
				JPanel finalP = new JPanel();
		        finalP.setLayout(new BoxLayout(finalP, BoxLayout.Y_AXIS));
		        finalP.setBackground(lightBlue);
		        okay = new JButton("Okay");
		        exit = new JButton("Exit");
		        setSize(new Dimension(400,200));
		        
		        JPanel finalB = new JPanel();
		        finalB.setLayout(new BoxLayout(finalB, BoxLayout.X_AXIS));
		        finalB.setBackground(lightBlue);
		        finalB.add(okay);
		        finalB.add(exit);
		        
				if(winner == false){
			        finalL1 = new JLabel("No winners :( ");
			        finalP.add(finalL1);	        
				}
				else{
					finalL1 = new JLabel("And the winner(s) is...");
					ArrayList<Integer> winners = gp.getWinners();
					
					finalP.add(finalL1);
					
					for(int i = 0; i < winners.size(); i++){
						JLabel w = new JLabel(gp.teamData.get(winners.get(i)).getTeamName()+"!");
						w.setFont(new Font("Times New Roman",Font.BOLD, 30));
						finalP.add(w);
					}
					
				}
				finalP.add(finalB);
				add(finalP);
				setVisible(true);
				addActions();
			}
			
			public void addActions(){
				exit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						System.exit(0);
					}
				});
				okay.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						setVisible(false);

					}
				});
			}
			
		}
		
		public class JPanelSide extends JPanel{
			private JLabel name, score;
			
			public JPanelSide(int index){
				setLayout(new GridLayout(1,2));
				JPanel nameP = new JPanel(new BorderLayout());
				nameP.setBackground(Color.DARK_GRAY);
				//System.out.println(gp.teamData.get(i).getTeamName());
				name = new JLabel(gp.teamData.get(index).getTeamName());
				name.setForeground(Color.WHITE);
				//somehow get their scores
				JPanel scoreP = new JPanel(new BorderLayout());
				scoreP.setBackground(Color.DARK_GRAY);
				score = new JLabel("$" + 0);
				score.setForeground(Color.WHITE);
				
				nameP.add(name);
				scoreP.add(score);
				add(nameP);
				add(scoreP);
			}
			
			public void setScore(Long val){
				score.setText("$" + Long.toString(val));
			}
		}
		
		//main function
		/*public static void main(String args[]){
			MainScreen wl = new MainScreen(args);
		}*/
		
}



