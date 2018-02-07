package Animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class FishAnimation extends JLabel implements Runnable {

	List<Image> images;
	
	public FishAnimation(){
		super();
		setIcon(new ImageIcon(new ImageIcon("images/entertainment.jpg").getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
		
		images = new ArrayList<Image>();
		for(int i = 1; i < 8; i++){
			images.add(new ImageIcon("images/fish/frame_"+i+"_delay-0.1s.jpg").getImage());
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				setIcon(null);
				break;
			}
			setIcon(new ImageIcon(images.get(i).getScaledInstance(80, 80, Image.SCALE_DEFAULT)));
			if(i < 6){
				i++;
			}
			else{
				i = 0;
			}
		}
	}
}