package com.github.brandonbai.game;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;

/**
 * 
 * MyButton 
 * @Description: 自定义按钮类
 * @author Feihu Ji
 * @sine 2017年9月22日
 *
 */
public class MyButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造方法
	 */
	public MyButton(String text, Color backColor) {

		super(text);
		setForeground(Color.white);
		setBackground(backColor);
		setBorder(new TitledBorder(""));
		setFont(new Font("微软雅黑", Font.PLAIN, 20));
	}
}
