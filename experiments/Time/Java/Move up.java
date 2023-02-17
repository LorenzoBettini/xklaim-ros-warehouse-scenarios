package performance;

import com.fasterxml.jackson.databind.JsonNode;

import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.SubscriptionRequestMsg;

public class MoveUp {
    

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
		long startTime = System.nanoTime();
				
			String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";

				// connect to the ROS bridge
			XklaimToRosConnection  bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);

				// initialize a publisher for the topic related to the control of the movements of the arm robot 
			Publisher pub = new Publisher("/arm_controller/command", "trajectory_msgs/JointTrajectory", bridge);


			double[] jointPositions = {0.0 - 3.14, -0.2862, -0.5000, 3.1400, 1.6613, -0.0142};
			String[] names = {
					"joint1",
					"joint2",
					"joint3",
					"joint4",
					"joint5",
					"joint6"
				 };

				// set joint positions for performing the moveUp movement of the arm
				 JointTrajectory getUpJointTrajectory = new JointTrajectory().positions(jointPositions).jointNames(names);

		
			// publish the moveUp movement trajectory
			pub.publish(getUpJointTrajectory);

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
						 System.out.print("Total execution time to perform Move up process in millis: "
		                + elapsedTime/1000000 + "ms.");
						bridge.unsubscribe("/arm_controller/state");
					}
				}
			);
		};
		}	
		

