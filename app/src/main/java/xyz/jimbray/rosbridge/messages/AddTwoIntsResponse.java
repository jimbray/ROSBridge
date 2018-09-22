package xyz.jimbray.rosbridge.messages;

import com.jilk.ros.message.Message;
import com.jilk.ros.message.MessageType;

/**
 * Created by jimbray on 2018/9/20.
 * Email: jimbray16@gmail.com
 */

    // MessageType 必须要写明 beginner_tutorials/AddTwoInts 后面加上 response 后缀
    // 参照 当时写 service 服务端 python 文件的时候，也有一个约定俗称的东西
    // 那个请求参数就要再看看了
@MessageType(string = "beginner_tutorials/AddTwoIntsResponse")
public class AddTwoIntsResponse extends Message {

    public int sum;
}
