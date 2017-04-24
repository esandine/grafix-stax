import java.util.LinkedList;
//a series of connected lines
public class PointList{
    //points is the list of coordinates for a connected line
    private LinkedList<Coor> points;
    //addCoor adds a new endpoint to the line
    public void addCoor(Coor c){
	points.add(c);
    }
    //getCoor returns a coor and shifts it to the back of the list
    public Coor getCoor(){
	Coor c = points.poll();
	points.add(c);
	return c;
    }
    public int len(){
	return points.size();
    }
    //toRows adds the elements to strings
    //Used when multiple pointlists are being viewed horizontally
    public String[] toRows(String[] rows){
	LinkedList<Coor> newPoints = new LinkedList<Coor>();
	Coor next = points.poll();
	while(next!=null){
	    newPoints.add(next);
	    rows[0]+=next.getX()+" ";
	    rows[1]+=next.getY()+" ";
	    rows[2]+=next.getZ()+" ";
	    rows[3]+=next.getL()+" ";
	    next = points.poll();
	}
	points = newPoints;
	return rows;
    }
    public void printCoors(){
	String[] rows = new String[4];
	for(int i = 0; i<4; i++){
	    rows[i]="";
	}
	rows=toRows(rows);
	for(int i = 0; i < 4; i++){
	    System.out.println(rows[i]);
	}
    }
    //scalar multiplication of matrix column
    public void scale(double d){
	Coor next;
	for(int i = 0; i < len(); i++){
	    next = getCoor();
	    next.scale(d);
	}
    }
    public PointList(){
	points = new LinkedList<Coor>();
    }
    
}
