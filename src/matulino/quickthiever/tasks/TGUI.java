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

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

public class TGUI extends ClientAccessor {

	private JFrame frame;
	private JPanel contentPane = new JPanel();
	private QuickThiever main;
	private JComboBox options;
	private String[] NPC = {"Man","Farmer","Warrior","Master Farmer","Guard","Knight of Ardougne","Paladin","Gnome","Hero","Elf"};
	private JTextArea itemsToKeep = new JTextArea();
	private JLabel lbl1,lbl2,lblTip,lblHp;
	private JButton btnStart;
	private JSlider sliderHealth;

	
	public TGUI (ClientContext ctx, QuickThiever main) {
			super(ctx);
			this.main = main;
			initialize();
		}
	
	public void initialize() {
		frame = new JFrame("QuicKThiever");
		frame.setResizable(false);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 483	, 282);

		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.setContentPane(contentPane);
		
		sliderHealth = new JSlider(JSlider.HORIZONTAL,0,100,50);
		sliderHealth.setPaintLabels(true);
		sliderHealth.setSnapToTicks(true);
		sliderHealth.setMajorTickSpacing(10);
		sliderHealth.setPaintTicks(true);
		sliderHealth.setMinorTickSpacing(5);
		sliderHealth.setToolTipText("At what % to eat.");
		
		lblTip = new JLabel("-  use item names, separate with \",\" (Coins,Rannar seed,...)");
		lblHp = new JLabel("Percantage to eat:");
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
					main.taskList.add(new Eat(ctx,main));
			    	main.taskList.add(new DropItems(ctx,main));
			    	main.taskList.add(new Pickpocket(ctx,main));;
					
					frame.dispose();
				}
			});
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(sliderHealth, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStart, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
						.addComponent(itemsToKeep, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
						.addComponent(lbl1, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(options, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lbl2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(100)
							.addComponent(lblHp))
						.addComponent(lblTip, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 301, GroupLayout.PREFERRED_SIZE))
					.addGap(38))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lbl1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(options, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblHp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderHealth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(lbl2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(itemsToKeep, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(lblTip, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnStart)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		frame.setVisible(true);
	}
}
