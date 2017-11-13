package m0tionl3ss.JewelEnchanter.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import m0tionl3ss.JewelEnchanter.util.Info.EnchantSpells;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import m0tionl3ss.JewelEnchanter.util.Info.Mode;
import m0tionl3ss.JewelEnchanter.util.BoltSpell;

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JComboBox jewelComboBox;
	private JComboBox modeComboBox;
	private boolean startScript = false;
	private JCheckBox chckbxCloseBankUsing;
	private JComboBox boltComboBox;
	private JCheckBox chckbxSimulateAfk;
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("M0tionl3ss Enchanter v2.0");
		frame.setBounds(100, 100, 396, 224);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		
		JLabel lblItemIdTo = new JLabel("Item id to withdraw");
		lblItemIdTo.setBounds(8, 47, 121, 25);
		frame.getContentPane().add(lblItemIdTo);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(277, 143, 89, 25);
		btnStart.addActionListener(l -> {this.startScript = true;frame.dispose();});
		frame.getContentPane().add(btnStart);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(141, 49, 114, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblEnchantSpellTo = new JLabel("Enchant spell to use");
		lblEnchantSpellTo.setBounds(8, 74, 130, 31);
		frame.getContentPane().add(lblEnchantSpellTo);

		jewelComboBox = new JComboBox<>();
		jewelComboBox.setModel(new DefaultComboBoxModel(EnchantSpells.values()));
		jewelComboBox.setBounds(141, 82, 114, 23);
		jewelComboBox.setVisible(false);
		frame.getContentPane().add(jewelComboBox);
		
		chckbxCloseBankUsing = new JCheckBox("Close bank using escape");
		chckbxCloseBankUsing.setBounds(8, 115, 180, 23);
		frame.getContentPane().add(chckbxCloseBankUsing);
		
		modeComboBox = new JComboBox();
		modeComboBox.setModel(new DefaultComboBoxModel(Mode.values()));
		modeComboBox.addActionListener(l -> {
			
			switch((Mode)modeComboBox.getSelectedItem())
			{
			case BOLT: textField.setEnabled(false);
						if (jewelComboBox.isVisible())
						{
							jewelComboBox.setVisible(false);
						}
						chckbxCloseBankUsing.setEnabled(false);
						boltComboBox.setVisible(true);
				break;	
			case JEWEL: textField.setEnabled(true);
						if(boltComboBox.isVisible())
						{
							boltComboBox.setVisible(false);
						}
						jewelComboBox.setVisible(true);
						chckbxCloseBankUsing.setEnabled(true);
			}
		});
		modeComboBox.setBounds(54, 14, 89, 22);
		frame.getContentPane().add(modeComboBox);
		
		JLabel lblMode = new JLabel("Mode");
		lblMode.setBounds(12, 13, 63, 25);
		frame.getContentPane().add(lblMode);
		
		boltComboBox = new JComboBox();
		boltComboBox.setVisible(false);
		boltComboBox.setModel(new DefaultComboBoxModel(BoltSpell.values()));
		boltComboBox.setBounds(141, 82, 114, 20);
		frame.getContentPane().add(boltComboBox);
		
		chckbxSimulateAfk = new JCheckBox("Simulate AFK");
		chckbxSimulateAfk.setBounds(8, 143, 113, 25);
		frame.getContentPane().add(chckbxSimulateAfk);
		
	}
	public int getId()
	{
		if (textField.getText().isEmpty())
			return 0;
		return Integer.parseInt(textField.getText());
	}
	public EnchantSpells getSpell()
	{
		return (EnchantSpells)jewelComboBox.getSelectedItem();
	}
	public boolean closeBankUsingEscape()
	{
		return chckbxCloseBankUsing.isSelected();
	}
	public boolean startScript()
	{
		return startScript;
	}
	public Mode getMode()
	{
		return (Mode)modeComboBox.getSelectedItem();
	}
	public BoltSpell getBoltSpell()
	{
		return (BoltSpell) boltComboBox.getSelectedItem();
	}
	public boolean useAntiban()
	{
		return chckbxSimulateAfk.isSelected();
	}
}
