package com.github.brandonbai.game;

import com.github.brandonbai.game.MyButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * 经典模式的面板
 */
public class ClassicPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private MyButton backButton,restartButton;
	Random random = new Random();
	//状态
	private int state = 1;
	//返回按钮与重新开始按钮
	//控制2或4生成的数字
	private int APPEAR = 0;
	private int appear = 0;
	//用于存放方格上的数字的数组
	private int  [][] number ;
	//用来判断是否移动或变化的数组
	private int [][] NUMBER;
	//用于存放背景颜色的数组
	Color[][] colors;
	Cell cell ;
	/**舞台面板的宽高*/
	public static final int PANEL_WIDTH = 424;
	public static final int PANEL_HEIGHT = 424;
	/**小方格的宽高*/
	public static final int CELL_WIDTH = 100;
	public static final int CELL_HEIGHT = 100;
	//用于控制方向的数字
	public static final int UP = 1;
	public static final int DOWN =2;
	public static final int RIGHT =3;
	public static final int LEFT = 4;
	int dir = 0;
	//控制数字相加的数字
	public static final int UP_ADD=5;
	public static final int DOWN_ADD=6;
	public static final int LEFT_ADD=7;
	public static final int RIGHT_ADD=8;
	int number_add = 0;
	//当前分数
	private int score = 0;
	//最高分
	int mostScore;
	//number数组的分数的总和
	private int sums ;
	/**
	 * 构造方法
	 */
	public ClassicPanel() {
		
			Color backColor = new Color(255, 158, 90);
			
			backButton = new MyButton("返回", backColor);
			backButton.setBounds(310, 100, 110, 30);
			backButton.addActionListener(this);
			
			restartButton = new MyButton("重新开始", backColor);
			restartButton.setBounds(180, 100, 110, 30);
			restartButton.addActionListener(this);
			this.add(backButton);
			this.add(restartButton);
			number = new int[4][4];
			NUMBER = new int[4][4];
		
	}
	/**
	 * 画的主方法
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintScore(g);
		paintCellArea(g);
		if(state == 2){
			//画游戏结束时的画面
			paintGameOver(g);
		}
	}

	/**画方格区的背景*/
	public void paintCellArea(Graphics g) {
		
			//设置背景方格框颜色
			g.setColor(new Color(227, 179, 155));
			//画背景方格框
			g.fillRect(11, 138, PANEL_WIDTH, PANEL_HEIGHT);
			//设置背景小方格颜色
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					g.setColor(new Color(243,223,214));
					//画小方格背景
					if(number[i][j]!=0){
						cell= new Cell(number[i][j]);
						g.setColor(cell.getColor());
						g.fillRoundRect(15+i*105, 142+j*105, CELL_WIDTH, CELL_HEIGHT, 10, 10);
						String str  = "" + number[i][j];
						g.setColor(Color.BLACK);
						//数字大小
						g.setFont(new Font("华文新魏", Font.PLAIN, 30));
						int l = str.length();
						g.drawString(str, 15+i*105+(5-l)*10,142+j*105+60);
						
					}else{
						g.fillRoundRect(15+i*105, 142+j*105, CELL_WIDTH, CELL_HEIGHT, 10, 10);
					}
					
				}
			}
		
	}
	/**画分数等显示信息*/
	public void paintScore(Graphics g) {
		g.setColor(new Color(227, 179, 155));
		//画当前分数框
		g.fillRoundRect(180, 3, 110, 70, 11, 11);
		//画最高分框
		g.fillRoundRect(310, 3, 110, 70, 11, 11);
		g.setColor(new Color(239, 198, 0));
		//画图标
		g.fillRoundRect(25, 3, 130, 120, 12, 12);
		g.setColor(Color.white);
		g.setFont(new Font("微软雅黑",Font.PLAIN,20));
		g.drawString("当前分数", 195, 30);
		
		g.drawString(""+score, 200, 57);
		g.drawString("最高分", 335, 30);
		g.drawString(""+mostScore, 335, 57);
		g.setFont(new Font("Impact",Font.PLAIN,48));
		g.drawString("2048", 40, 50);
		g.setFont(new Font("Impact",Font.BOLD,36));
		g.drawString("4 * 4", 50, 90);
	}
	//画游戏结束的方法
	public void paintGameOver(Graphics g) {
		g.setColor(new Color(255, 255, 255, 150));
		g.fillRect(11, 138, PANEL_WIDTH, PANEL_HEIGHT);
		g.setColor(new Color(148, 117, 99));
		g.setFont(new Font("微软雅黑",Font.BOLD,59));
		g.drawString("Game Over !", 39, 320);
	}
	/**
	 * 监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton ) {
			try {
				this.saveScore();
				this.saveState();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			setState(0);
		}else if(e.getSource()==restartButton ) {
			state = 1;
			//一个为空的数组
			number = new int[4][4];
			APPEAR=0;
			appear=0;
			sums  = 0;
			try {
				saveState();
				saveScore();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			go();
			score=0;
			dir = 0;
			number_add =0;
		}
	}
	/**
	 * 运行游戏的主方法
	 */
	public void go(){
		try {
			readState();
			readScore();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.requestFocus();
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				APPEAR=0;
				if(e.getKeyCode()==37){
					dir= LEFT;
					number_add = LEFT_ADD;
				}
				if(e.getKeyCode()==38){
					dir = UP;
					number_add = UP_ADD;
				}
				if(e.getKeyCode()==39){
					dir = RIGHT;
					number_add = RIGHT_ADD;
				}
				if(e.getKeyCode()==40){
					dir = DOWN;
					number_add = DOWN_ADD;
				}
			}
		});
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				//生成2或者4
				appearAction();
				//将number赋值给NUMBER
				giveAction();
				//动一下
				stepAction();
				//判断是否发生变化
				judgeAction();
				try {
					updateTopScore();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//判断游戏是否结束
				gameOverAction();
				repaint();
			}
		}, 0, 20);
	}
	/**
	 * 随机生成2或4的方法
	 */
	public void appearAction(){
		int i = random.nextInt(4);
		int j = random.nextInt(4);	
		int a = random.nextInt(3);
		//判断是否会重叠
		if(APPEAR==0&&appear==0){
			if(this.isExit(i, j)==true){
				//如果a为0，生成4,其他的生成2
				if(a==0||a==1){
					number[i][j]=2;
				}else{
					number[i][j]=4;
					
				}
				APPEAR=1;
				appear=1;
			}
		}
		
	}
	/**
	 * 判断小方块是否在已经有方块的地方生产
	 */
	public boolean isExit(int i ,int j) {
		boolean IsExit = false;
		if(number[i][j]==0){
			IsExit = true;
		}
		return IsExit;
	}
	/**
	 * 赋值
	 */
	public void giveAction(){
		for(int i =0;i<4;i++){
			for(int j = 0;j<4;j++){
				NUMBER[i][j]=number[i][j];
			}
		}
	}
	/**
	 * 判断是否发生变化
	 */
	public void judgeAction(){
		for(int i = 0 ;i<4;i++){
			for(int j = 0 ; j<4 ;j++){
				if(NUMBER[i][j]!=number[i][j]){
					appear=0;
				}
			}
		}
	}
	/**
	 * 先移动 再相加  再移动
	 */
	//数字走一步
	public void step(){
		/**数字移动*/
		//上
		if(dir==UP){
			for(int i = 0;i<4;i++){
				for(int j =0;j<3;j++){
					if(number[i][j]==0){
						number[i][j]=number[i][j+1];
						number[i][j+1]=0;
					}
				}
			}
		}//下
		else if(dir == DOWN) {
			for(int i = 3;i>-1;i--){
				for(int j = 3;j>0;j--){
					if(number[i][j]==0){
						number[i][j]=number[i][j-1];
						number[i][j-1]=0;
					}
				}
			}
		}//左
		else if(dir ==LEFT){
			for(int i =0;i<3;i++){
				for(int j = 0;j<4;j++){
					if(number[i][j]==0){
						number[i][j]=number[i+1][j];
						number[i+1][j]=0;
					}
				}
			}
		}//右
		else if(dir == RIGHT) {
			for(int i = 3;i>0;i--){
				for(int j =3;j>-1;j--){
					if(number[i][j]==0){
						number[i][j]=number[i-1][j];
						number[i-1][j]=0;
					}
				}
			}
		}
	}
	//将数字相加封装成一个方法
	public void numberAdd(){
		/**数字相加*/
		//按上时
		if(number_add==UP_ADD){
			for(int i = 0;i<4;i++){
				for(int j =0;j<3;j++){
					if(number[i][j]==number[i][j+1]){
						number[i][j]=2*number[i][j+1];
						number[i][j+1]=0;
						score += number[i][j];
					}
				}
			}
		}//当按下时
		else if(number_add == DOWN_ADD) {
			for(int i = 3;i>-1;i--){
				for(int j = 3;j>0;j--){
					if(number[i][j]==number[i][j-1]){
						number[i][j] = 2*number[i][j-1];
						number[i][j-1]=0;
						score += number[i][j];
					}
				}
			}
		}
		//当按左时
		else if(number_add ==LEFT_ADD){
			for(int i =0;i<3;i++){
				for(int j = 0;j<4;j++){
					if(number[i][j]==number[i+1][j]){
						number[i][j]=2*number[i+1][j];
						number[i+1][j]=0;
						score += number[i][j];
					}
				}
			}
		}
		//当按右时
		else if(number_add == RIGHT_ADD) {
			for(int i = 3;i>0;i--){
				for(int j =3;j>-1;j--){
					if(number[i][j]==number[i-1][j]){
						number[i][j]=2*number[i-1][j];
						number[i-1][j]=0;
						score += number[i][j];
					}
				}
			}
		}
		number_add = 0;
	}
	//将第一次移动封装成一个方法
	public void firstMove(){
		step();
		step();
		step();
	}
	//将第二次移动封装成一个方法
	public void secondMove(){
		firstMove();
		dir = 0;
	}
	//将上面三个方法封装成一个方法
	public int [][] stepAction(){
		try {
			Thread.sleep(70);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**先移动*/
		firstMove();
		/**再相加*/
		numberAdd();
		/**再移动*/
		secondMove();
		return number;
	}
	
/**
 * 获取文件中保存的最高分
 * 判断最高分和当前分数的大小，若当前分数大，则替换
 */
	public void updateTopScore() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("gamedata/mostScore.txt"));
		String str = br.readLine();
		if(str!=null){
			mostScore = Integer.parseInt(str);
			if(score>mostScore){
				br.close();
				PrintWriter pw = new PrintWriter(new FileWriter("gamedata/mostScore.txt"));
				mostScore = score;
				pw.write(""+score);
				pw.close();
			}
		}
	}
	/**
	 * 保存当前分数
	 * @throws IOException 
	 */
	public void saveScore() throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter("gamedata/score.txt"));
		pw.write(score+"");
		pw.close();
	}
	/**
	 *读取文件中的分数 
	 * @throws IOException 
	 */
	public void readScore() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("gamedata/score.txt"));
		String str = null;
		if((str = br.readLine()) != null ) {
			score = Integer.parseInt(str);
			br.close();
		}
	}
	/**
	 * 将number写入Number.Txt文件中
	 * @throws IOException 
	 */
	public void saveState() throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter("gamedata/Number.txt"));
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<number.length;i++){
			for(int j = 0 ;j<number[i].length;j++){
				sb.append(number[i][j]).append(",");
			}
		}
		pw.write(sb.toString());
		pw.close();
	}
	/**
	 * 将Number.txt中的值读取出来，赋值给number
	 */
	public void readState() throws IOException{
		BufferedReader bf = new BufferedReader(new FileReader("gamedata/Number.txt"));
		String str = null ;
		if((str = bf.readLine())!=null){
			String [] s = str.split(",");
			for(int j = 0 ;j<4;j++){
				int su0 = Integer.parseInt(s[j]);
				number[0][j] = su0 ; 
				int su1 = Integer.parseInt(s[j+4]);
				number[1][j] = su1;
				int su2 = Integer.parseInt(s[j+8]);
				number[2][j] = su2;
				int su3 = Integer.parseInt(s[j+12]);
				number[3][j] = su3;
			}
		}
		bf.close();
		//判断是否生成2或4
		this.isAllZero();
	}
	/**
	 * 通过判断是否number的各个数都为0，来判断是否需要生成2或4
	 */
	public void isAllZero(){
		
		for(int i = 0 ;i<4;i++){
			for(int j = 0;j<4;j++){
				int sum = sums;
				sums = number[i][j] + sum;
			}
		}
		if(sums == 0 ){
			APPEAR = 0;
			appear = 0;
		}else {
			APPEAR = 1;
			appear = 1;
		}
	}
	/**
	 *判断游戏是否结束的方法 
	 */
	public void gameOverAction(){
		if(this.upAndDown() == true && this.leftAndRight() == true){
			state = 2;
		}
	}
	//判断左右之间知否相等
	public boolean upAndDown(){
		boolean isNotEqual = true; 
		for(int i = 0;i<number.length-1;i++){
			for(int j = 0;j<number[i].length;j++){
				if(number[i][j]==number[i+1][j] || number[i][j] == 0 || number[i+1][j]==0){
					isNotEqual = false;
				}
			}
		}
		return isNotEqual;
	}
	//判断上下之间是否相等
	public boolean leftAndRight(){
		boolean isNotEqual = true;
		for(int i =0;i<number.length;i++){
			for(int j =0;j<number[i].length-1;j++){
				if(number[i][j] == number[i][j+1] || number[i][j] == 0 || number[i][j+1]==0){
					isNotEqual = false;
				}
			}
		}
		return isNotEqual;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
