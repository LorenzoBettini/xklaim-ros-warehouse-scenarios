package xklaim.arm;

import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class ArmBehavior extends KlavaProcess {
  public ArmBehavior() {
    
  }
  
  @Override
  public void executeProcess() {
    PickAndReleaseOneItem _pickAndReleaseOneItem = new PickAndReleaseOneItem();
    eval(_pickAndReleaseOneItem, this.self);
    in(new Tuple(new Object[] {ArmConstants.IS_IN_THE_INITIAL_POSITION}), this.self);
    ArmBehavior _armBehavior = new ArmBehavior();
    eval(_armBehavior, this.self);
  }
}
