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
	ButtonGroup radioBtnGroup = new ButtonGroup();
	
	JTextField featherRangeMin = new JTextField(4);
	JTextField featherRangeMax = new JTextField(4);

	public SettingsGUI() {
		super("Ryuk's MultiTasking Fisher Settings");
		super.setLayout(new GridBagLayout());
	
		fullOptions();
		featherCollect();

		c.gridx = 0;
		c.gridy = 4;
		add(startBottingBtn, c);
	}
	
	public void fullOptions(){	
		JLabel fullActionLabel = new JLabel("What would you like to do with the fish?");
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 0;
		add(fullActionLabel, c);
		
		radioBtnGroup.add(cookNDrop);
		radioBtnGroup.add(drop);
		
		JPanel fullOptionPanel = new JPanel();
		fullOptionPanel.add(cookNDrop);
		fullOptionPanel.add(drop);
		
		c.gridx = 0;
		c.gridy = 1;
		add(fullOptionPanel, c);

	}
	
	public void featherCollect(){
		JLabel featherLabel = new JLabel("How many feathers would you like to collect per trip? Default:(400-600)");
		c.insets = new Insets(10,10,10,10);
		c.gridx = 0;
		c.gridy = 2;
		add(featherLabel, c);
		
		JLabel from = new JLabel("-");
		JPanel featherRangePanel = new JPanel();
		
		featherRangePanel.add(featherRangeMin);
		featherRangePanel.add(from);
		featherRangePanel.add(featherRangeMax);
		
		c.gridx = 0;
		c.gridy = 3;
		add(featherRangePanel, c);
	}
	
	
}
