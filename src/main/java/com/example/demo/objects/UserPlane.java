package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

/**
 * Represents the user's fighter plane in the game.
 * Extends {@code FighterPlane} and includes controls for movement, firing projectiles,
 * and tracking the number of kills.
 */
public class UserPlane extends FighterPlane {

	/** The image name for the user plane. */
	private static final String IMAGE_NAME = "userplane.png";

	/** The upper boundary for the Y-axis movement. */
	private static final double Y_UPPER_BOUND = 25;

	/** The lower boundary for the Y-axis movement. */
	private static final double Y_LOWER_BOUND = 680.0;

	/** The initial X-axis position of the user plane. */
	private static final double INITIAL_X_POSITION = 5.0;

	/** The initial Y-axis position of the user plane. */
	private static final double INITIAL_Y_POSITION = 300.0;

	/** The height of the user plane image. */
	private static final int IMAGE_HEIGHT = 150;

	/** The vertical velocity of the user plane. */
	private static final int VERTICAL_VELOCITY = 8;

	/** The X-axis position of the projectile when fired. */
	private static final int PROJECTILE_X_POSITION = 110;

	/** The Y-axis offset for the projectile when fired. */
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

	/** The X-axis offset for the hitbox. */
	private static final double HITBOX_OFFSET_X = 10;

	/** The Y-axis offset for the hitbox. */
	private static final double HITBOX_OFFSET_Y = 60;

	/** Multiplier to control vertical velocity. */
	private int velocityMultiplier;

	/** The number of kills made by the user plane. */
	private int numberOfKills;

	/** The hitbox for collision detection. */
	private final Rectangle hitbox;

	/**
	 * Constructs a UserPlane with the specified initial health.
	 *
	 * @param initialHealth the initial health of the user plane
	 */
	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
		hitbox = new Rectangle(INITIAL_X_POSITION, INITIAL_Y_POSITION, 100, 30);
	}

	/**
	 * Updates the position of the user plane, ensuring it stays within bounds,
	 * and updates its hitbox.
	 */
	@Override
	public void updatePosition() {
		if (isMoving()) {
			double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}
		}
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
	 * Fires a projectile from the user plane.
	 *
	 * @return a new {@code UserProjectile} representing the fired projectile
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	/**
	 * Checks if the user plane is currently moving.
	 *
	 * @return {@code true} if moving, {@code false} otherwise
	 */
	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	/** Moves the user plane upwards by setting the velocity multiplier to -1. */
	public void moveUp() {
		velocityMultiplier = -1;
	}

	/** Moves the user plane downwards by setting the velocity multiplier to 1. */
	public void moveDown() {
		velocityMultiplier = 1;
	}

	/** Stops the user plane's movement by setting the velocity multiplier to 0. */
	public void stop() {
		velocityMultiplier = 0;
	}

	/**
	 * Returns the number of kills made by the user plane.
	 *
	 * @return the number of kills
	 */
	public int getNumberOfKills() {
		return numberOfKills;
	}

	/** Increments the kill count of the user plane by one. */
	public void incrementKillCount() {
		numberOfKills++;
	}

	/**
	 * Updates the hitbox position to align with the user plane's current location.
	 */
	private void updateHitbox() {
		hitbox.setX(getLayoutX() + getTranslateX() + HITBOX_OFFSET_X);
		hitbox.setY(getLayoutY() + getTranslateY() + HITBOX_OFFSET_Y);
	}

	/**
	 * Returns the hitbox of the user plane for collision detection.
	 *
	 * @return the {@code Rectangle} representing the user plane's hitbox
	 */
	@Override
	public Rectangle getHitbox() {
		return hitbox;
	}
}
