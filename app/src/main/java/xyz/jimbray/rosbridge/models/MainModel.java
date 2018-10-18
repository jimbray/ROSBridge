package xyz.jimbray.rosbridge.models;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.contracts.MainContract;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public class MainModel implements MainContract.IRosOperationModel {

    private RosBridgeClientManager mRoslientManager;

    public MainModel() {
        mRoslientManager = RosBridgeClientManager.getInstance();
    }

    @Override
    public void connect2ROS(final String url, final int port, final ROSClient.ConnectionStatusListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();

    }

    @Override
    public void autoConnect2ROS(final ROSClient.ConnectionStatusListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRoslientManager.connect("172.20.10.2", new ROSClient.ConnectionStatusListener() {
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
        }).start();

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
