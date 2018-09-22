package xyz.jimbray.rosbridge.ros_common;

/**
 * Created by jimbray on 2018/9/19.
 * Email: jimbray16@gmail.com
 */

public class SubscribeTopicObject {

    private String op = "subscribe";

    private String topic;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
