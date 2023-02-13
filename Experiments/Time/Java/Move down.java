package performance;

import com.fasterxml.jackson.databind.JsonNode;

import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.SubscriptionRequestMsg;

public class MoveDown {
    

	public static void main(String[] args) {
//		// TODO Auto-generated method stub
			long startTime = System.nanoTime();
				
			String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";

				// connect to the ROS bridge
			XklaimToRosConnection  bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);

				// initialize a publisher for the topic related to the control of the movements of the arm robot 
			Publisher pub = new Publisher("/arm_controller/command", "trajectory_msgs/JointTrajectory", bridge);


			double[] trajectoryPositions = {0.0 - 3.14, -0.2169, -0.5822, 3.14, 1.66, -0.01412};
			String[] names = {
					"joint1",
					"joint2",
					"joint3",
					"joint4",
					"joint5",
					"joint6"
				 };

				// set joint positions for performing the first movement of the arm
				 JointTrajectory firstMovement = new JointTrajectory().positions(trajectoryPositions).jointNames(names);

			// set joint positions for performing the second movement of the arm
			 double[] secondTrajectoryPositions = {0.0 - 3.14, -0.9975, -0.4970, 3.1400, 1.6613, -0.0142};
			 	JointTrajectory secondMovement = new JointTrajectory().positions(secondTrajectoryPositions).jointNames(names);

			// publish the first movement trajectory
			pub.publish(firstMovement);

			// subscribe to the topic providing the actual status of the arm 
			bridge.subscribe(
				SubscriptionRequestMsg.generate("/arm_controller/state").setType("control_msgs/JointTrajectoryControllerState").
					setThrottleRate(1).setQueueLength(1),
				(data, stringRep)-> {
					// extract the actual joint positions from the arm's status	
					 JsonNode actual = data.get("msg").get("actual").get("positions");

					// calculate the delta between the actual joint positions and the destination positions
					// to measure the completeness of the first and second movements
					double delta1 = 0.0;
					double delta2 = 0.0;
					double tolerance1 = 0.000001;
					double tolerance2 = 0.00001;
					for (int i = 0; i < trajectoryPositions.length; i++) {
						delta1 += Math.abs(actual.get(i).asDouble() - trajectoryPositions[i]);
						delta2 += Math.pow(actual.get(i).asDouble() - secondTrajectoryPositions[i], 2);
					}
					double norm = Math.sqrt(delta2);

					if (delta1 <= tolerance1) { /* the arm has completed the first movement */
						// publish the second movement trajectory
						pub.publish(secondMovement);
					}

					if (norm <= tolerance2) { /* the arm has completed the second movement */
						long elapsedTime = System.nanoTime() - startTime;
						 System.out.print("Total execution time to perform Move down process in millis: "
		                + elapsedTime/1000000 + "ms.");
						bridge.unsubscribe("/arm_controller/state");
					}
				}
			);
		};
		}	
		

