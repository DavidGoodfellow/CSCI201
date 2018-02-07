package Game_Layout;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TeamNames extends JPanel {
	
	private Color lightBlue = new Color(64,224,208);
	
	public TeamNames(){
		JPanel grid = new JPanel(new GridLayout(2,2));
		grid.setBackground(Color.BLUE);
		
		InnerBox t1 = new InnerBox(1);
		InnerBox t2 = new InnerBox(2);
		InnerBox t3 = new InnerBox(3);
		InnerBox t4 = new InnerBox(4);
		
		grid.add(t1);
		grid.add(t2);
		grid.add(t3);
		grid.add(t4);
		
		t1.setVisible(true);
		t2.setVisible(false);
		t3.setVisible(false);
		t4.setVisible(false);
		
		
	}
	
	public void updateTeams(int n){
		
	}
	
	class InnerBox extends JPanel{
		public JPanel panel;
		
		public InnerBox(int num){
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBackground(Color.BLUE);
			
			//text part
			JPanel textP = new JPanel();
			textP.setLayout(new BoxLayout(textP, BoxLayout.X_AXIS));
			textP.setBackground(Color.DARK_GRAY);
			
			JLabel text = new JLabel("Please name Team " + num);
			text.setBorder(new EmptyBorder(10,15,3,15));
			text.setForeground(Color.WHITE);
			
			textP.add(text);
			
			//Text Field
			JTextField team = new JTextField();
			team.setBackground(lightBlue);
			team.setBorder(new EmptyBorder(0,15,15,15));
			
			//adding in
			panel.add(textP);
			panel.add(team);
			
		}
		public void setVisible(boolean b){
			if(b == true){
				panel.setVisible(true);
			}
			else if(b == false){
				panel.setVisible(false);
			}
		}
	}
}
