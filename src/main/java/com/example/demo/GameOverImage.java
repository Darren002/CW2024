package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the "Game Over" image displayed when the game ends.
 * Extends {@code ImageView} and sets the image for the "Game Over" screen.
 */
public class GameOverImage extends ImageView {

	/** The path to the "Game Over" image. */
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	/**
	 * Constructs a GameOverImage at the specified X and Y positions.
	 * The image is loaded from the specified resource path and displayed at the given position.
	 *
	 * @param xPosition the X-axis position to place the "Game Over" image
	 * @param yPosition the Y-axis position to place the "Game Over" image
	 */
	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}
}
