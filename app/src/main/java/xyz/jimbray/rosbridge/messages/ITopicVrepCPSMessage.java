package xyz.jimbray.rosbridge.messages;

public interface ITopicVrepCPSMessage {


    String MESSAGE_ROBOT_LOAD_START = "robot_load_start"; // 上下料机器人开始抓取石材

    String MESSAGE_ROBOT_LOAD_STARTED = "robot_load_started"; // 上下料机器人开始抓取石材

    String MESSAGE_ROBOT_LOADED = "robot_loaded"; // 上下料机器人抓取石材完毕

    String MESSAGE_ROBOT_CARRY_COM_START = "robot_carry_come_start"; // 上下料机器人运输石材中

    String MESSAGE_ROBOT_CARRY_COMED = "robot_carry_comed"; // 上下料机器人运石材到指定点

    String MESSAGE_ROBOT_LOAD_PLANE_HANDOVER1 = "robot_load_plane_handover1"; // 抓取石材到指定位置

    String MESSAGE_ROBOT_LOAD_PLANE_HANDOVER2 = "robot_load_plane_handover2"; // 安装机器人抓取石材完毕

    String MESSAGE_ROBOT_LOAD_LEAVE = "robot_load_leave"; // 上下料机器人离开，安装机器人开始安装

    String MESSAGE_ROBOT_PLANE_INSTALLED = "robot_plane_installed"; // 石材安装完毕

}
