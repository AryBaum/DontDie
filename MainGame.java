
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    GS gamestate = GS.TITLE;
    final static int SCRW = 1300, SCRH = 800;
    DrawingPanel dp = new DrawingPanel();
    Rectangle person = new Rectangle(new Point(80, 80));
    Color personColor = Color.BLUE;
    Color pink = new Color(242, 196, 229);
    Color blue = new Color(167, 199, 231);
    Color darkBlue = new Color(61, 66, 107);
    int won = 0, trial = 0, laneChosen = 0, safe = 0;
    boolean driving = false;
    int[] standLocations = {217, 367, 517, 667};
    Timer t = new Timer(1, this);
    int carx = SCRW;
    Font big = new Font("Monospaced", Font.BOLD, 140), small = new Font("Monospaced", Font.BOLD, 30), bigger = new Font("Monospaced", Font.BOLD, 300);

    


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
            Graphics2D g2 = (Graphics2D) g;


            if(gamestate == GS.TITLE) {
                g2.setFont(big);
                g2.drawString("Don't Die!!", 200, 150);
                g2.setFont(small);
                g2.drawString("Cost of play : 2 Pennies", 400, 200);

                g2.drawString("Goal : Choose the empty lane", 50, 300);
                g2.drawString("Choose a lane with 'w' and 's'", 50, 350);
                g2.drawString("Hit space to start cars", 50, 400);
                
                
                g2.drawString("---- Winnings ----", 700, 350);
                g2.drawString("0/3 Wins - 0 Pennies", 650, 400);
                g2.drawString("1/3 Wins - 2 Pennies", 650, 450);
                g2.drawString("2/3 Wins - 4 Pennies", 650, 500);
                g2.drawString("3/3 Wins - 10 Pennies", 650, 550);


            }

            
            if(gamestate == GS.PLAYING || gamestate == GS.HIT || gamestate == GS.WIN) {
                g2.setFont(big);
                g2.setColor(Color.white);
                g2.drawString("Pick a lane >:(", 10, 150);

                for(int i = 0; i < 4; i++) {
                    g2.setColor(Color.black);
                    g2.fillRect(0, i*150+200, SCRW, 125);

                    g2.setColor(Color.YELLOW);
                    g2.fillRect(100, i*150+257, 100, 10);
                    g2.fillRect(400, i*150+257, 100, 10);
                    g2.fillRect(700,i*150+257, 100, 10);
                    g2.fillRect(1000,i*150+257, 100, 10);
                    
                    
                }
                g2.setColor(pink);
                g2.fillOval(265, standLocations[laneChosen], person.x, person.y);

                for(int i = 0; i < 4; i ++) {
                    if(driving && i != safe) {
                        g2.setColor(blue);
                        g2.fillRect(carx, i*150+207, 150, 110);
                    }
                }
            }

            if(gamestate == GS.HIT || gamestate == GS.WIN) {
                String outcome = "";
                if(gamestate == GS.HIT) outcome = "YOU DIED";
                else outcome = "YOU WON!";

                g2.setColor(blue);
                g2.fillRoundRect(100, 100, 1100, 600, 100, 100);
                g2.setColor(darkBlue);
                g2.setStroke(new BasicStroke(5));
                g2.drawRoundRect(100, 100, 1100, 600, 100, 100);

                g2.setColor(Color.white);
                g2.setFont(big);
                g2.drawString(outcome, 300,300);
                g2.setFont(small);
                g2.setColor(Color.white);
                // g.drawString("Trial " + trial, 300, 400);
                g2.drawString("Pennies", 200, 360);
                g2.drawString("accumulated:", 200, 395);
                g2.setFont(bigger);
                g2.setColor(pink);
                g2.drawString("" + getEarned(), 200, 610);

                g2.fillRoundRect(700, 400, 400, 200, 100, 100);


            }
            
            if(gamestate == GS.END) {
                g2.setFont(big);
                g2.drawString("GAME OVER!", 300,350);
                g2.setFont(small);
                g2.drawString("Pennies accumulated: ", 300, 400);
                g2.setFont(big);
                g2.drawString("" + getEarned(), 300, 525);
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
