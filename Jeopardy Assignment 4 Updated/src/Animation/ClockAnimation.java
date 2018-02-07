package Animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ClockAnimation extends JLabel implements Runnable {

	List<Image> images;
	
	public ClockAnimation(){
		super();
		setIcon(new ImageIcon(new ImageIcon("images/clock.jpg").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
		
		images = new ArrayList<Image>();
		for(int i = 0; i < 143; i++){
			images.add(new ImageIcon("images/clock/frame_"+i+"_delay-0.06s.jpg").getImage());
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		while(true){
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				setIcon(null);
				break;
			}
			setIcon(new ImageIcon(images.get(i).getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
			if(i < 142){
				i++;
			}
			else{
				i = 0;
			}
		}
	}
}
