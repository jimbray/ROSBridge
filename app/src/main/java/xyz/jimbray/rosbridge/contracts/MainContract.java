package xyz.jimbray.rosbridge.contracts;

import xyz.jimbray.rosbridge.presenters.BasePresenter;
import xyz.jimbray.rosbridge.views.BaseView;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public interface MainContract {

    interface IMainView extends BaseView<IMainPresenter> {

        void rosConnected();

        void rosDisconnected();

        void rosConnectError(Exception ex);

        void chatterTopicMessageReceived(String data_str);
    }

    interface IMainPresenter extends BasePresenter {

        void connect2ROS(String url, int port);

        void autoConnect2ROS();

        <T> void publishTopic(String topicName, T msg);

        void subscribeTopic(String topicName);

        void unSubscribeTopic(String topicName);

    }

}
