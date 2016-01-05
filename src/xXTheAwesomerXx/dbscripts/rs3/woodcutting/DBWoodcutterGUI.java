package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DBWoodcutterGUI extends JFrame {
	private static final long serialVersionUID = 2302682813453596014L;
	public DBWoodcutterGUI() {
		initComponents();
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		panel1 = new JPanel();
		willowLocation = new JComboBox();
		label2 = new JLabel();
		willowLevel = new JSpinner();
		label1 = new JLabel();
		chopWillows = new JCheckBox();
		bankWillows = new JCheckBox();
		panel2 = new JPanel();
		addHatchet = new JCheckBox();
		checkBox1 = new JCheckBox();
		panel3 = new JPanel();
		yewLocation = new JComboBox();
		chopYews = new JCheckBox();
		label3 = new JLabel();
		yewLevel = new JSpinner();
		label4 = new JLabel();
		bankYews = new JCheckBox();
		panel4 = new JPanel();
		muleTrading = new JCheckBox();
		textField1 = new JTextField();
		label5 = new JLabel();
		label6 = new JLabel();
		panel5 = new JPanel();
		oakLocation = new JComboBox();
		label7 = new JLabel();
		oakLevel = new JSpinner();
		label8 = new JLabel();
		chopOaks = new JCheckBox();
		bankOaks = new JCheckBox();
		panel6 = new JPanel();
		chopIvy = new JCheckBox();
		label9 = new JLabel();
		ivyLevel = new JSpinner();
		label10 = new JLabel();
		ivyLocation = new JComboBox();
		dropNests = new JCheckBox();
		dropRocks = new JCheckBox();
		buttonBar = new JPanel();
		startButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle("DBWoodcutter - Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{

				//======== panel1 ========
				{
					panel1.setBorder(new TitledBorder("Willows"));

					//---- willowLocation ----
					willowLocation.setModel(new DefaultComboBoxModel(new String[] {
						"Draynor Village",
						"Port Sarim"
					}));
					willowLocation.setSelectedIndex(1);

					//---- label2 ----
					label2.setText("From");

					//---- willowLevel ----
					willowLevel.setModel(new SpinnerNumberModel(30, 30, 99, 1));
					willowLevel.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent event) {
							if ((Integer) willowLevel.getValue() > 59) {
								yewLevel.setModel(new SpinnerNumberModel(
										(Integer) willowLevel.getValue() + 1,
										(Integer) willowLevel.getValue() + 1,
										99, 1));
							} else {
								yewLevel.setModel(new SpinnerNumberModel(60,
										60, 99, 1));
							}
							if ((Integer) yewLevel.getValue() > 67) {
								ivyLevel.setModel(new SpinnerNumberModel((Integer) yewLevel.getValue() + 1, (Integer) yewLevel.getValue() + 1, 99, 1));
							} else {
								ivyLevel.setModel(new SpinnerNumberModel(68, 68, 99, 1));
							}
						}

					});

					//---- label1 ----
					label1.setText("at level");

					//---- chopWillows ----
					chopWillows.setText("Chop Willows");
					chopWillows.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							boolean enabled = chopWillows.isSelected();
							bankWillows.setEnabled(enabled);
							label1.setEnabled(enabled);
							label2.setEnabled(enabled);
							willowLocation.setEnabled(enabled);
							willowLevel.setEnabled(enabled);
						}

					});
					chopWillows.setSelected(true);

					//---- bankWillows ----
					bankWillows.setText("Bank Willows");
					bankWillows.setSelected(true);

					GroupLayout panel1Layout = new GroupLayout(panel1);
					panel1.setLayout(panel1Layout);
					panel1Layout.setHorizontalGroup(
						panel1Layout.createParallelGroup()
							.addGroup(panel1Layout.createSequentialGroup()
								.addComponent(chopWillows)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(willowLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label2)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(willowLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bankWillows)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
					panel1Layout.setVerticalGroup(
						panel1Layout.createParallelGroup()
							.addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addGroup(panel1Layout.createParallelGroup()
									.addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(chopWillows)
										.addComponent(label1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(label2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(willowLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(bankWillows))
									.addComponent(willowLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					);
				}

				//======== panel2 ========
				{
					panel2.setBorder(new TitledBorder("General Settings"));

					//---- addHatchet ----
					addHatchet.setText("Add best hatchet to Toolbelt*");

					//---- checkBox1 ----
					checkBox1.setText("Bank Regular Logs");

					GroupLayout panel2Layout = new GroupLayout(panel2);
					panel2.setLayout(panel2Layout);
					panel2Layout.setHorizontalGroup(
						panel2Layout.createParallelGroup()
							.addGroup(panel2Layout.createSequentialGroup()
								.addComponent(addHatchet)
								.addGap(18, 18, 18)
								.addComponent(checkBox1)
								.addGap(0, 177, Short.MAX_VALUE))
					);
					panel2Layout.setVerticalGroup(
						panel2Layout.createParallelGroup()
							.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(addHatchet)
									.addComponent(checkBox1)))
					);
				}

				//======== panel3 ========
				{
					panel3.setBorder(new TitledBorder("Yews"));

					//---- yewLocation ----
					yewLocation.setModel(new DefaultComboBoxModel(new String[] {
						"Varrock"
					}));

					//---- chopYews ----
					chopYews.setText("Chop Yews");
					chopYews.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							boolean enabled = chopYews.isSelected();
							bankYews.setEnabled(enabled);
							label3.setEnabled(enabled);
							label4.setEnabled(enabled);
							yewLocation.setEnabled(enabled);
							yewLevel.setEnabled(enabled);
						}

					});
					chopYews.setSelected(true);

					//---- label3 ----
					label3.setText("at level");

					//---- yewLevel ----
					yewLevel.setModel(new SpinnerNumberModel(60, 60, 99, 1));
					yewLevel.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent event) {
							if ((Integer) yewLevel.getValue() > 67) {
								ivyLevel.setModel(new SpinnerNumberModel(
										(Integer) yewLevel.getValue() + 1,
										(Integer) yewLevel.getValue() + 1, 99,
										1));
							} else {
								ivyLevel.setModel(new SpinnerNumberModel(68,
										68, 99, 1));
							}
						}
						
					});

					//---- label4 ----
					label4.setText("From");

					//---- bankYews ----
					bankYews.setText("Bank Yews");
					bankYews.setSelected(true);

					GroupLayout panel3Layout = new GroupLayout(panel3);
					panel3.setLayout(panel3Layout);
					panel3Layout.setHorizontalGroup(
						panel3Layout.createParallelGroup()
							.addGroup(panel3Layout.createSequentialGroup()
								.addComponent(chopYews)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label3)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(yewLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label4)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(yewLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bankYews)
								.addGap(0, 96, Short.MAX_VALUE))
					);
					panel3Layout.setVerticalGroup(
						panel3Layout.createParallelGroup()
							.addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addGroup(panel3Layout.createParallelGroup()
									.addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(chopYews, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
										.addComponent(label3, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
									.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addGroup(GroupLayout.Alignment.LEADING, panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(yewLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(bankYews))
										.addComponent(yewLevel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
										.addComponent(label4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					);
				}

				//======== panel4 ========
				{
					panel4.setBorder(new TitledBorder("Mule Settings"));

					//---- muleTrading ----
					muleTrading.setText("Listen for Mule Messages");

					//---- label5 ----
					label5.setText("Mule Name:");

					GroupLayout panel4Layout = new GroupLayout(panel4);
					panel4.setLayout(panel4Layout);
					panel4Layout.setHorizontalGroup(
						panel4Layout.createParallelGroup()
							.addGroup(panel4Layout.createSequentialGroup()
								.addComponent(muleTrading)
								.addGap(18, 18, 18)
								.addComponent(label5)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 76, Short.MAX_VALUE))
					);
					panel4Layout.setVerticalGroup(
						panel4Layout.createParallelGroup()
							.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(muleTrading)
								.addComponent(label5)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					);
				}

				//---- label6 ----
				label6.setText("*Items added to the toolbelt can NEVER be regained! Use at your own risk");
				label6.setForeground(UIManager.getColor("InternalFrame.borderShadow"));

				//======== panel5 ========
				{
					panel5.setBorder(new TitledBorder("Oaks"));

					//---- oakLocation ----
					oakLocation.setModel(new DefaultComboBoxModel(new String[] {
						"Port Sarim"
					}));

					//---- label7 ----
					label7.setText("From");

					//---- oakLevel ----
					oakLevel.setModel(new SpinnerNumberModel(15, 15, 99, 1));
					oakLevel.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(ChangeEvent event) {
							if ((Integer) oakLevel.getValue() > 29) {
								willowLevel.setModel(new SpinnerNumberModel(
										(Integer) oakLevel.getValue() + 1,
										(Integer) oakLevel.getValue() + 1, 99,
										1));
							} else {
								willowLevel.setModel(new SpinnerNumberModel(30,
										30, 99, 1));
							}
							if ((Integer) willowLevel.getValue() > 59) {
								yewLevel.setModel(new SpinnerNumberModel(
										(Integer) willowLevel.getValue() + 1,
										(Integer) willowLevel.getValue() + 1,
										99, 1));
							} else {
								yewLevel.setModel(new SpinnerNumberModel(60,
										60, 99, 1));
							}
							if ((Integer) yewLevel.getValue() > 67) {
								ivyLevel.setModel(new SpinnerNumberModel(
										(Integer) yewLevel.getValue() + 1,
										(Integer) yewLevel.getValue() + 1,
										99, 1));
							} else {
								ivyLevel.setModel(new SpinnerNumberModel(68,
										68, 99, 1));
							}
						}

					});

					//---- label8 ----
					label8.setText("at level");

					//---- chopOaks ----
					chopOaks.setText("Chop Oaks");
					chopOaks.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							boolean enabled = chopOaks.isSelected();
							bankOaks.setEnabled(enabled);
							label7.setEnabled(enabled);
							label8.setEnabled(enabled);
							oakLocation.setEnabled(enabled);
							oakLevel.setEnabled(enabled);
						}

					});
					chopOaks.setSelected(true);

					//---- bankOaks ----
					bankOaks.setText("Bank Oaks");

					GroupLayout panel5Layout = new GroupLayout(panel5);
					panel5.setLayout(panel5Layout);
					panel5Layout.setHorizontalGroup(
						panel5Layout.createParallelGroup()
							.addGroup(panel5Layout.createSequentialGroup()
								.addComponent(chopOaks)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label8)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(oakLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label7)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(oakLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(bankOaks)
								.addGap(0, 80, Short.MAX_VALUE))
					);
					panel5Layout.setVerticalGroup(
						panel5Layout.createParallelGroup()
							.addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addGroup(panel5Layout.createParallelGroup()
									.addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(chopOaks)
										.addComponent(label8, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(label7, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
										.addComponent(oakLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(bankOaks))
									.addComponent(oakLevel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					);
				}

				//======== panel6 ========
				{
					panel6.setBorder(new TitledBorder("Choking Ivy"));

					//---- chopIvy ----
					chopIvy.setText("Chop Ivy");
					chopIvy.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent event) {
							boolean enabled = chopIvy.isSelected();
							dropNests.setEnabled(enabled);
							dropRocks.setEnabled(enabled);
							label9.setEnabled(enabled);
							label10.setEnabled(enabled);
							ivyLocation.setEnabled(enabled);
							ivyLevel.setEnabled(enabled);
						}
						
					});
					chopIvy.setSelected(true);

					//---- label9 ----
					label9.setText("at level");

					//---- ivyLevel ----
					ivyLevel.setModel(new SpinnerNumberModel(68, 68, 99, 1));

					//---- label10 ----
					label10.setText("From");

					//---- ivyLocation ----
					ivyLocation.setModel(new DefaultComboBoxModel(new String[] {
						"Varrock (N)", "Falador (SE)"
					}));

					//---- dropNests ----
					dropNests.setText("Drop Nests");
					dropNests.setSelected(true);

					//---- dropRocks ----
					dropRocks.setText("Drop Rocks");
					dropRocks.setSelected(true);

					GroupLayout panel6Layout = new GroupLayout(panel6);
					panel6.setLayout(panel6Layout);
					panel6Layout.setHorizontalGroup(
						panel6Layout.createParallelGroup()
							.addGroup(panel6Layout.createSequentialGroup()
								.addComponent(chopIvy)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label9)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(ivyLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label10)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(ivyLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addGroup(panel6Layout.createParallelGroup()
									.addComponent(dropNests)
									.addComponent(dropRocks))
								.addGap(0, 64, Short.MAX_VALUE))
					);
					panel6Layout.setVerticalGroup(
						panel6Layout.createParallelGroup()
							.addGroup(panel6Layout.createSequentialGroup()
								.addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
									.addGroup(GroupLayout.Alignment.LEADING, panel6Layout.createSequentialGroup()
										.addComponent(dropRocks)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(dropNests))
									.addGroup(panel6Layout.createParallelGroup()
										.addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(label9, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
											.addComponent(chopIvy))
										.addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
											.addComponent(ivyLevel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(label10)
												.addComponent(ivyLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
								.addGap(0, 2, Short.MAX_VALUE))
					);
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(
					contentPanelLayout.createParallelGroup()
						.addComponent(panel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addGroup(contentPanelLayout.createParallelGroup()
								.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
									.addComponent(panel2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(panel5, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(panel1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(panel3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(0, 0, Short.MAX_VALUE))
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label6)
							.addContainerGap(88, Short.MAX_VALUE))
				);
				contentPanelLayout.setVerticalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
							.addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(label6)
							.addContainerGap(5, Short.MAX_VALUE))
				);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

				//---- startButton ----
				startButton.setText("Start");
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] {
						0, 85, 80 };
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {
						1.0, 0.0, 0.0 };

				// ---- startButton ----
				startButton.setText("Start");
				startButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						DBWoodcutter.allowTrade = muleTrading.isSelected();
						DBWoodcutter.muleName = textField1.getText();

						DBWoodcutter.addToToolbelt = addHatchet.isSelected();

						DBWoodcutter.bankRegular = checkBox1.isSelected();

						DBWoodcutter.chopOaks = chopOaks.isSelected();
						DBWoodcutter.oakLevel = (Integer) oakLevel.getValue();
						DBWoodcutter.bankOak = bankOaks.isSelected();
						DBWoodcutter.willowLocation = (String) willowLocation
								.getSelectedItem();

						DBWoodcutter.chopWillows = chopWillows.isSelected();
						DBWoodcutter.willowLevel = (Integer) willowLevel
								.getValue();
						DBWoodcutter.bankWillow = bankWillows.isSelected();
						DBWoodcutter.willowLocation = (String) willowLocation
								.getSelectedItem();

						DBWoodcutter.chopYews = chopYews.isSelected();
						DBWoodcutter.yewLevel = (Integer) yewLevel.getValue();
						DBWoodcutter.bankYews = bankYews.isSelected();
						DBWoodcutter.yewLocation = (String) yewLocation
								.getSelectedItem();
						
						DBWoodcutter.chopIvy = chopIvy.isSelected();
						DBWoodcutter.ivyLevel = (Integer) ivyLevel.getValue();
						DBWoodcutter.dropNests = dropNests.isSelected();
						DBWoodcutter.dropRocks = dropRocks.isSelected();
						DBWoodcutter.ivyLocation = (String) ivyLocation
								.getSelectedItem();

						DBWoodcutter.CHOP_TIME = System.currentTimeMillis();
						DBWoodcutter.START_TIME = System.currentTimeMillis();
						DBWoodcutter.TASK_TIME = System.currentTimeMillis();
						setVisible(false);
					}

				});
				buttonBar.add(startButton, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						
					}
					
				});
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
	}

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JPanel panel1;
	private JComboBox willowLocation;
	private JLabel label2;
	private JSpinner willowLevel;
	private JLabel label1;
	private JCheckBox chopWillows;
	private JCheckBox bankWillows;
	private JPanel panel2;
	private JCheckBox addHatchet;
	private JCheckBox checkBox1;
	private JPanel panel3;
	private JComboBox yewLocation;
	private JCheckBox chopYews;
	private JLabel label3;
	private JSpinner yewLevel;
	private JLabel label4;
	private JCheckBox bankYews;
	private JPanel panel4;
	private JCheckBox muleTrading;
	private JTextField textField1;
	private JLabel label5;
	private JLabel label6;
	private JPanel panel5;
	private JComboBox oakLocation;
	private JLabel label7;
	private JSpinner oakLevel;
	private JLabel label8;
	private JCheckBox chopOaks;
	private JCheckBox bankOaks;
	private JPanel panel6;
	private JCheckBox chopIvy;
	private JLabel label9;
	private JSpinner ivyLevel;
	private JLabel label10;
	private JComboBox ivyLocation;
	private JCheckBox dropNests;
	private JCheckBox dropRocks;
	private JPanel buttonBar;
	private JButton startButton;
	private JButton cancelButton;
}
