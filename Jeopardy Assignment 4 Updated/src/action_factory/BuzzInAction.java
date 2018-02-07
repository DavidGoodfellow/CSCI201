package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.BuzzInMessage;
import messages.Message;
import other_gui.QuestionGUIElementNetworked;
import server.Client;

public class BuzzInAction extends Action{

	@Override
	public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData,
			Message message, Client client) {
		BuzzInMessage buzzMessage = (BuzzInMessage) message;
		//System.out.println("1");
		//update the team on the current question to be the one who buzzed in
		client.getCurrentQuestion().updateTeam(buzzMessage.getBuzzInTeam(), gameData);
		//System.out.println("2");
		mainGUI.addUpdate(gameData.getTeamDataList().get(buzzMessage.getBuzzInTeam()).getTeamName() + " buzzed in to answer");
		//System.out.println("3");
		
		client.getCurrentQuestion().interruptAndStartTimer();
		//System.out.println("4");
	}

}
