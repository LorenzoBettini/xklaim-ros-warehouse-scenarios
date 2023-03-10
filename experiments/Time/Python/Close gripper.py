#!/usr/bin/python3.8
# license removed for brevity
import rospy
from std_msgs.msg import String
from trajectory_msgs.msg import JointTrajectory
from trajectory_msgs.msg import JointTrajectoryPoint
from control_msgs.msg import JointTrajectoryControllerState
import math
import time 



class CloseGripper:

	def __init__(self):

		self.start = time.time_ns()

		
		rospy.init_node('CloseGripper', anonymous=True)
		
		self.actual = None
			
		self.pub = rospy.Publisher('/gripper_controller/command', JointTrajectory, queue_size=10)

		self.pose_subscriber = rospy.Subscriber('/gripper_controller/state', JointTrajectoryControllerState, self.update_pose)

		self.trajectoryPositions = JointTrajectoryPoint()
		
		self.closeGripper = JointTrajectory()
		
		self.rate = rospy.Rate(0.1)
		
		self.tick = False

	def update_pose(self, data):
		"""Callback function which is called when a new message of type Pose is
		received by the subscriber."""
		global actual

		self.actual = data		
		
		
		delta = 0.0
		tolerance = 0.007
		for i in range(len(self.trajectoryPositions.positions)):
			delta += pow(self.actual.actual.positions[i] - self.trajectoryPositions.positions[i],2)
		norm = math.sqrt(delta)
		if norm < tolerance:
			self.end = time.time_ns()
			print("Total execution time to perform Close gripper process in Python in millis: ", (self.end-self.start)/1000000, "ms.") 
			   

        
	def Publish(self):

		self.closeGripper.joint_names = ["f_joint1","f_joint2"]
		self.trajectoryPositions.positions = [0.0138, -0.0138]
		self.trajectoryPositions.time_from_start = rospy.Duration(20, 0)
		self.closeGripper.points.append(self.trajectoryPositions)

		while self.pub.get_num_connections() < 1:
			pass
		self.pub.publish(self.closeGripper)



if __name__ == '__main__':
	try:
		x = CloseGripper()
		x.Publish()
		rospy.spin()
	except rospy.ROSInterruptException:
		pass

              


