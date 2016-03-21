package al.jamil.suvo.dxball;

public interface Entity {
	public void draw();
	public void update(int x,int y);
	public void updateD(int dx,int dy);
	public void setX(int x);
	public void setY(int y);
	public void updateX(int dx);
	public void updateY(int dy);
	public boolean intersect(int x,int y);
	public int getX();
	public int getY();

	

}
