import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;
import java.lang.Math;
//Curves assignement starts at line 413
//3d assignment starts at line 513
//polygon assignment starts at line 580
public class Grafix{
    //instance variables of Grafix
    private int width;
    private int height;
    private Pixel[][] data;
    private LinkedList<PointList> edges;
    private LinkedList<Coor[]> triangles;
    private Stack<double[][]> relativities;
    private Pixel drawcolor;
    public Grafix(int width, int height){
	setWidth(width);
	setHeight(height);
	data = new Pixel[width][height];
	resetPixels();
	edges = new LinkedList<PointList>();
	triangles = new LinkedList<Coor[]>();
	relativities = new Stack();
	relativities.push(makeIdentityMatrix());
	setIdentityMatrix();
	drawcolor = new Pixel(200, 0 ,0);
    }
    public Grafix(){
	this(500,500);
    }
    //mutators
    public void setWidth(int w){
	width = w;
    }
    public void setHeight(int h){
	height = h;
    }
    public void plot(int x, int y, Pixel p){
	if(x<getWidth()&&x>-1&&y<getHeight()&&y>-1)
	    data[x][y]=p;
    }
    public void plot(Coor c, Pixel p){
	plot((int)c.getX(), (int)c.getY(), p);
    }
    public void addEdge(PointList p){
	edges.add(p);
    }
    public void addEdge(Coor[] vals){
	PointList p = new PointList();
	for(int i = 0; i < vals.length; i++){
	    p.addCoor(vals[i]);
	}
	addEdge(p);
    }
    public void addPoint(Coor c){
	PointList p = new PointList();
	p.addCoor(c);
	addEdge(p);
    }
    public void resetPixels(){
	for(int i = 0; i< width; i++){
            for(int j = 0; j< height; j++){
                data[i][j]=new Pixel(0,0,0);
            }
        }
    }
    //accessors
    public int getWidth(){
        return width;
    }
    public int getHeight(){
	return height;
    }
    public Pixel getPixel(int x, int y){
	return data[x][y];
    }
    public int getSize(){
	int total = 0;
	int i = edges.size();
	PointList edge;
	while(i>0){
	    edge = edges.poll();
	    edges.add(edge);
	    total+=edge.len();
	    i--;
	}
	return total;
    }
    //printEdgeList prints out the entire edgelist
    public void printEdgeList(){
	String[] rows = new String[4];
        for(int i = 0; i<4; i++){
            rows[i]="";
        }
	LinkedList<PointList> newEdgeList = new LinkedList<PointList>();
	PointList next = edges.poll();
	while(next!=null){
	    newEdgeList.add(next);
	    rows = next.toRows(rows);
	    next = edges.poll();
	}
	edges=newEdgeList;
	for(int i = 0; i < 4; i++){
            System.out.println(rows[i]);
        }
    }
    //printTriangles prints out all of the triangles
    public void printTriangles(){
	String retstr = "";
	Coor[] current;
	for(int j = 0; j < triangles.size(); j ++){
	    current = triangles.poll();
	    triangles.add(current);
	    retstr+='[';
	    for(int i = 0; i<3; i++){
		retstr+="("+current[i].getX()+
		    ","+current[i].getY()+
		    ","+current[i].getZ()+")";
		if(i<2)
		    retstr+=",";
	    }
	    retstr+= "] ";
	}
	System.out.println(retstr);
    }
    //getData converts all of the pixel data to a string
    public String printData(){
	String retStr = "P3 ";
	retStr+=getWidth();
	retStr+=" ";
	retStr+=getHeight();
	retStr+=" 255\n";
	for(int i = 0; i< getWidth(); i++){
	    for(int j = 0; j< getHeight(); j++){
		//System.out.println(data[i][j].toString());
		retStr+=data[i][j].toString();
	    }
	}
	return retStr;
    }

    //Line Functions

    //bresLine1 draws the line in quadrant I using bresenham's line algorithm
    public void bresLine1(int xi, int yi, int xf, int yf, Pixel color){
	int x = xi;
	int y = yi;
	int a = 2*(yf-yi);
	int b = 2*(xi-xf);
	int d=a+xi-xf;
	while(x<=xf){
	    plot(x,y,color);
	    if(d>0){
		y++;
		d+=b;
	    }
	    x++;
	    d+=a;
	}
    }
    //bresLine2 draws the line in quadrant I using bresenham's line algorithm
    //it uses x=my+b as starting equation, reflecting the line about y=x
    public void bresLine2(int xi, int yi, int xf, int yf, Pixel color){
        int x = xi;
        int y = yi;
        int a = 2*(xf-xi);
        int b = 2*(yi-yf);
        int d=a+yi-yf;
        while(y<=yf){
            plot(x,y,color);
            if(d>0){
                x++;
                d+=b;
            }
            y++;
            d+=a;
        }
    }

    //bresLine8 draws the line in quadrant I using bresenham's line algorithm
    //it uses y=-(mx+b) as starting equation, reflecting the line
    public void bresLine8(int xi, int yi, int xf, int yf, Pixel color){
        int x = xi;
        int y = yi;
        int a = 2*(yi-yf);
        int b = 2*(xi-xf);
        int d=a+xi-xf;
        while(x<=xf){
            plot(x,y,color);
            if(d>0){
                y--;
                d+=b;
            }
            x++;
            d+=a;
        }
    }

    //bresLine7 draws the line in quadrant I using bresenham's line algorithm
    //it uses x=-(my+b) as starting equation, reflecting the line
    //this is kinda unintuitive but reflecting the equation is funner
    public void bresLine7(int xi, int yi, int xf, int yf, Pixel color){
        int x = xi;
        int y = yi;
	int a = 2*(xf-xi);
        int b = 2*(yf-yi);
        int d=a+yf-yi;
        while(y>=yf){
            plot(x,y,color);
            if(d>0){
                x++;
                d+=b;
            }
            y--;
            d+=a;
        }
    }
    //bresLine is a wrapper for all of the other functions
    public void bresLine(int xi, int yi, int xf, int yf, Pixel color){
	int temp;
	//swaps to take care of octants 3-6
	if(xi>xf){
	    temp=xi;
	    xi=xf;
	    xf=temp;
	    temp=yi;
	    yi=yf;
	    yf=temp;
	}
	//checks if quad I or quad IV
	if(yf>yi){
	    //checks oct II or oct I
	    if(yf-yi>xf-xi){
		bresLine2(xi,yi,xf,yf,color);
	    }else{
		bresLine1(xi,yi,xf,yf,color);
	    }
	}else{
	    //checks if oct VII or oct VIII
	    if(yi-yf>xf-xi){
		bresLine7(xi,yi,xf,yf,color);
	    }else{
		bresLine8(xi,yi,xf,yf,color);
	    }
	}

    }
    public void bresLine(Coor start, Coor end, Pixel p){
	bresLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY(),p);
    }

    //edgeList functions

    //writeEdge writes an edge by connecting the dots
    public void writeEdge(PointList p, Pixel color){
	Coor start = p.getCoor();
	plot(start,color);
	Coor end = p.getCoor();
	for(int i = 0; i < p.len()-1; i++){
	    bresLine(start,end,color);
	    start = end;
	    end = p.getCoor();
	}
	for(int i = 0; i < p.len()-1; i++){
	    p.getCoor();
	}
    }
    //writeCoors uses th einstructions to drawan image
    public void writeCoors(Pixel color){
	applyTransformation();
	int i = edges.size();
	PointList edge;
	while(i>0){
	    edge = edges.poll();
	    edges.add(edge);
	    writeEdge(edge, color);
	    i--;
	}
	Coor[] triangle;
	i = triangles.size();
	while(i>0){
	    triangle = triangles.poll();
	    triangles.add(triangle);
	    if(checkCull(triangle)){
		bresLine(triangle[0],triangle[1],color);
		bresLine(triangle[1],triangle[2],color);
		bresLine(triangle[2],triangle[0],color);
	    }
	    i--;
	}
	edges = new LinkedList<PointList>();
	triangles = new LinkedList<Coor[]>();
    }

    //Matrix functions

    //scale handles scalar multiplication of edge matrices
    //deprecated
    /*
    public void scale(double d){
	LinkedList<PointList> newEdges = new LinkedList<PointList>();
	PointList edge = edges.poll();
	while(edge!=null){
	    edge.scale(d);
	    newEdges.add(edge);
	    edge = edges.poll();
	}
	edges = newEdges;
    }
    */
    //makeMatrix returns a new empty 4x4 matrix
    public double[][] makeMatrix(){
	double[][] ret = new double[4][4];
	for(int i = 0; i < ret.length; i++){
	    for(int j = 0; j < ret.length; j++){
		ret[i][j] = 0.0;
	    }
	}
	return ret;
    }

    //makeIdentityMatrix makes an identity matrix
    public double[][] makeIdentityMatrix(){
	double[][] ret = makeMatrix();
	for(int i = 0; i < ret.length; i++){
	    for(int j = 0; j < ret.length; j++){
		if(i==j)
		    ret[i][j]=1.0;
	    }
	}
	return ret;
    }

    //setIdentityMatrix resets the relativities.peek() matrix
    public void setIdentityMatrix(){
	relativities.pop();
	relativities.push(makeIdentityMatrix());
    }

    //makeTranslationMatrix makes a translation matrix
    public double[][] makeTranslationMatrix(double x, double y, double z){
	double[][] ret = makeIdentityMatrix();
	ret[0][3] = x;
	ret[1][3] = y;
	ret[2][3] = z;
	return ret;
    }
    //translate makes and sets translation matrix
    public void translate(double x, double y, double z){
	multTransformation(makeTranslationMatrix(x, y, z));
    }

    public double[][] makeScaleMatrix(double x, double y, double z){
	double[][] ret = makeIdentityMatrix();
	ret[0][0] = x;
	ret[1][1] = y;
	ret[2][2] = z;
	return ret;
    }

    public void scale(double x, double y, double z){
	multTransformation(makeScaleMatrix(x, y, z));
    }

    public double[][] makeRotationMatrix(double theta, char axis){
	theta = Math.PI*theta/180;//converts
	double[][] ret = makeIdentityMatrix();
	if(axis == 'z'){
	    ret[0][0] = Math.cos(theta);
	    ret[0][1] = Math.sin(theta);
	    ret[1][0] = -Math.sin(theta);
	    ret[1][1] = Math.cos(theta);
	}else if(axis == 'x'){
	    ret[1][1] = Math.cos(theta);
	    ret[1][2] = Math.sin(theta);
	    ret[2][1] = -Math.sin(theta);
	    ret[2][2] = Math.cos(theta);
	}else{
	    ret[0][0] = Math.cos(theta);
	    ret[0][2] = Math.sin(theta);
	    ret[2][0] = Math.sin(theta);
	    ret[2][2] = -Math.cos(theta);
	}
	return ret;
    }
    public void rotate(char axis, double theta){
	multTransformation(makeRotationMatrix(theta, axis));
    }
    public double[][] multMatrices(double[][] m1, double[][] m2){
	double[][] ret = new double[m1.length][m2[0].length];
        double value;
        for(int row = 0; row < m1.length; row++){
            for(int col = 0; col < m2[0].length; col++){
        	value = 0;
        	for(int i = 0; i < m1[0].length; i++){
                    value+=m1[row][i]*m2[i][col];
        	}
        	ret[row][col] = value;
            }
        }
	return ret;
    }
    public void multTransformation(double[][] newMatrix){
	double[][] newTrans = multMatrices(newMatrix, relativities.pop());
	relativities.push(newTrans);
    }
    //displays a matrix
    public void displayMatrix(double[][] mat){
	for(int i = 0; i < mat.length; i++){
	    for(int j = 0; j < mat[0].length; j++){
		System.out.print(mat[i][j]+" ");
	    }
	    System.out.println("");
	}
    }

    //displays the transformation matrix
    public void displayTransformation(){
	displayMatrix(relativities.peek());
    }
    //multipies edgelist by transformation matrix
    //theres no way this long function works
    //it didn't on the first try but I tihnk it works now
    public void applyTransformation(){
	double[][] mat  = relativities.peek();
	PointList points;
	Coor[] verts;
	Coor point;
	Coor newpoint;
	for(int i = 0; i<edges.size(); i++){
		points=edges.poll();
		edges.add(points);
		for(int j = 0; j < points.len(); j++){
		    point = points.getCoor();
		    newpoint = new Coor();
		    newpoint.setX(mat[0][0]*point.getX()+
			       mat[0][1]*point.getY()+
			       mat[0][2]*point.getZ()+
			       mat[0][3]*point.getL());
		    newpoint.setY(mat[1][0]*point.getX()+
			       mat[1][1]*point.getY()+
			       mat[1][2]*point.getZ()+
			       mat[1][3]*point.getL());
		    newpoint.setZ(mat[2][0]*point.getX()+
			       mat[2][1]*point.getY()+
			       mat[2][2]*point.getZ()+
				  mat[2][3]*point.getL());
		    point.setL(mat[3][0]*point.getX()+
			       mat[3][1]*point.getY()+
			       mat[3][2]*point.getZ()+
			       mat[3][3]*point.getL());
		    point.setX(newpoint.getX());
		    point.setY(newpoint.getY());
		    point.setZ(newpoint.getZ());
		}
	}
	for(int k = 0; k<triangles.size(); k++){
		verts=triangles.poll();
		triangles.add(verts);
		for(int l = 0; l < verts.length; l++){
		    point = verts[l];
		    newpoint = new Coor();
		    newpoint.setX(mat[0][0]*point.getX()+
			       mat[0][1]*point.getY()+
				  mat[0][2]*point.getZ()+
			       mat[0][3]*point.getL());
		    newpoint.setY(mat[1][0]*point.getX()+
			       mat[1][1]*point.getY()+
			       mat[1][2]*point.getZ()+
			       mat[1][3]*point.getL());
		    newpoint.setZ(mat[2][0]*point.getX()+
			       mat[2][1]*point.getY()+
			       mat[2][2]*point.getZ()+
			       mat[2][3]*point.getL());
		    point.setL(mat[3][0]*point.getX()+
			       mat[3][1]*point.getY()+
			       mat[3][2]*point.getZ()+
			       mat[3][3]*point.getL());
		    point.setX(newpoint.getX());
		    point.setY(newpoint.getY());
		    point.setZ(newpoint.getZ());
		}
	}
    }

    //Curves assignment
    public void drawCircle(double cx, double cy, double cz, double r){
	PointList p = new PointList();
	double x;
	double y;
	for(double t = 0; t <= 1.001; t+=.01){
	    x = r*Math.cos(t*Math.PI*2)+cx;
	    y = r*Math.sin(t*Math.PI*2)+cy;
	    p.addCoor(new Coor(x, y, cz));
	}
	addEdge(p);
    }
    public double[][] makeHermiteMatrix(){
	double[][] ret = new double[4][4];
	ret[0][0] = 2;
	ret[0][1] = -2;
	ret[0][2] = 1;
	ret[0][3] = 1;
	ret[1][0] = -3;
	ret[1][1] = 3;
	ret[1][2] = -2;
	ret[1][3] = -1;
	ret[2][0] = 0;
	ret[2][1] = 0;
	ret[2][2] = 1;
	ret[2][3] = 0;
	ret[3][0] = 1;
	ret[3][1] = 0;
	ret[3][2] = 0;
	ret[3][3] = 0;
	return ret;
    }
    public double[][] makeBezierMatrix(){
	double[][] ret = new double[4][4];
	ret[0][0] = -1;
	ret[0][1] = 3;
	ret[0][2] = -3;
	ret[0][3] = 1;
	ret[1][0] = 3;
	ret[1][1] = -6;
	ret[1][2] = 3;
	ret[1][3] = 0;
	ret[2][0] = -3;
	ret[2][1] = 3;
	ret[2][2] = 0;
	ret[2][3] = 0;
	ret[3][0] = 1;
	ret[3][1] = 0;
	ret[3][2] = 0;
	ret[3][3] = 0;
	return ret;
    }
    public void drawHermite(double x0, double y0, double x1, double y1, double rx0, double ry0, double rx1, double ry1){
	PointList p = new PointList();
	double x;
	double y;
	double [][] xvals = new double[4][1];
	xvals[0][0] = x0;
	xvals[1][0] = x1;
	xvals[2][0] = rx0;
	xvals[3][0] = rx1;
	double [][] yvals = new double[4][1];
	yvals[0][0] = y0;
	yvals[1][0] = y1;
	yvals[2][0] = ry0;
	yvals[3][0] = ry1;
	double [][] xco = multMatrices(makeHermiteMatrix(), xvals);
	double [][] yco = multMatrices(makeHermiteMatrix(), yvals);
	for(double t = 0; t <= 1.001; t+=.01){
	    x = xco[0][0]*t*t*t+xco[1][0]*t*t+xco[2][0]*t+xco[3][0];
	    y = yco[0][0]*t*t*t+yco[1][0]*t*t+yco[2][0]*t+yco[3][0];
	    p.addCoor(new Coor(x, y, 0));
	}
	addEdge(p);
    }
    public void drawBezier(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3){
	PointList p = new PointList();
	double x;
	double y;
	double [][] xvals = new double[4][1];
	xvals[0][0] = x0;
	xvals[1][0] = x1;
	xvals[2][0] = x2;
	xvals[3][0] = x3;
	double [][] yvals = new double[4][1];
	yvals[0][0] = y0;
	yvals[1][0] = y1;
	yvals[2][0] = y2;
	yvals[3][0] = y3;
	double [][] xco = multMatrices(makeBezierMatrix(), xvals);
	double [][] yco = multMatrices(makeBezierMatrix(), yvals);
	for(double t = 0; t <= 1.001; t+=.01){
	    x = xco[0][0]*t*t*t+xco[1][0]*t*t+xco[2][0]*t+xco[3][0];
	    y = yco[0][0]*t*t*t+yco[1][0]*t*t+yco[2][0]*t+yco[3][0];
	    p.addCoor(new Coor(x, y, 0));
	}
	addEdge(p);
    }
    //end curves assignment
    //start 3d assignment
    public void clear(){
	edges = new LinkedList<PointList>();
    }
    public void addBox(double x, double y, double z,double w,double h,double d){
	LinkedList<Coor> vertices = new LinkedList<Coor>();
	Coor A = new Coor(x, y, z);
	Coor B = new Coor(x, y, z-d);
	Coor C = new Coor(x, y-h, z);
	Coor D = new Coor(x, y-h, z-d);
	Coor E = new Coor(x+w, y, z);
	Coor F = new Coor(x+w, y, z-d);
	Coor G = new Coor(x+w, y-h, z);
	Coor H = new Coor(x+w, y-h, z-d);
	addTriangle(A, B, D);
	addTriangle(D, C, A);
	addTriangle(C, D, H);
	addTriangle(H, G, C);
	addTriangle(G, H, F);
	addTriangle(F, E, G);
	addTriangle(E, F, B);	
	addTriangle(B, A, E);	
	addTriangle(B, F, H);
	addTriangle(H, D, B);
	addTriangle(A, C, G);
	addTriangle(G, E, A);

	writeCoors(drawcolor);
    }
    public void addSphere(double cx, double cy, double cz, double r, int steps){
	LinkedList<Coor> sphere = new LinkedList<Coor>();
	Coor c;
	double theta;
	double phi;
	for(int i = 0; i <= steps; i++){
	    theta = Math.PI*i/steps;
	    for(int j = 0; j <= steps; j++){
		phi = 2*Math.PI*j/steps;
		sphere.add(new Coor(r*Math.cos(theta)+cx,
				    r*Math.sin(theta)*Math.cos(phi)+cy,
				    r*Math.sin(theta)*Math.sin(phi)+cz));
	    }
	}
	LinkedList<Coor> sphere2 = new LinkedList<Coor>(sphere);
	LinkedList<Coor> sphere3 = new LinkedList<Coor>(sphere);
	LinkedList<Coor> sphere4 = new LinkedList<Coor>(sphere);
	Coor v1;
	Coor v2;
	Coor v3;
	Coor v4;
	for(int i = 0; i < steps+1; i++){
	    v1 = sphere3.poll();
	    sphere3.add(v1);//sphere2 is one ahead of sphere
	    v1 = sphere4.poll();	    
	    sphere4.add(v1);
	}
	v1 = sphere2.poll();
	sphere2.add(v1);
	v1 = sphere4.poll();
	sphere4.add(v1);
	for(int i = 0; i <steps*steps-1; i++){
		v1 = sphere.poll();
		v2 = sphere2.poll();
		v3 = sphere3.poll();
		v4 = sphere4.poll();
		//addPoint(v1);
		//addPoint(v2);
		//addPoint(v3);
		addTriangle(v1, v2, v4);
		addTriangle(v4, v3, v1);
		sphere.add(v1);
		sphere2.add(v2);
		sphere3.add(v3);
		sphere4.add(v4);
	}
	writeCoors(drawcolor);
    }
    public void addTorus(double cx, double cy, double cz, double r1, double r2, int steps){
        LinkedList<Coor> torus = new LinkedList<Coor>();
	Coor c;
        double theta;
        double phi;
	for(int i = 0; i <= steps; i++){
            phi = 2*Math.PI*i/steps;
            for(int j = 0; j <= steps; j++){
                theta = 2*Math.PI*j/steps;
                torus.add(new Coor(Math.cos(phi)*(r2*Math.cos(theta)+r1)+cx,
                                    r2*Math.sin(theta)+cy,
				   -Math.sin(phi)*(r2*Math.cos(theta)+r1)+cz));
            }
        }
	LinkedList<Coor> torus2 = new LinkedList<Coor>(torus);
	LinkedList<Coor> torus3 = new LinkedList<Coor>(torus);
        LinkedList<Coor> torus4 = new LinkedList<Coor>(torus);
	Coor v1;
	Coor v2;
	Coor v3;
	Coor v4;
	for(int i = 0; i < steps+1; i++){
	    v1 = torus3.poll();
	    torus3.add(v1);//sphere2 is one ahead of sphere
	    v1 = torus4.poll();	    
	    torus4.add(v1);
	}
	v1 = torus2.poll();
	torus2.add(v1);
	v1 = torus4.poll();
	torus4.add(v1);
	for(int i = 0; i <steps*steps; i++){
		v1 = torus.poll();
		v2 = torus2.poll();
		v3 = torus3.poll();
		v4 = torus4.poll();
		addTriangle(v1, v2, v4);
		addTriangle(v4, v3, v1);
		torus.add(v1);
		torus2.add(v2);
		torus3.add(v3);
		torus4.add(v4);
        }
	writeCoors(drawcolor);
    }
    public boolean checkCull(Coor[] tri){
	double v1x = tri[1].getX()-tri[0].getX();
	double v1y = tri[1].getY()-tri[0].getY();
	double v2x = tri[2].getX()-tri[0].getX();
	double v2y = tri[2].getY()-tri[0].getY();
	double v3z = v1x*v2y-v1y*v2x;
	return v3z>=0;
    }
    //polygons start here
    /*public void addPolygon(Coor[] coors){

	addPo(p);
	}*/
    public void addTriangle(Coor c0, Coor c1, Coor c2){
	Coor[] coos = new Coor[3];
	coos[0] = c0.copyCoor();
	coos[1] = c1.copyCoor();
	coos[2] = c2.copyCoor();
	triangles.add(coos);
    }
    public void addTriangle(double x0, double y0, double z0, double x1, double y1, double z1, double x2, double y2, double z2){
	addTriangle(new Coor(x0, y0, z0), new Coor(x1, y1, z1), new Coor(x2, y2, z2));
    }
    //Write function copies the pixels to image file
    //Assisnment Eight Coordinate Systems
    public void push(){
	relativities.push(relativities.peek());
    }
    public void pop(){
	relativities.pop();
    }
    public void write(String name){
	try{
	    File f = new File(name);
	    f.delete();
	    f.createNewFile();
	    FileWriter w = new FileWriter(f, true);
	    w.write("P3 "+getWidth()+" "+getHeight()+" 255\n");
	    for(int i = 0; i< getHeight(); i++){
		for(int j = 0; j<getWidth(); j++){
		    //the [j][bleh] stuff serves to rotate the coordinates
		    w.write(data[j][getHeight()-1-i].toString());
		}
	    }
	    w.close();
	}catch(IOException e){
	    System.out.println("IO Error PANIC");
	}
    }
}
