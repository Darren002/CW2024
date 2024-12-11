package com.example.demo;

/**
 * An interface representing destructible objects in the game.
 * Any class implementing this interface must define how the object takes damage
 * and how it is destroyed.
 */
public interface Destructible {

	/**
	 * Causes the object to take damage. The specific implementation depends on the object.
	 */
	void takeDamage();

	/**
	 * Destroys the object. The specific implementation depends on the object.
	 */
	void destroy();
}
