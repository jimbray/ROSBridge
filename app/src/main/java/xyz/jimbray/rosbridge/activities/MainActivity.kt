package xyz.jimbray.rosbridge.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import xyz.jimbray.rosbridge.BaseActivity
import xyz.jimbray.rosbridge.R
import xyz.jimbray.rosbridge.fragments.ConnectingFragment
import xyz.jimbray.rosbridge.fragments.RosPannelFragment

class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    fun initViews() {
        supportFragmentManager.beginTransaction().replace(R.id.layout_content, ConnectingFragment.newInstanece()).commitNow()
    }


    fun switchFragment(fg : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.layout_content, fg).commitNow()
    }

}
