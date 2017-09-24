package sscripts.sgaltar.gui;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.tasks.PortalChat;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.*;
import sscripts.sgaltar.tasks.walk.WalkEnergy;
import sscripts.sgaltar.tasks.walk.WalkToPortal;
import sscripts.sgaltar.tasks.walk.Walking;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

public class Gui extends ClientAccessor {

    private List<Task> tasks;
    private JFrame frame = new JFrame("SGAltar - by sscripts, enjoy :)");
    public Gui(ClientContext arg0, List<Task> tasks) {
        super(arg0);
        this.tasks = tasks;
        init();
    }



    public void init() {
        frame.setSize(500,200);
        frame.setVisible(true);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());

        final JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new FlowLayout());

        final JLabel label = new JLabel("Enter Bone ID");
        label.add(centerPanel);

        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);



        final JFormattedTextField boneid = new JFormattedTextField(formatter);
        centerPanel.add(boneid);

        final JLabel label2 = new JLabel("Enter Host-Name (Playername)");
        label.add(centerPanel);


        final JTextField playerid = new JTextField();
        centerPanel.add(playerid);

        boneid.setColumns(6);
        playerid.setColumns(15);

        final JCheckBox fountain = new JCheckBox("Refresh Run-Energy?");
        lowPanel.add(fountain);
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (boneid.getValue() != null){
                    SGAltar.boneID = (Integer) boneid.getValue();
                }
                if (playerid.getText() != null){
                    SGAltar.playername = playerid.getText();
                } if (fountain.isSelected()){
                synchronized (tasks) {
                    SGAltar.useFountain = true;
                    tasks.add(new Bank(ctx));
                    tasks.add(new Walking(ctx));
                    tasks.add(new EnterPortal(ctx));
                    tasks.add(new Altar(ctx));
                    tasks.add(new Banking(ctx));
                    tasks.add(new CloseBank(ctx));
                    tasks.add(new WalkToPortal(ctx));
                    tasks.add(new PortalChat(ctx));
                    tasks.add(new LeaveHouse(ctx));
                    tasks.add(new WalkEnergy(ctx));
                    tasks.notifyAll();
                }
                }else {
                    synchronized (tasks) {
                        tasks.add(new Bank(ctx));
                        tasks.add(new Walking(ctx));
                        tasks.add(new EnterPortal(ctx));
                        tasks.add(new Altar(ctx));
                        tasks.add(new Banking(ctx));
                        tasks.add(new CloseBank(ctx));
                        tasks.add(new WalkToPortal(ctx));
                        tasks.add(new PortalChat(ctx));
                        tasks.add(new LeaveHouse(ctx));
                        tasks.notifyAll();
                        // add task without fountain usage
                    }
                }

                frame.dispose();
            }

        });
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(label2, BorderLayout.SOUTH);
        lowPanel.add(startButton, BorderLayout.SOUTH);
        mainPanel.add(lowPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.pack();
    }
}
