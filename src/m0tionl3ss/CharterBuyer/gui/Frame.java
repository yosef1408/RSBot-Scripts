package m0tionl3ss.CharterBuyer.gui;

import javax.swing.JFrame;

import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.powerbot.script.rt4.ClientContext;

import javax.swing.JLabel;

public class Frame {

	private JFrame frame;
	private JTextField textField;
	private JCheckBox chckbxUseMouseWheel;
	private JCheckBox chckbxUseEscapeTo;
	private JCheckBox chckbxShuffleWorlds;
	private ClientContext ctx;

	/**
	 * Create the application.
	 */
	public Frame(ClientContext ctx) {
		ctx.controller.suspend();
		initialize();
		this.ctx = ctx;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("M0tionl3ss CharterBuyer V1.3");
		frame.setBounds(100, 100, 450, 187);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(l -> {this.frame.dispose(); ctx.controller.resume();});
		btnStart.setBounds(323, 97, 97, 25);
		frame.getContentPane().add(btnStart);
		
		chckbxUseMouseWheel = new JCheckBox("Use mouse wheel to scroll");
		chckbxUseMouseWheel.setBounds(8, 67, 171, 25);
		frame.getContentPane().add(chckbxUseMouseWheel);
		
		chckbxUseEscapeTo = new JCheckBox("Use escape to close interface");
		chckbxUseEscapeTo.setBounds(8, 97, 213, 25);
		frame.getContentPane().add(chckbxUseEscapeTo);
		
		textField = new JTextField();
		textField.setToolTipText("use ' , ' to seperate ids");
		textField.setBounds(12, 36, 408, 22);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblItemIds = new JLabel("Item id(s)");
		lblItemIds.setBounds(12, 13, 72, 16);
		frame.getContentPane().add(lblItemIds);
		
		chckbxShuffleWorlds = new JCheckBox("Shuffle worlds");
		chckbxShuffleWorlds.setBounds(181, 68, 119, 23);
		frame.getContentPane().add(chckbxShuffleWorlds);
	}
	public boolean useMouseWheel()
	{
		return chckbxUseMouseWheel.isSelected();
	}
	public boolean useEscape()
	{
		return chckbxUseEscapeTo.isSelected();
	}
	public boolean shuffleWorlds()
	{
		return chckbxShuffleWorlds.isSelected();
	}
	public int[] getIds()
	{
		return Arrays.asList(textField.getText().split(",")).stream().mapToInt(str -> Integer.parseInt(str)).toArray();
	}
}
