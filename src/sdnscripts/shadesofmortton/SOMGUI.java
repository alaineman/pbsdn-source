package sdnscripts.shadesofmortton;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 *
 * @author Alaineman
 */
public class SOMGUI extends JFrame {

    private JRadioButton combat;
    private JRadioButton building;
    private JButton runButton;
    private JButton cancelButton;
    ButtonGroup group2 = null;

    public static void main(String[] args) {
        JFrame a = new SOMGUI("Test");
        a.setVisible(true);
    }

    public SOMGUI(String name) {
        super(name);
        setLayout(new GridLayout(3, 2));
        setSize(300, 300);

        ActionListener listener = new MyListener();
        combat = new JRadioButton("Combat mode");
        building = new JRadioButton("Build mode");
        group2 = new ButtonGroup();
        group2.add(combat);
        group2.add(building);

        add(new JLabel("Pick your sactity style"));
        add(new JLabel(""));
        add(combat);
        add(building);

        runButton = new JButton("Run");
        runButton.addActionListener(listener);
        add(runButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(listener);
        add(cancelButton);
    }

    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj.equals(runButton)) {
                if (group2 != null) {
                    if (combat.isSelected()) {
                        SacredOil.fightModus = true;
                        SacredOil.provide(new FightModus());
                    } else if (building.isSelected()) {
                        SacredOil.fightModus = false;
                        SacredOil.provide(new BuildModus());
                    }
                }
                SacredOil.provide(new Banking());
                setVisible(false);
            } else if (obj.equals(cancelButton)) {
                SacredOil.canceled = true;
                dispose();
            }
        }
    }
}
