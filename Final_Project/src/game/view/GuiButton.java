package game.view;

import java.awt.Color;
import java.awt.Graphics;

public class GuiButton {

	private String name = "NULL";
	private int height = 0;
	private int width = 0;
	private int x = 0;
	private int y = 0;
	private int headSize = 28;
	private boolean isHovered = false;
	private String[] buttonAddress = {"defalut_button.png"};

	/**
	 * Button style, coorelates to the buttonAddress of the base image of the button
	 * @author Bieleski, Bryers, Gill & Thompson MMXV.
	 *
	 */
	enum BUTTON_STYLE{
		DEFAULT;
	}

	public GuiButton(String name, int x, int y, int width, int height){
		this.name=name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public GuiButton(String name,int width, int height){
		this.name=name;
		this.width = width;
		this.height = height;
	}

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}

	public boolean checkHovered(int x, int y){
		if(x>=this.x && x<=this.x+width
				&& y>=this.y+headSize && y<=this.y+height+headSize){
			isHovered = true;
			return true;
		}
		isHovered = false;
		return true;
	}

	public String getName(){
		return name;
	}

	public boolean isHovered(){
		return isHovered;
	}

	public void draw(Graphics g){
		if(isHovered){
			g.setColor(Color.ORANGE);
		}else{
			g.setColor(Color.CYAN);
		}
		g.fillRect(x, y, width, height);
		if(isHovered){
			g.setColor(Color.RED);
		}else{
			g.setColor(Color.BLACK);
		}
		g.drawRect(x, y, width, height);
		g.setColor(Color.BLACK);
		drawCenteredString(name, width, height, x, y, g);

	}

	private void drawCenteredString(String s, int width, int height, int XPos, int YPos, Graphics g){
        int stringLen = (int)
            g.getFontMetrics().getStringBounds(s, g).getWidth();
        int stringHeight = (int)
                g.getFontMetrics().getStringBounds(s, g).getHeight();
        int startX = width/2 - stringLen/2;
        int startY = height/2 + stringHeight/2;
        g.drawString(s, startX + XPos, startY+ YPos);
 }
}
