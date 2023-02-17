#!/usr/bin/python3.8
# license removed for brevity
import rospy
from std_msgs.msg import String
from trajectory_msgs.msg import JointTrajectory
from trajectory_msgs.msg import JointTrajectoryPoint
from control_msgs.msg import JointTrajectoryControllerState
import math
import time 
import tracemalloc
from guppy import hpy


class MoveDown:
	def __init__(self):
		self.start = time.time_ns()
		
		rospy.init_node('MoveDown', anonymous=True)
		
		self.actual = None
			
		self.pub = rospy.Publisher('/arm_controller/command', JointTrajectory, queue_size=10)

		self.pose_subscriber = rospy.Subscriber('/arm_controller/state', JointTrajectoryControllerState, self.update_pose)

		self.trajectoryPositions = JointTrajectoryPoint()
		
		self.moveHalf = JointTrajectory()
		
		self.secondTrajectoryPositions = JointTrajectoryPoint()
		
		self.moveDown = JointTrajectory()
		
		self.rate = rospy.Rate(0.1)
		
		self.tick = False
	def update_pose(self, data):
		"""Callback function which is called when a new message of type Pose is
		received by the subscriber."""
		global actual

		self.actual = data		
		
		delta1 = 0.0
		delta2 = 0.0
		tolerance1 = 0.000001
		tolerance2 = 0.00001
		for i in range(len(self.trajectoryPositions.positions)):
			delta1 += abs(self.actual.actual.positions[i] - self.trajectoryPositions.positions[i])
			delta2 += pow(self.actual.actual.positions[i] - self.secondTrajectoryPositions.positions[i],2)
		norm = math.sqrt(delta2)	
		if delta1 <= tolerance1:
			self.tick = True	
		if norm < tolerance2:
			self.end = time.time_ns()
			print("Total execution time to perform Move down process in Python in millis: ", (self.end-self.start)/1000000, "ms.") 
			   

	def Publish(self):

		#################### First mouvement ###############################

		self.moveHalf.joint_names = ["joint1","joint2","joint3","joint4","joint5","joint6"]
		self.trajectoryPositions.positions = [0.0 - 3.14, -0.2862, -0.5000, 3.1400, 1.6613, -0.0142]
		self.trajectoryPositions.time_from_start = rospy.Duration(20, 0)
		self.moveHalf.points.append(self.trajectoryPositions)
		#################### Second mouvement ###############################
		
		self.moveDown.joint_names = ["joint1","joint2","joint3","joint4","joint5","joint6"]
		self.secondTrajectoryPositions.positions = [0.0 - 3.14, -0.9975, -0.4970, 3.1400, 1.6613, -0.0142]
		self.secondTrajectoryPositions.time_from_start = rospy.Duration(20, 0)
		self.moveDown.points.append(self.secondTrajectoryPositions)

		while self.pub.get_num_connections() < 1:
			pass
		self.pub.publish(self.moveHalf)		
		while self.tick == False:
			pass
		self.pub.publish(self.moveDown)			


if __name__ == '__main__':
	try:
		x = MoveDown()
		x.Publish()
		rospy.spin()
	except rospy.ROSInterruptException:
		pass


              
