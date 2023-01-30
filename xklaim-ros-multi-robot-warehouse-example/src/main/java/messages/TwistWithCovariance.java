package messages;

public class TwistWithCovariance {
	public Twist twist=new Twist();
	public double[] covariance;
	
	
	   public TwistWithCovariance(Twist twist, double[] covariance){
	        this.twist = twist;
	        this.covariance = covariance;

	    }
	    public TwistWithCovariance(){this.covariance = new double[36];}
	

}
