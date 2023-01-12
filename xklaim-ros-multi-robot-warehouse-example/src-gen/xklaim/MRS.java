package xklaim;

import klava.LogicalLocality;
import klava.PhysicalLocality;
import klava.Tuple;
import klava.topology.ClientNode;
import klava.topology.KlavaNodeCoordinator;
import klava.topology.LogicalNet;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.mikado.imc.common.IMCException;
import xklaim.arm.ArmBehaviour;
import xklaim.arm.DeliveryRobotBehaviour;

@SuppressWarnings("all")
public class MRS extends LogicalNet {
  private static final LogicalLocality Arm = new LogicalLocality("Arm");
  
  private static final LogicalLocality DeliveryRobot1 = new LogicalLocality("DeliveryRobot1");
  
  private static final LogicalLocality DeliveryRobot2 = new LogicalLocality("DeliveryRobot2");
  
  private static final LogicalLocality SimuationHandler = new LogicalLocality("SimuationHandler");
  
  public static class Arm extends ClientNode {
    private static class ArmProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
        ArmBehaviour _armBehaviour = new ArmBehaviour(rosbridgeWebsocketURI);
        eval(_armBehaviour, this.self);
      }
    }
    
    public Arm() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("Arm"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS.Arm.ArmProcess());
    }
  }
  
  public static class DeliveryRobot1 extends ClientNode {
    private static class DeliveryRobot1Process extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        try {
          final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
          final String robotId = "robot1";
          final String sector = "sector1";
          while (true) {
            DeliveryRobotBehaviour _deliveryRobotBehaviour = new DeliveryRobotBehaviour(rosbridgeWebsocketURI, robotId, sector, MRS.Arm);
            this.executeNodeProcess(_deliveryRobotBehaviour);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    }
    
    public DeliveryRobot1() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("DeliveryRobot1"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS.DeliveryRobot1.DeliveryRobot1Process());
    }
  }
  
  public static class DeliveryRobot2 extends ClientNode {
    private static class DeliveryRobot2Process extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        try {
          final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
          final String robotId = "robot2";
          final String sector = "sector2";
          while (true) {
            DeliveryRobotBehaviour _deliveryRobotBehaviour = new DeliveryRobotBehaviour(rosbridgeWebsocketURI, robotId, sector, MRS.Arm);
            this.executeNodeProcess(_deliveryRobotBehaviour);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    }
    
    public DeliveryRobot2() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("DeliveryRobot2"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS.DeliveryRobot2.DeliveryRobot2Process());
    }
  }
  
  public static class SimuationHandler extends ClientNode {
    private static class SimuationHandlerProcess extends KlavaNodeCoordinator {
      @Override
      public void executeProcess() {
        final String rosbridgeWebsocketURI = "ws://0.0.0.0:9090";
        out(new Tuple(new Object[] {"item", "item1", "sector1", "red", 0.583518, 0.0}), MRS.Arm);
        out(new Tuple(new Object[] {"item", "item2", "sector2", "blue", 0.554542, 0.187360}), MRS.Arm);
        out(new Tuple(new Object[] {"item", "item3", "sector2", "red", 0.504, 0.307}), MRS.Arm);
        out(new Tuple(new Object[] {"item", "item4", "sector1", "blue", 0.332977, 0.470854}), MRS.Arm);
        out(new Tuple(new Object[] {"type2destination", "red", (-9.0), (-9.0)}), MRS.DeliveryRobot1);
        out(new Tuple(new Object[] {"type2destination", "blue", 9.0, (-9.0)}), MRS.DeliveryRobot1);
        out(new Tuple(new Object[] {"type2destination", "red", 9.0, 9.0}), MRS.DeliveryRobot2);
        out(new Tuple(new Object[] {"type2destination", "blue", (-9.0), 9.0}), MRS.DeliveryRobot2);
        out(new Tuple(new Object[] {"availableForDelivery"}), MRS.DeliveryRobot1);
        out(new Tuple(new Object[] {"availableForDelivery"}), MRS.DeliveryRobot2);
        Unload _unload = new Unload(rosbridgeWebsocketURI, MRS.DeliveryRobot1, (-9.0), (-9.0));
        eval(_unload, this.self);
        Unload _unload_1 = new Unload(rosbridgeWebsocketURI, MRS.DeliveryRobot1, 9.0, (-9.0));
        eval(_unload_1, this.self);
        Unload _unload_2 = new Unload(rosbridgeWebsocketURI, MRS.DeliveryRobot2, (-9.0), 9.0);
        eval(_unload_2, this.self);
        Unload _unload_3 = new Unload(rosbridgeWebsocketURI, MRS.DeliveryRobot2, 9.0, 9.0);
        eval(_unload_3, this.self);
      }
    }
    
    public SimuationHandler() {
      super(new PhysicalLocality("localhost:9999"), new LogicalLocality("SimuationHandler"));
    }
    
    public void addMainProcess() throws IMCException {
      addNodeCoordinator(new MRS.SimuationHandler.SimuationHandlerProcess());
    }
  }
  
  public MRS() throws IMCException {
    super(new PhysicalLocality("localhost:9999"));
  }
  
  public void addNodes() throws IMCException {
    MRS.Arm arm = new MRS.Arm();
    MRS.DeliveryRobot1 deliveryRobot1 = new MRS.DeliveryRobot1();
    MRS.DeliveryRobot2 deliveryRobot2 = new MRS.DeliveryRobot2();
    MRS.SimuationHandler simuationHandler = new MRS.SimuationHandler();
    arm.addMainProcess();
    deliveryRobot1.addMainProcess();
    deliveryRobot2.addMainProcess();
    simuationHandler.addMainProcess();
  }
}
