package xyz.jimbray.rosbridge.models;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.contracts.ConnectingContract;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;

public class ConnectingModelI implements ConnectingContract.IRosConnectingModel {

    private RosBridgeClientManager mRoslientManager;

    public ConnectingModelI() {
        this.mRoslientManager = RosBridgeClientManager.getInstance();
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

}
