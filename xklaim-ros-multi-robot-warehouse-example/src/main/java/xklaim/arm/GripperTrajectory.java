package xklaim.arm;

public class GripperTrajectory {
	private double[] trajectoryPoints;
	private double tolerance;
	
	public GripperTrajectory(double[] trajectoryPoints, double tolerance) {
		if (trajectoryPoints.length!=2)
			throw new IllegalArgumentException();
		this.trajectoryPoints = trajectoryPoints;
		this.tolerance = tolerance;
	}
	
	
	public double[] getTrajectoryPoints() {
		return trajectoryPoints;
	}

	public double getTolerance() {
		return tolerance;
	}
}
