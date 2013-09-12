package elements;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import common.global;

public class AI {

	String battleState = "Attack";
	public int AITIME = 1000;
	public int AIBATTLETIME = 200;
	public static int PLAYERAITIME = 200;
	public static int AUTOAITIME = 0;

	public void doWorld1(World world){

	}

	public int getRandomTroop(int player){
		return (int)(Math.ceil(Math.random()*4)+1)+(player-1)*5;
	}

	public int[] generateRandomList(int level){
		
		int flist[] = new int[6];
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i =1; i<=4; i++) list.add(i);
		Collections.shuffle(list);
		
		if (level ==1){
			flist[1]=3;
			int comp[] = {0,1,1,1,1}; 
			for(int i = 2; i<=5; i++)
				flist[i]=comp[list.get(i-2)];
			return flist;
		}
		else if (level == 2){
			flist[1]=3;
			int comp[] = {0,1,1,2,3}; 
			for(int i = 2; i<=5; i++)
				flist[i]=comp[list.get(i-2)];
			return flist;
		}
		else if (level==3){
			flist[1]=2;
			int comp[] = {0,3,3,3,3}; 
			for(int i = 2; i<=5; i++)
				flist[i]=comp[list.get(i-2)];
			return flist;
		}
		return null;
	}
	
	public void prepare(Battle battle, int player, int level){
			
		int flist[] = generateRandomList(level);
		for(int i = 1; i<=5;i++) battle.troop[player][i].type=flist[i];
//		battle.clearType(player);
//		battle.updateAvailable(player);
//
//		int k = getRandomTroop(player); 
//
//		while(battle.getTroop(k).isAvailable==false){
//			k = getRandomTroop(player);
//
//		}
//		battle.getTroop(k).type=3;
//		//		System.out.println(k);
//		battle.updateAvailable(player);
//		while(battle.getTroop(k).isAvailable==false) k = getRandomTroop(player);
//		battle.getTroop(k).type=2;
//		//		System.out.println(k);
	}

	public void doBattlePlan(Battle battle){
		

		if (battle.currentPlayer==2){
			battleState = "Defend";
			for(int i = 6; i<=10; i++){
				if (!battle.flag){
					battle.currentTroop=i;
					
					if (i==8||i==9){
						Point tar = new Point(9,7);
						if (battle.noTroop(tar)){
							screen.delay(AIBATTLETIME);
							battle.moveTo(i, tar);		
						}						
					}
					doBattle(battle,i);				
				}
			}			
		}	
		else{
			battleState = "Attack";
			Point tar = new Point(battle.getTroop(6).x,battle.getTroop(6).y);
			for(int i = 2; i<=5; i++){
				battle.currentTroop=i;
				screen.delay(AIBATTLETIME);
				battle.moveTo(i,tar);
				if (!battle.flag){
					doBattle(battle,i);				
				}
			}
			if (!battle.flag){
				battle.currentTroop=1;
				screen.delay(AIBATTLETIME);
				battle.moveTo(1,tar);
				doBattle(battle,1);
			}
		}
			
	}
	
	
	public void doWorld(World world, screen scr){
		int pnow = global.currentPlayer;

		for(int i = 1; i <= world.CityNum; i ++){
			world.city[i].Ap=0;
			world.city[i].Mp=0;
			if (world.city[i].belongTo==pnow){
				int maxEnemy = 0;
				for(int j = 1; j<=world.CityNum;j++){
					if (world.connect[i][j]){
						if (world.city[j].belongTo!=pnow){ // if is enemy
							maxEnemy = Math.max(maxEnemy, world.city[j].soldier);
						}
						else{  // if isn't enemy

						}
					}					
				}
				double nows = world.city[i].soldier;
				if (nows==0) nows = 1;
				world.city[i].Ap=maxEnemy*1.5/nows;
			}		
		}

		for(int i = 1; i <= world.CityNum; i ++){
			if (!(world.city[i].level==3&&world.city[i].Ap>1.5)&&world.city[i].belongTo==pnow&&world.city[i].soldier>=200&&world.city[i].moved==false){
				int atChoice = 0;
				ArrayList<Integer> cityList = new ArrayList<Integer>();
				
				for(int j = 1; j<=world.CityNum;j++){
					if (world.connect[i][j]){
						if (world.city[j].belongTo!=pnow){ // if is enemy
							if (world.city[i].soldier>world.city[j].soldier*1.5){
								atChoice = j;
								cityList.add(j);
							}
						}
						else{  // if isn't enemy
							//							world.city[i].Mp+= (-world.city[j].Ap);
						}
					}					
				}
				if (atChoice>0){
					//Attack
					int tar = getRandom(cityList);
					int troopNum = Math.max((int)(world.city[i].soldier*0.7),(int)(world.city[tar].soldier*1.5));

					global.currentCity = i;
					global.targetCity = tar;

					//								screen.msgbox("Attack from "+global.currentCity+" to "+tar+"with "+troopNum);
					scr.msg="Attack";
					scr.msg1="from "+global.currentCity+" to "+tar+" with "+troopNum;

					screen.delay(AITIME/2);

					scr.doBattle(global.currentCity,tar,troopNum);
					global.targetCity=0;

					screen.delay(AITIME/2);
					world.city[i].moved=true;
					break;
				}
			}		
		}

		
		for(int i = 1; i <= world.CityNum; i ++){
			//Upgrade Choice
			if (world.city[i].belongTo==pnow&&world.city[i].soldier>=100&&world.city[i].moved==false&&world.city[i].canUpgrade()){
				world.city[i].upgrade();
			}

		}
		
		
		for(int p = 1; p<=5; p++){
			for(int i = 1; i <= world.CityNum; i ++){
				//Move Choice

				if (!(world.city[i].level==3&&world.city[i].soldier<200)&&world.city[i].belongTo==pnow&&world.city[i].soldier>=100&&world.city[i].moved==false&&world.city[i].Ap==0){

					ArrayList<Integer> cityList = new ArrayList<Integer>();

					int mvChoice = 0;
					double maxAp = 0;
					for(int j = 1; j<=world.CityNum;j++){
						if (world.connect[i][j]){
							if (world.city[j].belongTo!=pnow||world.city[j].canMove()==false){ // if is enemy
								//							world.city[i].Ap+= (world.city[i].soldier-world.city[j].soldier*1.5);
							}
							else{  // if isn't enemy
								if (world.city[j].Ap>maxAp){
									cityList.clear();
									maxAp = world.city[j].Ap;
									mvChoice = j;
									cityList.add(j);
								}
								else if(world.city[j].Ap==maxAp){
									cityList.add(j);
								}
							}					
						}
					}
					// move choice:
					if (mvChoice >0){
						mvChoice = getRandom(cityList);
						global.currentCity = i;
						global.targetCity=mvChoice;
						int tar = mvChoice;
						
						int troopNum = world.city[global.currentCity].soldier/5*4;
						
						scr.msg="Move";
						scr.msg1="from "+global.currentCity+" to "+tar+" with "+troopNum;
//						screen.delay(AITIME/2);						
						world.city[global.currentCity].soldier-=troopNum;
						world.city[tar].waitAdd+=troopNum;
						global.targetCity=0;

						world.city[i].Ap=maxAp/2.0;

						world.city[i].moved=true;
//						screen.delay(AITIME/2);
					}
				}

			}

		}
		
		
		
		


	}

	public Integer getRandom(ArrayList<Integer> cityList){
		Collections.shuffle(cityList);
		return cityList.get(0);
	}

	public void doBattle(Battle battle, int seq){
		int choice = 0;
		int maxDamage = -battle.getTroop(battle.currentTroop).num/2;

		if (seq>5){
			battleState = "Defend";
		}	
		else{
			battleState = "Attack";
		}
		battle.setAvailable(battle.currentTroop);
//		screen.delay(AIBATTLETIME);
		for(int i = 1; i<=10;i++){
			if (battle.getTroop(i).isAvailable){
				int a1 = battle.currentTroop;
				int a2 = i;
				if (battle.getDamage(a1, a2)>maxDamage){
					choice = i;
					maxDamage = battle.getDamage(a1, a2);
				}
			}
		}		
		battle.clear();
		if (choice==0){
			battle.getTroop(battle.currentTroop).defend=true;
		}
		else{
			battle.targetTroop=choice;
			screen.delay(AIBATTLETIME);
			if (battle.currentPlayer==1) battle.getTroop(battle.currentTroop).defend=false;
			battle.fight(battle.currentTroop, choice);
			battle.targetTroop=0;
		}
		screen.delay(AIBATTLETIME);
	}		



	public void retreat(Battle battle,World world, int c2){

		//judge whether should retreat:

		int p1 = battle.currentPlayer;
		int p2 = battle.op(p1);

		if (battle.countRest(p1)<battle.countRest(p2)/3||battle.countRestTroopNum(battle.currentPlayer)<=3&&battle.troop[p1][1].num<battle.troop[p2][1].num/3){

			if(battle.currentPlayer==1){
				battle.win = 2;
				battle.flag = true;
				battle.battleMsg = "Attack side retreat!";
				screen.delay(AIBATTLETIME);
			}
			else if(battle.currentPlayer==2){
				if (world.hasRetreatCity(c2)){
					//target city;
					int tar =world.getMaxFriendNear(c2);
					if (tar>0){
						world.city[tar].soldier+=battle.countRest(2);
						battle.win = 1;
						battle.flag = true;
						battle.battleMsg = "Defend side retreat to "+tar+"!";
						screen.delay(AIBATTLETIME);
					}
				}
			}

		}
	}
}

