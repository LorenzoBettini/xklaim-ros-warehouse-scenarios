#!/usr/bin/python3.8
# license removed for brevity
import rospy
from std_msgs.msg import String
from trajectory_msgs.msg import JointTrajectory
from trajectory_msgs.msg import JointTrajectoryPoint
from control_msgs.msg import JointTrajectoryControllerState
import math
import time 



class MoveToInitialPosition:

	def __init__(self):

		self.start = time.time_ns()
		
		rospy.init_node('MoveToInitialPosition', anonymous=True)
		
		self.actual = None
			
		self.pub = rospy.Publisher('/arm_controller/command', JointTrajectory, queue_size=10)


		self.pose_subscriber = rospy.Subscriber('/arm_controller/state', JointTrajectoryControllerState, self.update_pose)

		self.trajectoryPositions = JointTrajectoryPoint()
		
		self.firstMovement = JointTrajectory()
		
		self.secondTrajectoryPositions = JointTrajectoryPoint()
		
		self.secondMovement = JointTrajectory()
		
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
			self.end = time.time_ns()
			print("Total execution time to perform Move to initial position process in Python in millis: ", (self.end-self.start)/1000000, "ms.") 
			   

        
	def Publish(self):

		#################### First mouvement ###############################

		self.firstMovement.joint_names = ["joint1","joint2","joint3","joint4","joint5","joint6"]
		self.trajectoryPositions.positions = [0.000, 0.000, 0.000, 0.000, 0.000, 0.000]
		self.trajectoryPositions.time_from_start = rospy.Duration(20, 0)
		self.firstMovement.points.append(self.trajectoryPositions)
		#################### Second mouvement ###############################

		while self.pub.get_num_connections() < 1:
			pass
		self.pub.publish(self.firstMovement)



if __name__ == '__main__':
	try:
		x = MoveToInitialPosition()
		x.Publish()
		rospy.spin()
	except rospy.ROSInterruptException:
		pass

