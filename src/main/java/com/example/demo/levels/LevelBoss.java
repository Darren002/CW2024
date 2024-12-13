package com.example.demo.levels;

import com.example.demo.objects.Boss;
import com.example.demo.display.LevelView;
import com.example.demo.display.LevelViewBoss;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LevelBoss class represents a specific boss battle level in the game, extending the LevelParent class.
 * This level features a boss and includes mechanisms for displaying game over, level cleared messages, and transitioning between levels.
 */
public class LevelBoss extends LevelParent {

	// Constants for background image, player health, and next level transition
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/bossbackground.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.levels.BonusLevel";
	private final Boss boss;
	private LevelViewBoss levelView;

	/**
	 * Constructs a new LevelBoss with the specified screen dimensions.
	 * Initializes the boss for this level.
	 *
	 * @param screenHeight the height of the screen.
	 * @param screenWidth the width of the screen.
	 */
	public LevelBoss(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
	}

	/**
	 * Initializes the friendly units, in this case, adding the user (player) to the root.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Checks whether the game is over. The game is lost if the user is destroyed.
	 * The game is won if the boss is destroyed, showing a level cleared message and transitioning to the next level.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			timeline.stop();
			loseGame();
		} else if (boss.isDestroyed()) {
			timeline.stop();
			showLevelClearedMessage();
			hideBurstReadyText();
			canShoot = false;
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

	/**
	 * Spawns the boss as an enemy unit if no enemies are currently on the screen.
	 * Adds the boss' shield and health bar images to the root.
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
			getRoot().getChildren().addAll(boss.getShieldImage(), boss.getHealthBar());
		}
	}

	/**
	 * Instantiates the level view for this boss level and returns it.
	 *
	 * @return the instantiated LevelViewBoss for this level.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelViewBoss(getRoot(), PLAYER_INITIAL_HEALTH);
		return levelView;
	}

	/**
	 * Displays a "Level Cleared!" message on the screen, with a fade effect.
	 */
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
