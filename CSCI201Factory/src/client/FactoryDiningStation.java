package client;

import java.awt.Rectangle;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.JTable;

import libraries.ImageLibrary;
import resource.Product;

public class FactoryDiningStation extends FactoryObject{
	
	private Semaphore diningpermit;
	
	public FactoryDiningStation(int x, int y){
		super(new Rectangle(x,y,1,1));
		mLabel = "Dining Table";
		mImage = ImageLibrary.getImage(Constants.resourceFolder + "table" + Constants.png);
		diningpermit = new Semaphore(3);
	}
	
	public void getSpot() throws InterruptedException{
		diningpermit.acquire();
	}
	public void returnSpot(){
		diningpermit.release();
	}
}
