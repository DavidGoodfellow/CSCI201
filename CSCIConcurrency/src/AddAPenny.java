import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AddAPenny implements Runnable {
	
	private static PiggyBank piggy = new PiggyBank();//keeps it consistent among all of the different threads
													//otherwise they would each have a separate on
	public void run(){
		piggy.deposit(1);
	}
	
	public static void main(String [] args){
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i = 0; i < 100; i++){
			//Thread t = new Thread(new AddAPenny());
			//t.start();
			executor.execute(new AddAPenny());//creates the new thread and starts it
		}
		executor.shutdown();
		//are any of the threads still going? If so yield the CPU for this.
		while(!executor.isTerminated()){
			Thread.yield();
		}
		//only get to line 25 once the run method in each have completed
		System.out.println("balance = " + piggy.getBalance());
	}
}

class PiggyBank{
	private int balance = 0;
	private String s = "";
	private Lock myLock = new ReentrantLock();
	
	public int getBalance(){
		return balance;
	}
	public void deposit(int amount){
		myLock.lock();//must always match with an unlock otherwise there will be starvation
		//int newBalance = balance + amount;
		//Thread.yield(); this causes the run method to false complete potentially
		//synchronized(s){//only one instance of s which makes the String s still work
		
		//finally always has to come after a try but it doesn't have to have a catch
		try{
			balance += amount;
		}//there are no checked exceptions thrown so don't need the catch which only handles those.
		//could still be unchecked exceptions but we don't handle those
		//}
		//two operations (1) adding (2) Assigning
		//not always going to get 100 because this could do 1 then yield to 2, less likely but still can happen
		finally{
			myLock.unlock();//should use this with a finally block to ensure that it will run
		}
	}
}