package xyz.jimbray.rosbridge;

import android.app.Application;

import com.jilk.ros.rosbridge.ROSBridgeClient;


/**
 * Created by jimbray on 2018/9/18.
 * Email: jimbray16@gmail.com
 */

public class App extends Application {

    private ROSBridgeClient mRosBridgeClient = null;

    private static App mRef = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mRef = this;
    }

    public static App getInstance() {
        return mRef;
    }

    public ROSBridgeClient getRosBridgeClient() {
        return mRosBridgeClient;
    }

    public void setRosBridgeClient(ROSBridgeClient client) {
       this.mRosBridgeClient = client;
    }
}
