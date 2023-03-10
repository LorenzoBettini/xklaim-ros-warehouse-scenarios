package xklaim.singleDeliveryRobot;

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

@SuppressWarnings("all")
public class WaitForItem extends KlavaProcess {
  private String robotId;
  
  public WaitForItem(final String robotId) {
    this.robotId = robotId;
  }
  
  @Override
  public void executeProcess() {
    final Locality local = this.self;
    final XklaimToRosConnection bridge = new XklaimToRosConnection(DeliveryRobotConstants.ROS_BRIDGE_SOCKET_URI);
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
