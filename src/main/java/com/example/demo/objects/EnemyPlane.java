package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

/**
 * Represents an enemy plane in the game.
 * This class extends {@code FighterPlane} and includes a hitbox and functionality for firing projectiles.
 */
public class EnemyPlane extends FighterPlane {

	/** The image name for the enemy plane. */
	private static final String IMAGE_NAME = "enemyplane.png";

	/** The height of the enemy plane image. */
	private static final int IMAGE_HEIGHT = 150;

	/** The horizontal velocity of the enemy plane. */
	private static final int HORIZONTAL_VELOCITY = -6;

	/** X-axis offset for the position of fired projectiles. */
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;

	/** Y-axis offset for the position of fired projectiles. */
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;

	/** Initial health of the enemy plane. */
	private static final int INITIAL_HEALTH = 1;

	/** Probability of firing a projectile during each update. */
	private static final double FIRE_RATE = 0.01;

	/** Offset for the hitbox's Y-axis position. */
	private static final double HITBOX_OFFSET_Y = 20;

	/** The hitbox for collision detection. */
	private final Rectangle hitbox;

	/**
	 * Constructs an EnemyPlane with specified initial X and Y positions.
	 *
	 * @param initialXPos the initial X-axis position of the enemy plane
	 * @param initialYPos the initial Y-axis position of the enemy plane
	 */
	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
		hitbox = new Rectangle(initialXPos, initialYPos, 110, 30);
	}

	/**
	 * Updates the position of the enemy plane by moving it horizontally
	 * and updating its hitbox.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitbox();
	}

	/**
	 * Fires a projectile from the enemy plane with a certain probability.
	 *
	 * @return a new {@code EnemyProjectile} if fired; otherwise {@code null}
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return new EnemyProjectile(projectileXPosition, projectileYPosition);
		}
		return null;
	}

	/**
	 * Updates the actor's state, including its position and hitbox.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Updates the hitbox position to align with the enemy plane's current location.
	 */
	private void updateHitbox() {
		hitbox.setX(getLayoutX() + getTranslateX());
		hitbox.setY(getLayoutY() + getTranslateY() + HITBOX_OFFSET_Y);
	}

	/**
	 * Returns the hitbox of the enemy plane for collision detection.
	 *
	 * @return the {@code Rectangle} representing the enemy plane's hitbox
	 */
	@Override
	public Rectangle getHitbox() {
		return hitbox;
	}
}
