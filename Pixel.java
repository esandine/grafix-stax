//pixel class for each pixel                                                
public class Pixel{
    //instance variables of pixel                                           
    private int r;
    private int g;
    private int b;
    //constructors                                                          
    public Pixel(int r, int g, int b){
	setR(r);
	setG(g);
	setB(b);
    }
    public Pixel(){
	this(0,0,0);
    }
    //mutators                                                              
    public void setR(int r){
	this.r=r;
    }
    public void setG(int g){
	this.g=g;
    }
    public void setB(int b){
	this.b=b;
    }
    //accessors                                                             
    public int getR(){
	return r;
    }
    public int getG(){
	return g;
    }
    public int getB(){
	return b;
    }
    public String toString(){
	return ""+getR()+" "+getG()+" "+getB()+"\n";
    }
}
