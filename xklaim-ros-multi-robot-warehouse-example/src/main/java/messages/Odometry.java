package messages;

import ros.msgs.std_msgs.Header;

public class Odometry {
    public Header header =new Header();
    public String child_frame_id;
    public PoseWithCovariance pose = new PoseWithCovariance();
    public TwistWithCovariance twist = new TwistWithCovariance();


   public Odometry(Header header, String child_frame_id, PoseWithCovariance pose, TwistWithCovariance twist){
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.pose = pose;
        this.twist = twist;


    }
    public Odometry(){}
}


