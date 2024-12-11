package com.example.demo.objects;

/**
 * An abstract representation of a fighter plane in the game.
 * Extends {@code ActiveActorDestructible} and includes health tracking
 * and projectile firing capabilities.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

	/** The health of the fighter plane. */
	private int health;

	/**
	 * Constructs a FighterPlane with the specified parameters.
	 *
	 * @param imageName   the name of the image representing the fighter plane
	 * @param imageHeight the height of the fighter plane image
	 * @param initialXPos the initial X-axis position of the fighter plane
	 * @param initialYPos the initial Y-axis position of the fighter plane
	 * @param health      the initial health of the fighter plane
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
	}

	/**
	 * Fires a projectile from the fighter plane.
	 * This method must be implemented by subclasses.
	 *
	 * @return the fired {@code ActiveActorDestructible} projectile
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Reduces the health of the fighter plane by one.
	 * Destroys the plane if health reaches zero.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Calculates the X-axis position of a projectile based on an offset.
	 *
	 * @param xPositionOffset the offset to apply to the X-axis position
	 * @return the calculated X-axis position
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y-axis position of a projectile based on an offset.
	 *
	 * @param yPositionOffset the offset to apply to the Y-axis position
	 * @return the calculated Y-axis position
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the health of the fighter plane is zero.
	 *
	 * @return {@code true} if health is zero, {@code false} otherwise
	 */
	private boolean healthAtZero() {
		return health == 0;
	}

	/**
	 * Returns the current health of the fighter plane.
	 *
	 * @return the current health value
	 */
	public int getHealth() {
		return health;
	}
}
