package frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class LoginGUI extends JFrame {
	private BufferedReader br;
	private FileReader fr;
	
	private JPanel mainPanel;
	private JLabel r1, r2, r3;
	private JTextField r4, r5;
	private JPanel r6;
	private JButton b1, b2;
	
	public LoginGUI(){
		super();
		initializeComponents();
		createGUI();
		addListeners();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeComponents(){
		mainPanel = new JPanel();
		r1 = new JLabel("login or create an account to play");
		r2 = new JLabel("Jeopardy!");
		r3 = new JLabel("");
		r4 = new JTextField("username");
		r5 = new JTextField("password");
		r6 = new JPanel();
		b1 = new JButton("Login");
		b2 = new JButton("Create Account");
		
	}
	
	public void createGUI(){
		//setting background and foreground colors
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, b1, b2);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, mainPanel, r6);
		AppearanceSettings.setForeground(Color.lightGray, r4, r5);
		AppearanceSettings.setBackground(Color.white, b1,b2);
		
		//setting fonts
		AppearanceSettings.setFont(AppearanceConstants.fontLarge, r2);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, r1);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, r3);
		
		//creating the panel
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		//add padding to the sides
		mainPanel.setBorder(new EmptyBorder(10,75,10,75));
		
		//set preferred size
		r1.setPreferredSize(new Dimension(50,400));
		r2.setPreferredSize(new Dimension(50,400));
		r3.setPreferredSize(new Dimension(50,400));
		r4.setPreferredSize(new Dimension(50,400));
		r5.setPreferredSize(new Dimension(50,400));
		r6.setPreferredSize(new Dimension(50,400));
		b1.setPreferredSize(new Dimension(50,199));
		b2.setPreferredSize(new Dimension(50,199));
		
		
		//setting the two buttons to disabled
		b1.setEnabled(false);
		b2.setEnabled(false);
		
		//add everything into the panel
		mainPanel.add(r1);
		mainPanel.add(r2);
		mainPanel.add(r3);
		mainPanel.add(r4);
		mainPanel.add(r5);
		
		//set alignments of each
		r1.setAlignmentX(Component.CENTER_ALIGNMENT);
		r2.setAlignmentX(Component.CENTER_ALIGNMENT);
		r3.setAlignmentX(Component.CENTER_ALIGNMENT);
		r4.setAlignmentX(Component.CENTER_ALIGNMENT);
		r5.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//adding the buttons
		r6.setLayout(new BoxLayout(r6,BoxLayout.X_AXIS));
		r6.setAlignmentX(Component.CENTER_ALIGNMENT);
		r6.add(b1);
		r6.add(b2);
		mainPanel.add(r6);
		add(mainPanel);
		
		setBackground(AppearanceConstants.lightBlue);
		setSize(600, 600);
	}
	
	public String accountExists(){
		String delims = "[ ]+";
		
		try {
			fr = new FileReader("game_files/Accounts.txt");//???
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null){
				String[] account = line.split(delims);
				if(account[0].equals(r4.getText())){
					if( account[1].equals(r5.getText())){
						return "yes";
					}
					return "username";
				}
				line = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "no";
	}
	
	public void login(){
		StartWindowGUI startWindow = new StartWindowGUI();
		startWindow.setVisible(true);
		dispose();
	}
	
	public void addListeners(){
		//Check to see if correct log in information
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				String a1 = accountExists();
				if(a1.equals("no") || a1.equals("username"))
					r3.setText("this password and username combination does not exist");
				else{
					//log in to their account
					login();
				}
				
			}
		});
		
		//check to see if an account w this username already exists
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a){
				String a2 = accountExists();
				if(a2.equals("yes") || a2.equals("username"))
					r3.setText("this username already exists");
				else{
					try {
						FileWriter fw = new FileWriter("game_files/Accounts.txt",true);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write("\n" + r4.getText() + " " + r5.getText());
						bw.close();
						login();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		//TEXT FIELD ACTION LISTENERS
		//clears the background gray message
		r4.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                r4.setText("");
                r4.setForeground(Color.BLACK);
            }
        });
		r5.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                r5.setText("");
                r5.setForeground(Color.BLACK);
            }
        });
		
		r4.addKeyListener(new KeyListener(){

	                public void keyPressed(KeyEvent e){
	                	if(r4.getForeground().equals(Color.lightGray)){
	                		r4.setText("");
		                    r4.setForeground(Color.BLACK);
	                	}
	              
	                }

					@Override
					public void keyTyped(KeyEvent e) {
						// TODO Auto-generated method stub
						if(r4.getForeground().equals(Color.lightGray)){
	                		r4.setText("");
		                    r4.setForeground(Color.BLACK);
	                	}
						
					}

					@Override
					public void keyReleased(KeyEvent e) {
						// TODO Auto-generated method stub
						
					}
	            }
	     );
		
		//to disable/enable the buttons
		r4.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    check();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    check();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    check();
			  }

			  public void check() {
			     if(r4.getText().equals("") || r5.getText().equals("") ||
			    		 r4.getForeground().equals(Color.lightGray) || 
			    		 r5.getForeground().equals(Color.lightGray)){
			    	 b1.setEnabled(false);
			    	 b2.setEnabled(false);
			     }
			     else{
			    	 b1.setEnabled(true);
			    	 b2.setEnabled(true);
			     }
			  }
			  
		});
		
		r5.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    check();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    check();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    check();
			  }

			  public void check() {
			     if(r4.getText().equals("") || r5.getText().equals("") || 
			    		 r4.getForeground().equals(Color.lightGray) || 
			    		 r5.getForeground().equals(Color.lightGray)){
			    	 b1.setEnabled(false);
			    	 b2.setEnabled(false);
			     }
			     else{
			    	 b1.setEnabled(true);
			    	 b2.setEnabled(true);
			     }
			  }
			  
		});
	}
	
	
}
