package com.github.brandonbai.game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * 
 * @Description: 主类
 * @author Feihu Ji
 * @since 2015年9月21日 
 *
 */
public class Main {

	//窗体
	private static JFrame frame;
	//游戏主面板
	private static MainPanel mainPanel;
	
	public static void main(String[] args) {
		//新建窗体对象
		frame = new JFrame("2048");
		frame.setIconImage(new ImageIcon(MainPanel.class.getResource("gameicon.png")).getImage());
		//窗体大小
		frame.setSize(460, 680);
		//窗体居中
		frame.setLocationRelativeTo(null);
		//窗体大小不可变
		frame.setResizable(false);
		//默认的关闭操作
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//新建主面板 对象
		mainPanel = new MainPanel(frame);
		//去掉面板默认布局
		mainPanel.setLayout(new FlowLayout(1, 200, 90));
		//面板背景颜色
		mainPanel.setBackground(new Color(247, 239, 230));
		//把面板添加到窗体中
		frame.add(mainPanel);
		//窗体可见
		frame.setVisible(true);
		//主界面的控制方法
		mainPanel.run();
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					mainPanel.getClassicPanel().saveScore();
					mainPanel.getClassicPanel().saveState();
					mainPanel.getDropPanel().updateTopScore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});		
		
	}
	
}
