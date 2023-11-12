package com.goreckia.game.plane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
    private Socket clientSocket;
    private String hostName;
    private int serverPort;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Protocol protocol;
    private static Client client;

    private Client() throws IOException {
        this.protocol = new Protocol();
    }

    public void register(final String Ip, final int port, final int posX, final int posY) throws IOException {
        this.serverPort = port;
        this.hostName = Ip;
        this.clientSocket = new Socket(Ip, port);
        (this.writer = new DataOutputStream(this.clientSocket.getOutputStream())).writeUTF(this.protocol.RegisterPacket(posX, posY));
    }

    public void sendToServer(final String message) {
        if (message.equals("exit")) {
            System.exit(0);
        }
        else {
            try {
                final Socket s = new Socket(this.hostName, this.serverPort);
                System.out.println(message);
                (this.writer = new DataOutputStream(s.getOutputStream())).writeUTF(message);
            }
            catch (IOException ex) {}
        }
    }

    public Socket getSocket() {
        return this.clientSocket;
    }

    public String getIP() {
        return this.hostName;
    }

    public static Client getGameClient() {
        if (Client.client == null) {
            try {
                Client.client = new Client();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return Client.client;
    }

    public void closeAll() {
        try {
            this.reader.close();
            this.writer.close();
            this.clientSocket.close();
        }
        catch (IOException ex) {}
    }
}