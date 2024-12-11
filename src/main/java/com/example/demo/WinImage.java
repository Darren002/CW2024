package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "You Win" image displayed when the player wins the game.
 * Extends {@code ImageView} and manages the display of the win image.
 */
public class WinImage extends ImageView {

	/** The path to the "You Win" image. */
	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";

	/** The height of the "You Win" image. */
	private static final int HEIGHT = 500;

	/** The width of the "You Win" image. */
	private static final int WIDTH = 600;

	/**
	 * Constructs a WinImage at the specified X and Y positions.
	 * Initializes the "You Win" image with a default size and sets its visibility to false.
	 *
	 * @param xPosition the X position of the "You Win" image
	 * @param yPosition the Y position of the "You Win" image
	 */
	public WinImage(double xPosition, double yPosition) {
		this.setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		this.setVisible(false);
		this.setFitHeight(HEIGHT);
		this.setFitWidth(WIDTH);
		this.setLayoutX(xPosition + 100);
		this.setLayoutY(yPosition);
	}

	/**
	 * Displays the "You Win" image, making it visible on the screen.
	 */
	public void showWinImage() {
		this.setVisible(true);
	}
}
