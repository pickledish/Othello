/**
 * Created by Brandon on 4/18/15.
 *
 * Test main, nothing to see here
 *
 */

public class testMain {

	public static void main(String[] args) {
		
		String player = "two";
		if (args[0] == "B")
			player = "one";
		String depthLimit = args[1];
		String timeLimit1 = args[2];
		String timeLimit2 = args[3];

		GameWindow othello = new GameWindow(player);
		othello.drawWindow();

	}
}
