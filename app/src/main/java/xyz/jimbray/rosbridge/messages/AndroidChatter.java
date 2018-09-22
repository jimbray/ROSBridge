package xyz.jimbray.rosbridge.messages;

import com.jilk.ros.message.Message;
import com.jilk.ros.message.MessageType;

/**
 * Created by jimbray on 2018/9/21.
 * Email: jimbray16@gmail.com
 */

@MessageType(string = "std_msgs/String")
public class AndroidChatter extends Message {

    public String data;
}
