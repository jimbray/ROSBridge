package xyz.jimbray.rosbridge.fragments;

import xyz.jimbray.rosbridge.BaseFragment;
import xyz.jimbray.rosbridge.contracts.RosPannelContract;

public class RosPannelTopticBaseFragment extends BaseFragment {

    protected RosPannelContract.IRosPannalPresenter mPresenter;


    public void setOeration(RosPannelContract.IRosPannalPresenter presenter) {
        this.mPresenter = presenter;
    }
}
