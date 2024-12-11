package com.example.demo;

import javafx.scene.Group;

/**
 * Represents the view of the boss level, extending {@code LevelView} to include additional elements like the shield.
 * It manages the display of the boss level’s visual elements, including hearts and the shield image.
 */
public class LevelViewBoss extends LevelView {

	/** The X position for the shield image. */
	private static final int SHIELD_X_POSITION = 1150;

	/** The Y position for the shield image. */
	private static final int SHIELD_Y_POSITION = 500;

	/** The root group of the scene, which holds all the visual elements. */
	private final Group root;

	/** The shield image representing the boss’s shield. */
	private final ShieldImage shieldImage;

	/**
	 * Constructs a LevelViewBoss to manage and display the visual elements of the boss level.
	 * Initializes the heart display and shield image.
	 *
	 * @param root the root group of the scene
	 * @param heartsToDisplay the initial number of hearts to display
	 */
	public LevelViewBoss(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();
	}

	/**
	 * Adds the shield image to the root of the scene.
	 */
	private void addImagesToRoot() {
		root.getChildren().addAll(shieldImage);
	}
}
