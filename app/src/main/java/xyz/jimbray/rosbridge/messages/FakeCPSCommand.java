package xyz.jimbray.rosbridge.messages;

public class FakeCPSCommand {

    private Header header;

    private String from_cps_id = "Android CPS client";

    private String to_robot_id = "anyway";

    private int emergency_stop = 0;

    private int task_command_type = 0;

    private int task_command = 0;

    private int mobile_platform_move_enable = 0;

    private int mobile_platform_mode = 0;

    private String mobile_platform_move = "forward";

    private int arm_move_enable = 0;

    private int arm_mode = 0;

    private String arm_move = "move move move！";

    private int get_sensor_data_enable = 0;

    private int get_sensor_data_mode = 0;

    private String sensor_name = "sensor's name";

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getFrom_cps_id() {
        return from_cps_id;
    }

    public void setFrom_cps_id(String from_cps_id) {
        this.from_cps_id = from_cps_id;
    }

    public String getTo_robot_id() {
        return to_robot_id;
    }

    public void setTo_robot_id(String to_robot_id) {
        this.to_robot_id = to_robot_id;
    }

    public int getEmergency_stop() {
        return emergency_stop;
    }

    public void setEmergency_stop(int emergency_stop) {
        this.emergency_stop = emergency_stop;
    }

    public int getTask_command_type() {
        return task_command_type;
    }

    public void setTask_command_type(int task_command_type) {
        this.task_command_type = task_command_type;
    }

    public int getTask_command() {
        return task_command;
    }

    public void setTask_command(int task_command) {
        this.task_command = task_command;
    }

    public int getMobile_platform_move_enable() {
        return mobile_platform_move_enable;
    }

    public void setMobile_platform_move_enable(int mobile_platform_move_enable) {
        this.mobile_platform_move_enable = mobile_platform_move_enable;
    }

    public int getMobile_platform_mode() {
        return mobile_platform_mode;
    }

    public void setMobile_platform_mode(int mobile_platform_mode) {
        this.mobile_platform_mode = mobile_platform_mode;
    }

    public String getMobile_platform_move() {
        return mobile_platform_move;
    }

    public void setMobile_platform_move(String mobile_platform_move) {
        this.mobile_platform_move = mobile_platform_move;
    }

    public int getArm_move_enable() {
        return arm_move_enable;
    }

    public void setArm_move_enable(int arm_move_enable) {
        this.arm_move_enable = arm_move_enable;
    }

    public int getArm_mode() {
        return arm_mode;
    }

    public void setArm_mode(int arm_mode) {
        this.arm_mode = arm_mode;
    }

    public String getArm_move() {
        return arm_move;
    }

    public void setArm_move(String arm_move) {
        this.arm_move = arm_move;
    }

    public int getGet_sensor_data_enable() {
        return get_sensor_data_enable;
    }

    public void setGet_sensor_data_enable(int get_sensor_data_enable) {
        this.get_sensor_data_enable = get_sensor_data_enable;
    }

    public int getGet_sensor_data_mode() {
        return get_sensor_data_mode;
    }

    public void setGet_sensor_data_mode(int get_sensor_data_mode) {
        this.get_sensor_data_mode = get_sensor_data_mode;
    }

    public String getSensor_name() {
        return sensor_name;
    }

    public void setSensor_name(String sensor_name) {
        this.sensor_name = sensor_name;
    }

    public static class Header {

        private int seq;

        private long stamp;

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public long getStamp() {
            return stamp;
        }

        public void setStamp(long stamp) {
            this.stamp = stamp;
        }
    }
}
