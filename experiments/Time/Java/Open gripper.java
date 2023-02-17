package performance;

import com.fasterxml.jackson.databind.JsonNode;

import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.SubscriptionRequestMsg;

public class OpenGripper {
    

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
			String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";

				// connect to the ROS bridge
			XklaimToRosConnection  bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);

				// initialize a publisher for the topic related to the control of the movements of the arm robot 
			Publisher pub = new Publisher("/gripper_controller/command", "trajectory_msgs/JointTrajectory", bridge);

			double[] jointPositions = {0.0, 0.0};
			String[] names = {
					"f_joint1",
					"f_joint2"
				 };

				// set joint positions for performing the OpenGripper movement of the arm
				 JointTrajectory OpenGripperJointTrajectory = new JointTrajectory().positions(jointPositions).jointNames(names);

		

			pub.publish(OpenGripperJointTrajectory);

			// subscribe to the topic providing the actual status of the arm 
			bridge.subscribe(
				SubscriptionRequestMsg.generate("/gripper_controller/state").setType("control_msgs/JointTrajectoryControllerState").
					setThrottleRate(1).setQueueLength(1),
				(data, stringRep)-> {
					// extract the actual joint positions from the arm's status	
					 JsonNode actual = data.get("msg").get("actual").get("positions");

					double delta = 0.0;
					double tolerance=0.0008;
					for (int i = 0; i < jointPositions.length; i++) {
						delta += Math.pow(actual.get(i).asDouble() - jointPositions[i],2);
					}
					double norm = Math.sqrt(delta);


					if (norm <= tolerance) {
						long elapsedTime = System.nanoTime() - startTime;
						 System.out.print("Total execution time to perform Open gripper process in millis: "
		                + elapsedTime/1000000 + "ms.");
						bridge.unsubscribe("/gripper_controller/state");
					}
				}
			);
		};
		}	

