package xklaim.singleDeliveryRobot;

import com.fasterxml.jackson.databind.JsonNode;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.XklaimToRosConnection;
import ros.Publisher;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;
import ros.msgs.geometry_msgs.Twist;
import xklaim.GlobalConstants;

@SuppressWarnings("all")
public class MoveTo extends KlavaProcess {
  private String robotId;
  
  private Double x;
  
  private Double y;
  
  public MoveTo(final String robotId, final Double x, final Double y) {
    this.robotId = robotId;
    this.x = x;
    this.y = y;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final String rosbridgeWebsocketURI;
    Tuple _Tuple = new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, String.class});
    read(_Tuple, this.self);
    rosbridgeWebsocketURI = (String) _Tuple.getItem(1);
    final XklaimToRosConnection bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);
    final Publisher pub = new Publisher((("/" + this.robotId) + "/cmd_vel"), "geometry_msgs/Twist", bridge);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      final JsonNode pose = data.get("msg").get("pose").get("pose");
      final JsonNode position = pose.get("position");
      final double currentX = position.get("x").asDouble();
      final double currentY = position.get("y").asDouble();
      final JsonNode orientation = pose.get("orientation");
      final double qx = orientation.get("x").asDouble();
      final double qy = orientation.get("y").asDouble();
      final double qz = orientation.get("z").asDouble();
      final double qw = orientation.get("w").asDouble();
      final double siny_cosp = (2 * ((qw * qz) + (qx * qy)));
      final double cosy_cosp = (1 - (2 * ((qy * qy) + (qz * qz))));
      final double currentTheta = Math.atan2(siny_cosp, cosy_cosp);
      double _pow = Math.pow((currentX - (this.x).doubleValue()), 2);
      double _pow_1 = Math.pow((currentY - (this.y).doubleValue()), 2);
      double _plus = (_pow + _pow_1);
      final double distance = Math.sqrt(_plus);
      final double tolerance = 0.16;
      if ((distance >= tolerance)) {
        double linearVelocity = 1.5;
        if ((distance < 1)) {
          double _linearVelocity = linearVelocity;
          linearVelocity = (_linearVelocity * distance);
        }
        final double steeringAngle = Math.atan2(((this.y).doubleValue() - currentY), ((this.x).doubleValue() - currentX));
        final double angularVelocity = (1.0 * (steeringAngle - currentTheta));
        final Twist destination = new Twist();
        destination.linear.x = linearVelocity;
        destination.angular.z = angularVelocity;
        pub.publish(destination);
      } else {
        final Twist twistMsg = new Twist();
        pub.publish(twistMsg);
        out(new Tuple(new Object[] {DeliveryRobotConstants.MOVE_TO_COMPLETED}), local);
        bridge.unsubscribe((("/" + this.robotId) + "/odom"));
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate((("/" + this.robotId) + "/odom")).setType("nav_msgs/Odometry").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
