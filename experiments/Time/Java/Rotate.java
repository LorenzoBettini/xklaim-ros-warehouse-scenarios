package performance;

import com.fasterxml.jackson.databind.JsonNode;

import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.SubscriptionRequestMsg;

public class Rotate {
    

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
			String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";

				// connect to the ROS bridge
			XklaimToRosConnection  bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);

				// initialize a publisher for the topic related to the control of the movements of the arm robot 
			Publisher pub = new Publisher("/arm_controller/command", "trajectory_msgs/JointTrajectory", bridge);


			double[] jointPositions = {-0.9546, -0.20, -0.7241, 3.1400, 1.6613, -0.0142};
			String[] names = {
					"joint1",
					"joint2",
					"joint3",
					"joint4",
					"joint5",
					"joint6"
				 };

				// set joint positions for performing the rotate movement of the arm
				 JointTrajectory rotateJointTrajectory = new JointTrajectory().positions(jointPositions).jointNames(names);

		
			// publish the rotate movement trajectory
			pub.publish(rotateJointTrajectory);

			// subscribe to the topic providing the actual status of the arm 
			bridge.subscribe(
				SubscriptionRequestMsg.generate("/arm_controller/state").setType("control_msgs/JointTrajectoryControllerState").
					setThrottleRate(1).setQueueLength(1),
				(data, stringRep)-> {
					// extract the actual joint positions from the arm's status	
					 JsonNode actual = data.get("msg").get("actual").get("positions");

					double delta = 0.0;
					double tolerance=0.008;
					for (int i = 0; i < jointPositions.length; i++) {
						delta += Math.pow(actual.get(i).asDouble() - jointPositions[i],2);
					}
					double norm = Math.sqrt(delta);


					if (norm <= tolerance) { 
						long elapsedTime = System.nanoTime() - startTime;
						 System.out.print("Total execution time to perform Rotate process in millis: "
		                + elapsedTime/1000000 + "ms.");
						bridge.unsubscribe("/arm_controller/state");
					}
				}
			);
		};
		}	

