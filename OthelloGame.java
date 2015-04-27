import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Brandon on 4/21/15.
 *
 * The game class! Manages each in-game part of the project
 *
 * STAYS TOTALLY SEPARATE FROM THE GAMEWINDOW CLASS / ALL GUI ELEMENTS PLS
 *
 * But yeah, logic stuff goes here. Each method should be explained well enough, right?
 */

public class OthelloGame {

	// BoardState is the game board, and flip keeps track of whose turn it is! Every click --> flip changes
	Tile[][] boardState;
	String current = "red";

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
		
		rowColorChanger(pressed);

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

		double slope = Math.abs((y2 - y1) / (double) (x2 - x1));

		if (((slope == 0) || (slope == Double.POSITIVE_INFINITY) || (slope == 1) || (slope == -1))
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

			for (int i = start.getx() + 1; i < end.getx(); i++)
				returner.add(boardState[y1][i]);



		} else if (slope == Double.POSITIVE_INFINITY) {

			Tile start = (y1 < y2) ? tile1 : tile2;
			Tile end = (y1 < y2) ? tile2 : tile1;
			assert (x1 == x2);

			for (int i = start.gety() + 1; i < end.gety(); i++)
				returner.add(boardState[i][x1]);



		// Can have a smaller x OR a smaller y
		} else if (slope == 1) {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = 1; i < (end.getx() - start.getx()); i++)
				returner.add(boardState[start.gety() + i][start.getx() + i]);


		} else {

			Tile start = (x1 < x2) ? tile1 : tile2;
			Tile end = (x1 < x2) ? tile2 : tile1;

			for (int i = 1; i < (end.getx() - start.getx()); i++)
				returner.add(boardState[start.gety() - i][start.getx() + i]);

		}

		return returner;

	}

	public boolean tilesAreFilled(Tile tile1, Tile tile2, double slope) {

		LinkedList<Tile> between = getBetweenTiles(tile1, tile2, slope);
		boolean notAllTheSame = false;

		for (Tile current : between) {

			if (current.getColor() == null) return false;
			if (!current.getColor().equals(tile2.getColor())) notAllTheSame = true;

		}

		return notAllTheSame;

	}
	
	public int[][] bestMove()
	{
		int[][] chooseThis = 0; //this will be the move chosen
		boolean[][] availableMoves = getViableMoves(); //finds all possible moves
		
		//copy game board
		boolean[][] tempBoard = new boolean [availableMove.length][availableMove.length];
		
		for(int i=0;i<availableMoves.length;i++) //iterate through all squares on the board
		{
			for(int j=0;j<availableMoves.length;i++)
			{
				
				if(availableMoves[i][j]==true)
				{
					
					//make a possible prospective move
						//rowColorChanger(Tile pressed)
					//simulate the opponent's possible counter move
					
					}
					
					//if you can complete a row, pick that
					//if the avaiblae spot is in between two opponents, choose it, they cant change you
					
				//after tree of simulated moves are made, second recursive function
				//transverse tree backwards, calculating move with best score
					
					
				}
				else
				{
					return; 
					// if the move is not available skip over it
				}
			}
		}
	
				//if corner move is avaiblae, automatically choose corner
				if([i][j]==[0][7]){
					chooseThis = availableMoves[0][7];
				}
				else if([i][j]==[0][0]){
					chooseThis = availableMoves[0][0];
				}
				else if([i][j]==[7][7]){
					chooseThis = availableMoves[7][7];
				}
				else if([i][j]==[7][0]){
					chooseThis = availableMoves[7][0];
				}
				
				//if a the area around corners is avaible, skip it
				//would be a problem is these areas are the only ones left available
				
				else if([i][j]==[0][6]){
					return;
				}
				else if([i][j]==[0][1]){
					return;
				}
				else if([i][j]==[1][0]){
					return;
				}
				else if([i][j]==[1][1]){
					return;
				}
				else if([i][j]==[1][6]){
					return;
				}
				else if([i][j]==[1][7]){
					return;
				}
				else if([i][j]==[6][0]){
					return;
				}
				else if([i][j]==[6][1]){
					return;
				}
				else if([i][j]==[7][1]){
					return;
				}
				else if([i][j]==[6][7]){
					return;
				}
				else if([i][j]==[6][6]){
					return;
				}
				else if([i][j]==[7][6]){
					return;
		
		return chooseThis;
	}
	
}
