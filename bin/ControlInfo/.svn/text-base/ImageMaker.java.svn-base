package ControlInfo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ImageMaker extends JFrame{

	File _file = new File("pics/road0.jpg"); //read in file
	Image src = null;
	Image img = null;
	int width;   //get width
	int height; //get height

	int[][] srcA = new int[1000][1000];
	int[][] srcR = new int[1000][1000];
	int[][] srcG = new int[1000][1000];
	int[][] srcB = new int[1000][1000];
	
	int[][] tarY = new int[1000][1000];
	int[][] tarU = new int[1000][1000];
	int[][] tarV = new int[1000][1000];

	JFrame myFrame=new JFrame("img");

	public ImageMaker(){
//
//		try {
//			src = javax.imageio.ImageIO.read(_file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} //build Image object
//		width=src.getWidth(null);   //get width
//		height=src.getHeight(null); //get height
	}

	public Image getCurrentImage(){
		int w = width;
		int h = height;
		int pix[] = new int[w * h];
		int index = 0;
		for (int y = 0; y < h; y++) {
			
			for (int x = 0; x < w; x++) {
				int alpha = srcA[x][y];
				int red = srcR[x][y];
				int blue = srcB[x][y];
				int green = srcG[x][y];
				pix[index++] = (alpha << 24) | (red << 16) | (green << 8) | blue;
			}
		}
		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pix, 0, w));
		return img;
	}
	
	public void saveTempImage(){
		int w = width;
		int h = height;
		int pix[] = new int[w * h];
		int index = 0;
		for (int y = 0; y < h; y++) {
			
			for (int x = 0; x < w; x++) {
				int alpha = srcA[x][y];
				int red = srcR[x][y];
				int blue = srcB[x][y];
				int green = srcG[x][y];
				pix[index++] = (alpha << 24) | (red << 16) | (green << 8) | blue;
			}
		}
		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pix, 0, w));
		
//		BufferedImage bfimg = toBufferedImage(img);
//		save(bfimg,"jpg");
	}

	private static BufferedImage toBufferedImage(Image src) {
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB;  // other options
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}

	private static void save(BufferedImage image, String ext) {
		String fileName = "savingAnImage";
		File file = new File(fileName + "." + ext);
		try {
			ImageIO.write(image, ext, file);  // ignore returned boolean
			System.out.println("Write success");
		} catch(IOException e) {
			System.out.println("Write error for " + file.getPath() +
					": " + e.getMessage());
		}
	}


	public void handlesinglepixel(int x, int y, int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red   = (pixel >> 16) & 0xff;
		int green = (pixel >>  8) & 0xff;
		int blue  = (pixel      ) & 0xff;

		srcA[x][y] = alpha;
		srcR[x][y] = red;
		srcG[x][y] = green;
		srcB[x][y] = blue;

//		System.out.println(alpha);
		// Deal with the pixel as necessary...
	}

	public void loadPic(Image img) {
//		Image img = src;
		src = img;
		width=src.getWidth(null);   //get width
		height=src.getHeight(null); //get height
		int x = 0;
		int y = 0;
		int w = width;
		int h = height;
		int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
			return;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			System.err.println("image fetch aborted or errored");
			return;
		}
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				handlesinglepixel(x+i, y+j, pixels[j * w + i]);
			}
		}
	}


	public void handle(Color c){
		int h = height;
		int w = width;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int red = srcR[x][y];
				int blue = srcB[x][y];
				int green = srcG[x][y];
				if (blue<50&&green<50&&red>50){
					srcR[x][y]=c.getRed();
					srcB[x][y]=c.getBlue();
					srcG[x][y]=c.getGreen();
				}
				if (blue>200&&green>200&&red>200){
					srcA[x][y]=0;
					srcR[x][y]=250;
					srcB[x][y]=250;
					srcG[x][y]=250;
				}
			}
		}
	}
	
	public void flip(){
		int h = height;
		int w = width;
		
		int tA[][] = new int[2000][2000];
		int tR[][] = new int[2000][2000];
		int tG[][] = new int[2000][2000];
		int tB[][] = new int[2000][2000];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tA[w-x-1][y] = srcA[x][y];
				tR[w-x-1][y] = srcR[x][y];
				tB[w-x-1][y] = srcB[x][y];
				tG[w-x-1][y] = srcG[x][y];
			}
		}
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				srcA[x][y] = tA[x][y];
				srcR[x][y] = tR[x][y];
				srcG[x][y] = tG[x][y];
				srcB[x][y] = tB[x][y];
			}
		}
	}
	
	public void trans(){
		int h = height;
		int w = width;
		
		int tA[][] = new int[2000][2000];
		int tR[][] = new int[2000][2000];
		int tG[][] = new int[2000][2000];
		int tB[][] = new int[2000][2000];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				tA[y][x] = srcA[x][y];
				tR[y][x] = srcR[x][y];
				tB[y][x] = srcB[x][y];
				tG[y][x] = srcG[x][y];
			}
		}
		
		int nh = width;
		int nw = height;
		
		for (int y = 0; y < nh; y++) {
			for (int x = 0; x < nw; x++) {
				srcA[x][y] = tA[x][y];
				srcR[x][y] = tR[x][y];
				srcG[x][y] = tG[x][y];
				srcB[x][y] = tB[x][y];
			}
		}
		
		height = nh;
		width = nw;
	}
	
	public void makeAlpha(){
		int h = height;
		int w = width;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int red = srcR[x][y];
				int blue = srcB[x][y];
				int green = srcG[x][y];
				if (blue>200&&green>200&&red>200){
					srcA[x][y]=0;
					srcR[x][y]=250;
					srcB[x][y]=250;
					srcG[x][y]=250;
				}
			}
		}
	}
	
	public void rotate(){
		trans();
		flip();
	}
	
	public void initScreen(){
		this.setBackground(Color.GRAY);
		this.setVisible(true);
		this.setBounds(0,0,800,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		repaint();
	}
	
	public void paint(Graphics g){
		g.drawImage(img,50,50,400,400, myFrame);
	}
	
	public void doall(){
		Image tempimg = new ImageIcon("pics/roadL.jpg").getImage();
		loadPic(tempimg);
//		handle(Color.YELLOW);
//		flip();
		makeAlpha();
		saveTempImage();
		initScreen();		
	}
	
	// flip 0 is not 1 is  
	public Image transfer(Image tmpImg, Color c, int doflip){
		loadPic(tmpImg);
		handle(c);
		if (doflip==1) flip();
		return getCurrentImage();
	}
	
	public Image getAlpha(Image tmpImg){
		loadPic(tmpImg);
		makeAlpha();
		return getCurrentImage();
	}
	
	public Image getRotate(Image tmpImg){
		loadPic(tmpImg);
		rotate();
		return getCurrentImage();
	}
	
	public static void main(String[] args){
		ImageMaker cont = new ImageMaker();
		cont.doall();		
	}
}