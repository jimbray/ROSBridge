package xyz.jimbray.rosbridge.presenters;

import xyz.jimbray.rosbridge.contracts.RosPannelContract;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.models.RosPannelModel;

public class RosPannelPresenter implements RosPannelContract.IRosPannalPresenter {


    private RosPannelContract.IRosPannelModel mModel;
    private RosPannelContract.IRosPannelView mView;


    public RosPannelPresenter(RosPannelContract.IRosPannelView view) {
        this.mModel = new RosPannelModel();
        this.mView = view;
        this.mView.setPresenter(this);
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
        public void onStringMessageReceive(String topicName, RosStringData stringData) {
            mView.onRosStringMessageReceived(topicName, stringData);
        }

        @Override
        public void onImageMessageReceive(RosImageData imageData) {
            mView.onRosImageMessageeceived(imageData);
        }
    };
}
