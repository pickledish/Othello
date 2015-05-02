import java.util.Scanner;

/**
 * Created by Brandon on 4/18/15.
 *
 * Test main, nothing to see here
 *
 */

public class play {

	public static void main(String[] args) {

		boolean firstp = false;

		System.out.print("Please enter some arguments: ");
		Scanner scanny = new Scanner(System.in);
		String line = scanny.nextLine();
		String[] arguments = line.split(" ");

		if (arguments[0].equals("B"))
			firstp = true;
		String depthLimit = arguments[1];
		String timeLimit1 = arguments[2];
		String timeLimit2 = arguments[3];

		GameWindow othello = new GameWindow(firstp);
		othello.drawWindow();

	}
}
