package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DBWoodcutterGUI extends JFrame {
	private static final long serialVersionUID = -1833080218300759764L;
	public DBWoodcutterGUI() {
		initComponents();
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		addHatchet = new JCheckBox();
		bankReg = new JCheckBox();
		separator1 = new JSeparator();
		logDropAction = new JComboBox();
		label1 = new JLabel();
		panel2 = new JPanel();
		chopOaks = new JCheckBox();
		label8 = new JLabel();
		oakLevel = new JSpinner();
		label7 = new JLabel();
		oakLocation = new JComboBox();
		bankOaks = new JCheckBox();
		separator3 = new JSeparator();
		oakDropAction = new JComboBox();
		label14 = new JLabel();
		separator4 = new JSeparator();
		panel3 = new JPanel();
		chopWillows = new JCheckBox();
		label2 = new JLabel();
		willowLevel = new JSpinner();
		label3 = new JLabel();
		willowLocation = new JComboBox();
		bankWillows = new JCheckBox();
		separator5 = new JSeparator();
		willowDropAction = new JComboBox();
		label15 = new JLabel();
		separator6 = new JSeparator();
		panel6 = new JPanel();
		bankMaple = new JCheckBox();
		separator9 = new JSeparator();
		mapleDropAction = new JComboBox();
		label17 = new JLabel();
		mapleLocation = new JComboBox();
		label18 = new JLabel();
		separator10 = new JSeparator();
		mapleLevel = new JSpinner();
		label19 = new JLabel();
		chopMaples = new JCheckBox();
		panel4 = new JPanel();
		chopYews = new JCheckBox();
		label4 = new JLabel();
		yewLevel = new JSpinner();
		yewLocation = new JComboBox();
		label5 = new JLabel();
		bankYews = new JCheckBox();
		separator7 = new JSeparator();
		yewDropAction = new JComboBox();
		label16 = new JLabel();
		separator8 = new JSeparator();
		panel5 = new JPanel();
		chopIvy = new JCheckBox();
		label9 = new JLabel();
		ivyLevel = new JSpinner();
		label10 = new JLabel();
		ivyLocation = new JComboBox();
		dropNests = new JCheckBox();
		dropRocks = new JCheckBox();
		panel7 = new JPanel();
		muleTrading = new JCheckBox();
		label6 = new JLabel();
		muleName = new JTextField();
		separator2 = new JSeparator();
		label11 = new JLabel();
		label12 = new JLabel();
		label13 = new JLabel();
		getLevelMessage = new JTextField();
		getNotesMessage = new JTextField();
		tradePlayerMessage = new JTextField();
		buttonBar = new JPanel();
		startButton = new JButton();
		cancelButton = new JButton();

		// ======== this ========
		setTitle("DBWoodcutter - Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{

				// ======== tabbedPane1 ========
				{

					// ======== panel1 ========
					{

						// ---- addHatchet ----
						addHatchet.setText("Add hatchets to tool belt");

						// ---- bankReg ----
						bankReg.setText("Bank Regular Logs");
						bankReg.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent event) {
								logDropAction.setEnabled(!bankReg.isSelected());
								label1.setEnabled(!bankReg.isSelected());
							}

						});

						// ---- separator1 ----
						separator1.setOrientation(SwingConstants.VERTICAL);

						// ---- logDropAction ----
						logDropAction.setModel(new DefaultComboBoxModel(
								new String[] { /*"craft", */"Light", "Drop" }));
						logDropAction.setSelectedIndex(1);

						// ---- label1 ----
						label1.setText("Regular Logs");

						GroupLayout panel1Layout = new GroupLayout(panel1);
						panel1.setLayout(panel1Layout);
						panel1Layout
								.setHorizontalGroup(panel1Layout
										.createParallelGroup()
										.addGroup(
												panel1Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel1Layout
																		.createParallelGroup()
																		.addComponent(
																				addHatchet)
																		.addGroup(
																				panel1Layout
																						.createSequentialGroup()
																						.addComponent(
																								bankReg)
																						.addPreferredGap(
																								LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								separator1,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								logDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								label1)))
														.addContainerGap(213,
																Short.MAX_VALUE)));
						panel1Layout
								.setVerticalGroup(panel1Layout
										.createParallelGroup()
										.addGroup(
												GroupLayout.Alignment.TRAILING,
												panel1Layout
														.createSequentialGroup()
														.addContainerGap()
														.addComponent(
																addHatchet)
														.addGap(18, 18, 18)
														.addGroup(
																panel1Layout
																		.createParallelGroup()
																		.addComponent(
																				separator1)
																		.addComponent(
																				bankReg,
																				GroupLayout.DEFAULT_SIZE,
																				41,
																				Short.MAX_VALUE)
																		.addGroup(
																				panel1Layout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								logDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								40,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								label1,
																								GroupLayout.PREFERRED_SIZE,
																								40,
																								GroupLayout.PREFERRED_SIZE)))
														.addGap(117, 117, 117)));
					}
					tabbedPane1.addTab("General Settings", panel1);

					// ======== panel2 ========
					{

						// ---- chopOaks ----
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
								oakDropAction.setEnabled((enabled && !bankOaks
										.isSelected()));
								label14.setEnabled(enabled
										&& !bankOaks.isSelected());
							}

						});
						chopOaks.setSelected(true);

						// ---- label8 ----
						label8.setText("at level");

						// ---- oakLevel ----
						oakLevel.setModel(new SpinnerNumberModel(15, 15, 99, 1));
						oakLevel.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent event) {
								if ((Integer) oakLevel.getValue() > 29
										&& chopWillows.isSelected()) {
									willowLevel
											.setModel(new SpinnerNumberModel(
													(Integer) oakLevel
															.getValue() + 1,
													(Integer) oakLevel
															.getValue() + 1,
													99, 1));
								} else {
									if ((Integer) oakLevel.getValue() < 29
											&& chopWillows.isSelected()) {
										willowLevel
												.setModel(new SpinnerNumberModel(
														30, 30, 99, 1));
									}
								}
								if ((Integer) willowLevel.getValue() > 44
										&& chopMaples.isSelected()) {
									mapleLevel.setModel(new SpinnerNumberModel(
											(Integer) willowLevel.getValue() + 1,
											(Integer) willowLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) willowLevel.getValue() < 44
											&& chopMaples.isSelected()) {
										mapleLevel
												.setModel(new SpinnerNumberModel(
														45, 45, 99, 1));
									}
								}
								if ((Integer) mapleLevel.getValue() > 59
										&& chopYews.isSelected()) {
									yewLevel.setModel(new SpinnerNumberModel(
											(Integer) mapleLevel.getValue() + 1,
											(Integer) mapleLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) mapleLevel.getValue() < 59
											&& chopYews.isSelected()) {
										yewLevel.setModel(new SpinnerNumberModel(
												60, 60, 99, 1));
									}
								}
								if ((Integer) yewLevel.getValue() > 67
										&& chopIvy.isSelected()) {
									ivyLevel.setModel(new SpinnerNumberModel(
											(Integer) yewLevel.getValue() + 1,
											(Integer) yewLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) yewLevel.getValue() < 67
											&& chopIvy.isSelected()) {
										ivyLevel.setModel(new SpinnerNumberModel(
												68, 68, 99, 1));
									}
								}
							}

						});

						// ---- label7 ----
						label7.setText("From");

						// ---- oakLocation ----
						oakLocation.setModel(new DefaultComboBoxModel(
								new String[] { "Port Sarim", "Varrock (W)" }));

						// ---- bankOaks ----
						bankOaks.setText("Bank Oaks");
						bankOaks.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								oakDropAction.setEnabled(!bankOaks.isSelected());
								label14.setEnabled(!bankOaks.isSelected());
							}

						});
						bankOaks.setSelected(true);

						// ---- separator3 ----
						separator3.setOrientation(SwingConstants.VERTICAL);

						// ---- oakDropAction ----
						oakDropAction.setModel(new DefaultComboBoxModel(
								new String[] { /*"craft", */"Light", "Drop" }));
						oakDropAction.setSelectedIndex(1);
						oakDropAction.setEnabled(false);

						// ---- label14 ----
						label14.setText("Oak Logs");
						label14.setEnabled(false);

						GroupLayout panel2Layout = new GroupLayout(panel2);
						panel2.setLayout(panel2Layout);
						panel2Layout
								.setHorizontalGroup(panel2Layout
										.createParallelGroup()
										.addGroup(
												panel2Layout
														.createSequentialGroup()
														.addGroup(
																panel2Layout
																		.createParallelGroup()
																		.addGroup(
																				panel2Layout
																						.createSequentialGroup()
																						.addGap(126,
																								126,
																								126)
																						.addComponent(
																								bankOaks)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								separator3,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								oakDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								label14))
																		.addGroup(
																				panel2Layout
																						.createSequentialGroup()
																						.addGap(109,
																								109,
																								109)
																						.addGroup(
																								panel2Layout
																										.createParallelGroup(
																												GroupLayout.Alignment.LEADING,
																												false)
																										.addComponent(
																												separator4)
																										.addGroup(
																												panel2Layout
																														.createSequentialGroup()
																														.addComponent(
																																chopOaks)
																														.addPreferredGap(
																																LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(
																																label8)
																														.addPreferredGap(
																																LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(
																																oakLevel,
																																GroupLayout.PREFERRED_SIZE,
																																GroupLayout.DEFAULT_SIZE,
																																GroupLayout.PREFERRED_SIZE)
																														.addPreferredGap(
																																LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(
																																label7)
																														.addPreferredGap(
																																LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(
																																oakLocation,
																																GroupLayout.PREFERRED_SIZE,
																																GroupLayout.DEFAULT_SIZE,
																																GroupLayout.PREFERRED_SIZE)))))
														.addContainerGap(113,
																Short.MAX_VALUE)));
						panel2Layout
								.setVerticalGroup(panel2Layout
										.createParallelGroup()
										.addGroup(
												panel2Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel2Layout
																		.createParallelGroup()
																		.addGroup(
																				GroupLayout.Alignment.TRAILING,
																				panel2Layout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								chopOaks)
																						.addComponent(
																								label8,
																								GroupLayout.PREFERRED_SIZE,
																								28,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								label7,
																								GroupLayout.PREFERRED_SIZE,
																								28,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								oakLocation,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addComponent(
																				oakLevel,
																				GroupLayout.Alignment.TRAILING,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGap(18, 18, 18)
														.addComponent(
																separator4,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(
																panel2Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(
																				separator3,
																				GroupLayout.DEFAULT_SIZE,
																				41,
																				Short.MAX_VALUE)
																		.addComponent(
																				oakDropAction,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label14,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				bankOaks,
																				GroupLayout.DEFAULT_SIZE,
																				41,
																				Short.MAX_VALUE))
														.addContainerGap(75,
																Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Oak", panel2);

					// ======== panel3 ========
					{

						// ---- chopWillows ----
						chopWillows.setText("Chop Willows");
						chopWillows.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent event) {
								boolean enabled = chopWillows.isSelected();
								bankWillows.setEnabled(enabled);
								label1.setEnabled(enabled);
								label3.setEnabled(enabled);
								willowLocation.setEnabled(enabled);
								willowLevel.setEnabled(enabled);
								willowDropAction
										.setEnabled((enabled && !bankWillows
												.isSelected()));
								label15.setEnabled(enabled
										&& !bankWillows.isSelected());
							}

						});
						chopWillows.setSelected(true);

						// ---- label2 ----
						label2.setText("at level");

						// ---- willowLevel ----
						willowLevel.setModel(new SpinnerNumberModel(30, 30, 99,
								1));
						willowLevel.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent event) {
								if ((Integer) willowLevel.getValue() > 44
										&& chopMaples.isSelected()) {
									mapleLevel.setModel(new SpinnerNumberModel(
											(Integer) willowLevel.getValue() + 1,
											(Integer) willowLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) willowLevel.getValue() < 44
											&& chopMaples.isSelected()) {
										mapleLevel
												.setModel(new SpinnerNumberModel(
														45, 45, 99, 1));
									}
								}
								if ((Integer) mapleLevel.getValue() > 59
										&& chopYews.isSelected()) {
									yewLevel.setModel(new SpinnerNumberModel(
											(Integer) mapleLevel.getValue() + 1,
											(Integer) mapleLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) mapleLevel.getValue() < 59
											&& chopYews.isSelected()) {
										yewLevel.setModel(new SpinnerNumberModel(
												60, 60, 99, 1));
									}
								}
								if ((Integer) yewLevel.getValue() > 67) {
									ivyLevel.setModel(new SpinnerNumberModel(
											(Integer) yewLevel.getValue() + 1,
											(Integer) yewLevel.getValue() + 1,
											99, 1));
								} else {
									ivyLevel.setModel(new SpinnerNumberModel(
											68, 68, 99, 1));
								}
							}

						});

						// ---- label3 ----
						label3.setText("From");

						// ---- willowLocation ----
						willowLocation
								.setModel(new DefaultComboBoxModel(
										new String[] { "Draynor Village",
												"Port Sarim" }));

						// ---- bankWillows ----
						bankWillows.setText("Bank Willows");
						bankWillows.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								willowDropAction.setEnabled(!bankWillows
										.isSelected());
								label15.setEnabled(!bankWillows.isSelected());
							}

						});
						bankWillows.setSelected(true);

						// ---- separator5 ----
						separator5.setOrientation(SwingConstants.VERTICAL);

						// ---- willowDropAction ----
						willowDropAction.setModel(new DefaultComboBoxModel(
								new String[] { /*"craft", */"Light", "Drop" }));
						willowDropAction.setSelectedIndex(1);
						willowDropAction.setEnabled(false);

						// ---- label15 ----
						label15.setText("Willow Logs");
						label15.setEnabled(false);

						GroupLayout panel3Layout = new GroupLayout(panel3);
						panel3.setLayout(panel3Layout);
						panel3Layout
								.setHorizontalGroup(panel3Layout
										.createParallelGroup()
										.addGroup(
												panel3Layout
														.createSequentialGroup()
														.addGroup(
																panel3Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																		.addGroup(
																				panel3Layout
																						.createSequentialGroup()
																						.addGap(85,
																								85,
																								85)
																						.addComponent(
																								chopWillows)
																						.addGap(5,
																								5,
																								5)
																						.addComponent(
																								label2)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								willowLevel,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								label3)
																						.addGap(5,
																								5,
																								5)
																						.addComponent(
																								willowLocation,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addGroup(
																				panel3Layout
																						.createSequentialGroup()
																						.addGap(131,
																								131,
																								131)
																						.addComponent(
																								bankWillows)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								separator5,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								willowDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								label15))
																		.addGroup(
																				GroupLayout.Alignment.TRAILING,
																				panel3Layout
																						.createSequentialGroup()
																						.addContainerGap(
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								separator6,
																								GroupLayout.PREFERRED_SIZE,
																								429,
																								GroupLayout.PREFERRED_SIZE)))
														.addContainerGap(89,
																Short.MAX_VALUE)));
						panel3Layout
								.setVerticalGroup(panel3Layout
										.createParallelGroup()
										.addGroup(
												panel3Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel3Layout
																		.createParallelGroup()
																		.addComponent(
																				label2,
																				GroupLayout.PREFERRED_SIZE,
																				28,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				willowLevel,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label3,
																				GroupLayout.PREFERRED_SIZE,
																				28,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				panel3Layout
																						.createSequentialGroup()
																						.addGap(2,
																								2,
																								2)
																						.addGroup(
																								panel3Layout
																										.createParallelGroup()
																										.addComponent(
																												chopWillows)
																										.addComponent(
																												willowLocation,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE))))
														.addGap(18, 18, 18)
														.addComponent(
																separator6,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addGap(16, 16, 16)
														.addGroup(
																panel3Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(
																				separator5)
																		.addComponent(
																				willowDropAction,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label15,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				bankWillows,
																				GroupLayout.PREFERRED_SIZE,
																				41,
																				GroupLayout.PREFERRED_SIZE))
														.addContainerGap(71,
																Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Willow", panel3);

					// ======== panel6 ========
					{

						// ---- bankMaple ----
						bankMaple.setText("Bank Maples");
						bankMaple.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								mapleDropAction.setEnabled(!bankMaple
										.isSelected());
								label17.setEnabled(!bankMaple.isSelected());
							}

						});
						bankMaple.setSelected(true);

						// ---- separator9 ----
						separator9.setOrientation(SwingConstants.VERTICAL);

						// ---- mapleDropAction ----
						mapleDropAction.setModel(new DefaultComboBoxModel(
								new String[] { /*"craft", */"Light", "Drop" }));
						mapleDropAction.setSelectedIndex(1);
						mapleDropAction.setEnabled(false);

						// ---- label17 ----
						label17.setText("Maple Logs");
						label17.setEnabled(false);

						// ---- mapleLocation ----
						mapleLocation.setModel(new DefaultComboBoxModel(
								new String[] { "Seers' Village (Bank)"/*, "Seers' Village (NLS)"*/ }));

						// ---- label18 ----
						label18.setText("From");

						// ---- mapleLevel ----
						mapleLevel.setModel(new SpinnerNumberModel(45, 45, 99,
								1));
						mapleLevel.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent arg0) {
								if ((Integer) mapleLevel.getValue() > 59
										&& chopYews.isSelected()) {
									yewLevel.setModel(new SpinnerNumberModel(
											(Integer) mapleLevel.getValue() + 1,
											(Integer) mapleLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) mapleLevel.getValue() < 59
											&& chopYews.isSelected()) {
										yewLevel.setModel(new SpinnerNumberModel(
												60, 60, 99, 1));
									}
								}
								if ((Integer) yewLevel.getValue() > 67) {
									ivyLevel.setModel(new SpinnerNumberModel(
											(Integer) yewLevel.getValue() + 1,
											(Integer) yewLevel.getValue() + 1,
											99, 1));
								} else {
									ivyLevel.setModel(new SpinnerNumberModel(
											68, 68, 99, 1));
								}
							}

						});

						// ---- label19 ----
						label19.setText("at level");

						// ---- chopMaples ----
						chopMaples.setText("Chop Maple");
						chopMaples.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								boolean enabled = chopMaples.isSelected();
								bankMaple.setEnabled(enabled);
								label19.setEnabled(enabled);
								label18.setEnabled(enabled);
								mapleLocation.setEnabled(enabled);
								mapleLevel.setEnabled(enabled);
								mapleDropAction
										.setEnabled((enabled && !bankWillows
												.isSelected()));
								label17.setEnabled(enabled
										&& !bankWillows.isSelected());
							}

						});
						chopMaples.setSelected(true);

						GroupLayout panel6Layout = new GroupLayout(panel6);
						panel6.setLayout(panel6Layout);
						panel6Layout
								.setHorizontalGroup(panel6Layout
										.createParallelGroup()
										.addGroup(
												panel6Layout
														.createSequentialGroup()
														.addGap(87, 87, 87)
														.addGroup(
																panel6Layout
																		.createParallelGroup()
																		.addGroup(
																				panel6Layout
																						.createSequentialGroup()
																						.addComponent(
																								chopMaples)
																						.addGap(5,
																								5,
																								5)
																						.addComponent(
																								label19)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								mapleLevel,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								label18)
																						.addGap(5,
																								5,
																								5)
																						.addComponent(
																								mapleLocation,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addComponent(
																				separator10,
																				GroupLayout.PREFERRED_SIZE,
																				429,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				panel6Layout
																						.createSequentialGroup()
																						.addGap(46,
																								46,
																								46)
																						.addComponent(
																								bankMaple)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								separator9,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								mapleDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								label17)))
														.addContainerGap(87,
																Short.MAX_VALUE)));
						panel6Layout
								.setVerticalGroup(panel6Layout
										.createParallelGroup()
										.addGroup(
												panel6Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel6Layout
																		.createParallelGroup()
																		.addComponent(
																				label19,
																				GroupLayout.PREFERRED_SIZE,
																				28,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				mapleLevel,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label18,
																				GroupLayout.PREFERRED_SIZE,
																				28,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				panel6Layout
																						.createSequentialGroup()
																						.addGap(2,
																								2,
																								2)
																						.addGroup(
																								panel6Layout
																										.createParallelGroup()
																										.addComponent(
																												chopMaples)
																										.addComponent(
																												mapleLocation,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE))))
														.addGap(18, 18, 18)
														.addComponent(
																separator10,
																GroupLayout.PREFERRED_SIZE,
																20,
																GroupLayout.PREFERRED_SIZE)
														.addGap(16, 16, 16)
														.addGroup(
																panel6Layout
																		.createParallelGroup()
																		.addComponent(
																				bankMaple,
																				GroupLayout.PREFERRED_SIZE,
																				41,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				separator9,
																				GroupLayout.PREFERRED_SIZE,
																				41,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				mapleDropAction,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label17,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE))
														.addContainerGap(71,
																Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Maple", panel6);
					// tabbedPane1.setEnabledAt(3, false);

					// ======== panel4 ========
					{

						// ---- chopYews ----
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
								yewDropAction.setEnabled((enabled && !bankYews
										.isSelected()));
								label16.setEnabled(enabled
										&& !bankYews.isSelected());
							}

						});
						chopYews.setSelected(true);

						// ---- label4 ----
						label4.setText("at level");

						// ---- yewLevel ----
						yewLevel.setModel(new SpinnerNumberModel(60, 60, 99, 1));
						yewLevel.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent event) {
								if ((Integer) yewLevel.getValue() > 67
										&& chopIvy.isSelected()) {
									ivyLevel.setModel(new SpinnerNumberModel(
											(Integer) yewLevel.getValue() + 1,
											(Integer) yewLevel.getValue() + 1,
											99, 1));
								} else {
									if ((Integer) yewLevel.getValue() < 67
											&& chopIvy.isSelected()) {
										if ((Integer) yewLevel.getValue() < 67) {
											ivyLevel.setModel(new SpinnerNumberModel(
													68, 68, 99, 1));
										}
									}
								}
							}

						});

						// ---- yewLocation ----
						yewLocation.setModel(new DefaultComboBoxModel(
								new String[] { "Varrock" }));

						// ---- label5 ----
						label5.setText("From");

						// ---- bankYews ----
						bankYews.setText("Bank Yews");
						bankYews.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								yewDropAction.setEnabled(!bankYews.isSelected());
								label16.setEnabled(!bankYews.isSelected());
							}

						});
						bankYews.setSelected(true);

						// ---- separator7 ----
						separator7.setOrientation(SwingConstants.VERTICAL);

						// ---- yewDropAction ----
						yewDropAction.setModel(new DefaultComboBoxModel(
								new String[] { /*"craft", */"Light", "Drop" }));
						yewDropAction.setSelectedIndex(1);
						yewDropAction.setEnabled(false);

						// ---- label16 ----
						label16.setText("Yew Logs");
						label16.setEnabled(false);

						GroupLayout panel4Layout = new GroupLayout(panel4);
						panel4.setLayout(panel4Layout);
						panel4Layout
								.setHorizontalGroup(panel4Layout
										.createParallelGroup()
										.addGroup(
												panel4Layout
														.createSequentialGroup()
														.addGap(112, 112, 112)
														.addGroup(
																panel4Layout
																		.createParallelGroup()
																		.addGroup(
																				panel4Layout
																						.createSequentialGroup()
																						.addGap(21,
																								21,
																								21)
																						.addComponent(
																								bankYews)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								separator7,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								yewDropAction,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(18,
																								18,
																								18)
																						.addComponent(
																								label16))
																		.addComponent(
																				separator8,
																				GroupLayout.PREFERRED_SIZE,
																				365,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				panel4Layout
																						.createSequentialGroup()
																						.addComponent(
																								chopYews)
																						.addGap(5,
																								5,
																								5)
																						.addComponent(
																								label4)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								yewLevel,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGap(10,
																								10,
																								10)
																						.addComponent(
																								label5)
																						.addPreferredGap(
																								LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								yewLocation,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)))
														.addContainerGap(126,
																Short.MAX_VALUE)));
						panel4Layout
								.setVerticalGroup(panel4Layout
										.createParallelGroup()
										.addGroup(
												panel4Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel4Layout
																		.createParallelGroup()
																		.addComponent(
																				chopYews,
																				GroupLayout.PREFERRED_SIZE,
																				32,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label4,
																				GroupLayout.PREFERRED_SIZE,
																				32,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				yewLevel,
																				GroupLayout.PREFERRED_SIZE,
																				31,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(
																				panel4Layout
																						.createParallelGroup(
																								GroupLayout.Alignment.TRAILING,
																								false)
																						.addComponent(
																								yewLocation,
																								GroupLayout.Alignment.LEADING,
																								GroupLayout.DEFAULT_SIZE,
																								31,
																								Short.MAX_VALUE)
																						.addComponent(
																								label5,
																								GroupLayout.Alignment.LEADING,
																								GroupLayout.DEFAULT_SIZE,
																								31,
																								Short.MAX_VALUE)))
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(
																separator8,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(
																panel4Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.LEADING,
																				false)
																		.addComponent(
																				separator7)
																		.addComponent(
																				yewDropAction,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				label16,
																				GroupLayout.PREFERRED_SIZE,
																				40,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				bankYews,
																				GroupLayout.PREFERRED_SIZE,
																				41,
																				GroupLayout.PREFERRED_SIZE))
														.addContainerGap(84,
																Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Yew", panel4);

					// ======== panel5 ========
					{

						// ---- chopIvy ----
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

						// ---- label9 ----
						label9.setText("at level");

						// ---- ivyLevel ----
						ivyLevel.setModel(new SpinnerNumberModel(68, 68, 99, 1));

						// ---- label10 ----
						label10.setText("From");

						// ---- ivyLocation ----
						ivyLocation
								.setModel(new DefaultComboBoxModel(
										new String[] { "Varrock (N)",
												"Falador (SE)" }));

						// ---- dropNests ----
						dropNests.setText("Drop Nests");
						dropNests.setSelected(true);

						// ---- dropRocks ----
						dropRocks.setText("Drop Strange Rocks");
						dropRocks.setSelected(true);

						GroupLayout panel5Layout = new GroupLayout(panel5);
						panel5.setLayout(panel5Layout);
						panel5Layout
								.setHorizontalGroup(panel5Layout
										.createParallelGroup()
										.addGroup(
												panel5Layout
														.createSequentialGroup()
														.addGap(104, 104, 104)
														.addComponent(dropRocks)
														.addGap(43, 43, 43)
														.addComponent(dropNests)
														.addContainerGap(197,
																Short.MAX_VALUE))
										.addGroup(
												GroupLayout.Alignment.TRAILING,
												panel5Layout
														.createSequentialGroup()
														.addContainerGap(113,
																Short.MAX_VALUE)
														.addComponent(chopIvy)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(label9)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(
																ivyLevel,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(label10)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(
																ivyLocation,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addGap(97, 97, 97)));
						panel5Layout
								.setVerticalGroup(panel5Layout
										.createParallelGroup()
										.addGroup(
												panel5Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel5Layout
																		.createParallelGroup()
																		.addGroup(
																				GroupLayout.Alignment.TRAILING,
																				panel5Layout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								chopIvy,
																								GroupLayout.PREFERRED_SIZE,
																								28,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								label9,
																								GroupLayout.PREFERRED_SIZE,
																								27,
																								GroupLayout.PREFERRED_SIZE))
																		.addGroup(
																				panel5Layout
																						.createParallelGroup(
																								GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								label10,
																								GroupLayout.PREFERRED_SIZE,
																								28,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								ivyLocation,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE))
																		.addComponent(
																				ivyLevel,
																				GroupLayout.Alignment.TRAILING,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED,
																106,
																Short.MAX_VALUE)
														.addGroup(
																panel5Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				dropNests)
																		.addComponent(
																				dropRocks))
														.addGap(37, 37, 37)));
					}
					tabbedPane1.addTab("Ivy", panel5);

					// ======== panel7 ========
					{

						// ---- muleTrading ----
						muleTrading.setText("Listen for Mule Messages");
						muleTrading.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								boolean enabled = muleTrading.isSelected();
								label6.setEnabled(enabled);
								label11.setEnabled(enabled);
								label12.setEnabled(enabled);
								label13.setEnabled(enabled);
								getLevelMessage.setEnabled(enabled);
								getNotesMessage.setEnabled(enabled);
								tradePlayerMessage.setEnabled(enabled);
							}

						});

						// ---- label6 ----
						label6.setText("Mule Name:");
						label6.setEnabled(false);

						// ---- label11 ----
						label11.setText("Get Level Message:");
						label11.setEnabled(false);

						// ---- label12 ----
						label12.setText("Get Notes Message:");
						label12.setEnabled(false);

						// ---- label13 ----
						label13.setText("Trade Player Message:");
						label13.setEnabled(false);

						getLevelMessage
								.setText("What's your current woodcutting level?");
						getLevelMessage.setEnabled(false);

						getNotesMessage.setText("Withdraw your notes");
						getNotesMessage.setEnabled(false);

						tradePlayerMessage.setText("Trade me");
						tradePlayerMessage.setEnabled(false);

						GroupLayout panel7Layout = new GroupLayout(panel7);
						panel7.setLayout(panel7Layout);
						panel7Layout
								.setHorizontalGroup(panel7Layout
										.createParallelGroup()
										.addGroup(
												panel7Layout
														.createSequentialGroup()
														.addGroup(
																panel7Layout
																		.createParallelGroup()
																		.addGroup(
																				panel7Layout
																						.createSequentialGroup()
																						.addContainerGap()
																						.addGroup(
																								panel7Layout
																										.createParallelGroup()
																										.addComponent(
																												separator2,
																												GroupLayout.DEFAULT_SIZE,
																												563,
																												Short.MAX_VALUE)
																										.addGroup(
																												panel7Layout
																														.createSequentialGroup()
																														.addGroup(
																																panel7Layout
																																		.createParallelGroup()
																																		.addComponent(
																																				label13)
																																		.addComponent(
																																				label12))
																														.addGap(18,
																																18,
																																18)
																														.addGroup(
																																panel7Layout
																																		.createParallelGroup()
																																		.addComponent(
																																				getNotesMessage,
																																				GroupLayout.DEFAULT_SIZE,
																																				407,
																																				Short.MAX_VALUE)
																																		.addComponent(
																																				tradePlayerMessage,
																																				GroupLayout.DEFAULT_SIZE,
																																				407,
																																				Short.MAX_VALUE)))))
																		.addGroup(
																				panel7Layout
																						.createSequentialGroup()
																						.addGroup(
																								panel7Layout
																										.createParallelGroup()
																										.addGroup(
																												panel7Layout
																														.createSequentialGroup()
																														.addGap(59,
																																59,
																																59)
																														.addComponent(
																																muleTrading)
																														.addGap(18,
																																18,
																																18)
																														.addComponent(
																																label6)
																														.addGap(10,
																																10,
																																10)
																														.addComponent(
																																muleName,
																																GroupLayout.PREFERRED_SIZE,
																																191,
																																GroupLayout.PREFERRED_SIZE))
																										.addGroup(
																												panel7Layout
																														.createSequentialGroup()
																														.addContainerGap()
																														.addComponent(
																																label11)))
																						.addGap(0,
																								0,
																								Short.MAX_VALUE))
																		.addGroup(
																				GroupLayout.Alignment.TRAILING,
																				panel7Layout
																						.createSequentialGroup()
																						.addGap(176,
																								176,
																								176)
																						.addComponent(
																								getLevelMessage,
																								GroupLayout.DEFAULT_SIZE,
																								407,
																								Short.MAX_VALUE)))
														.addContainerGap()));
						panel7Layout
								.setVerticalGroup(panel7Layout
										.createParallelGroup()
										.addGroup(
												panel7Layout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																panel7Layout
																		.createParallelGroup()
																		.addGroup(
																				panel7Layout
																						.createSequentialGroup()
																						.addGap(2,
																								2,
																								2)
																						.addComponent(
																								muleTrading))
																		.addGroup(
																				panel7Layout
																						.createSequentialGroup()
																						.addGap(6,
																								6,
																								6)
																						.addComponent(
																								label6))
																		.addComponent(
																				muleName,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGap(13, 13, 13)
														.addComponent(
																separator2,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(
																panel7Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				label11)
																		.addComponent(
																				getLevelMessage,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGap(18, 18, 18)
														.addGroup(
																panel7Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				label12)
																		.addComponent(
																				getNotesMessage,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addGap(18, 18, 18)
														.addGroup(
																panel7Layout
																		.createParallelGroup(
																				GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				label13)
																		.addComponent(
																				tradePlayerMessage,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addContainerGap(10,
																Short.MAX_VALUE)));
					}
					tabbedPane1.addTab("Mule Settings", panel7);
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout
						.createParallelGroup().addComponent(tabbedPane1,
								GroupLayout.Alignment.TRAILING));
				contentPanelLayout
						.setVerticalGroup(contentPanelLayout
								.createParallelGroup().addComponent(
										tabbedPane1,
										GroupLayout.Alignment.TRAILING,
										GroupLayout.DEFAULT_SIZE, 261,
										Short.MAX_VALUE));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
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
						DBWoodcutter.muleName = muleName.getText();

						DBWoodcutter.addToToolbelt = addHatchet.isSelected();

						DBWoodcutter.bankRegular = bankReg.isSelected();
						DBWoodcutter.regDropAction = (String) logDropAction
								.getSelectedItem();

						DBWoodcutter.chopOaks = chopOaks.isSelected();
						DBWoodcutter.oakLevel = (Integer) oakLevel.getValue();
						DBWoodcutter.bankOak = bankOaks.isSelected();
						DBWoodcutter.oakDropAction = (String) oakDropAction
								.getSelectedItem();
						DBWoodcutter.oakLocation = (String) oakLocation
								.getSelectedItem();

						DBWoodcutter.chopWillows = chopWillows.isSelected();
						DBWoodcutter.willowLevel = (Integer) willowLevel
								.getValue();
						DBWoodcutter.bankWillow = bankWillows.isSelected();
						DBWoodcutter.willowDropAction = (String) willowDropAction
								.getSelectedItem();
						DBWoodcutter.willowLocation = (String) willowLocation
								.getSelectedItem();

						DBWoodcutter.chopMaples = chopMaples.isSelected();
						DBWoodcutter.mapleLevel = (Integer) mapleLevel
								.getValue();
						DBWoodcutter.bankMaples = bankMaple.isSelected();
						DBWoodcutter.mapleDropAction = (String) mapleDropAction
								.getSelectedItem();
						DBWoodcutter.mapleLocation = (String) mapleLocation
								.getSelectedItem();

						DBWoodcutter.chopYews = chopYews.isSelected();
						DBWoodcutter.yewLevel = (Integer) yewLevel.getValue();
						DBWoodcutter.bankYews = bankYews.isSelected();
						DBWoodcutter.yewDropAction = (String) yewDropAction
								.getSelectedItem();
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
				buttonBar.add(startButton, new GridBagConstraints(1, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
	}

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JTabbedPane tabbedPane1;
	private JPanel panel1;
	private JCheckBox addHatchet;
	private JCheckBox bankReg;
	private JSeparator separator1;
	private JComboBox logDropAction;
	private JLabel label1;
	private JPanel panel2;
	private JCheckBox chopOaks;
	private JLabel label8;
	private JSpinner oakLevel;
	private JLabel label7;
	private JComboBox oakLocation;
	private JCheckBox bankOaks;
	private JSeparator separator3;
	private JComboBox oakDropAction;
	private JLabel label14;
	private JSeparator separator4;
	private JPanel panel3;
	private JCheckBox chopWillows;
	private JLabel label2;
	private JSpinner willowLevel;
	private JLabel label3;
	private JComboBox willowLocation;
	private JCheckBox bankWillows;
	private JSeparator separator5;
	private JComboBox willowDropAction;
	private JLabel label15;
	private JSeparator separator6;
	private JPanel panel6;
	private JCheckBox bankMaple;
	private JSeparator separator9;
	private JComboBox mapleDropAction;
	private JLabel label17;
	private JComboBox mapleLocation;
	private JLabel label18;
	private JSeparator separator10;
	private JSpinner mapleLevel;
	private JLabel label19;
	private JCheckBox chopMaples;
	private JPanel panel4;
	private JCheckBox chopYews;
	private JLabel label4;
	private JSpinner yewLevel;
	private JComboBox yewLocation;
	private JLabel label5;
	private JCheckBox bankYews;
	private JSeparator separator7;
	private JComboBox yewDropAction;
	private JLabel label16;
	private JSeparator separator8;
	private JPanel panel5;
	private JCheckBox chopIvy;
	private JLabel label9;
	private JSpinner ivyLevel;
	private JLabel label10;
	private JComboBox ivyLocation;
	private JCheckBox dropNests;
	private JCheckBox dropRocks;
	private JPanel panel7;
	private JCheckBox muleTrading;
	private JLabel label6;
	private JTextField muleName;
	private JSeparator separator2;
	private JLabel label11;
	private JLabel label12;
	private JLabel label13;
	private JTextField getLevelMessage;
	private JTextField getNotesMessage;
	private JTextField tradePlayerMessage;
	private JPanel buttonBar;
	private JButton startButton;
	private JButton cancelButton;
}
