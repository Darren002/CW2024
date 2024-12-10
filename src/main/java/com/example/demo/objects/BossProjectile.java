package com.example.demo.objects;

import javafx.scene.shape.Rectangle;

public class BossProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;
	private static final int INITIAL_X_POSITION = 950;
	private final Rectangle hitbox;

	public BossProjectile(double initialYPos) {super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
	hitbox= new Rectangle(INITIAL_X_POSITION,initialYPos,55,40);
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
		updateHitbox();
	}
	
	@Override
	public void updateActor() {
		updatePosition();
	}

	private void updateHitbox(){
		hitbox.setX(getLayoutX()+getTranslateX());
		hitbox.setY(getLayoutY()+getTranslateY());
	}
	@Override
	public Rectangle getHitbox(){
		return hitbox;
	}

}
