public class Coor{
    private double x;
    private double y;
    private double z;
    private double l;
    //Mutators
    public void setX(double val){
	x=val;
    }
    public void setY(double val){
        y=val;
    }
    public void setZ(double val){
        z=val;
    }
    public void setL(double val){
        l=val;
    }
    public void scale(double val){
	setX(getX()*val);
	setY(getY()*val);
	setZ(getZ()*val);
    }
    //Accessors
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public double getL(){
        return l;
    }
    public Coor(double x, double y, double z){
	setX(x);
	setY(y);
	setZ(z);
	setL(1);
    }
    public Coor(double[] vals){
	setX(vals[0]);
	setY(vals[1]);
	setZ(vals[2]);
	setL(1);
    }
    public Coor(){
	this(1,1,1);
    }
    public Coor copyCoor(){
	return new Coor(getX(), getY(), getZ());
    }
    public String toString(){
	return ""+getX()+" "+
	    getY()+" "+
	    getZ()+" "+
	    getL();
			   
    }

}
