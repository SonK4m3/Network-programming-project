package com.goreckia.game.states;

import com.goreckia.game.client.GameClient;

import java.awt.*;

public abstract class State {
    public GameStateManager gsm;

    public GameClient client;

    public State(GameStateManager gsm) {
        this.gsm = gsm;
//        this.client = new GameClient();
    }

    public abstract void tick();

    public abstract void keyPressed(int k);

    public abstract void keyReleased(int k);

    public abstract void draw(Graphics g);

}
