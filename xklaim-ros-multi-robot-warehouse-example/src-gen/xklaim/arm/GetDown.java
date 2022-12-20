package xklaim.arm;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.List;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import ros.Publisher;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;

@SuppressWarnings("all")
public class GetDown extends KlavaProcess {
  private String rosbridgeWebsocketURI;
  
  private Double x;
  
  private Double y;
  
  private double[] positions;
  
  public GetDown(final String rosbridgeWebsocketURI, final Double x, final Double y, final double[] positions) {
    this.rosbridgeWebsocketURI = rosbridgeWebsocketURI;
    this.x = x;
    this.y = y;
    this.positions = positions;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final XklaimToRosConnection bridge = new XklaimToRosConnection(this.rosbridgeWebsocketURI);
    final Publisher pub = new Publisher("/arm_controller/command", "trajectory_msgs/JointTrajectory", bridge);
    double _divide = DoubleExtensions.operator_divide(this.y, this.x);
    double _atan = Math.atan(_divide);
    double _minus = (_atan - 3.14);
    double _get = this.positions[0];
    double _get_1 = this.positions[1];
    double _get_2 = this.positions[2];
    double _get_3 = this.positions[3];
    double _get_4 = this.positions[4];
    final List<Double> trajectoryPositions = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(_minus), Double.valueOf(_get), Double.valueOf(_get_1), Double.valueOf(_get_2), Double.valueOf(_get_3), Double.valueOf(_get_4)));
    final JointTrajectory movement = new JointTrajectory().positions(((double[])Conversions.unwrapArray(trajectoryPositions, double.class))).jointNames(
      new String[] { "joint1", "joint2", "joint3", "joint4", "joint5", "joint6" });
    pub.publish(movement);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      final JsonNode actual = data.get("msg").get("actual").get("positions");
      double delta = 0.0;
      final double tolerance = 0.000001;
      for (int i = 0; (i < trajectoryPositions.size()); i++) {
        double _delta = delta;
        double _asDouble = actual.get(i).asDouble();
        Double _get_5 = trajectoryPositions.get(i);
        double _minus_1 = (_asDouble - (_get_5).doubleValue());
        double _pow = Math.pow(_minus_1, 2.0);
        delta = (_delta + _pow);
      }
      final double norm = Math.sqrt(delta);
      if ((norm <= tolerance)) {
        out(new Tuple(new Object[] {"getDownCompleted"}), local);
        bridge.unsubscribe("/arm_controller/state");
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate("/arm_controller/state").setType("control_msgs/JointTrajectoryControllerState").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
