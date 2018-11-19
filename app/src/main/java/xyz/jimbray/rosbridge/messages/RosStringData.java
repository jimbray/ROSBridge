package xyz.jimbray.rosbridge.messages;

import com.jilk.ros.message.Message;
import com.jilk.ros.message.MessageType;

/**
 * Created by jimbray on 2018/9/19.
 * Email: jimbray16@gmail.com
 */

@MessageType(string = "std_msgs/String")
public class RosStringData extends Message {

    public RosStringData() {

    }

    public RosStringData(String data) {
        this.data = data;
    }

    public String data;

}
