package xyz.jimbray.rosbridge.fragments;

import android.util.Log;

import xyz.jimbray.rosbridge.BaseFragment;
import xyz.jimbray.rosbridge.contracts.RosPannelContract;
import xyz.jimbray.rosbridge.messages.ITopicNames;

public class RosPannelTopticBaseFragment extends BaseFragment {

    protected RosPannelContract.IRosPannalPresenter mPresenter;


    public void setOeration(RosPannelContract.IRosPannalPresenter presenter) {
        this.mPresenter = presenter;
    }
}
