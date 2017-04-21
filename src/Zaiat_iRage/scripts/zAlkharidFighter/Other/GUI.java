package Zaiat_iRage.scripts.zAlkharidFighter.Other;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import Zaiat_iRage.scripts.zAlkharidFighter.zAlkharidFighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class GUI
{
    private JFrame frame = new JFrame("zAlkharidFighter");
    private JTextField textfield_food_name = new JTextField("", 14);

    public GUI(final zAlkharidFighter script, final ClientContext ctx)
    {
        final JButton button = new JButton("Start");
        JLabel label_food_name = new JLabel    ("Food name     ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        layout.putConstraint(SpringLayout.WEST, label_food_name,10, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, label_food_name,15,SpringLayout.NORTH, contentPane);

        layout.putConstraint(SpringLayout.WEST, textfield_food_name,10, SpringLayout.EAST, label_food_name);
        layout.putConstraint(SpringLayout.SOUTH, textfield_food_name, 3, SpringLayout.SOUTH, label_food_name);

        layout.putConstraint(SpringLayout.WEST, button,10, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.EAST, button,0, SpringLayout.EAST, textfield_food_name);
        layout.putConstraint(SpringLayout.NORTH, button, 20, SpringLayout.SOUTH, label_food_name);

        layout.putConstraint(SpringLayout.WEST, contentPane,10, SpringLayout.WEST, label_food_name);
        layout.putConstraint(SpringLayout.EAST, contentPane, 10, SpringLayout.EAST, textfield_food_name);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 0, SpringLayout.SOUTH, button);

        contentPane.add(label_food_name);
        contentPane.add(textfield_food_name);
        contentPane.add(button);
        frame.pack();
        frame.setResizable(false);
        frame.setLocation(10, 10);
        frame.setVisible(true);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(textfield_food_name.getText().length() == 0)
                {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter your desired food name",
                            "Missing Food",
                            JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    zAlkharidFighter.food = textfield_food_name.getText().substring(0, 1).toUpperCase() + textfield_food_name.getText().substring(1).toLowerCase();
                    if(!zAlkharidFighter.start)
                    {
                        zAlkharidFighter.starting_HP_exp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
                        zAlkharidFighter.starting_CB_exp =
                                ctx.skills.experience(Constants.SKILLS_ATTACK) +
                                        ctx.skills.experience(Constants.SKILLS_STRENGTH) +
                                        ctx.skills.experience(Constants.SKILLS_DEFENSE) +
                                        ctx.skills.experience(Constants.SKILLS_RANGE) +
                                        ctx.skills.experience(Constants.SKILLS_MAGIC);
                        button.setText("Update");
                        script.startTime = script.getRuntime();
                        zAlkharidFighter.start = true;
                    }
                }
            }
        });
    }
}
