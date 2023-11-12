package com.goreckia.game.states;

import com.goreckia.game.main.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ParingState extends State {

    private String characterList = "";
    private final String[] options = {"FIND", "BACK"};
    private final String[] pairingOptions = {"CANCEL"};

    private int currentOption = -1;
    private int ping = 0;
    private int ping2 = 0;

    private boolean isPairing = false;

    public ParingState(GameStateManager gms) {
        super(gms);

    }

    @Override
    public void tick() {
        ping++;
        ping2++;
    }

    @Override
    public void keyPressed(int k) {
        if (isPairing) {

            return;
        }

        switch (k) {
            case KeyEvent.VK_SPACE:
                characterList += ' ';
                break;
            case KeyEvent.VK_ENTER:
                if (currentOption == -1 && characterList.length() > 0) {
                    currentOption = 0;
                } else {
                    switch (currentOption) {
                        case 0 -> isPairing = true; // PLAYING
                        case 1 -> gsm.setStateTo(GameStateManager.MAIN_MENU); // ONLINE
                    }
                }
                break;
            case KeyEvent.VK_UP:
                if (currentOption > 0 && characterList.length() > 0)
                    currentOption--;
                break;
            case KeyEvent.VK_DOWN:
                if (currentOption < options.length - 1 && characterList.length() > 0)
                    currentOption++;

                break;
            case KeyEvent.VK_BACK_SPACE:
                if (characterList.length() > 0) {
                    characterList = characterList.substring(0, characterList.length() - 1);
                }
                break;
            case KeyEvent.VK_0
                    , KeyEvent.VK_1
                    , KeyEvent.VK_2
                    , KeyEvent.VK_3
                    , KeyEvent.VK_4
                    , KeyEvent.VK_5
                    , KeyEvent.VK_6
                    , KeyEvent.VK_7
                    , KeyEvent.VK_8
                    , KeyEvent.VK_9, KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_U, KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z:
                if (characterList.length() <= 10)
                    characterList += ((char) k);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Constants.PANEL_SIZE, Constants.PANEL_SIZE);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);

        int fontSize = Constants.PANEL_SIZE / 18;
        Font font = new Font("Impact", Font.BOLD | Font.ITALIC, (int) (1.5 * fontSize));
        drawCenteredString(g, "Enter your name:", new Rectangle(0, 75, Constants.PANEL_SIZE, 0), font);
        String name = ".";
        if (ping >= 20 && ping <= 40) {
            name = characterList + name;
            if (ping == 40) ping = 0;
        } else {
            name = characterList;
        }
        drawCenteredString(g, name, new Rectangle(0, 75 + fontSize * 2, Constants.PANEL_SIZE, 0), font);

        font = new Font("Impact", Font.PLAIN, fontSize);
        for (int i = 0; i < options.length; i++) {
            if (i == currentOption)
                g.setColor(new Color(200, 30, 30));
            else
                g.setColor(Color.WHITE);
            drawCenteredString(g, options[i], new Rectangle(0, Constants.PANEL_SIZE / 2 + fontSize * i, Constants.PANEL_SIZE, 0), font);
        }

        if (isPairing) {
            g.setColor(Color.GRAY);
            g.fillRect(Constants.PANEL_SIZE / 4, Constants.PANEL_SIZE / 4, Constants.PANEL_SIZE / 2, Constants.PANEL_SIZE / 2);
            String findnNoti = ".";
            if (ping2 <= 20) {
                findnNoti = "Finding opponent .";
            } else if (ping2 <= 40) {
                findnNoti = "Finding opponent ..";
            } else if (ping2 <= 60) {
                findnNoti = "Finding opponent ...";
                if (ping2 == 60) ping2 = 0;
            }
            g.setColor(Color.WHITE);
            drawCenteredString(g, findnNoti, new Rectangle(0, Constants.PANEL_SIZE / 4 + 75 + fontSize * 2, Constants.PANEL_SIZE, 0), font);
        }
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
