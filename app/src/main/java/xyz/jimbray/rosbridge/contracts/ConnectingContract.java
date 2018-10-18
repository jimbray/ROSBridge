package xyz.jimbray.rosbridge.contracts;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.presenters.BasePresenter;
import xyz.jimbray.rosbridge.views.BaseView;

public interface ConnectingContract {

    interface IConnectingPresenter extends BasePresenter {

        void connect2ROS(String url, int port);

        void autoConnect2ROS();

    }

    interface IConnectingView extends BaseView<IConnectingPresenter> {

        void rosConnected();

        void rosDisconnected();

        void rosConnectError(Exception ex);

        String getUrl();

        int getPort();

    }

    interface IRosConnectingModel {

        void connect2ROS(String url, int port, ROSClient.ConnectionStatusListener listener);

    }

}
