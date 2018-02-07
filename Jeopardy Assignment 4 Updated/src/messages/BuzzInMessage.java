package messages;

import other_gui.QuestionGUIElementNetworked;

public class BuzzInMessage implements Message {
	//all we need to know is which team buzzed in
	private int team;
	//private QuestionGUIElementNetworked qGUI;
	
	public BuzzInMessage(int team, QuestionGUIElementNetworked qGUI){
		this.team = team;
		//this.qGUI = qGUI;
	}
	
	public int getBuzzInTeam(){
		return team;
	}

}
