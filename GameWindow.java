/**
 * Created by Brandon on 4/18/15.
 *
 * The GUI class! Handles all visual parts of the project
 *
 * Makes a bunch of GameButtons and throws them onto an 8x8 board on the screen GUI
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class GameWindow {

	JFrame window = new JFrame("Othello");
	GameButton[][] buttons;
	JLabel redCount;
	JLabel blueCount;
	JLabel current;
	OthelloGame game;
	boolean goFirst = false;

	public GameWindow(Boolean first) {
		if (first)
			goFirst = true;
	}

	// Makes the window, using GridBagLayout, and adds all tiles and labels to it
	public void drawWindow() {

		window.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JPanel board = new JPanel();
		board.setLayout(new GridBagLayout());

		buttons = new GameButton[8][8];

		// The button adding loop! Also adds the actionListeners so they can be clicked
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j] = new GameButton("", j, i);
				buttons[i][j].setPreferredSize(new Dimension(60, 60));

				buttons[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {

						GameButton source = (GameButton) actionEvent.getSource();
						game.tileClicked(source);
						refreshBoard();
					}
				});

				c.gridy = i;
				c.gridx = j;
				c.insets = new Insets(5, 5, 5, 5);
				board.add(buttons[i][j], c);
			}
		}

		// int ai = JOptionPane.showConfirmDialog(null, "Do you want an AI to play the other side?",
		//		"Artificial Intelligence Option", JOptionPane.YES_NO_OPTION);

		int ai = -3;

		game = (ai == JOptionPane.YES_OPTION) ?
				new OthelloGame(buttons, "blue", true, goFirst) : new OthelloGame(buttons, "blue", false, goFirst);

		game.setUp();

		// Makes sure that all buttons that should be disabled, ARE disabled for the first opening
		boolean viable[][] = game.getViableMoves();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!viable[i][j]) buttons[i][j].setEnabled(false);
			}
		}

		// Adds a bar at the bottom that contains counts for red and blue tiles, as well as a board reset button
		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());

		redCount = new JLabel("Red Tiles: 2\t\t");
		redCount.setFont(new Font("Serif", Font.PLAIN, 18));
		blueCount = new JLabel("Blue Tiles: 2\t\t");
		blueCount.setFont(new Font("Serif", Font.PLAIN, 18));

		JButton reset = new JButton("Reset Game");
		reset.setFont(new Font("Serif", Font.PLAIN, 14));
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				game.setUp();
				refreshBoard();
				redCount.setText("Red Tiles: 2\t\t\t");
				blueCount.setText("Blue Tiles: 2\t\t\t");
			}
		});

		current = new JLabel("Current move: blue");
		current.setFont(new Font("Serif", Font.PLAIN, 18));

		c.gridx = 0;
		options.add(redCount, c);
		c.gridx++;
		options.add(blueCount, c);
		c.gridx++;
		options.add(reset, c);
		c.gridx++;
		options.add(current, c);


		c.gridy = 0;
		window.add(board, c);
		c.gridy++;
		window.add(options, c);

		window.setBackground(Color.LIGHT_GRAY);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);

		refreshBoard();

	}

	// For use after a button is pressed! Updates the board to represent all newly enabled/disabled buttons
	public void refreshBoard() {

		boolean viable[][] = game.getViableMoves();
		int toggled = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!buttons[i][j].getToggled()) buttons[i][j].setEnabled(false);
				else toggled++;
				if (viable[i][j]) buttons[i][j].setEnabled(true);
			}
		}


		// Updates the counts of the red and blue labels, and current move
		redCount.setText("Red Tiles: " + game.getTiles("red") + "\t\t\t");
		blueCount.setText("Blue Tiles: " + game.getTiles("blue") + "\t\t\t");
		current.setText(("Current move: " + game.current));

		// Checks to see if every button on the board is toggled. If it is, the game is over
		if (toggled == 64) {
			JOptionPane.showMessageDialog(null, "Game is over!", "Done", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		if (game.getTiles("red") == 0) {
			JOptionPane.showMessageDialog(null, "Game is over, Blue wins!", "Done", JOptionPane.PLAIN_MESSAGE);
			System.out.println("Blue Wins");
			System.exit(0);
		}
		if (game.getTiles("blue") == 0) {
			JOptionPane.showMessageDialog(null, "Game is over, Red wins!", "Done", JOptionPane.PLAIN_MESSAGE);
			System.out.println("Red Wins");
			System.exit(0);
		}

		window.pack();

		System.out.print("Enter move: ");

		Scanner input = new Scanner(System.in);
		parseAndMove(input.nextLine());

	}

	// Takes in a string of 2 integers and makes a move based on them
	public void parseAndMove(String move) {

		int x = Integer.parseInt(move.substring(0,1));
		int y = Integer.parseInt(move.substring(2,3));

		boolean[][] viable = game.getViableMoves();

		if (viable[7-y][x]) game.tileClicked(buttons[7-y][x]);
		else JOptionPane.showMessageDialog(null, "Move not possible", "No", JOptionPane.PLAIN_MESSAGE);

		refreshBoard();

	}
}
