package m0tionl3ss.SandRunner.gui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.powerbot.script.rt4.ClientContext;

import m0tionl3ss.SandRunner.util.Options;
import javax.swing.JCheckBox;

public class GUI {

	private JFrame frame;
	private ClientContext ctx;
	private JComboBox<Options.Mode> comboBox;
	// private JComboBox comboBox;
	private JCheckBox chckbxUseEscapeTo;
	private JCheckBox chckbxUseCompass;
	private boolean startScript = false;
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 340, 160);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setTitle("M0tionl3ss SandRunner v1.0");
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(l -> { startScript = true;
			frame.dispose();
		});
		btnStart.setBounds(221, 73, 89, 23);
		frame.getContentPane().add(btnStart);

		comboBox = new JComboBox<Options.Mode>(Options.Mode.values());
		// comboBox = new JComboBox<>();
		comboBox.setBounds(74, 14, 97, 20);
		frame.getContentPane().add(comboBox);

		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(8, 16, 54, 17);
		frame.getContentPane().add(lblMode);

		chckbxUseEscapeTo = new JCheckBox("Use escape to close bank");
		chckbxUseEscapeTo.setBounds(8, 42, 205, 25);
		frame.getContentPane().add(chckbxUseEscapeTo);

		chckbxUseCompass = new JCheckBox("Use Compass");
		chckbxUseCompass.setBounds(8, 72, 113, 25);
		frame.getContentPane().add(chckbxUseCompass);
	}

	public Options.Mode getMode() {
		return (Options.Mode) comboBox.getSelectedItem();
	}

	public boolean useEscape() {
		return chckbxUseEscapeTo.isSelected();
	}

	public boolean useCompass() {
		return chckbxUseCompass.isSelected();
	}
	public boolean startScript()
	{
		return startScript;
	}
}
