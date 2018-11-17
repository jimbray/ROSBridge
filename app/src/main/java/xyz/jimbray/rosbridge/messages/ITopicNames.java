package xyz.jimbray.rosbridge.messages;

public interface ITopicNames {


    String VREP_CPS = "/vrep_cps";

    String TURTLE1_CMD_VEL = "/turtle1/cmd_vel";

    String CHATTER = "/chatter";

    String IMAGE_BASE64_STR = "/image/base64_str";

//    String CAMERA_IMAGE_COMPRESSED = "/camera/image_test";
    String CAMERA_IMAGE_COMPRESSED = "/usb_cam/image_raw/compressed";

    String CPS_STATUS = "/cps_status";

    String CPS_COMMAND = "/cps_command";
}
