package xyz.jimbray.rosbridge.data;

public class RosReceivedMessage {

    public RosReceivedMessage() {

    }

    public RosReceivedMessage(String message , String time) {
        this.message = message;
        this.time = time;
    }

    private String message;

    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
