package xyz.jimbray.rosbridge.models;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public class MainModel implements RosOperationModel {

    private RosBridgeClientManager mRoslientManager;

    public MainModel() {
        mRoslientManager = RosBridgeClientManager.getInstance();
    }

    @Override
    public void connect2ROS(String url, int port, final ROSClient.ConnectionStatusListener listener) {
        mRoslientManager.connect(url, port, new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                listener.onConnect();
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                listener.onDisconnect(normal, reason, code);
            }

            @Override
            public void onError(Exception ex) {
                listener.onError(ex);
            }
        });
    }

    @Override
    public void autoConnect2ROS(final ROSClient.ConnectionStatusListener listener) {
        mRoslientManager.connect("172.16.95.101", new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                listener.onConnect();
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                listener.onDisconnect(normal, reason, code);
            }

            @Override
            public void onError(Exception ex) {
                listener.onError(ex);
            }
        });
    }

    @Override
    public <T> void publishTopic(String topicName, T msg) {
        mRoslientManager.publishTopic(topicName, msg);
    }

    @Override
    public void subscribeTopic(String topicName, final RosBridgeClientManager.OnRosMessageListener listener) {
        mRoslientManager.subscribeTopic(topicName, listener);
    }

    @Override
    public void unSubscribeTopic(String topicName, final RosBridgeClientManager.OnRosMessageListener listener) {
        mRoslientManager.unSubscribeTopic(topicName, listener);
    }
}
