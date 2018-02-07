package server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FactoryServerGUI extends JFrame {

	public static final long serialVersionUID = 1;
	private static JTextArea textArea;
	private JScrollPane textAreaScrollPane;
	private JButton selectFactoryButton;
	private JButton factoryInfoButton; //Make a new Infobutton for opening a new FactoryInfoFrame
	private JComboBox<String> selectFactoryComboBox;

	public FactoryServerGUI() {
		super(Constants.factoryGUITitleString);
		initializeVariables();
		createGUI();
		addActionAdapters();
		setVisible(true);
	}
	
	private void initializeVariables() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textAreaScrollPane = new JScrollPane(textArea);

		Vector<String> filenamesVector = new Vector<String>();
		File directory = new File(Constants.defaultResourcesDirectory);
		File[] filesInDirectory = directory.listFiles();
		for (File file : filesInDirectory) {
		    if (file.isFile()) {
		    	filenamesVector.add(file.getName());
		    }
		}
		selectFactoryComboBox = new JComboBox<String>(filenamesVector);
		selectFactoryButton = new JButton(Constants.selectFactoryButtonString);
		factoryInfoButton = new JButton("Factory Info"); //Initialize it!
	}
	
	private void createGUI() {
		setSize(Constants.factoryGUIwidth, Constants.factoryGUIheight);
		JPanel northPanel = new JPanel();
		northPanel.add(selectFactoryComboBox);
		northPanel.add(selectFactoryButton);
		northPanel.add(factoryInfoButton); //Add it to the northpanel, which will pop up next to the select factory button
		add(northPanel, BorderLayout.NORTH);
		add(textAreaScrollPane, BorderLayout.CENTER);
	}
	
	private void addActionAdapters() {
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent we) {
				  System.exit(0);
			  }
		});
		
		selectFactoryButton.addActionListener(new ConfigureFactoryListener(selectFactoryComboBox));
		factoryInfoButton.addActionListener(new ActionListener() { //Of course, tack an action listener to this button so it can so stuff
			
			@Override
			public void actionPerformed(ActionEvent e) { //When an action is done
				ConfigureFactoryListener listener = new ConfigureFactoryListener(selectFactoryComboBox); //Get the factory file
				//must read it for it to process into 'factory' and then call a getter for the factory
				listener.readFactory((String)selectFactoryComboBox.getSelectedItem(), false);  //read it
				FactoryInfoFrame info = new FactoryInfoFrame(listener.getFactory(), FactoryServerGUI.this); //create a new infoframe and pass in a pointer to the server gui and the factory
				info.setVisible(true); //set it visible
			}
		});
	}
	
	public static void addMessage(String msg) {
		if (textArea.getText() != null && textArea.getText().trim().length() > 0) {
			textArea.append("\n" + msg);
		}
		else {
			textArea.setText(msg);
		}
	}
}
