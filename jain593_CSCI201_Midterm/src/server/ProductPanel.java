package server;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resource.Product;
import resource.Resource;

class ProductPanel extends JPanel {

	// Serializable
	private static final long serialVersionUID = 1L;
	
	// instance constructor
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	// Product
	private Product product;
	
	// Constructor
	public ProductPanel(Product p) {
		this.product = p;
		createGUI();
	}
	
	// create the GUI
	private void createGUI() {
		for (Resource r: product.getResourcesNeeded()) {
			JLabel label = new JLabel("" + r.getQuantity() + " " + r.getName());
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(Box.createGlue());
			add(label);
		}
	}
}
