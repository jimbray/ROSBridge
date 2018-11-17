package xyz.jimbray.rosbridge.presenters;

import android.util.Log;

import com.jilk.ros.ROSClient;

import xyz.jimbray.rosbridge.contracts.MainContract;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.models.MainModel;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public class MainPresenter implements MainContract.IMainPresenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.IMainView mView;
    private MainModel mModel;

    public MainPresenter(MainContract.IMainView IMainView) {
        this.mView = IMainView;
        this.mModel = new MainModel();
        mView.setPresenter(this);
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
        mModel.autoConnect2ROS(new ROSClient.ConnectionStatusListener() {
            @Override
            public void onConnect() {
                mView.rosConnected();
            }

            @Override
            public void onDisconnect(boolean normal, String reason, int code) {
                Log.d(TAG, "ROS disconect reason -> " + reason);
                mView.rosDisconnected();
            }

            @Override
            public void onError(Exception ex) {
                mView.rosConnectError(ex);
            }
        });
    }

    @Override
    public <T> void publishTopic(String topicName, T msg) {
        mModel.publishTopic(topicName, msg);
    }

    @Override
    public void subscribeTopic(String topicName) {
        mModel.subscribeTopic(topicName, rosMessageListener);
    }

    @Override
    public void unSubscribeTopic(String topicName) {
        mModel.unSubscribeTopic(topicName, rosMessageListener);
    }

    private RosBridgeClientManager.OnRosMessageListener rosMessageListener = new RosBridgeClientManager.OnRosMessageListener() {
        @Override
        public void onStringMessageReceive(RosStringData stringData, String topicName) {
            mView.topicStringMessageReceived(stringData);
        }

        @Override
        public void onImageMessageReceive(RosImageData imageData) {

        }
    };


}
