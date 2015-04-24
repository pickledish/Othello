import javax.swing.*;
import java.awt.*;

/**
 * Created by Brandon on 4/18/15.
 *
 * The Button class! Each one of these is a button you see on the gameboard.
 * They each extend from JButton cause they're buttons
 * And they implement Tile because they're tiles too, and we want those methods but multiple inheritance isn't a thing
 *
 * Contains methods for getting/setting color of the button and toggling whether it's been pressed!
 *
 */

public class GameButton extends JButton implements Tile {

	public boolean toggled = false;
	String color = null;

	public GameButton(String text) {
		super(text);
	}

	// Sets up the button to be colored! I used a string because reasons!
	public void setColor(String col) {

		if (toggled) {
			this.setOpaque(true);
			this.setBorderPainted(false);

			if (col.equals("blue")) this.setBackground(Color.blue);
			if (col.equals("red")) this.setBackground(Color.red);

			color = col;
		}

	}

	@Override
	public String getColor() {
		return color;
	}

	// Toggles toggle. What up.
	public void toggle() {
		this.toggled = !this.toggled;
		this.setEnabled(false);
	}

	@Override
	public boolean getToggled() {
		return toggled;
	}

}
