package com.example.demo;
import javafx.scene.Group;
public class BonusLevelView extends LevelView {
    private static final int SHIELD_BOSS_ONE_X_POSITION = 1150;
    private static final int SHIELD_BOSS_ONE_Y_POSITION = 500;
    private static final int SHIELD_BOSS_TWO_X_POSITION = 950;
    private static final int SHIELD_BOSS_TWO_Y_POSITION = 300;

    private final Group root;
    private final ShieldImage bossOneShieldImage;
    private final ShieldImage bossTwoShieldImage;

    public BonusLevelView(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.root = root;
        this.bossOneShieldImage = new ShieldImage(SHIELD_BOSS_ONE_X_POSITION, SHIELD_BOSS_ONE_Y_POSITION);
        this.bossTwoShieldImage = new ShieldImage(SHIELD_BOSS_TWO_X_POSITION, SHIELD_BOSS_TWO_Y_POSITION);

        addImagesToRoot();
    }

    private void addImagesToRoot() {
        root.getChildren().addAll(bossOneShieldImage, bossTwoShieldImage);
    }

    public void showBossOneShield() {
        bossOneShieldImage.showShield();
    }

    public void hideBossOneShield() {
        bossOneShieldImage.hideShield();
    }

    public void showBossTwoShield() {
        bossTwoShieldImage.showShield();
    }

    public void hideBossTwoShield() {
        bossTwoShieldImage.hideShield();
    }
}
