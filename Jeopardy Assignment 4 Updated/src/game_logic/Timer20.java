package game_logic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import other_gui.QuestionGUIElementNetworked;

public class Timer20 extends Thread{
	
	QuestionGUIElementNetworked qGUI;
	Lock lock;
	
	public Timer20(QuestionGUIElementNetworked qGUI){
		super();
		this.qGUI = qGUI;
		lock = new ReentrantLock();
	}

    public void run() {
        this.runTimer();
    }

    public void runTimer(){
    	//lock.lock();
		try {
			int i = 20;
			while (i > 0) {
				System.out.println("T20 Remaining: " + i + " seconds");

				qGUI.setTimerLabel(i);
				i--;
				Thread.sleep(1000L); // 1000L = 1000ms = 1 second
			}
			System.out.println("Q HIT 0");
			//if(qGUI.getTimerLabel().equals(":1")){
				qGUI.questionHitZero();
			//}
		}
        catch (InterruptedException e) {
           //being interrupted is fine..
        	qGUI.removeIcon();
        }
		
    }

}