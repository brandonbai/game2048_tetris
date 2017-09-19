package com.github.brandonbai.game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 主界面
 */
public class MainPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	//主界面上的模式按钮
	private MyButton dropButton, classicButton;
	//经典模式面板
	public ClassicPanel classicPanel;
	//下落模式面板
	private DropPanel dropPanel;
	//2048标志
	private JLabel iconLabel;
	//窗体
	private static JFrame frame;
	//游戏主面板
	private static MainPanel mainPanel;
	private static final String DROP_STYLE = "2048/下落模式";
	private static final String CLASSIC_STYLE = "2048/经典模式";
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
		mainPanel = new MainPanel();
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
			public void windowClosing(WindowEvent e) {
				try {
					mainPanel.classicPanel.scoreSave();
					mainPanel.classicPanel.numberWrite();
					mainPanel.dropPanel.topScore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	/**
	 * 构造方法
	 */
	public MainPanel() {

		Color color = new Color(206, 170, 132);
		
		dropButton = new MyButton("下 落 模 式", color);
		classicButton = new MyButton("经 典 模 式", color);
		
		dropPanel = new DropPanel();
		classicPanel = new ClassicPanel();
		zujian();
	}
	/**各组件*/
	public void zujian() {
		
		iconLabel = new JLabel("2048");
		iconLabel.setFont(new Font("Impact",Font.BOLD,108));
		iconLabel.setForeground(new Color(173, 113, 66));
		add(iconLabel);
		Font font = new Font("微软雅黑", Font.PLAIN, 27);
		dropButton.addActionListener(this);
		dropButton.setFont(font);
		dropPanel.setBounds(0, 0, 460, 680);
		add(dropButton);

		classicButton.addActionListener(this);
		classicButton.setFont(font);
		classicPanel.setBounds(0, 0, 460, 680);
		add(classicButton);
		
	}
	/**游戏运行及控制方法*/
	public void run() {
		//主控制流程
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				back();
			}
		}, 0, 20);
	}
	/**返回主界面的方法*/
	public void back() {
		if(classicPanel.getState() == 0 || dropPanel.getState() == 0) {
			frame.setTitle("2048");
			removeAll();
			add(classicButton);
			add(dropButton);
			add(iconLabel);
			classicPanel.setState(1);
			if(dropPanel.getState() == 0)
				dropPanel.setState(2);
			repaint();
		}
	}
	/**按钮监听*/
	public void actionPerformed(ActionEvent e) {
		removeAll();
		if(e.getSource() == classicButton) {
			frame.setTitle(CLASSIC_STYLE);
			add(classicPanel);
			if(classicPanel.getState() == 1) {
				classicPanel.go();
			}
		}else if(e.getSource() == dropButton) {
			frame.setTitle(DROP_STYLE);
			add(dropPanel);
			dropPanel.requestFocus();
			if(dropPanel.getState() == 1) {
				dropPanel.run();
			}
		}
		repaint();
	}

}
