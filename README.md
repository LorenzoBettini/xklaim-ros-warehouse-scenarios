# xklaim-ros-warehouse-scenarios

This repository provides examples of using X-Klaim and ROS to program multi-robot systems (MRS) in warehouse scenarios. X-Klaim is a programming language that allows for high-level abstractions of MRS behavior, while ROS is a popular robotics framework that provides useful libraries and tools for robot development. The examples demonstrate the feasibility and effectiveness of using X-Klaim and ROS for developing MRSs in warehouse scenarios, and highlight the reusability of code when scaling from simpler to more complex scenarios.


# Install


1. Clone the repository: `git clone https://github.com/LorenzoBettini/xklaim-ros-warehouse-scenarios`.
2. Change directory to: `cd /xklaim-ros-warehouse-scenarios/warehouse_ws`

## Install dependencies
```
$ source /opt/ros/noetic/setup.bash 
$ sudo apt-get install ros-noetic-gazebo-ros ros-noetic-eigen-conversions ros-neotic-object-recognition-msgs ros-neotic-roslint

$ sudo apt-get install ros-noetic-gazebo-ros-pkgs ros-noetic-gazebo-msgs ros-noetic-gazebo-plugins ros-noetic-gazebo-ros-control


$ sudo apt-get install ros-noetic-moveit ros-noetic-moveit-plugins ros-noetic-moveit-planners

$ sudo apt-get install ros-noetc-joint-state-controller
$ ros-noetic-position-controllers ros-noetic-joint-trajectory-controller

$ sudo apt-get install ros-noetic-gmapping

$ sudo apt-get install ros-noetic-map-server

$ sudo apt-get install ros-noetic-joy ros-noetic-teleop-twist-joy \
  ros-noetic-teleop-twist-keyboard ros-noetic-laser-proc \
  ros-noetic-rgbd-launch ros-noetic-rosserial-arduino \
  ros-noetic-rosserial-python ros-noetic-rosserial-client \
  ros-noetic-rosserial-msgs ros-noetic-amcl ros-noetic-map-server \
  ros-noetic-move-base ros-noetic-urdf ros-noetic-xacro \
  ros-noetic-compressed-image-transport ros-noetic-rqt* ros-noetic-rviz \
  ros-noetic-gmapping ros-noetic-navigation ros-noetic-interactive-markers
  
$ sudo apt install ros-noetic-dynamixel-sdk
$ sudo apt install ros-noetic-turtlebot3-msgs
$ sudo apt install ros-noetic-turtlebot3  

$ sudo apt-get install ros-noetic-gazebo-ros-pkgs ros-noetic-gazebo-ros-control

$ sudo apt-get install \
    ros-noetic-gazebo-ros \
    ros-noetic-eigen-conversions \
    ros-noetic-object-recognition-msgs \
    ros-noetic-roslint
$ rosdep update
$ rosdep install --from-paths src --ignore-src -r -y --rosdistro noetic    
```
# Simple Warehouse Scenario
## Build 

1. Change directory to: `cd xklaim-ros-warehouse-scenarios/one_delivery_robot`.
2. Update ROS dependencies: `rosdep update`.
3. Install dependencies: `rosdep install --from-paths src --ignore-src -r -y --rosdistro noetic`.
4. Build the workspace: `catkin_make`.

## Run 
### terminal 1: 

```
$ cd xklaim-ros-warehouse-scenarios/one_delivery_robot
$ ./bridge.sh
```
### terminal 2

```
$ cd xklaim-ros-warehouse-scenarios/one_delivery_robot/
$ ./start.sh
```
# Enriched Warehouse Scenario
## Build 
1. Change directory:`cd /xklaim-ros-warehouse-scenarios/warehouse_ws/src/multi_robot/models`.
2. Copy models to gazebo to add pallets to the warehouse.
```
$ sudo cp -r warehouse_walls ~/.gazebo/models
$ sudo cp -r warehouse_pallet3 ~/.gazebo/models
$ sudo cp -r warehouse_pallet ~/.gazebo/models
```
3. Change directory to: `cd xklaim-ros-warehouse-scenarios/warehouse_ws`.
4. Update ROS dependencies: `rosdep update`.
5. Install dependencies: `rosdep install --from-paths src --ignore-src -r -y --rosdistro noetic`.
6. Build the workspace: `catkin_make`.


## Run 
### terminal 1: 

```
$ cd xklaim-ros-warehouse-scenarios/warehouse_ws
$ ./bridge.sh

```
### terminal 2

```
$ cd xklaim-ros-warehouse-scenarios/warehouse_ws
$ ./start.sh
```


*Simple Warehouse Scenario* [video link](https://www.youtube.com/watch?v=2RDD93x1bGM).<br>
*Enriched Warehouse Scenario* [video link](https://www.youtube.com/watch?v=lTS2582fciU&t=46s).
