/**
 * Created by Brandon on 4/18/15.
 *
 * Test main, nothing to see here
 *
 */

public class play {


	public static void main(String[] args) {

		boolean firstp = false;
		if (args[0].equals("B"))
			firstp = true;
		String depthLimit = args[1];
		String timeLimit1 = args[2];
		String timeLimit2 = args[3];

		GameWindow othello = new GameWindow(firstp);
		othello.drawWindow();

	}
}
