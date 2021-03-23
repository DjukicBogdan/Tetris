import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Tetris extends JFrame {

    public static int moveDown = 0;
    public static int moveX = 95;
    public static int rnd = 0;
    public static int shapeNum = 0;
    public static int sec = 0;
    public static int minute = 0;
    public static int score = 0;
    public static int hiScore = 0;
    public static int level = 1;

    public static JPanel mainPanel = new JPanel();
    public static JPanel gamePanel = new JPanel();
    public static JPanel infoPanel = new JPanel();
    public static JPanel controlPanel = new JPanel();
    public static ArrayList list = new ArrayList();
    public static ArrayList Templist = new ArrayList();
    public static ArrayList Templist2 = new ArrayList();
    public static ArrayList FixedList = new ArrayList();
    public static JPanel gameScreen = new JPanel();
    public static DrawPanel1 drawPanel = new DrawPanel1();
    public static DrawPanelNext drawPanelNext = new DrawPanelNext();
    public static ArrayList<Object>drawPanelNextArrayList = new ArrayList<>();
    public static DrawPanel1 nextDrawPanel = new DrawPanel1();
    public static ArrayList<Integer> pointX = new ArrayList<>();
    public static ArrayList<Integer> pointY = new ArrayList<>();
    public static int speed = 1000;

    JPanel panelNextShape = new JPanel();
    BorderLayout borderLayout = new BorderLayout();
    BoxLayout boxLayout = new BoxLayout(infoPanel,BoxLayout.PAGE_AXIS);

    JButton buttonLeft = new JButton("LEFT");
    JButton buttonRight = new JButton("RIGHT");
    JButton buttonDown = new JButton("DOWN");
    JButton buttonRotate = new JButton("ROTATE");

    JLabel lbScore = new JLabel("Score: \n" + score);
    JLabel lbHiScore = new JLabel("Hi-Score: \n" + hiScore);
    JLabel lbLevel = new JLabel("Level: " + level);
    JLabel lbTime = new JLabel("time: 00:00");

    public Tetris() {
        setName("Tetris");
        setTitle("Tetris");
        setSize(450, 630);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setFocusable(true);
        requestFocus();
        startGame();
        timer();
        setVisible(true);
    }

    public static void startGame() {
        FixedList.clear();
        list.clear();
        Templist.clear();
        sec = 0;
        minute = 0;
        moveDown = 0;
        if (score > hiScore) hiScore = score;
        score = 0;
        level = 1;
        rnd = (int)(Math.random() * 21 + 1);
        NextShape();
    }

    public void initComponents() {
        setFocusable(true);
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_DOWN){
                    MoveShape();

                    speed = 500;
                };

                if (key == KeyEvent.VK_SPACE){
                    MoveShape();

                    speed = 500;
                };
            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_ENTER){
                    moveAllLines(481);
                }

                if (key == KeyEvent.VK_LEFT){
                    buttonLeft.doClick();
                };
                if (key == KeyEvent.VK_RIGHT){
                    buttonRight.doClick();
                };
                if (key == KeyEvent.VK_DOWN){

                    speed = 1000;
                };
                if (key == KeyEvent.VK_UP){
                    buttonRotate.doClick();
                };
                if (key == KeyEvent.VK_SPACE){
                    for (int i = 0; i < 400; i++){

                        speed = 1000;
                    }
                };
            }
        });
        mainPanel.setLayout(borderLayout);
        mainPanel.setOpaque(false);
        infoPanel.setPreferredSize(new Dimension(110,175));
        infoPanel.setLayout(boxLayout);
        infoPanel.add(lbHiScore);
        infoPanel.add(lbScore);

        panelNextShape.setPreferredSize(new Dimension(100,100));
        panelNextShape.add(new DrawPanelNext());
        panelNextShape.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.black));
        infoPanel.add(panelNextShape);
        infoPanel.add(lbLevel);
        infoPanel.add(lbTime);

        controlPanel.add(buttonLeft);
        controlPanel.add(buttonDown);
        controlPanel.add(buttonRight);
        controlPanel.add(buttonRotate);

        gameScreen.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.black));
        gameScreen.setPreferredSize(new Dimension(259,517));
        gameScreen.add(drawPanel);

        gamePanel.add(gameScreen);
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
        gamePanel.add(infoPanel, borderLayout.LINE_END);
        mainPanel.add(gamePanel, borderLayout.CENTER);
        mainPanel.add(controlPanel, borderLayout.PAGE_END);

        buttonRotate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateShape();
                requestFocus();
            }
        });

        buttonDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDown += 25;
                CheckShape();
                requestFocus();
            }
        });

        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < Tetris.Templist2.size(); i++) {
                    Point p2 = (Point) Tetris.Templist2.get(i);
                    int px = p2.x;
                    if ( px < 26) {
                        return;
                    }
                }
                moveX -= 25;
                requestFocus();
            }
        });

        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < Tetris.Templist2.size(); i++) {
                    Point p2 = (Point) Tetris.Templist2.get(i);
                    int px = p2.x;
                    if ( px > 201) {
                        return;
                    }
                }
                moveX += 25;
                requestFocus();
            }
        });
        setContentPane(mainPanel);
    }

    public void timer() {
        Timer t = new Timer(speed, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sec++;
                if (sec >= 60) {
                    sec = 0;
                    minute++;
                }
                String time = "time: " + String.format("%02d", minute) + ":" + String.format("%02d", sec);
                lbTime.setText(time);
                MoveShape();
            }
        });
        t.start();
    }

    public static void NextShape(){
        checkLines();
        Templist2.clear();
        list.clear();
        moveX = 95;
        new DrawPanelNext();
        score++;
        shapeNum = rnd;
        rnd = (int)(Math.random() * 21 + 1);
        moveDown = 0;
    }

    public void updateInfo(){
        lbScore.setText("Score: \n" + score);
        lbHiScore.setText("Hiscore: \n" + hiScore);
        lbLevel.setText("Level: " + level);
    }

    public void MoveShape(){
        boolean check2 = false;
        if (moveDown == 0){moveDown = 25;check2 = true;}
        CheckShape();
        if (!check2)
        {moveDown += 25;}
    }

    public void CheckShape(){
        if (moveDown >= 400 || !checkRules()){
            updateInfo();
            pointX.add(moveX);
            pointY.add(moveDown);
            NextShape();
        }
        updateInfo();
    }

    public static void rotateShape(){
        //4 10 13 16    5 11 14 15    17 18 19 20
        if (shapeNum == 2){shapeNum=7;if (moveX > 145) moveX = 145;} // 259  120 145 170 195 220 245 270
        else if (shapeNum == 7)shapeNum=2;

        if (shapeNum == 1){shapeNum=12;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 12)shapeNum=1;

        if (shapeNum == 6)shapeNum=9;
        else if (shapeNum == 9){shapeNum=6;if (moveX > 170) moveX = 170;}

        if (shapeNum == 4){shapeNum=10;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 10)shapeNum=13;
        else if (shapeNum == 13){shapeNum=16;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 16)shapeNum=4;

        if (shapeNum == 5){shapeNum=11;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 11)shapeNum=14;
        else if (shapeNum == 14){shapeNum=15;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 15)shapeNum=5;

        if (shapeNum == 17)shapeNum=18;
        else if (shapeNum == 18){shapeNum=19;if (moveX > 170) moveX = 170;}
        else if (shapeNum == 19)shapeNum=20;
        else if (shapeNum == 20){shapeNum=17;if (moveX > 170) moveX = 170;}
    }

    public static boolean checkRules(){
        for (int i = 0; i < Tetris.list.size(); i++){
            for (int j = 0; j < Tetris.FixedList.size(); j++) {
                Point p = (Point) Tetris.FixedList.get(j);
                Point p2 = (Point) Tetris.list.get(i);
                int py = p.y;
                int py2 = p2.y;
                int px = p.x;
                int px2 = p2.x;
                if (py == py2 && px == px2 && moveDown <= 50)
                    startGame();

                if (py == py2 && px == px2) {
                    if (moveDown == 0)
                        startGame();

                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkRulesForOnePoint(Point p){
        for (int i = 0; i < FixedList.size(); i++) {

            Point p2 = (Point) FixedList.get(i);
            int px = p.x;
            int py = p.y;
            int px2 = p2.x;
            int py2 = p2.y;
            if (py == py2 && px == px2) {
                return false;
            }
        }
        return true;
    }

    public static void checkLines() {
        int count = 0;

        for (int j = 481; j >= 0; j -= 25) {
            count = 0;
            for (int k = 1; k <= 226; k += 25) {
                for (int i = 0; i < Tetris.FixedList.size(); i++) {
                    Point p2 = (Point) Tetris.FixedList.get(i);
                    int px2 = p2.x;
                    int py2 = p2.y;
                    if (k == px2 && j == py2) {
                        count++;
                        if (count == 10) {

                            for (int l = 1; l <= 226; l +=25) {
                                for (int m = 0;m < Tetris.FixedList.size(); m++) {
                                    Point p1 = (Point) Tetris.FixedList.get(m);
                                    if (p1.y == j && p1.x == l) {
                                        Tetris.FixedList.remove(p1);
                                    }
                                }
                            }
                            Tetris.moveAllLines(j);
                            count = 0;
                        } break;
                    }
                }
            }
        }
    }

    public static void moveAllLines(int line) {

        for (int j = line; j >= 0; j -= 25) {
            for (int k = 1; k <= 226; k += 25) {
                for (int i = 0; i < FixedList.size(); i++) {
                    Point p2 = (Point) FixedList.get(i);
                    int px2 = p2.x;
                    int py2 = p2.y;
                    if (k == px2 && j == py2) {
                        for (int l = 1; l <= 226; l += 25) {
                            for (int m = 0; m < FixedList.size(); m++) {
                                Point p1 = (Point) FixedList.get(m);
                                if (p1.y == j && p1.x == l) {
                                    FixedList.remove(p1);
                                    int xx = p1.x;
                                    int yy = p1.y;
                                    yy += 25;
                                    Point p3 = new Point(xx,yy);
                                    FixedList.add(p3);
                                }
                            }

                        }
                        score += 10;
                        for (int s = 1;s <= 100;s++){
                            if (score > 200 * s && score < 250 * s) level = s;
                        }

                        break;
                    }
                }
            }
        }
    }

    //end of class
}

class DrawPanel1 extends JPanel
{
    boolean check = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int moveDownY = Tetris.moveDown;
        int moveX = Tetris.moveX;
        setBackground(Color.gray);
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
        if (Tetris.shapeNum == 2) {
            // 1         2
            Point p = new Point(moveX + 6, moveDownY + 6);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 2
            Point p = new Point(moveX + 31, moveDownY + 6);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 3
            Point p = new Point(moveX + 56, moveDownY + 6);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 4
            Point p = new Point(moveX + 81, moveDownY + 6);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 1 || Tetris.shapeNum == 2 || Tetris.shapeNum == 4
                || Tetris.shapeNum == 13 || Tetris.shapeNum == 14 || Tetris.shapeNum == 20) {
            // 11         1 2 4 13 14 20
            Point p = new Point(moveX + 6, moveDownY + 31);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 5 || Tetris.shapeNum == 8 || Tetris.shapeNum == 9 || Tetris.shapeNum == 13 || Tetris.shapeNum == 14  || Tetris.shapeNum == 18) {
            // 12         5 8 9 13 14 18
            Point p = new Point(moveX + 31, moveDownY + 31);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 13
            Point p = new Point(moveX + 56, moveDownY + 31);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 14
            Point p = new Point(moveX + 81, moveDownY + 31);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 1 || Tetris.shapeNum == 2 || Tetris.shapeNum == 4 || Tetris.shapeNum == 6 || Tetris.shapeNum == 8 || Tetris.shapeNum == 9 || Tetris.shapeNum == 11 ||
                Tetris.shapeNum == 14 || Tetris.shapeNum == 15 || Tetris.shapeNum == 16 || Tetris.shapeNum == 18 || Tetris.shapeNum == 19 || Tetris.shapeNum == 20) {
            // 21        1 2 4 6 8 9 11 14 15 16 18 19 20
            Point p = new Point(moveX + 6, moveDownY + 56);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 1 || Tetris.shapeNum == 3 || Tetris.shapeNum == 5 || Tetris.shapeNum == 6 || Tetris.shapeNum == 8 || Tetris.shapeNum == 9 || Tetris.shapeNum == 12 ||
                Tetris.shapeNum == 13 || Tetris.shapeNum == 15 || Tetris.shapeNum == 16 || Tetris.shapeNum == 17 || Tetris.shapeNum == 18 || Tetris.shapeNum == 19 || Tetris.shapeNum == 20) {
            // 22         1 3 5 6 8 9 12 13 15 16 17 18 19 20
            Point p = new Point(moveX + 31, moveDownY + 56);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 3 || Tetris.shapeNum == 8 || Tetris.shapeNum == 10 || Tetris.shapeNum == 12 || Tetris.shapeNum == 15 || Tetris.shapeNum == 16 || Tetris.shapeNum == 19) {
            // 23        3 8 10 12 15 16 19
            Point p = new Point(moveX + 56, moveDownY + 56);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 0) {
            // 24
            Point p = new Point(moveX + 81, moveDownY + 56);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 2 || Tetris.shapeNum == 4 || Tetris.shapeNum == 5 || Tetris.shapeNum == 7 || Tetris.shapeNum == 9 || Tetris.shapeNum == 10
                || Tetris.shapeNum == 11|| Tetris.shapeNum == 12 || Tetris.shapeNum == 14 || Tetris.shapeNum == 16 || Tetris.shapeNum == 17 || Tetris.shapeNum == 20 || Tetris.shapeNum == 21) {
            // 31         2 4 5 7 9 10 11 12 14 16 17 20 21
            Point p = new Point(moveX + 6, moveDownY + 81);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 1 || Tetris.shapeNum == 3 || Tetris.shapeNum == 4 || Tetris.shapeNum == 5 || Tetris.shapeNum == 6 || Tetris.shapeNum == 7 || Tetris.shapeNum == 8
                || Tetris.shapeNum == 10 || Tetris.shapeNum == 11|| Tetris.shapeNum == 12 || Tetris.shapeNum == 13 || Tetris.shapeNum == 17 || Tetris.shapeNum == 18 || Tetris.shapeNum == 19) {
            // 32        1 3 4 5 6 7 8 10 11 12 13 17 18 19
            Point p = new Point(moveX + 31, moveDownY + 81);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 3 || Tetris.shapeNum == 6 || Tetris.shapeNum == 7 || Tetris.shapeNum == 10 || Tetris.shapeNum == 11 || Tetris.shapeNum == 15 || Tetris.shapeNum == 17) {
            // 33        3 6 7 10 11 15 17
            Point p = new Point(moveX + 56, moveDownY + 81);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
        if (Tetris.shapeNum == 7) {
            // 34         7
            Point p = new Point(moveX + 81, moveDownY + 81);
            if(Tetris.checkRulesForOnePoint(p))
                Tetris.Templist.add(p);
            else check = true;
        }
       // fill table ---------------------
        setBackground(Color.white);
        for (int j = 481; j >= 0; j -= 25) {
            for (int k = 1; k <= 226; k += 25) {
                int x = k;
                int y = j;
                g2d.setColor(Color.lightGray);
                g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
                g2d.drawRect(x , y , 21, 21);
                g2d.setColor(Color.lightGray);
                g2d.fillRect(x + 5, y + 5, 12, 12);
            }
        }

        // draw move
        if (!check){
            for (int i = 0; i < Tetris.Templist.size(); i++) {
                Tetris.list.add(Tetris.Templist.get(i));
            }
            for (int i = 0; i < Tetris.list.size(); i++) {
                Point p = (Point) Tetris.list.get(i);
                int x = p.x;
                int y = p.y;
                g2d.setColor(Color.black);
                g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
                g2d.drawRect(x , y , 21, 21);
                g2d.setColor(Color.black);
                g2d.fillRect(x + 5, y + 5, 12, 12);
            }
        }

        //draw fixed
        for (int i = 0; i < Tetris.FixedList.size(); i++) {
            Point pFixed = (Point) Tetris.FixedList.get(i);
            int xFixed = pFixed.x;
            int yFixed = pFixed.y;
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect(xFixed , yFixed , 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(xFixed + 5, yFixed + 5, 12, 12);
        }
        // check new game

        if (!Tetris.checkRules() && moveDownY <= 25) {
            System.out.println(moveDownY);
            Tetris.startGame();
        }
        // end of move, take shape
        if (check){
            for (int i = 0; i < Tetris.Templist2.size(); i++)
                Tetris.FixedList.add(Tetris.Templist2.get(i));

            if(moveDownY <= 25) {
                Tetris.startGame();
            }

            Tetris.NextShape();
        }
        // loop new round
        if (!Tetris.checkRules() || moveDownY >= 400) {
            for (int i = 0; i < Tetris.list.size(); i++)
                Tetris.FixedList.add(Tetris.list.get(i));

            Tetris.NextShape();
        }

        Tetris.Templist2.clear();
        // keep record of moved shape
        for (int i = 0; i < Tetris.list.size(); i++) {
            Tetris.Templist2.add(Tetris.list.get(i));
        }
        check = false;
        Tetris.Templist.clear();
        Tetris.list.clear();

        repaint();

        g2d.dispose();
        g.dispose();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(250, 505);
    }

}

class DrawPanelNext extends JPanel
{
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3.0f,BasicStroke.JOIN_ROUND,BasicStroke.JOIN_BEVEL));

        setBackground(Color.white);
        for (int j = 100; j >= 0; j -= 25) { // 110 170
            for (int k = 1; k <= 100; k += 25) {
                int x = k;
                int y = j;
                g2d.setColor(Color.lightGray);
                g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
                g2d.drawRect(x , y , 21, 21);
                g2d.setColor(Color.lightGray);
                g2d.fillRect(x + 5, y + 5, 12, 12);
            }
        }

        if (Tetris.rnd == 2){
            // 1         2
            g2d.setColor(Color.black);
            g2d.drawRect( 1,  1, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(  6,  6, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 2
            g2d.drawRect( 26,  1, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 31,  6, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 3
            g2d.drawRect( 51,  1, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 56,  6, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 4
            g2d.drawRect( 76,  1, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(81,  6, 12, 12);
        }
        if (Tetris.rnd == 1 || Tetris.rnd == 2 || Tetris.rnd == 4
                || Tetris.rnd == 13 || Tetris.rnd == 14 || Tetris.rnd == 20) {
            // 11         1 2 4 13 14 20
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect(1,  26, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 6,  31, 12, 12);
        }
        if (Tetris.rnd == 5 || Tetris.rnd == 8 || Tetris.rnd == 9 || Tetris.rnd == 13 || Tetris.rnd == 14  || Tetris.rnd == 18) {
            // 12         5 8 9 13 14 18

            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 26,  26, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 31,  31, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 13
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 51,  26, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(56,  31, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 14
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 76, 26, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 81,  31, 12, 12);
        }
        if (Tetris.rnd == 1 || Tetris.rnd == 2 || Tetris.rnd == 4 || Tetris.rnd == 6 || Tetris.rnd == 8 || Tetris.rnd == 9 || Tetris.rnd == 11 ||
                Tetris.rnd == 14 || Tetris.rnd == 15 || Tetris.rnd == 16 || Tetris.rnd == 18 || Tetris.rnd == 19 || Tetris.rnd == 20) {
            // 21        1 2 4 6 8 9 11 14 15 16 18 19 20
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 1,  51, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 6,  56, 12, 12);
        }
        if (Tetris.rnd == 1 || Tetris.rnd == 3 || Tetris.rnd == 5 || Tetris.rnd == 6 || Tetris.rnd == 8 || Tetris.rnd == 9 || Tetris.rnd == 12 ||
                Tetris.rnd == 13 || Tetris.rnd == 15 || Tetris.rnd == 16 || Tetris.rnd == 17 || Tetris.rnd == 18 || Tetris.rnd == 19 || Tetris.rnd == 20) {
            // 22         1 3 5 6 8 9 12 13 15 16 17 18 19 20
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 26,  51, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(31,  56, 12, 12);
        }
        if (Tetris.rnd == 3 || Tetris.rnd == 8 || Tetris.rnd == 10 || Tetris.rnd == 12 || Tetris.rnd == 15 || Tetris.rnd == 16 || Tetris.rnd == 19) {
            // 23        3 8 10 12 15 16 19
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect(51,  51, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 56,  56, 12, 12);
        }
        if (Tetris.rnd == 0) {
            // 24
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 76,  51, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 81,  56, 12, 12);
        }
        if (Tetris.rnd == 2 || Tetris.rnd == 4 || Tetris.rnd == 5 || Tetris.rnd == 7 || Tetris.rnd == 9 || Tetris.rnd == 10
                || Tetris.rnd == 11|| Tetris.rnd == 12 || Tetris.rnd == 14 || Tetris.rnd == 16 || Tetris.rnd == 17 || Tetris.rnd == 20 || Tetris.rnd == 21) {
            // 31         2 4 5 7 9 10 11 12 14 16 17 20 21
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 1,  76, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 6,  81, 12, 12);
        }
        if (Tetris.rnd == 1 || Tetris.rnd == 3 || Tetris.rnd == 4 || Tetris.rnd == 5 || Tetris.rnd == 6 || Tetris.rnd == 7 || Tetris.rnd == 8
                || Tetris.rnd == 10 || Tetris.rnd == 11|| Tetris.rnd == 12 || Tetris.rnd == 13 || Tetris.rnd == 17 || Tetris.rnd == 18 || Tetris.rnd == 19) {
            // 32        1 3 4 5 6 7 8 10 11 12 13 17 18 19
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 26, 76, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect(31,  81, 12, 12);
        }
        if (Tetris.rnd == 3 || Tetris.rnd == 6 || Tetris.rnd == 7 || Tetris.rnd == 10 || Tetris.rnd == 11 || Tetris.rnd == 15 || Tetris.rnd == 17) {
            // 33        3 6 7 10 11 15 17
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 51,  76, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 56,  81, 12, 12);
        }
        if (Tetris.rnd == 7) {
            // 34
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_BEVEL));
            g2d.drawRect( 76,  76, 21, 21);
            g2d.setColor(Color.black);
            g2d.fillRect( 81,  81, 12, 12);
        }
        repaint();
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}

//
//   1)             2)       3)                  4)             5)         6)               7)                           8)
//   ██ 11           ██ 1    ██ ██ 22 23                                  ██ ██    21 22     ██ ██ ██ ██  31 32 33 34       ██ 12
//   ██ ██ 21 22     ██ 11   ██ ██ 32 33          ██ 11           ██ 12      ██ ██ 32 33                                 ██ ██ ██ 21 22 23
//      ██ 32        ██ 21                        ██ 21           ██ 22                                                     ██ 32
//                   ██ 31                        ██ ██ 31 32  ██ ██ 31 32
//
//
//    //   9)         2)       3)                   10)             11)                    12)                     7)
////      ██ 12        ██ 1    ██ ██ 22 23                                                 ██ ██   22 23     ██ ██ ██ ██  31 32 33 34
////   ██ ██ 21 22     ██ 11   ██ ██ 32 33                                              ██ ██ 31 32
////   ██ 31           ██ 21                          ██ 23          ██ 21
////                   ██ 31                    ██ ██ ██ 31 32 33    ██ ██ ██ 31 32 33
//
////      13) ██ ██ 11 12      14)   ██ ██ 11 12                                                       17) ██ 22               18) ██ 12
//               ██ 22               ██ 21          15)  ██ ██ ██ 21 22 23    16)  ██ ██ ██ 21 22 23    ██ ██ ██ 31 32 33       ██ ██ 21 22
//               ██ 32               ██ 31                     ██ 33               ██ 31                                           ██ 32
//                                                                                                      ██ ██ ██ 21 22 23
//        21) ██ 31                                                                                    19) ██ 32              20) ██ 11
//                                                                                                                                ██ ██ 21 22
//                                                                                                                                ██ 31
//
//   1  2  3  4     ██219   ■254
//   11 12 13 14
//   21 22 23 24
//   31 32 33 34
//   shape have place          place have shape
//  1) 11 21 22 32            1) 2                  21) 1 2 4 6 8 9 11 14 15 16 18 19 20
//  2) 1 11 21 31             2)                    22) 1 3 5 6 8 9 12 13 15 16 17 18 19 20
//  3) 22 23 32 33            3)                    23) 3 8 10 12 15 16 19
//  4) 11 21 31 32            4)                    24)
//  5) 12 22 31 32            11) 1 2 4 13 14 20    31) 2 4 5 7 9 10 11 12 14 16 17 20 21
//  6) 21 22 32 33            12) 5 8 9 13 14 18    32) 1 3 4 5 6 7 8 10 11 12 13 17 18 19
//  7) 31 32 33 34            13)                   33) 3 7 8 10 11 15 17
//  8) 12 21 22 23 32         14)                   34) 7
//  9) 12 21 22 31
// 10) 23 31 32 33
// 11) 21 31 32 33
// 12) 22 23 31 32
// 13) 11 12 22 32
// 14) 11 12 21 31
// 15) 21 22 23 33
// 16) 21 22 23 31
// 17) 22 31 32 33
// 18) 12 21 22 32
// 19) 21 22 23 32
// 20) 11 21 22 31
// 21) 31