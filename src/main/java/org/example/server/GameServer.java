package org.example.server;

import org.example.controller.PlayerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int PORT = 12345;
    private static final List<PlayerHandler> players = new ArrayList<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for players on port " + PORT);

            while (true) {
                Socket playerSocket = serverSocket.accept();
                System.out.println("Player connected from: " + playerSocket.getInetAddress().getHostAddress());

                PlayerHandler playerHandler = new PlayerHandler(playerSocket);
                players.add(playerHandler);

                // Tạo cặp khi có đủ hai người chơi
                if (players.size() % 2 == 0) {
                    PlayerHandler player1 = players.get(players.size() - 2);
                    PlayerHandler player2 = players.get(players.size() - 1);

                    player1.setOpponent(player2);
                    player2.setOpponent(player1);
                    System.out.println(player1.getPlayerName() + " " + player2.getPlayerName());
                    // Gửi thông tin đối thủ cho mỗi người chơi
                    player1.sendResponse("opponent: " + player2.getPlayerName());
                    player2.sendResponse("opponent: " + player1.getPlayerName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void handlePlayerDisconnect(PlayerHandler disconnectedPlayer) {
        players.remove(disconnectedPlayer);

        // Thông báo cho đối thủ nếu có
        if (disconnectedPlayer.getOpponent() != null) {
            PlayerHandler opponent = disconnectedPlayer.getOpponent();
            opponent.sendResponse("opponentDisconnected");
            opponent.setOpponent(null);
        }
    }
}