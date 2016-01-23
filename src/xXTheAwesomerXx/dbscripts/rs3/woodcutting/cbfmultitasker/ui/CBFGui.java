package xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.CBFMultitasker;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks.ChopBankTask;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks.ChopBurnTask;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks.ChopDropTask;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks.ChopFletchTask;

public class CBFGui extends JFrame {
	private static final long serialVersionUID = 5003827993551111948L;

	private JCheckBox addHatchets;

	private JButton addTaskButton;

	private JPanel buttonBar;
	private JButton cancelButton;
	private JPanel contentPanel;
	private JPanel dialogPane;
	private final JFrame frame = this;
	private JPanel generalPanel;
	private JTextField getNotesMessage;
	private JTextField initiateMuleMessage;
	private JLabel label10;
	private JLabel label11;
	private JLabel label12;
	private JLabel label13;
	private JTextField muleName;
	private JCheckBox muleTrading;
	private JPanel panel4;
	private JButton removeTaskButton;
	private JButton startButton;
	private JTabbedPane tabbedPane1;
	private JTextField tradePlayerMessage;
	private JCheckBox useLodestone;

	public CBFGui() {
		initComponents();
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		tabbedPane1 = new JTabbedPane();
		generalPanel = new JPanel();
		useLodestone = new JCheckBox();
		addHatchets = new JCheckBox();
		panel4 = new JPanel();
		muleTrading = new JCheckBox();
		label10 = new JLabel();
		muleName = new JTextField();
		tradePlayerMessage = new JTextField();
		getNotesMessage = new JTextField();
		initiateMuleMessage = new JTextField();
		label13 = new JLabel();
		label12 = new JLabel();
		label11 = new JLabel();
		buttonBar = new JPanel();
		startButton = new JButton();
		addTaskButton = new JButton();
		removeTaskButton = new JButton();
		cancelButton = new JButton();

		// ======== this ========
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{

				// ======== tabbedPane1 ========
				{

					// ======== generalPanel ========
					{

						// ---- useLodestone ----
						useLodestone.setText("Use Lodestone Network");
						useLodestone.setSelected(true);

						// ---- addHatchets ----
						addHatchets.setText("Add hatchets to Tool belt");

						// ======== panel4 ========
						{
							panel4.setBorder(new TitledBorder("Mule Settings"));

							// ---- muleTrading ----
							muleTrading.setText("Listen for Mule Messages");

							// ---- label10 ----
							label10.setText("Mule Name:");

							// ---- label13 ----
							label13.setText("Initiate Trade Task Message:");

							// ---- label12 ----
							label12.setText("Get Notes Message:");

							// ---- label11 ----
							label11.setText("Trade Player Message:");

							final GroupLayout panel4Layout = new GroupLayout(
									panel4);
							panel4.setLayout(panel4Layout);
							panel4Layout
							.setHorizontalGroup(panel4Layout
									.createParallelGroup()
									.addGroup(
											panel4Layout
											.createParallelGroup()
											.addGroup(
													panel4Layout
													.createSequentialGroup()
													.addGap(0,
															0,
															Short.MAX_VALUE)
															.addGroup(
																	panel4Layout
																	.createParallelGroup()
																	.addGroup(
																			panel4Layout
																			.createSequentialGroup()
																			.addGap(39,
																					39,
																					39)
																					.addComponent(
																							muleTrading)
																							.addGap(18,
																									18,
																									18)
																									.addComponent(
																											label10)
																											.addGap(10,
																													10,
																													10)
																													.addComponent(
																															muleName,
																															GroupLayout.PREFERRED_SIZE,
																															191,
																															GroupLayout.PREFERRED_SIZE))
																															.addGroup(
																																	panel4Layout
																																	.createSequentialGroup()
																																	.addComponent(
																																			label11)
																																			.addGap(37,
																																					37,
																																					37)
																																					.addComponent(
																																							tradePlayerMessage,
																																							GroupLayout.PREFERRED_SIZE,
																																							407,
																																							GroupLayout.PREFERRED_SIZE))
																																							.addGroup(
																																									panel4Layout
																																									.createSequentialGroup()
																																									.addComponent(
																																											label12)
																																											.addGap(32,
																																													32,
																																													32)
																																													.addComponent(
																																															getNotesMessage,
																																															GroupLayout.PREFERRED_SIZE,
																																															407,
																																															GroupLayout.PREFERRED_SIZE))
																																															.addGroup(
																																																	panel4Layout
																																																	.createSequentialGroup()
																																																	.addComponent(
																																																			label13)
																																																			.addGap(18,
																																																					18,
																																																					18)
																																																					.addComponent(
																																																							initiateMuleMessage,
																																																							GroupLayout.PREFERRED_SIZE,
																																																							407,
																																																							GroupLayout.PREFERRED_SIZE)))
																																																							.addGap(0,
																																																									0,
																																																									Short.MAX_VALUE)))
																																																									.addGap(0, 0, Short.MAX_VALUE));
							panel4Layout
							.setVerticalGroup(panel4Layout
									.createParallelGroup()
									.addGroup(
											panel4Layout
											.createParallelGroup()
											.addGroup(
													panel4Layout
													.createSequentialGroup()
													.addGap(0,
															0,
															Short.MAX_VALUE)
															.addGroup(
																	panel4Layout
																	.createParallelGroup()
																	.addGroup(
																			panel4Layout
																			.createSequentialGroup()
																			.addGap(2,
																					2,
																					2)
																					.addComponent(
																							muleTrading))
																							.addGroup(
																									panel4Layout
																									.createSequentialGroup()
																									.addGap(6,
																											6,
																											6)
																											.addComponent(
																													label10))
																													.addComponent(
																															muleName,
																															GroupLayout.PREFERRED_SIZE,
																															GroupLayout.DEFAULT_SIZE,
																															GroupLayout.PREFERRED_SIZE))
																															.addGap(37,
																																	37,
																																	37)
																																	.addGroup(
																																			panel4Layout
																																			.createParallelGroup()
																																			.addGroup(
																																					panel4Layout
																																					.createSequentialGroup()
																																					.addGap(6,
																																							6,
																																							6)
																																							.addComponent(
																																									label11))
																																									.addComponent(
																																											tradePlayerMessage,
																																											GroupLayout.PREFERRED_SIZE,
																																											GroupLayout.DEFAULT_SIZE,
																																											GroupLayout.PREFERRED_SIZE))
																																											.addGap(18,
																																													18,
																																													18)
																																													.addGroup(
																																															panel4Layout
																																															.createParallelGroup()
																																															.addGroup(
																																																	panel4Layout
																																																	.createSequentialGroup()
																																																	.addGap(6,
																																																			6,
																																																			6)
																																																			.addComponent(
																																																					label12))
																																																					.addComponent(
																																																							getNotesMessage,
																																																							GroupLayout.PREFERRED_SIZE,
																																																							GroupLayout.DEFAULT_SIZE,
																																																							GroupLayout.PREFERRED_SIZE))
																																																							.addGap(18,
																																																									18,
																																																									18)
																																																									.addGroup(
																																																											panel4Layout
																																																											.createParallelGroup()
																																																											.addGroup(
																																																													panel4Layout
																																																													.createSequentialGroup()
																																																													.addGap(6,
																																																															6,
																																																															6)
																																																															.addComponent(
																																																																	label13))
																																																																	.addComponent(
																																																																			initiateMuleMessage,
																																																																			GroupLayout.PREFERRED_SIZE,
																																																																			GroupLayout.DEFAULT_SIZE,
																																																																			GroupLayout.PREFERRED_SIZE))
																																																																			.addGap(0,
																																																																					0,
																																																																					Short.MAX_VALUE)))
																																																																					.addGap(0, 0, Short.MAX_VALUE));
						}

						final GroupLayout generalPanelLayout = new GroupLayout(
								generalPanel);
						generalPanel.setLayout(generalPanelLayout);
						generalPanelLayout
						.setHorizontalGroup(generalPanelLayout
								.createParallelGroup()
								.addGroup(
										generalPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												generalPanelLayout
												.createParallelGroup()
												.addComponent(
														panel4,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addGroup(
																generalPanelLayout
																.createSequentialGroup()
																.addGroup(
																		generalPanelLayout
																		.createParallelGroup()
																		.addComponent(
																				useLodestone)
																				.addComponent(
																						addHatchets))
																						.addGap(0,
																								411,
																								Short.MAX_VALUE)))
																								.addContainerGap()));
						generalPanelLayout
						.setVerticalGroup(generalPanelLayout
								.createParallelGroup()
								.addGroup(
										generalPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												useLodestone)
												.addPreferredGap(
														LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(
																addHatchets)
																.addGap(18, 18, 18)
																.addComponent(
																		panel4,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																		.addContainerGap()));
					}
					tabbedPane1.addTab("General Settings", generalPanel);
				}

				initiateMuleMessage.setText("Hey what's up dude?!");
				getNotesMessage.setText("How many logs do you have now?");
				tradePlayerMessage.setText("Okay trade me");
				final GroupLayout contentPanelLayout = new GroupLayout(
						contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(contentPanelLayout
						.createParallelGroup().addComponent(tabbedPane1));
				contentPanelLayout.setVerticalGroup(contentPanelLayout
						.createParallelGroup().addComponent(tabbedPane1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{
						0, 0, 85, 80};
				((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{
						1.0, 0.0, 0.0, 0.0};

				// ---- startButton ----
				startButton.setText("Start");
				startButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent arg0) {
						CBFMultitasker.addToToolbelt = addHatchets.isSelected();
						CBFMultitasker.useLodestone = useLodestone.isSelected();
						CBFMultitasker.allowMuleTrading = muleTrading
								.isSelected();
						if (muleTrading.isSelected()) {
							CBFMultitasker.muleName = muleName.getText();
							CBFMultitasker.initiateMuleMessage = initiateMuleMessage
									.getText();
							CBFMultitasker.getNotesMessage = getNotesMessage
									.getText();
							CBFMultitasker.tradePlayerMessage = tradePlayerMessage
									.getText();
						}
						final TaskPanel[] tasks = new TaskPanel[tabbedPane1
						                                        .getTabCount() - 1];
						boolean clearList = false;
						for (int i = 0; i < tasks.length; i++) {
							tasks[i] = (TaskPanel) tabbedPane1
									.getComponentAt(i + 1);
							final String typeSelectionString = tasks[i]
									.getTypeSelection();
							if ((i < tasks.length) && (i != (tasks.length - 1))) {
								if (!tasks[i].shouldStop()) {
									JOptionPane
									.showMessageDialog(
											frame,
											"Script failed to add task "
															+ (i + 2)
													+ " because a prepending task will not stop.\n                             Script will now exit",
													"Error",
													JOptionPane.OK_OPTION);
									clearList = true;
								} else {
									if (typeSelectionString
											.equalsIgnoreCase("Chop | Drop")) {
										CBFMultitasker.taskList.add(new ChopDropTask(
												CBFMultitasker.script(),
												i + 1,
												tasks[i].getTreeSelection(),
												tasks[i].getLocationSelection(),
												tasks[i].getConditionType(),
												tasks[i].getConditionAmount(),
												tasks[i].shouldStop()));
									} else if (typeSelectionString
											.equalsIgnoreCase("Chop | Burn")) {
										CBFMultitasker.taskList.add(new ChopBurnTask(
												CBFMultitasker.script(),
												i + 1,
												tasks[i].getTreeSelection(),
												tasks[i].getLocationSelection(),
												tasks[i].getConditionType(),
												tasks[i].getConditionAmount(),
												tasks[i].shouldStop()));
									} else if (typeSelectionString
											.equalsIgnoreCase("Chop | Bank")
											|| typeSelectionString
											.equalsIgnoreCase("Chop")) {
										CBFMultitasker.taskList.add(new ChopBankTask(
												CBFMultitasker.script(),
												i + 1,
												tasks[i].getTreeSelection(),
												tasks[i].getLocationSelection(),
												tasks[i].getConditionType(),
												tasks[i].getConditionAmount(),
												tasks[i].shouldStop()));
									} else if (typeSelectionString
											.equalsIgnoreCase("Chop | Fletch")) {
										CBFMultitasker.taskList.add(new ChopFletchTask(
												CBFMultitasker.script(),
												i + 1,
												tasks[i].getTreeSelection(),
												tasks[i].getLocationSelection(),
												tasks[i].getOptionSelection(),
												tasks[i].getConditionType(),
												tasks[i].getConditionAmount(),
												tasks[i].shouldStop()));
									}
								}
							} else {
								if (typeSelectionString
										.equalsIgnoreCase("Chop | Drop")) {
									CBFMultitasker.taskList.add(new ChopDropTask(
											CBFMultitasker.script(), i + 1,
											tasks[i].getTreeSelection(),
											tasks[i].getLocationSelection(),
											tasks[i].getConditionType(),
											tasks[i].getConditionAmount(),
											tasks[i].shouldStop()));
								} else if (typeSelectionString
										.equalsIgnoreCase("Chop | Burn")) {
									CBFMultitasker.taskList.add(new ChopBurnTask(
											CBFMultitasker.script(), i + 1,
											tasks[i].getTreeSelection(),
											tasks[i].getLocationSelection(),
											tasks[i].getConditionType(),
											tasks[i].getConditionAmount(),
											tasks[i].shouldStop()));
								} else if (typeSelectionString
										.equalsIgnoreCase("Chop | Bank")
										|| typeSelectionString
										.equalsIgnoreCase("Chop")) {
									CBFMultitasker.taskList.add(new ChopBankTask(
											CBFMultitasker.script(), i + 1,
											tasks[i].getTreeSelection(),
											tasks[i].getLocationSelection(),
											tasks[i].getConditionType(),
											tasks[i].getConditionAmount(),
											tasks[i].shouldStop()));
								} else if (typeSelectionString
										.equalsIgnoreCase("Chop | Fletch")) {
									CBFMultitasker.taskList.add(new ChopFletchTask(
											CBFMultitasker.script(), i + 1,
											tasks[i].getTreeSelection(),
											tasks[i].getLocationSelection(),
											tasks[i].getOptionSelection(),
											tasks[i].getConditionType(),
											tasks[i].getConditionAmount(),
											tasks[i].shouldStop()));
								}
								setVisible(false);
							}
						}
						if (clearList) {
							CBFMultitasker.taskList.clear();
						}
						CBFMultitasker.taskListSize = tasks.length;
						CBFMultitasker.scriptStarted = true;
					}

				});
				buttonBar.add(startButton, new GridBagConstraints(0, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- addTaskButton ----
				addTaskButton.setText("+");
				addTaskButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent arg0) {
						final JPanel newTaskPanel = new TaskPanel();
						tabbedPane1.add("Task " + tabbedPane1.getTabCount(),
								newTaskPanel);
					}

				});
				buttonBar.add(addTaskButton, new GridBagConstraints(1, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- removeTaskButton ----
				removeTaskButton.setText("-");
				removeTaskButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent arg0) {
						final int selectedIndex = tabbedPane1
								.getSelectedIndex();
						if (selectedIndex != 0) {
							tabbedPane1.remove(selectedIndex);
						}
						for (int i = 0; i < tabbedPane1.getTabCount(); i++) {
							if (i != 0) {
								tabbedPane1.setTitleAt(i, "Task " + i);
							}
						}
					}

				});
				buttonBar.add(removeTaskButton, new GridBagConstraints(2, 0, 1,
						1, 0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, new GridBagConstraints(3, 0, 1, 1,
						0.0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(690, 465);
		setLocationRelativeTo(null);
	}
}
