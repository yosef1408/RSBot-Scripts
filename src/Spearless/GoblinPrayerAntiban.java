package Spearless;


import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import org.powerbot.script.PollingScript;

/**
 * Created by Gaston on 15/12/2017.
 */
@Script.Manifest(name = "WcFishMine", properties = "author=Spearless; topic=1333332; client=4;", description = "The objective of the script is for new accounts to level up Mining, Woodcutting. Start at Lumbridge with an axe, a pickaxe and a net, nothing else")
public class GoblinAntiban extends PollingScript<ClientContext> {

    Random random = new Random();
    public Boolean xpalterB = false;
    public Boolean checkSkillB = false;
    public Boolean rotateCameraB = false;
    public Boolean dropB = false;
    public Boolean slepeB = false;

    public void start() {
        final JFrame frame = new JFrame();
        frame.setSize(300, 500);
        final JCheckBox xpalterr = new JCheckBox("Alter xp");
        final JCheckBox checkSkill = new JCheckBox("Check skill xp");
        final JCheckBox RotateCamera = new JCheckBox("Rotate camera");
        final JCheckBox Drop = new JCheckBox("Error dropping");
        final JCheckBox slepe = new JCheckBox("Sleep");
        final JLabel label = new JLabel("------------------Antiban------------------");
        label.setLocation(20, 60);

        slepe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (slepe.isEnabled()) {
                    slepeB = true;
                }
            }
        });
        Drop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Drop.isEnabled()) {
                    dropB = true;
                }
            }
        });
        xpalterr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (xpalterr.isEnabled()) {
                    xpalterB = true;
                }
            }
        });
        checkSkill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkSkill.isEnabled()) {
                    checkSkillB = true;
                }
            }
        });
        RotateCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (RotateCamera.isEnabled()) {
                    rotateCameraB = true;
                }
            }
        });

        JPanel panel = new JPanel();

        panel.add(RotateCamera);
        panel.add(xpalterr);
        panel.add(checkSkill);
        panel.add(Drop);
        panel.add(slepe);
        panel.add(label);
        frame.add(panel);
        frame.setVisible(true);


    }

    void xpAlter() {
        int xpcheck = random.nextInt(25);
        if (xpcheck == 3) {
            ctx.widgets.component(160, 1).hover();
            ctx.widgets.component(160, 1).click();
        }
    }

    void checkPraySkill() {
        if (!ctx.widgets.component(320, 16).inViewport()) {
            ctx.widgets.component(548, 48).valid();
            ctx.widgets.component(548, 48).hover();
            Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
            ctx.widgets.component(548, 48).click();
            log.info("Checking skill");
        }
        ctx.widgets.component(320, 5).hover();
        Condition.sleep(org.powerbot.script.Random.nextInt(550, 900));
        ctx.widgets.component(548, 50).hover();
        ctx.widgets.component(548, 50).click();
    }

    void antiban() {
        int switcher = random.nextInt(50);
        switch (switcher) {

            default:
                Condition.sleep(org.powerbot.script.Random.nextInt(100, 500));
                break;
            case 1:
                if (checkSkillB == true) {
                    checkPraySkill();
                }
                break;

            case 2:
                if (xpalterB == true) {
                    int xpcheck = random.nextInt(25);
                    if (xpcheck == 3) {
                        xpAlter();
                    }
                }

                break;
            case 3:
                int camera = random.nextInt(4);
                if (rotateCameraB == true) {
                    switch (camera) {
                        case 1:
                            ctx.camera.angle('n');
                            break;
                        case 2:
                            ctx.camera.angle('w');
                            break;
                        case 3:
                            ctx.camera.angle('s');
                            break;
                        case 4:
                            ctx.camera.angle('e');
                            break;
                    }
                }
                break;
            case 5:
                if (slepeB == true) {
                    Condition.sleep(org.powerbot.script.Random.nextInt(7000, 10000));
                }
                break;
        }


    }


    public void poll() {

    }
}
