package elements;

import java.awt.Point;

public class Troop {
	public int type;
	public int num;
	public int x,y;
	public boolean moved = false;
	public int range = 1;
	public int speed = 3;
	
	
	public int element;
	public int id;
	public boolean defend = true;
	public Troop(int nx, int ny, int nnum, int ntype){
		x = nx;
		y=ny;
		num=nnum;
		type=ntype;
	}
	
	public boolean isAvailable = false;
		
	public void updatePosition(Point p){
		x=p.x;
		y=p.y;
	}
	public void changeType(int newtype){
		type=newtype;
	}

	public boolean isInRange(int mx, int my){

		if ((x==mx)&&(y==my)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean canReach(Troop t){
		int ex = t.x;
		int ey = t.y;
		for(int i = -range; i<=range; i++) for(int j = -range; j<=range;j++){
			if (x+i==ex&&y+j==ey) return true; 
		}
		return false;
	}
	
}
