
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

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
        TITLE,
        PLAYING,
        HIT,
        WIN,
        END
    }

    GS gamestate = GS.PLAYING;
    final static int SCRW = 1400, SCRH = 800;
    DrawingPanel dp = new DrawingPanel();
    Rectangle person = new Rectangle(new Point(80, 80));
    Color personColor = Color.BLUE;
    int won = 0, trial = 0, laneChosen = 0, safe = 0;
    boolean driving = false;
    int[] standLocations = {217, 367, 517, 667};
    Timer t = new Timer(1, this);
    int carx = SCRW;
    Font big = new Font("Monospaced", Font.BOLD, 150), small = new Font("Monospaced", Font.BOLD, 50);

    


    void setup() {
        this.add(dp);
        this.addKeyListener(this);
        this.setTitle("Don't Die");
        
        this.pack();
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        t.start();
    }

    void drive() {
        checkEnd();
        if(carx == 0) {
            trial++;
            driving = false;
            carx = SCRW;
            if(laneChosen != safe) gamestate = GS.HIT;
            else win();
            
        }

        if(driving) carx -= 10;
    }

    void win() {
       won++;
       gamestate = GS.WIN;
    }

   

    int getEarned() {
        int earned = 0;

        if(won == 0) earned = 0;
        else if(won == 1) earned = 2;
        else if(won == 2) earned = 4;
        else earned = 10;

        return earned;
    }

    void checkEnd() {
        if(trial == 3) {
            gamestate = GS.END;
            return;
        }
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

            
            if(gamestate == GS.PLAYING || gamestate == GS.HIT || gamestate == GS.WIN) {
                g.setFont(big);
                g.setColor(Color.black);
                g.drawString("Pick a lane >:(", 50, 150);

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
                        g.setColor(Color.RED);
                        g.fillRect(carx, i*150+207, 150, 110);
                    }
                }
            }

            if(gamestate == GS.HIT || gamestate == GS.WIN) {
                String outcome = "";
                if(gamestate == GS.HIT) outcome = "YOU DIED";
                else outcome = "YOU WON!";


                g.fillRoundRect(100, 100, 1200, 600, 100, 100);
                g.setColor(Color.black);
                g.setFont(big);
                g.drawString(outcome, 300,300);
                g.setFont(small);
                g.drawString("Trial " + trial, 300, 400);
                g.drawString("Penny's accumulated: ", 300, 450);
                g.setFont(big);
                g.drawString("" + getEarned(), 300, 625);
            }
            
            if(gamestate == GS.END) {
                g.setFont(big);
                g.drawString("GAME OVER!", 300,350);
                g.setFont(small);
                g.drawString("Penny's accumulated: ", 300, 400);
                g.setFont(big);
                g.drawString("" + getEarned(), 300, 525);
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if(gamestate == GS.PLAYING && !driving) {
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
                driving = true;
            }
        }

        if(gamestate == GS.WIN || gamestate == GS.HIT || gamestate == GS.TITLE) {
            if(e.getKeyCode() == ' ') {
                gamestate = GS.PLAYING;
            }
        }

        if(gamestate == GS.END) {
            if(e.getKeyCode() == ' ') {
                trial = 0;
                won = 0;
                laneChosen = 0;
                gamestate = GS.PLAYING;
            }

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        drive();
        repaint();
    }

    
}
