package xyz.jimbray.rosbridge.messages;

/**
 * Created by jimbray on 2018/9/25.
 * Email: jimbray16@gmail.com
 */

// 构造 geometry_msgs/Twist 消息
public class TwistData {

    public TwistData() {
        this.linear = new LinearData();
        this.angular = new AngularData();
    }

    public LinearData linear;

    public AngularData angular;

    public class LinearData {

        public LinearData() {

        }

        public LinearData(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float x;
        public float y;
        public float z;
    }

    public class AngularData {

        public AngularData() {
            
        }

        public AngularData(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float x;
        public float y;
        public float z;

    }

}
