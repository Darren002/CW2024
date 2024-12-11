package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents the shield image displayed in the game.
 * The shield can be shown or hidden based on the boss's status.
 * Extends {@code ImageView} and manages the shield's image and visibility.
 */
public class ShieldImage extends ImageView {

	/** The path to the shield image. */
	private static final String IMAGE_NAME = "/images/shield.png";

	/** The size of the shield image. */
	private static final int SHIELD_SIZE = 200;

	/**
	 * Constructs a ShieldImage at the specified X and Y positions.
	 * Initializes the shield image with a default size and sets its visibility to false.
	 *
	 * @param xPosition the X position of the shield image
	 * @param yPosition the Y position of the shield image
	 */
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm()));
		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	/**
	 * Displays the shield image, making it visible on the screen.
	 */
	public void showShield() {
		this.setVisible(true);
	}

	/**
	 * Hides the shield image, making it invisible on the screen.
	 */
	public void hideShield() {
		this.setVisible(false);
	}
}
