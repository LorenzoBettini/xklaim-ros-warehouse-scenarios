# NeuronBot2 Swarm Examples

Gazebo view:
![](readme_resource/swarm_gazebo.png)

Rviz view:
![](readme_resource/swarm_rviz.png)

TF tree:
![](readme_resource/tf_tree.png)

## Video demo:

https://www.youtube.com/watch?v=KfzoqO9HJKs

## Download and install dependencies
```bash
mkdir -p ~/nb2_swarm_ws/src
cd ~/nb2_swarm_ws/
wget https://raw.githubusercontent.com/Adlink-ROS/neuronbot2_multibot/noetic-devel/nb2_swarm.repos
vcs import src < nb2_swarm.repos
sudo apt update
rosdep update
rosdep install --from-path src --ignore-src -r -y --rosdistro noetic
```

## Build code
```bash
source /opt/ros/noetic/setup.bash
cd ~/nb2_swarm_ws/
catkin_make
```

## Run NeuronBot2 multibot in gazebo
```bash
source /opt/ros/noetic/setup.bash
source ~/nb2_swarm_ws/devel/setup.bash
roslaunch neuronbot2_multibot gazebo_start.launch
```

## See multibot in Rviz view
Open another terminal, follow below instructions.
```bash
source /opt/ros/noetic/setup.bash
source ~/nb2_swarm_ws/devel/setup.bash
roslaunch neuronbot2_multibot multibot_view.launch
```

Note:
1. If it's your first time to run gazebo_start.launch, it may take few minutes to download gazebo model.

2. Running this example in Gazebo simulation requires a little bit high computing power, Intel Core i5/i7 CPU is recommended.
