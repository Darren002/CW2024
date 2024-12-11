package com.example.demo.levels;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.objects.ActiveActorDestructible;
import com.example.demo.objects.FighterPlane;
import com.example.demo.LevelView;
import com.example.demo.objects.UserPlane;
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
import javafx.animation.PauseTransition;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * The abstract class representing the base level in a game. It manages the game loop,
 * user inputs, actors (friendly and enemy units), projectiles, and collision detection.
 * It also handles the game's pause functionality, background music, and level progression.
 */
public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 30;
	private static final double BURST_COOLDOWN_TIME = 4.0;

	private final double screenHeight;
	protected final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	protected final Timeline timeline;
	protected final UserPlane user;
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
	protected boolean canShoot = true;
	private Text burstReadyText;
	private Timeline blinkAnimation;
	private Text pauseText;
	private boolean isPaused = false;
	protected Stage stage;
	private Clip backgroundMusicClip;
	/**
	 * Constructor for LevelParent.
	 * Initializes the basic game settings, including the user plane, background, and timeline.
	 *
	 * @param backgroundImageName Name of the background image file.
	 * @param screenHeight Height of the game screen.
	 * @param screenWidth Width of the game screen.
	 * @param playerInitialHealth Initial health of the player.
	 */
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

	/**
	 * Initializes the user interface elements for the pause functionality, including
	 * the "PAUSED" text and its positioning on the screen.
	 */
	private void initializePauseUI() {
		pauseText = new Text("PAUSED");
		pauseText.setFill(Color.WHITE);
		pauseText.setFont(RetroFont(70));
		pauseText.setVisible(false);
		pauseText.setX(screenWidth / 2 - pauseText.getLayoutBounds().getWidth() / 2);
		pauseText.setY(screenHeight / 2 + pauseText.getLayoutBounds().getHeight() / 4);
	}

	/**
	 * Initializes the friendly units in the game. This method is abstract and must
	 * be implemented in subclasses.
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks if the game is over. This method is abstract and must be implemented in subclasses.
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns the enemy units in the game. This method is abstract and must be implemented in subclasses.
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Instantiates the level view for displaying the game status. This method is abstract and
	 * must be implemented in subclasses.
	 *
	 * @return The level view instance.
	 */
	protected abstract LevelView instantiateLevelView();

	/**
	 * Initializes the scene with the user plane, friendly units, and background. This also sets up the
	 * user interface elements such as the heart display and burst ready text.
	 *
	 * @return The fully initialized game scene.
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		initializeBurstReadyText();
		levelView.showHeartDisplay();
		return scene;
	}

	/**
	 * Starts the game's background music loop.
	 *
	 * @param musicFileName The file name of the background music to play.
	 */
	protected void initializeBackgroundMusic(String musicFileName) {
		try {
			File musicFile = new File(getClass().getResource(musicFileName).toURI());
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);

			backgroundMusicClip = AudioSystem.getClip();
			backgroundMusicClip.open(audioInputStream);

			backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
			backgroundMusicClip.start();
		} catch (Exception e) {
			System.out.println("Error initializing background music: " + e.getMessage());
		}
	}

	/**
	 * Starts the game loop, which begins updating the scene and playing the timeline animation.
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	/**
	 * Updates the scene by handling actions such as spawning enemies, updating actors, and checking for game over.
	 * Also includes handling projectiles and collisions.
	 */
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

	/**
	 * Initializes the timeline that controls the game loop. This timeline runs continuously, updating the scene
	 * at regular intervals.
	 */
	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}


	/**
	 * Initializes the game background, sets event handlers for key and mouse events, and adds it to the scene.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		background.setOnKeyPressed(this::handleKeyPressed);
		background.setOnKeyReleased(this::handleKeyReleased);
		background.setOnMouseClicked(this::handleMouseClick);

		root.getChildren().add(background);
	}

	/**
	 * Handles the key pressed event for user input actions such as movement, shooting, and pausing.
	 *
	 * @param e The key event triggered by the user.
	 */
	private void handleKeyPressed(KeyEvent e) {
		KeyCode kc = e.getCode();

		if (kc == KeyCode.P) {
			isPaused = !isPaused;
			togglePauseUI();
			return;
		}
		if (!isPaused) {
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

	/**
	 * Toggles the pause UI visibility and pauses or resumes the game based on the current state.
	 * This method updates the "PAUSED" text and pauses/resumes the timeline accordingly.
	 */
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

	/**
	 * Handles the key released event for user input actions like  movement and shooting.
	 *
	 * @param e The key event triggered when a key is released.
	 */
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

	/**
	 * Handles mouse click events to trigger projectile firing.
	 *
	 * @param e The mouse event triggered by a click.
	 */
	private void handleMouseClick(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			fireProjectile();
		}
	}

	/**
	 * Fires a projectile from the user plane.
	 */
	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	/**
	 * Handles the burst shooting functionality, allowing the user to shoot multiple projectiles.
	 */
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

	/**
	 * Generates enemy projectiles and adds them to the scene.
	 */
	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	/**
	 * Updates the actors' states during each game loop cycle.
	 */
	protected void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	/**
	 * Removes destroyed actors from the scene and the corresponding lists.
	 */
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

	/**
	 * Handles the collision between friendly units and enemy units.
	 */
	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	/**
	 * Handles the collisions between user projectiles and enemy units.
	 */
	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	/**
	 * Handles the collisions between enemy projectiles and friendly units.
	 */
	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	/**
	 * Handles collision detection between two lists of actors, checking if their hitboxes intersect.
	 * If a collision is detected, the involved actors take damage.
	 *
	 * @param actors1 The first list of actors involved in potential collisions.
	 * @param actors2 The second list of actors involved in potential collisions.
	 */
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

	/**
	 * Handles the logic for when an enemy has penetrated the player's defenses.
	 */
	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	/**
	 * Updates the level view UI to reflect changes in the game state.
	 */
	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	/**
	 * Updates the kill count and related game statistics.
	 */
	protected void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	/**
	 * Handles enemy units penetrating the user plane's defenses.
	 */
	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	/**
	 * Handles the actions required when the player wins the game.
	 * Stops the game timeline and background music, removes UI elements,
	 * and displays the win screen.
	 */
	protected void winGame() {
		timeline.stop();
		stopBackgroundMusic();
		getRoot().getChildren().remove(backgroundMusicClip);
		getRoot().getChildren().remove(burstReadyText);
		hideBurstReadyText();
		canShoot=false;
		levelView.showWinImage();
	}

	/**
	 * Handles the actions required when the player loses the game.
	 * Stops the game timeline and background music, removes UI elements,
	 * and displays the game over screen.
	 */
	protected void loseGame() {
		timeline.stop();
		stopBackgroundMusic();
		getRoot().getChildren().remove(backgroundMusicClip);
		hideBurstReadyText();
		canShoot=false;
		levelView.showGameOverImage();
	}

	/**
	 * Stops the background music if it is currently playing and closes the audio clip.
	 */
	private void stopBackgroundMusic() {
		if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
			backgroundMusicClip.stop();
			backgroundMusicClip.close();
		}
	}

	/**
	 * Returns the user-controlled plane in the game.
	 *
	 * @return the user's plane.
	 */
	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	/**
	 * Gets the current number of active enemy units in the game.
	 *
	 * @return the number of enemy units.
	 */
	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	/**
	 * Adds a new enemy unit to the game and displays it on the scene.
	 *
	 * @param enemy the enemy unit to add.
	 */
	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	/**
	 * Returns the maximum Y position an enemy can reach in the game.
	 *
	 * @return the maximum Y position for enemies.
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected double getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Checks if the user's plane has been destroyed.
	 *
	 * @return true if the user's plane is destroyed, false otherwise.
	 */
	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	/**
	 * Updates the count of active enemy units in the game.
	 */
	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	/**
	 * Initializes the burst ready text UI element that shows when the player can shoot a burst of projectiles.
	 */
	private void initializeBurstReadyText() {
		burstReadyText = new Text("BURST READY! PRESS [B]");
		burstReadyText.setFill(Color.LIGHTGREEN);
		burstReadyText.setFont(RetroFont(20));
		burstReadyText.setX((screenWidth - burstReadyText.getLayoutBounds().getWidth()) / 2);
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

	/**
	 * Displays the burst ready text when the player can shoot again after the burst cooldown period.
	 */
	private void showBurstReadyText() {
		if (canShoot) {
			burstReadyText.setVisible(true);
			blinkAnimation.play();
		}
	}

	/**
	 * Hides the burst ready text after the player uses the burst shot and during the cooldown period.
	 */
	protected void hideBurstReadyText() {
		burstReadyText.setVisible(false);
		blinkAnimation.stop();
	}

	/**
	 * Loads a retro-style font from a specified file.
	 *
	 * @param size the size of the font.
	 * @return the loaded font, or a default font if loading fails.
	 */
	public Font RetroFont(double size) {
		try {
			URL fontURL = getClass().getResource("/Fonts/font.ttf");
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


	/**
	 * Transitions the game to the next level with a screen fade effect.
	 *
	 * @param stage the game stage.
	 * @param nextLevelClassName the class name of the next level.
	 * @param transitionImagePath the path to the transition image.
	 * @param transitionDuration the duration of the transition effect.
	 */
	protected void transitionToNextLevel(Stage stage, String nextLevelClassName, String transitionImagePath, Duration transitionDuration) {
		showTransitionScreen(
				stage,
				transitionImagePath,
				transitionDuration,
				() -> {
					try {
						Class<?> nextLevelClass = Class.forName(nextLevelClassName);
						LevelParent nextLevel = (LevelParent) nextLevelClass
								.getConstructor(double.class, double.class)
								.newInstance(stage.getHeight(), stage.getWidth());

						stage.setScene(nextLevel.initializeScene());
						nextLevel.startGame();
					} catch (Exception e) {

					}
				}
		);
	}

	/**
	 * Displays a transition screen with an image before progressing to the next level.
	 *
	 * @param stage the game stage.
	 * @param imagePath the path to the transition image.
	 * @param duration the duration of the transition effect.
	 * @param onComplete a Runnable to execute after the transition completes.
	 */
	private void showTransitionScreen(Stage stage, String imagePath, Duration duration, Runnable onComplete) {
		ImageView transitionImage = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
		transitionImage.setFitWidth(stage.getWidth());
		transitionImage.setFitHeight(stage.getHeight());
		transitionImage.setPreserveRatio(false);

		Pane transitionPane = new Pane(transitionImage);
		Scene transitionScene = new Scene(transitionPane);

		stage.setScene(transitionScene);
		stage.show();

		PauseTransition pause = new PauseTransition(duration);
		pause.setOnFinished(e -> onComplete.run());
		pause.play();
	}
}

