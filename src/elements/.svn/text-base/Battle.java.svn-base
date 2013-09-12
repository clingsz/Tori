package elements;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Battle {


	public int element[][] = new int[200][200];
	public int state[][] = new int[200][200];
	public String info[][] = new String[200][200];
	public String type[][] = new String[200][200];
	
	public boolean choosingMove = false;
	
	public Point targetPoint = null;
	
	public boolean canMove[][] = new boolean[200][200];

	public int score[][] = new int[200][200];
	
	public boolean flag = false;
	public int win = -1;
	
	public double scale = 1.0;
	
	public int p1;
	public int p2;

	public int l1;
	public int l2;
	
	public int tp1;
	public int tp2;

	public int currentPlayer = 0;
	public int currentTroop = 0;
	public int targetTroop = 0;

	public int turn = 1;

	public Troop troop[][]=new Troop[5][20];

	private int X=15,Y=15;
	
	
	public String battleMsg= "";

	public String battleMap = "1.map";
	
	public Battle(int np1, int np2, int ntp1, int ntp2, int nl1, int nl2){
		p1 = np1;
		p2 =np2;
		tp1 =ntp1;
		tp2 = ntp2;
		l1 = nl1;
		l2 = nl2;
		initial();
		loadFile(new File("battle.map"));
	}
	
	public void fillMove(){
		clearCanMove();
		int x = getTroop(currentTroop).x;
		int y = getTroop(currentTroop).y;
		int speed = getTroop(currentTroop).speed; 
		expand(x,y,speed);
	}
	
	int dx[] = {0,0,1,0,-1};
	int dy[] = {0,1,0,-1,0};
	
	public void expand(int x, int y, int rest){
//		System.out.println(x+","+y+","+rest);
		canMove[x][y]=true;
		if (rest==0) return;
		for(int dir = 1; dir<=4;dir++){
			int nx = x +dx[dir];
			int ny = y +dy[dir];
			if(isInMap(nx,ny)){
				if(canPass(nx,ny)){
					expand(nx,ny,rest-1);
				}
			}
		}
	}
	
	public boolean canPass(int x, int y){
		int thing = element[x][y];
		if (thing==7||thing==12){
			for(int i = 1; i<=10;i++){
				if (getTroop(i).x==x&&getTroop(i).y==y&&getTroop(i).num>0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean isInMap(int x, int y){
		if (x<=0||x>=X) return false;
		if (y<=0||y>=Y) return false;
		return true;
	}
	
	public boolean isOkToMove(Point k){
		if (k==null) return false;
		if (k.x<=0||k.x>=X) return false;
		if (k.y<=0||k.y>=Y) return false;
		return canMove[k.x][k.y];
	}

	public void initial(){
		int size1 = (int)(Math.floor(tp1/5));
		int size2 = (int)(Math.floor(tp2/5));
		
		for(int i = 1;i<=5;i++){
			troop[1][i]=new Troop(5,i*2,size1,1);
		}
		for(int i = 1;i<=5;i++){
			troop[2][i]=new Troop(9,i*2,size2,1);
			troop[2][i].range=2;
		}
		troop[1][1].x=3;
		troop[1][1].y=7;
		troop[2][1].x=11;
		troop[2][1].y=7;
		
//		troop[1][2].x+=1;
//		troop[1][5].x+=1;
		
		
		troop[2][2].x+=1;
		troop[2][2].y+=2;
		troop[2][5].x+=1;
		troop[2][5].y-=2;

//		for(int i = 1; i<=5; i++){
//			troop[1][i].x/=2;
//			troop[1][i].y/=2;
//			troop[2][i].x/=2;
//			troop[2][i].y/=2;
//		}
		
		troop[1][1].num=tp1-size1*4;
		troop[2][1].num=tp2-size2*4;
		troop[1][1].type=3;
		troop[2][1].type=3;
		
		troop[1][2].type=1;
		troop[2][2].type=1;
		
		troop[1][4].type=1;
		troop[1][5].type=1;
		troop[2][3].type=1;
		troop[2][5].type=1;
		
		for (int i=0;i<X;i++) for(int j=0;j<Y;j++){
			element[i][j]=7;
			state[i][j]=0;
			info[i][j]="";
			canMove[i][j] = false;
		}
		for(int i=1;i<=5;i++){
			troop[1][i].element=(p1+2)*10+troop[1][i].type;
			troop[2][i].element=(p2+2)*10+3+troop[2][i].type;
			troop[1][i].id=i;
			troop[2][i].id=i+5;
			troop[1][i].defend=false;
		}
		
		
		troop[1][1].range=2;
		troop[2][1].range=3;
		
		
	}
	/*
	 * Element 1-19 Map Type
	 * Element 21-30 City Unit
	 * Element 31-40 P1 Unit
	 * Element 41-50 P2 Unit
	 * 
	 * Element 301-350 Control Type
	 */
	public void fillElement(){
		for(int i=1;i<=5;i++){
			info[troop[1][i].x][troop[1][i].y]=troop[1][i].num+"";
			info[troop[2][i].x][troop[2][i].y]=troop[2][i].num+"";
			troop[1][i].element=(p1+2)*10+troop[1][i].type;
			troop[2][i].element=(p2+2)*10+3+troop[2][i].type;
		}

	}
	
	public boolean noTroop(Point p){
		for(int i = 1; i<=10; i++){
			if (getTroop(i).x==p.x&&getTroop(i).y==p.y&&getTroop(i).num>0){
				return false;
			}
		}
		return true;
	}
	
	public void newTurn(){
		for(int i=1;i<=10;i++){
			getTroop(i).moved=false;
		}
	}
	
	public void clearType(int x){
		for(int i=2;i<=5;i++){
			troop[x][i].type=1;
						
		}
	}
	
	public Troop getTroop(int x){
		for(int i=1;i<=5;i++){
			if (x==i) return troop[1][i];
			if (x==i+5) return troop[2][i];
		}
		return null;		
	}
	
	public void selectTroop(int x, int y, int button){
		int k = getTroop(x, y);
		if (k>0){
			if (button==1) {
				targetTroop=k;
//				global.currentPlayer=city[k].belongTo;
			}
//			if (button==3) global.targetCity=k;
		}else{
			targetTroop=0;
		}
	}
	
	public double kill[][] = {{0,0,0,0},
			{0,0.2,0.2,0.1},
			{0,0.1,0.1,0.3},
			{0,0.3,0.1,0.3}};
	
	public int getDamage(int a1, int a2){
		int num1 = getTroop(a1).num;
		int type1 = getTroop(a1).type;
		int num2 = getTroop(a2).num;
		int type2 = getTroop(a2).type;
		
		int loss1 = (int)(Math.ceil(num2*kill[type2][type1]));		
		int loss2 = (int)(Math.ceil(num1*kill[type1][type2]));
		
//		System.out.println(a1+","+a2+":"+getTroop(a1).defend+","+getTroop(a2).defend);
		if (a1>5&&getTroop(a2).defend==false){
//			loss1*=2;
			loss2*=2;
		}
		
		return (loss2-loss1);
	}
	
	public void fight(int a1, int a2){
		int num1 = getTroop(a1).num;
		int type1 = getTroop(a1).type;
		int num2 = getTroop(a2).num;
		int type2 = getTroop(a2).type;
		
		int loss1 = (int)(Math.ceil(num2*kill[type2][type1]));		
		int loss2 = (int)(Math.ceil(num1*kill[type1][type2]));
		
//		System.out.println(a1+","+a2+":"+getTroop(a1).defend+","+getTroop(a2).defend);
		if (a1>5&&getTroop(a2).defend==false){
//			loss1*=2;
			loss2*=2;
		}
		
		num1-=loss1;
		if (num1<0) num1=0;
		num2-=loss2;
		if (num2<0) num2=0;
		
		getTroop(a1).num=num1;
		getTroop(a2).num=num2;
		
//		screen.msgbox("Loss: "+loss1+":"+loss2);
		battleMsg = "Loss: "+loss1+":"+loss2;
		if (troop[1][1].num==0) {
			win = 0;
			flag = true;
		}
		else if(troop[2][1].num==0){
			win = 1;
			flag = true;
		}				
	}
	
	public int getTroop(int x, int y){
		for (int i=1;i<=10;i++){
			if ((getTroop(i).isInRange(x,y))&&getTroop(i).isAvailable==true) {
				return i;
			}			
		}
		return 0;
	}
	
	public ArrayList<Integer> getCanAttack(int curPlayer){
		
		ArrayList<Integer> tar  = new ArrayList<Integer>();
		
		for(int i = 1; i<=5; i++){
			Troop enemy = troop[op(curPlayer)][i];
			if (getTroop(currentTroop).canReach(enemy)&&enemy.num>0)
				tar.add(enemy.id);
		}		
		return tar;
	}

	
	public void moveTo(int no,Point p){
		int tarx = p.x;
		int tary = p.y;
		clearCanMove();
		currentTroop = no;
		int cx = getTroop(no).x;
		int cy = getTroop(no).x;
		fillMove();
		int minscore = 999999;
		Point tarPoint = new Point(cx,cy); 
		for(int i = 0; i<X; i++ ) for(int j = 0; j<Y;j++){
			if (canMove[i][j]){
				int tempscore = Math.abs(tarx-i)+Math.abs(tary-j);
				if (tempscore<minscore){
					tarPoint = new Point(i,j);
					minscore = tempscore;
				}
			}
		}
		clearCanMove();
		getTroop(no).updatePosition(tarPoint);
	}
	
	public void moveAttack(int tp){
		int tarx = getTroop(6).x;
		int tary = getTroop(6).y;
		clearCanMove();
		currentTroop = tp;
		int cx = getTroop(tp).x;
		int cy = getTroop(tp).x;
		fillMove();
		int minscore = 999999;
		Point tarPoint = new Point(cx,cy); 
		for(int i = 0; i<X; i++ ) for(int j = 0; j<Y;j++){
			if (canMove[i][j]){
				int tempscore = Math.abs(tarx-i)+Math.abs(tary-j);
				if (tempscore<minscore){
					tarPoint = new Point(i,j);
					minscore = tempscore;
				}
			}
		}
		clearCanMove();
		getTroop(tp).updatePosition(tarPoint);		
	}
	
	public void clearCanMove(){
		for(int i = 0; i<X; i++ ) for(int j = 0; j<Y;j++) canMove[i][j]=false;
	}
	
	public int countRest(int p){
		int count = 0;
		for(int i = 1; i<=5;i++) count+=troop[p][i].num;
		return count;		
	}
	
	public int countRestTroopNum(int p){
		int count = 0;
		for(int i = 1; i<=5;i++) if(troop[p][i].num>0) count++;
		return count;		
	}
	
	public int op(int p){
		if (p==1) return 2;
		else return 1;
	}
	
	public String getInfo(int i, int j){
		return info[i][j];
	}

	public int getState(int i,int j){
		return state[i][j];
	}

	public void refresh(){
		fillElement();
	}

	public int getElement(int i, int j){
		return element[i][j];
	}
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	
	public void updateAvailable(int player){
		clear();
		for(int i = 2;i<=5;i++) if (troop[player][i].type==1) troop[player][i].isAvailable=true; 
	}
	
	
	public void setAvailable(int x){
		
		clear();
		
		int k = 1;
		if (x<=5){
			k=1;
		}
		else if(x>5){
			k=2;
			x-=5;
		}
		
		ArrayList<Integer> alist = getCanAttack(k);
		for(int p : alist){
			getTroop(p).isAvailable=true;
		}		
//		System.out.println(x+":");
//		printList(alist);
	}
	
	public void printList(ArrayList<Integer> a){
		for(int k : a) System.out.print(k+" ");
		System.out.println();
	}

	
	public void setAvailable2(int x){
		
		clear();
		
		int k = 1;
		if (x<=5){
			k=2;
		}
		else if(x>5){
			k=1;
			x-=5;
		}
		
		if (x==1){
			for(int i =2;i<=5;i++) if (troop[k][i].num>0) troop[k][i].isAvailable=true;
			if(troop[k][3].num==0&&troop[k][4].num==0) troop[k][1].isAvailable=true;
		}
		else if (x==2){
			for(int i =2;i<=3;i++) if (troop[k][i].num>0) troop[k][i].isAvailable=true;
			if(troop[k][2].num==0) troop[k][1].isAvailable=true;
		}
		else if (x==3){
			for(int i =2;i<=4;i++) if (troop[k][i].num>0) troop[k][i].isAvailable=true;
			if(troop[k][3].num==0&&troop[k][4].num==0) troop[k][1].isAvailable=true;
		}
		else if (x==4){
			for(int i =3;i<=5;i++) if (troop[k][i].num>0) troop[k][i].isAvailable=true;
			if(troop[k][3].num==0&&troop[k][4].num==0) troop[k][1].isAvailable=true;
		}
		else if (x==5){
			for(int i =4;i<=5;i++) if (troop[k][i].num>0) troop[k][i].isAvailable=true;
			if(troop[k][5].num==0) troop[k][1].isAvailable=true;
		}
	}

	
	public void clear(){
		for(int i = 1; i<=10;i++) getTroop(i).isAvailable=false;
	}
	
	public void loadFile(File openfile){

		int temp[][] = new int[100][100];
		int x,y;
		try {
			Scanner reader = new Scanner(openfile);
			x = reader.nextInt();
			y = reader.nextInt();
			for(int i=0;i<x;i++) for (int j=0;j<y;j++){
				temp[i][j]=reader.nextInt();
			}
			loadMap(x,y,temp);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void loadMap(int x,int y,int[][] temp){
		
		X=x;
		Y=y;
		
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++){
			int number = temp[i][j];
			element[i][j]=number;
		}

	}
}
