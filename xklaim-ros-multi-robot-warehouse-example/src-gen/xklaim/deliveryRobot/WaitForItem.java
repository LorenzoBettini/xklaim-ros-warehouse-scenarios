package xklaim.deliveryRobot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import messages.ContactState;
import messages.ContactsState;
import messages.XklaimToRosConnection;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import ros.RosListenDelegate;
import ros.SubscriptionRequestMsg;
import xklaim.GlobalConstants;

@SuppressWarnings("all")
public class WaitForItem extends KlavaProcess {
  private String robotId;
  
  public WaitForItem(final String robotId) {
    this.robotId = robotId;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final String rosbridgeWebsocketURI;
    Tuple _Tuple = new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, String.class});
    read(_Tuple, this.self);
    rosbridgeWebsocketURI = (String) _Tuple.getItem(1);
    final XklaimToRosConnection bridge = new XklaimToRosConnection(rosbridgeWebsocketURI);
    final RosListenDelegate _function = (JsonNode data, String stringRep) -> {
      try {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rosMsgNode = data.get("msg");
        ContactsState state = mapper.<ContactsState>treeToValue(rosMsgNode, ContactsState.class);
        if (((!((List<ContactState>)Conversions.doWrapArray(state.states)).isEmpty()) && ((state.states[0]).total_wrench.force.z != 0.0))) {
          out(new Tuple(new Object[] {DeliveryRobotConstants.ITEM_LOADED}), local);
          bridge.unsubscribe((("/" + this.robotId) + "/pressure_sensor_state"));
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    bridge.subscribe(
      SubscriptionRequestMsg.generate((("/" + this.robotId) + "/pressure_sensor_state")).setType("gazebo_msgs/ContactsState").setThrottleRate(Integer.valueOf(1)).setQueueLength(Integer.valueOf(1)), _function);
  }
}
