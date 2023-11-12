package com.goreckia.game.plane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GameBoardPanel extends JPanel
{
    private Tank tank;
    private int width;
    private int height;
    private static ArrayList<Tank> tanks;
    private static ArrayList<PlaneEnemy> enemys;
    private boolean gameStatus;

    public GameBoardPanel(final Tank tank, final Client client, final boolean gameStatus) {
        this.width = 609;
        this.height = 523;
        this.tank = tank;
        this.gameStatus = gameStatus;
        this.setSize(this.width, this.height);
        this.setBounds(-50, 0, this.width, this.height);
        this.addKeyListener(new InputManager(tank));
        this.setFocusable(true);
        GameBoardPanel.tanks = new ArrayList<Tank>(100);
        GameBoardPanel.enemys = new ArrayList<PlaneEnemy>(50);
        for (int i = 0; i < 100; ++i) {
            GameBoardPanel.tanks.add(null);
        }
        for (int i = 0; i < 50; ++i) {
            GameBoardPanel.enemys.add(null);
        }
    }

    public void paintComponent(final Graphics gr) {
        super.paintComponent(gr);
        final Graphics2D g = (Graphics2D)gr;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(70, 50, this.getWidth() - 100, this.getHeight());
        g.drawImage(new ImageIcon("Images/bg.JPG").getImage(), 70, 50, null);
        g.setColor(Color.RED);
        g.setFont(new Font("Comic Sans MS", 1, 25));
        g.drawString("Tank Fire Multiple players Game", 255, 30);
        if (this.gameStatus) {
            g.drawImage(this.tank.getBuffImage(), this.tank.getXposition(), this.tank.getYposition(), this);
            for (int j = 0; j < 1000; ++j) {
                if (this.tank.getBomb()[j] != null && !this.tank.getBomb()[j].stop) {
                    g.drawImage(this.tank.getBomb()[j].getBomBufferdImg(), this.tank.getBomb()[j].getPosiX(), this.tank.getBomb()[j].getPosiY(), this);
                }
            }
            for (int k = 1; k < GameBoardPanel.enemys.size(); ++k) {
                if (GameBoardPanel.enemys.get(k) != null) {
                    g.drawImage(GameBoardPanel.enemys.get(k).getBombBuffImage(), GameBoardPanel.enemys.get(k).getPosiX(), GameBoardPanel.enemys.get(k).getPosiY(), this);
                }
            }
            for (int i = 1; i < GameBoardPanel.tanks.size(); ++i) {
                if (GameBoardPanel.tanks.get(i) != null) {
                    g.drawImage(GameBoardPanel.tanks.get(i).getBuffImage(), GameBoardPanel.tanks.get(i).getXposition(), GameBoardPanel.tanks.get(i).getYposition(), this);
                }
                for (int l = 0; l < 1000; ++l) {
                    if (GameBoardPanel.tanks.get(i) != null && GameBoardPanel.tanks.get(i).getBomb()[l] != null && !GameBoardPanel.tanks.get(i).getBomb()[l].stop) {
                        g.drawImage(GameBoardPanel.tanks.get(i).getBomb()[l].getBomBufferdImg(), GameBoardPanel.tanks.get(i).getBomb()[l].getPosiX(), GameBoardPanel.tanks.get(i).getBomb()[l].getPosiY(), this);
                    }
                }
            }
        }
        this.repaint();
    }

    public void registerNewTank(final Tank newTank) {
        GameBoardPanel.tanks.set(newTank.getTankID(), newTank);
    }

    public void removeTank(final int tankID) {
        GameBoardPanel.tanks.set(tankID, null);
    }

    public Tank getTank(final int id) {
        return GameBoardPanel.tanks.get(id);
    }

    public void setGameStatus(final boolean status) {
        this.gameStatus = status;
    }

    public static ArrayList<Tank> getClients() {
        return GameBoardPanel.tanks;
    }

    public static ArrayList<PlaneEnemy> getEnemys() {
        return GameBoardPanel.enemys;
    }
}