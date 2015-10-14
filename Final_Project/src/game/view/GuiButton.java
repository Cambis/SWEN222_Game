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

	/**
	 * Create a Gui Button
	 * @param name Name of button (what text in button is)
	 * @param x x-position of button
	 * @param y y-position of button
	 * @param width of button
	 * @param height of button
	 */
	public GuiButton(String name, int x, int y, int width, int height){
		this.name=name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Create a Gui Button at 0,0
	 * @param name Name of button (what text in button is)
	 * @param width of button
	 * @param height of button
	 */
	public GuiButton(String name,int width, int height){
		this.name=name;
		this.width = width;
		this.height = height;
	}

	/**
	 * Set position of button
	 * @param x set x position to
	 * @param y set y position to
	 */
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Check if point is on button, sets Hovered to true if it is
	 * @param x x-position of point
	 * @param y y-position of point
	 * @return if point on button
	 */
	public boolean checkHovered(int x, int y){
		if(x>=this.x && x<=this.x+width
				&& y>=this.y+headSize && y<=this.y+height+headSize){
			isHovered = true;
			return true;
		}
		isHovered = false;
		return true;
	}

	/**
	 * Gets name of button
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns if hovered variable currently true
	 * @return isHovered
	 */
	public boolean isHovered(){
		return isHovered;
	}

	/**
	 * Draw button to a graphics
	 * @param g graphics to draw button to
	 */
	public void draw(Graphics g){
		if(isHovered){
			g.setColor(Color.ORANGE);
		}else{
			g.setColor(Color.WHITE);
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

	/**
	 * Draw string in centered between boundary
	 * @param s String to draw
	 * @param width width of object
	 * @param height height of object
	 * @param XPos x-position
	 * @param YPos y-position
	 * @param g grapics to draw to
	 */
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
