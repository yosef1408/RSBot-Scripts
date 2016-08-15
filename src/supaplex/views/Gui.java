package supaplex.views;

import supaplex.util.GlobalVariables;
import supaplex.util.TaskHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Andreas on 29.06.2016.
 */
public class Gui extends JFrame implements ActionListener {

    // Frame properties
    GridLayout mainPanelLayout = new GridLayout(1, 2);
    Dimension frameDimension = new Dimension(600, 220);

    // West panel
    JButton removeTaskButton = new JButton("REMOVE TASK");
    JButton clearTasksButton = new JButton("CLEAR TASKS");
    DefaultListModel listModel;
    JList jList;

    // East panel
    String[] actionTypes = {
            "Cut bows",
            "String bows"
    };
    String[] bowsTypes = {
            "Shortbow",
            "Longbow",
            "Oak Shortbow",
            "Oak Longbow",
            "Willow Shortbow",
            "Willow Longbow",
            "Maple Shortbow",
            "Maple Longbow",
            "Yew Shortbow",
            "Yew Longbow",
            "Magic Shortbow",
            "Magic Longbow"
    };
    JComboBox actionTypesComboBox = new JComboBox(actionTypes);
    JComboBox bowTypesComboBox = new JComboBox(bowsTypes);

    JButton addTaskButton = new JButton("ADD");
    JButton startButton = new JButton("START");

    public Gui() {
        super("SUpaFletch");
        setResizable(false);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        //Create and set up the window.
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Set up the content pane.

        this.addComponentsToPane(getContentPane());
        //Display the window.
        this.pack();
        this.setSize(frameDimension);
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void addComponentsToPane(final Container pane) {

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(mainPanelLayout);

        mainPanel.add(addComponentsToWestPanel());
        mainPanel.add(addComponentsToEastPanel());

        pane.add(mainPanel);


    }

    // West panel
    private JPanel addComponentsToWestPanel() {
        JPanel westPanel = new JPanel(new BorderLayout());
        this.listModel = new DefaultListModel();
        this.jList = new JList(listModel);
        JScrollPane jScrollPane = new JScrollPane(jList);

        westPanel.add(jScrollPane, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 20);
        buttonPanel.add(removeTaskButton, gbc);
        buttonPanel.add(clearTasksButton);

        westPanel.add(buttonPanel);

        removeTaskButton.addActionListener(this);
        clearTasksButton.addActionListener(this);

        return westPanel;
    }

    // East panel
    private JPanel addComponentsToEastPanel() {
        JPanel eastPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(0, 0, 10, 0);
        c.gridy = 0;
        eastPanel.add(actionTypesComboBox, c);
        c.gridy++;
        eastPanel.add(bowTypesComboBox, c);
        c.gridy++;
        eastPanel.add(addTaskButton, c);
        c.gridy++;
        eastPanel.add(startButton, c);

        addTaskButton.addActionListener(this);
        startButton.addActionListener(this);

        return eastPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeTaskButton) {
            int selectedIndex = jList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
            }
        } else if (e.getSource() == clearTasksButton) {
            listModel.clear();
        } else if (e.getSource() == addTaskButton) {
            String actionType = actionTypesComboBox.getSelectedItem().toString();
            String bowType = bowTypesComboBox.getSelectedItem().toString();
            listModel.addElement(actionType + " - " + bowType);
        } else if (e.getSource() == startButton) {
            int size = jList.getModel().getSize();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    TaskHandler.getInstance().addTask(jList.getModel().getElementAt(i));
                }
                GlobalVariables.startScriptFlag = true;

                setVisible(false);
                dispose();
            }

        }
    }
}
