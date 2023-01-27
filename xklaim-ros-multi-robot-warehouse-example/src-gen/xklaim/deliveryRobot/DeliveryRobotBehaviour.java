package xklaim.deliveryRobot;

import klava.Locality;
import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class DeliveryRobotBehaviour extends KlavaProcess {
  private String robotId;
  
  private String sector;
  
  private Locality Arm;
  
  public DeliveryRobotBehaviour(final String robotId, final String sector, final Locality Arm) {
    this.robotId = robotId;
    this.sector = sector;
    this.Arm = Arm;
  }
  
  @Override
  public void executeProcess() {
    DeliveryOneItem _deliveryOneItem = new DeliveryOneItem(this.robotId, this.sector, this.Arm);
    eval(_deliveryOneItem, this.self);
    in(new Tuple(new Object[] {DeliveryRobotConstants.AVAILABLE_FOR_DELIVERY}), this.self);
    DeliveryRobotBehaviour _deliveryRobotBehaviour = new DeliveryRobotBehaviour(this.robotId, this.sector, this.Arm);
    eval(_deliveryRobotBehaviour, this.self);
  }
}
