package com.goreckia.game.plane;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;

public class InputManager implements KeyListener {
    public static int KEY_PRESSED;
    public static int KEY_RELEASED;
    private Tank tank;
    private Client client;
    private int shootinDelay = 4;

    static {
        InputManager.KEY_PRESSED = 0;
        InputManager.KEY_RELEASED = 1;
    }

    public InputManager(final Tank tank) {
        this.client = Client.getGameClient();
        this.tank = tank;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        this.KEY_ACTION(e, InputManager.KEY_RELEASED);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        this.KEY_ACTION(e, InputManager.KEY_PRESSED);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        this.KEY_ACTION(e, InputManager.KEY_RELEASED);
    }

    public void KEY_ACTION(final KeyEvent e, final int Event) {
        if (e.getKeyCode() == 65 && Event == InputManager.KEY_PRESSED) {
            this.tank.moveLeft();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        } else if (e.getKeyCode() == 65 && Event == InputManager.KEY_RELEASED) {
            this.tank.notMoveLeft();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        }
        if (e.getKeyCode() == 68 && Event == InputManager.KEY_PRESSED) {
            this.tank.moveRight();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        } else if (e.getKeyCode() == 68 && Event == InputManager.KEY_RELEASED) {
            this.tank.notMoveRight();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        }
        if (e.getKeyCode() == 87 && Event == InputManager.KEY_PRESSED) {
            this.tank.moveForward();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        } else if (e.getKeyCode() == 87 && Event == InputManager.KEY_RELEASED) {
            this.tank.notMoveForward();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        }
        if (e.getKeyCode() == 83 && Event == InputManager.KEY_PRESSED) {
            this.tank.moveBackward();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        } else if (e.getKeyCode() == 83 && Event == InputManager.KEY_RELEASED) {
            this.tank.notMoveBackward();
            this.client.sendToServer(new Protocol().UpdatePacket(this.tank.getXposition(), this.tank.getYposition(), this.tank.getTankID(), this.tank.getDirection()));
        }
        if (Event == InputManager.KEY_PRESSED && e.getKeyCode() == 74) {
            shootinDelay++;
            if (shootinDelay == 5) {
                this.client.sendToServer(new Protocol().ShotPacket(this.tank.getTankID()));
                this.tank.shot();
                shootinDelay = 0;
            }
        }
    }
}
