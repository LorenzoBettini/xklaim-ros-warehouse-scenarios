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
public class UseGripper extends KlavaProcess {
  private GripperTrajectory gripperTrajectory;
  
  public UseGripper(final GripperTrajectory gripperTrajectory) {
    this.gripperTrajectory = gripperTrajectory;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final XklaimToRosConnection bridge = new XklaimToRosConnection(ArmConstants.ROS_BRIDGE_SOCKET_URI);
    final Publisher pub = new Publisher("/gripper_controller/command", "trajectory_msgs/JointTrajectory", bridge);
    final JointTrajectory trajectory = new JointTrajectory().positions(this.gripperTrajectory.getTrajectoryPoints()).jointNames(
      new String[] { "f_joint1", "f_joint2" });
    pub.publish(trajectory);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      final JsonNode actual = data.get("msg").get("actual").get("positions");
      double delta = 0.0;
      for (int i = 0; (i < ((List<Double>)Conversions.doWrapArray(this.gripperTrajectory.getTrajectoryPoints())).size()); i++) {
        double _delta = delta;
        double _asDouble = actual.get(i).asDouble();
        double _get = this.gripperTrajectory.getTrajectoryPoints()[i];
        double _minus = (_asDouble - _get);
        double _pow = Math.pow(_minus, 2.0);
        delta = (_delta + _pow);
      }
      final double norm = Math.sqrt(delta);
      double _tolerance = this.gripperTrajectory.getTolerance();
      boolean _lessEqualsThan = (norm <= _tolerance);
      if (_lessEqualsThan) {
        out(new Tuple(new Object[] {ArmConstants.USE_GRIPPER_COMPLETED}), local);
        bridge.unsubscribe("/gripper_controller/state");
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate("/gripper_controller/state").setType(
        "control_msgs/JointTrajectoryControllerState").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
