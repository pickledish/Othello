import javax.swing.*;
import java.awt.*;

/**
 * Created by Brandon on 4/18/15.
 *
 * The Button class! Each one of these is a button you see on the gameboard.
 * They each extend from JButton cause they're buttons
 * And they implement Tile because they're tiles too, and we want those methods
 *
 * Contains methods for getting/setting color of the button and toggling whether it's been pressed!
 *
 */

public class GameButton extends JButton implements Tile {

	public boolean toggled = false;
	String color = null;

	// The x and y coordinates of the button in question
	int x;
	int y;

	public GameButton(String text, int x, int y) {
		super(text);
		this.x = x;
		this.y = y;
	}

	// Sets up the button to be colored. Or, if the color is null, resets the button to its uncolored state
	public void setColor(String col) {

		if (toggled) {
			this.setOpaque(true);
			this.setBorderPainted(false);

			if (col.equals("blue")) this.setBackground(Color.blue);
			if (col.equals("red")) this.setBackground(Color.red);

			color = col;

		} else if (col == null) {
			this.setOpaque(false);
			this.setBorderPainted(true);
			this.setBackground(new JButton().getBackground());
			color = null;
		}

	}

	@Override
	public String getColor() {
		return color;
	}

	// Toggles toggle
	public void toggle() {
		this.toggled = !this.toggled;
		this.setEnabled(false);
	}

	@Override
	public boolean getToggled() {
		return toggled;
	}

	@Override
	public int getx() {
		return x;
	}

	@Override
	public int gety() {
		return y;
	}

}
