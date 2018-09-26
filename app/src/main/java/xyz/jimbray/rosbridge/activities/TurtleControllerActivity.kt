package xyz.jimbray.rosbridge.activities

import android.content.Context
import android.os.Bundle
import xyz.jimbray.rosbridge.BaseActivity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_turtle_controller.*
import xyz.jimbray.rosbridge.R
import xyz.jimbray.rosbridge.managers.RosBridgeClientManager
import xyz.jimbray.rosbridge.messages.TwistData

/**
 * Created by jimbray on 2018/9/25.
 * Email: jimbray16@gmail.com
 */
class TurtleControllerActivity : BaseActivity() {

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TurtleControllerActivity::class.java))
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turtle_controller)

        btn_forward.setOnClickListener {
            RosBridgeClientManager.getInstance().publishTopic("/turtle1/cmd_vel", forward())
        }

        btn_backward.setOnClickListener {
            RosBridgeClientManager.getInstance().publishTopic("/turtle1/cmd_vel", backward())
        }

        btn_left.setOnClickListener {
            RosBridgeClientManager.getInstance().publishTopic("/turtle1/cmd_vel", turnLeft())
        }

        btn_right.setOnClickListener {
            RosBridgeClientManager.getInstance().publishTopic("/turtle1/cmd_vel", turnRight())
        }
    }

    private fun forward(): TwistData {

        val twistData = TwistData()
        twistData.linear.x = 2.0f
        twistData.linear.y = 0.0f
        twistData.linear.y = 0.0f

        twistData.angular.x = 0.0f
        twistData.angular.y = 0.0f
        twistData.angular.z = 0.0f

        return twistData
    }

    private fun turnLeft(): TwistData {

        val twistData = TwistData()
        twistData.linear.x = 2.0f
        twistData.linear.y = 0.0f
        twistData.linear.y = 0.0f

        twistData.angular.x = 0.0f
        twistData.angular.y = 0.0f
        twistData.angular.z = 1.5f

        return twistData
    }

    private fun turnRight(): TwistData {

        val twistData = TwistData()
        twistData.linear.x = 2.0f
        twistData.linear.y = 0.0f
        twistData.linear.y = 0.0f

        twistData.angular.x = 0.0f
        twistData.angular.y = 0.0f
        twistData.angular.z = -1.5f

        return twistData
    }

    private fun backward(): TwistData {

        val twistData = TwistData()
        twistData.linear.x = -2.0f
        twistData.linear.y = 0.0f
        twistData.linear.y = 0.0f

        twistData.angular.x = 0.0f
        twistData.angular.y = 0.0f
        twistData.angular.z = 0.0f

        return twistData
    }

}