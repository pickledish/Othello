/**
 * Created by Brandon on 4/21/15.
 *
 *
 * Changed again
 */
public class OthelloGame {

	Tile[][] boardState;
	boolean flip = false;

	public OthelloGame(Tile[][] buttons) {

		boardState = buttons;

	}

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

	public boolean[][] getViableMoves() {

		boolean[][] returner = new boolean[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++){


				boolean[][] truths = new boolean[3][3];

				for (int k = -1; k <= 1; k++) {
					for (int l = -1; l <= 1; l++) {

						try { truths[k+1][l+1] = boardState[i+k][j+l].getToggled();
						} catch (ArrayIndexOutOfBoundsException e) { truths[k+1][l+1] = false; }

					}
				}

				boolean flag = false;
				for (boolean[] a : truths)
					for (boolean b : a)
						if (b) flag = true;

				returner[i][j] = flag;

				// TODO: Insert color-specific code here
			}
		}

		return returner;
	}

	public void tileClicked(Tile pressed) {

		if (!pressed.getToggled()) {
			pressed.toggle();
			if (flip) pressed.setColor("red");
			else pressed.setColor("blue");

			flip = !flip;
		}
	}

}
