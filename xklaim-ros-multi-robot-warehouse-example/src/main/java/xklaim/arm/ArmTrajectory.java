package xklaim.arm;

public class ArmTrajectory {
	private double[] trajectoryPoints;
	private double tolerance;
	
	public ArmTrajectory(double[] trajectoryPoints, double tolerance) {
		if (trajectoryPoints.length!=6)
			throw new IllegalArgumentException();
		this.trajectoryPoints = trajectoryPoints;
		this.tolerance = tolerance;
	}
	
	// Constructor used for movement of the arm depending on the coordinates of the item   
	public ArmTrajectory(double[] trajectoryPoints, double itemCoordinateX, double itemCoordinateY, double tolerance) {
		if (trajectoryPoints.length!=5)
			throw new IllegalArgumentException();
		this.trajectoryPoints = new double[6];
		this.trajectoryPoints[0]=Math.atan(itemCoordinateY / itemCoordinateX) - 3.14;
		for (int i = 0; i < trajectoryPoints.length; i++) {
			this.trajectoryPoints[i+1]=trajectoryPoints[i];
		}		
		this.tolerance = tolerance;
	}
	
	public double[] getTrajectoryPoints() {
		return trajectoryPoints;
	}

	public double getTolerance() {
		return tolerance;
	}
		
}
