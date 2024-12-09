package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelBoss extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.BonusLevel";
	private final Boss boss;
	private LevelViewBoss levelView;

	public LevelBoss(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			timeline.stop();
			loseGame();
		}
		 else if (boss.isDestroyed()) {
			timeline.stop();
			showLevelClearedMessage();
			hideBurstReadyText();
			canShoot=false;
			PauseTransition pause = new PauseTransition(Duration.seconds(3));
			pause.setOnFinished(e -> {
				Stage stage = (Stage) getRoot().getScene().getWindow();
				transitionToNextLevel(
						stage,
						NEXT_LEVEL,
						"/com/example/demo/images/bonuslevel.jpg",
						Duration.seconds(3)
				);
			});
			pause.play();
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
			getRoot().getChildren().addAll(boss.getShieldImage(), boss.getHealthBar());
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelViewBoss(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
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

		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), levelClearedMessage);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
		fadeTransition.setAutoReverse(true);
		fadeTransition.play();
	}
}