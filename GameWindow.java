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

public class GameWindow {

	JFrame window = new JFrame("Othello");
	GameButton[][] buttons;
	JLabel redCount;
	JLabel blueCount;
	OthelloGame game;

	public GameWindow() {}

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

		game = new OthelloGame(buttons, "blue");
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
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				game.setUp();
				refreshBoard();
				redCount.setText("Red Tiles: 2\t\t\t");
				blueCount.setText("Blue Tiles: 2\t\t\t");
			}
		});

		c.gridx = 0;
		options.add(redCount, c);
		c.gridx++;
		options.add(blueCount, c);
		c.gridx++;
		options.add(reset, c);


		c.gridy = 0;
		window.add(board, c);
		c.gridy++;
		window.add(options, c);

		window.setBackground(Color.LIGHT_GRAY);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);

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


		// Updates the counts of the red and blue labels
		redCount.setText("Red Tiles: " + game.getTiles("red") + "\t\t\t");
		blueCount.setText("Blue Tiles: " + game.getTiles("blue") + "\t\t\t");

		// Checks to see if every button on the board is toggled. If it is, the game is over
		if (toggled == 64) {
			JOptionPane.showMessageDialog(null, "Game is over!", "Done", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}

		window.pack();

	}
}
