package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a display of hearts (typically used for showing player health).
 * The hearts are displayed as images in a horizontal layout, and hearts can be removed when the player loses health.
 */
public class HeartDisplay {

	/** The image name for the heart icon. */
	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";

	/** The height of the heart image. */
	private static final int HEART_HEIGHT = 50;

	/** The index of the first heart in the display. */
	private static final int INDEX_OF_FIRST_ITEM = 0;

	/** The container holding the hearts. */
	private HBox container;

	/** The X position of the heart container. */
	private double containerXPosition;

	/** The Y position of the heart container. */
	private double containerYPosition;

	/** The number of hearts to display. */
	private int numberOfHeartsToDisplay;

	/**
	 * Constructs a HeartDisplay with the specified position and number of hearts.
	 * Initializes the container and populates it with heart images.
	 *
	 * @param xPosition the X position of the heart container
	 * @param yPosition the Y position of the heart container
	 * @param heartsToDisplay the number of hearts to display
	 */
	public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.numberOfHeartsToDisplay = heartsToDisplay;
		initializeContainer();
		initializeHearts();
	}

	/**
	 * Initializes the container for the hearts and sets its position.
	 */
	private void initializeContainer() {
		container = new HBox();
		container.setLayoutX(containerXPosition);
		container.setLayoutY(containerYPosition);
	}

	/**
	 * Initializes the hearts in the container based on the number of hearts to display.
	 * Each heart is represented by an {@code ImageView}.
	 */
	private void initializeHearts() {
		for (int i = 0; i < numberOfHeartsToDisplay; i++) {
			ImageView heart = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
			heart.setFitHeight(HEART_HEIGHT);
			heart.setPreserveRatio(true);
			container.getChildren().add(heart);
		}
	}

	/**
	 * Removes the first heart from the display.
	 * Typically used when the player loses a heart.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	/**
	 * Returns the container holding the hearts for display.
	 *
	 * @return the {@code HBox} containing the hearts
	 */
	public HBox getContainer() {
		return container;
	}
}
