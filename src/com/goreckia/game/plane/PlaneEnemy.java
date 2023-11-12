package com.goreckia.game.plane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

public class PlaneEnemy
{
	private Image enemyImg;
	private BufferedImage enemyBuffImage;
	Random generator;
	private int xPosi;
	private int yPosi;
	private int ID;
	public boolean stop;
	private float velocityX;
	private float velocityY;

	public PlaneEnemy(final int x, final int y) {
		this.generator = new Random();
		this.stop = false;
		this.velocityX = 0.05f;
		this.velocityY = 0.05f;
		final SimpleSoundPlayer sound_boom = new SimpleSoundPlayer("Sounds/boom.wav");
		final InputStream stream_boom = new ByteArrayInputStream(sound_boom.getSamples());
		this.xPosi = this.generator.nextInt(12) * 50;
		this.yPosi = 300;
		this.ID = y;
		this.stop = false;
		this.enemyImg = new ImageIcon("Images/enemyplane.png").getImage();
		this.enemyBuffImage = new BufferedImage(this.enemyImg.getWidth(null), this.enemyImg.getHeight(null), 1);
		this.enemyBuffImage.createGraphics().drawImage(this.enemyImg, 0, 0, null);
		final Thread t = new Thread((Runnable)new PlaneEnemy.PlaneEnemyRun(sound_boom, stream_boom));
		t.start();
	}

	public int getPosiX() {
		return this.xPosi;
	}

	public int getPosiY() {
		return this.yPosi;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(final int id) {
		this.ID = id;
	}

	public void setPosiX(final int x) {
		this.xPosi = x;
	}

	public void setPosiY(final int y) {
		this.yPosi = y;
	}

	public BufferedImage getBomBufferdImg() {
		return this.enemyBuffImage;
	}

	public BufferedImage getBombBuffImage() {
		return this.enemyBuffImage;
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

	class PlaneEnemyRun implements Runnable {
		private final SimpleSoundPlayer sound_boom;
		private final InputStream stream_boom;

		public PlaneEnemyRun(SimpleSoundPlayer sound_boom, InputStream stream_boom) {
			this.sound_boom = sound_boom;
			this.stream_boom = stream_boom;
		}

		@Override
		public void run() {
			this.sound_boom.play(this.stream_boom);
		}
	}
}