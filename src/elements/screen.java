package elements;

//import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ControlInfo.Control;
import ControlInfo.CtrlBtn;
import ControlInfo.ImageMaker;

import common.global;

public class screen{

	private static Color SYSCOLOR = Color.getHSBColor(0, 0, 0);
	boolean haveLoad = false;
	public World world = new World("Normal");
	private Control control = new Control("");
	private Battle battle = new Battle(4,3,1320,9,1,1);

	private int humanPlayerNum = 1;

	public int curx = 0;
	public int cury = 0;

	private final int X=15, Y=15;
	private int OFFSET=5;

	private int nowX=0,nowY=0;
	private int WIDTH=20,HEIGHT=20;
	private int LEFT=WIDTH, TOP=HEIGHT;
	private int RIGHT=LEFT+X*WIDTH, BOTTOM=TOP+HEIGHT*Y;
	private int CLEFT=LEFT;

	private static boolean showMsg = false;
	private static String msgStr = "";

	private String state = "";
	private int choiceNum;
	public String msg = "";
	public String msg1 = "";
	public String msg2 = "";
	private String statusMsg = "status:";

	private static int DELAYMS = 100;
	private int MSGMS = 1000;
	private int sizeIndex = 60;

	public static int strokeLen = 5;

	Image[] img=new Image[500];

	double perc[] = {0,0.1,0.25,0.5,0.75,0.9,1.0}; 

	public boolean mouseCancel = false;

	private AI ai = new AI();

	String stage;

	/*
	 * Element 1-19 Map Type
	 * Element 21-30 city Unit
	 * 
	 * Element 301-350 Control Type
	 */
	public screen(String method){

		stage = method;

		if (method.equals("Edit")){
			world = new World("Edit");
			control = new Control("Edit");		
		}
		else{
			world = new World("Normal");
			control = new Control(global.state);
		}
		world.refresh();
		initialImage();
	}

	public void mainGame(){
		startGame();
		while (haveLoad) game();
		endGame();

	}

	public void changeStage(String newStage){
		global.state=newStage;

	}


	public void startGame(){
		msg = "How many Human Players?";
		msg1 = "";
		msg2 = "";
		global.currentPlayer=0;

		changeStage("Menu");
		int choice = getChoice(4);
		if (choice>=0){
			haveLoad = true;
			changeStage("World");			
			for(int i = 1; i<=choice; i++){
				world.player[i].AI="No";
			}
		}		
		else{
			haveLoad = false;
		}
	}
	public void endGame(){
		msg = "";
		msg1 = "";
		msg2 = "";
		global.currentPlayer=0;
		control.updateButtonSet("");
		for(int i = 1; i<= 4; i++){
			if (world.isPlayerAlive(i)){
				msgbox("Player "+i+" is alive!");
			}
		}

		changeStage("End");		
		delay(2000);
		System.exit(0);
	}
	public void game(){

		//Choose Map:

		//chooseAvailable
		msg = "";
		msg1 = "";
		msg2 = "";
		msgbox("Welcome!");
		while(true){
			while(global.currentPlayer<=world.PlayerNum){

				haveLoad = false;
				boolean endPlayer = false;

				if (world.countRestPlayer()==1){
					msgbox("Game is done!");
					endGame();
				}

				if (!world.isPlayerAlive(global.currentPlayer)){
					endPlayer=true;
				}
				else{
					//					msgbox("Player "+global.currentPlayer+"'s Turn");

				}


				while(!endPlayer){
					if (world.player[global.currentPlayer].AI!="No"){
						ai.doWorld(world,this);
						endPlayer = true;
					}
					else{
						global.currentCity=0;
						global.targetCity=0;
						msg = "Which city to order?";
						msg1 = "Left Click to End Turn";

						int next = gettargetCity("Select");
						if (next==0) {
							int kk = getokChoice();
							if (kk==1){
								endPlayer = true;
							}
							continue;
						}

						if (world.city[next].moved==false){
							global.currentCity=next;
							boolean endFlag = false;
							msg = "Year Plan";
							msg1 = "";
							int k = getcurrentChoice();
							if (k==0) continue;
							if (k==1){ //Attack
								msg = "Attack";
								msg1 = "From City "+global.currentCity;
								int tar = gettargetCity("Attack");
								if (tar==0) continue;
								msg1 = "From "+global.currentCity+" to "+tar;
								int kk = getpercentChoice();
								if (kk==0) continue;
								double ratio = perc[kk];
								int troopNum = (int)(Math.ceil(world.city[global.currentCity].soldier*ratio));

								if (troopNum<100){
									msgbox("You must have at least 100 soldiers to attack");
								}
								else{
									msg1 = global.currentCity+" to "+tar+": "+troopNum;
									kk = getokChoice();
									if (kk==0) continue;
									if(kk==1){
										doBattle(global.currentCity,tar,troopNum);
										endFlag =true;
									}
									global.targetCity=0;
								}

							}
							else if(k==2){ //Move
								msg = "Move";
								msg1 = "From City "+global.currentCity;
								int tar = gettargetCity("Move");
								if (tar==0) continue;
								msg1 = "From "+global.currentCity+" to "+tar;
								int kk = getpercentChoice();
								if (kk==0) continue;
								double ratio = perc[kk];
								int troopNum = (int)(Math.ceil(world.city[global.currentCity].soldier*ratio));
								msg1 = global.currentCity+" to "+tar+": "+troopNum;
								kk = getokChoice();
								if (kk==0) continue;
								if(kk==1){
									world.city[global.currentCity].soldier-=troopNum;
									world.city[tar].waitAdd+=troopNum;
									endFlag =true;
								}
								global.targetCity=0;
							}
							else if(k==3){ //Recruit
								msg = "Recruit";
//								msg1 = "You can add 30 army";
								int kk = getokChoice();
								if (kk==1){
									world.city[global.currentCity].recruit();
//									msgbox("City "+global.currentCity+" recruited 30 soldiers!");
									endFlag =true;
								}
							}
							else if (k==4){
								msg = "End Turn";
								msg1 = "";
								int kk = getokChoice();
								if (kk==1){
									endFlag =true;
								}						
							}
							else if (k==5){
								msg = "UpgradeCity";
								if (!world.city[global.currentCity].canUpgrade()) {
									msgbox("You don't have enough soldier to upgrade!");
									continue;
								}
								int kk = getokChoice();
								if (kk==1){
									endFlag =true;
									world.city[global.currentCity].upgrade();
								}
							}
							else if (k==6){
								msg = "Save World";
								control.saveWorld(world);
							}
							else if (k==7){
								msg = "Load World";
								control.loadWorld(world);
								haveLoad = true;
								return;
								//							endFlag = true;
								//							endPlayer = true;

							}

							if(haveLoad) break;
							if (endFlag) world.city[next].moved=true;
						}
						if(haveLoad) break;

					}//Ai judge
				}// End player
				if(haveLoad) continue;
				world.addWaitList();

				if (global.currentPlayer==world.PlayerNum){
					global.currentPlayer=1;
					break;
				}
				else{
					global.currentPlayer++;
					world.newPlayerTurn();
				}
			}
			global.year++;
			//			msgbox("This is Year "+global.year+" All city get new soldiers!");
			world.newTurn();

		}

	}

	String pstr[] = {"","Attack","Defend"}; 
	//player is the battle 1 2, pNo is in world
	public void prepare(int player,int pNo, int level){
		ai.prepare(battle, player, level);
//		if(world.player[pNo].AI=="No"){
//
//			battle.clearType(player);
//			battle.currentPlayer=player;
//			screen.msgbox(pstr[player]+" Side designate the horse troop:");
//			battle.updateAvailable(player);
//			int k = gettargetTroop();
//			if(k!=0){
//				battle.getTroop(k).type=3;
//				battle.updateAvailable(player);
//			}
//			screen.msgbox(pstr[player]+" Side designate the pike troop:");
//			k = gettargetTroop();
//			if(k!=0) battle.getTroop(k).type=2;
//		}
//		else if(world.player[pNo].AI=="AI"){
//			ai.prepare(battle, player);
//		}
	}
/*
	public void doBattle3(int c1, int c2, int t1){
		battle.win=-1;
		int t2 = world.city[c2].soldier;
		int p1 = world.city[c1].belongTo;
		int p2 = world.city[c2].belongTo;
		battle = new Battle(p1,p2,t1,t2);

		if (t2<10){
			battle.win=1;
			battle.flag=true;
		}
		else{
			changeStage("Battle");
			control.updateButtonSet(global.state);
			curx = 0;
			cury = 0;

			int kk,k;
			//			msgbox("Want to set?");
			if (world.player[p1].AI=="No"||world.player[p2].AI=="No"){
				kk = getokChoice();
				k = 0;
				if (kk==1){
					prepare(1,p1);
					prepare(2,p2);
				}
				ai.AIBATTLETIME=500;
			}
			else{
				ai.AIBATTLETIME=0;
			}

			//			msgbox("Battle Start!");
			battle.clear();

			battle.turn=1;
			int ps[] = {0,p1,p2};
			while(!battle.flag){

				if(battle.turn>10){
					msgbox("We don't have enough food to continue battle!");
					battle.flag=true;
					battle.win=2;
				}
				else{
					int seq[] = {0,6,7,8,9,10,1,2,3,4,5};
					for(int seqi=1;seqi<=10;seqi++){
						if (battle.flag) break;
						int i = seq[seqi];
						if (i==1){
							//							msgbox("Day "+battle.turn+", Attack Side");
							battle.currentPlayer=1;
						}
						else if(i==6){
							//							msgbox("Day "+battle.turn+", Defend Side");
							battle.currentPlayer=2;
						}					

						//player choice
						if (battle.getTroop(i).num>0){
							battle.currentTroop=i;
							//							for(int ss = 1; ss<=world.PlayerNum; ss++){
							//								System.out.println(world.player[ss].AI);
							//							}
							if (world.player[ps[battle.currentPlayer]].AI!="No"){
								ai.doBattle(battle,i);
								ai.retreat(battle, world, c2);
							}
							else{
								boolean endFlag = false;
								while(!endFlag){
									msg = "Player "+ps[battle.currentPlayer];

									curx = battle.getTroop(i).x;
									cury = battle.getTroop(i).y;

									k = getcurrentChoice();
									if (k==1){ //Attack
										msg = "Attack";
										battle.setAvailable(battle.currentTroop);
										int tar = gettargetTroop();
										battle.clear();
										if (tar==0) continue;

										if (battle.currentPlayer==1) battle.getTroop(battle.currentTroop).defend=false;

										battle.fight(battle.currentTroop,tar);									
										endFlag =true;
										battle.targetTroop=0;
									}
									else if (k==2){
										msg = "Defend";
										battle.getTroop(battle.currentTroop).defend=true;
										endFlag =true;
									}
									else if (k==3){
										msg = "Retreat";
										msgbox("Are you sure to retreat?");
										kk = getokChoice();
										if (kk==1){
											if(battle.currentPlayer==1){
												battle.win = 2;
												battle.flag = true;
												endFlag =true;
											}
											else if(battle.currentPlayer==2){
												if (world.hasRetreatCity(c2)){
													changeStage("World");

													int tempc = global.currentCity;
													int tempp = global.currentPlayer;
													global.currentCity=c2;
													global.currentPlayer = world.city[2].belongTo;
													int tar = gettargetCity("Move");
													global.currentCity=tempc;
													global.currentPlayer = tempp;
													if (tar>0){
														world.city[tar].soldier+=battle.countRest(2);
														battle.win = 1;
														battle.flag = true;
														endFlag =true;
													}
													else{
														changeStage("Battle");														
													}
												}
												else{
													msgbox("No city to retreat!");
												}
											}										
										}
									}
								}//endflag	
							}//judge ai
							battle.currentTroop=0;
							curx = 0;
							cury = 0;
						}// if num >0;
					}// for troops
					battle.turn++;

				}//while battle flag			
			}
			//			msgbox("Battle is End!");


			changeStage("World");
			control.updateButtonSet(global.state);
		}

		if (battle.win==1){
			//			msgbox("We have taken city "+c2);
			world.city[c2].belongTo=world.city[c1].belongTo;
			world.city[c2].moved=true;
			world.city[c2].soldier=battle.countRest(1);
			world.city[c1].soldier-=t1;
		}
		else if(battle.win==0){
			//			msgbox("We Lose the battle in "+c2+", "+battle.countRest(1)+" soldiers surrendered!");
			world.city[c1].soldier=world.city[c1].soldier-t1;
			world.city[c2].soldier=battle.countRest(2)+battle.countRest(1);			
		}
		else if(battle.win==2){
			//			msgbox("We retreat from "+c2);
			world.city[c1].soldier=world.city[c1].soldier-t1+battle.countRest(1);
			world.city[c2].soldier=battle.countRest(2);			
		}

	}
*/

	public void doBattle(int c1, int c2, int t1){
		battle.win=-1;
		global.targetCity=c2;
		int t2 = world.city[c2].soldier;
		int p1 = world.city[c1].belongTo;
		int p2 = world.city[c2].belongTo;
		int l1 = world.city[c1].level;
		int l2 = world.city[c2].level;
		battle = new Battle(p1,p2,t1,t2,l1,l2);

		if (t2<10){
			battle.win=1;
			battle.flag=true;
		}
		else{
			changeStage("Battle");
			control.updateButtonSet(global.state);
			curx = 0;
			cury = 0;

			int kk,k;
			//			msgbox("Want to set?");
			if (world.player[p1].AI=="No"||world.player[p2].AI=="No"){
				prepare(1,p1,l1);
				prepare(2,p2,l2);
				ai.AIBATTLETIME=ai.PLAYERAITIME;
			}
			else{
				prepare(1,p1,l1);
				prepare(2,p2,l2);
				ai.AIBATTLETIME=ai.AUTOAITIME;
			}

			
//			prepare(1,p1);
//			prepare(2,p2);
//			ai.AIBATTLETIME=200;

			
			//			msgbox("Battle Start!");
			battle.clear();

			battle.turn=1;
			int ps[] = {0,p1,p2};

			while(!battle.flag){
				battle.newTurn();
				//				int seq[] = {0,p2,p1};
				if(battle.turn>10){
//					msgbox("We don't have enough food to continue battle!");
					battle.flag=true;
					battle.win=2;
				}
				else{
					for(int currentPlayer = 2; currentPlayer >=1; currentPlayer--){
						battle.currentPlayer=currentPlayer;
						boolean endPlayer = false;

						while(!endPlayer){
							int what = 1;
							if (battle.flag) break;
							//							System.out.println(world.player[seq[battle.currentPlayer]].AI);
//							if (world.player[ps[battle.currentPlayer]].AI!="No"){
							if(what==1){
								ai.retreat(battle, world, c2);
								ai.doBattlePlan(battle);								
								endPlayer = true;
							}
							else if(what==2){
								battle.currentTroop=0;
								battle.targetTroop=0;
								msg = "Which troop to order?";
								msg1 = "Left Click to End Turn";

								int next = gettargetTroop("Select");
								if (next==0) {
									kk = getokChoice();
									if (kk==1){
										endPlayer = true;
									}
									continue;
								}

								if (battle.getTroop(next).moved==false){
									battle.currentTroop=next;
									int i = next;
									msg = "Player "+ps[battle.currentPlayer];
									msg1 = "Make order";
									

									//									k = getcurrentChoice();
									//									if (k==1){ //Move
									curx = battle.getTroop(i).x;
									cury = battle.getTroop(i).y;
									msg = "Move";
									Point oldp = new Point(curx,cury);
									curx=0;
									cury=0;
									Point tarp = getTargetMove("Move");
									battle.clear();
									if (tarp==null) continue;
									if (battle.currentPlayer==1) battle.getTroop(battle.currentTroop).defend=false;
//									battle.getTroop(i).moved=true;
									battle.getTroop(i).updatePosition(tarp);
									curx = battle.getTroop(i).x;
									cury = battle.getTroop(i).y;
									
									battle.targetTroop=0;
									k = getcurrentChoice();
									if (k==0){
										battle.getTroop(i).updatePosition(oldp);
										curx=0;
										cury=0;
										continue;
									}
									if (k==1){ //Attack
										msg = "Attack";
										int tar = gettargetTroop("Attack");
										battle.clear();
										if (tar==0) continue;
										if (battle.currentPlayer==1) battle.getTroop(battle.currentTroop).defend=false;
										battle.fight(battle.currentTroop,tar);									
										battle.getTroop(i).moved=true;
										battle.targetTroop=0;
									}
									else if (k==2){
										msg = "Defend";
										battle.getTroop(battle.currentTroop).defend=true;
										battle.getTroop(i).moved=true;
									}
									else if (k==3){
										msg = "Retreat";
										msgbox("Are you sure to retreat?");
										kk = getokChoice();
										if (kk==1){
											if(battle.currentPlayer==1){
												battle.win = 2;
												battle.flag = true;

											}
											else if(battle.currentPlayer==2){
												if (world.hasRetreatCity(c2)){
													changeStage("World");

													int tempc = global.currentCity;
													int tempp = global.currentPlayer;
													global.currentCity=c2;
													global.currentPlayer = world.city[2].belongTo;
													int tar = gettargetCity("Move");
													global.currentCity=tempc;
													global.currentPlayer = tempp;
													if (tar>0){
														world.city[tar].soldier+=battle.countRest(2);
														battle.win = 1;
														battle.flag = true;
													}
													else{
														changeStage("Battle");														
													}
												}
												else{
													msgbox("No city to retreat!");
												}
											}										
										}
									}
								}//In a troop
							}//judge ai
							battle.currentTroop=0;
							curx = 0;
							cury = 0;
						}// if num >0;
					}// for troops
				}// turn judge
				battle.turn++;

			}//while battle flag			
		}// after battle
		//msgbox("Battle is End!");


		changeStage("World");
		control.updateButtonSet(global.state);

		if (battle.win==1){
			//			msgbox("We have taken city "+c2);
			world.city[c2].belongTo=world.city[c1].belongTo;
			world.city[c2].moved=true;
			world.city[c2].soldier=battle.countRest(1);
			world.city[c1].soldier-=t1;
		}
		else if(battle.win==0){
			//			msgbox("We Lose the battle in "+c2+", "+battle.countRest(1)+" soldiers surrendered!");
			world.city[c1].soldier=world.city[c1].soldier-t1;
			world.city[c2].soldier=battle.countRest(2)+battle.countRest(1);			
		}
		else if(battle.win==2){
			//			msgbox("We retreat from "+c2);
			world.city[c1].soldier=world.city[c1].soldier-t1+battle.countRest(1);
			world.city[c2].soldier=battle.countRest(2);			
		}

	}

	public static void msgbox(String input){
		msgStr = input;
		showMsg = true;
		while(showMsg){
			delay(DELAYMS);
		}
	}

	public int getpercentChoice(){
		String stemp = state;
		String temp = control.buttonMode;

		choiceNum = 6;
		state = "percentChoice";

		control.updateButtonChoice(state,choiceNum);


		global.currentChoice=0;
		msg2 = "How much percent?";
		mouseCancel = false;
		while(global.currentChoice<=0){
			if (mouseCancel) break;
			delay(DELAYMS);
		}
		msg2 = "";
		control.updateButtonSet(temp);
		state=stemp;

		return global.currentChoice;
	}

	public int getokChoice(){
		// 1 for yes 3 for no
		String stemp = state;
		String temp = control.buttonMode;

		choiceNum = 2;
		state = "okChoice";

		control.updateButtonChoice(state,choiceNum);


		global.currentChoice=0;
		msg2 = "Is this OK?";
		mouseCancel = false;
		while(global.currentChoice<=0){
			if (mouseCancel) break;
			delay(DELAYMS);
		}
		msg2 = "";
		control.updateButtonSet(temp);
		state=stemp;
		return global.currentChoice;
	}


	public int gettargetCity(String mode){
		String temp = control.buttonMode;
		control.updateButtonChoice("",0);
		msg2 = "To which City?";
		world.availableClear();
		int choicenum = 0;
		if (mode=="Attack"){
			for(Integer k : world.getNear(global.currentCity)){
				if (world.city[k].belongTo!=world.city[global.currentCity].belongTo){
					world.city[k].isAvailable=true;
					choicenum++;
				}
			}
		}	
		else if (mode=="Move"){
			for(Integer k : world.getNear(global.currentCity)){
				if (world.city[k].belongTo==world.city[global.currentCity].belongTo){
					world.city[k].isAvailable=true;
					choicenum++;
				}
			}
		}		
		else if (mode=="Select"){
			msg2 = "Choose:";
			for(int i = 1; i<=world.CityNum;i++){
				if (world.city[i].belongTo==global.currentPlayer&&world.city[i].moved==false&&world.city[i].soldier>=100)
					world.city[i].isAvailable=true;
				choicenum++;
			}
		}	
		else{
			for(int i = 1; i<=world.CityNum;i++){
				world.city[i].isAvailable=true;
				choicenum++;
			}
		}	

		if (choicenum==0){
			msgbox("No near city to make this choice!");
			global.targetCity=0;
		}
		else{

			global.tempCity=0;
			mouseCancel = false;
			while(global.tempCity<=0||world.city[global.tempCity].isAvailable==false){
				if (mouseCancel){
					global.tempCity=0;
					break;
				}
				delay(DELAYMS);
			}
		}
		world.availableClear();

		msg2 = "";
		control.updateButtonSet(temp);
		global.targetCity=global.tempCity;
		return global.targetCity;
	}


	public Point getTargetMove(String mode){
		String temp = control.buttonMode;
		control.updateButtonChoice("",0);

		battle.clearCanMove();

		battle.choosingMove=true;
		if (mode=="Move"){
			battle.fillMove();
		}	

		msg2 = "Where to Move?";

		mouseCancel = false;

		battle.targetPoint=null;
		Point k = battle.targetPoint;
		while(battle.isOkToMove(k)==false){ 
			delay(DELAYMS);
			k = battle.targetPoint;
			if (mouseCancel==true) {
				k=null;
				break;
			}
		}

		msg2 = "";
		control.updateButtonSet(temp);
		battle.clearCanMove();
		battle.choosingMove=false;
		return k;
	}

	public int gettargetTroop(String mode){
		String temp = control.buttonMode;
		control.updateButtonChoice("",0);


		battle.clear();

		int choicenum = 0;
		if (mode=="Attack"){
			for(Integer k : battle.getCanAttack(battle.currentPlayer)){
				battle.getTroop(k).isAvailable=true;
				choicenum++;				
			}
		}	
		else if (mode=="Select"){
			msg2 = "Choose:";
			for(int i = 1; i<=5;i++){
				if (battle.troop[battle.currentPlayer][i].moved==false&&battle.troop[battle.currentPlayer][i].num>0){
					battle.troop[battle.currentPlayer][i].isAvailable=true;
					choicenum++;
				}
			}
		}	
		else{
			for(int i = 1; i<=10;i++){
				battle.getTroop(i).isAvailable=true;
				choicenum++;
			}
		}	

		msg2 = "Which Troop?";
		battle.targetTroop = 0;
		mouseCancel = false;
		int k = battle.targetTroop;
		while(battle.targetTroop<=0||battle.getTroop(k).isAvailable==false){ 
			delay(DELAYMS);
			k = battle.targetTroop;
			if (mouseCancel==true) {
				battle.targetTroop=0;
				break;
			}
		}

		msg2 = "";
		control.updateButtonSet(temp);
		int tempchoice = battle.targetTroop;
		battle.targetTroop=0;
		battle.clear();
		return tempchoice;

	}

	public int gettargetTroop(){
		String temp = control.buttonMode;
		control.updateButtonChoice("",0);

		msg2 = "Which Troop?";
		battle.targetTroop = 0;
		int k = battle.targetTroop;
		mouseCancel = false;
		while(battle.targetTroop<=0||battle.getTroop(k).isAvailable==false){ 
			delay(DELAYMS);
			k = battle.targetTroop;
			if (mouseCancel==true) {
				battle.targetTroop=0;
				break;
			}
		}

		msg2 = "";
		control.updateButtonSet(temp);
		int tempchoice = battle.targetTroop;
		battle.targetTroop=0;
		return tempchoice;
	}

	public int getcurrentChoice(){
		msg2 = "Please choose:";
		global.currentChoice=0;
		mouseCancel = false;
		while(global.currentChoice<=0){
			delay(DELAYMS);
			if (mouseCancel==true) {
				global.currentChoice=0;
				break;
			}
		}
		msg2="";
		return global.currentChoice;
	}

	public int getChoice(int num){
		String stemp = state;
		String temp = control.buttonMode;

		choiceNum = num;
		state = "Choice";

		control.updateButtonChoice(state,num);

		msg2 = "Please choose:";
		global.currentChoice=0;
		mouseCancel = false;
		while(global.currentChoice<=0){
			delay(DELAYMS);
			if (mouseCancel==true) {
				global.currentChoice=0;
				break;
			}
		}
		msg2 = "";
		control.updateButtonSet(temp);
		state=stemp;
		return global.currentChoice;
	}

	public static void delay(long time){
		long l1 = System.currentTimeMillis();
		long lnow = System.currentTimeMillis();
		while(lnow-l1<time){
			lnow = System.currentTimeMillis();
		}
	}

	public void update(){
		if (global.state=="World"||global.state=="Edit"||global.state=="Menu"){
			world.refresh();
		}
		else if (global.state=="Battle"){
			battle.refresh();
		}
	}	

	public void draw(Graphics g, JFrame tt){
		drawBackGround(g,tt);
		if (global.state=="Menu"){
			g.setColor(Color.BLACK);
			g.drawString(msg,locateX(1-OFFSET), locateY(5) );
			g.drawImage(img[201],locateX(0), locateY(0), WIDTH*(X), HEIGHT*Y,tt);
			g.setColor(Color.BLACK);
			//			g.drawImage(img[319],locateX(0), locateY(0), WIDTH*(X), HEIGHT*Y,tt);
		}
		else if (global.state=="End"){
			g.drawImage(img[202],locateX(0), locateY(0), WIDTH*X, HEIGHT*Y,tt);
		}
		else if (global.state=="World"||global.state=="Edit"){
			drawWorld(g,tt);
		}
		else if (global.state=="Battle"){
			drawBattle(g,tt);
		}
		drawControlInfo(g,tt);
		if (showMsg){
			drawMsg(g,tt);
		}
	}

	public void drawBackGround(Graphics g, JFrame tt){
		Color c=g.getColor();
		g.setColor(Color.GRAY);
		//		g.fillRect(0, 0, WIDTH*X, HEIGHT*Y);
		g.drawImage(img[315],0, 0, WIDTH*OFFSET, HEIGHT*(Y+1),tt);
		g.setColor(c);
	}

	public void drawMsg(Graphics g, JFrame tt){
		Color c=g.getColor();

		g.drawImage(img[315],locateX(2), locateY(4), WIDTH*11, HEIGHT*5,tt);
		g.setFont(new Font("lucida",Font.ITALIC,getFontSize(25)));
		String tempStr1 =msgStr;
		String tempStr2 ="";
		if (msgStr.length()>30) {
			int ind = 25;
			for(int i = 25;i<msgStr.length();i++){
				if (msgStr.charAt(i)==' ') {
					ind = i;
					break;
				}
			}
			tempStr2=msgStr.substring(ind);
			tempStr1=msgStr.substring(0,ind);
		}

		g.drawString(tempStr1,locateX(3), locateY(5));
		g.drawString(tempStr2,locateX(3), locateY(6));

		g.setFont(new Font("lucida",Font.ITALIC,getFontSize(12)));
		g.drawString("Click to continue...",locateX(10), locateY(8));

		g.setColor(c);
	}

	public void drawControlInfo(Graphics g, JFrame tt){
		Color c=g.getColor();
		int i;
		int type;

		int ARC = WIDTH / 3;

		if (global.state=="World"||global.state=="Edit"){
			//Caption
			g.setColor(Color.black);

			g.setFont(new Font("Serif",Font.PLAIN,getFontSize(12)));
			g.drawString(msg,locateX(1-OFFSET), locateY(5) );
			g.drawString(msg1,locateX(1-OFFSET), locateY(6) );
			g.drawString(msg2,locateX(1-OFFSET), locateY(7) );

			if (global.currentCity!=0) g.drawImage(img[getCityImage(world.city[global.currentCity])],locateX(1-OFFSET)-20, locateY(1)+10, WIDTH*2, HEIGHT*2,tt);
			g.setFont(new Font("Serif",Font.BOLD,getFontSize(15)));
			g.drawString("Year: "+global.year,locateX(1-OFFSET), locateY(1)-5);
			g.drawString("Player: "+global.currentPlayer,locateX(3-OFFSET)-5, locateY(2)-10);
			g.drawString("City: "+global.currentCity,locateX(3-OFFSET)-5, locateY(3)-10);
			//			g.drawString("Army: "+world.city[global.currentCity].soldier,locateX(1-OFFSET), locateY(4) );



			g.setColor(global.playerColor[global.currentPlayer]);
			((Graphics2D)g).setStroke(new BasicStroke(strokeLen));

			//Border
			g.drawRoundRect(strokeLen, TOP+strokeLen, WIDTH*OFFSET-strokeLen, HEIGHT*Y-strokeLen,ARC,ARC);
		}
		else if (global.state=="Battle"){
			int dplayer = battle.p1;
			if (battle.currentPlayer==2) dplayer=battle.p2;
			//Caption
			g.setColor(Color.black);

			g.setFont(new Font("Serif",Font.PLAIN,getFontSize(15)));
			g.drawString(msg,locateX(1-OFFSET), locateY(5) );
			g.drawString(msg1,locateX(1-OFFSET), locateY(6) );
			g.drawString(msg2,locateX(1-OFFSET), locateY(7) );

			try{
				if (global.targetCity!=0) g.drawImage(img[getCityImage(world.city[global.targetCity])],locateX(1-OFFSET)-20, locateY(1)+10, WIDTH*2, HEIGHT*2,tt);
			}
			catch (Exception e){
				e.printStackTrace();
				System.out.println(global.targetCity);
				System.out.println(world.city[global.targetCity].belongTo+20);
			}
			g.setFont(new Font("Serif",Font.BOLD,getFontSize(15)));
			g.drawString("Year: "+global.year,locateX(1-OFFSET), locateY(1)-5);

			g.drawString("Player: "+dplayer,locateX(3-OFFSET)-5, locateY(2)-10);

			g.drawString("City: "+global.targetCity,locateX(3-OFFSET)-5, locateY(3)-10);
			g.drawString("Turn: "+battle.turn,locateX(1-OFFSET), locateY(4) );


			g.setColor(global.playerColor[dplayer]);
			((Graphics2D)g).setStroke(new BasicStroke(strokeLen));

			//Border
			//			g.drawRect(strokeLen, TOP+strokeLen, WIDTH*OFFSET-strokeLen, HEIGHT*Y-strokeLen);
			g.drawRoundRect(strokeLen, TOP+strokeLen, WIDTH*OFFSET-strokeLen, HEIGHT*Y-strokeLen,ARC,ARC);
		}

		statusMsg = global.currentPlayer+ ","+world.PlayerNum;

		//		g.drawString(statusMsg,locateX(1-OFFSET), locateY(15) );

		//Buttons

		for (i=1;i<=control.buttonNumber;i++){
			type = control.button[i].getElement();
			int x = control.button[i].getX();
			int y = control.button[i].getY();
			String s = control.button[i].name;

			Font tFont = g.getFont();
			if (control.button[i].onmap==true&&(curx!=0&&cury!=0)){
				x=curx+x-2;
				y=cury+y-CtrlBtn.BUTTONOFFSET;
				g.drawImage(img[type],locateX(x), locateY(y), WIDTH, HEIGHT,tt);
				g.setColor(Color.black);
				g.drawRoundRect(locateX(x),locateY(y),WIDTH,HEIGHT,ARC,ARC);

				g.setFont(new Font("Serif",Font.BOLD,getFontSize(13)));
				if (state=="Choice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x)+10, locateY(y)+25);
				}
				else if (state=="percentChoice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x)+5, locateY(y)+25);
				}
				else if (state=="okChoice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x)+8, locateY(y)+25);
				}



			}
			else if (control.button[i].onmap==false){
				g.setColor(Color.black);
				g.drawImage(img[type],locateX(x-OFFSET), locateY(y), WIDTH, HEIGHT,tt);
				g.drawRoundRect(locateX(x-OFFSET),locateY(y),WIDTH,HEIGHT,ARC,ARC);

				g.setFont(new Font("Serif",Font.BOLD,getFontSize(13)));
				if (state=="Choice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x-OFFSET)+10, locateY(y)+25);
				}
				else if (state=="percentChoice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x-OFFSET)+5, locateY(y)+25);
				}
				else if (state=="okChoice"){
					g.setColor(Color.black);
					g.drawString(s, locateX(x-OFFSET)+8, locateY(y)+25);

				}

			}
			g.setFont(tFont);

		}

		g.setColor(c);
	}

	public void drawWorld(Graphics g, JFrame tt){
		Color c=g.getColor();
		int i,j;
		int ii,jj;


		for (i=0; i<X; i++) for (j=0;j<Y;j++) {
			ii=nowX+i; jj=nowY+j;
			g.drawImage(img[7],locateX(i), locateY(j), WIDTH, HEIGHT,tt);
			g.setColor(Color.black);
		}


		for (i=0; i<X; i++) for (j=0;j<Y;j++) {
			ii=nowX+i; jj=nowY+j;
			int currentElement = world.getElement(ii,jj);
			g.drawImage(img[currentElement],locateX(i), locateY(j), WIDTH, HEIGHT,tt);
			if (world.getType(currentElement).equals("unit")){
				g.drawImage(img[7],locateX(i), locateY(j), WIDTH, HEIGHT,tt);
			}
			g.setColor(Color.black);
		}

		int ARC = WIDTH / 3;
		for (i=1;i<=world.CityNum;i++){
			int x = world.city[i].x;
			int y = world.city[i].y;
			g.drawImage(img[getCityImage(world.city[i])],locateX(x),locateY(y),WIDTH,HEIGHT,tt);
			if (global.currentCity==i){
				g.setColor(Color.ORANGE);
				g.drawRoundRect(locateX(x),locateY(y),WIDTH,HEIGHT,ARC,ARC);
				//				g.drawRect(locateX(x),locateY(y),WIDTH-1,HEIGHT-1);
			}
			if (world.city[i].isAvailable){
				g.setColor(Color.RED);
				//				g.drawRect(locateX(x),locateY(y),WIDTH-1,HEIGHT-1);
				g.drawRoundRect(locateX(x),locateY(y),WIDTH,HEIGHT,ARC,ARC);
			}
			if(global.targetCity==i){
				g.setColor(Color.GRAY);
				//				g.drawRect(locateX(x)+1,locateY(y)+1,WIDTH-3,HEIGHT-3);
				g.drawRoundRect(locateX(x),locateY(y),WIDTH,HEIGHT,ARC,ARC);
			}
			g.setColor(Color.white);

			g.setFont(new Font("Serif",Font.PLAIN,getFontSize(12)));
			String cityinfo = world.city[i].soldier+"";
			if (world.city[i].waitAdd>0){
				cityinfo+="("+world.city[i].waitAdd+")";
			}
			g.drawString(cityinfo, locateX(x),locateY(y)+10);
			g.setFont(new Font("Serif",Font.BOLD,getFontSize(15)));
			g.drawString(i+"", locateX(x),locateY(y+1));
			if (world.city[i].moved==true) g.drawString("E",locateX(x)+25, locateY(y+1));
		}
		g.setColor(c);
	}

	public int getFontSize(int original){
		return (int)(original*1.0*15/X);
	}

	public void drawBattle(Graphics g, JFrame tt){
		Color c=g.getColor();
		int i,j;
		int ii,jj;
		int border = 3;

		int ARC = WIDTH / 3;

		g.drawImage(img[318],locateX(0), 0, WIDTH*X, HEIGHT*(Y-border+1),tt);
		g.drawImage(img[319],locateX(0), locateY(Y-border), WIDTH*X, HEIGHT*(border+1),tt);


		g.setColor(Color.black);
		g.setFont(new Font("Lucida",Font.BOLD,getFontSize(30)));
		g.drawString(battle.battleMsg,locateX(1),locateY(Y-border+1));


		for (i=0; i<X; i++) for (j=border;j<Y-border;j++) {
			ii=nowX+i; jj=nowY+j;
			g.drawImage(img[battle.getElement(ii,jj)],locateX(i), locateY(j), WIDTH, HEIGHT,tt);
			//					g.drawRect(locateX(i), locateY(j), WIDTH, HEIGHT);
		}

		g.setFont(new Font("Serif",Font.BOLD,getFontSize(15)));

		double scale = battle.scale;
		int pw = (int)(WIDTH*scale);
		int ph = (int)(HEIGHT*scale);
		for (i = 1; i<=10; i++){
			if (battle.getTroop(i).num==0) continue;
			int x = battle.getTroop(i).x;
			int y = battle.getTroop(i).y;

			
			g.drawImage(img[battle.getTroop(i).element],locateX(x), locateY(y), pw, ph,tt);

			if (battle.currentTroop==i){
				g.setColor(Color.PINK);
				//				g.drawRect(locateX(x)-strokeLen,locateY(y)-strokeLen,pw+strokeLen*2,ph+strokeLen*2);
				g.drawRoundRect(locateX(x),locateY(y),pw,ph,ARC,ARC);
			}
			if (battle.targetTroop==i){
				g.setColor(Color.ORANGE);
				g.drawRoundRect(locateX(x)-strokeLen,locateY(y)-strokeLen,pw+strokeLen*2,ph+strokeLen*2,ARC,ARC);
			}
			if (battle.getTroop(i).isAvailable){
				g.setColor(Color.RED);
				g.drawRoundRect(locateX(x),locateY(y),pw,ph,ARC,ARC);
			}
			if (battle.getTroop(i).defend==true){
				g.setColor(Color.BLACK);
				g.drawRoundRect(locateX(x)-strokeLen,locateY(y)-strokeLen,strokeLen*2,strokeLen*2,ARC,ARC);
				g.setColor(Color.GRAY);
				g.fillRect(locateX(x)-strokeLen,locateY(y)-strokeLen,strokeLen*2,strokeLen*2);
			}
			if (i==1||i==6){
				g.setColor(Color.BLACK);
				g.drawString("A",locateX(x)-strokeLen,locateY(y)-strokeLen);
//				g.fillRect(locateX(x)-strokeLen,locateY(y)-strokeLen,strokeLen*2,strokeLen*2);
			}
			if (battle.getTroop(i).moved==true){
				g.setColor(Color.BLACK);
				g.drawString("E",locateX(x)+25, locateY(y+1));
			}
			
			//edit
			g.setColor(Color.white);
			g.drawString(battle.getTroop(i).num+"",locateX(x), locateY(y+1));

		}

		//spread the shadow of move

		if (battle.choosingMove){
			for (i=0; i<X; i++) for (j=border;j<Y-border;j++) {
				int x=nowX+i; int y=nowY+j;
				if (!battle.canMove[x][y]){
					g.drawImage(img[320],locateX(x), locateY(y), WIDTH, HEIGHT,tt);
				}
				//					g.drawRect(locateX(i), locateY(j), WIDTH, HEIGHT);
			}
		}
		g.setColor(c);
	}


	public void reloadSize(Dimension d){
		WIDTH=d.width/(X+OFFSET);
		TOP=26;
		HEIGHT=(d.height-TOP)/(Y);
		//		HEIGHT=d.height/Y;
		LEFT=(OFFSET)*WIDTH;
		CLEFT=0;
		RIGHT=LEFT+X*WIDTH; BOTTOM=TOP+HEIGHT*Y;
	}


	public int getNowX(){
		return nowX;
	}

	public int getNowY(){
		return nowY;
	}

	public void setNowXY(int newX, int newY, World m){
		if ((newX>=0)&&(newY>=0)&&(m.getX()>=newX+X)&&(m.getY()>=newY+Y)){
			nowX=newX;
			nowY=newY;
		}
		/*
		if ((newX>=1)&&(newY>=1)&&(m.getX()>=newX+X)&&(m.getY()>=newY+Y)){
			nowX=newX;
			nowY=newY;
		}
		 */
	}


	public int locateX(int x){
		int realX;
		//realX=LEFT+WIDTH*(x-1)-nowX;
		realX=LEFT+WIDTH*(x);
		return (realX);
	}

	public int locateY(int y){
		int realY;
		//realY=TOP+HEIGHT*(y-1)-nowY;
		realY=TOP+HEIGHT*(y);

		return (realY);
	}

	public int findX(int x){
		return ((x-LEFT)/WIDTH);
	}

	public int findY(int y){
		return ((y-TOP)/HEIGHT);
	}

	public int getShowX(){
		return X;
	}
	public int getShowY(){
		return Y;
	}
	
	public static int getCityImage(City c){
		int belong = c.belongTo;
		int level  = c.level;
		return (belong*10+20+level+6);
	}

	public boolean isInRange(int mx, int my){
		if ((LEFT<=mx)&&(RIGHT>=mx)&&(TOP<=my)&&(BOTTOM>=my)) 
			return true;
		else
			return false;
	}
	public boolean isInControl(int mx, int my){
		if ((CLEFT<=mx)&&(LEFT>=mx)&&(TOP<=my)&&(BOTTOM>=my)) 
			return true;
		else
			return false;
	}


	public void handleClick(int mx, int my, int button){
		//Normal Game
		if (button==3){
			mouseCancel = true;
		}

		if (showMsg) showMsg =false;
		else{
			if (isInControl(mx,my)){
				int x =findX(mx)+OFFSET-1;
				int y =findY(my);
				//click on the somewhere on control board
				try {
					control.select(x,y,world);
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			//In the world
			if (!isInRange(mx, my)) return;
			int x =findX(mx)+getNowX();
			int y =findY(my)+getNowY();

			int nx=x-curx+2;
			int ny=y-cury+CtrlBtn.BUTTONOFFSET;
			try {
				control.selectOnMap(nx, ny, world);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if (global.state=="Edit"){
				world.changeType(x, y, control.getTempType());

			}
			else if(global.state=="World"){
				world.selectCity(x, y, button);
			}
			else if(global.state=="Battle"){
				battle.selectTroop(x, y, button);
				battle.targetPoint=new Point(x,y);
			}

		}
	}


	public void processImage(){
		Image temp = img[2];
		temp.getGraphics();

	}

	public void initialImage(){
		img[7] = new ImageIcon("pics/grass2.jpg").getImage();
		img[8] = new ImageIcon("pics/hill.jpg").getImage();
		img[9] = new ImageIcon("pics/water.jpg").getImage();
		img[10] = new ImageIcon("pics/tree.jpg").getImage();
		img[11] = new ImageIcon("pics/castleWall.jpg").getImage();
		img[12] = new ImageIcon("pics/bridge.jpg").getImage();


		ImageMaker IM = new ImageMaker();

		//		for(int i = 1; i<=6;i++) img[i] = new ImageIcon("pics/road"+i+".JPG").getImage();

		Image imgRoad0 = new ImageIcon("pics/road0.JPG").getImage();
		Image imgRoadL = new ImageIcon("pics/roadL.JPG").getImage();

		img[1]=IM.getAlpha(imgRoad0);
		img[2]=IM.getRotate(img[1]);
		img[3]=IM.getAlpha(imgRoadL);
		img[4]=IM.getRotate(img[3]);
		img[5]=IM.getRotate(img[4]);	
		img[6]=IM.getRotate(img[5]);	

		Image imgCity1 = new ImageIcon("pics/city1.gif").getImage();
		Image imgCity2 = new ImageIcon("pics/city2.gif").getImage();
		Image imgCity3 = new ImageIcon("pics/city3.gif").getImage();

		Image imgS = new ImageIcon("pics/soldier.jpg").getImage();
		Image imgP = new ImageIcon("pics/pike.jpg").getImage();
		Image imgH = new ImageIcon("pics/horse.jpg").getImage();

		//30 p1
		//40 p2
		//50 p3
		//60 p4
		for(int i = 1; i<=4;i++){
			int k = (i+2)*10;
			Color c = global.playerColor[i];
			img[k+1] = IM.transfer(imgS, c, 1);
			img[k+2] = IM.transfer(imgP, c, 1);
			img[k+3] = IM.transfer(imgH, c, 1);
			img[k+4] = IM.transfer(imgS, c, 0);
			img[k+5] = IM.transfer(imgP, c, 0);
			img[k+6] = IM.transfer(imgH, c, 0);

			img[k+7] = IM.transfer(imgCity1, c, 0);
			img[k+8] = IM.transfer(imgCity2, c, 0);
			img[k+9] = IM.transfer(imgCity3, c, 0);
		}

		img[201] = new ImageIcon("pics/startMenu.jpg").getImage();
		img[202] = new ImageIcon("pics/endGame.jpg").getImage();

		img[301] = new ImageIcon("pics/savemap.jpg").getImage();
		img[302] = new ImageIcon("pics/loadmap.jpg").getImage();
		img[303] = new ImageIcon("pics/resize.jpg").getImage();
		img[304] = new ImageIcon("pics/attack.jpg").getImage();
		img[305]= new ImageIcon("pics/move.jpg").getImage();
		img[306]= new ImageIcon("pics/recruit.jpg").getImage();
		img[307] = new ImageIcon("pics/saveworld.jpg").getImage();
		img[308] = new ImageIcon("pics/loadworld.jpg").getImage();
		img[309] = new ImageIcon("pics/exit.jpg").getImage();

		img[310] = new ImageIcon("pics/choice.jpg").getImage();
		img[311] = new ImageIcon("pics/endturn.jpg").getImage();
		img[312] = new ImageIcon("pics/upgradeCity.gif").getImage();

		img[313] = new ImageIcon("pics/defend.jpg").getImage();
		img[314] = new ImageIcon("pics/retreat.jpg").getImage();

		img[315] = new ImageIcon("pics/paper.jpg").getImage();
		img[316] = new ImageIcon("pics/controlBoard.jpg").getImage();
		img[317] = new ImageIcon("pics/battleGround.jpg").getImage();
		img[318] = new ImageIcon("pics/battleBackGround.jpg").getImage();

		img[319] = new ImageIcon("pics/battleMsgBackground.jpg").getImage();

		Image imgFog = new ImageIcon("pics/fog.jpg").getImage();
		img[320] = IM.getAlpha(imgFog);

	}

}

