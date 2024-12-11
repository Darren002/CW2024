package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

/**
 * Represents a projectile fired by the user's plane in the game.
 * Extends {@code Projectile} and includes a hitbox for collision detection.
 */
public class UserProjectile extends Projectile {

	/** The image name for the user projectile. */
	private static final String IMAGE_NAME = "userfire.png";

	/** The height of the projectile image. */
	private static final int IMAGE_HEIGHT = 125;

	/** The horizontal velocity of the projectile. */
	private static final int HORIZONTAL_VELOCITY = 15;

	/** The hitbox for collision detection. */
	private final Rectangle hitbox;

	/**
	 * Constructs a UserProjectile at the specified initial X and Y positions.
	 *
	 * @param initialXPos the initial X-axis position of the projectile
	 * @param initialYPos the initial Y-axis position of the projectile
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		hitbox = new Rectangle(initialXPos, initialYPos, 30, 37);
	}

	/**
	 * Updates the position of the projectile by moving it horizontally
	 * and updates its hitbox.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitbox();
	}

	/**
	 * Updates the actor's state, including its position and hitbox.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Updates the hitbox position to align with the projectile's current location.
	 */
	private void updateHitbox() {
		hitbox.setX(getLayoutX() + getTranslateX());
		hitbox.setY(getLayoutY() + getTranslateY());
	}

	/**
	 * Returns the hitbox of the projectile for collision detection.
	 *
	 * @return the {@code Rectangle} representing the projectile's hitbox
	 */
	@Override
	public Rectangle getHitbox() {
		return hitbox;
	}
}
