package xyz.jimbray.rosbridge.messages;

import com.jilk.ros.message.Header;
import com.jilk.ros.message.Message;
import com.jilk.ros.message.MessageType;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Created by jimbray on 2018/11/10.
 * Email: jimbray16@gmail.com
 */

@MessageType(string = "std_msgs/Image")
public class RosImageData extends Message {

    public Header header;

    //public int height;
    public long height;

    //public int width;
    public long width;

    public String encoding;

    //public byte is_bigendian;
    public long is_bigendian;

    //public int step;
    public long step;

    public ChannelBuffer data;
}
