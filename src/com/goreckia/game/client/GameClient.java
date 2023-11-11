package com.goreckia.game.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public GameClient() {

    }

    public void connect() {
        String serverAddress = "localhost"; // Địa chỉ IP của máy chạy server
        int port = 12345;

        try {
            Socket socket = new Socket(serverAddress, port);
            System.out.println("Connected to server on port " + port);

            // Gửi tên người chơi đến server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String playerName = scanner.nextLine();
            out.println(playerName);

            // Đọc và hiển thị thông tin đợi ghép cặp
            String response = receiveMessage(socket);
            System.out.println(response);

            // Nếu đang chờ ghép cặp, tiếp tục đợi thông báo từ server
            if (response.equals("Waiting for opponent...")) {
                while (true) {
                    response = receiveMessage(socket);
                    System.out.println(response);

                    // Nếu đã ghép cặp thành công, hiển thị thông tin đối thủ
                    if (response.startsWith("opponent:")) {
                        String opponentName = response.substring(9);
                        System.out.println("Matched with opponent: " + opponentName);
                        break;
                    }
                }
            }

            // Chờ đọc dữ liệu từ server (ví dụ: tọa độ đối thủ)
            new Thread(() -> {
                Scanner serverIn = null;
                try {
                    serverIn = new Scanner(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                    assert serverIn != null;
                    if (!serverIn.hasNextLine()) break;
                    String serverResponse = serverIn.nextLine();
                    System.out.println("Received from server: " + serverResponse);
                }
            }).start();

            // Gửi dữ liệu đến server (ví dụ: tọa độ của người chơi)
            while (true) {
                System.out.print("Enter your move (type 'exit' to quit): ");
                String move = scanner.nextLine();
                out.println("move:" + move);

                if ("exit".equalsIgnoreCase(move)) {
                    break;
                }
            }

            // Đóng kết nối
            scanner.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String receiveMessage(Socket socket) throws IOException {
        Scanner in = new Scanner(socket.getInputStream());
        return in.nextLine();
    }
}
