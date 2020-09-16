import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Kosmos extends JPanel {
    int x = 10;
    int y = 10;
    int pkt = 0;
    ArrayList<Kosmos.Meteoryt> meteoryty = new ArrayList();

    public boolean zderzenie(int mouseX, int mouseY) {
        Kosmos.Meteoryt[] meteorytyTab = new Kosmos.Meteoryt[this.meteoryty.size()];
        this.meteoryty.toArray(meteorytyTab);
        Kosmos.Meteoryt[] var7 = meteorytyTab;
        int var6 = meteorytyTab.length;
        for(int var5 = 0; var5 < var6; ++var5) {
            Kosmos.Meteoryt meteor = var7[var5];
            if (!meteor.czyWidoczny()) {
                this.meteoryty.remove(meteor);
            }
            if (meteor.x <= mouseX && meteor.y <= mouseY && meteor.x + meteor.size >= mouseX && meteor.y + meteor.size >= mouseY) {
                return true;
            }
        }
        return false;
    }

    private void przegrana() {
        JOptionPane.showMessageDialog((Component)null, String.format("Zdobyłeś : %d punkty", this.pkt));
        this.pkt = 0;
        this.meteoryty.clear();
        this.repaint();
    }

    public Kosmos() {
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                if (Kosmos.this.zderzenie(e.getX(), e.getY())) {
                    Kosmos.this.przegrana();
                }
                while(Kosmos.this.x != e.getX() || Kosmos.this.y != e.getY()) {
                    Kosmos var10000 = Kosmos.this;
                    var10000.x += e.getX() - Kosmos.this.x;
                    var10000 = Kosmos.this;
                    var10000.y += e.getY() - Kosmos.this.y;
                    Kosmos.this.repaint();
                    try {
                        ++Kosmos.this.pkt;
                        Thread.sleep(10L);
                    } catch (InterruptedException var3) {
                        var3.printStackTrace();
                    }
                }
            }

            public void mouseDragged(MouseEvent e) {
            }
        });
        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException var2) {
                        var2.printStackTrace();
                    }
                    Kosmos.this.meteoryty.add(Kosmos.this.new Meteoryt());
                }
            }
        })).start();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Space\\src\\main\\resources\\Kosmos\\tlo.png")),0,0,null);
            g.drawImage(ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Space\\src\\main\\resources\\Kosmos\\ufo.png")),x-25,y-25,null);
        } catch (IOException var9) {
            var9.printStackTrace();
        }
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", 1, 20));
        g.drawString(String.valueOf(this.pkt), 950, 30);
        Kosmos.Meteoryt[] meteorytyTab = new Kosmos.Meteoryt[this.meteoryty.size()];
        this.meteoryty.toArray(meteorytyTab);
        Kosmos.Meteoryt[] var6 = meteorytyTab;
        int var5 = meteorytyTab.length;
        for(int var4 = 0; var4 < var5; ++var4) {
            Kosmos.Meteoryt meteor = var6[var4];
            try {
                meteor.ruch();
                g.drawImage(resize(ImageIO.read(new File("C:\\Users\\Adrian\\Desktop\\Space\\src\\main\\resources\\Kosmos\\meteor.png")), meteor.size,meteor.size),meteor.x,meteor.y,null);
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, 4);
        BufferedImage skalowanie = new BufferedImage(width, height, 2);
        Graphics2D g2d = skalowanie.createGraphics();
        g2d.drawImage(tmp, 0, 0, (ImageObserver)null);
        g2d.dispose();
        return skalowanie;
    }

    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    public static void main(String[] args) {
        JFrame okno = new JFrame("Kosmos");
        Kosmos main = new Kosmos();
        okno.add(main);
        okno.setDefaultCloseOperation(3);
        okno.setVisible(true);
        okno.pack();
    }

    class Meteoryt {
        int x;
        int y;
        int size;
        int dx;
        int dy;

        public Meteoryt() {
            Random r = new Random();
            this.size = (r.nextInt(10) + 1) * 20;
            int kierunek = r.nextInt(4);
            if (kierunek == 0) {
                this.x = 0;
                this.y = r.nextInt(1000);
                this.dx = r.nextInt(15) + 1;
                this.dy = r.nextInt(15) - 7;
            } else if (kierunek == 1) {
                this.x = r.nextInt(1000);
                this.y = 0;
                this.dx = r.nextInt(15) - 7;
                this.dy = r.nextInt(15) + 1;
            } else if (kierunek == 2) {
                this.x = r.nextInt(1000);
                this.y = 1000;
                this.dx = r.nextInt(15) - 7;
                this.dy = r.nextInt(15) - 7;
            } else if (kierunek == 3) {
                this.x = 1000;
                this.y = r.nextInt(1000);
                this.dx = r.nextInt(15) - 7;
                this.dy = r.nextInt(15) - 7;
            }
        }

        public boolean czyWidoczny() {
            return this.x + this.size >= 0 && this.y + this.size >= 0 && this.x < 1000 && this.y < 1000;
        }

        public void ruch() {
            this.x += this.dx;
            this.y += this.dy;
        }
    }
}

