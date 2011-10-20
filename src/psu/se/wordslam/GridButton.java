package psu.se.wordslam;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class GridButton extends Button {
	protected boolean 		isClicked = false;
	protected int			x;
	protected int			y;
	

	public GridButton(Context context) {
		super(context);
	}

	
	public GridButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public boolean isClicked() {
		return isClicked;
	}
	
	
	/**
	 * Accessor method for retrieving this button's x coordinate on the game board
	 * @return x The x coordinate on the game boar
	 */
	public int getX() {
		return x;
	}
	
	
	/**
	 * @param x The x coordinate for this button on the game board
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	
	/**
	 * Accessor method for retrieving this button's y coordinate on the game board
	 * @return y The y coordinate on the game boar
	 */
	public int getY() {
		return y;
	}
	
	
	/**
	 * @param y The y coordinate for this button on the game board
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		    // Change color
			if (!isClicked) {
				this.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_selected));
				isClicked = true;
			} else {
				this.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
				isClicked = false;
			}
		}	
		return super.onTouchEvent(event);
	}
}
