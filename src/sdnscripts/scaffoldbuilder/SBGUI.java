package sdnscripts.scaffoldbuilder;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import org.powerbot.game.api.wrappers.Tile;

/**
 *
 * @author Vincent W
 */
public class SBGUI extends JFrame {

    JButton runButton;
    JButton cancelButton;
    JCheckBox taverleyBox;
    JCheckBox lumbridgeBox;
    JCheckBox canifisBox;
    JCheckBox yanilleBox;
    ButtonGroup group;
    JRadioButton slayerButton;
    JRadioButton prayerButton;

    public static void main(String[] args) {
        JFrame a = new SBGUI("Test");
        a.setVisible(true);
    }

    public SBGUI(String name) {
        super(name);
        setLayout(new GridLayout(6, 2));
        setSize(640, 360);

        ActionListener listener = new MyListener();

        taverleyBox = new JCheckBox("Taverley");
        lumbridgeBox = new JCheckBox("Lumbridge");
        canifisBox = new JCheckBox("Canifis");
        yanilleBox = new JCheckBox("Yanille");

        slayerButton = new JRadioButton("Slayer");
        prayerButton = new JRadioButton("Prayer");
        prayerButton.setSelected(true);
        group = new ButtonGroup();
        group.add(slayerButton);
        group.add(prayerButton);

        add(new JLabel("Select which building sites to visit:"));
        add(new JLabel("Select which type of experience you want to receive:"));
        add(taverleyBox);
        add(slayerButton);
        add(lumbridgeBox);
        add(prayerButton);
        add(canifisBox);
        add(new JLabel("For more setup info check the PB forums"));
        add(yanilleBox);
        add(new JLabel("Enjoy your sculptures!"));
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
            boolean xpType = true;
            Object obj = e.getSource();
            if (obj.equals(runButton)) {
                if (slayerButton.isSelected()) {
                    xpType = false;
                } else if (prayerButton.isSelected()) {
                    xpType = true;
                }
                if (yanilleBox.isSelected()) {
                    ScaffoldBuilder.addSite((new BuildingSite(new Tile(2542, 3063, 0), 16678, xpType, "yanille")));
                }
                if (taverleyBox.isSelected()) {
                    ScaffoldBuilder.addSite(new BuildingSite(new Tile(2916, 3496, 0), 16678, xpType, "taverley"));                    
                }
                if (lumbridgeBox.isSelected()) {
                    ScaffoldBuilder.addSite(new BuildingSite(new Tile(3178, 3281, 0), 16678, xpType, "lumbridge"));
                }
                if (canifisBox.isSelected()) {
                    ScaffoldBuilder.addSite(new BuildingSite(new Tile(3525, 3511, 0), 16678, xpType, "canifis"));
                }
                JOptionPane.showMessageDialog(null, "You have selected " + ScaffoldBuilder.sites.size() + " build sites.");
                String[] XP_OPTION = {"Prayer", "Slayer"};                
                String option = xpType ? XP_OPTION[0] : XP_OPTION[1];
                JOptionPane.showMessageDialog(null, "You have selected " + option + " experience as reward.");
                ScaffoldBuilder.guiRunning = false;
                dispose();                
            } else if (obj.equals(cancelButton)) {
                JOptionPane.showMessageDialog(null, "Did not complete the GUI fase, the script will be stopped.");
                ScaffoldBuilder.guiRunning = false;
                dispose();
            }
        }
    }
}
