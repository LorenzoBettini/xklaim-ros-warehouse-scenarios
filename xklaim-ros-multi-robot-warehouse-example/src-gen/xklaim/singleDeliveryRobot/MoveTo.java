package xklaim.singleDeliveryRobot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.PoseStamped;
import messages.PoseWithCovarianceStamped;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import ros.Publisher;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;
import ros.msgs.geometry_msgs.Twist;
import xklaim.GlobalConstants;

@SuppressWarnings("all")
public class MoveTo extends KlavaProcess {
  private String robotId;
  
  private String sector;
  
  private Double x;
  
  private Double y;
  
  public MoveTo(final String robotId, final String sector, final Double x, final Double y) {
    this.robotId = robotId;
    this.sector = sector;
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
    final PoseStamped destination = new PoseStamped().headerFrameId("world").posePositionXY((this.x).doubleValue(), (this.y).doubleValue()).poseOrientation(1.0);
    final Twist twistMsg2 = new Twist();
    twistMsg2.linear.x = 1.5;
    twistMsg2.angular.z = 0.5;
    pub.publish(twistMsg2);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rosMsgNode = data.get("msg");
        PoseWithCovarianceStamped current_position = mapper.<PoseWithCovarianceStamped>treeToValue(rosMsgNode, PoseWithCovarianceStamped.class);
        final double tolerance = 0.16;
        double deltaX = Math.abs((current_position.pose.pose.position.x - destination.pose.position.x));
        double deltaY = Math.abs((current_position.pose.pose.position.y - destination.pose.position.y));
        if (((deltaX <= tolerance) && (deltaY <= tolerance))) {
          final Publisher pubvel = new Publisher((("/" + this.robotId) + "/cmd_vel"), "geometry_msgs/Twist", bridge);
          final Twist twistMsg = new Twist();
          pubvel.publish(twistMsg);
          out(new Tuple(new Object[] {DeliveryRobotConstants.MOVE_TO_COMPLETED}), local);
          bridge.unsubscribe((("/" + this.robotId) + "/amcl_pose"));
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate((("/" + this.robotId) + "/amcl_pose")).setType(
        "geometry_msgs/PoseWithCovarianceStamped").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
    final RosListenDelegate _function_1 = (JsonNode data, String stringRep) -> {
      final JsonNode pose = data.get("msg").get("pose").get("pose");
      final JsonNode position = pose.get("position");
      final double x = position.get("x").asDouble();
      final double y = position.get("y").asDouble();
      final JsonNode orientation = pose.get("orientation");
      final double qx = orientation.get("x").asDouble();
      final double qy = orientation.get("y").asDouble();
      final double qz = orientation.get("z").asDouble();
      final double qw = orientation.get("w").asDouble();
      final double siny_cosp = (2 * ((qw * qz) + (qx * qy)));
      final double cosy_cosp = (1 - (2 * ((qy * qy) + (qz * qz))));
      final double theta = Math.atan2(siny_cosp, cosy_cosp);
      InputOutput.<String>println(((((("position x:" + Double.valueOf(x)) + " y:") + Double.valueOf(y)) + "theta:") + Double.valueOf(theta)));
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate((("/" + this.robotId) + "/odom")).setType("nav_msgs/Odometry").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function_1);
  }
}
