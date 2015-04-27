import javax.swing.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by Brandon on 4/21/15.
 *
 * The game class! Manages each in-game part of the project
 *
 * STAYS TOTALLY SEPARATE FROM THE GAMEWINDOW CLASS / ALL GUI ELEMENTS PLS
 *
 * But yeah, logic stuff goes here. Each method should be explained well enough, right?
 *
 */

public class OthelloGame {

	// BoardState is the game board, and current keeps track of whose turn it is! Every click --> current changes
	Tile[][] boardState;
	String current;
	boolean ai;

	// These are Som's things but I'm sure they're important!
	TreeMap<Tile, Integer> possibleMove=null;
	boolean[][] availableMoves;

	// The constructor! Sets up the global variables and states for this game
	public OthelloGame(Tile[][] buttons, String c, boolean ai) {

		boardState = buttons;
		current = c;
		this.ai = ai;

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
			if (noMoves(getViableMoves())) {
				JOptionPane.showMessageDialog(null, "No Moves available! " + current + " skips turn!",
						"No moves!", JOptionPane.PLAIN_MESSAGE);
				current = current.equals("blue") ? "red" : "blue";

			}
			System.out.println(compMove());
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

	// Returns an array of booleans, each one corresponding to a button on the board (true = viable)
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
		return getActualViableMoves(returner);
	}

	// When given perimeter tiles, checks a million criteria to see if a tile is an actual possibility
	public boolean[][] getActualViableMoves(boolean[][] firstStep) {

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
											// Test to see if it's toggled yet, if it is, then it can't be a choice
											if (!boardState[i][j].getToggled())
												// If so, it's an actual choice!
												returner[i][j] = true;

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

	//For making colors change appropriately in all directions
	public int rowColorChanger(Tile pressed, boolean change) {
		int count = 0;
		String currColor = current;
		String otherColor = (current.equals("red")) ? "blue" : "red";

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

	public void possibleMove()
	{
		int[][] chooseThis = null; //this will be the move chosen
		availableMoves = getViableMoves(); //finds all possible moves
		
		//tree to hold possible outcome that can occur after you make a move
		possibleMove = new TreeMap<Tile, Integer>();
		
		for(int i=0;i<availableMoves.length;i++) //iterate through all squares on the board
		{
			for(int j=0;j<availableMoves.length;i++)
			{
				
				if(availableMoves[i][j])
				{
					
					//make a possible prospective move
						rowColorChanger(boardState[i][j], true);
						possibleMove.put(boardState[i][j], 0);
						
					//simulate the opponent's possible counter move
						boolean[][] temp = getViableMoves();
						this.possibleMove(); //THIS IS RECURSIVE LETS HOPE IT DOESNT CRASH
						
					}
				//after tree of simulated moves are made, second recursive function
				//transverse tree backwards, calculating move with best score
					
					
				}
		
		}
	}
	
	public Tile bestMove()
	{
		Tile best =null;
		int bestScore = 0;
		
		//iterates through list of next moves to find one with best next location
		
		for(Map.Entry<Tile, Integer> entry: possibleMove.entrySet())
		{
			Tile key = entry.getKey();
			Integer value = entry.getValue();
			
			//highest score given to corners
			if(key==boardState[0][0]||key==boardState[availableMoves.length][availableMoves.length]||
					key==boardState[availableMoves.length][0]||key==boardState[0][availableMoves.length])
			{
				value = 5;
			}
			
			//lowest score given to around right around corners
			else if(key==boardState[0][1]||key==boardState[availableMoves.length][availableMoves.length-1]||
					key==boardState[availableMoves.length][1]||key==boardState[0][availableMoves.length-1]||
					key==boardState[availableMoves.length-1][0]||key==boardState[availableMoves.length-1][1]||
					key==boardState[1][1]||key==boardState[1][0]||key==boardState[1][availableMoves.length-1]||
					key==boardState[1][availableMoves.length]||key==boardState[availableMoves.length-1][availableMoves.length-1]||
					key==boardState[availableMoves.length-1][availableMoves.length])
			{
				value = 1;
			}
			
			//Second highest score given to edges
			else if(key==boardState[0][2]||key==boardState[0][3]||key==boardState[0][4]||key==boardState[0][5]||
					key==boardState[2][0]||key==boardState[3][0]||key==boardState[4][0]||key==boardState[5][0]||
					key==boardState[availableMoves.length][2]||key==boardState[availableMoves.length][3]||key==boardState[availableMoves.length][4]||
					key==boardState[availableMoves.length][5]||key==boardState[2][availableMoves.length]||key==boardState[3][availableMoves.length]||
					key==boardState[4][availableMoves.length]||key==boardState[5][availableMoves.length])
			{
				value = 4;
			}
			
			//second lowest points to area right beyond area around corners
			//lowest score given to around right around corners
			else if(key==boardState[1][2]||key==boardState[1][3]||key==boardState[5][availableMoves.length-1]||
					key==boardState[1][4]||key==boardState[1][5]||key==boardState[4][availableMoves.length-1]||
					key==boardState[availableMoves.length-1][2]||key==boardState[availableMoves.length-1][3]||
					key==boardState[availableMoves.length-1][4]||key==boardState[availableMoves.length-1][5]||key==boardState[2][1]||
					key==boardState[3][1]||key==boardState[4][1]||key==boardState[5][1]||key==boardState[3][availableMoves.length-1])
			{
				value = 2;
			}
			else
			{
				value = 3; 
			}
			
		}
		
		//iterates through whole tree to finds tile with the highest score
		
		Map.Entry<Tile, Integer> max = null;
		
		for(Map.Entry<Tile, Integer> entry: possibleMove.entrySet())
		{
			if(max==null||entry.getValue().compareTo(max.getValue())>0)
			{
				max=entry;
				best=max.getKey();
			}
		}
		
		return best;
		//returns the tile with the highest score, and thus this is the square that will be chosen for next move
		
	}
	
	//returns best move for computer
	public String compMove() {
		int bestX = -1;
		int bestY = -1;
		int bestScore = -10000;
		int currScore = 0;

		boolean[][] available = getViableMoves();

		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++) {
				if (available[x][y]) {
					currScore = rowColorChanger(boardState[x][y], false);
					if (x == 0 || y == 0 || x == 7 || y == 7)
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
						currScore += 5;
					if (currScore > bestScore) {
						bestScore = rowColorChanger(boardState[x][y], false);
						bestX = y;
						bestY = 7-x;
						
					}
					
					
				}
			}
		return (bestX + " " + bestY);
	}
	
}
