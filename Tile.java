/**
 * Created by Brandon on 4/22/15.
 *
 * An abstract Tile class, so that we can talk about tiles in the OthelloGame class without
 * having to mention the whole JButton aspect of it that GameButton has. Keepin it separate!
 *
 */

public interface Tile {

	void setColor(String c);
	String getColor();

	void toggle();
	boolean getToggled();

	int getx();
	int gety();

}