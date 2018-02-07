import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SleepingBarber extends Thread {
	private int totalCustomers;
	private int maxSeats;
	private ArrayList<Customer> customersWaiting;//used for simplicity but could use another data structure
	private Lock barberLock;
	private Condition sleepingCondition;
	private boolean moreCustomers;
	
	public SleepingBarber(){
		totalCustomers = 10;
		maxSeats = 3;
		customersWaiting = new ArrayList<Customer>();
		barberLock = new ReentrantLock();
		sleepingCondition = barberLock.newCondition();
		moreCustomers = true;
		this.start();
	}
	
	public boolean addCustomerToWaiting(Customer customer){
		if(customersWaiting.size() == maxSeats){
			Util.printMessage("No where fore customer " + customer.getCustomerName());
		}
	}
	
	public void run(){
		while(moreCustomers){
			while(!customersWaiting.isEmpty()){
				Customer customer = customersWaiting.remove(0);
				customer.startHaircut();
				
				try{
					Thread.sleep(1000);//sleep for one second while you are cutting their hair
				} catch(InterruptedException ie){
					Util.printMessage("ie cutting customer's hair: " + ie.getMessage());
				}
				customer.finishHaircut();
				Util.printMessage("Checking for more customers...");
			}
			try{
				barberLock.lock();
				Util.printMessage("No customers, time to sleep...");
				sleepingCondition.await();//barber sleeping in his chair
				Util.printMessage("Someone woke me up!");
			} catch (InterruptedException ie){
				Util.printMessage("ie while barber sleeping: " + ie.getMessage());
			} finally {
				barberLock.unlock();
			}
		}//while(moreCustomers)
		Util.printMessage("All done for today! Time to go home!");
		
	}
	
	public static void main(String [] args){
		SleepingBarber sb = new SleepingBarber();
		
		ExecutorService executors = Executors.newCachedThreadPool();
		for(int i = 0; i < sb.totalCustomers; i++){
			Customer customer = new Customer(i);
			executors.execute(customer);
			try{
				Random rand = new Random();
				//going to wait at a random time between none to 2 seconds until you start the next thread
				int timeBetweenCustomers = rand.nextInt(2000);
				Thread.sleep(timeBetweenCustomers);
			} catch(InterruptedException ie){//happens when another thread called the interrupt method on that one
				Util.printMessage("ie in customers entering: " + ie.getLocalizedMessage());
			}
		}
		executors.shutdown();//no more threads can be added into this pool at this point
		while(!executors.isTerminated()){
			Thread.yield();
		}//know everything has run and finished
		Util.printMessage("No more customers coming today...");
		sb.moreCustomers = false;
	}
}
