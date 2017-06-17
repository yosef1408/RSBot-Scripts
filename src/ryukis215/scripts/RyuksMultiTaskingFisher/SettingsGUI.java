package ryukis215;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SettingsGUI extends JFrame{
	
	JButton startBottingBtn = new JButton("Apply");
	GridBagConstraints c = new GridBagConstraints();
	
	JRadioButton cookNDrop = new JRadioButton("Cook, then drop");
	JRadioButton drop = new JRadioButton("Just drop");
	ButtonGroup fullInvenGroup = new ButtonGroup();
	
	JRadioButton barbarianFishing = new JRadioButton("Barbarian Fishing");
	JRadioButton regularLure = new JRadioButton("Regular Fishing");
	ButtonGroup fishingTypeGroup = new ButtonGroup();
	
	JTextField featherRangeMin = new JTextField(4);
	JTextField featherRangeMax = new JTextField(4);

	public SettingsGUI() {
		super("Ryuk's MultiTasking Fisher Settings");
		super.setLayout(new GridBagLayout());
		
		whatLocation();
		fullOptions();
		featherCollect();

		c.gridx = 0;
		c.gridy = 6;
		add(startBottingBtn, c);
	}
	
	public void whatLocation(){
		JLabel locationLabel = new JLabel("What type of fishing are we doing today?");
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 0;
		add(locationLabel, c);
		
		fishingTypeGroup.add(barbarianFishing);
		fishingTypeGroup.add(regularLure);
		
		JPanel fullOptionPanel = new JPanel();
		fullOptionPanel.add(barbarianFishing);
		fullOptionPanel.add(regularLure);
		
		c.gridx = 0;
		c.gridy = 1;
		add(fullOptionPanel, c);
	}
	
	public void fullOptions(){	
		JLabel fullActionLabel = new JLabel("What would you like to do with the fish?");
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 2;
		add(fullActionLabel, c);
		
		fullInvenGroup.add(cookNDrop);
		fullInvenGroup.add(drop);
		
		JPanel fullOptionPanel = new JPanel();
		fullOptionPanel.add(cookNDrop);
		fullOptionPanel.add(drop);
		
		c.gridx = 0;
		c.gridy = 3;
		add(fullOptionPanel, c);

	}
	
	public void featherCollect(){
		JLabel featherLabel = new JLabel("How many feathers would you like to collect per trip? Recommended:(400-600)");
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 4;
		add(featherLabel, c);
		
		JLabel from = new JLabel("-");
		JPanel featherRangePanel = new JPanel();
		
		featherRangePanel.add(featherRangeMin);
		featherRangePanel.add(from);
		featherRangePanel.add(featherRangeMax);
		
		c.gridx = 0;
		c.gridy = 5;
		add(featherRangePanel, c);
	}
	
	
}
