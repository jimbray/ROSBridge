package xyz.jimbray.rosbridge.views;

import xyz.jimbray.rosbridge.presenters.BasePresenter;

/**
 * Created by jimbray on 2018/9/22.
 * Email: jimbray16@gmail.com
 */

public interface BaseView<T> {

    public void setPresenter(T presenter);
}
