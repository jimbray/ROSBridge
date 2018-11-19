package xyz.jimbray.rosbridge.contracts;

import xyz.jimbray.rosbridge.managers.RosBridgeClientManager;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.presenters.BasePresenter;
import xyz.jimbray.rosbridge.views.BaseView;

public interface RosPannelContract {


    interface IRosPannalPresenter extends BasePresenter {

        <T> void publishTopic(String topicName, T msg);

        void subscribeTopic(String topicName);

        void unSubscribeTopic(String topicName);

    }

    interface IRosPannelView extends BaseView<IRosPannalPresenter> {
        void onRosStringMessageReceived(String topicName, RosStringData message);
        void onRosImageMessageeceived(RosImageData imageData);
    }

    interface IRosPannelModel {

        <T> void publishTopic(String topicName, T msg);

        void subscribeTopic(String topicName, RosBridgeClientManager.OnRosMessageListener listener);

        void unSubscribeTopic(String topicName, RosBridgeClientManager.OnRosMessageListener listener);

    }

}
