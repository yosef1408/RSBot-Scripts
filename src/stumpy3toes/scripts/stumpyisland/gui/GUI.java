package stumpy3toes.scripts.stumpyisland.gui;

import stumpy3toes.scripts.stumpyisland.AccountType;
import stumpy3toes.scripts.stumpyisland.gui.GUIListener;
import stumpy3toes.scripts.stumpyisland.tasks.charactercreation.Gender;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener, ListDataListener {
    private JPanel contentPanel;
    private JComboBox accountTypeComboBox;
    private JCheckBox enterMainlandCheckBox;
    private JButton startButton;
    private JComboBox genderChoiceComboBox;
    private JCheckBox randomiseCharacterCheckBox;
    private JCheckBox userEnterPin;

    private final GUIListener listener;
    private final DefaultComboBoxModel<String> accountTypeComboBoxModel;
    private final DefaultComboBoxModel<String> genderChoiceComboBoxModel;

    private boolean started = false;

    public GUI(final GUIListener listener) {
        super("Choose Account Options");

        this.listener = listener;

        accountTypeComboBoxModel = new DefaultComboBoxModel<String>();
        for (AccountType accountType : AccountType.values()) {
            accountTypeComboBoxModel.addElement(accountType.name());
        }
        accountTypeComboBox.setModel(accountTypeComboBoxModel);
        accountTypeComboBoxModel.addListDataListener(this);

        genderChoiceComboBoxModel = new DefaultComboBoxModel<String>();
        for (Gender gender : Gender.values()) {
            genderChoiceComboBoxModel.addElement(gender.name());
        }
        genderChoiceComboBox.setModel(genderChoiceComboBoxModel);

        startButton.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!started) {
                    listener.closed();
                }
            }
        });

        setContentPane(contentPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            started = true;
            listener.started(AccountType.valueOf(accountTypeComboBoxModel.getSelectedItem().toString()),
                    Gender.valueOf(genderChoiceComboBoxModel.getSelectedItem().toString()),
                    randomiseCharacterCheckBox.isSelected(), enterMainlandCheckBox.isSelected(),
                    userEnterPin.isSelected());
            dispose();
        }
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        userEnterPin.setVisible(AccountType.valueOf(accountTypeComboBoxModel.getSelectedItem().toString())
                != AccountType.NORMAL);
        pack();
    }

    @Override
    public void intervalAdded(ListDataEvent e) {}
    @Override
    public void intervalRemoved(ListDataEvent e) {}
}
