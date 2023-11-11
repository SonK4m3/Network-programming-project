package org.example.socket;

public class GameClient2 {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Địa chỉ IP của máy chạy server
        int port = 12345;
        SocketClient client = new SocketClient(serverAddress, port);
        client.connect();
    }
}
