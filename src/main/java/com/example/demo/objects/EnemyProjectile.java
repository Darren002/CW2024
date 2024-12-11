package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

/**
 * Represents a projectile fired by an enemy in the game.
 * This class extends {@code Projectile} and includes a hitbox for collision detection.
 */
public class EnemyProjectile extends Projectile {

	/** The image name for the enemy projectile. */
	private static final String IMAGE_NAME = "enemyFire.png";

	/** The height of the projectile image. */
	private static final int IMAGE_HEIGHT = 50;

	/** The horizontal velocity of the projectile. */
	private static final int HORIZONTAL_VELOCITY = -10;

	/** The hitbox for collision detection. */
	private final Rectangle hitbox;

	/**
	 * Constructs an EnemyProjectile at the specified initial X and Y positions.
	 *
	 * @param initialXPos the initial X-axis position of the projectile
	 * @param initialYPos the initial Y-axis position of the projectile
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
		hitbox = new Rectangle(initialXPos, initialYPos, 25, IMAGE_HEIGHT / 2);
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
