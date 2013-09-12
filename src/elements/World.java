package elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import common.global;

public class World {
	private int element[][] = new int[200][200];
	private int state[][] = new int[200][200];
	public String info[][] = new String[200][200];
	public String type[][] = new String[200][200];
	//	private Unit t[]=new Unit[10];
	public int CityNum;
	public City city[] = new City[100];

	public boolean[][] connect = new boolean[400][400];

	public Player player[] = new Player[10];
	
	public int PlayerNum = 0;

	private int X,Y;
	public Map m;

	// 1 2 3 4 -> V <- ^
	public static int dx[] = {0,1,0,-1,0};
	public static int dy[] = {0,0,+1,0,-1};

	


	public World(String setting){
		if (setting=="Random"){
			m = new Map("Random");
			CityNum=5;
			X=m.getX();
			Y=m.getY();
		}
		else if (setting=="Normal"){
			File f = new File("aslanter.world");
//			File f = new File("fourland.world");
			loadWorld(f);
		}
		else if(setting=="Edit"){
			CityNum=0;
			m = new Map("Empty");
			X=m.getX();
			Y=m.getY();
		}
//		initial();
	}

	public void connectFill(){
		for(int i = 1; i<=CityNum;i++)
			for(int j = 1; j<=CityNum;j++){
				connect[i][j]=false;
			}
		for(int i = 1; i<=CityNum;i++){
			judgeConnect(i);
		}
	}
	
	public boolean isACity(int type){
		if (type>30&&type<=100){
			int temp = type % 10;
			if (temp>=7&&temp<=9){
				return true;
			}
		}
		return false;
	}
	
	public void setCityAttribute(City c, int type){
		int belong = type /10 - 2;
		int level = (type % 10)-6;
		c.belongTo=belong;
		c.level=level;
	}
	
	public void locateNewCity(){
		PlayerNum = 0;
		CityNum  = 0;
		city[0] =new City(0,0);
		city[0].belongTo=0;		
		for(int i = 0 ;i <X;i++) for(int j=0;j<Y;j++){
			int temp = m.type[i][j];
			if (isACity(temp)){
				CityNum++;
				city[CityNum]=new City(i,j);
//				city[CityNum].belongTo=temp-20;
				setCityAttribute(city[CityNum],temp);
				PlayerNum = Math.max(city[CityNum].belongTo, PlayerNum);
			}			
		}

		for(int i = 0; i<=PlayerNum;i++) player[i]=new Player();
		fillElement();
		connectFill();
	}


	public String getType(int temp){
		String res = "";
		if ((temp>=1)&&(temp<=6)){
			res="road";

		}
		if ((temp==7)){
			res="obstruct";		
		}
		if ((temp>=20)&&(temp<=200)){
			res="unit";
		}
		return res;
	}

	public ArrayList<Integer> getNear(int x){
		ArrayList<Integer> nearcity = new ArrayList<Integer>();
		for(int i = 1; i<=CityNum; i++){
			if (connect[x][i]) nearcity.add(i);
		}
		return nearcity;
	}
	
	public void availableClear(){
		for(int i = 1; i<=CityNum; i++){
			city[i].isAvailable=false;
		}
	}
	public void addWaitList(){
		for(int i =1;i<=CityNum;i++){
			city[i].soldier+=city[i].waitAdd;
			city[i].waitAdd=0;
		}
		
		for(int i = 1; i<=CityNum;i++){
			if (city[i].moved==false&&city[i].belongTo==global.currentPlayer){
//				screen.msgbox("City "+i+" Recruit 30 soldiers");
				city[i].recruit();
				city[i].moved = true;
			}
		}
	}

	public boolean hasRetreatCity(int c){
		for(int i = 1; i<=CityNum;i++){
			if (city[i].belongTo==city[c].belongTo&&connect[i][c]) return true;
			
		}		
		return false;
	}
	private void judgeConnect(int c){
//		System.out.println(c);
		int x = city[c].x;
		int y = city[c].y;

		for(int dir = 1; dir<=4;dir++){
			judgedir(dir,x,y,c);
		}
	}

	int diroad[][] = {{0,0,0,0,0,0,0},
			{0,1,0,0,0,2,4},
			{0,0,2,1,0,0,3},
			{0,3,0,4,2,0,0},
			{0,0,4,0,1,3,0}
	};			

	private void judgedir(int dir,int x, int y, int c){
		int elenow = m.type[x+dx[dir]][y+dy[dir]];
		if (getType(elenow)=="road"){
			int findir = 0;
			findir = diroad[dir][elenow];
			if (findir>0) judgedir(findir,x+dx[dir],y+dy[dir],c);
		}
		else if(getType(elenow)=="unit"){
			int c2 = getCity(x+dx[dir],y+dy[dir]);
			connect[c][c2]=true;
			connect[c2][c]=true;
		}
	}

	public int countLeft(){
		int temp = 0 ;
		for(int i = 1;i<=CityNum;i++) if (city[i].moved==false) temp++;
		return temp;
	}
	
	public int countPlayerLeft(){
		int temp = 0 ;
		for(int i = 1;i<=PlayerNum;i++) if (player[i].moved==false) temp++;
		return temp;
	}
	
	public boolean isPlayerAlive(int x){
		for(int i = 1; i<=CityNum;i++){
			if (city[i].belongTo==x) return true;
		}
		return false;
	}
	public void newTurn(){
		for(int i = 1;i<=CityNum;i++){
//			city[i].soldier+=30;
			city[i].moved=false;
		}
		
	}
	
	public void newPlayerTurn(){
		for(int i = 1;i<=CityNum;i++){
			city[i].moved=false;
		}
		
	}

	/*
	 * Element 1-19 Map Type
	 * Element 21-30 Player1 Unit
	 * Element 31-40 Player2 Unit
	 * Element 41-50 Player3 Unit
	 * 
	 * Element 301-350 Control Type
	 */
	public void fillElement(){
		int i,j,temp;
		int citycnt = 0;
		for (i=0;i<X;i++) for(j=0;j<Y;j++){
			element[i][j]=m.type[i][j];
			state[i][j]=0;
			info[i][j]="";
			temp = element[i][j];
			if ((temp>=1)&&(temp<=19)){
				type[i][j]="Map";

			}
			if ((temp>=21)&&(temp<=200)){
				type[i][j]="Unit";
				info[i][j]=+citycnt+"";

			}
			if ((temp>=300)&&(temp<=350)){
				type[i][j]="Control";
			}
		}

		for(i=1;i<=CityNum;i++){
			state[city[i].x][city[i].y] = 0;
			if (global.currentCity==i) state[city[i].x][city[i].y]=1;
			if (global.targetCity==i) state[city[i].x][city[i].y]=2;
		}
	}
	public String getInfo(int i, int j){
		return info[i][j];
	}

	public int getState(int i,int j){
		return state[i][j];
	}

	public void refresh(){
		if (global.state=="Edit")locateNewCity();
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

	public void selectCity(int x, int y, int button){
		int k = getCity(x, y);
		if (k>0){
			if (button==1) {
				global.tempCity=k;
				//				global.currentPlayer=city[k].belongTo;
			}
			//			if (button==3) global.targetCity=k;
		}else{
			global.tempCity=0;
		}
	}

	public int getCity(int x, int y){
		for (int i=1;i<=CityNum;i++){
			if ((city[i].isInRange(x,y))) {
				city[i].select();
				return i;
			}
		}
		return 0;
	}

	public void changeType(int i,int j,int newType){
		m.changeType(i, j, newType);
	}

	public void clear(){
		m.clear();
		CityNum = 0;
		PlayerNum = 0;
	}

	public void saveWorld(File file){
		FileWriter out = null;
		try {
			out = new FileWriter(file);

			out.write(String.valueOf(X));
			out.write(" ");

			out.write(String.valueOf(X));
			out.write(" ");
			for (int i=0;i<X;i++){
				for (int j=0;j<Y;j++){

					out.write(String.valueOf(m.type[i][j]));
					out.write(" ");
				}
				out.write("\n");
			}
			out.write(PlayerNum+"\n");
			out.write(CityNum+"\n");
			for(int i = 1; i<=CityNum;i++){
				String output = city[i].x+" "+city[i].y+" "+city[i].belongTo+" "+city[i].soldier+" "+city[i].level+" "+city[i].moved;
				out.write(output+"\n");
			}
			out.write(global.currentPlayer+"\n");
			out.write(global.currentCity+"\n");
			out.write(global.year+"\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				out = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	
	public int countRestPlayer(){
		int cnt = 0;
		for(int i = 1; i<= PlayerNum; i++){
			if (isPlayerAlive(i)) cnt++;
		}
		return cnt;
	}

	public int getMaxFriendNear(int c){
		int max = -1; 
		int tar = 0;
		for(int i = 1; i<=CityNum; i++){
			if (connect[c][i]&&city[i].belongTo==city[c].belongTo){
				if (city[i].soldier>max){
					max = city[i].soldier;
					tar = i;
				}
			}
		}
		return tar;
	}
	
	public void updateCityInMap(){
		for(int i = 1; i<=CityNum;i++){
			int x = city[i].x;
			int y = city[i].y;
			int t = screen.getCityImage(city[i]);
			m.type[x][y]=t;
		}
	}
	
	public void loadWorld(File file){
		int temp[][] = new int[100][100];
		int x,y;
		m=new Map("Empty");
		try {
			Scanner reader = new Scanner(file);
			x = reader.nextInt();
			y = reader.nextInt();
			X=x;
			Y=y;
			for(int i=0;i<x;i++) for (int j=0;j<y;j++){
				temp[i][j]=reader.nextInt();
			}
			m.loadMap(x,y,temp);
			PlayerNum = reader.nextInt();
			for(int i = 1; i<=PlayerNum;i++){
				player[i]=new Player();
				player[i].AI="AI";
				if (i==1) player[i].AI="AI";
			}
			CityNum = reader.nextInt();
			for(int i = 1; i<=CityNum;i++){
				int nx = reader.nextInt();
				int ny = reader.nextInt();
				int nbelongto = reader.nextInt();
				int ns = reader.nextInt();
				int nl = reader.nextInt();
				boolean nmoved = reader.nextBoolean();
				city[i]=new City(nx,ny);
				city[i].belongTo=nbelongto;
				city[i].soldier=ns;
				city[i].level=nl;
				city[i].moved=nmoved;
			}
			global.currentPlayer=reader.nextInt();
			global.currentCity=reader.nextInt();
			global.year=reader.nextInt();
			updateCityInMap();
			connectFill();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	/********
	 * world file format:
	 * X Y
	 * [i][j] 
	 * PlayerNum
	 * CityNum
	 * [i] X Y BelogTo Soldier
	 * CurrentPlayer
	 * CurrentCity
	 * Year
	 * 
	 * 
	 */

}
