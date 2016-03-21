package al.jamil.suvo.dxball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl .GL11.*;

public class Game {
	
	List<GameBody> Block=new ArrayList<GameBody>();
	GameBody ball,bat;
	int bdx,bdy;
	boolean run=true;

	Game(){
		
		setDisplay();
		setOpenGL();
		setData();
		while (!Display.isCloseRequested()){
			
			GL11.glClear(GL_COLOR_BUFFER_BIT);
			render();
			if (run){
				update();
				logic();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Display.destroy();
				System.exit(0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				if (Block.size()==0) {
					new Game();
				}
				run=true;
			}
			
			Display.update();
			Display.sync(60);
			new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			});
			
		}
		
	}
	

	private void setDisplay() {
		
		try {
			Display.setFullscreen(true);
			Display.setTitle("Suvo DX Ball");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
	}


	private void setOpenGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1200, 700, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
	}


	private void setData() {
		for(int i=0;i<10;i++){
			for (int j=0;j<10;j++){
				Block.add(new GameBody(i*80+80,j*16+16,15,78,1.0f,1.0f,1.0f));
			}
		}
		Random rand=new Random();
		int nr=rand.nextInt(50)+12;
		//System.out.println(nr);
		for (int i=0;i<nr;i++){
			int k=rand.nextInt(100-i);
			int j=0;
			//System.out.println(k);
			for (GameBody g:Block)
			{
				if (j==k) {Block.remove(g);break;}
				j++;
			}
		}
		ball=new GameBody(112,608-16,15,15,1.0f,0.0f,0.0f);
		bat=new GameBody(80,608,15,160,0.0f,1.0f,0.0f);
		bdx=8;
		bdy=-8;
	Mouse.setGrabbed(true);	
	}


	private void render() {
		ball.draw();
		bat.draw();
		for(GameBody _gamebody:Block){
			_gamebody.draw();
		}
		GL11.glColor3f(0.0f, 0.0f,1.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(1, 622);
			GL11.glVertex2i(880, 622);
			GL11.glVertex2i(880, 623);
			GL11.glVertex2i(1, 623);
			GL11.glVertex2i(78,1);
			GL11.glVertex2i(80,1);
			GL11.glVertex2i(80, 624);
			GL11.glVertex2i(78, 624);
		GL11.glEnd();
	}


	private void logic() {
		
		for(GameBody _gamebody:Block){
			if (_gamebody.intersect(ball.getX(), ball.getY())){
				
				Block.remove(_gamebody);
				bdx=-bdx;
				bdy=-bdy;
				break;
			}
		}
		if (ball.getY()==608){
			if  (ball.getX()>=bat.getX() && ball.getX()<=bat.getX()+160){
				if (ball.getX()<bat.getX()+80) 
					{
						int k=bat.getX()+80-ball.getX();
						bdx=-(int)(8*k/80);
						
						
					}
				else {
					int k=ball.getX()-bat.getX()-80;
					bdx=(int)(8*k/80);
					
				}
			}
			else {
				run=false;
			}
		}
		if (Block.size()==0) run=false;
		
	}


	private void update() {
		if (ball.getX()<80) bdx=-bdx;
		if (ball.getX()>880) bdx=-bdx;
		if (ball.getY()<16) bdy=-bdy;
		if (ball.getY()>=608) bdy=-bdy;
		ball.updateD(bdx, bdy);
		bat.updateX(Mouse.getDX());
	}


	public class GameBody implements Entity{

		protected int x,y,h,w;
		protected float cb,cg,cr;
		GameBody(int x,int y,int h,int w,float cr,float cg,float cb){
			this.x=x;
			this.y=y;
			this.h=h;
			this.w=w;
			this.cr=cr;
			this.cb=cb;
			this.cg=cg;
		}

		@Override
		public void draw() {
			GL11.glColor3f(cr, cg, cb);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2i(x, y);
				GL11.glVertex2i(x+w, y);
				GL11.glVertex2i(x+w, y+h);
				GL11.glVertex2i(x, y+h);
			GL11.glEnd();
		}

		@Override
		public void update(int x, int y) {
			this.x=x;
			this.y=y;
			
		}

		@Override
		public void updateD(int dx, int dy) {
			this.x+=dx;
			this.y+=dy;
		}

		@Override
		public void setX(int x) {
			this.x=x;
		}

		@Override
		public void setY(int y) {
			this.y=y;
		}

		@Override
		public void updateX(int dx) {
			this.x+=dx;
		}

		@Override
		public void updateY(int dy) {
			this.y+=dy;
		}

		public boolean intersect(int x, int y) {
			if (y==this.y){
				if ((x>=this.x && x<this.x+this.w+15)|| (x>=this.x-16 && x<this.x))
				{
					return true;
				}
				else return false;
			}
			else return false;
		}

		@Override
		public int getX() {
			return x;
		}

		@Override
		public int getY() {
			return y;
		}

	}
	public class Color3{
		public float r,g,b;
		Color3(float r,float g,float b)
		{
			this.r=r;
			this.g=g;
			this.b=b;
		}
	}
	public static void main(String[] args){
		new Game();
	}
}
