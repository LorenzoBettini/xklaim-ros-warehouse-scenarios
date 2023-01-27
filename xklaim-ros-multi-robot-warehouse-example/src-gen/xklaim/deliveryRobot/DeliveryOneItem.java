package xklaim.deliveryRobot;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;
import xklaim.GlobalConstants;

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
    in(new Tuple(new Object[] {GlobalConstants.ITEM_READY_FOR_DELIVERY, this.sector}), this.Arm);
    final double x = (-0.21);
    final double y = 0.31;
    MoveTo _moveTo = new MoveTo(this.robotId, Double.valueOf(x), Double.valueOf(y));
    eval(_moveTo, this.self);
    in(new Tuple(new Object[] {DeliveryRobotConstants.MOVE_TO_COMPLETED}), this.self);
    out(new Tuple(new Object[] {GlobalConstants.DELIVERY_ROBOT_ARRIVED}), this.Arm);
    String itemId = null;
    String itemType = null;
    Tuple _Tuple = new Tuple(new Object[] {GlobalConstants.GRIPPER_OPENED, String.class, String.class});
    in(_Tuple, this.Arm);
    itemId = (String) _Tuple.getItem(1);
    itemType = (String) _Tuple.getItem(2);
    WaitForItem _waitForItem = new WaitForItem(this.robotId);
    eval(_waitForItem, this.self);
    in(new Tuple(new Object[] {DeliveryRobotConstants.ITEM_LOADED}), this.self);
    Double x2 = null;
    Double y2 = null;
    Tuple _Tuple_1 = new Tuple(new Object[] {GlobalConstants.TYPE_2_DESTINATION, itemType, Double.class, Double.class});
    read(_Tuple_1, this.self);
    x2 = (Double) _Tuple_1.getItem(2);
    y2 = (Double) _Tuple_1.getItem(3);
    MoveTo _moveTo_1 = new MoveTo(this.robotId, x2, y2);
    eval(_moveTo_1, this.self);
    in(new Tuple(new Object[] {DeliveryRobotConstants.MOVE_TO_COMPLETED}), this.self);
    out(new Tuple(new Object[] {GlobalConstants.ITEM_DELIVERED, itemId, x2, y2}), this.self);
    out(new Tuple(new Object[] {DeliveryRobotConstants.AVAILABLE_FOR_DELIVERY}), this.self);
  }
}
