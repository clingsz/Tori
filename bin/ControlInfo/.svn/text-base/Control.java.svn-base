package ControlInfo;



import elements.*;


import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

import common.global;



//import java.awt.*;
//import java.awt.event.*;

public class Control {

	private int element[][] = new int[200][200];
	//private int X,Y;
	public CtrlBtn button[] = new CtrlBtn[100];
	public int buttonNumber;
	private int tempType=1;

	public String buttonMode="";
	/*
	 * Element 1-19 Map Type
	 * Element 21-30 Player1 Unit
	 * Element 31-40 Player2 Unit
	 * Element 41-50 Player3 Unit
	 * 
	 * Element 301-350 Control Type
	 */


	public Control(String newMode){
		buttonMode = newMode;
		updateButtonSet(buttonMode);
	}
	public int getTempType(){
		return tempType;
	}

	public int getElement(int x,int y){
		return element[x][y];
	}
	public void select(int x,int y,World world) throws IOException{
		for(int i=1;i<=buttonNumber;i++){
			if (button[i].onmap==false&&(button[i].getX()==x)&&(button[i].getY()==y)){
				work(button[i],world);
			}
		}
	}
	
	public void selectOnMap(int x,int y,World world) throws IOException{
		for(int i=1;i<=buttonNumber;i++){
			if (button[i].onmap==true&&(button[i].getX()==x)&&(button[i].getY()==y)){
				work(button[i],world);
			}
		}
	}
	
	public void work(CtrlBtn button,World world) {
		String function = button.getFunction();
		if (function=="SaveMap"){
			saveMap(world);
		}
		else if (function=="LoadMap"){
			loadMap(world);
		}
		else if (function=="ResizeMap"){
			world.clear();
		}
		else if (function=="LoadWorld"){
			if (global.state=="Edit"){
				loadWorld(world);
			}
			else if(global.state=="World"){
				global.currentChoice=button.buttonNo;
			}
		}
		else if (function=="SaveWorld"){
			if (global.state=="Edit"){
				saveWorld(world);
			}
			else if(global.state=="World"){
				global.currentChoice=button.buttonNo;
			}
			
		}
		else if (function=="Exit"){
			System.exit(0);
		}
		else if (function=="change"){
			tempType=element[button.getX()][button.getY()];
		}
		else if (function=="Choice"){
			global.currentChoice=button.buttonNo;
		}
		else{
			System.out.println("Haven't build function for "+function);
		}
	}

	public void saveMap(World world){

		JFileChooser dlg = new JFileChooser(".");
		dlg.setDialogTitle("Save MAP file");
		int result = dlg.showSaveDialog(null);  
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = dlg.getSelectedFile();
			world.m.saveFile(file);
		}
		else {

		}

	}

	public void saveWorld(World world){

		JFileChooser dlg = new JFileChooser(".");
		dlg.setDialogTitle("Save World file");
		int result = dlg.showSaveDialog(null);  
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = dlg.getSelectedFile();
			world.saveWorld(file);
		}
		else {
			System.out.println("Cancelled!");
		}

	}

	public void loadMap(World world){
		JFileChooser file = new JFileChooser (".");
		file.setAcceptAllFileFilterUsed(false);
		file.addChoosableFileFilter(new MapFileFilter("map"));
		int result = file.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			//String path = file.getSelectedFile().getAbsolutePath();
			File openfile = file.getSelectedFile();
			world.m.loadFile(openfile);
		}
		else
		{
			System.out.println("Cancelled!");
		}
	}


	public void loadWorld(World world){
		JFileChooser file = new JFileChooser (".");
		file.setAcceptAllFileFilterUsed(false);
		file.addChoosableFileFilter(new MapFileFilter("world"));
		int result = file.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			//String path = file.getSelectedFile().getAbsolutePath();
			File openfile = file.getSelectedFile();
			world.loadWorld(openfile);
		}
		else
		{
			System.out.println("Cancelled!");
		}
	}

	public void updateButtonSet(String mode){
		buttonMode = mode;
		if (mode=="Edit"){
			buttonNumber=30;
			button[1] = new CtrlBtn("Save",1,-3,"SaveMap",301);
			button[2] = new CtrlBtn("Load",2,-3,"LoadMap",302);
			button[3] = new CtrlBtn("Resize",3,-3,"ResizeMap",303);

			button[4] = new CtrlBtn("SaveWorld",1,-2,"SaveWorld",307);
			button[5] = new CtrlBtn("LoadWorld",2,-2,"LoadWorld",308);
			button[6] = new CtrlBtn("Exit",3,-2,"Exit",309);

			button[7] = new CtrlBtn("Grass",3,-1,"change",7);
			button[8] = new CtrlBtn("Hill",1,-1,"change",8);
			button[9] = new CtrlBtn("Water",2,-1,"change",9);
						
			button[10] = new CtrlBtn("Tree",1,0,"change",10);
			button[11] = new CtrlBtn("CastleWall",2,0,"change",11);
			button[12] = new CtrlBtn("Bridge",3,0,"change",12);

			
			button[13] = new CtrlBtn("City11",1,1,"change",37);
			button[14] = new CtrlBtn("City12",2,1,"change",38);
			button[15] = new CtrlBtn("City13",3,1,"change",39);
			
			button[16] = new CtrlBtn("City21",1,2,"change",47);
			button[17] = new CtrlBtn("City22",2,2,"change",48);
			button[18] = new CtrlBtn("City23",3,2,"change",49);
			
			button[19] = new CtrlBtn("City31",1,3,"change",57);
			button[20] = new CtrlBtn("City32",2,3,"change",58);
			button[21] = new CtrlBtn("City33",3,3,"change",59);
			
			button[22] = new CtrlBtn("City41",1,4,"change",67);
			button[23] = new CtrlBtn("City42",2,4,"change",68);
			button[24] = new CtrlBtn("City43",3,4,"change",69);
			
			
			button[25] = new CtrlBtn("road3",1,5,"change",3);
			button[26] = new CtrlBtn("road4",2,5,"change",4);
			button[27] = new CtrlBtn("road5",3,5,"change",5);
			
			button[28] = new CtrlBtn("road6",1,6,"change",6);
			button[29] = new CtrlBtn("road1",2,6,"change",1);
			button[30] = new CtrlBtn("road2",3,6,"change",2);
			

		}
		else if (mode=="World"){
			buttonNumber=8;
			button[1] = new CtrlBtn("Attack",1,1,"Choice",304);
			button[2] = new CtrlBtn("Move",2,1,"Choice",305);
			button[3] = new CtrlBtn("Recruit",3,1,"Choice",306);
//			for(int i = 1; i<=3; i++) button[i].onmap=true;
			
			button[4] = new CtrlBtn("EndTurn",1,2,"Choice",311);
			button[5] = new CtrlBtn("UpgradeCity",2,2,"Choice",312);
			
			button[6] = new CtrlBtn("SaveWorld",1,4,"SaveWorld",307);
			button[7] = new CtrlBtn("LoadWorld",2,4,"LoadWorld",308);
			button[8] = new CtrlBtn("Exit",3,4,"Exit",309);
		}
		else if (mode=="Battle"){
			buttonNumber=3;
//			button[1] = new CtrlBtn("Move",0,1,"Choice",305);
			button[1] = new CtrlBtn("Attack",1,1,"Choice",304);
			button[2] = new CtrlBtn("Defend",2,1,"Choice",313);
			button[3] = new CtrlBtn("Retreat",3,1,"Choice",314);
			for(int i = 1; i<=buttonNumber; i++) button[i].onmap=true;
		}
		else if (mode=="Choice"){
			buttonNumber=0;
		}
		else if (mode=="askOK"){
			buttonNumber=0;
		}
		else if (mode=="Select"){
			buttonNumber=2;
			button[1] = new CtrlBtn("EndTurn",1,1,"Choice",311);
			button[2] = new CtrlBtn("Look",2,1,"Choice",312);
		}
		else{
			buttonNumber = 0;
		}
		for (int i=1;i<=buttonNumber;i++){
			button[i].buttonNo=i;
			element[button[i].getX()][button[i].getY()]=button[i].getElement();
		}

	}
	public void updateButtonChoice(String state,int num){
		buttonNumber=num;

		if (state=="Choice"){
			for(int i = 1; i <= num; i++){
				button[i] = new CtrlBtn(i+"",(i-1)%3+1,(i-1)/3+1,"Choice",310);
			
			}
		}
		else if (state=="percentChoice"){
			buttonNumber=6;
			for(int i = 1; i <= num; i++){
				button[i] = new CtrlBtn(i+"",(i-1)%3+1,(i-1)/3+1,"Choice",310);
			
			}
			button[1].name="10%";
			button[2].name="25%";
			button[3].name="50%";
			button[4].name="75%";
			button[5].name="90%";
			button[6].name="100%";
		}	
		else if (state=="okChoice"){
			buttonNumber=2;
			button[1]=new CtrlBtn("YES",1,1,"Choice",310);
			button[2]=new CtrlBtn("NO",3,1,"Choice",310);
			
			
		}


		for (int i=1;i<=buttonNumber;i++){
			button[i].buttonNo=i;
//			button[i].onmap=true;
			element[button[i].getX()][button[i].getY()]=button[i].getElement();
		}
	}

}

class MapFileFilter extends FileFilter {
	String ext;
	MapFileFilter(String ext) {
		this.ext = ext;
	}
	public boolean accept(File file) {
		if (file.isDirectory())
		{
			return true;
		}
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1)
		{
			String extension = fileName.substring(index + 1).toLowerCase();
			if (extension.equals(ext))
				return true;
		}
		return false;
	}
	@Override
	public String getDescription() {
		if (ext.equals("map"))
		{
			return "Map files(*.map)";
		}
		return "";
	}
}