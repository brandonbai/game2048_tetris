package com.github.brandonbai.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @Description: 主面板 
 * @author Feihu Ji
 * @since 2015年9月21日
 *
 */
public class MainPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	//主界面上的模式按钮
	private MyButton dropButton, classicButton;
	//经典模式面板
	private ClassicPanel classicPanel;
	//下落模式面板
	private DropPanel dropPanel;
	//2048标志
	private JLabel iconLabel;
	private JFrame frame;
	private static final String DROP_STYLE = "2048/下落模式";
	private static final String CLASSIC_STYLE = "2048/经典模式";
	/**
	 * 构造方法
	 */
	public MainPanel(JFrame frame) {
		this.frame = frame;
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
			
			@Override
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
			if(dropPanel.getState() == 0) {
				dropPanel.setState(2);
			}
			repaint();
		}
	}
	/**按钮监听*/
	@Override
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
	public MyButton getDropButton() {
		return dropButton;
	}
	public void setDropButton(MyButton dropButton) {
		this.dropButton = dropButton;
	}
	public MyButton getClassicButton() {
		return classicButton;
	}
	public void setClassicButton(MyButton classicButton) {
		this.classicButton = classicButton;
	}
	public ClassicPanel getClassicPanel() {
		return classicPanel;
	}
	public void setClassicPanel(ClassicPanel classicPanel) {
		this.classicPanel = classicPanel;
	}
	public DropPanel getDropPanel() {
		return dropPanel;
	}
	public void setDropPanel(DropPanel dropPanel) {
		this.dropPanel = dropPanel;
	}
	public JLabel getIconLabel() {
		return iconLabel;
	}
	public void setIconLabel(JLabel iconLabel) {
		this.iconLabel = iconLabel;
	}
	public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
	

}
