package main;

import javax.swing.*;

import common.global;


import java.awt.*;


import java.awt.event.*;
import java.io.IOException;

import elements.*;
import ControlInfo.*;
//import common.global.*;


public class ToriEditor extends JFrame
{
	/**
	 * 
	 */
	
	String mode = "Edit";
	
	
	private static final long serialVersionUID = 1L;
	private boolean fullScreen=false;
	private boolean initial=true;
	private boolean fps=true;
	
	private int width=800;
	private int height=630;
	
	private String titleName = "MapEditor";
	
	private screen scr =new screen("Edit");
	
	
//	private boolean needMove = false;
	private int mouseX,mouseY;
    //Double Buffer
    Image bufferImage;
    Graphics offgx;
     
	//initiate ToriWar
	public ToriEditor(){
		global.state=mode;
		
    	//When mouse moved, check the mouse position 
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				int mx=e.getX();
				int my=e.getY();
				mouseX= mx;
				mouseY=my;
				if (!scr.isInRange(mx, my)) return;
				
			}
        });
		
		//When mouse clicked, do sth.
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
               scr.handleClick(e.getX(),e.getY(),e.getButton());
            }
        });
	}
	//End of tankwar initiation
	

	
	// Show the whole screen, make it fullscreen;
	public void showFrame() {
			
		if (fullScreen){
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
			this.setBackground(Color.BLACK);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			//this.setUndecorated(true);  
			this.setVisible(true);
			
			//Dimension screenSize = new Dimension(800,600);
			Dimension   screenSize   =   Toolkit.getDefaultToolkit().getScreenSize();
			this.setBounds(0,0,screenSize.width,screenSize.height);
			scr.reloadSize(screenSize);
			//new Thread(new RepaintThread()).start();
			repaint();
		}
		else{
			//this.show();
			//Dimension   screenSize   =   Toolkit.getDefaultToolkit().getScreenSize(); 
			this.setBounds(0,0,800,600);
			Dimension screenSize = new Dimension(800,600);
			scr.reloadSize(screenSize);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);

			this.setBackground(Color.WHITE);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
		    			
			this.setResizable(false);//					 
			this.setTitle(titleName);
			this.setLocation(100,100);
			this.setVisible(true);
			
			repaint();
			
		}
		new Thread(new RepaintThread()).start();
	}
	
	
	//Show paint thing
	//Double buffer implement, firstly draw the things on offgx, which is the graphics of bufferImage, then draw bufferimage on to screen
	public void drawBuffer(){
		super.paint(offgx);
		if (initial) {
//			scr.drawScreen(offgx);
			
			initial=false;
		}

		scr.draw(offgx, this);
		
	}
	
	public void paint(Graphics g) {
		try	{
			g.setColor(Color.WHITE);
			if (bufferImage!=null){
				drawBuffer();
				g.drawImage(bufferImage,0,0,this);
			}
			else
			{			
				Dimension d = getSize();
				bufferImage = createImage(d.width,d.height);
				offgx = bufferImage.getGraphics();
				drawBuffer();
			}
		}
		catch(Exception e)	{
			e.printStackTrace();
		} 
	}

	//Repaint thread
	private class RepaintThread implements Runnable	{
		public void run()	{
			long second = 0L;
			long second2 = 0L;
			long second3 = 0L;
			int moveCount = 0;
			while(true)	{
				long start = System.currentTimeMillis();
				repaint();
				long end = System.currentTimeMillis();
				long time = end - start;
				long sleepTime = 100L - time;
				if (sleepTime < 0L)
					sleepTime = 0L;
				try   {
					Thread.sleep(sleepTime);
				}
				catch(Exception e)   {
					e.printStackTrace();
				}
				//Time counter, different from refresh paint;
				if (true) {
					moveCount++;
					second += System.currentTimeMillis() - start;
					//do per second
					if (second >= 1000L) {
						if (fps) resetTitle(moveCount);
						moveCount = 0;
						second = 0L;
					}
					//do per 0.1second
					second2 += System.currentTimeMillis() - start;
					if (second2>=200L){
						//for (int i=1;i<=tankNum;i++) t[i].go();
						scr.update();
						second2 = 0L;
					}
					second3 += System.currentTimeMillis() - start;
					if (second3>=500L){
						//for (int i=1;i<=tankNum;i++) t[i].go();
						
//						moveFrame(mouseX,mouseY);
						second3 = 0L;
					}
				}
			}
		}
//		public void moveFrame(int mx,int my){
//			if (needMove) {
//				if (scr.findX(mx)<=1) { scr.setNowXY(scr.getNowX()-1, scr.getNowY(), world); }
//				if (scr.findX(mx)>=scr.getShowX()) { scr.setNowXY(scr.getNowX()+1, scr.getNowY(), world); }
//				if (scr.findY(my)<=1) { scr.setNowXY(scr.getNowX(), scr.getNowY()-1, world); }
//				if (scr.findY(my)>=scr.getShowY()) { scr.setNowXY(scr.getNowX(), scr.getNowY()+1, world); }
//			}
//		}
	}
	
	public void resetTitle(int moveCount){
		this.setTitle(new StringBuilder(titleName).append(" FPS:")
				.append(moveCount).toString());
		return;
	}

	
	public static void main(String[]args)	{
		
		ToriEditor tk=new ToriEditor();
		Container c=tk.getContentPane(); 
		c.setBackground(Color.BLACK);
		tk.showFrame();
		
   }
}





















