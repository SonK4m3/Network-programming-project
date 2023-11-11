package org.example.controller;

public interface SocketDataOperation {
    Boolean readMessage();
    void sendResponse(String response);
}
