package sdnscripts.pestcontrol;

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
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PestGUI extends JFrame {

    private JRadioButton[] modusButtons = {new JRadioButton("Portal-only"), new JRadioButton("Knight-only"),
        new JRadioButton("Monster-only"), new JRadioButton("Hybrid")};
    private JCheckBox[] monsterBoxs = {new JCheckBox("Ravager"), new JCheckBox("Splatter"), new JCheckBox("Torcher"),
        new JCheckBox("Spinner"), new JCheckBox("Defiler"), new JCheckBox("Brawler"), new JCheckBox("Shifter")};
    private final int[][] MONSTER_IDS = {
        {3742, 3743, 3744, 3745, 3746}, //ravager
        {3727, 3728, 3729, 3730, 3731},
        {3752, 3753, 3754, 3755, 3756, 3757, 3758, 3759, 3760, 3761},
        {3747, 3748, 3749, 3750, 3751},
        {3762, 3763, 3764, 3765, 3766, 3767, 3768, 3769, 3770, 3771},
        {3772, 3773, 3774, 3775, 3776},
        {3732, 3733, 3734, 3735, 3736, 3737, 3738, 3739, 3740, 3741}};
    private JRadioButton[] difficultyButtons = {new JRadioButton("Novice"),
        new JRadioButton("Intermediate"), new JRadioButton("Veteran")};
    private JButton removeButton = new JButton("Remove");
    private JButton addButton = new JButton("Add");
    private JMenuItem runItem;
    private JList list;
    private JComboBox rewardComboBox;
    private DefaultListModel listModel;
    private JTabbedPane basicPane;
    ActionListener listener;

    public static void main(String[] args) {
        JFrame a = new PestGUI("Test");
        a.setVisible(true);
    }

    public PestGUI(String name) {
        super(name);
        setSize(640, 360);
        listener = new MyListener();

        basicPane = new FilledTitleTabbedPane();
        basicPane.setBackground(Color.GRAY);
        basicPane.add("Fight option", firstPanel());
        basicPane.add("Rewards", rewardPanel());
        basicPane.add("Difficulty", levelPanel());
        add(basicPane, BorderLayout.CENTER);

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
    }

    private JPanel firstPanel() {
        JPanel modi = new JPanel();
        modi.setLayout(new GridLayout(6, 2));
        ButtonGroup group = new ButtonGroup();
        for (JRadioButton jRadioButton : modusButtons) {
            group.add(jRadioButton);
            modi.add(jRadioButton);
            jRadioButton.addActionListener(listener);
        }
        modusButtons[3].setSelected(true);
        for (JCheckBox jCheckBox : monsterBoxs) {
            modi.add(jCheckBox);
        }
        return modi;
    }

    private JPanel rewardPanel() {
        ListSelectionListener lsl = new ListListener();
        JPanel rewardPanel = new JPanel();
        rewardPanel.setLayout(new GridLayout(2, 2));
        String[] rewardStrings = new String[Reward.values().length];
        for (int i = 0; i < Reward.values().length; i++) {
            rewardStrings[i] = Reward.values()[i].name();
        }
        rewardComboBox = new JComboBox(rewardStrings);
        rewardPanel.add(rewardComboBox);
        listModel = new DefaultListModel();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(lsl);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        rewardPanel.add(listScrollPane);
        rewardPanel.add(removeButton);
        rewardPanel.add(addButton);
        removeButton.addActionListener(listener);
        addButton.addActionListener(listener);
        return rewardPanel;
    }

    private JPanel levelPanel() {
        JPanel levelPanel = new JPanel();
        ButtonGroup group2 = new ButtonGroup();
        for (JRadioButton jRadioButton : modusButtons) {
            group2.add(jRadioButton);
            levelPanel.add(jRadioButton);
        }
        modusButtons[0].setSelected(true);
        return levelPanel;
    }

    private class ListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
        }
    }

    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object component = e.getSource();
            if (component instanceof JRadioButton) {
                if (component.equals(modusButtons[3]) || component.equals(modusButtons[2])) {
                    for (JCheckBox jcb : monsterBoxs) {
                        jcb.setEnabled(true);
                        jcb.setSelected(false);
                    }
                } else {
                    for (JCheckBox jcb : monsterBoxs) {
                        jcb.setEnabled(false);
                        jcb.setSelected(false);
                    }
                    if (component.equals(modusButtons[1])) {
                        monsterBoxs[6].setSelected(true);
                    }
                }
            } else if (component.equals(removeButton)) {
                if (list.getSelectedIndex() != -1) {
                    for (Reward rew : Reward.values()) {
                        if (rew.toString().equalsIgnoreCase(list.getSelectedValue().toString())) {
                            PestMain.rewardQueue.remove(rew);
                        }
                    }
                    listModel.removeElement(list.getSelectedValue());
                }
            } else if (component.equals(addButton)) {
                if (rewardComboBox.getSelectedIndex() != -1) {
                    listModel.addElement(rewardComboBox.getSelectedItem());
                    for (Reward rew : Reward.values()) {
                        if (rew.toString().equalsIgnoreCase(rewardComboBox.getSelectedItem().toString())) {
                            PestMain.rewardQueue.add(rew);
                        }
                    }

                } else {
                    System.out.println(list.getSelectedIndex() + " , " + rewardComboBox.getSelectedIndex());
                }
            } else if (component.equals(runItem)) {
                for (int m = 0; m < monsterBoxs.length; m++) {
                    if (monsterBoxs[m].isSelected()) {
                        PestMain.game.addMonster(MONSTER_IDS[m]);
                    }
                }
                for (int i = 0; i < difficultyButtons.length; i++) {
                    if (difficultyButtons[i].isSelected()) {
                        PestMain.difficultySelection = i;
                    }
                }
                PestMain.provide(new Board(), new GamePlay(), new Exchange());
            }
        }
    }

    private static Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {;
            return null;
        }
    }
}
