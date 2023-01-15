package xklaim.arm;

import klava.Tuple;
import klava.topology.KlavaProcess;

@SuppressWarnings("all")
public class ArmBehaviour extends KlavaProcess {
  public ArmBehaviour() {
    
  }
  
  @Override
  public void executeProcess() {
    PickAndReleaseOneItem _pickAndReleaseOneItem = new PickAndReleaseOneItem();
    eval(_pickAndReleaseOneItem, this.self);
    in(new Tuple(new Object[] {"initialPosition"}), this.self);
    ArmBehaviour _armBehaviour = new ArmBehaviour();
    eval(_armBehaviour, this.self);
  }
}
