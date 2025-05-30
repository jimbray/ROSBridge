package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.RosStringData;

/**
 * Created by jimbray on 2018/10/29.
 * Email: jimbray16@gmail.com
 */

public class TopicChatterFragment extends RosPannelTopticBaseFragment implements View.OnClickListener {

    private Button btn_publish, btn_subscribe, btn_unsubscribe;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new TopicChatterFragment();
        fg.setHasOptionsMenu(true);

        return fg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_operation, container, false);

        initViews(view);

        return view;
    }

    @Override
    protected void initViews(View view) {
        btn_publish = view.findViewById(R.id.btn_publish);
        btn_subscribe = view.findViewById(R.id.btn_subscribe);
        btn_unsubscribe = view.findViewById(R.id.btn_unsubscribe);

        btn_publish.setOnClickListener(this);
        btn_subscribe.setOnClickListener(this);
        btn_unsubscribe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish:
                mPresenter.publishTopic(ITopicNames.CHATTER, new RosStringData("message from android"));
                break;

            case R.id.btn_subscribe:
                mPresenter.subscribeTopic(ITopicNames.CHATTER);
                break;

            case R.id.btn_unsubscribe:
                mPresenter.unSubscribeTopic(ITopicNames.CHATTER);
                break;
        }
    }
}
