package xyz.jimbray.rosbridge.managers;

import android.util.Log;

import com.jilk.ros.MessageHandler;
import com.jilk.ros.PublishEvent;
import com.jilk.ros.ROSClient;
import com.google.gson.Gson;
import com.jilk.ros.Service;
import com.jilk.ros.Topic;
import com.jilk.ros.message.Empty;
import com.jilk.ros.rosapi.message.GetTime;
import com.jilk.ros.rosbridge.ROSBridgeClient;

import de.greenrobot.event.EventBus;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.jimbray.rosbridge.App;
import xyz.jimbray.rosbridge.messages.AddTwoInstRequest;
import xyz.jimbray.rosbridge.messages.AddTwoIntsResponse;
import xyz.jimbray.rosbridge.messages.AndroidChatter;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.ros_common.AdvertiseTopicObject;
import xyz.jimbray.rosbridge.ros_common.PublishTopicObject;
import xyz.jimbray.rosbridge.ros_common.SubscribeTopicObject;
import xyz.jimbray.rosbridge.ros_common.UnSubscribeTopicObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jimbray on 2018/9/18.
 * Email: jimbray16@gmail.com
 */

public class RosBridgeClientManager {

    private static final String TAG = RosBridgeClientManager.class.getSimpleName();

    private static RosBridgeClientManager instance;

    private ROSBridgeClient mRosBridgeClient = null;

    private String mCurUrl;

    private List<OnRosMessageListener> mROSListenerList;

    private Gson mGson;

    private RosBridgeClientManager() {
        mGson = new Gson();
        EventBus.getDefault().register(this);
    }

    public static RosBridgeClientManager getInstance() {
        if (instance == null) {
            synchronized (RosBridgeClientManager.class) {
                if (instance == null) {
                    instance = new RosBridgeClientManager();
                }
            }
        }
        return instance;
    }


    public void connect(String url, int port, final ROSClient.ConnectionStatusListener listener) {
        if (url != null && url.equals(mCurUrl)) {
            // already connected
        } else {
            mRosBridgeClient = new ROSBridgeClient("ws://" + url + ":" + port);
            mRosBridgeClient.connect(new ROSClient.ConnectionStatusListener() {
                @Override
                public void onConnect() {
                    // connected successful
                    App.getInstance().setRosBridgeClient(mRosBridgeClient);
                    if (listener != null) {
                        listener.onConnect();
                    }
                }

                @Override
                public void onDisconnect(boolean normal, String reason, int code) {
                    // client disconnected
                    if (listener != null) {
                        listener.onDisconnect(normal, reason, code);
                    }

                }

                @Override
                public void onError(Exception ex) {
                    // connect error
                    if (listener != null) {
                        listener.onError(ex);
                    }
                }
            });
        }
    }

    public void connect(String url, final ROSClient.ConnectionStatusListener listener) {
        int port = 9090;
        if (url != null && url.equals(mCurUrl)) {
            // already connected
        } else {
            mRosBridgeClient = new ROSBridgeClient("ws://" + url + ":" + port);
            mRosBridgeClient.connect(new ROSClient.ConnectionStatusListener() {
                @Override
                public void onConnect() {
                    // connected successful
                    App.getInstance().setRosBridgeClient(mRosBridgeClient);
                    if (listener != null) {
                        listener.onConnect();
                    }
                }

                @Override
                public void onDisconnect(boolean normal, String reason, int code) {
                    // client disconnected
                    if (listener != null) {
                        listener.onDisconnect(normal, reason, code);
                    }

                }

                @Override
                public void onError(Exception ex) {
                    // connect error
                    if (listener != null) {
                        listener.onError(ex);
                    }
                }
            });
        }
    }


    public <T> void publishTopic(String topicName, T msg) {
        PublishTopicObject<T> publishTopicObject = new PublishTopicObject<>();
        publishTopicObject.setTopic(topicName);
        publishTopicObject.setMsg(msg);

        String msg_str = mGson.toJson(publishTopicObject);
        if (mRosBridgeClient != null) {
            Log.d(TAG, msg_str);
            mRosBridgeClient.send(msg_str);
            //mRosBridgeClient.send("{\"msg\":\"12121212\",\"op\":\"publish\",\"topic\":\"/chatter\"}");
            //mRosBridgeClient.send("{\"op\":\"publish\",\"topic\":\"/chatter\",\"msg\":{\"data\":\"11111\"}}");

            //mRosBridgeClient.send("{\"op\":\"publish\",\"topic\":\"/turtle1/cmd_vel\",\"msg\":{\"linear\":{\"x\":2.0,\"y\":0.0,\"z\":0.0},\"angular\":{\"x\":0.0,\"y\":0.0,\"z\":1.8}}}");
        }



    }

    public <T> void advertiseTopic(String topicName, T data_type) {
        AdvertiseTopicObject<T> topic = new AdvertiseTopicObject<>(topicName, data_type, mRosBridgeClient);
        topic.setMessage_type(data_type);
        topic.advertise();
    }

    // 还是需要看看如何设计为 泛型
    public void advertiseAndroidChatterTopic(String topicName) {
        //java封装方式
        Topic<AndroidChatter> chatterTopic = new Topic<>("/android_chatter", AndroidChatter.class, mRosBridgeClient);
        AndroidChatter chatterData = new AndroidChatter();
        chatterData.data = "aaaaaaaaaaaa";
        chatterTopic.advertise(); // 广播 topic
        chatterTopic.publish(chatterData); // 发布 topic
        chatterTopic.subscribe(new MessageHandler<AndroidChatter>() { // 订阅topic
            @Override
            public void onMessage(AndroidChatter message) {
                Log.d("android_chatter_tag", message.data);
            }
        });
    }

    public void subscribeTopic(String topicName, OnRosMessageListener listener) {

        //json方式
        SubscribeTopicObject subscribeTopicObject = new SubscribeTopicObject();
        subscribeTopicObject.setTopic(topicName);

        String msg_str = mGson.toJson(subscribeTopicObject);
        if (mRosBridgeClient != null) {
            mRosBridgeClient.send(msg_str);
            addROSMessageListener(listener);
        }

        // 好像没有 直接send稳定，重新进入时无法连接上ROS？
        //Topic<RosStringData> chatterTopic = new Topic<>("/chatter", RosStringData.class, mRosBridgeClient);
        //chatterTopic.subscribe();

    }

    public void unSubscribeTopic(String topicName, OnRosMessageListener listener) {
        UnSubscribeTopicObject unSubscribeTopicObject = new UnSubscribeTopicObject();
        unSubscribeTopicObject.setTopic(topicName);

        String msg_str = mGson.toJson(unSubscribeTopicObject);
        if (mRosBridgeClient != null) {
            mRosBridgeClient.send(msg_str);
            removeROSMessageListener(listener);
        }
    }

    public void callAtwoInts() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Service<AddTwoInstRequest, AddTwoIntsResponse> addTwoIntsResponseService =
                        new Service<>("/add_two_ints", AddTwoInstRequest.class, AddTwoIntsResponse.class, mRosBridgeClient);
                AddTwoInstRequest request = new AddTwoInstRequest();
                request.a = 3;
                request.b = 5;
                addTwoIntsResponseService.callWithHandler(request, new MessageHandler<AddTwoIntsResponse>() {
                    @Override
                    public void onMessage(AddTwoIntsResponse message) {
                        Log.d("add_two_ints_tag", message.sum + "");
                    }
                });

                /*
                Service<Empty, GetTime> getTimeService = new Service<>("/rosapi/get_time", Empty.class, GetTime.class, mRosBridgeClient);
                getTimeService.callWithHandler(new Empty(), new MessageHandler<GetTime>() {
                    @Override
                    public void onMessage(GetTime message) {
                        Log.d("get_time", message.time.toString());
                    }
                });
                */
            }
        }).start();
    }

    public void callGetTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Service<Empty, GetTime> getTimeService = new Service<>("/rosapi/get_time", Empty.class, GetTime.class, mRosBridgeClient);
                getTimeService.callWithHandler(new Empty(), new MessageHandler<GetTime>() {
                    @Override
                    public void onMessage(GetTime message) {
                        Log.d("get_time", message.time.toString());
                    }
                });
            }
        }).start();
    }

    public void callService(String serviceName) {
        /*
        if (mRosBridgeClient != null) {
            String msg = "{\"op\":\"call_service\",\"service\":\"" + "beginner_tutorials/Add_Two_Ints" +"\",\"args\":["+ "\"a\":9, \"b\":6" +"]}";
            mRosBridgeClient.send(msg);
        }
        */

        //Service<Empty, GetTime> getTimeService = new Service<>("/rosapi/get_time", Empty.class, GetTime.class, mRosBridgeClient);
        //getTimeService.call(new Empty());

        /*
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Service<AddTwoInstRequest, AddTwoIntsResponse> addTwoIntsResponseService =
                        new Service<>("/add_two_ints", AddTwoInstRequest.class, AddTwoIntsResponse.class, mRosBridgeClient);
                AddTwoInstRequest request = new AddTwoInstRequest();
                request.setA(3);
                request.setB(5);
                addTwoIntsResponseService.call(request);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
           */

    }

    public void addROSMessageListener(OnRosMessageListener listener) {
        if (mROSListenerList == null) {
            mROSListenerList = new ArrayList<>();
        }

        if (listener != null) {
            mROSListenerList.add(listener);
        }
    }

    public void removeROSMessageListener(OnRosMessageListener listener) {
        if (listener != null && mROSListenerList != null) {
            mROSListenerList.remove(listener);
        }
    }


    //Receive data from ROS server, send from ROSBridgeWebSocketClient onMessage()
    // using eventbus ?!
    public void onEvent(final PublishEvent event) {
        Log.d(TAG, "receive rosdata -> " + event.msg);

        if (mROSListenerList != null) {
            for (int index = 0 ; index < mROSListenerList.size(); index++) {
                final int curIndex = index;
                if (event.name.equals(ITopicNames.CAMERA_IMAGE_TEST)) {
                    Observable.create(new ObservableOnSubscribe<RosImageData>() {
                        @Override
                        public void subscribe(ObservableEmitter<RosImageData> emitter) throws Exception {

                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<RosImageData>() {
                                @Override
                                public void accept(RosImageData imageData) throws Exception {
                                    mROSListenerList.get(curIndex).onImageMessageReceive(imageData);
                                }
                            });

                } else {
                    mROSListenerList.get(index).onStringMessageReceive(new RosStringData(event.msg));
                }
            }
        }
    }

    public interface OnRosMessageListener {
        void onStringMessageReceive(RosStringData stringData);
        void onImageMessageReceive(RosImageData imageData);
    }

}
