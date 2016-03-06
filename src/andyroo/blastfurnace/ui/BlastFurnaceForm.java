package andyroo.blastfurnace.ui;

import andyroo.blastfurnace.BarInfo;
import andyroo.blastfurnace.BlastFurnace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlastFurnaceForm extends JFrame {
    private JComboBox barTypeComboBox;
    private JPanel mainPanel;
    private JButton startButton;
    private JLabel barTypeLabel;

    private BarInfo barType;
    public boolean start;


    public BlastFurnaceForm() {
        super("Blast Furnace");
        barType = null;
        createUIComponents();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        start = false;
    }

    public BarInfo getBarType() {
        return barType;
    }

    private void createUIComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        barTypeComboBox = new JComboBox(BlastFurnace.BAR.values());
        c.gridx = 1;
        c.gridy = 0;
        //c.anchor = GridBagConstraints.WEST;
        mainPanel.add(barTypeComboBox, c);
        barTypeComboBox.setSelectedItem(null);

        barTypeLabel = new JLabel();
        barTypeLabel.setText("Bar Type");
        c.gridx = 0;
        c.gridy = 0;
        barTypeLabel.setLabelFor(barTypeComboBox);
        mainPanel.add(barTypeLabel, c);

        startButton = new JButton();
        startButton.setText("Start");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        mainPanel.add(startButton, c);

        barTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                barType = new BarInfo((BlastFurnace.BAR) ((JComboBox) actionEvent.getSource()).getSelectedItem());
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                start = true;
                dispose();
            }
        });
    }
}