package com.example.demo.objects;

/**
 * An abstract representation of a projectile in the game.
 * Extends {@code ActiveActorDestructible} and provides the base functionality for projectiles.
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a Projectile with the specified parameters.
	 *
	 * @param imageName   the name of the image representing the projectile
	 * @param imageHeight the height of the projectile image
	 * @param initialXPos the initial X-axis position of the projectile
	 * @param initialYPos the initial Y-axis position of the projectile
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Destroys the projectile upon taking damage.
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	/**
	 * Updates the position of the projectile.
	 * This method must be implemented by subclasses to define specific movement logic.
	 */
	@Override
	public abstract void updatePosition();
}
