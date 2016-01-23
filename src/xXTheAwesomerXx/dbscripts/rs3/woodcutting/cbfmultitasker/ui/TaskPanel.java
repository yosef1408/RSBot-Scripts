package xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TaskPanel extends JPanel {
	private static final long serialVersionUID = 7425862627959125574L;

	private JSpinner conditionAmount;

	private JComboBox conditionType;

	private JLabel locationLabel;

	private JList locationList;

	private JLabel moreThanLabel;

	private JLabel optionsLabel;

	private JList optionsList;

	private JScrollPane scrollPane6;

	private JScrollPane scrollPane7;

	private JScrollPane scrollPane8;
	private JScrollPane scrollPane9;
	private JCheckBox stopCheck;
	private JLabel treeLabel;
	private JList treeList;
	private JLabel typeLabel;
	private JList typeList;

	public TaskPanel() {
		initComponents();
	}

	public int getConditionAmount() {
		return (Integer) conditionAmount.getValue();
	}

	public String getConditionType() {
		return (String) conditionType.getSelectedItem();
	}

	public String getLocationSelection() {
		return (String) locationList.getSelectedValue();
	}

	public String getOptionSelection() {
		return (String) optionsList.getSelectedValue();
	}

	public String getTreeSelection() {
		return (String) treeList.getSelectedValue();
	}

	public String getTypeSelection() {
		return (String) typeList.getSelectedValue();
	}

	private void initComponents() {
		scrollPane9 = new JScrollPane();
		optionsList = new JList();
		optionsLabel = new JLabel();
		scrollPane8 = new JScrollPane();
		typeList = new JList();
		typeLabel = new JLabel();
		locationLabel = new JLabel();
		scrollPane7 = new JScrollPane();
		locationList = new JList();
		scrollPane6 = new JScrollPane();
		treeList = new JList();
		treeLabel = new JLabel();
		stopCheck = new JCheckBox();
		conditionType = new JComboBox();
		conditionAmount = new JSpinner();
		moreThanLabel = new JLabel();

		// ======== scrollPane9 ========
		{
			// ---- optionsList ----
			optionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane9.setViewportView(optionsList);
		}

		// ---- optionsLabel ----
		optionsLabel.setText("Task Options");
		optionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// ======== scrollPane8 ========
		{

			// ---- typeList ----
			typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			typeList.setModel(new AbstractListModel() {
				private static final long serialVersionUID = -9204733498239431627L;
				String[] values = {"Chop | Bank", "Chop | Drop", "Chop | Burn",
						"Chop | Fletch"};

				@Override
				public Object getElementAt(final int i) {
					return values[i];
				}

				@Override
				public int getSize() {
					return values.length;
				}
			});
			typeList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(final ListSelectionEvent arg0) {
					if (typeList.getSelectedValue() != null) {
						if (typeList.getSelectedValue().toString()
								.contains("Fletch")) {
							final String treeType = (String) treeList
									.getSelectedValue();
							String[] values;
							if (treeType.equalsIgnoreCase("Elder")) {
								values = new String[]{"Shortbow", "Shieldbow",
										"Arrow Shaft"};
							} else {
								values = new String[]{"Shortbow", "Shieldbow",
										"Stock", "Arrow Shaft"};
							}
							final String[] setValues = values;
							optionsList.setModel(new AbstractListModel() {
								private static final long serialVersionUID = 7323341814502688060L;

								@Override
								public Object getElementAt(final int i) {
									return setValues[i];
								}

								@Override
								public int getSize() {
									return setValues.length;
								}
							});
							conditionType.setModel(new DefaultComboBoxModel(
									new String[]{"Woodcutting Lvl",
											"Fletching Lvl", "Gained WC Exp",
											"Gained FL Exp",
											"Collected Amount",
											"Fletched Amount",
											"Task Runtime (Minutes)"}));
						} else if (typeList.getSelectedValue().toString()
								.contains("Burn")) {
							optionsList.setModel(new AbstractListModel() {
								private static final long serialVersionUID = -9132766367366840782L;
								String[] values = {

								};

								@Override
								public Object getElementAt(final int i) {
									return values[i];
								}

								@Override
								public int getSize() {
									return values.length;
								}
							});
							conditionType.setModel(new DefaultComboBoxModel(
									new String[]{"Woodcutting Lvl",
											"Firemaking Lvl", "Gained WC Exp",
											"Gained FM Exp",
											"Collected Amount",
											"Burned Amount",
											"Task Runtime (Minutes)"}));
						} else {
							optionsList.setModel(new AbstractListModel() {
								private static final long serialVersionUID = -9132766367366840782L;
								String[] values = {

								};

								@Override
								public Object getElementAt(final int i) {
									return values[i];
								}

								@Override
								public int getSize() {
									return values.length;
								}
							});
							conditionType.setModel(new DefaultComboBoxModel(
									new String[]{"Woodcutting Lvl",
											"Gained WC Exp",
											"Collected Amount",
											"Task Runtime (Minutes)"}));
						}
					}
				}
			});
			typeList.setSelectedIndex(0);
			scrollPane8.setViewportView(typeList);
		}

		// ---- typeLabel ----
		typeLabel.setText("Task Type");
		typeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// ---- locationLabel ----
		locationLabel.setText("Location");
		locationLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// ======== scrollPane7 ========
		{
			// ---- locationList ----
			locationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			locationList.setModel(new AbstractListModel() {
				private static final long serialVersionUID = -8782912794018595995L;
				String[] values = {"Port Sarim", "Falador", "Catherby",
						"Castle Wars"};

				@Override
				public Object getElementAt(final int i) {
					return values[i];
				}

				@Override
				public int getSize() {
					return values.length;
				}
			});
			locationList.setSelectedIndex(0);
			locationList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(final ListSelectionEvent arg0) {
					final String treeType = (String) treeList
							.getSelectedValue();
					final String location = (String) locationList
							.getSelectedValue();
					if (treeType.equalsIgnoreCase("Normal")
							|| treeType.equalsIgnoreCase("Oak")
							|| treeType.equalsIgnoreCase("Willow")
							|| treeType.equalsIgnoreCase("Elder")) {
						if ((location != null)
								&& location.equalsIgnoreCase("Port Sarim")) {
							typeList.setModel(new AbstractListModel() {
								private static final long serialVersionUID = 8856907723354735022L;
								String[] values = {"Chop | Bank",
										"Chop | Drop", "Chop | Burn",
										"Chop | Fletch"};

								@Override
								public Object getElementAt(final int i) {
									return values[i];
								}

								@Override
								public int getSize() {
									return values.length;
								}
							});
						} else {
							typeList.setModel(new AbstractListModel() {
								private static final long serialVersionUID = 8856907723354735022L;
								String[] values = {"Chop | Bank",
										"Chop | Drop", "Chop | Burn",
										"Chop | Fletch"};

								@Override
								public Object getElementAt(final int i) {
									return values[i];
								}

								@Override
								public int getSize() {
									return values.length;
								}
							});
						}
					}
				}

			});
			scrollPane7.setViewportView(locationList);
		}

		// ======== scrollPane6 ========
		{

			// ---- treeList ----
			treeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			treeList.setModel(new AbstractListModel() {
				private static final long serialVersionUID = 5681940091342407467L;
				String[] values = {"Normal", "Oak", "Willow", "Maple", "Yew",
						"Ivy"};

				@Override
				public Object getElementAt(final int i) {
					return values[i];
				}

				@Override
				public int getSize() {
					return values.length;
				}
			});
			treeList.setSelectedIndex(0);
			treeList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(final ListSelectionEvent arg0) {
					final String treeString = (String) treeList
							.getSelectedValue();
					if (treeString.equalsIgnoreCase("Normal")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 1624501101377377243L;
							String[] values = {"Port Sarim", "Falador",
									"Catherby", "Castle Wars"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.equalsIgnoreCase("Oak")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 1239562048523199865L;
							String[] values = {"Port Sarim", "Falador",
									"Varrock", "Catherby", "Castle Wars"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.equalsIgnoreCase("Willow")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 6020631242705269955L;
							String[] values = {"Draynor", "Port Sarim",
									"Catherby"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.equalsIgnoreCase("Maple")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 6528822906832163970L;
							String[] values = {"Seers' Village",
									"McGrubor's Wood"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Arctic Pine")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = -8097864026074221556L;
							String[] values = {"Neitiznot"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Yew")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 2785617856655315032L;
							String[] values = {"Varrock", "Edgeville",
									"Catherby"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Ivy")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = -8264834920077162153L;
							String[] values = {"Varrock", "Falador",
									"Castle Wars"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Magic")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = -6486074242516333184L;
							String[] values = {"Tree Gnome Stronghold",
									"Sorcerer's Tower", "Sorcerer's Tower (NW)"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Elder")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = -4550893245275053294L;
							String[] values = {"Draynor", "Falador", "Varrock",
									"Edgeville", "Port Sarim",
									"Seers' Village", "Yanille",
									"Eagles' Peek", "Tirannwn", "Prifddinas"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.endsWith("Crystal")) {
						locationList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 549949060474868812L;
							String[] values = {"Falador", "Seers' Village",
									"Yanille", "Prifddinas"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					}
					if (treeString.equalsIgnoreCase("Arctic Pine")) {
						typeList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = -3906371814222699639L;
							String[] values = {"Chop | Bank", "Chop | Drop",
									"Chop | Burn"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else if (treeString.equalsIgnoreCase("Ivy")
							|| treeString.equalsIgnoreCase("Crystal")) {
						typeList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 5512710454802508263L;
							String[] values = {"Chop"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					} else {
						typeList.setModel(new AbstractListModel() {
							private static final long serialVersionUID = 8856907723354735022L;
							String[] values = {"Chop | Bank", "Chop | Drop",
									"Chop | Burn", "Chop | Fletch"};

							@Override
							public Object getElementAt(final int i) {
								return values[i];
							}

							@Override
							public int getSize() {
								return values.length;
							}
						});
					}
					if (treeString.equalsIgnoreCase("Elder")) {
						locationList
								.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
					} else {
						locationList
								.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					}
					if (locationList.getSelectedIndices().length == 0) {
						locationList.setSelectedIndex(0);
					}
					if (typeList.getSelectedIndices().length == 0) {
						typeList.setSelectedIndex(0);
					}
				}

			});
			scrollPane6.setViewportView(treeList);
		}

		// ---- treeLabel ----
		treeLabel.setText("Tree Type");
		treeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// ---- stopCheck ----
		stopCheck.setText("Stop when");
		stopCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				conditionType.setEnabled(stopCheck.isSelected());
				conditionAmount.setEnabled(stopCheck.isSelected());
				moreThanLabel.setEnabled(stopCheck.isSelected());
			}

		});

		// ---- conditionType ----
		conditionType.setModel(new DefaultComboBoxModel(new String[]{
				"Woodcutting Lvl", "Gained WC Exp", "Collected Amount",
				"Task Runtime (Minutes)"}));
		conditionType.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String selectedValue = conditionType.getItemAt(
						conditionType.getSelectedIndex()).toString();
				if (selectedValue.contains("Lvl")) {
					conditionAmount
							.setModel(new SpinnerNumberModel(1, 1, 99, 1));
				} else if (selectedValue.contains("Exp")) {
					conditionAmount.setModel(new SpinnerNumberModel(1000, 1,
							Integer.MAX_VALUE, 1));
					conditionAmount.setSize(120, conditionAmount.HEIGHT);
				} else if (selectedValue.contains("Amount")) {
					conditionAmount.setModel(new SpinnerNumberModel(1000, 1,
							Integer.MAX_VALUE, 1));
					conditionAmount.setSize(120, conditionAmount.HEIGHT);
				} else if (selectedValue.contains("Runtime")) {
					conditionAmount.setModel(new SpinnerNumberModel(1, 1, 1440,
							1));
					conditionAmount.setSize(120, conditionAmount.HEIGHT);
				}
			}

		});
		conditionType.setEnabled(false);

		// ---- conditionAmount ----
		conditionAmount.setModel(new SpinnerNumberModel(1, 1, 99, 1));
		conditionAmount.setEnabled(false);

		// ---- moreThanLabel ----
		moreThanLabel.setText(">=");
		moreThanLabel.setEnabled(false);

		final GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup()
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		stopCheck)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(
																		conditionType,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		moreThanLabel)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		conditionAmount,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addContainerGap(
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup()
																				.addComponent(
																						treeLabel,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						scrollPane6))
																.addGap(18, 18,
																		18)
																.addGroup(
																		layout.createParallelGroup()
																				.addComponent(
																						locationLabel,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						scrollPane7,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE))
																.addGap(18, 18,
																		18)
																.addGroup(
																		layout.createParallelGroup()
																				.addComponent(
																						typeLabel,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						scrollPane8,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE))
																.addGap(18, 18,
																		18)
																.addGroup(
																		layout.createParallelGroup()
																				.addComponent(
																						optionsLabel,
																						GroupLayout.PREFERRED_SIZE,
																						135,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						scrollPane9,
																						GroupLayout.PREFERRED_SIZE,
																						141,
																						GroupLayout.PREFERRED_SIZE))
																.addContainerGap(
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)))));
		layout.setVerticalGroup(layout
				.createParallelGroup()
				.addGroup(
						GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.BASELINE)
																.addComponent(
																		treeLabel)
																.addComponent(
																		locationLabel)
																.addComponent(
																		typeLabel))
												.addComponent(
														optionsLabel,
														GroupLayout.Alignment.TRAILING))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING,
												false)
												.addComponent(
														scrollPane8,
														GroupLayout.Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE,
														230, Short.MAX_VALUE)
												.addComponent(
														scrollPane7,
														GroupLayout.Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE,
														230, Short.MAX_VALUE)
												.addComponent(
														scrollPane6,
														GroupLayout.Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE,
														230, Short.MAX_VALUE)
												.addComponent(
														scrollPane9,
														GroupLayout.DEFAULT_SIZE,
														230, Short.MAX_VALUE))
								.addGap(18, 18, 18)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.BASELINE)
																.addComponent(
																		conditionType)
																.addComponent(
																		moreThanLabel))
												.addComponent(
														stopCheck,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(conditionAmount))
								.addGap(0, 20, Short.MAX_VALUE)));
	}

	public boolean shouldStop() {
		return stopCheck.isSelected();
	}
}
