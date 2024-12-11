package com.example.demo.objects;

import com.example.demo.Destructible;
import javafx.scene.shape.Rectangle;

/**
 * Abstract base class for destructible active actors.
 * Extends {@link ActiveActor} and implements {@link Destructible} to add functionality
 * for handling destruction and hit detection.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	/**
	 * Indicates whether the actor has been destroyed.
	 */
	private boolean isDestroyed;

	/**
	 * Constructs an {@code ActiveActorDestructible} with the specified image, size, and initial position.
	 *
	 * @param imageName    the name of the image file to use for this actor
	 * @param imageHeight  the desired height of the image (aspect ratio is preserved)
	 * @param initialXPos  the initial X position of the actor
	 * @param initialYPos  the initial Y position of the actor
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		isDestroyed = false;
	}

	/**
	 * Updates the position of the actor. Subclasses must provide the implementation
	 * to define how the position is updated.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the state of the actor. Subclasses must provide the implementation
	 * to define how the actor's state changes.
	 */
	public abstract void updateActor();

	/**
	 * Applies damage to the actor. Subclasses must provide the implementation
	 * to define how the actor responds to damage.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Destroys the actor by setting its destroyed state to {@code true}.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}

	/**
	 * Sets the destroyed state of the actor.
	 *
	 * @param isDestroyed the new destroyed state of the actor
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Checks whether the actor has been destroyed.
	 *
	 * @return {@code true} if the actor is destroyed; {@code false} otherwise
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}

	/**
	 * Retrieves the hitbox of the actor. Subclasses must provide the implementation
	 * to define the shape and size of the hitbox.
	 *
	 * @return the {@link Rectangle} representing the actor's hitbox
	 */
	public abstract Rectangle getHitbox();
}
