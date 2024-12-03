package com.example.demo;

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
			goToNextLevel(NEXT_LEVEL);
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

}