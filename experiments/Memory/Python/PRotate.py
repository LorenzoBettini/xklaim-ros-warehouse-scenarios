#!/usr/bin/python3.8
# license removed for brevity
import rospy
from std_msgs.msg import String
from trajectory_msgs.msg import JointTrajectory
from trajectory_msgs.msg import JointTrajectoryPoint
from control_msgs.msg import JointTrajectoryControllerState
import math
import time 


class Rotate:

	def __init__(self):
		
		rospy.init_node('Rotate', anonymous=True)
		
		self.actual = None
			
		self.pub = rospy.Publisher('/arm_controller/command', JointTrajectory, queue_size=10)
.
		self.pose_subscriber = rospy.Subscriber('/arm_controller/state', JointTrajectoryControllerState, self.update_pose)

		self.trajectoryPositions = JointTrajectoryPoint()
		
		self.rotate = JointTrajectory()
		
		self.rate = rospy.Rate(0.1)
		
		self.tick = False

	def update_pose(self, data):
		"""Callback function which is called when a new message of type Pose is
		received by the subscriber."""
		global actual

		self.actual = data		
		
		delta = 0.0
		tolerance = 0.008
		for i in range(len(self.trajectoryPositions.positions)):
			delta += pow(self.actual.actual.positions[i] - self.trajectoryPositions.positions[i],2)
		norm = math.sqrt(delta)
		if norm < tolerance:
			print("stop")
			   

        
	def Publish(self):


		self.rotate.joint_names = ["joint1","joint2","joint3","joint4","joint5","joint6"]
		self.trajectoryPositions.positions = [-0.9546, -0.20, -0.7241, 3.1400, 1.6613, -0.0142]
		self.trajectoryPositions.time_from_start = rospy.Duration(20, 0)
		self.rotate.points.append(self.trajectoryPositions)


		while self.pub.get_num_connections() < 1:
			pass
		self.pub.publish(self.rotate)



if __name__ == '__main__':
	try:
		x = Rotate()
		x.Publish()
		rospy.spin()
	except rospy.ROSInterruptException:
		pass

              


