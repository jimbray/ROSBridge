package xyz.jimbray.rosbridge.models;

import xyz.jimbray.rosbridge.contracts.RosPannelContract;
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;

public class RosPannelModel implements RosPannelContract.IRosPannelModel {


    private RosBridgeClientManager mRoslientManager;

    public RosPannelModel() {
        mRoslientManager = RosBridgeClientManager.getInstance();
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
