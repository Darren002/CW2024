package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

/**
 * Represents a projectile launched by the boss in the game.
 * This class extends {@code Projectile} and includes a hitbox for collision detection.
 */
public class BossProjectile extends Projectile {

	/** The image name for the boss projectile. */
	private static final String IMAGE_NAME = "fireball.png";

	/** The height of the projectile image. */
	private static final int IMAGE_HEIGHT = 75;

	/** The horizontal velocity of the projectile. */
	private static final int HORIZONTAL_VELOCITY = -15;

	/** The initial X-axis position of the projectile. */
	private static final int INITIAL_X_POSITION = 950;

	/** The hitbox for collision detection. */
	private final Rectangle hitbox;

	/**
	 * Constructs a BossProjectile at a specified initial Y-axis position.
	 *
	 * @param initialYPos the initial Y-axis position of the projectile
	 */
	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
		hitbox = new Rectangle(INITIAL_X_POSITION, initialYPos, 55, 40);
	}

	/**
	 * Updates the position of the projectile by moving it horizontally
	 * and updating its hitbox.
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
	 * Updates the hitbox position to match the projectile's current location.
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
