package xklaim.arm;

import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class ArmBehaviour extends KlavaProcess {
  private String rosbridgeWebsocketURI;
  
  public ArmBehaviour(final String rosbridgeWebsocketURI) {
    this.rosbridgeWebsocketURI = rosbridgeWebsocketURI;
  }
  
  @Override
  public void executeProcess() {
    PickAndReleaseOneItem _pickAndReleaseOneItem = new PickAndReleaseOneItem(this.rosbridgeWebsocketURI);
    eval(_pickAndReleaseOneItem, this.self);
    in(new Tuple(new Object[] {"initialPosition"}), this.self);
    ArmBehaviour _armBehaviour = new ArmBehaviour(this.rosbridgeWebsocketURI);
    eval(_armBehaviour, this.self);
  }
}
