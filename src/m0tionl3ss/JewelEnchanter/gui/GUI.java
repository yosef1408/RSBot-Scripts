package m0tionl3ss.JewelEnchanter.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.powerbot.script.rt4.ClientContext;

import m0tionl3ss.JewelEnchanter.util.Info;
import m0tionl3ss.JewelEnchanter.util.Info.EnchantSpell;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JComboBox<Info.EnchantSpell> comboBox;
	private ClientContext ctx;
	private JCheckBox chckbxCloseBankUsing;
	public GUI(ClientContext ctx) {
		initialize();
		this.ctx = ctx;
		ctx.controller.suspend();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 406, 137);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		
		JLabel lblItemIdTo = new JLabel("Item id to withdraw");
		lblItemIdTo.setBounds(10, 11, 93, 25);
		frame.getContentPane().add(lblItemIdTo);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(291, 62, 89, 25);
		btnStart.addActionListener(l -> {ctx.controller.resume(); frame.dispose();});
		frame.getContentPane().add(btnStart);
		
		textField = new JTextField();
		textField.setBounds(10, 35, 93, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblEnchantSpellTo = new JLabel("Enchant spell to use");
		lblEnchantSpellTo.setBounds(143, 16, 96, 14);
		frame.getContentPane().add(lblEnchantSpellTo);
		
		comboBox = new JComboBox<EnchantSpell>(Info.EnchantSpell.values());
		//comboBox = new JComboBox<>();
		comboBox.setBounds(143, 36, 96, 20);
		frame.getContentPane().add(comboBox);
		
		chckbxCloseBankUsing = new JCheckBox("Close bank using escape");
		chckbxCloseBankUsing.setBounds(10, 63, 153, 23);
		frame.getContentPane().add(chckbxCloseBankUsing);
	}
	public int getId()
	{
		return Integer.parseInt(textField.getText());
	}
	public Info.EnchantSpell getSpell()
	{
		return (EnchantSpell)comboBox.getSelectedItem();
	}
	public boolean closeBankUsingEscape()
	{
		return chckbxCloseBankUsing.isSelected();
	}
}
