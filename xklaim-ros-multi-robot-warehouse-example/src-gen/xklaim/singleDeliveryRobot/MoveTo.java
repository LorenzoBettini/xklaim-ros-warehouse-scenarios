package xklaim.singleDeliveryRobot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.Odometry;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.Exceptions;
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
    final double distanceTolerance = 0.1;
    final double angleTolerance = 0.1;
    final XklaimToRosConnection bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);
    final Publisher pub = new Publisher((("/" + this.robotId) + "/cmd_vel"), "geometry_msgs/Twist", bridge);
    final Twist vel_msg = new Twist();
    final double PI = 3.141592654;
    final double K_l = 0.5;
    final double K_a = 0.5;
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rosMsgNode = data.get("msg");
        Odometry odom = mapper.<Odometry>treeToValue(rosMsgNode, Odometry.class);
        double currentX = odom.pose.pose.position.x;
        double currentY = odom.pose.pose.position.y;
        EulerAngles angle = new EulerAngles(odom.pose.pose.orientation);
        double currentTheta = angle.getYaw();
        double deltaX = ((this.x).doubleValue() - currentX);
        double deltaY = ((this.y).doubleValue() - currentY);
        double agular = Math.atan2(deltaY, deltaX);
        double headingError = (agular - currentTheta);
        if ((headingError > PI)) {
          headingError = (headingError - (2 * PI));
        }
        if ((headingError < (-PI))) {
          headingError = (headingError + (2 * PI));
        }
        double _pow = Math.pow(((this.x).doubleValue() - currentX), 2);
        double _pow_1 = Math.pow(((this.y).doubleValue() - currentY), 2);
        double _plus = (_pow + _pow_1);
        double distance = Math.sqrt(_plus);
        if ((distance > distanceTolerance)) {
          double _abs = Math.abs(headingError);
          boolean _greaterThan = (_abs > angleTolerance);
          if (_greaterThan) {
            vel_msg.linear.x = 0.0;
            vel_msg.angular.z = (K_a * headingError);
          } else {
            vel_msg.linear.x = (K_l * distance);
            vel_msg.angular.z = 0.0;
          }
          pub.publish(vel_msg);
        } else {
          vel_msg.linear.x = 0;
          vel_msg.angular.z = 0;
          pub.publish(vel_msg);
          out(new Tuple(new Object[] {DeliveryRobotConstants.MOVE_TO_COMPLETED}), local);
          bridge.unsubscribe((("/" + this.robotId) + "/odom"));
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate((("/" + this.robotId) + "/odom")).setType("nav_msgs/Odometry").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
