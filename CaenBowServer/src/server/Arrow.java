/**
 * @Author Valentin Durand - ENSICAEN
 * @Project CaenBowServer
 * @Package server
 * @Class Arrow
 * @ Dec 15, 2016 11:42:47 AM
 */
package server;

public class Arrow {
	private double x;
	private double y;
	private int point;
	
	/**
	 * 
	 */
	public Arrow(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
}
