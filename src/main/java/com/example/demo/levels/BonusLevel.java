package com.example.demo.levels;

import com.example.demo.display.BonusLevelView;
import com.example.demo.objects.Boss;
import com.example.demo.display.LevelView;

/**
 * The BonusLevel class represents a specific level in the game, extending the LevelParent class.
 * This level features two bosses and a unique background image.
 * It manages the spawning of enemy units, the health and movements of the bosses,
 * and checks for win/loss conditions.
 */
public class BonusLevel extends LevelParent {

    // Constants for background image and initial player health
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/bonusbackground.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 7;

    // Bosses for the level
    private final Boss boss1;
    private final Boss boss2;

    // Level view for the bonus level
    private BonusLevelView levelView;

    /**
     * Constructs a new BonusLevel with the specified screen dimensions.
     * Initializes the bosses' positions and layout.
     *
     * @param screenHeight the height of the screen.
     * @param screenWidth the width of the screen.
     */
    public BonusLevel(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);

        boss1 = new Boss();
        boss2 = new Boss();

        boss1.setLayoutX(1000.0);
        boss1.setLayoutY(200.0);

        boss2.setLayoutX(1000.0);
        boss2.setLayoutY(400.0);
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
     * The game is won if both bosses are destroyed.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (boss1.isDestroyed() && boss2.isDestroyed()) {
            winGame();
        }
    }

    private boolean boss1Spawned = false;
    private boolean boss2Spawned = false;

    /**
     * Spawns the enemy units (bosses) if they have not been spawned yet.
     * Adds the bosses' shield and health bar images to the root.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (!boss1Spawned) {
            addEnemyUnit(boss1);
            getRoot().getChildren().addAll(boss1.getShieldImage(), boss1.getHealthBar());
            boss1Spawned = true;
        }

        if (!boss2Spawned) {
            addEnemyUnit(boss2);
            getRoot().getChildren().addAll(boss2.getShieldImage(), boss2.getHealthBar());
            boss2Spawned = true;
        }
    }

    /**
     * Updates the positions and behavior of the actors (bosses) in the level.
     * Ensures that the bosses do not overlap by adjusting their Y-positions if they are too close.
     */
    @Override
    protected void updateActors() {
        double distance = Math.abs(boss1.getLayoutY() - boss2.getLayoutY());
        double minDistance = 150.0;

        if (distance < minDistance) {
            if (boss1.getLayoutY() < boss2.getLayoutY()) {
                boss1.setTranslateY(boss1.getTranslateY() - 5);
                boss2.setTranslateY(boss2.getTranslateY() + 5);
            } else {
                boss1.setTranslateY(boss1.getTranslateY() + 5);
                boss2.setTranslateY(boss2.getTranslateY() - 5);
            }
        }

        super.updateActors();
    }

    /**
     * Instantiates the level view for this bonus level and returns it.
     *
     * @return the instantiated BonusLevelView for this level.
     */
    @Override
    protected LevelView instantiateLevelView() {
        levelView = new BonusLevelView(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }
}
