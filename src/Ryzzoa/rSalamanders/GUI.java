package Ryzzoa.rSalamanders;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUI extends JFrame {
    private int runeCompId = -1;
    String[] runeStrings = new String[]{"Mind Rune", "Air Rune", "Water Rune", "Earth Rune", "Fire Rune", "Body Rune", "Cosmic Rune", "Chaos Rune", "Astral Rune", "Law Rune", "Death Rune", "Blood Rune", "Nature Rune", "Soul Rune"};
    private JComboBox combobox;

    public static void main(String[] args) {
        (new GUI()).setVisible(true);
    }

    public GUI() {
        super("Sally Hunter");
        combobox = new JComboBox(runeStrings);
        this.setSize(400, 400);
        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        this.setDefaultCloseOperation(2);
        this.setBounds(500, 400, 300, 80);
        JLabel label = new JLabel("Rune to use for banking: ");
        label.setHorizontalAlignment(2);
        this.add(label);
        this.add(this.combobox);
        JButton button = new JButton("Start!");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GUI.this.jButton1ActionPerformed(evt);
            }
        });
        button.setHorizontalAlignment(0);
        this.add(button);
    }

    public void jButton1ActionPerformed(ActionEvent evt) {
        if(combobox.getSelectedItem() == "Mind Rune") {
            runeCompId = 29;
        } else if(combobox.getSelectedItem() == "Air Rune") {
            runeCompId = 28;
        } else if(combobox.getSelectedItem() == "Water Rune") {
            runeCompId = 30;
        } else if(combobox.getSelectedItem() == "Earth Rune") {
            runeCompId = 31;
        } else if(combobox.getSelectedItem() == "Fire Rune") {
            runeCompId = 32;
        } else if(combobox.getSelectedItem() == "Body Rune") {
            runeCompId = 33;
        } else if(combobox.getSelectedItem() == "Cosmic Rune") {
            runeCompId = 34;
        } else if(combobox.getSelectedItem() == "Chaos Rune") {
            runeCompId = 35;
        } else if(combobox.getSelectedItem() == "Astral Rune") {
            runeCompId = 36;
        } else if(combobox.getSelectedItem() == "Law Rune") {
            runeCompId = 37;
        } else if(combobox.getSelectedItem() == "Death Rune") {
            runeCompId = 38;
        } else if(combobox.getSelectedItem() == "Blood Rune") {
            runeCompId = 39;
        } else if(combobox.getSelectedItem() == "Nature Rune") {
            runeCompId = 40;
        } else if(combobox.getSelectedItem() == "Soul Rune") {
            runeCompId = 41;
        }

        this.setVisible(false);
    }

    public int getID() {
        return runeCompId;
    }
}
