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
 * 
 * @Description: 下落模式的面板 
 * @author Feihu Ji
 * @since 2015年9月21日 
 *
 */
public class DropPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	//返回按钮和暂停按钮
	private MyButton backButton, pauseButton;
	//游戏方格数组
	private Cell[][] cells = new Cell[8][6];
	//下一个落下的方格
	private Cell nextCell;
	//当前游戏分数
	private int nowScore;
	//定义游戏最高分数
	private int topScore;
	//游戏运行的三个状态
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAMEOVER = 3;
	//游戏状态
	private int state = 1;
	/**
	 * 构造方法
	 */
	public DropPanel() {
		
		Color backColor = new Color(209, 174, 115);
		
		backButton = new MyButton("back", backColor);
		backButton.setBounds(277, 79, 60, 40);
		backButton.addActionListener(this);
		this.add(backButton);

		pauseButton = new MyButton("pause", backColor);
		pauseButton.setBounds(110, 79, 60, 40);
		pauseButton.addActionListener(this);
		this.add(pauseButton);
		
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j<cells[i].length; j++){
				cells[i][j] = new Cell(0, i, j);
			}
		}
		try {
			updateTopScore();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**游戏运行的方法*/
	public void run() {
		//键盘监听
		keyListener();
		//生成下一个落下的方格
		createAction();
		//方格进入游戏区
		enterAction();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
						//方格下落及合并
						addAction();
				}
			}, 100, 20);
	}
	/**键盘监听*/
	private void keyListener() {
		//获取焦点
		this.requestFocus();
		//键盘监听
		this.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
				 int code = e.getKeyCode();
				 if(state == RUNNING) {
					 if(code == 37) {
						 //方格左移
						 leftAciton();
					 }else if(code == 39) {
						 //方格右移
						 rightAciton();
					 }else if(code == 40) {
						 //方格下移
						 downAction();
					 }
					 repaint();
				 }else if(state == GAMEOVER && code == KeyEvent.VK_SPACE) {
					 nowScore = 0;
					 for(int i = 0; i < cells.length; i++) {
						 for(int j = 0; j<cells[i].length; j++) {
							 cells[i][j] = new Cell(0, i, j);
						 }
					 }
					 state = RUNNING;
				 }
			 }
		});
	}
	/**方格左移的方法*/
	private void leftAciton() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 1; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0) {
					if(cells[i][j-1].getNumber() == 0 && cells[i+1][j].getNumber() == 0) {
						cells[i][j-1]=new Cell(cells[i][j].getNumber(), i, j-1);
						cells[i][j] = new Cell(0, i, j);
						return ;
					}
				}
			}
		}
	}
	/**方格右移的方法*/
	private void rightAciton() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length-1; j++) {
				if(cells[i][j].getNumber() != 0) {
					if(cells[i][j+1].getNumber() == 0 && cells[i+1][j].getNumber() == 0) {
						cells[i][j+1] = new Cell(cells[i][j].getNumber(), i, j+1);
						cells[i][j] = new Cell(0, i, j);
						return ;
					}
				}
			}
		}
	}
	/**方格下移的方法*/
	private void downAction() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0 && cells[i+1][j].getNumber() == 0) {
					cells[i+1][j] = new Cell(cells[i][j].getNumber(), i+1, j);
					cells[i][j] = new Cell(0, i, j);
					return ;
				}
			}
		}
	}
	/**方格下落及合并的方法*/
	private void addAction() {
		for(int j = 0; j < cells[0].length; j++) {
			for(int i = 0; i < cells.length; i++) {
				if(cells[i][j].getNumber() != 0 && state == RUNNING) {
					if(i < cells.length-1 && cells[i+1][j].getNumber() == 0) {
						cells[i+1][j]=new Cell(cells[i][j].getNumber(), i+1, j);
						cells[i][j] = new Cell(0, i, j);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						repaint();
						continue;
					}
					//第一类合并情况
					if(i < cells.length-1 && j > 0 && j < cells[i].length-1) {
						//第四种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
						//第三种合并情况
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
						//第二种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
						//第五种合并情况
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						}
						//第六种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i+1][j].getNumber();
						}
						//第七种合并情况
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}
						//第一种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}
					//第二类合并情况	
					}else if(i < cells.length-1 && j == cells[i].length-1) {
						//第八种合并情况 
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i+1][j].getNumber();
						 }//第九种合并情况
						 if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						 }//第十种合并情况
						 if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						 }
					//第三类合并情况	 
					}else if(i < cells.length-1 && j == 0) {
						//第十一种合并情况
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}//第十二种合并情况
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//第十三种合并情况
						if(cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						}
					//第四类合并情况	
					}else if(i == cells.length-1 &&  j > 0 && j < cells[i].length-1) {
						//第十四种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//第十五种合并情况
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//第十六种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
					//第五类合并情况	
					}else if(i == cells.length-1 && j == cells[i].length-1) {
						//第十七种合并情况
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
					//第六类合并情况	
					}else if(i == cells.length-1 && j == 0) {
						//第十八种合并情况
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
					}
				}
			}
		}
		repaint();
		//如果方格全部落下则产生新的方格
		if(!isNoDroped())
			enterAction();
		if(isGameOver())
			state = GAMEOVER;
		repaint();
	}
	/**判断是否还有未落下的方格*/
	private boolean isNoDroped() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0) {
					if( cells[i+1][j].getNumber() == 0) {
						return true;  //isNoDroped = true;
					}else if(cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**下一个方格进入游戏区的方法*/
	private void enterAction() {
		if(cells[0][3].getNumber() == 0) {
			cells[0][3] = new Cell(nextCell.getNumber(), 0, 3);
			createAction();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	/**生成下一个落下的方格*/
	private void createAction() {
		Random random = new Random();
		int num = random.nextInt(6);
		nextCell = new Cell((int)(Math.pow(2, num+1)));
	}
	/**判断游戏是否结束*/
	private boolean isGameOver() {
		for(int i = 0; i < cells.length; i++) {
			if(cells[i][3].getNumber() == 0 || i < cells.length-1 
					&& cells[i][3].getNumber() == cells[i+1][3].getNumber()) {
				try {
					updateTopScore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return false; //isGameOver = false;
			}
		}
		return true;
	}
	public void paint(Graphics g) {
		super.paint(g);
		//画背景
		paintBG(g);
		//画方格
		paintCell(g);
		if(state == GAMEOVER) {
			paintGameOver(g) ;
		}
	}
	/**画游戏结束*/
	public void paintGameOver(Graphics g) {
		g.setColor(new Color(255, 255, 255, 150));
		g.fillRect(0, 130, 460, 680);
		g.setColor(new Color(230, 89 , 58));
		g.setFont(new Font("Impact", Font.BOLD, 70));
		g.drawString("Game Over", 39, 360);
	}
	/**画背景*/
	public void paintBG(Graphics g) {
		g.setColor(new Color(181, 170 ,156));
		//画提示下一个下落的方格背景
		g.fillRect(193, 63, 59, 59);
		//画当前分数框
		g.fillRoundRect(30, 5, 110, 70, 11, 11);
		//画最高分框
		g.fillRoundRect(310, 5, 110, 70, 11, 11);
		g.setColor(new Color(247, 247, 247));
		g.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		g.drawString("当前分数", 49, 30);
		g.drawString("" + nowScore, 53, 57);
		g.drawString("最高分", 339, 30);
		g.drawString("" + topScore, 339, 57);
		//画游戏框的背景
		g.setColor(new Color(181, 170 ,156));
		g.fillRect(41, 140, 361, 481);
		//画小方格的背景
		g.setColor(new Color(206, 190, 181));
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<6; j++) {
				g.fillRect(42+j*60, 141+i*60, Cell.SIZE, Cell.SIZE);
			}
		}
	}
	/**画带数字的小方格*/
	private void paintCell(Graphics g) {
		//画下一个落下的方格
		g.setColor(nextCell.getColor());
		g.fillRect(nextCell.getX(), nextCell.getY(), nextCell.getWidth(), nextCell.getHeight());
		g.setColor(new Color(20, 20 ,20));
		g.setFont(new Font("等线", Font.PLAIN, 23));
		g.drawString(""+nextCell.getNumber(), nextCell.getX()+getNumLen(nextCell.getNumber()),
				nextCell.getY()+nextCell.getHeight()*2/3);
		//画游戏区的方格
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				Cell cell = cells[i][j];
				int num = cell.getNumber();
				if(num != 0) {
					g.setColor(cell.getColor());
					g.fillRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
					g.setColor(new Color(10, 10, 10));
					g.setFont(new Font("等线", Font.PLAIN, 23));
					g.drawString(""+cell.getNumber(), cell.getX()+getNumLen(num), 
							cell.getY()+cell.getHeight()*2/3);
				}
			}
		}
	}
	/**数字居中的方法*/
	private int getNumLen(int num) {
		int len = 59*2/5 ;
		if(num/1000 != 0) {
			len = 59/11-5;
		} else if(num/100 !=0) {
			len = 59/5;
		} else if(num/10 !=0) {
			len = 59/3-3;
		}
		return len;
	}
	/**按钮监听*/
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == backButton) {
			setState(0);
		}else if(e.getSource() == pauseButton) {
			if(state == RUNNING) {
				state = PAUSE;
			}else if(state == PAUSE) {
				state = RUNNING;
				this.requestFocus();
			}
		}
	}
	/**
	 * 获取文件中保存的最高分
	 * 判断最高分和当前分数的大小，若当前分数大，则替换
	 */
	public void updateTopScore() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("gamedata/topScore.txt"));
		String str = br.readLine();
		if(str != null) {
			topScore = Integer.parseInt(str);
			if(nowScore > topScore) {
				br.close();
				PrintWriter pw = new PrintWriter(new FileWriter("gamedata/topScore.txt"));
				topScore = nowScore;
				pw.write("" + nowScore);
				pw.close();
			}
		}
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
