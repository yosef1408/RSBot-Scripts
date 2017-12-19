package Spearless;

/**
 * Created by Gaston on 17/12/2017.
 */

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Gaston on 31/05/2017.
 */
@Script.Manifest(name = "Frame2", properties = "author=Spearless; topic=1333332; client=4;", description = "The objective of the script is for new accounts to level up Mining, Woodcutting. Start at Lumbridge with an axe, a pickaxe and a net, nothing else")
public class Frame2 extends PollingScript<ClientContext> {
    final JCheckBox hoverObjects = new JCheckBox("Hover over objects");
    final JCheckBox checkdropp = new JCheckBox("Error while dropping");
    final JCheckBox checkSkill = new JCheckBox("Check skill");
    final JCheckBox RotateCamera = new JCheckBox("Rotate camera");
    final JCheckBox HoverPlayers = new JCheckBox("Hover over players");
    final JCheckBox moveMouseRandomly = new JCheckBox("Move mouse Randomly");
    final JCheckBox sleep = new JCheckBox("Sleep short time");
    final JCheckBox changeBright = new JCheckBox("Change bright");
    final JCheckBox xpAl = new JCheckBox("Xp alter");
    final JCheckBox checkMoney = new JCheckBox("Check inv price");
    final JCheckBox lazyDay = new JCheckBox("Slower catching (Nor chins/red chins");
    final JCheckBox rightClickC = new JCheckBox("Right click");
    volatile boolean checkingSkil =false;
    volatile boolean errorDropping=false ;
    volatile boolean hovering = false;
    volatile boolean rotateB=false;
    volatile boolean HoverOverPlaers = false;
    volatile boolean moveMOuse = false;
    volatile boolean sleepe = false;
   volatile boolean changebright =false;
    volatile boolean xpAlter = false;
    volatile boolean CheckInv = false;
    volatile boolean rightClick = false;
    int tardandoMasx = 500, tarandomasY = 750;

    public boolean getXp(){
            log.info( "this(1): " + this );
            log.info( "xpAlter.get()(1): " + xpAlter);
            return xpAlter;
        }
    public boolean getBrigh(){
        return changebright;
    }
    public boolean getMon(){
        return CheckInv;
    }
    public boolean getSleep() {
        return sleepe;
    }
    public boolean getMoveM(){
        return moveMOuse;
    }
    public boolean getHoverPlay(){
        return HoverOverPlaers;
    }
    public boolean getRotate(){
        return rotateB;
    }
    public boolean getHoverOb(){
        return hovering;
    }
    public boolean getErrorOnDrop (){
        return errorDropping;
    }
    public boolean getCheckSk(){
        return checkingSkil;
    }
    public boolean getRightC(){
        return rightClick;
    }
    public void start() {
        final JFrame frame = new JFrame();
        frame.setSize(300, 400);

        lazyDay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lazyDay.isEnabled()) {
                    tardandoMasx = 600;
                    tarandomasY = 800;
                }
            }
        });
        checkMoney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (checkMoney.isEnabled()) {

                    CheckInv = checkSkill.isSelected();

                }
            }
        });
        xpAl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    xpAlter = xpAl.isSelected();

            }
        });
        changeBright.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                changebright=changeBright.isSelected();

            }
        });
        sleep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                    sleepe = sleep.isSelected();

            }
        });
        moveMouseRandomly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (moveMouseRandomly.isEnabled()) {
                    moveMOuse=moveMouseRandomly.isSelected();
                }
            }
        });
        HoverPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (HoverPlayers.isEnabled()) {
                    HoverOverPlaers =HoverPlayers.isSelected();

                }
            }
        });
        RotateCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rotateB=RotateCamera.isSelected();
            }
        });
        hoverObjects.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                hovering=hoverObjects.isSelected();
            }
        });
        checkdropp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                errorDropping=checkdropp.isSelected();
            }
        });
        checkSkill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            checkingSkil=checkSkill.isSelected();

            }
        });
        rightClickC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                rightClick=rightClickC.isSelected();
            }
        });
        JPanel panel = new JPanel();
        panel.add(checkdropp);
        panel.add(checkSkill);
        panel.add(hoverObjects);
        panel.add(RotateCamera);
        panel.add(HoverPlayers);
        panel.add(moveMouseRandomly);
        panel.add(sleep);
        panel.add(changeBright);
        panel.add(xpAl);
        panel.add(checkMoney);
        panel.add(lazyDay);
        panel.add(rightClickC);
        frame.add(panel);
        frame.setVisible(true);

        log.info("" + changebright);
        log.info("" + CheckInv);

    }


    public void poll() {


        switch(state())

        {
            case MINE:


        }
    }

    private State state(){


        return State.MINE;
    }

    private enum State{
        MINE
    }
}

