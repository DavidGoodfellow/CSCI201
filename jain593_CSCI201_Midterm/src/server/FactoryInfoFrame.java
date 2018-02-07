package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import resource.Factory;
import resource.Product;
import resource.Resource;

public class FactoryInfoFrame extends JDialog implements ActionListener { //It's going to be like a JDialog, but has a lot of new stuff, also needs an AL to do button clicks

	// Serializable
	private static final long serialVersionUID = 1L;
	// Factory
	private Factory factory;
	
	// Parent JFrame
	private JFrame parent;
	
	// Instance Constructor
	{
		setSize(800, 600); //set the size
		//setModal(true); ignore this
		// Set a menu bar
		JMenuBar bar = new JMenuBar(); //New menu bar
		JMenuItem item = new JMenuItem("About"); //the about menu button
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK)); //Accelerator for the about panel
		item.addActionListener(this); //Allow actions to be linked to the about button
		bar.add(item); //Add the about button
		setJMenuBar(bar); //Add the JMenubar to the dialogbox
	}
	
	// Constructor
	public FactoryInfoFrame(Factory factory, JFrame parent)
	{	
		super(parent, factory.getName() + " Information"); //Super call
		this.factory = factory; //set the factory
		this.parent = parent; //set the parent frame of this dialogbox
		
		add(new InfoPanel(this.factory)); //Add the infopanel, done below
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this.parent, "CSCI201 Solution"); //Create a new message dialog that shows a name. easy peasy
                //JOptionPane.showMessageDialog(this.parent, "Rainbow Dash");				
	}
	
	// Private class that extends JPanel, to contain all info for us to see
	private class InfoPanel extends JPanel {
		
		// Serializable
		private static final long serialVersionUID = 1L;

		private Factory factory; //pointer to the factory
		
		// Instance Constructor
		{
			setLayout(new BorderLayout()); //New borderlayout
			// the window is titled
			setBorder(new TitledBorder(new LineBorder(Color.black)));
		}
		
		// Constructor
		public InfoPanel(Factory factory) {
			this.factory = factory;
			createGUI();
		}
		
		
		// Create GUI
		private void createGUI() {
			
			/*      Resource GUI Layout. You should do this during the midterm to keep track of your layout!!!!!!!!!!
			 * 	[ Top Panel]
			 * 	-> Center Panel
			 * 		-> Available Panel
			 * 		- >Inner Most Panel
			 * 			-> resourcesPanel
			 * 				-> Resource Panel (Multiple)
			 * 			-> productPanel
			 * 				-> Product Panel (Multiple)
			 */
			
			// Top Panel
			JPanel topPanel = new JPanel(); //Make a new JPanel
			topPanel.setBorder(new LineBorder(Color.BLACK)); //Make the border black
			topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); //set it to a box layout
			
			JLabel width = new JLabel("Width: " + factory.getWidth()); //Get the width of the factory
			JLabel height = new JLabel(" Height: " + factory.getHeight()); //get the height of the factory
			JLabel taskBoard = new JLabel("TaskBoard: (" + factory.getTaskBoardLocation().getX() + ", " + factory.getTaskBoardLocation().getY() + ")"); //get the location of the TB

			topPanel.add(Box.createGlue()); //Add some glue to fill the cracks between the buttons
			topPanel.add(width); //add width
			topPanel.add(height); //add height
			
			topPanel.add(Box.createGlue()); //MORE GLUE
			topPanel.add(taskBoard); //add the TB info 
			topPanel.add(Box.createGlue()); //ALL YOUR GLUE ARE BELONG TO US
			
			// Note: this glue is great for spacing your components. Add it and experiment with it. 
			
			// Center Panel
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			
			// Available Panel
			JPanel availablePanel = new JPanel();
			availablePanel.setLayout(new BoxLayout(availablePanel, BoxLayout.X_AXIS));
			JLabel resource = new JLabel("Available Resources");
			JLabel products = new JLabel("Available Products");
			availablePanel.add(resource);
			availablePanel.add(Box.createGlue());
			availablePanel.add(products);
			availablePanel.add(Box.createGlue());
			
			// Inner Most Panel
			JPanel innermost = new JPanel();
			innermost.setLayout(new GridLayout(1, 2));

			// Contains all the resources
			JPanel resourcePanel = new JPanel();
			resourcePanel.setLayout(new GridLayout(factory.getResources().size(), 1));
			
			// Create multiple resource panel
			for (Resource r: factory.getResources()) {
				ResourcePanel p = new ResourcePanel(r);
				p.setBorder(new TitledBorder(new LineBorder(Color.BLACK), r.getName()));
				resourcePanel.add(p);
			}
			
			JScrollPane motherboardPane = new JScrollPane(resourcePanel); //A new scroll bar for the motherboardpane
			motherboardPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

			
			// Contains all the products
			JPanel productsPanel = new JPanel();
			productsPanel.setLayout(new GridLayout(factory.getProducts().size(), 1));
			
			// Create multiple product panel
			for (Product p: factory.getProducts()) {
				ProductPanel panel = new ProductPanel(p);
				panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), p.getName()));
				productsPanel.add(panel);
			}
			
			JScrollPane cheapPane = new JScrollPane(productsPanel);
			cheapPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
			// Add the two scroll panes
			innermost.add(motherboardPane);
			innermost.add(cheapPane);
			
			// Contains two panels
			centerPanel.add(availablePanel, BorderLayout.NORTH);
			centerPanel.add(innermost);
			
			// Adding the top most JPanels
			add(topPanel, BorderLayout.NORTH);
			add(centerPanel, BorderLayout.CENTER);
		}
	}	
}
