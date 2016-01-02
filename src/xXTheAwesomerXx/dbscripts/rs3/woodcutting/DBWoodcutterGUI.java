package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class DBWoodcutterGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8943272136926285858L;
	private JPanel contentPane;
	private JTextField textField;
	private JCheckBox chckbxBankWillow, chckbxBankYew, chckbxDraynorWillows,
			chckbxVarrockYews, chckbxAllowTradesEnabled;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DBWoodcutterGUI frame = new DBWoodcutterGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DBWoodcutterGUI() {
		DBWoodcutter.guiShowing = true;
		setTitle("DBWoodcutter - Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		chckbxDraynorWillows = new JCheckBox("Draynor Willows");
		chckbxDraynorWillows.setBounds(182, 73, 134, 23);
		chckbxDraynorWillows.setSelected(true);
		contentPane.add(chckbxDraynorWillows);
		
		chckbxVarrockYews = new JCheckBox("Varrock Yews");
		chckbxVarrockYews.setEnabled(false);
		chckbxVarrockYews.setSelected(true);
		chckbxVarrockYews.setBounds(182, 108, 128, 23);
		contentPane.add(chckbxVarrockYews);
		
		chckbxAllowTradesEnabled = new JCheckBox("Allow Mule Trading");
		chckbxAllowTradesEnabled.setBounds(182, 143, 166, 23);
		contentPane.add(chckbxAllowTradesEnabled);
		
		textField = new JTextField();
		textField.setBounds(272, 177, 134, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblMuleName = new JLabel("Mule Name:");
		lblMuleName.setBounds(192, 183, 88, 16);
		contentPane.add(lblMuleName);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				DBWoodcutter.muleName = textField.getText();
				DBWoodcutter.bankWillow = chckbxBankWillow.isSelected();
				DBWoodcutter.bankYews = chckbxBankYew.isSelected();
				DBWoodcutter.varrockYews = chckbxVarrockYews.isSelected();
				DBWoodcutter.draynorWillows = chckbxDraynorWillows.isSelected();
				DBWoodcutter.allowTrade = chckbxAllowTradesEnabled.isSelected();
				DBWoodcutter.scriptSetup = true;
				dispose();
			}
			
		});
		btnStart.setBounds(160, 243, 117, 29);
		contentPane.add(btnStart);
		
		chckbxBankWillow = new JCheckBox("Bank Willow");
		chckbxBankWillow.setSelected(true);
		chckbxBankWillow.setBounds(42, 73, 128, 23);
		contentPane.add(chckbxBankWillow);
		
		chckbxBankYew = new JCheckBox("Bank Yew");
		chckbxBankYew.setSelected(true);
		chckbxBankYew.setBounds(42, 108, 128, 23);
		contentPane.add(chckbxBankYew);
	}
}
