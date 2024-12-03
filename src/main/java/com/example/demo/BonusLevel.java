package com.example.demo;

public class BonusLevel extends LevelParent{
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 7;

    private final Boss boss1;
    private final Boss boss2;

        private BonusLevelView levelView;

    public BonusLevel(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);

        boss1 = new Boss();
        boss2 = new Boss();

        boss1.setLayoutX(1000.0);
        boss1.setLayoutY(200.0);

        boss2.setLayoutX(1000.0);
        boss2.setLayoutY(400.0);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

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

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new BonusLevelView(getRoot(), PLAYER_INITIAL_HEALTH);
        return levelView;
    }


}

