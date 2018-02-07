package client;

import java.awt.Rectangle;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;

import libraries.ImageLibrary;
import resource.Product;

public class FactoryCoffeeStation extends FactoryObject{
	
	ArrayList<String> orders;
	
	public FactoryCoffeeStation(int x, int y){
		super(new Rectangle(x,y,1,1));
		mLabel = "Coffee Shop";
		mImage = ImageLibrary.getImage(Constants.resourceFolder + "coffee" + Constants.png);
		orders = new ArrayList<String>();
	}
	
	public void orderCoffee(int workerNum){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String order = "Worker"+workerNum+" ordered coffee at " + timestamp;
		orders.add(order);
	}
	
	public ArrayList<String> getOrders(){
		return orders;
	}
}