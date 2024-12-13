package com.example.demo.levels;

import com.example.demo.objects.ActiveActorDestructible;
import com.example.demo.objects.EnemyPlane;
import com.example.demo.display.LevelView;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The LevelOne class represents the first level of the game. It manages the gameplay for the level,
 * including the spawning of enemy units, tracking the player's kill count, and determining when the level is completed.
 * It extends the LevelParent class and includes mechanisms for checking game over conditions,
 * displaying level cleared messages, and transitioning to the next level.
 */
public class LevelOne extends LevelParent {

	// Constants for background image, next level transition, and enemy spawn configuration
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/arctic.png";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private Text killtracker;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	/**
	 * Constructs a new LevelOne with the specified screen dimensions.
	 * Initializes the background music for the level.
	 *
	 * @param screenHeight the height of the screen.
	 * @param screenWidth the width of the screen.
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		initializeBackgroundMusic("/com/example/demo/images/backgroundmusic.wav");
	}

	/**
	 * Checks whether the game is over. The game is lost if the user is destroyed,
	 * and the game is won if the user reaches the kill target. If the user wins,
	 * a level cleared message is shown and the game transitions to the next level.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			timeline.stop();
			loseGame();
		} else if (userHasReachedKillTarget()) {
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
						"/com/example/demo/images/leveltwo.jpg",
						Duration.seconds(3)
				);
			});
			pause.play();
		}
	}

	/**
	 * Initializes the friendly units, specifically adding the user (player) to the root.
	 * Additionally, it initializes and displays the kill tracker on the screen.
	 */
	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
		killtracker = new Text("KILLTRACKER:" + user.getNumberOfKills());
		killtracker.setFill(Color.PALEVIOLETRED);
		killtracker.setFont(RetroFont(23));
		killtracker.setX(screenWidth - 400);
		killtracker.setY(75);
		getRoot().getChildren().add(killtracker);
	}

	/**
	 * Spawns enemy units, specifically enemy planes, at random positions based on a spawn probability.
	 * The number of enemies is limited by the TOTAL_ENEMIES constant.
	 */
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

	/**
	 * Displays a "Level Cleared!" message on the screen with a rotating effect.
	 * This message is displayed when the user has reached the required number of kills to complete the level.
	 */
	private void showLevelClearedMessage() {
		Text levelClearedMessage = new Text("Level Cleared!");
		levelClearedMessage.setFont(super.RetroFont(60));
		levelClearedMessage.setFill(Color.PALEVIOLETRED);
		double textWidth = levelClearedMessage.getBoundsInLocal().getWidth();
		double textHeight = levelClearedMessage.getBoundsInLocal().getHeight();
		levelClearedMessage.setX((super.getScreenWidth() - textWidth) / 2);
		levelClearedMessage.setY((super.getScreenHeight() + textHeight) / 2);

		getRoot().getChildren().add(levelClearedMessage);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5), levelClearedMessage);
		rotateTransition.setByAngle(360);
		rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
		rotateTransition.setAutoReverse(true);
		rotateTransition.play();
	}

	/**
	 * Instantiates and returns the level view for this level, including the player's initial health.
	 *
	 * @return the instantiated LevelView for this level.
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}

	/**
	 * Checks if the user has reached the target kill count to advance to the next level.
	 *
	 * @return true if the user has reached the required number of kills to advance, false otherwise.
	 */
	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

	/**
	 * Updates the kill count displayed on the kill tracker as the user earns kills.
	 * This method is called whenever the user's kill count is updated.
	 */
	@Override
	protected void updateKillCount() {
		super.updateKillCount();
		killtracker.setText("KILLTRACKER: " + user.getNumberOfKills());
	}
}
