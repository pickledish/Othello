import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


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

	// BoardState is the game board, and flip keeps track of whose turn it is! Every click --> flip changes
	Tile[][] boardState;
	String current = "red";
	TreeMap<Tile, Integer> possibleMove=null;
	boolean[][] availableMoves;

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
			rowColorChanger(pressed);
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
								if (current.equals(tile.getColor())) {
									// If they're in the same line and > 1 distance apart
									double slope = inSameLine(boardState[i][j], tile);
									if (slope != 0.1)
										// Test here to see if every piece between the two is filled & not all same)
										if (tilesAreFilled(boardState[i][j], tile, slope))
											returner[i][j] = true;
								}

		return returner;
	}

	// Checks to see if 2 tiles are in the same vertical, horizontal, or (perfect) diagonal line
	// Also checks to see if they're more than 1 tile apart
	// If both are true, then returns true!
	public double inSameLine(Tile tile1, Tile tile2) {

		int x1 = tile1.getx();
		int y1 = tile1.gety();
		int x2 = tile2.getx();
		int y2 = tile2.gety();

		double slope = (y2 - y1) / (double) (x2 - x1);

		if (((slope == 0) || (slope == Double.POSITIVE_INFINITY) || (slope == Double.NEGATIVE_INFINITY) || (slope == 1) || (slope == -1))
				&& ((Math.abs(x1 - x2) > 1) || (Math.abs(y2 - y1) > 1)))
			return slope;

		else return 0.1;

	}

	//For making colors change appropriately in all directions
	public void rowColorChanger(Tile pressed) {
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
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
					boardState[currY][currX].setColor(currColor);
					currX--;
					currY--;
				}
			}
		}
		currY = myY;
		currX = myX;

	}

	public LinkedList<Tile> getBetweenTiles(Tile tile1, Tile tile2, double slope) {

		LinkedList<Tile> returner = new LinkedList<Tile>();
		int x1 = tile1.getx();
		int y1 = tile1.gety();
		int x2 = tile2.getx();
		int y2 = tile2.gety();

		if (slope == 0) {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;
			assert (y1 == y2);

			for (int i = start.getx() + 1; i < end.getx(); i++) {

				// If the current color and the first tile next to the possible viable tile are the same, no
				int checkNum = (tile1 == start) ? tile1.getx() + 1 : tile1.getx() - 1;
				if ((i == checkNum) && (boardState[y1][i].getColor() != null) && (boardState[y1][i].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[y1][i]);

			}



		} else if ((slope == Double.POSITIVE_INFINITY) || (slope == Double.NEGATIVE_INFINITY)) {

			Tile start = (y1 < y2) ? tile1 : tile2;
			Tile end = (y1 < y2) ? tile2 : tile1;
			assert (x1 == x2);

			for (int i = start.gety() + 1; i < end.gety(); i++) {

				int checkNum = (tile1 == start) ? tile1.gety() + 1 : tile1.gety() - 1;
				if ((i == checkNum) && (boardState[i][x1].getColor() != null) && (boardState[i][x1].getColor().equals(current)))
					returner.add(null);
				else
					returner.add(boardState[i][x1]);
			}

			// Can have a smaller x OR a smaller y
		} else if (slope == 1) {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = 1; i < (end.getx() - start.getx()); i++) {

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
				
				if(availableMoves[i][j]==true)
				{
					
					//make a possible prospective move
						rowColorChanger(boardState[i][j]);
						possibleMove.put(boardState[i][j], 0);
						
					//simulate the opponent's possible counter move
						boolean[][] temp = getViableMoves();
						this.possibleMove(); //THIS IS RECURSIVE LETS HOPE IT DOESNT CRASH
						
					}
				//after tree of simulated moves are made, second recursive function
				//transverse tree backwards, calculating move with best score
					
					
				}
		
		}
		return;
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
	
}
