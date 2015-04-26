import java.util.ArrayList;

/**
 * Created by Brandon on 4/21/15.
 * <p/>
 * The game class! Manages each in-game part of the project
 * <p/>
 * STAYS TOTALLY SEPARATE FROM THE GAMEWINDOW CLASS / ALL GUI ELEMENTS PLS
 * <p/>
 * But yeah, logic stuff goes here. Each method should be explained well enough, right?
 */

public class OthelloGame {

	// BoardState is the game board, and flip keeps track of whose turn it is! Every click --> flip changes
	Tile[][] boardState;
	String current = "blue";

	public OthelloGame(Tile[][] buttons) {

		boardState = buttons;

	}

	// Sets the middle 4 tiles to their starting colors, and toggles each to disabled
	public void setUp() {

		boardState[3][3].toggle();
		boardState[3][3].setColor("red");
		boardState[3][4].toggle();
		boardState[3][4].setColor("blue");
		boardState[4][4].toggle();
		boardState[4][4].setColor("red");
		boardState[4][3].toggle();
		boardState[4][3].setColor("blue");

	}

	// Returns an array of booleans, each one corresponding to a button on the board
	// If the boolean for the location is true, then it's a viable move! If false, leave it disabled
	public boolean[][] getViableMoves() {

		boolean[][] returner = new boolean[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {


				// Checks
				boolean[][] truths = new boolean[3][3];

				for (int k = -1; k <= 1; k++) {
					for (int l = -1; l <= 1; l++) {

						try {
							truths[k + 1][l + 1] = boardState[i + k][j + l].getToggled();
						} catch (ArrayIndexOutOfBoundsException e) {
							truths[k + 1][l + 1] = false;
						}

					}
				}

				boolean flag = false;
				for (boolean[] a : truths)
					for (boolean b : a)
						if (b) flag = true;

				returner[i][j] = flag;

			}
		}

		return getActualViableMoves(returner);
		//return returner;
	}

	// The actionEvent we add to each button in GameWindow! Sets the button color, disables it, and flips flip
	public void tileClicked(Tile pressed) {

		if (!pressed.getToggled()) {
			pressed.toggle();
			pressed.setColor(current);

			current = current.equals("blue") ? "red" : "blue";
		}
	}

	// Jesus christ here we go
	public boolean[][] getActualViableMoves(boolean[][] firstStep) {

		boolean[][] returner = new boolean[8][8];

		// For each tile S
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				// If it's a reasonable option for a move to be made
				if (firstStep[i][j])
					// For each other tile T
					for (Tile[] row : boardState)
						for (Tile tile : row)
							// if T has been clicked and has a color
							if (tile.getToggled())
								// If the current placing color is the same as S's color
								if (current.equals(tile.getColor()))
									// If they're in the same line and > 1 distance apart
									if (inSameLine(boardState[i][j], tile))
										// TODO: (Test here to see if every piece between the two is filled & not all same)
										// Return true for now!
										returner[i][j] = true;

		return returner;
	}

	// Checks to see if 2 tiles are in the same vertical, horizontal, or (perfect) diagonal line
	// Also checks to see if they're more than 1 tile apart
	// If both are true, then returns true!
	public boolean inSameLine(Tile tile1, Tile tile2) {

		int x1 = tile1.getx();
		int y1 = tile1.gety();
		int x2 = tile2.getx();
		int y2 = tile2.gety();

		double slope = Math.abs((y2 - y1) / (double) (x2 - x1));

		return (((slope == 0) || (slope == Double.POSITIVE_INFINITY) || (slope == 1)) && ((Math.abs(x1 - x2) > 1) || (Math.abs(y2 - y1) > 1)));

	}
}