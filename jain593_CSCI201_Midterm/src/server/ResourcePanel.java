package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import libraries.ImageLibrary;
import resource.Resource;

// The resource Panel
class ResourcePanel extends JPanel {
	
	// Serializable
	private static final long serialVersionUID = 1L;

	// instance constructor
	{
		setLayout(new BorderLayout());
	}
	
	// Info
	private int amount;
	private int x;
	private int y;
	
	// Resource
	private Resource resource;
	
	public ResourcePanel(Resource r) {
		this.amount = r.getQuantity();
		this.x = r.getX();
		this.y = r.getY();
		this.resource = r;
		createGUI();
	}
	
	private void createGUI() {
		
		// Add outer panel
		Image img = ImageLibrary.getImage(Constants.defaultResourcesDirectory + "img/" + resource.getName() + ".png");
		JPanel outerPanel = new OuterPanel(img);
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		
		// Amount
		JLabel amountLabel = new JLabel("Amount: " + amount);
		amountLabel.setBackground(Color.gray);
		amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Location
		JLabel locationLabel = new JLabel("Location: (" + x + ", " + y + ")");
		locationLabel.setBackground(Color.gray);
		locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Box Layout
		outerPanel.add(Box.createGlue());
		outerPanel.add(amountLabel);
		outerPanel.add(locationLabel);
		outerPanel.add(Box.createGlue());
		
		add(outerPanel);
	}
	
	// Outer panel
	private class OuterPanel extends JPanel {
		
		// Serializable
		private static final long serialVersionUID = 1L;
		Image img;
		
		// Constructor
		public OuterPanel(Image img) {
			this.img = img;
		}
		
		@Override
		protected void paintComponent(Graphics g)  {
			if (img != null) {
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
			}
		}
	}
	
}
