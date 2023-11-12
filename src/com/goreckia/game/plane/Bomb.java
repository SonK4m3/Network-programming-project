package com.goreckia.game.plane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Bomb
{
    private Image bombImg;
    private BufferedImage bombBuffImage;
    private int xPosi;
    private int yPosi;
    private int direction;
    public boolean stop;
    private float velocityX;
    private float velocityY;

    public Bomb(final int x, final int y, final int direction) {
        this.stop = false;
        this.velocityX = 0.05f;
        this.velocityY = 0.05f;
        final SimpleSoundPlayer sound_boom = new SimpleSoundPlayer("Sounds/boom.wav");
        final InputStream stream_boom = new ByteArrayInputStream(sound_boom.getSamples());
        this.xPosi = x;
        this.yPosi = y;
        this.direction = direction;
        this.stop = false;
        this.bombImg = new ImageIcon("Images/bomb.PNG").getImage();
        this.bombBuffImage = new BufferedImage(this.bombImg.getWidth(null), this.bombImg.getHeight(null), 1);
        this.bombBuffImage.createGraphics().drawImage(this.bombImg, 0, 0, null);
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                sound_boom.play(stream_boom);
            }
        });
        t.start();
    }

    public int getPosiX() {
        return this.xPosi;
    }

    public int getPosiY() {
        return this.yPosi;
    }

    public void setPosiX(final int x) {
        this.xPosi = x;
    }

    public void setPosiY(final int y) {
        this.yPosi = y;
    }

    public BufferedImage getBomBufferdImg() {
        return this.bombBuffImage;
    }

    public BufferedImage getBombBuffImage() {
        return this.bombBuffImage;
    }

    public boolean checkCollision() {
        final ArrayList<Tank> clientTanks = (ArrayList<Tank>)GameBoardPanel.getClients();
        for (int i = 1; i < clientTanks.size(); ++i) {
            if (clientTanks.get(i) != null) {
                final int x = clientTanks.get(i).getXposition();
                final int y = clientTanks.get(i).getYposition();
                if (this.yPosi >= y && this.yPosi <= y + 43 && this.xPosi >= x && this.xPosi <= x + 43) {
                    ClientGUI.setScore(50);
                    ClientGUI.gameStatusPanel.repaint();
                    try {
                        Thread.sleep(200L);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    if (clientTanks.get(i) != null) {
                        Client.getGameClient().sendToServer(new Protocol().RemoveClientPacket(clientTanks.get(i).getTankID()));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void startBombThread(final boolean chekCollision) {
        new Bomb.BombShotThread(chekCollision).start();
    }

    private class BombShotThread extends Thread
    {
        boolean checkCollis;
        public BombShotThread(final boolean chCollision) {
            this.checkCollis = chCollision;
        }

        @Override
        public void run() {
            if (this.checkCollis) {
                if (Bomb.this.direction == 1) {
                    final Bomb this$0 = Bomb.this;
                    this$0.xPosi += 17;
                    while (Bomb.this.yPosi > 50) {
                        Bomb.this.yPosi -= (int)(Bomb.this.yPosi * Bomb.this.velocityY);
                        if (Bomb.this.checkCollision()) {
                            break;
                        }
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 2) {
                    final Bomb this$2 = Bomb.this;
                    this$2.yPosi += 17;
                    final Bomb this$3 = Bomb.this;
                    this$3.xPosi += 30;
                    while (Bomb.this.xPosi < 564) {
                        Bomb.this.xPosi += (int)(Bomb.this.xPosi * Bomb.this.velocityX);
                        if (Bomb.this.checkCollision()) {
                            break;
                        }
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 3) {
                    final Bomb this$4 = Bomb.this;
                    this$4.yPosi += 30;
                    final Bomb this$5 = Bomb.this;
                    this$5.xPosi += 20;
                    while (Bomb.this.yPosi < 505) {
                        Bomb.this.yPosi += (int)(Bomb.this.yPosi * Bomb.this.velocityY);
                        if (Bomb.this.checkCollision()) {
                            break;
                        }
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 4) {
                    final Bomb this$6 = Bomb.this;
                    this$6.yPosi += 21;
                    while (Bomb.this.xPosi > 70) {
                        Bomb.this.xPosi -= (int)(Bomb.this.xPosi * Bomb.this.velocityX);
                        if (Bomb.this.checkCollision()) {
                            break;
                        }
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                Bomb.this.stop = true;
            }
            else {
                if (Bomb.this.direction == 1) {
                    final Bomb this$7 = Bomb.this;
                    this$7.xPosi += 17;
                    while (Bomb.this.yPosi > 50) {
                        Bomb.this.yPosi -= (int)(Bomb.this.yPosi * Bomb.this.velocityY);
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 2) {
                    final Bomb this$8 = Bomb.this;
                    this$8.yPosi += 17;
                    final Bomb this$9 = Bomb.this;
                    this$9.xPosi += 30;
                    while (Bomb.this.xPosi < 564) {
                        Bomb.this.xPosi += (int)(Bomb.this.xPosi * Bomb.this.velocityX);
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 3) {
                    final Bomb this$10 = Bomb.this;
                    this$10.yPosi += 30;
                    final Bomb this$11 = Bomb.this;
                    this$11.xPosi += 20;
                    while (Bomb.this.yPosi < 505) {
                        Bomb.this.yPosi += (int)(Bomb.this.yPosi * Bomb.this.velocityY);
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (Bomb.this.direction == 4) {
                    final Bomb this$12 = Bomb.this;
                    this$12.yPosi += 21;
                    while (Bomb.this.xPosi > 70) {
                        Bomb.this.xPosi -= (int)(Bomb.this.xPosi * Bomb.this.velocityX);
                        try {
                            Thread.sleep(40L);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                Bomb.this.stop = true;
            }
        }
    }
}
