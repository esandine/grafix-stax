import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Driver{
    public static void main(String[] args){
	Grafix g = new Grafix();
	Coor c1 = new Coor(100,200,300);
	Coor c2 = new Coor(123,342,300);
	Coor c3 = new Coor(234,443,239);
	Coor c4 = new Coor(239,467,235);
	Coor c5 = new Coor(458,134,135);
	Coor c6 = new Coor(196,147,138);
	Coor[] coors = {c1,c2,c3};
	Coor[] coors2 = {c4,c5,c6};
	g.addEdge(coors);
	g.addEdge(coors2);
	Pixel p = new Pixel(200,0,0);
	System.out.println("Initial Coordinates:");
	g.printEdgeList();
	g.writeCoors(p);
	g.write("edgy.ppm");
	//g.scale(.5,.5,.5);
	//g.translate(100,0,0);
	g.rotate(90,'z');
	g.translate(0,500,0);
	g.applyTransformation();
	System.out.println("Final Coordinates:");
	g.printEdgeList();
	g.writeCoors(p);
	g.write("edgy2.ppm");
	
    }
}
