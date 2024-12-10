package com.example.demo.objects;

import com.example.demo.HealthBar;
import com.example.demo.ShieldImage;
import javafx.scene.shape.Rectangle;

import java.util.*;

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
	private static final int Y_POSITION_LOWER_BOUND = 475;
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

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
		updateHealthBar();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

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

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

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

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
	}


	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
	}

	public ShieldImage getShieldImage() {
		return shieldImage;
	}

	private void updateHitbox(){
		hitbox.setX(getLayoutX()+getTranslateX());
		hitbox.setY(getLayoutY()+getTranslateY()+HITBOX_OFFSET_Y);
	}
	@Override
	public Rectangle getHitbox(){
		return hitbox;
	}

	private void updateHealthBar() {
		healthBar.setLayoutX(getLayoutX() + getTranslateX() + 5);
		healthBar.setLayoutY(getLayoutY() + getTranslateY() + 10);
		healthBar.updateHealth(Math.max(getHealth(), 0), HEALTH);
	}

	public HealthBar getHealthBar() {
		return healthBar;
	}

}