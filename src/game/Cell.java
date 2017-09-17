package game;

import java.awt.Color;

/**
 * 下落模式的方格类
 */
public class Cell {
	//方格的数字
	private int number;
	//方格的X坐标
	private int x;
	//方格的Y坐标
	private int y;
	//方格的宽
	private int width;
	//方格的高
	private int height;
	//方格的颜色
	private Color color;
	//下落模式方格大小
	public static final int SIZE = 59;
	
	/**
	 * 构造方法
	 */
	public Cell(int num) {
		this.number = num;
		this.x = 193;
		this.y = 63;
		this.width = SIZE;
		this.height = SIZE;
		this.color = getColor(num);
	}
	public Cell(int num,int i, int j) {
		this.number = num;
		this.x = 42+j*60;
		this.y = 141+i*60;
		this.width = SIZE;
		this.height = SIZE;
		this.color = getColor(num);
	}
	public static final Color getColor(int num) {
		Color color = null;
		switch(num) {
		case 0:
			break;
		case 2:
			color = new Color(239, 219, 173);
			break;
		case 4:
			color = new Color(239, 174, 115);
			break;
		case 8:
			color = new Color(239, 142, 82);
			break;
		case 16:
			color = new Color(247, 117, 0);
			break;
		case 32:
			color = new Color(230, 89 , 58);
			break;
		case 64:
			color = new Color(247, 235, 164);
			break;
		case 128:
			color = new Color(222, 227, 148);
			break;
		case 256:
			color = new Color(239, 215, 115);
			break;
		case 512:
			color = new Color(239, 198, 0);
			break;
		case 1024:
			color = new Color(164, 235, 239);
			break;
		case 2048:
			color = new Color(115, 231, 230);
			break;
		case 4096:
			color = new Color(8, 231, 222);
			break;
		case 8192:
			color = new Color(0, 0, 0);
			break;
		}
		return color;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

}
