package elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Map{

	public int X=15,Y=15;
	private final int totalType=7;

	public int obstruct;
	public int[][] type = new int[40][40];
	
	
	
//	public int[][][][] shortestPath = new int[40][40][40][40];
	//next step: 0 stay; 1 right; 2 down; 3 left; 4 up; 
//	public int[][][][] nextStep = new int[40][40][40][40];

	public Map(String setting){
		if (setting=="Random"){
			obstruct=1;
			for(int i=0; i<X; i++) for(int j=0; j<Y; j++){
				int number = (int)(Math.random()*3)%(totalType-1)+1;
				type[i][j]=number;
			}


		}
		else if (setting=="Normal"){
			loadFile("1.map");
		}
		else if(setting=="Edit"){
			for(int i=0; i<X; i++) for(int j=0; j<Y; j++){
				int number = (int)(Math.random()*3)%(totalType-1)+1;
				type[i][j]=number;
			}
		}
		else if(setting=="Empty"){
			for(int i=0; i<X; i++) for(int j=0; j<Y; j++){
				int number = 7;
				type[i][j]=number;
			}
		}
	}

	public void loadMap(int x,int y,int[][] temp){
		
		X=x;
		Y=y;
		
		for(int i=0; i<X; i++) for(int j=0; j<Y; j++){
			int number = temp[i][j];
			type[i][j]=number;
		}

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

	public void saveFile(File file){
		FileWriter out = null;
		try {
			out = new FileWriter(file);

			out.write(String.valueOf(X));
			out.write(" ");

			out.write(String.valueOf(X));
			out.write(" ");
			for (int i=0;i<X;i++) {
				for (int j=0;j<Y;j++){

					out.write(String.valueOf(type[i][j]));
					out.write(" ");
				}
				out.write("\n");
			}
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

	public void loadFile(String filename){
		File openfile = new File(filename);
		loadFile(openfile);		   
	}

	public void changeType(int i,int j, int newType){
		type[i][j]=newType;
	}


	public void clear(){
		for(int i = 0;i<X; i++) for(int j= 0 ; j<Y;j++) type[i][j]=7;
	}
	public int[][] getInfo(){
		return type;
	}

	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
}