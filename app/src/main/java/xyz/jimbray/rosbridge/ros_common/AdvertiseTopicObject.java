package xyz.jimbray.rosbridge.ros_common;

import com.jilk.ros.Topic;
import com.jilk.ros.message.Message;
import com.jilk.ros.rosbridge.ROSBridgeClient;
import com.jilk.ros.rosbridge.operation.Advertise;

/**
 * Created by jimbray on 2018/9/28.
 * Email: jimbray16@gmail.com
 */

public class AdvertiseTopicObject<T> {

    private T message_type;

    private String topicName;

    private ROSBridgeClient client;

    public AdvertiseTopicObject(String topicName, T type, ROSBridgeClient rosBridgeClient) {
        this.client = rosBridgeClient;
        this.topicName = topicName;
        this.message_type = type;
    }


    public void advertise() {
        Topic topic = new Topic(topicName, message_type.getClass(), client);
        topic.advertise();
    }

    public T getMessage_type() {
        return message_type;
    }

    public void setMessage_type(T message_type) {
        this.message_type = message_type;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
