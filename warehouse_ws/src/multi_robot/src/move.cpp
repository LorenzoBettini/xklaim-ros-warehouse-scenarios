#include "ros/ros.h"
#include "trajectory_msgs/JointTrajectory.h"
#include "ros/time.h"
#include "std_msgs/String.h"
#include "sstream"

int setValeurPoint(trajectory_msgs::JointTrajectory* trajectoire,int pos_tab, int val);

int main(int argc, char** argv) {
    ros::init(argc, argv, "state_publisher");
    ros::NodeHandle n;
    ros::Publisher arm_pub = n.advertise<trajectory_msgs::JointTrajectory>("/arm_controller/command",1);
    ros::Rate loop_rate(10);

    trajectory_msgs::JointTrajectory traj;
    trajectory_msgs::JointTrajectoryPoint points_n;

    traj.header.frame_id = "world";
    traj.joint_names.resize(6);
    traj.points.resize(1);

    traj.points[0].positions.resize(6);

    traj.joint_names[0] ="joint1";
    traj.joint_names[1] ="joint2";
    traj.joint_names[2] ="joint3";
    traj.joint_names[3] ="joint4";
    traj.joint_names[4] ="joint5";
    traj.joint_names[5] ="joint6";

    int i(1);

    while(ros::ok()) {

        traj.header.stamp = ros::Time::now();

        for(int j=0; j<6; j++) {
                setValeurPoint(&traj,j,i);
        }

        traj.points[0].time_from_start = ros::Duration(1);

        arm_pub.publish(traj);
        ros::spinOnce();

        loop_rate.sleep();
        i++;
    }

    return 0;
}

int setValeurPoint(trajectory_msgs::JointTrajectory* trajectoire,int pos_tab, int val){
    trajectoire->points[0].positions[pos_tab] = val;
    return 0;
}
