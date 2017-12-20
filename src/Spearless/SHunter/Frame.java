package Spearless;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends PollingScript<ClientContext> {
    int animal = 0;
    private int RedCB =0;
    private int champsB =0;

    void frameAnimal() {
        final JCheckBox birds = new JCheckBox("Brown birds");
        final JCheckBox blueBirds = new JCheckBox("Blue birds");
        final JCheckBox butter = new JCheckBox("Icy Butterflys");
        final JCheckBox centerCheckerr = new JCheckBox("Return to center butterfly");
        final JCheckBox krebbt = new JCheckBox("Kebbit brown");
        final JCheckBox salaman = new JCheckBox("Salamanders");
        final JCheckBox champs = new JCheckBox("Normal Chin");
        final JCheckBox RedC = new JCheckBox("Red chin");
        final JFrame frame = new JFrame();
        frame.setSize(300, 400);
        RedC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (RedC.isEnabled()) {
                    champsB = 1;
                    animal = 7;
                }
            }
        });
        champs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (champs.isEnabled()) {
                    animal = 6;

                }
            }
        });
        salaman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (salaman.isEnabled()) {
                    animal = 5;
                }
            }
        });
        krebbt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (krebbt.isEnabled()) {
                    animal = 4;
                }
            }
        });


        birds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (birds.isEnabled()) {
                    animal = 1;
                }
            }
        });
        blueBirds.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (blueBirds.isEnabled()) {
                    animal = 2;
                }
            }
        });
        butter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (butter.isEnabled()) {
                    animal = 3;
                }
            }
        });
        RedC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (RedC.isEnabled()) {
                    RedCB = 1;
                }
            }
        });


        JPanel panel = new JPanel();
        panel.add(birds);
        panel.add(blueBirds);
        panel.add(butter);
        panel.add(centerCheckerr);
        panel.add(krebbt);
        panel.add(salaman);
        panel.add(champs);
        panel.add(RedC);
        frame.add(panel);
        frame.setVisible(true);
        log.info("Saliendo frame"+animal);
    }



        public void poll() {


        switch(state())

        {
            case FRAME:
        }
    }

    private State state(){


        return State.FRAME;
    }

    private enum State{
        FRAME
    }
}
