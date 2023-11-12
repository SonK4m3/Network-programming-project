package com.goreckia.game.plane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Tank {
    private Image[] tankImg;
    private BufferedImage ImageBuff;
    private Bomb[] bomb;
    private int curBomb;
    private int tankID;
    private int posiX;
    private int posiY;
    private int direction;
    private float velocityX;
    private float velocityY;
    private int width;
    private int height;

    public int getDirection() {
        return this.direction;
    }

    public Tank() {
        this.bomb = new Bomb[1000];
        this.curBomb = 0;
        this.posiX = -1;
        this.posiY = -1;
        this.direction = 1;
        this.velocityX = 0.05f;
        this.velocityY = 0.05f;
        this.width = 559;
        this.height = 473;
        while (this.posiX < 70 | this.posiY < 50 | this.posiY > this.height - 43 | this.posiX > this.width - 43) {
            this.posiX = (int) (Math.random() * this.width);
            this.posiY = 400;
        }
        this.loadImage(4);
    }

    public Tank(final int x, final int y, final int dir, final int id) {
        this.bomb = new Bomb[1000];
        this.curBomb = 0;
        this.posiX = -1;
        this.posiY = -1;
        this.direction = 1;
        this.velocityX = 0.05f;
        this.velocityY = 0.05f;
        this.width = 559;
        this.height = 473;
        this.posiX = x;
        this.posiY = y;
        this.tankID = id;
        this.direction = dir;
        this.loadImage(0);
    }

    public void loadImage(final int a) {
        this.tankImg = new Image[4];
        for (int i = a; i < this.tankImg.length + a; ++i) {
            this.tankImg[i - a] = new ImageIcon("Images/" + (i - a) + ".png").getImage();
        }
        this.ImageBuff = new BufferedImage(this.tankImg[this.direction - 1].getWidth(null), this.tankImg[this.direction - 1].getHeight(null), 1);
        this.ImageBuff.createGraphics().drawImage(this.tankImg[this.direction - 1], 0, 0, null);
    }

    public BufferedImage getBuffImage() {
        this.ImageBuff.createGraphics().drawImage(this.tankImg[this.direction - 1], 0, 0, null);
        System.out.println(this.direction);
        return this.ImageBuff;
    }

    public int getXposition() {
        return this.posiX;
    }

    public int getYposition() {
        return this.posiY;
    }

    public void setXpoistion(final int x) {
        this.posiX = x;
    }

    public void setYposition(final int y) {
        this.posiY = y;
    }

    public void update() {
        final int temp = (int) (this.posiX - this.velocityX * this.posiX);
        if (!this.checkCollision(temp, this.posiY) && temp < 70) {
            this.posiX = 70;
        } else if (!this.checkCollision(temp, this.posiY)) {
            this.posiX = temp;
        }
    }

    public void moveLeft() {
        this.velocityX = 0.03f;
        this.direction = 4;
        this.update();
    }

    public void moveRight() {
        this.velocityX = 0.03f;
        this.direction = 2;
        final int temp = (int) (this.posiX + this.velocityX * this.posiX);
        if (!this.checkCollision(temp, this.posiY) && temp > this.width - 43 + 20) {
            this.posiX = this.width - 43 + 20;
        } else if (!this.checkCollision(temp, this.posiY)) {
            this.posiX = temp;
        }
    }

    public void moveForward() {
        this.direction = 1;

        final int temp = (int) (this.posiY - this.velocityY * this.posiY);
        if (!this.checkCollision(this.posiX, temp) && temp < 50) {
            this.posiY = 50;
        } else if (!this.checkCollision(this.posiX, temp)) {
            this.posiY = temp;
        }
    }

    public void moveBackward() {
        this.direction = 3;
        final int temp = (int) (this.posiY + this.velocityY * this.posiY);
        if (!this.checkCollision(this.posiX, temp) && temp > this.height - 43 + 50) {
            this.posiY = this.height - 43 + 50;
        } else if (!this.checkCollision(this.posiX, temp)) {
            this.posiY = temp;
        }
    }

    public void notMoveForward() {
        final int temp = this.posiY - 0 * this.posiY;
        if (!this.checkCollision(this.posiX, temp) && temp < 50) {
            this.posiY = 50;
        } else if (!this.checkCollision(this.posiX, temp)) {
            this.posiY = temp;
        }
    }

    public void notMoveBackward() {
        final int temp = this.posiY + 0 * this.posiY;
        if (!this.checkCollision(this.posiX, temp) && temp > this.height - 43 + 50) {
            this.posiY = this.height - 43 + 50;
        } else if (!this.checkCollision(this.posiX, temp)) {
            this.posiY = temp;
        }
    }

    public void notMoveRight() {
        this.velocityX = 0.0f;
        final int temp = (int) (this.posiX + this.velocityX * this.posiX);
        if (!this.checkCollision(temp, this.posiY) && temp > this.width - 43 + 20) {
            this.posiX = this.width - 43 + 20;
        } else if (!this.checkCollision(temp, this.posiY)) {
            this.posiX = temp;
        }
    }

    public void notMoveLeft() {
        this.velocityX = 0.0f;
        final int temp = (int) (this.posiX - this.velocityX * this.posiX);
        if (!this.checkCollision(temp, this.posiY) && temp < 70) {
            this.posiX = 70;
        } else if (!this.checkCollision(temp, this.posiY)) {
            this.posiX = temp;
        }
    }

    public void shot() {
        (this.bomb[this.curBomb] = new Bomb(this.getXposition(), this.getYposition(), this.direction)).startBombThread(true);
        ++this.curBomb;
    }

    public Bomb[] getBomb() {
        return this.bomb;
    }

    public void setTankID(final int id) {
        this.tankID = id;
    }

    public int getTankID() {
        return this.tankID;
    }

    public void setDirection(final int dir) {
        this.ImageBuff = new BufferedImage(this.tankImg[dir - 1].getWidth(null), this.tankImg[dir - 1].getHeight(null), 1);
        this.ImageBuff.createGraphics().drawImage(this.tankImg[dir - 1], 0, 0, null);
        this.direction = dir;
    }

    public void Shot() {
        (this.bomb[this.curBomb] = new Bomb(this.getXposition(), this.getYposition(), this.direction)).startBombThread(false);
        ++this.curBomb;
    }

    public boolean checkCollision(final int xP, final int yP) {
        final ArrayList<Tank> clientTanks = (ArrayList<Tank>) GameBoardPanel.getClients();
        for (int i = 1; i < clientTanks.size(); ++i) {
            if (clientTanks.get(i) != null) {
                final int x = clientTanks.get(i).getXposition();
                final int y = clientTanks.get(i).getYposition();
                if (this.direction == 1) {
                    if (yP <= y + 43 && yP >= y && ((xP <= x + 43 && xP >= x) || (xP + 43 >= x && xP + 43 <= x + 43))) {
                        return true;
                    }
                } else if (this.direction == 2) {
                    if (xP + 43 >= x && xP + 43 <= x + 43 && ((yP <= y + 43 & yP >= y) || (yP + 43 >= y && yP + 43 <= y + 43))) {
                        return true;
                    }
                } else if (this.direction == 3) {
                    if (yP + 43 >= y && yP + 43 <= y + 43 && ((xP <= x + 43 && xP >= x) || (xP + 43 >= x && xP + 43 <= x + 43))) {
                        return true;
                    }
                } else if (this.direction == 4 && xP <= x + 43 && xP >= x && ((yP <= y + 43 && yP >= y) || (yP + 43 >= y && yP + 43 <= y + 43))) {
                    return true;
                }
            }
        }
        return false;
    }
}
