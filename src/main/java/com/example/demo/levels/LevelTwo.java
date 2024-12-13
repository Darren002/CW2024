package com.example.demo.levels;

import com.example.demo.objects.ActiveActorDestructible;
import com.example.demo.objects.EnemyPlane;
import com.example.demo.display.LevelView;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents Level Two of the game. This level includes specific configurations
 * such as background image, enemy spawn probabilities, and player health.
 * Implements functionality to transition to the next level upon meeting kill requirements.
 */
public class LevelTwo extends LevelParent {

    /** Path to the background image used for Level Two. */
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/desert.png";

    /** Fully qualified name of the next level to transition to. */
    private static final String NEXT_LEVEL = "com.example.demo.levels.LevelThree";

    /** Total number of enemies in this level. */
    private static final int TOTAL_ENEMIES = 7;

    /** Number of kills required to advance to the next level. */
    private static final int KILLS_TO_ADVANCE = 20;

    /** Probability of spawning an enemy during a single iteration. */
    private static final double ENEMY_SPAWN_PROBABILITY = 0.20;

    /** Initial health of the player in this level. */
    private static final int PLAYER_INITIAL_HEALTH = 5;

    /** Text element displaying the player's kill count. */
    private Text killtracker;

    /**
     * Constructs Level Two with specified screen dimensions.
     *
     * @param screenHeight the height of the game screen
     * @param screenWidth  the width of the game screen
     */
    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
    }

    /**
     * Checks the current game state to determine if the game is over or the level is cleared.
     * Stops the timeline and transitions to the next level if the kill target is met.
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
                        "/com/example/demo/images/levelthree.jpg",
                        Duration.seconds(3)
                );
            });
            pause.play();
        }
    }

    /**
     * Initializes the player's character and the kill tracker on the screen.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
        killtracker = new Text("KILLTRACKER:" + user.getNumberOfKills());
        killtracker.setFill(Color.HONEYDEW);
        killtracker.setFont(RetroFont(23));
        killtracker.setX(screenWidth - 400);
        killtracker.setY(75);
        getRoot().getChildren().add(killtracker);
    }

    /**
     * Spawns enemy units on the screen based on the predefined probability and maximum number of enemies.
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
     * Displays a message indicating that the level has been cleared.
     * Adds an animated text element to the game screen.
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

        TranslateTransition shimmerTransition = new TranslateTransition(Duration.seconds(5), levelClearedMessage);
        shimmerTransition.setFromX(-textWidth);
        shimmerTransition.setToX(super.getScreenWidth());
        shimmerTransition.setCycleCount(TranslateTransition.INDEFINITE);
        shimmerTransition.setAutoReverse(true);
        shimmerTransition.play();
    }

    /**
     * Creates and returns the LevelView for this level.
     *
     * @return a new instance of {@link LevelView}
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    /**
     * Checks if the user has achieved the required kill count to advance to the next level.
     *
     * @return true if the user has reached the kill target, false otherwise
     */
    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }

    /**
     * Updates the kill count displayed on the kill tracker.
     */
    @Override
    protected void updateKillCount() {
        super.updateKillCount();
        killtracker.setText("KILLTRACKER: " + user.getNumberOfKills());
    }
}
