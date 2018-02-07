package networkMessages;

import java.io.Serializable;

import javax.swing.JPanel;

import game_logic.GameData;
import other_gui.TeamGUIComponents;

public class ChatMessage implements Serializable {
	public static final long serialVersionUID = 1;
	private String message;
	private String name;
	private GameData gd;
	private JPanel panel;
	private String username;
	private int x;
	private int y;
	TeamGUIComponents tgui;
	int pointVal;
	boolean posNeg;
	
	public ChatMessage(String name, String message){
		this.message = message;
		this.name = name;
		gd = null;
		panel = null;
		username = null;
		x = -1;
		y = -1;
		tgui = null;
		pointVal = 0;
	}
	
	public ChatMessage(String name, String message, GameData gd){
		this.message = message;
		this.name = name;
		this.gd = gd;
		panel = null;
		username = null;
		x = -1;
		y = -1;
		tgui = null;
		pointVal = 0;
	}
	
	public ChatMessage(String name, String message, TeamGUIComponents tgui, int pointVal, boolean posNeg){
		this.message = message;
		this.name = name;
		gd = null;
		panel = null;
		username = null;
		x = -1;
		y = -1;
		this.tgui = tgui;
		this.pointVal = pointVal;
		this.posNeg = posNeg;
	}
	
	public ChatMessage(String name, String message, GameData gd, JPanel panel){
		//switch the panel but depending on it will disable/enable
		this.message = message;
		this.name = name;
		this.gd = gd;
		this.panel = panel;
		username = null;
		x = -1;
		y = -1;
		tgui = null;
		pointVal = 0;
	}
	public ChatMessage(String name, String message,int x, int y){
		this.name = name;
		this.message = message;
		this.x = x;
		this.y = y;
		tgui = null;
		pointVal = 0;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	public String getUsername(){
		return username;
	}

	public String getMessage() {
		return message;
	}
	public String getName(){
		return name;
	}
	public void setGameData(GameData gd){
		this.gd = gd;
	}
	public GameData getGameData(){
		return gd;
	}
	public int getX(){
		return x;
	}
	public TeamGUIComponents getTeamGUI(){
		return tgui;
	}
	public int getPointVal(){
		return pointVal;
	}
	public boolean getPosNeg(){
		return posNeg;
	}
	public int getY(){
		return y;
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
}
