package org.example.controller;

import org.example.server.GameServer;
import org.example.socket.Protocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerHandler implements Runnable, SocketDataOperation {
    private final Socket playerSocket;
    private PrintWriter out;
    private Scanner in;
    private PlayerHandler opponent;
    private String playerName;
    private Protocol protocolFromClient;
    private Protocol protocolToClient;

    public PlayerHandler(Socket playerSocket) {
        this.playerSocket = playerSocket;
        this.protocolFromClient = new Protocol();
        this.protocolToClient = new Protocol();
        try {
            this.out = new PrintWriter(playerSocket.getOutputStream(), true);
            this.in = new Scanner(playerSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOpponent(PlayerHandler opponent) {
        this.opponent = opponent;
    }
    public PlayerHandler getOpponent() {
        return this.opponent;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void run() {
        // Đọc tên người chơi từ client
        playerName = in.nextLine();
        out.println("Hello, " + playerName + "! Waiting for opponent...");

        // Xử lý dữ liệu từ client
        while (in.hasNextLine()) {
            String input = in.nextLine();
            if ("disconnect".equalsIgnoreCase(input)) {
                // Thông báo server khi người chơi disconnect
                GameServer.handlePlayerDisconnect(this);
                break;
            } else if (input.startsWith("move:")) {
                // Gửi tọa độ đến đối thủ
                opponent.sendResponse("opponentMove:" + input.substring(6));
            }
        }

        // Đóng kết nối khi client ngắt kết nối
        try {
            playerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean readMessage() {
        return null;
    }

    @Override
    public void sendResponse(String response) {
        out.println(response);
    }
}
