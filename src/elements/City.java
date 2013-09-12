package elements;

public class City {
	public static int totalUnit = 0;
	
	//private final int WIDTH=1,HEIGHT=1;

	public int UnitID;
	public int x,y;
	int state;

	public int belongTo;
	public String name;
	//contains
	public int soldier;
	public int waitAdd=0;
	
	public boolean moved = false;
	public boolean isAvailable = false;
	
	public double Ap = 0;
	public double Mp = 0;
	
	public int level = 1;
	public static int recruit[] = {0,30,60,100};
	public static int TroopSize[] = {0,1000,2000,5000};
	public static int upgradeCost[] = {0,0,500,1000};
	
	
	public City(int newX,int newY){
		x=newX; y=newY; 
		state=0;
		totalUnit++;

		UnitID=totalUnit;
		
		soldier = 100;
	}
	
	public boolean isSelected(){
		if (state==1) return true;
		else return false;
	}

	public void select(){
		state=1;
	}
	public void clear(){
		state=0;
	}
	
	public void recruit(){
		if (soldier<TroopSize[level]){
			soldier+=recruit[level];
		}
	}
	
	public boolean canMove(){
		if (soldier>TroopSize[level]){
			return false;
		}
		return true;
		
	}
	
	
	public boolean canUpgrade(){
		if (level==3) return false;
		if (soldier>=upgradeCost[level+1]){
			return true;
		}
		return false;
		
	}
	
	public void upgrade(){
		if (level==3) return;
		if (soldier>=upgradeCost[level+1]){
			soldier-=upgradeCost[level+1];
			level++;
			
		}
		
	}
	
	
	public boolean isInRange(int mx, int my){
		
		if ((x==mx)&&(y==my)){
			return true;
		}
		else{
			return false;
		}
	}
	
}
