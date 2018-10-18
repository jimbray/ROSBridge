package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import xyz.jimbray.rosbridge.BaseFragment;
import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.adapters.RosMessageListAdapter;
import xyz.jimbray.rosbridge.contracts.RosPannelContract;
import xyz.jimbray.rosbridge.data.RosReceivedMessage;
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
        mTopicFragmentList.add(TopicVrepCpsFragment.newInstance());
        mTopicFragmentList.add(TopicTurtleCmdFragment.newInstance());

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
        getChildFragmentManager().beginTransaction().replace(R.id.layout_operation_pannel, mTopicFragmentList.get(position)).commitNow();
    }

    @Override
    public void onRosStringMessageReceived(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(message)) {

                    RosStringData data = mGson.fromJson(message, RosStringData.class);

                    String date = mDateFormat.format(new Date());
                    RosReceivedMessage messageData = new RosReceivedMessage(data.data, date);

                    mMessageAdapter.addItem(messageData);
                    recyclerview_message.scrollToPosition(mMessageAdapter.getItemCount() - 1);
                }

            }
        });
    }

    @Override
    public void setPresenter(RosPannelContract.IRosPannalPresenter presenter) {
        this.mPresenter = presenter;

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
