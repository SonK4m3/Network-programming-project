package com.goreckia.game.plane;

public class Protocol {
    private String message;

    public Protocol() {
        this.message = "";
    }

    public String RegisterPacket(final int x, final int y) {
        return this.message = "Hello" + x + "," + y;
    }

    public String UpdatePacket(final int x, final int y, final int id, final int dir) {
        return this.message = "Update" + x + "," + y + "-" + dir + "|" + id;
    }

    public String ShotPacket(final int id) {
        return this.message = "Shot" + id;
    }

    public String RemoveClientPacket(final int id) {
        return this.message = "Remove" + id;
    }

    public String ExitMessagePacket(final int id) {
        return this.message = "Exit" + id;
    }

    public String ResetEnemy(final int id) {
        return this.message = "Reset" + id;
    }
}