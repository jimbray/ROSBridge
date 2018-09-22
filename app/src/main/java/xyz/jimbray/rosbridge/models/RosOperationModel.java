package xyz.jimbray.rosbridge.models;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public interface RosOperationModel {

    void connect2ROS(String url, int port, ROSClient.ConnectionStatusListener listener);

    void autoConnect2ROS(ROSClient.ConnectionStatusListener listener);

    <T> void publishTopic(String topicName, T msg);

    void subscribeTopic(String topicName, RosBridgeClientManager.OnRosMessageListener listener);

    void unSubscribeTopic(String topicName, RosBridgeClientManager.OnRosMessageListener listener);

}
