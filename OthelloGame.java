import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;



/**
 * Created by Brandon on 4/21/15.
 *1
 * The game class! Manages each in-game part of the project
 *
 * STAYS TOTALLY SEPARATE FROM THE GAMEWINDOW CLASS / ALL GUI ELEMENTS PLS
 *
 * But yeah, logic stuff goes here. Each method should be explained well enough, right?
 *
 * TODO What happens when a player has no viable moves?
 *
 */

public class OthelloGame {

	// BoardState is the game board, and current keeps track of whose turn it is! Every click --> current changes
	Tile[][] boardState;
	String current = "blue";
	boolean ai;
	String myColor;

	// These are Som's things but I'm sure they're important!
	boolean[][] isAvailable = new boolean[8][8];

	// The constructor! Does nothing, really
	public OthelloGame(Tile[][] buttons, String c, boolean ai, boolean first) {

		boardState = buttons;
		current = c;
		this.ai = ai;
		if (first)
			myColor = "blue";
		else
			myColor = "red";

	}

	// Clears the board, then sets the middle 4 tiles to their starting colors, and toggles each to disabled
	public void setUp() {

		for (Tile[] row : boardState) {
			for (Tile spot : row) {
				if (spot.getToggled()) spot.toggle();
				spot.setColor(null);
			}
		}

		boardState[3][3].toggle();
		boardState[3][3].setColor("red");
		boardState[3][4].toggle();
		boardState[3][4].setColor("blue");
		boardState[4][4].toggle();
		boardState[4][4].setColor("red");
		boardState[4][3].toggle();
		boardState[4][3].setColor("blue");

	}

	public Tile[][] getDeepCopy() {

		Tile[][] returner = new Tile[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				returner[i][j] = boardState[i][j].returnCopy();
			}
		}
		return returner;
	}

	// The actionEvent we add to each button in GameWindow! Sets the button color, disables it, and flips current
	public void tileClicked(Tile pressed) {

		// Only runs if the button hasn't ever been pressed before
		if (!pressed.getToggled()) {

			// Changes every color in the row to match the current color #Jeff
			rowColorChanger(pressed, true);
			pressed.toggle();
			pressed.setColor(current);

			current = current.equals("blue") ? "red" : "blue";

			// Tests to see if there are any available moves following the press. If not, the next player passes turn
			// TODO: Might not work, hard to test
			if (availableMoveCount() == 0) {
				JOptionPane.showMessageDialog(null, "No Moves available! " + current + "skips turn!",
						"No moves!", JOptionPane.PLAIN_MESSAGE);
				current = current.equals("blue") ? "red" : "blue";

			}

			if (current.equals("red")) System.out.println(minimaxHelper(4));
		}
	}

	// Returns true if, for a given available move array, all spots are not available moves
	public boolean noMoves(boolean[][] available) {

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (available[i][j] && !boardState[i][j].getToggled() ) return false;

		return true;
	}

	// Returns the total number of (col) colored tiles on the board at the current moment
	public int getTiles(String col) {

		int red = 0;
		int blue = 0;

		for (Tile[] row : boardState) {
			for (Tile spot : row) {
				if (spot.getColor() != null) {
					if (spot.getColor().equals(("red"))) red++;
					else blue++;
				}
			}
		}

		return (col.equals("red")) ? red : blue;
	}


	
	// RETURN ALL AVAILABLE MOVES TO isAvailable :::::

	
	public int availableMoveCount() {
		getViableMoves();
		int count = 0;
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				if (isAvailable[x][y])
					count++;
			}
		return count;
	}

	public Tile[][] dummy(Tile pressed) {
		
		Tile[][] copy = getDeepCopy();
		OthelloGame temp = new OthelloGame(copy, current, ai, false);
		temp.rowColorChanger(pressed, true);
		return temp.boardState;
		
	}
	
	//For making colors change appropriately in all directions
	public int rowColorChanger(Tile pressed, boolean change) {
		int count = 0;
		String currColor = current;
		String otherColor = "red";
		if (current == "red")
			otherColor = "blue";

		int myX = pressed.getx();
		int myY = pressed.gety();
		int currX = myX;
		int currY = myY;

		currY++;
		if (currY < 8)
			while (boardState[currY][currX].getColor() == otherColor && currY < 7) {
				currY++;
			}
		if (currY < 8) {
			if (boardState[currY][currX].getColor() == currColor) {
				currY--;
				while (currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currY--;
				}
			}
		}
		currY = myY;
		currX = myX;

		currX++;
		if (currX < 8)
			while (boardState[currY][currX].getColor() == otherColor && currX < 7) {
				currX++;
			}
		if (currX != 8) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX--;
				while (currX != myX) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX--;
				}
			}
		}
		currY = myY;
		currX = myX;

		currY--;
		if (currY >= 0)
			while (boardState[currY][currX].getColor() == otherColor && currY > 0) {
				currY--;
			}
		if (currY != -1) {
			if (boardState[currY][currX].getColor() == currColor) {
				currY++;
				while (currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currY++;
				}
			}
		}
		currY = myY;
		currX = myX;

		currX--;
		if (currX >= 0)
			while (boardState[currY][currX].getColor() == otherColor && currX > 0) {
				currX--;
			}
		if (currX != -1) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX++;
				while (currX != myX) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX++;
				}
			}
		}
		currY = myY;
		currX = myX;


		currX--;
		currY--;
		if (currX >= 0 && currY >=0)
			while (boardState[currY][currX].getColor() == otherColor && (currX > 0 && currY > 0)) {
				currX--;
				currY--;
			}
		if (currX != -1 && currY !=-1) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX++;
				currY++;
				while (currX != myX && currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX++;
					currY++;
				}
			}
		}
		currY = myY;
		currX = myX;

		currX--;
		currY++;
		if (currX >= 0 && currY < 8)
			while (boardState[currY][currX].getColor() == otherColor && (currX > 0 && currY < 7)) {
				currX--;
				currY++;
			}
		if (currX != -1 && currY != 8) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX++;
				currY--;
				while (currX != myX && currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX++;
					currY--;
				}
			}
		}
		currY = myY;
		currX = myX;

		currX++;
		currY--;
		if (currX < 8 && currY > -1)
			while (boardState[currY][currX].getColor() == otherColor && (currX < 7 && currY > 0)) {
				currX++;
				currY--;
			}
		if (currX != 8 && currY !=-1) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX--;
				currY++;
				while (currX != myX && currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX--;
					currY++;
				}
			}
		}
		currY = myY;
		currX = myX;

		currX++;
		currY++;
		if (currX < 8 && currY < 8)
			while (boardState[currY][currX].getColor() == otherColor && (currX < 7 && currY < 7)) {
				currX++;
				currY++;
			}
		if (currX != 8 && currY != 8) {
			if (boardState[currY][currX].getColor() == currColor) {
				currX--;
				currY--;
				while (currX != myX && currY != myY) {
					if (change)
						boardState[currY][currX].setColor(currColor);
					count++;
					currX--;
					currY--;
				}
			}
		}
		currY = myY;
		currX = myX;
		return count;
	}


	
	public String compMove() {
		int bestX = -1;
		int bestY = -1;
		int bestScore = -10000;
		int currScore = 0;
		getViableMoves();

		boolean[][] available = isAvailable;

		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				if (available[x][y]) {
					currScore = rowColorChanger(boardState[x][y], false);
					if ((x == 0 || y == 0) || (x == 7 || y == 7))
						currScore += 10;
					if (((x == 0 && y == 0) || (x == 0 && y == 7)) || ((x == 7 && y == 0) || (x == 7 && y == 7)))
						currScore += 1000;
					if (x == 1 || y == 1 || x == 6 || y == 6)
						currScore -= 5;
					if (((x == 1 && y == 1) || (x == 1 && y == 6)) || ((x == 6 && y == 1) || (x == 6 && y == 6)))
						currScore -=10;
					if ((((x == 0 && y == 1) || (x == 0 && y == 6)) || ((x == 1 && y == 0) || (x == 6 && y == 0))) 
							|| (((x == 6 && y == 7) || (x == 7 && y == 6)) || ((x == 1 && y == 7) || (x == 7 && y == 1))))
						currScore-=15;
					if (((x == 2 && y == 2) || (x == 2 && y == 5)) || ((x == 5 && y == 2) || (x == 5 && y == 5)))
						currScore += 2;
					if (currScore > bestScore) {
						bestScore = currScore;
						bestX = y;
						bestY = 7-x;
						
					}
					
					
				}
			}
		return (bestX + " " + bestY);
	}

	// START VIABLE MOVE CODE
	
	public boolean[][] getViableMoves() {

		boolean[][] returner = new boolean[8][8];

		// Adds each tile on the perimeter of the blob of in-place tiles to returner
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				boolean[][] truths = new boolean[3][3];

				// Checks each of 8 tile surrounding boardstate[i][j] to see if it's part of the perimeter
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

		// This was only the first step (perimeter tiles). Keep going for actual viable moves
		return getAvailableMoves(returner);
	}

	// When given perimeter tiles, checks a million criteria to see if a tile is an actual possibility
	public boolean[][] getAvailableMoves(boolean[][] firstStep) {

		boolean[][] returner = new boolean[8][8];
		double slope;

		// For each tile S in the boardState
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				// If it's part of the perimeter of current colored tiles (as determined before)
				if (firstStep[i][j])
					// For each tile T in boardState (again)
					for (Tile[] row : boardState)
						for (Tile tile : row)
							// if T has been clicked and has a color
							if (tile.getToggled())
								// If the current placing color is the same as S's color
								if (current.equals(tile.getColor()))
									// If they're in the same line and > 1 distance apart, get their slope
									if ((slope = inSameLine(boardState[i][j], tile)) != 0.1)
										// Test here to see if every piece between the two is filled & not all same
										if (tilesAreFilled(boardState[i][j], tile, slope))
											// If so, it's an actual choice!
											if (!boardState[i][j].getToggled())
												returner[i][j] = true;
		
		
		isAvailable = returner;
		return returner;
	}

	// Returns whether 2 tiles are in the same vertical, horizontal, or (perfect, slope = 1) diagonal line
	public double inSameLine(Tile tile1, Tile tile2) {

		int x1 = tile1.getx();
		int y1 = tile1.gety();
		int x2 = tile2.getx();
		int y2 = tile2.gety();

		double slope = (y2 - y1) / (double) (x2 - x1);

		// The ugly if statement, also checks to make sure the two points are > 1 square apart
		if (((slope == 0) || (slope == Double.POSITIVE_INFINITY) || (slope == Double.NEGATIVE_INFINITY) || (slope == 1)
				|| (slope == -1)) && ((Math.abs(x1 - x2) > 1) || (Math.abs(y2 - y1) > 1)))
			return slope;

		// Otherwise, returns a useless slope (0.1)
		else return 0.1;

	}

	// Returns a linked list of all tiles between tile1 and tile2, as long as they're all toggled (colored)
	public LinkedList<Tile> getBetweenTiles(Tile tile1, Tile tile2, double slope) {

		LinkedList<Tile> returner = new LinkedList<Tile>();
		int x1 = tile1.getx();
		int y1 = tile1.gety();
		int x2 = tile2.getx();
		int y2 = tile2.gety();

		if (slope == 0) {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = start.getx() + 1; i < end.getx(); i++) {

				// If the current color and the first tile next to the possible viable tile are the same, return null
				int checkNum = (tile1 == start) ? tile1.getx() + 1 : tile1.getx() - 1;
				if ((i == checkNum) && (boardState[y1][i].getColor() != null) && (boardState[y1][i].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[y1][i]);

			}

		} else if ((slope == Double.POSITIVE_INFINITY) || (slope == Double.NEGATIVE_INFINITY)) {

			Tile start = (y1 < y2) ? tile1 : tile2;
			Tile end = (y1 < y2) ? tile2 : tile1;

			for (int i = start.gety() + 1; i < end.gety(); i++) {

				// If the current color and the first tile next to the possible viable tile are the same, return null
				int checkNum = (tile1 == start) ? tile1.gety() + 1 : tile1.gety() - 1;
				if ((i == checkNum) && (boardState[i][x1].getColor() != null) && (boardState[i][x1].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[i][x1]);
			}

		} else if (slope == 1) {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = 1; i < (end.getx() - start.getx()); i++) {

				// If the current color and the first tile next to the possible viable tile are the same, return null
				int checkNum = (tile1 == start) ? 1 : (tile1.getx() - tile2.getx() - 1);
				if ((i == checkNum) && (boardState[start.gety() + i][start.getx() + i].getColor() != null) &&
						(boardState[start.gety() + i][start.getx() + i].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[start.gety() + i][start.getx() + i]);
			}

		} else {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = 1; i < (end.getx() - start.getx()); i++) {

				// If the current color and the first tile next to the possible viable tile are the same, return null
				int checkNum = (tile1 == start) ? 1 : (tile1.getx() - tile2.getx() - 1);
				if ((i == checkNum) && (boardState[start.gety() - i][start.getx() + i].getColor() != null) &&
						(boardState[start.gety() - i][start.getx() + i].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[start.gety() - i][start.getx() + i]);
			}
		}

		return returner;

	}

	// Checks to see if all the tiles between tile1 and tile2 are colored and toggled, so they can be flipped
	public boolean tilesAreFilled(Tile tile1, Tile tile2, double slope) {

		LinkedList<Tile> between = getBetweenTiles(tile1, tile2, slope);
		boolean notAllTheSame = false;

		for (Tile current : between) {

			if (current == null) return false;
			if (current.getColor() == null) return false;
			if (!current.getColor().equals(tile2.getColor())) notAllTheSame = true;

		}

		return notAllTheSame;

	}

	// END VIABLE MOVE CODE

	String[] path;
	public String minimaxHelper(int depth) {

		path = new String[depth];
		boolean player = (current.equals("blue"));
		minimax(this.boardState, depth, player);

		return path[depth-1];

	}


	public int minimax(Tile[][] boardState, int depth, boolean maximizing) {

//this method is used to maximize the number of tiles we want and minimize
//the tiles that the opponent will acquire

		String current = (maximizing) ? "blue" : "red";
		OthelloGame temp = new OthelloGame(boardState, current, false, false);
		boolean[][] possibilities = temp.getViableMoves(); //possible states after a move
		boolean leaf = true;

		for (boolean[] row : possibilities) //checks through all the possibilites and see if its false
			for (boolean spot : row)
				if (spot) leaf = false;

		if ((depth == 0) || (leaf)) //if its a leaf node, return because it will not have
			return score(boardState); //any children

		if (maximizing) { //if true, it WHO'S TURN IS THIS AGAIN?

			int bestValue = -1000;

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (possibilities[i][j]) {

						int newValue = minimax(dummy(boardState[i][j]), depth-1, false);
						//a new state that creates that the board would look like after a certain move

						//finds which of the two values have more of the tiles we want maximized
						if (newValue > bestValue) {

							bestValue = newValue;
							path[depth-1] = (j + " " + (7-i));

						}
					}
				}
			}
			return bestValue; //return state that provides most tiles we want

		}
		else { //WHOS TURN IS THIS???? The minimizing player's turn

			int bestValue = 1000;

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (possibilities[i][j]) {

						int newValue = minimax(dummy(boardState[i][j]), depth-1, true);
						//a new state that creates that the board would look like after a certain move

						//finds which of the two values have more of the tiles we want maximized
						if (newValue < bestValue) {

							bestValue = newValue;
							path[depth-1] = (j + " " + (7-i));

						}
					}
				}
			}
			return bestValue;
			//returns state that provides the most tiles we want
		}

	}

	public int score(Tile[][] boardState) {

		// returns the number of blue tiles on the board
		return -1;

	}


// ** Note: Score function is literally just the number of black tiles in the boardState


}
