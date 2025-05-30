package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xyz.jimbray.rosbridge.BaseFragment;
import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.adapters.RosMessageListAdapter;
import xyz.jimbray.rosbridge.contracts.RosPannelContract;
import xyz.jimbray.rosbridge.data.RosReceivedMessage;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.ITopicVrepCPSMessage;
import xyz.jimbray.rosbridge.messages.RosImageData;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.presenters.RosPannelPresenter;

public class RosPannelFragment extends BaseFragment implements RosPannelContract.IRosPannelView {

    private RosPannelContract.IRosPannalPresenter mPresenter;

    private Spinner spinner_topic;
    private RecyclerView recyclerview_message;


    private RosMessageListAdapter mMessageAdapter;

    private String mCurSelectedTopicName;

    private String[] TOPIC_ARRAY;

    private Gson mGson = new Gson();

    private List<RosPannelTopticBaseFragment> mTopicFragmentList = new ArrayList<>();


    private RosPannelTopticBaseFragment mCurFragment = null;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("hh:mm:ss");

    public static Fragment newInstanece() {
        Fragment fg = new RosPannelFragment();
        fg.setHasOptionsMenu(true);

        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 需要与topic name list 对应
        mTopicFragmentList.add(TopicTurtleCmdFragment.newInstance());
        mTopicFragmentList.add(TopicVrepCpsFragment.newInstance());
        mTopicFragmentList.add(TopicChatterFragment.newInstance());
        mTopicFragmentList.add(TopicRosImageDecodeFragment.newInstance(ITopicNames.USB_CAM_IMAGE_RAW));
        mTopicFragmentList.add(TopicRosImageDecodeFragment.newInstance(ITopicNames.USB_CAM_IMAGE_COMPRESSED));
        mTopicFragmentList.add(TopicRosImageDecodeFragment.newInstance(ITopicNames.IMAGE_TAG));
        mTopicFragmentList.add(TopicRosImageDecodeFragment.newInstance(ITopicNames.IMAGE_ROD_LEFT));
        mTopicFragmentList.add(TopicRosImageDecodeFragment.newInstance(ITopicNames.IMAGE_ROD_RIGHT));
//        mTopicFragmentList.add(TopicBase64ImageGlideFragment.newInstance());

        new RosPannelPresenter(this);

        TOPIC_ARRAY = getResources().getStringArray(R.array.topic_list);
        mCurSelectedTopicName = TOPIC_ARRAY[0];
        loadTopicFragment(0);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fg_ros_pannel, container, false);

        initViews(view);

        return view;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        spinner_topic = view.findViewById(R.id.spinner_topic);
        recyclerview_message = view.findViewById(R.id.recyclerview_message);

        recyclerview_message.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessageAdapter = new RosMessageListAdapter(getActivity(), null);
        recyclerview_message.setAdapter(mMessageAdapter);

        spinner_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mCurSelectedTopicName = TOPIC_ARRAY[position];
                loadTopicFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadTopicFragment(int position) {
        mCurFragment = mTopicFragmentList.get(position);
        getChildFragmentManager().beginTransaction().replace(R.id.layout_operation_pannel, mCurFragment).commitNow();
    }

    @Override
    public void onRosImageMessageReceived(String topicName, RosImageData imageData) {
        if (imageData != null) {
            if (topicName.equals(ITopicNames.USB_CAM_IMAGE_COMPRESSED) ||
                    topicName.equals(ITopicNames.USB_CAM_IMAGE_RAW) ||
                    topicName.equals(ITopicNames.IMAGE_TAG) ||
                    topicName.equals(ITopicNames.IMAGE_ROD_LEFT) ||
                    topicName.equals(ITopicNames.IMAGE_ROD_RIGHT)) {
                if (mCurFragment instanceof ImageDecoderFragment) {
                    ((ImageDecoderFragment)mCurFragment).setImage(imageData);
                }
            }
        }
    }

    @Override
    public void onRosStringMessageReceived(final String topicName, final RosStringData rosStringData) {


        String message = rosStringData.data;
        if (TextUtils.isEmpty(message)) {
            return;
        }

        Disposable disposable = Observable.create(new ObservableOnSubscribe<RosReceivedMessage>() {
            @Override
            public void subscribe(ObservableEmitter<RosReceivedMessage> emitter) {

                String date = mDateFormat.format(new Date());

                RosReceivedMessage showData = new RosReceivedMessage();
                if (topicName.equals(ITopicNames.VREP_CPS) ||
                        topicName.equals(ITopicNames.CHATTER) ||
                        topicName.equals(ITopicNames.IMAGE_BASE64_STR)) {
                    String showStr = getTranlatedMessage(rosStringData.data);

                    if (!TextUtils.isEmpty(showStr)) {
                        Log.d("RosPannelFragment", showStr);
                        showData.setMessage(showStr);
                        showData.setTime(date);
                    }

                    if (mCurFragment instanceof ImageDecoderFragment) {
                        ((ImageDecoderFragment) mCurFragment).setImage(showStr);
                    }

                }

                emitter.onNext(showData);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RosReceivedMessage>() {
                    @Override
                    public void accept(RosReceivedMessage rosReceivedMessage) throws Exception {
                        mMessageAdapter.addItem(rosReceivedMessage);
                        recyclerview_message.scrollToPosition(mMessageAdapter.getItemCount() - 1);
                    }
                });

    }

    private String getTranlatedMessage(String ros_message) {
        String result = ros_message;
        switch (ros_message) {
            case ITopicVrepCPSMessage.MESSAGE_ROBOT_LOAD_STARTED:
                result = "上下料机器人开始抓取石材";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_LOADED:
                result = "上下料机器人抓取石材完毕";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_CARRY_COM_START:
                result = "上下料机器人运输石材中";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_CARRY_COMED:
                result = "抓取石材到指定位置";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_LOAD_PLANE_HANDOVER1:
                result = "抓取石材到指定位置";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_LOAD_PLANE_HANDOVER2:
                result = "安装机器人抓取石材完毕";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_LOAD_LEAVE:
                result = "上下料机器人离开，安装机器人开始安装";
                break;

            case ITopicVrepCPSMessage.MESSAGE_ROBOT_PLANE_INSTALLED:
                result = "石材安装完毕";
                break;

        }
        return result;
    }

    @Override
    public void setPresenter(RosPannelContract.IRosPannalPresenter presenter) {
        this.mPresenter = presenter;


//        Log.d("sss", "一次一次");
//        mPresenter.subscribeTopic(ITopicNames.VREP_CPS);
        for (int i = 0 ; i < mTopicFragmentList.size(); i++) {
            mTopicFragmentList.get(i).setOeration(presenter);
        }
    }

    public void onPublishMessage(String message) {
        String date = mDateFormat.format(new Date());
        RosReceivedMessage messageData = new RosReceivedMessage(message, date);

        mMessageAdapter.addItem(messageData);
        recyclerview_message.scrollToPosition(mMessageAdapter.getItemCount() - 1);
    }

}
