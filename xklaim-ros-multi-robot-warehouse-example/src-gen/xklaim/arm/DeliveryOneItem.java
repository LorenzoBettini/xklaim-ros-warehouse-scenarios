package xklaim.arm;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import xklaim.deliveryRobot.MoveTo;
import xklaim.deliveryRobot.WaitForItem;

@SuppressWarnings("all")
public class DeliveryOneItem extends KlavaProcess {
  private String robotId;
  
  private String sector;
  
  private Locality Arm;
  
  public DeliveryOneItem(final String robotId, final String sector, final Locality Arm) {
    this.robotId = robotId;
    this.sector = sector;
    this.Arm = Arm;
  }
  
  @Override
  public void executeProcess() {
    final String rosbridgeWebsocketURI;
    Tuple _Tuple = new Tuple(new Object[] {"rosbridgeWebsocketURI", String.class});
    read(_Tuple, this.self);
    rosbridgeWebsocketURI = (String) _Tuple.getItem(1);
    in(new Tuple(new Object[] {"itemReadyForTheDelivery", this.sector}), this.Arm);
    final double x = (-0.21);
    final double y = 0.31;
    MoveTo _moveTo = new MoveTo(this.robotId, this.sector, Double.valueOf(x), Double.valueOf(y));
    eval(_moveTo, this.self);
    in(new Tuple(new Object[] {"moveToCompleted"}), this.self);
    out(new Tuple(new Object[] {"deliveryRobotArrived"}), this.Arm);
    String itemId = null;
    String itemType = null;
    Tuple _Tuple_1 = new Tuple(new Object[] {"gripperOpened", String.class, String.class});
    in(_Tuple_1, this.Arm);
    itemId = (String) _Tuple_1.getItem(1);
    itemType = (String) _Tuple_1.getItem(2);
    WaitForItem _waitForItem = new WaitForItem(this.robotId);
    eval(_waitForItem, this.self);
    in(new Tuple(new Object[] {"itemLoaded"}), this.self);
    Double x2 = null;
    Double y2 = null;
    Tuple _Tuple_2 = new Tuple(new Object[] {"type2destination", itemType, Double.class, Double.class});
    read(_Tuple_2, this.self);
    x2 = (Double) _Tuple_2.getItem(2);
    y2 = (Double) _Tuple_2.getItem(3);
    MoveTo _moveTo_1 = new MoveTo(this.robotId, this.sector, x2, y2);
    eval(_moveTo_1, this.self);
    in(new Tuple(new Object[] {"moveToCompleted"}), this.self);
    String coordinates = (((("(" + x2) + ",") + y2) + ")");
    out(new Tuple(new Object[] {"itemDelivered", itemId, coordinates}), this.self);
    out(new Tuple(new Object[] {"availableForDelivery"}), this.self);
  }
}
