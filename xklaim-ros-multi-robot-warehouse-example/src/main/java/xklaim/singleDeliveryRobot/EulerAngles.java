package xklaim.singleDeliveryRobot;

import messages.Quaternion;

public class EulerAngles {
	  private double roll;
	  private double pitch;
	  private double yaw;
	  
	  
	  
	  
	/**
	 * @param roll
	 * @param pitch
	 * @param yaw
	 */
	public EulerAngles(double roll, double pitch, double yaw) {
		super();
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public EulerAngles(Quaternion q) {
		super();
		double sinr_cosp = 2 * (q.getW() * q.getX() + q.getY() * q.getZ());
		double cosr_cosp = 1 - 2 * (q.getX() * q.getX() + q.getY() * q.getY());
		this.roll = Math.atan2(sinr_cosp, cosr_cosp);
		
	    double sinp = Math.sqrt(1 + 2 * (q.getW() * q.getX() - q.getY() * q.getZ()));
	    double cosp = Math.sqrt(1 - 2 * (q.getW() * q.getX() - q.getY() * q.getZ()));
	    this.pitch = 2 * Math.atan2(sinp, cosp) - Math.PI / 2;
		
	    double siny_cosp = 2 * (q.getW() * q.getZ() + q.getX() * q.getY());
	    double cosy_cosp = 1 - 2 * (q.getY() * q.getY() + q.getZ() * q.getZ());
	    this.yaw = Math.atan2(siny_cosp, cosy_cosp);
	}
	
	/**
	 * @return the roll
	 */
	public double getRoll() {
		return roll;
	}
	/**
	 * @param roll the roll to set
	 */
	public void setRoll(double roll) {
		this.roll = roll;
	}
	/**
	 * @return the pitch
	 */
	public double getPitch() {
		return pitch;
	}
	/**
	 * @param pitch the pitch to set
	 */
	public void setPitch(double pitch) {
		this.pitch = pitch;
	}
	/**
	 * @return the yaw
	 */
	public double getYaw() {
		return yaw;
	}
	/**
	 * @param yaw the yaw to set
	 */
	public void setYaw(double yaw) {
		this.yaw = yaw;
	}
	  
	  
	

}
