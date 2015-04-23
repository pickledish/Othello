import javax.swing.*;
import java.awt.*;

/**
 * Created by Brandon on 4/18/15.
 */

public class GameButton extends JButton implements Tile {

	public boolean toggled = false;
	String color = null;

	public GameButton(String text) {
		super(text);
	}

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

	public void toggle() {
		this.toggled = !this.toggled;
		this.setEnabled(false);
	}

	@Override
	public boolean getToggled() {
		return toggled;
	}

}
