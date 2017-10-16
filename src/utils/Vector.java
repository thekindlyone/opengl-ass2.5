package utils;

public class Vector {
	
	public double x,y,z;
	
	public Vector(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public static Vector get_normal(Vector u, Vector v){
		return new Vector((u.y*v.z)-(u.z*v.y),(u.z*v.x)-(u.x*v.z),(u.x*v.y)-(u.y*v.x) );
		
	}
	
	public  Vector cross(Vector v){
		return new Vector((y*v.z)-(z*v.y),(z*v.x)-(x*v.z),(x*v.y)-(y*v.x) );
		
	}
	
	
	public double magnitude(){
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	
	public Vector minus(Vector v){
		return new Vector(x-v.x,y-v.y,z-v.z);
	}
	
	public Vector plus(Vector v){
		return new Vector(x+v.x,y+v.y,z+v.z);
	}
	
	public Vector minus(Vector v, boolean reverse){
		double mul = (reverse)?-1:1;
		return new Vector((x-v.x)*mul,(y-v.y)*mul,(z-v.z)*mul);
	}
	
	
	public static Vector get_average_4(Vector v1,Vector v2,Vector v3, Vector v4 ){
		return new Vector(
				(v1.x+v2.x+v3.x+v4.x)/4.0,
				(v1.y+v2.y+v3.y+v4.y)/4.0,
				(v1.z+v2.z+v3.z+v4.z)/4.0
				);
	}
	
	public void print(){
		System.out.println(x+","+y+","+z);
	}
	
	public Vector scalarprod(double s){
		return new Vector(s*x,s*y,s*z);
	}
	
	
	public Vector normalized(){
		double l = magnitude();
		return new Vector(x/l,y/l,z/l);
	}

}
