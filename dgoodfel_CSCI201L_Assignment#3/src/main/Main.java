package main;

import java.awt.Dimension;

import frames.LoginGUI;
import frames.StartWindowGUI;

public class Main {

	public static void main (String [] args){

		LoginGUI startWindow = new LoginGUI();
		startWindow.setVisible(true);
		Dimension d = startWindow.getSize();
		//System.out.println("height: "+d.getHeight()+"; width: "+d.getWidth());
	}

}
