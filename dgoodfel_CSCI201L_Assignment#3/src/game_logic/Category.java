package game_logic;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import other_gui.AppearanceConstants;
import other_gui.AppearanceSettings;

public class Category {

	//The category name
	private String category;
	//The category's index in ordering the categories
	private int index;
	//The label displayed on the MainGUI above the game board
	private JLabel categoryLabel;
	
	public Category(String category, int index, String imgStr){
		this.category = category;
		this.index = index;
		
		//create the label
		categoryLabel = new JLabel(category,new ImageIcon(imgStr), JLabel.CENTER);
		categoryLabel.setHorizontalTextPosition(JButton.CENTER);
		categoryLabel.setVerticalTextPosition(JButton.CENTER);
		categoryLabel.setForeground(Color.BLACK);
		categoryLabel.setFont(AppearanceConstants.fontMedium);
		//categoryLabel.setBackground(AppearanceConstants.darkBlue);
		categoryLabel.setForeground(Color.lightGray);
		categoryLabel.setOpaque(true);
		AppearanceSettings.setTextAlignment(categoryLabel);
		categoryLabel.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
	}
	
	public int getIndex(){
		return index;
	}
	
	public JLabel getCategoryLabel(){
		return categoryLabel;
	}
}
