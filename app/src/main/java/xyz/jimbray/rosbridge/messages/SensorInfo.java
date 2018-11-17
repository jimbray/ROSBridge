package xyz.jimbray.rosbridge.messages;

public class SensorInfo {

    private int all_sensor_num;

    private int failure_sensor_num;

    private String from_robot_id;

    private String from_sensor_id;

    private String sensor_data;

    private String sensor_state;

    private String to_cps_id;

    public int getAll_sensor_num() {
        return all_sensor_num;
    }

    public void setAll_sensor_num(int all_sensor_num) {
        this.all_sensor_num = all_sensor_num;
    }

    public int getFailure_sensor_num() {
        return failure_sensor_num;
    }

    public void setFailure_sensor_num(int failure_sensor_num) {
        this.failure_sensor_num = failure_sensor_num;
    }

    public String getFrom_robot_id() {
        return from_robot_id;
    }

    public void setFrom_robot_id(String from_robot_id) {
        this.from_robot_id = from_robot_id;
    }

    public String getFrom_sensor_id() {
        return from_sensor_id;
    }

    public void setFrom_sensor_id(String from_sensor_id) {
        this.from_sensor_id = from_sensor_id;
    }

    public String getSensor_data() {
        return sensor_data;
    }

    public void setSensor_data(String sensor_data) {
        this.sensor_data = sensor_data;
    }

    public String getSensor_state() {
        return sensor_state;
    }

    public void setSensor_state(String sensor_state) {
        this.sensor_state = sensor_state;
    }

    public String getTo_cps_id() {
        return to_cps_id;
    }

    public void setTo_cps_id(String to_cps_id) {
        this.to_cps_id = to_cps_id;
    }
}
