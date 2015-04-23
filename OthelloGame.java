/**
 * Created by Brandon on 4/21/15.
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

				boolean a1, a2, a3, a4;

				try { a1 = boardState[i - 1][j].getToggled();
				} catch (ArrayIndexOutOfBoundsException e) {a1 = false;}

				try { a2 = boardState[i][j - 1].getToggled();
				} catch (ArrayIndexOutOfBoundsException e) {a2 = false;}

				try { a3 = boardState[i + 1][j].getToggled();
				} catch (ArrayIndexOutOfBoundsException e) {a3 = false;}

				try { a4 = boardState[i][j + 1].getToggled();
				} catch (ArrayIndexOutOfBoundsException e) {a4 = false;}

				if (!(a1 || a2 || a3 || a4)) {
					returner[i][j] = false;
					continue;
				}

				returner[i][j] = true;

				/* // I don't think this code does anything?
				boolean temp = false;
				for (int p = 0; p < 8; p++) {
					if (boardState[i][p].getToggled()) temp = true;
					if (boardState[p][j].getToggled()) temp = true;
				}

				if (!temp) {

					returner[i][j] = false;
					continue;
				}
				*/


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
