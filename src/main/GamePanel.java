package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
	//use the Thread in this GamePanel and to run a Thread,need to implements Runnable
	//在这个GamePanel中使用这个线程，并且运行一个线程，需要在这个类中实现Runnable
	
	//screen settings//屏幕设置
	final int orginalTileSize=16;//16*16 title
	/*
	 * 此游戏中玩家角色、NPC和地图图块的默认尺寸
	 *16*16是许多复古2D游戏的标准尺寸，许多角色和图块都是用此尺寸制作的
	 *考虑到现代计算机屏幕尺寸，scale it
	*/
	final int scale=3;//16*3(scale)=48
	
	final int tileSize=orginalTileSize*scale;//48*48 title
	final int maxScreenCol=16;
	final int maxScreenRow=12;//经典4:3
	final int screenWidth=tileSize*maxScreenCol;//768 pixels
	final int screenHeight=tileSize*maxScreenRow;//576 pixels
	
	int FPS=60;
	
	KeyHandler keyH=new KeyHandler();
	//in 2D games,once you start the program,the program keeps running...
	//FPS 60=Updates the screen 60 times per second
	Thread gameThread;
	
	//set player's default position
	int playerX=100;
	int playerY=100;
	int playerSpeed=4;
	
	public GamePanel() {
		
		//set the size of this class(JPanel)//设置此GamePanel的面板大小
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setBackground(Color.black);
		//enabling this can improve game's rendering performance//提升游戏渲染性能
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);//with this,this GamePanel can be "focused" to receiver key input
		//按键按下-操作系统接受-判断当前输入法状态-英文模式：java程序，KeyListener/中文模式：输入法拦截...传给程序
		this.enableInputMethods(false);
	}

	public void startGameThread() {
		//instance the shape of thread
		
		//pass this to this thread constructor
		//创建一个新的线程对象，并将当前的GamePanel实例作为任务传递给这个线程
		gameThread=new Thread(this);//Thread(Runnable target)构造器
		gameThread.start();
		/*
		 *1. 调用 start()
		 *2. start() 告诉 JVM："请创建一个新的操作系统线程"
		 *3. JVM 创建新线程，并在这个新线程中自动调用 run()
		 *4. 新线程开始执行 run() 里的代码
		 */
	}

	//method 1 "SLEEP"
//	@Override
//	public void run() {
//		//Game Loop,which is the core of this game//游戏的核心：游戏循环
//		
//		double drawInterval=1000000000/FPS;//one billion nanoseconds...means one second//0.01666... seconds
//		double nextDrawTime=System.nanoTime()+drawInterval;//the allocated time for single loop is 0.01666 seconds
//		
//		while(gameThread!=null) {
//			
////			System.out.println("The game loop is running");//Testing
//			
//			long currentTime=System.nanoTime();
////			long currentTime=System.currentTimeMillis();
//			System.out.println(currentTime);//Testing
//			
//			// 1 UPDATE: update information such as character positions//更新
//			update();
//			
//			// 2 DRAW: draw the screen with  the updated information//重绘
//			repaint();
//			/*
//			 * repaint()
//   			 *	    ↓
//			 *	RepaintManager（Swing内部管理器）
//			 *	    ↓
//			 *	合并多个重绘请求（优化性能）
//			 *	    ↓
//			 *	在事件调度线程（EDT）上安排重绘任务
//			 *	    ↓
//		 	 *	调用 paint() 方法
//		 	 *	    ↓
//		 	 *	paint() 调用 paintComponent()
//			 */
//			
//			try {
//				double remainingTime=nextDrawTime-System.nanoTime();//let the thread sleep for the remainingTime
//				remainingTime=remainingTime/1000000;//1 second=1,000,000,000 nanoseconds=1,000 milliseconds
//				
//				if(remainingTime<0) {
//					remainingTime=0;//如果更新和重绘花费的时间超过drawInterval，这个线程不需要睡眠，因为已经使用了分配的时间//jast  in case
//				}
//				
//				Thread.sleep((long)remainingTime);//// Thread.sleep() 的声明//public static void sleep(long mills) throws InterruptedException
//				
//				nextDrawTime+=drawInterval;//increment
//				
//			} catch (InterruptedException e) {//当线程在 sleep() 期间被其他线程打断时，就会抛出这个异常
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	//method 2 "DELTA/ACCUMULATOR"
	public void run() {
		
		double drawInterval=1000000000/FPS;
		double delta=0;
		long lastTime=System.nanoTime();
		long currentTime;
		long timer=0;
		int drawCount=0;
		
		while(gameThread!=null) {
			
			currentTime=System.nanoTime();
			
			delta+=(currentTime-lastTime)/drawInterval;
			timer+=(currentTime-lastTime);
			lastTime=currentTime;
			
			if(delta>=1) {
				update();
				repaint(); 
				delta--;
				drawCount++;
			}
			
			if(timer>=1000000000) {
				System.out.println("FPS:"+drawCount);
				drawCount=0;
				timer=0;
			}
		}
	}
	public void update() {
		if(keyH.upPressed==true) {
			playerY-=playerSpeed;
		}
		else if(keyH.downPressed==true) {
			playerY+=playerSpeed;
		}
		else if(keyH.leftPressed==true) {
			playerX-=playerSpeed;
		}
		else if(keyH.rightPressed==true) {
			playerX+=playerSpeed;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
		
		g2.setColor(Color.white);
		
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		
		//Dispose of this graphics context and release any system resources that it is using
		g2.dispose();
	}
}
