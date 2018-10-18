package xyz.jimbray.rosbridge.presenters;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.contracts.ConnectingContract;
import xyz.jimbray.rosbridge.models.ConnectingModelI;

public class ConnectingPresenter implements ConnectingContract.IConnectingPresenter {

    private ConnectingContract.IConnectingView mView;
    private ConnectingModelI mModel;

    public ConnectingPresenter(ConnectingContract.IConnectingView view) {
        this.mView = view;
        this.mModel = new ConnectingModelI();

        this.mView.setPresenter(this);

    }

    @Override
    public void connect2ROS(String url, int port) {
        mModel.connect2ROS(url, port, new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                mView.rosConnected();
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                mView.rosDisconnected();
            }

            @Override
            public void onError(Exception ex) {
                mView.rosConnectError(ex);
            }
        });
    }

    @Override
    public void autoConnect2ROS() {

    }
}
