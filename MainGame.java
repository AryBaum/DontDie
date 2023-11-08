
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MainGame extends JFrame implements KeyListener, ActionListener {
    public static void main(String[] args) {
        new MainGame().setup();
    }

    enum GS {
        PLAYING,
        TITLE,
        HIT,
        DODGED
    }

    GS gamestate = GS.PLAYING;
    final static int SCRW = 1400, SCRH = 800;
    DrawingPanel dp = new DrawingPanel();
    Rectangle person = new Rectangle(new Point(80, 80));
    Color personColor = Color.BLUE;
    int laneChosen = 0;
    int safe = 0;
    boolean driving = false;
    int[] standLocations = {217, 367, 517, 667};
    Timer t = new Timer(1, this);
    int carx = SCRW;
    int hit = 0;
    


    void setup() {
        this.add(dp);
        this.addKeyListener(this);
        
        this.pack();
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        t.start();
    }

    void drive() {
        if(carx == 0) {
            if(laneChosen != safe) hit();
            else dodged();
            
            driving = false;
            carx = SCRW;
        }

        if(driving) carx -= 10;
    }

    void hit() {
        hit++;
        gamestate = GS.HIT;
    }

    void dodged() {
        gamestate = GS.DODGED;
    }

    class DrawingPanel extends JPanel {
        DrawingPanel() {
            this.setPreferredSize(new Dimension(SCRW, SCRH));
            this.setBackground(new Color(103, 191, 96));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(gamestate == GS.TITLE) {

            }

            
            if(gamestate == GS.PLAYING || gamestate == GS.HIT || gamestate == GS.DODGED) {
                for(int i = 0; i < 4; i++) {
                    g.setColor(Color.black);
                    g.fillRect(0, i*150+200, SCRW, 125);

                    g.setColor(Color.YELLOW);
                    g.fillRect(100, i*150+257, 100, 10);
                    g.fillRect(400, i*150+257, 100, 10);
                    g.fillRect(700,i*150+257, 100, 10);
                    g.fillRect(1000,i*150+257, 100, 10);
                    
                    
                }
                g.setColor(personColor);
                g.fillOval(265, standLocations[laneChosen], person.x, person.y);

                for(int i = 0; i < 4; i ++) {
                    if(driving && i != safe) {
                        System.out.println("AJLKES");
                        g.setColor(Color.RED);
                        g.fillRect(carx, i*150+207, 150, 110);
                    }
                }
            }
            
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
        
    }

    public void keyPressed(KeyEvent e) {
        if(gamestate == GS.PLAYING) {
            if(e.getKeyCode() == 'W') {
                if(laneChosen > 0) {
                    laneChosen--;
                }
            }
            if(e.getKeyCode() == 'S') {
                if(laneChosen < 3) {
                    laneChosen++;
                }
            }
        

            if(e.getKeyCode() == ' ') {
                safe = (int) (Math.random() * 4);
                System.out.print(safe);

                driving = true;
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        drive();
        
        repaint();
    }

    
}
