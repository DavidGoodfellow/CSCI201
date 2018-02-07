import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Customer extends Thread{
	private int name;
	private SleepingBarber sb;
	private Lock customerLock;
	private Condition gettingHaircutCondition;
	
	public Customer(int name, SleepingBarber sb){
		this.name = name;
		this.sb = sb;
		customerLock = new ReentrantLock();
		gettingHaircutCondition = customerLock.newCondition();
	}
	
	public void startHaircut(){
		
	}
	public void finishHaircut(){
		
	}
	public int getCustomerName(){
		return name;
	}
	public void run(){
		boolean seatsAvailable = sb.addCustomerToWaiting(this);
		if(!seatsAvailable){
			Util.printMessage("No seats availale..no haircute for " + name + " today!");
			return;
		}
		//first need to check to see if barber is asleep
		//however we can signal him whenever we want!
		sb.wakeUpBarber();
		try{
			customerLock.lock();
			gettingHaircutCondition.await();
		} catch(InterruptedException ie){
			Util.printMessage("ie getting haircut: " + ie.getMessage());
		} finally {
			customerLock.unlock();
		}
	}
}
