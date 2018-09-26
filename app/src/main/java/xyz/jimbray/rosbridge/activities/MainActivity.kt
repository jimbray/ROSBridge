package xyz.jimbray.rosbridge.activities

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import android.view.View
import com.jilk.ros.ROSClient
import xyz.jimbray.rosbridge.messages.ChatterData
import com.google.gson.Gson
import xyz.jimbray.rosbridge.BaseActivity
import xyz.jimbray.rosbridge.R
import xyz.jimbray.rosbridge.contracts.MainContract
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager
import xyz.jimbray.rosbridge.contracts.MainContract.IMainPresenter
import xyz.jimbray.rosbridge.presenters.MainPresenter

class MainActivity : BaseActivity(), MainContract.IMainView {
    private val TAG = MainActivity::class.java.simpleName

    private val mGson = Gson()

    private var mPresenter: IMainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainPresenter(this)

        initViews()
    }

    fun initViews() {
        btn_publish_chatter.setOnClickListener {
            //RosBridgeClientManager.getInstance().publishTopic("chatter", ChatterData("1212121"))
            mPresenter?.publishTopic("/chatter", ChatterData("11111222222"))
        }

        btn_subscribe_chatter.setOnClickListener {
            //RosBridgeClientManager.getInstance().subscribeTopic("chatter", onChatterMessageReceiedListener)
            mPresenter?.subscribeTopic("/chatter")
        }

        btn_ubsubscribe_chatter.setOnClickListener {
            //RosBridgeClientManager.getInstance().unSubscribeTopic("chatter", onChatterMessageReceiedListener)
            mPresenter?.unSubscribeTopic("/chatter")
        }

        btn_call_add_two_ints.setOnClickListener {
            RosBridgeClientManager.getInstance().callAtwoInts()
        }

        btn_call_get_time.setOnClickListener {
            RosBridgeClientManager.getInstance().callGetTime()
        }

        btn_turtle_controller.setOnClickListener {
            TurtleControllerActivity.startActivity(this@MainActivity)
        }
    }

    override fun setPresenter(presenterI: MainContract.IMainPresenter?) {
        mPresenter = presenterI

        mPresenter?.autoConnect2ROS()
    }

    override fun rosConnected() {
        Log.d(TAG, "onConnect")
        runOnUiThread {
            layout_content.visibility = View.VISIBLE
            layout_connecting.visibility = View.GONE
        }
    }

    override fun rosDisconnected() {
        Log.d(TAG, "onDisconnect")
        runOnUiThread {
            tv_connecting_text.text = "ROS disconnected!"
        }

    }

    override fun rosConnectError(ex: Exception) {
        Log.d(TAG, "onError")
        runOnUiThread {
            tv_connecting_text.text = "ROS connect error!"
        }

    }

    override fun chatterTopicMessageReceived(data_str: String?) {
        runOnUiThread {
            if (!TextUtils.isEmpty(data_str)) {
                //val chatterData = ChatterData(data_str)
                val chatterData = mGson.fromJson(data_str, ChatterData::class.java)
                Log.d(TAG, chatterData.data)
            }
        }

    }
}
