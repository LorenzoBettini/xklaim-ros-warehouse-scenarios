package xklaim.arm;

import java.util.Collections;
import java.util.List;
import klava.Tuple;
import klava.topology.KlavaProcess;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class PickAndReleaseOneItem extends KlavaProcess {
  private String rosbridgeWebsocketURI;
  
  public PickAndReleaseOneItem(final String rosbridgeWebsocketURI) {
    this.rosbridgeWebsocketURI = rosbridgeWebsocketURI;
  }
  
  @Override
  public void executeProcess() {
    final ArmTrajectory DOWN = new ArmTrajectory(new double[] { (-0.2169), (-0.5822), 3.14, 1.66, (-0.01412) }, 0.000001);
    String itemId = null;
    String sector = null;
    String itemType = null;
    Double x = null;
    Double y = null;
    Tuple _Tuple = new Tuple(new Object[] {"item", String.class, String.class, String.class, Double.class, Double.class});
    in(_Tuple, this.self);
    itemId = (String) _Tuple.getItem(1);
    sector = (String) _Tuple.getItem(2);
    itemType = (String) _Tuple.getItem(3);
    x = (Double) _Tuple.getItem(4);
    y = (Double) _Tuple.getItem(5);
    final List<Double> firstGetDownPositions = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf((-0.2169)), Double.valueOf((-0.5822)), Double.valueOf(3.14), Double.valueOf(1.66), Double.valueOf((-0.01412))));
    GetDown _getDown = new GetDown(this.rosbridgeWebsocketURI, x, y, ((double[])Conversions.unwrapArray(firstGetDownPositions, double.class)));
    eval(_getDown, this.self);
    in(new Tuple(new Object[] {"getDownCompleted"}), this.self);
    final List<Double> secondGetDownPositions = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf((-0.9975)), Double.valueOf((-0.4970)), Double.valueOf(3.1400), Double.valueOf(1.6613), Double.valueOf((-0.0142))));
    GetDown _getDown_1 = new GetDown(this.rosbridgeWebsocketURI, x, y, ((double[])Conversions.unwrapArray(secondGetDownPositions, double.class)));
    this.executeNodeProcess(_getDown_1);
    in(new Tuple(new Object[] {"getDownCompleted"}), this.self);
    Grip _grip = new Grip(this.rosbridgeWebsocketURI);
    this.executeNodeProcess(_grip);
    in(new Tuple(new Object[] {"gripCompleted"}), this.self);
    GetUp _getUp = new GetUp(this.rosbridgeWebsocketURI, x, y);
    this.executeNodeProcess(_getUp);
    in(new Tuple(new Object[] {"getUpCompleted"}), this.self);
    Rotate _rotate = new Rotate(this.rosbridgeWebsocketURI, sector);
    this.executeNodeProcess(_rotate);
    in(new Tuple(new Object[] {"rotateCompleted"}), this.self);
    in(new Tuple(new Object[] {"deliveryRobotArrived"}), this.self);
    Lay _lay = new Lay(this.rosbridgeWebsocketURI);
    this.executeNodeProcess(_lay);
    in(new Tuple(new Object[] {"layCompleted"}), this.self);
    Release _release = new Release(this.rosbridgeWebsocketURI, itemId, itemType);
    this.executeNodeProcess(_release);
    in(new Tuple(new Object[] {"releaseCompleted"}), this.self);
    GoToInitialPosition _goToInitialPosition = new GoToInitialPosition(this.rosbridgeWebsocketURI);
    this.executeNodeProcess(_goToInitialPosition);
  }
}
