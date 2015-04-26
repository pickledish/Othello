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
	
	public int[][] bestMove(boolean[][] availableMoves)
	{
		int[][] chooseThis = 0;
		for(int i=0;i<availableMoves.length;i++)
		{
			for(int j=0;j<availableMoves.length;i++)
			{
				if(availableMoves[i][j]==true)
				{
					//if corner move is avaiblae, automatically choose corner
					if([i][j]==[0][7]==true){
						chooseThis = availableMoves[0][7];
					}
					else if([i][j]==[0][0]==true){
						chooseThis = availableMoves[0][0];
					}
					else if([i][j]==[7][7]==true){
						chooseThis = availableMoves[7][7];
					}
					else if([i][j]==[7][0]==true){
						chooseThis = availableMoves[7][0];
					}
					
					//if a the area around corners is avaible, skip it
					
					else if([i][j]==[0][6]==true){
						return;
					}
					else if([i][j]==[0][1]==true){
						return;
					}
					else if([i][j]==[1][0]==true){
						return;
					}
					else if([i][j]==[1][1]==true){
						return;
					}
					else if([i][j]==[1][6]==true){
						return;
					}
					else if([i][j]==[1][7]==true){
						return;
					}
					else if([i][j]==[6][0]==true){
						return;
					}
					else if([i][j]==[6][1]==true){
						return;
					}
					else if([i][j]==[7][1]==true){
						return;
					}
					else if([i][j]==[6][7]==true){
						return;
					}
					else if([i][j]==[6][6]==true){
						return;
					}
					else if([i][j]==[7][6]==true){
						return;
					}
					//now you pick the spot that will open up the most moves 
					//for you not allow the oppent to gain a corner
					
					/*

					** Mobility Method **
					
					for(int i = 0; i<list of possible moves;i++)
						{

							insert to priority heap: available moves created
							after choosing a particular square

						}
					return top choice
				
				
				} 
				 **/
					
				}
				else
				{
					return; 
					// if the move is not available skip over it
				}
			}
		}
		
		return chooseThis;
	}
	
}