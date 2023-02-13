package performance;

import com.fasterxml.jackson.databind.JsonNode;

import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.SubscriptionRequestMsg;

public class Closegripper {
    

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.nanoTime();
			String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";

				// connect to the ROS bridge
			XklaimToRosConnection  bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);

				// initialize a publisher for the topic related to the control of the movements of the arm robot 
			Publisher pub = new Publisher("/gripper_controller/command", "trajectory_msgs/JointTrajectory", bridge);

			double[] jointPositions = {0.0138, -0.0138};
			String[] names = {
					"f_joint1",
					"f_joint2"
				 };

				// set joint positions for performing the first movement of the arm
				 JointTrajectory getUpJointTrajectory = new JointTrajectory().positions(jointPositions).jointNames(names);

		
			// publish the first movement trajectory
			pub.publish(getUpJointTrajectory);

			// subscribe to the topic providing the actual status of the arm 
			bridge.subscribe(
				SubscriptionRequestMsg.generate("/gripper_controller/state").setType("control_msgs/JointTrajectoryControllerState").
					setThrottleRate(1).setQueueLength(1),
				(data, stringRep)-> {
					// extract the actual joint positions from the arm's status	
					 JsonNode actual = data.get("msg").get("actual").get("positions");

					// calculate the delta between the actual joint positions and the destination positions
					// to measure the completeness of the first and second movements
					double delta = 0.0;
					double tolerance=0.007;
					for (int i = 0; i < jointPositions.length; i++) {
						delta += Math.pow(actual.get(i).asDouble() - jointPositions[i],2);
					}
					double norm = Math.sqrt(delta);


					if (norm <= tolerance) { /* the arm has completed the second movement */
						long elapsedTime = System.nanoTime() - startTime;
						 System.out.print("Total execution time to perform Close gripper process in millis: "
		                + elapsedTime/1000000 + "ms.");
						bridge.unsubscribe("/gripper_controller/state");
					}
				}
			);
		};
		}	

