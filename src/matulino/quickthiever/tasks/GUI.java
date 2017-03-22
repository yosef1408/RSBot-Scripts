package matulino.quickthiever.tasks;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Choice;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeListener;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;

public class GUI extends ClientAccessor {

	private JFrame frame;
	private JPanel pickPane = new JPanel();
	private JPanel stallPane = new JPanel();
	private JTabbedPane	tabbedPane = new JTabbedPane();
	private QuickThiever main;
	private JComboBox options;
	private String[] NPC = {"Man","Farmer","Warrior","Master Farmer","Guard","Knight of Ardougne","Paladin","Gnome","Hero","Elf"};
	private JTextArea itemsToKeep = new JTextArea();
	private JLabel lbl1,lbl2,lblTip,lblHp;
	private JButton btnStart;
	private JSlider sliderHealth;
	private JButton btnAddTile;
	private JLabel lblAddSafeSpot;
	private JLabel lvlsafeSpotTile;
	private JLabel lblBankOpts;
	private JButton btnGeneratePath;
	
	private boolean generatePath = false;
	private PathGenerator path;
	private Thread t;
	private JSeparator separator;
	private JTextField foodId;
	private JLabel lblStallOptions;
	private JCheckBox natureChest;

	public GUI (ClientContext ctx, QuickThiever main) {
			super(ctx);
			this.main = main;
			initialize();
		}
	
	public void initialize() {
		frame = new JFrame("QuicKThiever, v1.0");
		frame.setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 483	, 373);

		pickPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.setContentPane(tabbedPane);
		
		sliderHealth = new JSlider(JSlider.HORIZONTAL,0,100,50);
		sliderHealth.setPaintLabels(true);
		sliderHealth.setSnapToTicks(true);
		sliderHealth.setMajorTickSpacing(10);
		sliderHealth.setPaintTicks(true);
		sliderHealth.setMinorTickSpacing(5);
		sliderHealth.setToolTipText("At what % to eat.");
		
		lblTip = new JLabel("-  use item names, separate with \",\" (Coins,Rannar seed,...)");
		lblHp = new JLabel("Percentage to eat:");
		lbl1 = new JLabel("Select NPC to pickpocket:");
		lbl2 = new JLabel("Item to keep if inventory full:");
		itemsToKeep.setToolTipText("Coins,Rannar seed,Snapdragon seed");

		options = new JComboBox(NPC);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					main.percentToEat = sliderHealth.getValue();
					main.thievTarg = (String) options.getSelectedItem();
					main.keptItems = Arrays.asList(itemsToKeep.getText().split(","));
					try { main.foodId = Integer.parseInt(foodId.getText()); }
					catch (Exception ex) {}
					addTasks();
					
					frame.dispose();
				}
			});
		
		natureChest = new JCheckBox("Ardy nature chest");
		natureChest.setFont(new Font("Trajan Pro", Font.PLAIN, 13));
		
		
		GroupLayout gl_contentPane = new GroupLayout(pickPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(natureChest)
							.addContainerGap())
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lbl1)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(options, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(125)
											.addComponent(lblHp))
										.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
									.addContainerGap(124, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(itemsToKeep, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(lbl2, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
											.addGap(191))
										.addComponent(lblTip, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE))
									.addGap(38)))
							.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addComponent(sliderHealth, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lbl1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(options, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblHp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderHealth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(natureChest)
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addComponent(lbl2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(itemsToKeep, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(lblTip, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnStart)
					.addGap(35))
		);
		pickPane.setLayout(gl_contentPane);
		tabbedPane.add("Pickpocket", pickPane);
		tabbedPane.add("Options", stallPane);
		
		btnAddTile = new JButton("Add safespot");
		btnAddTile.setEnabled(false);
		btnAddTile.setBounds(15, 70, 97, 29);
		btnAddTile.setToolTipText("Makes your current tile safespot tile.");
		btnAddTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tile tile = ctx.players.local().tile();
				main.safeSpot = tile;
				lvlsafeSpotTile.setText(tile.toString());
			}
		});
		
		lblAddSafeSpot = new JLabel("Safespot tile:");
		lblAddSafeSpot.setBounds(22, 34, 64, 25);
		
		lvlsafeSpotTile = new JLabel("");
		lvlsafeSpotTile.setBounds(96, 35, 114, 24);
		lvlsafeSpotTile.setForeground(Color.BLUE);
		
		lblBankOpts = new JLabel("Banking options");
		lblBankOpts.setBounds(265, 0, 99, 27);
		lblBankOpts.setFont(new Font("Yu Gothic", Font.PLAIN, 13));
		
		btnGeneratePath = new JButton("Start generating path");
		btnGeneratePath.setBounds(227, 208, 137, 23);
		btnGeneratePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (generatePath) {
					generatePath = false;
					path.pathDone = true;
					btnGeneratePath.setText("Path generated");
					btnGeneratePath.setEnabled(false);
					main.bankPath = path.getPath();
					main.isBanking = true;

				}
				else {
					generatePath = true;
					btnGeneratePath.setText("Stop generating path...");
					
			        path = new PathGenerator(ctx, main);
			        ctx.input.blocking(false);
			        
			        EventQueue.invokeLater(new Runnable() {
			            @Override
			            public void run() {
			            	 t = new Thread(path);
						     t.start();
			               
			            }
			        }); 
			        
				}
				
			}
		});
		
		JLabel lblNewLabel = new JLabel("<html>Start generating at bank, walk to your thieving location and then click again to finish generating a path. Doors ,fences and other obstacles are NOT supported.<html>");
		lblNewLabel.setBounds(206, 242, 256, 63);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setFont(new Font("Cambria Math", Font.ITALIC, 11));
		stallPane.setLayout(null);
		stallPane.add(lblAddSafeSpot);
		stallPane.add(lvlsafeSpotTile);
		stallPane.add(btnAddTile);
		stallPane.add(btnGeneratePath);
		stallPane.add(lblNewLabel);
		stallPane.add(lblBankOpts);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(219, 0, 1, 242);
		stallPane.add(separator);
		
		JLabel lblFoodId = new JLabel("Food id:");
		lblFoodId.setBounds(227, 39, 45, 20);
		stallPane.add(lblFoodId);
		
		foodId = new JTextField();
		foodId.setBounds(270, 38, 57, 20);
		stallPane.add(foodId);
		foodId.setColumns(10);
		
		lblStallOptions = new JLabel("Stall options");
		lblStallOptions.setFont(new Font("Yu Gothic", Font.PLAIN, 13));
		lblStallOptions.setBounds(10, 6, 120, 24);
		stallPane.add(lblStallOptions);
		frame.setVisible(true);
	}
	
	private void addTasks() {
		if (natureChest.isSelected()) {
			main.taskList.add(new NatureChest(ctx,main));
		}
		else if (main.isBanking) {
			main.taskList.addAll(Arrays.asList(new WalkToBank(ctx,main), new WalkToLoc(ctx,main),
					new OpenBank(ctx,main), new Withdraw(ctx,main), new Pickpocket(ctx,main), 
					new DropItems(ctx,main), new Eat(ctx,main)));
		}
		else {
			main.taskList.addAll(Arrays.asList(new Pickpocket(ctx,main), 
					new DropItems(ctx,main), new Eat(ctx,main)));
		}
	}
}
