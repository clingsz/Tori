package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import elements.*;
import ControlInfo.*;



public class ToriWar extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean fullScreen=false;
	private boolean initial=true;
	private boolean fps=true;
	
	private String titleName = "ToriWar";
	

	private screen scr =new screen("Normal");
	private boolean needMove = false;
	private int mouseX,mouseY;

	private int width = 800;
	private int height = 630;
	
    //Double Buffer
//    Image bufferImage;
//    Graphics offgx;
	
    Image bufferImage;
    Graphics offgx;
	
     
	//initiate ToriWar
	public ToriWar(){

	
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
			this.setBounds(0,0,width,height);
			Dimension screenSize = new Dimension(width,height);
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
		Thread game = new Thread(new RepaintThread());
		game.start();
		
		scr.mainGame();
		System.exit(1);
		
		
	}
	
	//Show paint thing
	//Double buffer implement, firstly draw the things on offgx, which is the graphics of bufferImage, then draw bufferimage on to screen
	
	public void drawBuffer(){
		super.paint(offgx);
		if (initial) {
			initial=false;
		}
		scr.draw(offgx, this);
	
	}
	
	public void paint(Graphics g) {
		try	{
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
						repaint();
						scr.update();
						second2 = 0L;
					}
					second3 += System.currentTimeMillis() - start;
					if (second3>=500L){
						//for (int i=1;i<=tankNum;i++) t[i].go();
						//moveFrame(mouseX,mouseY);
						second3 = 0L;
					}
				}
			}
		}

	}
	
	public void resetTitle(int moveCount){
		this.setTitle(new StringBuilder(titleName).append(" FPS:")
				.append(moveCount).toString());
		return;
	}

	
	public static void main(String[]args)	{
		ToriWar tk=new ToriWar();
		Container c=tk.getContentPane(); 
		c.setBackground(Color.BLACK);
		tk.showFrame();
		
   }
}





















