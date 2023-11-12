package com.goreckia.game.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private final int port;
    private final String serverAddress;

    public GameClient(String ip, int port) {
        this.serverAddress = ip;
        this.port = port;
    }

    public void connect() {
        Scanner scanner = new Scanner(System.in);
        String namePlayer = "";
        do {
            System.out.print("Nhập tên người chơi: ");
            namePlayer = scanner.nextLine();
        } while (namePlayer.equals(""));

        try {
            Socket socket = new Socket(serverAddress, port);
            System.out.println(namePlayer + ": Connected to server on port " + port);

            // Gửi tên người chơi đến server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(namePlayer);

            // Đọc và hiển thị thông tin đợi ghép cặp
            String response = receiveMessage(socket);
            System.out.println(response);

            // Nếu đang chờ ghép cặp, tiếp tục đợi thông báo từ server
            if (response.endsWith("Waiting for opponent...")) {
                while (true) {
                    response = receiveMessage(socket);
                    System.out.println(response);

                    // Nếu đã ghép cặp thành công, hiển thị thông tin đối thủ
                    if (response.startsWith("opponent:")) {
                        String opponentName = response.substring(9);
                        System.out.println("Matched with opponent: " + opponentName);
                        break;
                    } else if (response.equals("opponentDisconnected")) {
                        System.out.println("Your opponent has disconnected. Exiting...");
                        // Thực hiện các xử lý khác khi đối thủ disconnect
                        // Ví dụ: quay lại màn hình chờ ghép cặp hoặc thoát chương trình
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

                    // Xử lý thông báo disconnect từ server
                    if (serverResponse.equals("disconnect")) {
                        System.out.println("Server has disconnected. Exiting...");
                        System.exit(0);
                    }
                }
            }).start();

            // Gửi dữ liệu đến server (ví dụ: tọa độ của người chơi)
            while (true) {
                System.out.print("Enter your move (type 'exit' to quit): ");
                String move = scanner.nextLine();
                System.out.println(move);
                out.println("move: " + move);

                if ("exit".equalsIgnoreCase(move)) {
                    out.println("disconnect"); // Gửi thông báo disconnect khi người chơi thoát
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

    private String receiveMessage(Socket socket) throws IOException {
        Scanner in = new Scanner(socket.getInputStream());
        return in.nextLine();
    }
}
