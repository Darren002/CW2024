package com.example.demo;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;



public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private static final double BURST_COOLDOWN_TIME = 4.0;

	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	protected final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private int currentNumberOfEnemies;
	private LevelView levelView;

	private boolean spacebarPressed = false;
	private boolean bKeyPressed = false;
	private boolean canShoot = true;
	private Text burstReadyText;
	private Timeline blinkAnimation;
	private Text pauseText;
	private boolean isPaused = false;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
		initializePauseUI();
	}
	private void initializePauseUI() {
		pauseText = new Text("PAUSED");
		pauseText.setFill(Color.WHITE);
		pauseText.setFont(loadRetroFont(80));
		pauseText.setVisible(false);
		pauseText.setX(screenWidth / 2 - pauseText.getLayoutBounds().getWidth() / 2);
		pauseText.setY(screenHeight / 2 + pauseText.getLayoutBounds().getHeight() / 4);
	}

	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();
	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		initializeBurstReadyText();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		if (isPaused) {
			return;
		}
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		background.setOnKeyPressed(this::handleKeyPressed);
		background.setOnKeyReleased(this::handleKeyReleased);
		background.setOnMouseClicked(this::handleMouseClick);

		root.getChildren().add(background);
	}

	private void handleKeyPressed(KeyEvent e) {
		KeyCode kc = e.getCode();

		if (kc == KeyCode.P) {
			isPaused = !isPaused;
			togglePauseUI();
			return;
		}
if(!isPaused) {
	if (kc == KeyCode.UP || kc == KeyCode.W) {
		user.moveUp();
	}
	if (kc == KeyCode.DOWN || kc == KeyCode.S) {
		user.moveDown();
	}

	if (kc == KeyCode.SPACE && !spacebarPressed) {
		fireProjectile();
		spacebarPressed = true;
	}

	if (kc == KeyCode.B && !bKeyPressed && canShoot) {
		shootBurst();
		bKeyPressed = true;
		canShoot = false;
		hideBurstReadyText();

		Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(BURST_COOLDOWN_TIME), e1 -> canShoot = true));
		cooldown.setCycleCount(1);
		cooldown.play();
	}
}
	}

	private void togglePauseUI() {
		pauseText.setVisible(isPaused);
		if (isPaused) {
			timeline.pause();
			pauseText.setVisible(true);
			getRoot().getChildren().add(pauseText);
		} else {
			timeline.play();
			pauseText.setVisible(false);
			getRoot().getChildren().remove(pauseText);
		}
	}

	private void handleKeyReleased(KeyEvent e) {
		KeyCode kc = e.getCode();

		if (kc == KeyCode.UP || kc == KeyCode.W || kc == KeyCode.DOWN || kc == KeyCode.S) {
			user.stop();
		}

		if (kc == KeyCode.SPACE) {
			spacebarPressed = false;
		}

		if (kc == KeyCode.B) {
			bKeyPressed = false;
		}
	}

	private void handleMouseClick(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			fireProjectile();
		}
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void shootBurst() {
		int burstCount = 5;
		int delayBetweenShots = 100;

		Timeline burstTimeline = new Timeline();
		for (int i = 0; i < burstCount; i++) {
			KeyFrame frame = new KeyFrame(Duration.millis(i * delayBetweenShots), e -> {
				ActiveActorDestructible projectile = user.fireProjectile();
				root.getChildren().add(projectile);
				userProjectiles.add(projectile);
			});
			burstTimeline.getKeyFrames().add(frame);
		}
		burstTimeline.setCycleCount(1);
		burstTimeline.play();

		canShoot = false;
		hideBurstReadyText();

		Timeline cooldown = new Timeline(new KeyFrame(Duration.seconds(BURST_COOLDOWN_TIME), e1 -> {
			canShoot = true;
			showBurstReadyText();
		}));
		cooldown.setCycleCount(1);
		cooldown.play();
	}


	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream()
				.filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1, List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getHitbox().intersects(otherActor.getHitbox().getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		root.getChildren().clear();
		levelView.showGameOverImage();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
	private void initializeBurstReadyText() {
		burstReadyText = new Text("BURST READY! PRESS [B]");
		burstReadyText.setFill(Color.LIGHTGREEN);
		burstReadyText.setFont(loadRetroFont(20));
		burstReadyText.setX(screenWidth / 2 - 65);
		burstReadyText.setY(50);
		burstReadyText.setVisible(true);
		burstReadyText.setStyle("-fx-effect: dropshadow(gaussian, limegreen, 10, 0.5, 0, 0);");
		root.getChildren().add(burstReadyText);

		blinkAnimation = new Timeline(
				new KeyFrame(Duration.seconds(0.5), e -> burstReadyText.setOpacity(1)),
				new KeyFrame(Duration.seconds(1), e -> burstReadyText.setOpacity(0))
		);
		blinkAnimation.setCycleCount(Timeline.INDEFINITE);
	}


	private void showBurstReadyText() {
		if (canShoot) {
			burstReadyText.setVisible(true);
			blinkAnimation.play();
		}
	}

	private void hideBurstReadyText() {
		burstReadyText.setVisible(false);
		blinkAnimation.stop();
	}
	private Font loadRetroFont(double size) {
		try {
			URL fontURL = getClass().getResource("Fonts/font.ttf");
			if (fontURL != null) {
				return Font.loadFont(fontURL.toExternalForm(), size);
			} else {
				System.out.println("Font not found. Using default font.");
				return Font.font("Arial", size);
			}
		} catch (Exception e) {
			System.out.println("Error loading font. Using default font.");
			return Font.font("Arial", size);
		}
	}
}
