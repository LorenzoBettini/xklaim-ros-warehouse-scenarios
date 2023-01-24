package xklaim;

import klava.LogicalLocality;
import klava.PhysicalLocality;
import klava.Tuple;
import klava.topology.ClientNode;
import klava.topology.KlavaNodeCoordinator;
import klava.topology.LogicalNet;
import org.mikado.imc.common.IMCException;
import xklaim.arm.ArmBehaviour;
import xklaim.arm.DeliveryRobotBehaviour;

@SuppressWarnings("all")
public class MRS_one_delivery extends LogicalNet {
  private static final LogicalLocality Arm = new LogicalLocality("Arm");
  
  private static final LogicalLocality DeliveryRobot1 = new LogicalLocality("DeliveryRobot1");
  
  private static final LogicalLocality SimuationHandler = new LogicalLocality("SimuationHandler");
  
  public static class Arm extends ClientNode {
    private static class ArmProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        out(new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, "ws://0.0.0.0:9090"}), this.self);
        ArmBehaviour _armBehaviour = new ArmBehaviour();
        eval(_armBehaviour, this.self);
      }
    }
    
    public Arm() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Arm"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS_one_delivery.Arm.ArmProcess());
    }
  }
  
  public static class DeliveryRobot1 extends ClientNode {
    private static class DeliveryRobot1Process extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        final String robotId = "robot1";
        final String sector = "sector1";
        out(new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, "ws://0.0.0.0:9090"}), this.self);
        DeliveryRobotBehaviour _deliveryRobotBehaviour = new DeliveryRobotBehaviour(robotId, sector, MRS_one_delivery.Arm);
        eval(_deliveryRobotBehaviour, this.self);
      }
    }
    
    public DeliveryRobot1() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("DeliveryRobot1"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS_one_delivery.DeliveryRobot1.DeliveryRobot1Process());
    }
  }
  
  public static class SimuationHandler extends ClientNode {
    private static class SimuationHandlerProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item1", "sector1", "red", 0.583518, 0.0}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item2", "sector1", "red", 0.554542, 0.187360}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item3", "sector1", "red", 0.504, 0.307}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item4", "sector1", "red", 0.332977, 0.470854}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.TYPE_2_DESTINATION, "red", (-8.0), 0.0}), MRS_one_delivery.DeliveryRobot1);
        out(new Tuple(new Object[] {GlobalConstants.TYPE_2_DESTINATION, "blue", 9.0, (-9.0)}), MRS_one_delivery.DeliveryRobot1);
        out(new Tuple(new Object[] {GlobalConstants.ROS_BRIDGE_SOCKET_URI, "ws://0.0.0.0:9090"}), this.self);
        Unload _unload = new Unload(MRS_one_delivery.DeliveryRobot1, (-8.0), 0.0);
        eval(_unload, this.self);
        Unload _unload_1 = new Unload(MRS_one_delivery.DeliveryRobot1, 9.0, (-9.0));
        eval(_unload_1, this.self);
      }
    }
    
    public SimuationHandler() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("SimuationHandler"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS_one_delivery.SimuationHandler.SimuationHandlerProcess());
    }
  }
  
  public MRS_one_delivery() throws IMCException {
    super(new PhysicalLocality("localhost:9999"));
  }
  
  public void addNodes() throws IMCException {
    MRS_one_delivery.Arm arm = new MRS_one_delivery.Arm();
    MRS_one_delivery.DeliveryRobot1 deliveryRobot1 = new MRS_one_delivery.DeliveryRobot1();
    MRS_one_delivery.SimuationHandler simuationHandler = new MRS_one_delivery.SimuationHandler();
    arm.addMainProcess();
    deliveryRobot1.addMainProcess();
    simuationHandler.addMainProcess();
  }
}
