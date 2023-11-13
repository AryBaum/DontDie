
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
    Color darkPink = new Color(209, 148, 192);
    int won = 0, trial = 0, laneChosen = 0, safe = 0;
    boolean driving = false;
    int[] standLocations = {217, 367, 517, 667};
    Timer t = new Timer(1, this);
    int carx = SCRW;
    Font big = new Font("Monospaced", Font.BOLD, 140), small = new Font("Monospaced", Font.BOLD, 50), smaller = new Font("Monospaced", Font.BOLD, 30), bigger = new Font("Monospaced", Font.BOLD, 300);
    
    String name = "";

    Winner[] leaderboardWinners = {new Winner(), new Winner(), new Winner(), new Winner(), new Winner()};

    boolean winner = false;


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
            updateLeaderboard();
            t.stop();
            return;
        }
    }

    void updateLeaderboard() {
        Winner temp = new Winner();
        for(int i = 0; i < leaderboardWinners.length; i++) {
               
            Winner w = leaderboardWinners[i];
            if(getEarned() > w.getScore() && !winner) {
                temp = leaderboardWinners[i];
                leaderboardWinners[i] = new Winner(name, getEarned());
                winner = true;
                
            }
            else if(winner) {
                leaderboardWinners[i] = temp;
                temp = w;
            }
            
        }
    }

    void getPlayerName() {
        if(name == "") {
            name = JOptionPane.showInputDialog("What your name? ");
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
                g2.setFont(smaller);
                g2.drawString("Cost of play : 2 Pennies", 400, 200);

                g2.drawString("Goal : Choose the empty lane",100, 300);
                g2.drawString("Four lanes three cars", 100, 350);
                g2.drawString("Choose a lane with 'w' and 's'",100, 400);
                g2.drawString("Hit space to start cars",100, 450);
                g2.drawString("You have 3 trials", 100, 500);
                
                
                g2.drawString("---- Winnings ----", 800, 300);
                g2.drawString("0/3 Wins - 0 Pennies", 775, 350);
                g2.drawString("1/3 Wins - 2 Pennies", 775, 400);
                g2.drawString("2/3 Wins - 4 Pennies", 775, 450);
                g2.drawString("3/3 Wins - 10 Pennies", 775, 500);

                g2.fillRoundRect(300, 550, 700, 200, 100, 100);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(5));
                g2.drawRoundRect(300, 550, 700, 200, 100, 100);
                g2.setFont(big);
                g2.drawString("BEGIN!", 425, 690);
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
                g2.setFont(smaller);
                g2.setColor(Color.white);
                g2.drawString("Pennies", 200, 360);
                g2.drawString("accumulated:", 200, 395);

                g2.setFont(bigger);
                g2.setColor(pink);
                g2.drawString("" + getEarned(), 200, 610);
                g2.fillRoundRect(700, 400, 400, 200, 100, 100);
                g2.setColor(darkPink);
                g2.drawRoundRect(700, 400, 400, 200, 100, 100);

                g2.setFont(small);
                g2.drawString("Next: ", 790, 480);
                g2.drawString("Trial " + trial, 790, 540);
            }
            
            if(gamestate == GS.END) {
                g2.setFont(big);
                g2.drawString("GAME OVER!", 250,150);

                g2.setFont(small);
                g2.drawString("Pennies Earned: ", 150, 250);
                g2.setFont(big);
                g2.drawString("" + getEarned(), 150, 375);
                
                g2.setFont(small);
                g2.drawString("Leaderboar", 750, 250);
                
                g2.setFont(smaller);
                for(int i = 0; i < leaderboardWinners.length; i++) {
                    g2.drawString("" + leaderboardWinners[i].getName(), 700, i*50+300);
                    g2.drawString("" + leaderboardWinners[i].getScore() + " Pennies", 1000, i*50+300);

                }
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
                gamestate = GS.TITLE;
                name = "";
                t.start();
                winner = false;
            }

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        drive();
        repaint();
        getPlayerName();
    }

    
}
