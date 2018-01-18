package Aff1x.choppenheimer.Util;

import Aff1x.choppenheimer.Choppenheimer;
import Aff1x.choppenheimer.Tasks.Bank;
import Aff1x.choppenheimer.Tasks.Chop;
import Aff1x.choppenheimer.Tasks.Walk;
import org.powerbot.script.rt6.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GUI extends JFrame {

    private static final long serialVersionUID = -7756889463610098394L;

    private ClientContext ctx;

    private JPanel panel;
    public boolean guiDone = false, bank;

    public GUI(ClientContext ctx) {

        this.ctx = ctx;
        setTitle("Affix");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 150);
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);

        final JLabel title = new JLabel("CHOPPENHEIMER");
        title.setFont(new Font("Arial", Font.PLAIN, 14));
        title.setBounds(153, 11, 154, 15);
        panel.add(title);

        final JLabel toTrain = new JLabel("Select a tree type");
        toTrain.setFont(new Font("Arial", Font.PLAIN, 12));
        toTrain.setBounds(187, 37, 76, 14);
        panel.add(toTrain);

        DefaultComboBoxModel trees = new DefaultComboBoxModel();
        for(TreeEnum.TreeType treeType: TreeEnum.TreeType.values()) {
            trees.addElement(treeType);
        }

        final JComboBox treeBox = new JComboBox();
        treeBox.setModel(trees);
        treeBox.setBounds(175, 56, 100, 20);
        panel.add(treeBox);

        final JButton start = new JButton("Start");
        start.setEnabled(false);
        start.setBounds(180, 90, 90, 23);
        panel.add(start);

        start.setEnabled(true);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                System.out.println(treeBox.getSelectedItem().toString());
                String selected = treeBox.getSelectedItem().toString();

                if(selected.equals("REGULAR"))
                    Choppenheimer.config.setTreeType(TreeEnum.TreeType.REGULAR);
                if(selected.equals("MAPLE"))
                    Choppenheimer.config.setTreeType(TreeEnum.TreeType.MAPLE);
                if(selected.equals("WILLOW"))
                    Choppenheimer.config.setTreeType(TreeEnum.TreeType.WILLOW);
                if(selected.equals("MAGIC"))
                    Choppenheimer.config.setTreeType(TreeEnum.TreeType.MAGIC);
                if(selected.equals("OAK"))
                    Choppenheimer.config.setTreeType(TreeEnum.TreeType.OAK);

                addTasks();

                setVisible(false);
                guiDone = true;
            }});
    }

    @SuppressWarnings("unchecked")
    public void addTasks() {
        Choppenheimer.taskList.addAll(Arrays.asList(
                new Bank(ctx),
                new Walk(ctx),
                new Chop(ctx)
        ));
    }

}