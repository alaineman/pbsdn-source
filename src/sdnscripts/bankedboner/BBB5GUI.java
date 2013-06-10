package sdnscripts.bankedboner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Alaineman
 */
public class BBB5GUI extends JFrame {

    private JRadioButton[] boneOptionButtons = {new JRadioButton("Normal Bones"), new JRadioButton("Big Bones"), new JRadioButton("Babydragon Bones"), new JRadioButton("Dragon bones"), new JRadioButton("Impious ashes")};
    private JRadioButton[] buryOptionButtons = {new JRadioButton("Ability bar"), new JRadioButton("Inventory (old skool)")};
    private JCheckBox lootBox = new JCheckBox("Loot & bury modus");
    private ButtonGroup boneGroup;
    private ButtonGroup buryGroup;
    private JTabbedPane basicPane;
    private JMenuItem runItem;
    ButtonGroup group2 = null;
    private ActionListener listener;
    public static final int BONES[] = {526, 532, 534, 536, 20264,};// normal, big, baby, dragon, imp 

    public static void main(String[] args) {
        JFrame a = new BBB5GUI("Test");
        a.setVisible(true);
    }

    public BBB5GUI(String name) {
        super(name);
        setSize(480, 320);

        listener = new MyListener();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "Template Options");
        Image image1 = getImage("http://www.eclipse.org/equinox-portal/tutorials/server-side/images/RunIcon.png");

        ImageIcon runIcon = new ImageIcon(image1);
        runItem = new JMenuItem("Run", runIcon);
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                ActionEvent.CTRL_MASK));
        runItem.addActionListener(listener);
        fileMenu.add(runItem);

        JMenuBar bar = new JMenuBar();
        bar.add(fileMenu);
        setJMenuBar(bar);

        basicPane = new FilledTitleTabbedPane();
        basicPane.setBackground(Color.GRAY);
        basicPane.add("Loot modus", infoPanel());
        basicPane.add("Bones type", bonePanel());
        basicPane.add("Bury style", buryPanel());
        getContentPane().add(basicPane, BorderLayout.CENTER);

    }

    private JPanel infoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 0));
        infoPanel.add(new JLabel("Check this box if you want to loot & bury"));
        infoPanel.add(new JLabel("Note: if you check this box \n you do  NOT need to select options at the other tabs"));
        infoPanel.add(lootBox);
        return infoPanel;
    }

    private JPanel bonePanel() {
        JPanel bonePanel = new JPanel();
        boneGroup = new ButtonGroup();
        bonePanel.setLayout(new GridLayout(boneOptionButtons.length, 0));
        for (JRadioButton b : boneOptionButtons) {
            bonePanel.add(b);
            b.addActionListener(listener);
            boneGroup.add(b);
        }
        return bonePanel;
    }

    private JPanel buryPanel() {
        JPanel bonePanel = new JPanel();
        buryGroup = new ButtonGroup();
        bonePanel.setLayout(new GridLayout(buryOptionButtons.length, 0));
        for (JRadioButton b : buryOptionButtons) {
            bonePanel.add(b);
            b.addActionListener(listener);
            buryGroup.add(b);
        }
        return bonePanel;
    }

    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (runItem != null && obj != null && obj.equals(runItem)) {
                if (lootBox.isSelected()) {
                    //provide loot module
                    JOptionPane.showMessageDialog(null, "You are a brave warrior who lives fight for booty! (loot modus selected)");
                } else {
                    boolean selectionMade = false;
                    JOptionPane.showMessageDialog(null, "You chose bank and bury modus.");
                    for (int i = 0; i < boneOptionButtons.length; i++) {
                        if (boneOptionButtons[i].isSelected()) {
                            BBB5.statusSelection = i;
                            selectionMade = true;
                            BBB5.provide(new Banking(BONES[i]));//set bone id on the same number of the array
                            JOptionPane.showMessageDialog(null, "You have selected " + boneOptionButtons[i].getText());
                            break;
                        }
                    }
                    if (selectionMade) {
                        if (buryOptionButtons[0].isSelected()) {
                            BBB5.provide(new AbilityBarBury());
                            JOptionPane.showMessageDialog(null, "You have picked " + buryOptionButtons[0].getText());
                        } else if (buryOptionButtons[1].isSelected()) {
                            BBB5.provide(new InventoryBury());
                            JOptionPane.showMessageDialog(null, "You have picked " + buryOptionButtons[1].getText());
                        } else {
                            BBB5.canceled = true;
                        }
                    } else {
                        BBB5.canceled = true;
                    }
                }
                dispose();
            }
        }
    }

    // Method to import
    private static Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }
}
