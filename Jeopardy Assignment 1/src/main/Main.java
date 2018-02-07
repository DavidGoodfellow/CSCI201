package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Game_Layout.MainScreen;
import Game_Layout.WelcomeLayout;
import game_logic.GamePlay;

public class Main {
	
	public static void main (String [] args){
		 GamePlay gp = new GamePlay();
		//String[] welcomeArgs = new String[4];
		WelcomeLayout wl = new WelcomeLayout(gp);
	
	}
}