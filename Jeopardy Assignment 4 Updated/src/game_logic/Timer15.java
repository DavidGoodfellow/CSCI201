package game_logic;

import frames.MainGUINetworked;

public class Timer15 extends Thread{
	
	MainGUINetworked mainGUI;
	
	public Timer15(MainGUINetworked mainGUI){
		super();
		this.mainGUI = mainGUI;
	}

    public void run() {
    		this.runTimer();
    }

    public void runTimer(){
    	try{
        int i = 15;
         while (i>0){
        	 System.out.println("T15 Remaining: "+i+" seconds");
        	 mainGUI.setJeopardyLabel(i);
        	 i--;
        	 Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
         }
        // if()
         mainGUI.gameBoardHitZero();
    	} catch(InterruptedException ie){
    		//being interrupted is fine..
    	}
    }

}