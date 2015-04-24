/**
 * Created by Brandon on 4/18/15.
 *
 * The GUI class! Handles all visual parts of the project
 *
 * Makes a shitton of GameButtons and throws them onto an 8x8 board for all to see
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow {

	GameButton[][] buttons;
	OthelloGame game;

	public GameWindow() {}

	// Makes the window, using GridBagLayout cause best layout
	public void drawWindow() {

		JFrame window = new JFrame("Othello");
		window.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		buttons = new GameButton[8][8];

		// Big-ass button adding loop! Also adds the actionListeners so they can be clicked
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[i][j] = new GameButton("");
				buttons[i][j].setPreferredSize(new Dimension(80, 80));

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
				window.add(buttons[i][j], c);
			}
		}

		game = new OthelloGame(buttons);
		game.setUp();
		refreshBoard();

		// Makes sure that all buttons that should be disabled, ARE disabled for the first opening
		boolean viable[][] = game.getViableMoves();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!viable[i][j]) buttons[i][j].setEnabled(false);
			}
		}

		window.setBackground(Color.gray);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);

	}

	// For use after a button is pressed! Updates the board to represent all newly enabled/disabled buttons
	public void refreshBoard() {

		boolean viable[][] = game.getViableMoves();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (viable[i][j]) buttons[i][j].setEnabled(true);
			}
		}

	}
}
