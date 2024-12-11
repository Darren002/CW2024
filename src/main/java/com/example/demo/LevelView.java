package com.example.demo;

import javafx.scene.Group;

/**
 * Represents the view of a game level, displaying elements such as the heart display, win image, and game over image.
 * It manages the visual aspects of the level, including showing and updating hearts and game outcome screens.
 */
public class LevelView {

	/** The X position for the heart display. */
	private static final double HEART_DISPLAY_X_POSITION = 5;

	/** The Y position for the heart display. */
	private static final double HEART_DISPLAY_Y_POSITION = 25;

	/** The X position for the win image. */
	private static final int WIN_IMAGE_X_POSITION = 355;

	/** The Y position for the win image. */
	private static final int WIN_IMAGE_Y_POSITION = 175;

	/** The X position for the loss screen image. */
	private static final int LOSS_SCREEN_X_POSITION = -160;

	/** The Y position for the loss screen image. */
	private static final int LOSS_SCREEN_Y_POSISITION = -375;

	/** The root group of the scene, which holds all the visual elements. */
	private final Group root;

	/** The image displayed when the player wins. */
	private final WinImage winImage;

	/** The image displayed when the game is over. */
	private final GameOverImage gameOverImage;

	/** The heart display showing the player's remaining health. */
	private final HeartDisplay heartDisplay;

	/**
	 * Constructs a LevelView to manage and display the visual elements of the level.
	 * Initializes the heart display, win image, and game over image.
	 *
	 * @param root the root group of the scene
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
	}

	/**
	 * Displays the heart display on the screen.
	 */
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	/**
	 * Displays the win image and shows the win animation.
	 */
	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	/**
	 * Displays the game over image on the screen.
	 */
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	/**
	 * Removes hearts from the display to reflect the player's remaining health.
	 *
	 * @param heartsRemaining the number of hearts remaining
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}
}
