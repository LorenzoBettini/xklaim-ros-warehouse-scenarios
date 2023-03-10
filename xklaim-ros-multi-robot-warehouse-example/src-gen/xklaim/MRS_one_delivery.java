package xklaim;

import klava.LogicalLocality;
import klava.PhysicalLocality;
import klava.Tuple;
import klava.topology.ClientNode;
import klava.topology.KlavaNodeCoordinator;
import klava.topology.LogicalNet;
import org.mikado.imc.common.IMCException;
import xklaim.arm.ArmBehavior;
import xklaim.singleDeliveryRobot.DeliveryRobotBehavior;

@SuppressWarnings("all")
public class MRS_one_delivery extends LogicalNet {
  private static final LogicalLocality Arm = new LogicalLocality("Arm");
  
  private static final LogicalLocality DeliveryRobot1 = new LogicalLocality("DeliveryRobot1");
  
  private static final LogicalLocality Environment = new LogicalLocality("Environment");
  
  public static class Arm extends ClientNode {
    private static class ArmProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        ArmBehavior _armBehavior = new ArmBehavior();
        eval(_armBehavior, this.self);
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
        DeliveryRobotBehavior _deliveryRobotBehavior = new DeliveryRobotBehavior(robotId, sector, MRS_one_delivery.Arm);
        eval(_deliveryRobotBehavior, this.self);
      }
    }
    
    public DeliveryRobot1() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("DeliveryRobot1"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS_one_delivery.DeliveryRobot1.DeliveryRobot1Process());
    }
  }
  
  public static class Environment extends ClientNode {
    private static class EnvironmentProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item1", "sector1", "red", 0.583518, 0.0}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item2", "sector1", "red", 0.554542, 0.187360}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item3", "sector1", "red", 0.504, 0.307}), MRS_one_delivery.Arm);
        out(new Tuple(new Object[] {GlobalConstants.ITEM, "item4", "sector1", "red", 0.332977, 0.470854}), MRS_one_delivery.Arm);
        Unload _unload = new Unload(MRS_one_delivery.DeliveryRobot1, (-8.0), 0.0);
        eval(_unload, this.self);
      }
    }
    
    public Environment() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Environment"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS_one_delivery.Environment.EnvironmentProcess());
    }
  }
  
  public MRS_one_delivery() throws IMCException {
    super(new PhysicalLocality("localhost:9999"));
  }
  
  public void addNodes() throws IMCException {
    MRS_one_delivery.Arm arm = new MRS_one_delivery.Arm();
    MRS_one_delivery.DeliveryRobot1 deliveryRobot1 = new MRS_one_delivery.DeliveryRobot1();
    MRS_one_delivery.Environment environment = new MRS_one_delivery.Environment();
    arm.addMainProcess();
    deliveryRobot1.addMainProcess();
    environment.addMainProcess();
  }
}
