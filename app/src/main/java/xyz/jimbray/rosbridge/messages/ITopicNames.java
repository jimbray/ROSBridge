package xyz.jimbray.rosbridge.messages;

public interface ITopicNames {


    String VREP_CPS = "/vrep_cps";

    String TURTLE1_CMD_VEL = "/turtle1/cmd_vel";

    String CHATTER = "/chatter";

//    String IMAGE_BASE64_STR = "/image/base64_str";
    String IMAGE_BASE64_STR = "/usb_cam/image_raw";

//    String USB_CAM_IMAGE_COMPRESSED = "/camera/image_test";
    String USB_CAM_IMAGE_COMPRESSED = "/usb_cam/image_raw/compressed";
    String USB_CAM_IMAGE_RAW = "/usb_cam/image_raw";

    String IMAGE_ROD_LEFT = "/image_rod_left/compressed";
    String IMAGE_ROD_RIGHT = "/image_rod_right/compressed";
    String IMAGE_TAG = "/image_tag/compressed";
}
