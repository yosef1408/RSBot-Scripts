package scripts.RyuksMultiTaskingFisher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class ControllerGUI {

	public ControllerGUI(){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final SettingsGUI sg = new SettingsGUI();
				sg.pack();
				sg.setVisible(true);
				sg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				sg.featherRangeMax.setText("600");
				sg.featherRangeMin.setText("400");
				
				sg.barbarianFishing.addMouseListener(new MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						sg.drop.setSelected(true);
					}
				
				});
				
				sg.startBottingBtn.addActionListener(new ActionListener() {	
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if (sg.cookNDrop.isSelected()) {
							Controller.fullAction = "cook";
						} else {
							Controller.fullAction = "drop";
						}
						
						if (sg.barbarianFishing.isSelected()) {
							Controller.fishingAction = "Use-rod";
							Controller.fullAction = "drop";
						} else {
							Controller.fishingAction = "Lure";
						}
						
						Controller.featherCountAimLower = Integer.parseInt(sg.featherRangeMin.getText());
						Controller.featherCountAimUpper = Integer.parseInt(sg.featherRangeMax.getText());
						
						sg.dispose();
					}
				});
			}
			
			
		});
	}
	
}
