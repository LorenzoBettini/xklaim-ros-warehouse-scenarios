package xklaim.arm;

import klava.Tuple;
import klava.topology.KlavaProcess;
import xklaim.GlobalConstants;

@SuppressWarnings("all")
public class PickAndReleaseOneItem extends KlavaProcess {
  public PickAndReleaseOneItem() {
    
  }
  
  @Override
  public void executeProcess() {
    String itemId = null;
    String sector = null;
    String itemType = null;
    Double x = null;
    Double y = null;
    Tuple _Tuple = new Tuple(new Object[] {GlobalConstants.ITEM, String.class, String.class, String.class, Double.class, Double.class});
    in(_Tuple, this.self);
    itemId = (String) _Tuple.getItem(1);
    sector = (String) _Tuple.getItem(2);
    itemType = (String) _Tuple.getItem(3);
    x = (Double) _Tuple.getItem(4);
    y = (Double) _Tuple.getItem(5);
    final ArmTrajectory HALF_DOWN = new ArmTrajectory(new double[] { (-0.2169), (-0.5822), 3.14, 1.66, (-0.01412) }, (x).doubleValue(), (y).doubleValue(), 0.000001);
    final ArmTrajectory COMPLETE_DOWN = new ArmTrajectory(new double[] { (-0.9975), (-0.4970), 3.1400, 1.6613, (-0.0142) }, (x).doubleValue(), (y).doubleValue(), 0.000001);
    final ArmTrajectory UP = new ArmTrajectory(new double[] { (-0.2862), (-0.5000), 3.1400, 1.6613, (-0.0142) }, (x).doubleValue(), (y).doubleValue(), 0.008);
    final ArmTrajectory ROTATE = new ArmTrajectory(new double[] { (-0.9546), (-0.20), (-0.7241), 3.1400, 1.6613, (-0.0142) }, 0.008);
    final ArmTrajectory LAY_DOWN = new ArmTrajectory(new double[] { (-0.9546), (-0.0097), (-0.9513), 3.1400, 1.7749, (-0.0142) }, 0.002);
    final ArmTrajectory INITIAL_POSITION = new ArmTrajectory(new double[] { 0.000, 0.000, 0.000, 0.000, 0.000, 0.000 }, 0.008);
    final GripperTrajectory CLOSE = new GripperTrajectory(new double[] { 0.0138, (-0.0138) }, 0.007);
    final GripperTrajectory OPEN = new GripperTrajectory(new double[] { 0.0, 0.0 }, 0.00008);
    MoveArm _moveArm = new MoveArm(HALF_DOWN);
    eval(_moveArm, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    MoveArm _moveArm_1 = new MoveArm(COMPLETE_DOWN);
    eval(_moveArm_1, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    UseGripper _useGripper = new UseGripper(CLOSE);
    eval(_useGripper, this.self);
    in(new Tuple(new Object[] {ArmConstants.USE_GRIPPER_COMPLETED}), this.self);
    MoveArm _moveArm_2 = new MoveArm(UP);
    eval(_moveArm_2, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    out(new Tuple(new Object[] {GlobalConstants.ITEM_READY_FOR_DELIVERY, sector}), this.self);
    MoveArm _moveArm_3 = new MoveArm(ROTATE);
    eval(_moveArm_3, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    in(new Tuple(new Object[] {GlobalConstants.DELIVERY_ROBOT_ARRIVED}), this.self);
    MoveArm _moveArm_4 = new MoveArm(LAY_DOWN);
    eval(_moveArm_4, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    UseGripper _useGripper_1 = new UseGripper(OPEN);
    eval(_useGripper_1, this.self);
    in(new Tuple(new Object[] {ArmConstants.USE_GRIPPER_COMPLETED}), this.self);
    out(new Tuple(new Object[] {GlobalConstants.GRIPPER_OPENED, itemId, itemType}), this.self);
    MoveArm _moveArm_5 = new MoveArm(INITIAL_POSITION);
    eval(_moveArm_5, this.self);
    in(new Tuple(new Object[] {ArmConstants.MOVE_ARM_COMPLETED}), this.self);
    out(new Tuple(new Object[] {ArmConstants.IS_IN_THE_INITIAL_POSITION}), this.self);
  }
}
