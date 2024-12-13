package com.example.demo.objects;

import com.example.demo.display.HealthBar;
import com.example.demo.display.ShieldImage;
import javafx.scene.shape.Rectangle;

import java.util.*;

/**
 * Represents the boss enemy in the game, which is a type of fighter plane.
 * The Boss has unique behaviors such as firing projectiles, activating shields,
 * and following a predefined move pattern.
 */
public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .04;
	private static final double BOSS_SHIELD_PROBABILITY = .002;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 100;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -50;
	private static final int Y_POSITION_LOWER_BOUND = 400;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private final ShieldImage shieldImage;
	private final Rectangle hitbox;
	private static final double HITBOX_OFFSET_Y=50;
	private final HealthBar healthBar;

	/**
	 * Constructs a new Boss with its initial properties, including
	 * position, health, movement pattern, shield, hitbox, and health bar.
	 */
	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
		hitbox= new Rectangle(INITIAL_X_POSITION,INITIAL_Y_POSITION,213,85);

		shieldImage = new ShieldImage(getLayoutX(),getLayoutY());
		healthBar = new HealthBar(200, 15);
		healthBar.setLayoutX(getLayoutX() + 5);
		healthBar.setTranslateY(40);

	}

	/** Updates the position of the boss based on its movement pattern. */
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
		updateHitbox();
	}

	/** Updates the boss's state, including position, shield, and health bar. */
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		updateHealthBar();
	}

	/**
	 * Fires a projectile if the boss's fire probability condition is met.
	 *
	 * @return A new BossProjectile object or null if no projectile is fired.
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}


	/** Reduces the boss's health when it takes damage, unless the shield is active. */
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
			if (getHealth() <= 0) {
				healthBar.updateHealth(0, HEALTH);
				healthBar.setVisible(false);
			}
		}
	}

	/** Initializes the movement pattern for the boss. */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/** Updates the boss's shield state and visual representation. */
	public void updateShield() {
		double offsetX= -65;
		double offsetY= 50;
		shieldImage.setLayoutX(getLayoutX() + getTranslateX() +offsetX);
		shieldImage.setLayoutY(getLayoutY() + getTranslateY()+offsetY);

		shieldImage.showShield();

		if (isShielded) {
			framesWithShieldActivated++;
			shieldImage.showShield();
		} else {
			shieldImage.hideShield();
			if (shieldShouldBeActivated()) activateShield();
		}
		if (shieldExhausted()) deactivateShield();
	}

	/**
	 * Retrieves the next movement direction based on the movement pattern.
	 *
	 * @return The vertical movement value.
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	/**
	 * Checks whether the boss should fire a projectile in the current frame.
	 *
	 * @return True if the boss fires a projectile, false otherwise.
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * Calculates the initial y-coordinate for a fired projectile.
	 *
	 * @return The y-coordinate for the projectile's starting position.
	 */
	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	/**
	 * Determines whether the boss's shield should be activated.
	 *
	 * @return True if the shield should activate, false otherwise.
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}


	/**
	 * Checks if the shield's active duration has been exhausted.
	 *
	 * @return True if the shield should deactivate, false otherwise.
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	/** Activates the boss's shield. */
	private void activateShield() {
		isShielded = true;
	}


	/** Deactivates the boss's shield and resets its active frame counter. */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}

	/**
	 * Gets the ShieldImage associated with the boss.
	 *
	 * @return The ShieldImage instance.
	 */
	public ShieldImage getShieldImage() {
		return shieldImage;
	}


	/** Updates the hitbox's position based on the boss's current layout and translation. */
	private void updateHitbox(){
		hitbox.setX(getLayoutX()+getTranslateX());
		hitbox.setY(getLayoutY()+getTranslateY()+HITBOX_OFFSET_Y);
	}

	/**
	 * Retrieves the hitbox for collision detection.
	 *
	 * @return The boss's hitbox as a Rectangle.
	 */
	@Override
	public Rectangle getHitbox(){
		return hitbox;
	}

	/** Updates the health bar's position and value. */
	private void updateHealthBar() {
		healthBar.setLayoutX(getLayoutX() + getTranslateX() + 5);
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + 10);
		healthBar.updateHealth(Math.max(getHealth(), 0), HEALTH);
	}

	/**
	 * Gets the HealthBar associated with the boss.
	 *
	 * @return The HealthBar instance.
	 */
	public HealthBar getHealthBar() {
		return healthBar;
	}

}