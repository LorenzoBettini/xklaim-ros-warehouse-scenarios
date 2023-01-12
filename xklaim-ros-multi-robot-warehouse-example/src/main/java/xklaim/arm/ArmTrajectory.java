package xklaim.arm;

public class ArmTrajectory {
	private double[] trajectoryPoints;
	private double tolerance;
	
	public ArmTrajectory(double[] trajectoryPoints, double tolerance) {
		super();
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
