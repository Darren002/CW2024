package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelOne extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/newbackground.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private Text killtracker;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			timeline.stop();
			loseGame();
		} else if (userHasReachedKillTarget()) {
			timeline.stop();

			showLevelClearedMessage();

			PauseTransition pause = new PauseTransition(Duration.seconds(3));
			pause.setOnFinished(e -> {
				Stage stage = (Stage) getRoot().getScene().getWindow();
				transitionToNextLevel(
						stage,
						NEXT_LEVEL,
						"/com/example/demo/images/leveltwo.jpg",
						Duration.seconds(3)
				);
			});
			pause.play();
		}
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
		killtracker = new Text("KILLTRACKER:"+ user.getNumberOfKills());
		killtracker.setFill(Color.HONEYDEW);
		killtracker.setFont(RetroFont(23));
		killtracker.setX(screenWidth - 400);
		killtracker.setY(75);
		getRoot().getChildren().add(killtracker);
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
		}
	}
	private void showLevelClearedMessage() {
		Text levelClearedMessage = new Text("Level Cleared!");
		levelClearedMessage.setFont(super.RetroFont(60));
		levelClearedMessage.setFill(Color.GHOSTWHITE);
		double textWidth = levelClearedMessage.getBoundsInLocal().getWidth();
		double textHeight = levelClearedMessage.getBoundsInLocal().getHeight();
		levelClearedMessage.setX((super.getScreenWidth() - textWidth) / 2);
		levelClearedMessage.setY((super.getScreenHeight() + textHeight) / 2);

		getRoot().getChildren().add(levelClearedMessage);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), levelClearedMessage);
		rotateTransition.setByAngle(360);
		rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
		rotateTransition.setAutoReverse(true);
		rotateTransition.play();
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

	@Override
	protected void updateKillCount(){
		super.updateKillCount();
		killtracker.setText("KILLTRACKER: "+user.getNumberOfKills());
	}
}
