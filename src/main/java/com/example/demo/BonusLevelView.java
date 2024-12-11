package com.example.demo;

import javafx.scene.Group;

/**
 * The BonusLevelView class represents the visual components of the bonus level in the game.
 * It extends the LevelView class and adds specific shield images for the boss characters.
 */
public class BonusLevelView extends LevelView {

    /**
     * X position for the shield of the first boss.
     */
    private static final int SHIELD_BOSS_ONE_X_POSITION = 1150;

    /**
     * Y position for the shield of the first boss.
     */
    private static final int SHIELD_BOSS_ONE_Y_POSITION = 500;

    /**
     * X position for the shield of the second boss.
     */
    private static final int SHIELD_BOSS_TWO_X_POSITION = 950;

    /**
     * Y position for the shield of the second boss.
     */
    private static final int SHIELD_BOSS_TWO_Y_POSITION = 300;

    private final Group root;
    private final ShieldImage bossOneShieldImage;
    private final ShieldImage bossTwoShieldImage;

    /**
     * Constructs a BonusLevelView with the specified root and hearts to display.
     * It initializes the shield images for both bosses and adds them to the root container.
     *
     * @param root the root Group where images will be added
     * @param heartsToDisplay the number of hearts to display in the level
     */
    public BonusLevelView(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.root = root;
        this.bossOneShieldImage = new ShieldImage(SHIELD_BOSS_ONE_X_POSITION, SHIELD_BOSS_ONE_Y_POSITION);
        this.bossTwoShieldImage = new ShieldImage(SHIELD_BOSS_TWO_X_POSITION, SHIELD_BOSS_TWO_Y_POSITION);

        addImagesToRoot();
    }

    /**
     * Adds the shield images of both bosses to the root container.
     */
    private void addImagesToRoot() {
        root.getChildren().addAll(bossOneShieldImage, bossTwoShieldImage);
    }
}
