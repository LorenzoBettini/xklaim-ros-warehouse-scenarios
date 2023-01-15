package xklaim.arm;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.JointTrajectory;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.Conversions;
import ros.Publisher;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;

@SuppressWarnings("all")
public class MoveArmTo extends KlavaProcess {
  private ArmTrajectory armTrajectory;
  
  public MoveArmTo(final ArmTrajectory armTrajectory) {
    this.armTrajectory = armTrajectory;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final String rosbridgeWebsocketURI;
    Tuple _Tuple = new Tuple(new Object[] {"rosbridgeWebsocketURI", String.class});
    read(_Tuple, this.self);
    rosbridgeWebsocketURI = (String) _Tuple.getItem(1);
    final XklaimToRosConnection bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);
    final Publisher pub = new Publisher("/arm_controller/command", "trajectory_msgs/JointTrajectory", bridge);
    final JointTrajectory trajectory = new JointTrajectory().positions(this.armTrajectory.getTrajectoryPoints()).jointNames(
      new String[] { "joint1", "joint2", "joint3", "joint4", "joint5", "joint6" });
    pub.publish(trajectory);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      final JsonNode actual = data.get("msg").get("actual").get("positions");
      double delta = 0.0;
      for (int i = 0; (i < ((List<Double>)Conversions.doWrapArray(this.armTrajectory.getTrajectoryPoints())).size()); i++) {
        double _delta = delta;
        double _asDouble = actual.get(i).asDouble();
        double _get = this.armTrajectory.getTrajectoryPoints()[i];
        double _minus = (_asDouble - _get);
        double _pow = Math.pow(_minus, 2.0);
        delta = (_delta + _pow);
      }
      final double norm = Math.sqrt(delta);
      double _tolerance = this.armTrajectory.getTolerance();
      boolean _lessEqualsThan = (norm <= _tolerance);
      if (_lessEqualsThan) {
        out(new Tuple(new Object[] {"MoveArmToCompleted"}), local);
        bridge.unsubscribe("/arm_controller/state");
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate("/arm_controller/state").setType("control_msgs/JointTrajectoryControllerState").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
