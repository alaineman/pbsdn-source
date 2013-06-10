package sdnscripts.bankedboner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

/*
 * 
 * @author Alaineman
 */
@Manifest(authors = {"Alaineman"}, name = "Brutal Banked Boner", description = "It's Back, so... Prepare your anus!", version = 5.07, topic = 819957, vip = false, hidden = false)
public class BBB5 extends ActiveScript implements PaintListener {

    /**
     * A List of Node elements which will push to the loop each time.
     */
    protected static final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    /**
     * A tree structure in which the Node's are processed in the loop.
     */
    protected static Tree jobContainer = null;
    /**
     * If canceled is set to true the script will stop.
     */
    public static boolean canceled = false;
    /**
     * This is the general status where the script is in.
     */
    public static String status = "";
    public static int statusNr;
    public static int statusSelection;
    /**
     * The paint object which does calculations and displays the paint.
     */
    public static Paint paint = null;

    public static synchronized void provide(final Node... jobs) {
        for (final Node job : jobs) {
            if (!jobsCollection.contains(job)) {
                jobsCollection.add(job);
            }
        }
        jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    @Override
    public void onStart() {
        JFrame gui = new BBB5GUI("BBB7 GUI");
        gui.setVisible(true);
        paint = new Paint();
    }

    @Override
    public int loop() {
        if (canceled) {
            JOptionPane.showMessageDialog(null, "Thanks for using Brutal Banked Boner 5.");
            stop();
        }
        if (jobContainer != null) {
            final Node job = jobContainer.state();
            if (job != null) {
                jobContainer.set(job);
                getContainer().submit(job);
                job.join();
            }
        }
        return Random.nextInt(400, 600);
    }

    public static void statusUpdate(boolean isBanking) {
        if (!isBanking) {
            statusNr = Random.nextInt(0, 4);
            status = stati[statusSelection][statusNr];
        } else {
            statusNr = Random.nextInt(0, 9);
            status = statusBank[statusNr];
        }
    }
    static final String[] normal = {"Hide the body...", "Holy Crab!", "Ossa Sepelio.", "Parvae Scapulae.", "Ain't nobody got time fo dat."};
    static final String[] big = {"These are gigantic!!",
        "These bones look enormous!!!",
        "RADIO: The giants lose again...",
        "Wasn't fat, just bigboned.", "Big girls, you are beautiful - Mika"};
    static final String[] baby = {"Don't let the animal police see this stack!",
        "Delivery for the dragonrider necro-pedo-filia club?",
        "Ossiculis draconum",
        "All hail the dragonslayer named pedobear!",
        "Nobody likes an overgrown chicken anyway..."};
    static final String[] dragon = {"Ossa draci", "Down they go...",
        "These would feed a dog for months.", "Y U NO USE ALTAR!",
        "Smaug not so tough now."};
    static final String[] imp = {"Releasing the urn!", "I'm a scatman!",
        "Another one bites the dust", "*Blows ash in bankers eye*",
        "Take this Zamorak!"};
    static final String[][] stati = {normal, big, baby, dragon, imp};
    static final String[] statusBank = {"Starting loan...",
        "This banker looks like a scammer to me...",
        "Is this bank safe?", "Starting loan...", "So many stuff!",
        "This looks like a hustle...", "Not enough credit?",
        "Y U NO PAY INTEREST!", "Shiny golden coins.", "Do you take paypal as well?"};

    
    private final String author = "Alaineman";
    private final String version = "5.00";
    private int bonesUsed;
    private int startLevel;
    private int xpGained;
    private int currentXp;
    private int startXp;
    private Timer runTimer;
    // Version details    
    private double xpPerSecond;
    private String timeNextLevel;
    private int nextLevel;
    private int nextLevelXp;
    private int currentLevel;
    private double xpPerHour;
    private int xpToNextLevel;
    
    
    private final Color color1 = new Color(0, 0, 0);
    private final Color color2 = new Color(255, 255, 255);
    private final Color color3 = new Color(204, 255, 204);
    private final Color color4 = new Color(0, 255, 0);
    private final Color color5 = new Color(255, 102, 102);
    private final Color color6 = new Color(255, 204, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Bookman Old Style", 1, 14);
    private final Font font2 = new Font("Blackadder ITC", 1, 32);
    private final Font font3 = new Font("Jokerman", 1, 25);
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<>();
    
    @Override
    public void onRepaint(Graphics g1) {   
        if(paint != null){
        paint.calculate();
        }
        Graphics2D g = (Graphics2D) g1;

        // Paint
        if (Game.getClientState() == 11) {

            final RenderingHints antialiasing = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHints(antialiasing);

            g.setColor(color1);
            g.fillRoundRect(6, 395, 491, 112, 16, 16);
            g.setColor(color2);
            g.setStroke(stroke1);
            g.drawRoundRect(6, 395, 491, 112, 16, 16);
            g.setFont(font1);
            g.drawString("Xp gained:", 325, 415);
            g.drawString("Xp per hour:", 325, 435);
            g.drawString("Xp until " + nextLevel
                    + ":", 325, 475);
            g.drawString("Amount used:", 16, 435);
            g.setFont(font2);
            g.setColor(color3);
            g.drawString("Author: " + author, 16, 100);
            g.setColor(color4);
            g.drawString("Author: " + author, 14, 100);
            g.setFont(font1);
            g.setColor(color2);
            g.drawString("Version:", 16, 415);
            g.drawString("Time running:", 16, 455);
            g.drawString("Time until next level:", 16, 495);
            g.drawString("Status:", 325, 455);
            g.drawString("Current level:", 16, 475);
            g.setFont(font3);
            g.drawString("Brutal Banked Boner", 16, 140);

            String levelStatus = displayLevel(currentLevel, startLevel);

            g.setFont(font1);
            g.setColor(Color.GREEN);
            g.drawString(version, 175, 415);
            g.setColor(color6);
            g.drawString(Integer.toString(bonesUsed), 175, 435);
            g.drawString(runTimer.toElapsedString(), 175, 455);
            g.drawString(levelStatus, 175, 475);
            g.drawString(timeNextLevel, 175, 495);
            g.drawString(Integer.toString(xpGained), 425, 415);
            g.drawString(Integer.toString((int) xpPerHour), 425, 435);
            g.drawString(BBB5.status, 380, 455);
            g.drawString(Integer.toString(xpToNextLevel), 425, 475);

        }

        // Mouse cursor
        g.setColor(Color.CYAN);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5,
                Mouse.getY() + 5);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5,
                Mouse.getY() - 5);

        // Mouse trail
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = Mouse.getLocation();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
                200); // Lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        MousePathPoint lastPoint = null;
        for (MousePathPoint mousePathPoint : mousePath) {
            if (lastPoint != null) {
                g.setColor(color5);// Trail color
                g.drawLine(mousePathPoint.x, mousePathPoint.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = mousePathPoint;
        }
    }

    private String displayLevel(int currentLevel, int startLevel) {
        int diff = currentLevel - startLevel;
        if (diff > 0) {
            return currentLevel + " (" + diff + ")";
        } else {
            return "" + startLevel;
        }
    }

    public String displayTime(int sec) {
        int min = sec / 60;
        int hour = min / 60;
        int day = hour / 24;
        int secrest = sec % 60;
        int minrest = min % 60;
        int hourrest = hour % 24;

        String hrre = Integer.toString(hourrest);
        String mrre = Integer.toString(minrest);
        String srre = Integer.toString(secrest);

        if (hourrest < 10) {
            hrre = "0" + hourrest;
        }
        if (minrest < 10) {
            mrre = "0" + minrest;
        }
        if (secrest < 10) {
            srre = "0" + secrest;
        }
        if (day > 99) {
            return org.powerbot.game.api.util.Random.nextBoolean() ? "Ain't nobody got time fo dat"
                    : "Yo dawg, DAT TIME IS OVER 9000!";
        } else {
            return (day + "." + hrre + "." + mrre + "." + srre);
        }
    }

    @SuppressWarnings("serial")
    private class MousePathPoint extends Point { // All credits to Enfilade

        private final long finishTime;
        private final double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }
    }    

    public class Paint {

        public Paint() {
            runTimer = new Timer(0);
            bonesUsed = 0;
            currentXp = Skills.getExperience(Skills.PRAYER);
            startXp = Skills.getExperience(Skills.PRAYER);
            startLevel = Skills.getLevel(Skills.PRAYER);
        }

        public void addBones(int i) {
            bonesUsed += i;
        }

        public void calculate() {
            currentXp = Skills.getExperience(Skills.PRAYER);
            double secPast = runTimer.getElapsed() / 1000;

            xpGained = currentXp - startXp;

            if (xpGained > 0 && secPast != 0) {
                xpPerSecond = xpGained / secPast;
            } else {
                xpPerSecond = 0;
            }

            xpPerHour = (xpPerSecond * 3600);
            currentLevel = Skills.getLevel(Skills.PRAYER);
            nextLevel = currentLevel + 1;
            nextLevelXp = Skills.getExperienceRequired(nextLevel);
            xpToNextLevel = nextLevelXp - currentXp;

            double secondsToNextLevel;
            if (xpPerSecond > 0) {
                secondsToNextLevel = (xpToNextLevel / xpPerSecond);
                timeNextLevel = displayTime((int) secondsToNextLevel);
            } else {
                timeNextLevel = "Not enough XP per hour.";
            }
        }

        public String displayTime(int sec) {
            int min = sec / 60;
            int hour = min / 60;
            int day = hour / 24;
            int secrest = sec % 60;
            int minrest = min % 60;
            int hourrest = hour % 24;

            String hrre = Integer.toString(hourrest);
            String mrre = Integer.toString(minrest);
            String srre = Integer.toString(secrest);

            if (hourrest < 10) {
                hrre = "0" + hourrest;
            }
            if (minrest < 10) {
                mrre = "0" + minrest;
            }
            if (secrest < 10) {
                srre = "0" + secrest;
            }
            if (day > 99) {
                return org.powerbot.game.api.util.Random.nextBoolean() ? "Ain't nobody got time fo dat"
                        : "Yo dawg, DAT TIME IS OVER 9000!";
            } else {
                return (day + "." + hrre + "." + mrre + "." + srre);
            }
        }
    }
}
