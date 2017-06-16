package ryukis215;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ControllerGUI {

	public ControllerGUI(){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final SettingsGUI sg = new SettingsGUI();
				sg.pack();
				sg.setVisible(true);
				sg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				
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
