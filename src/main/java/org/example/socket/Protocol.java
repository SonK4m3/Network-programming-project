package org.example.socket;

public class Protocol {
    private Integer messageSize;
    private String methodType;
    private String message;
    private String status;

    public Protocol() {

    }

    public Protocol(Integer messageSize, String methodType, String message, String status) {
        this.messageSize = messageSize;
        this.methodType = methodType;
        this.message = message;
        this.status = status;
    }

    public Integer getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(Integer messageSize) {
        this.messageSize = messageSize;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
