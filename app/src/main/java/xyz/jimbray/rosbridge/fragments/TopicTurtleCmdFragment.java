package xyz.jimbray.rosbridge.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.jimbray.rosbridge.R;
import xyz.jimbray.rosbridge.customviews.CircleButton;
import xyz.jimbray.rosbridge.messages.ITopicNames;
import xyz.jimbray.rosbridge.messages.ITopicVrepCPSMessage;
import xyz.jimbray.rosbridge.messages.RosStringData;
import xyz.jimbray.rosbridge.messages.TwistData;

public class TopicTurtleCmdFragment extends RosPannelTopticBaseFragment implements View.OnClickListener {

    private CircleButton btn_left, btn_right, btn_forward, btn_backward;
    private Button btn_start, btn_stop;

    public static RosPannelTopticBaseFragment newInstance() {
        RosPannelTopticBaseFragment fg = new TopicTurtleCmdFragment();
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
        View view = inflater.inflate(R.layout.layout_fg_topic_turtle_cmd, container, false);

        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);

        btn_left = view.findViewById(R.id.btn_left);
        btn_right = view.findViewById(R.id.btn_right);
        btn_forward = view.findViewById(R.id.btn_forward);
        btn_backward = view.findViewById(R.id.btn_backward);
        btn_start = view.findViewById(R.id.btn_start);
        btn_stop = view.findViewById(R.id.btn_stop);

        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_backward.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, turnLeft());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> left");
                break;

            case R.id.btn_right:
                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, turnRight());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> right");
                break;

            case R.id.btn_forward:
                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, forward());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> forward");
                break;

            case R.id.btn_backward:
                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, backward());
                //((RosPannelFragment)getParentFragment()).onPublishMessage("publish message -> backward");
                break;

            case R.id.btn_start:
                mPresenter.subscribeTopic(ITopicNames.VREP_CPS);
                mPresenter.publishTopic(ITopicNames.VREP_CPS, new RosStringData(ITopicVrepCPSMessage.MESSAGE_ROBOT_LOAD_START));
                break;

            case R.id.btn_stop:
                mPresenter.publishTopic(ITopicNames.TURTLE1_CMD_VEL, stop());
                break;
        }
    }

    private TwistData forward() {
        TwistData twistData = new TwistData();
        twistData.linear.x = 2.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

        return twistData;
    }


    private TwistData turnLeft() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 2.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 1.5f;

        return twistData;
    }

    private TwistData turnRight() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 2.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = -1.5f;

        return twistData;
    }

    private TwistData backward() {

        TwistData twistData = new TwistData();
        twistData.linear.x = -2.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

        return twistData;
    }

    private TwistData stop() {

        TwistData twistData = new TwistData();
        twistData.linear.x = 0.0f;
        twistData.linear.y = 0.0f;
        twistData.linear.y = 0.0f;

        twistData.angular.x = 0.0f;
        twistData.angular.y = 0.0f;
        twistData.angular.z = 0.0f;

        return twistData;
    }
}
