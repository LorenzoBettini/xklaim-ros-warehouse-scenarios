package xklaim;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.ModelState;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import ros.Publisher;

@SuppressWarnings("all")
public class Unload extends KlavaProcess {
  private Locality DeliveryRobot;
  
  private double x;
  
  private double y;
  
  public Unload(final Locality DeliveryRobot, final double x, final double y) {
    this.DeliveryRobot = DeliveryRobot;
    this.x = x;
    this.y = y;
  }
  
  @Override
  public void executeProcess() {
    try {
      final String rosbridgeWebsocketURI;
      Tuple _Tuple = new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, String.class});
      read(_Tuple, this.self);
      rosbridgeWebsocketURI = (String) _Tuple.getItem(1);
      double poseX = this.x;
      double poseY = this.y;
      if ((this.x > 0)) {
        double _poseX = poseX;
        poseX = (_poseX + 0.3);
      } else {
        double _poseX_1 = poseX;
        poseX = (_poseX_1 - 0.3);
      }
      if ((this.y < 0)) {
        double _poseY = poseY;
        poseY = (_poseY + 0.5);
      } else {
        double _poseY_1 = poseY;
        poseY = (_poseY_1 - 0.5);
      }
      while (true) {
        {
          String itemId = null;
          Tuple _Tuple_1 = new Tuple(new Object[] {"itemDelivered", String.class, this.x, this.y});
          in(_Tuple_1, this.DeliveryRobot);
          itemId = (String) _Tuple_1.getItem(1);
          Thread.sleep(2000);
          final XklaimToRosConnection bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);
          final Publisher Pose_item = new Publisher("/gazebo/set_model_state", "gazebo_msgs/ModelState", bridge);
          final ModelState modelstate = new ModelState();
          modelstate.pose.position.x = poseX;
          modelstate.pose.position.y = poseY;
          modelstate.model_name = itemId;
          modelstate.reference_frame = "world";
          Pose_item.publish(modelstate);
          InputOutput.<String>println((((((("############ Item " + itemId) + " posed at (") + Double.valueOf(poseX)) + ",") + Double.valueOf(poseY)) + ")"));
          if ((this.y < 0)) {
            double _poseY_2 = poseY;
            poseY = (_poseY_2 + 0.3);
          } else {
            double _poseY_3 = poseY;
            poseY = (_poseY_3 - 0.3);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
