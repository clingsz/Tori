package elements;


import elements.Map;


public class Unit {

	public static int totalUnit = 0;
	
	//private final int WIDTH=1,HEIGHT=1;
	private final int SPEED=0;
	public int UnitID;
	int x,y;
	int destX,destY;
	int vx,vy;
	int state;
	Map m;

	int belongTo;
	String name;
	//contains
	int soldier;
	
	//public depthSearch depSolver;
    
	public Unit(int newX,int newY, Map newMap)	{
		x=newX; y=newY; vx=vy=SPEED;
		destX=x; destY=y;
		state=0;
		totalUnit++;
		UnitID=totalUnit;
		m = newMap;
		soldier = 100;
		//initSearch();
	}

//	public void moveUnit(){
//		if (((x==destX)&&(y==destY))) {
//			vx=0; vy=0;
//		}
//		else {
//			int next;
//			next=m.getNextStep(x,y,destX,destY);
//			switch(next){
//			case 0: vx=0; vy=0; break;
//			case 1: vx=1; vy=0; break;
//			case 2: vx=0; vy=1; break;
//			case 3: vx=-1; vy=0; break;
//			case 4: vx=0; vy=-1; break;
//			}
//		}
//	}

	
	public int getSoldier(){
		return soldier;
	}
	public void changeDest(int newx,int newy){
		destX=newx; destY=newy;
//		moveUnit();
	}
	

	public void select(){
		state=1;
	}
	public void clear(){
		state=0;
	}
	
	public boolean isSelected(){
		if (state==1) return true;
		else return false;
	}
	
	public boolean isInRange(int mx, int my){
		
		if ((x==mx)&&(y==my)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	public int getVX(){
		return vx;
		
	}
	public int getVY(){
		return vy;
	}
	public int getX(){
		return x;
		
	}
	public int getY(){
		return y;
	}
	public int getDestX(){
		return destX;
		
	}
	public int getDestY(){
		return destY;
	}
	
//	public void go(){
//		x+=vx;
//		y+=vy;
//		//reloadSearch();
//		moveUnit();
//	}
	
}

