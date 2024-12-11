/**
 * The Controller class manages the flow of the game, including transitioning between levels and handling updates
 * from observed objects. It utilizes JavaFX for managing the UI and leverages reflection to dynamically load levels.
 */
package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;

/**
 * Controller class to manage game execution, level transitions, and UI interaction.
 */
public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne"; // The class name for Level One.
	private final Stage stage; // The JavaFX stage used to display the game.

	/**
	 * Constructs a Controller with the specified stage.
	 *
	 * @param stage the primary stage for the application
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game by initializing and displaying the first level.
	 *
	 * @throws ClassNotFoundException       if the specified level class cannot be found
	 * @throws NoSuchMethodException        if the required constructor is not found
	 * @throws SecurityException            if there is a security violation during reflection
	 * @throws InstantiationException       if the class cannot be instantiated
	 * @throws IllegalAccessException       if the constructor is inaccessible
	 * @throws IllegalArgumentException     if the constructor arguments are invalid
	 * @throws InvocationTargetException    if the constructor invocation causes an exception
	 */
	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stage.show();
		goToLevel(LEVEL_ONE_CLASS_NAME);
	}

	/**
	 * Transitions to a specific level by dynamically loading its class and initializing it.
	 *
	 * @param className the fully qualified class name of the level
	 * @throws ClassNotFoundException       if the specified level class cannot be found
	 * @throws NoSuchMethodException        if the required constructor is not found
	 * @throws SecurityException            if there is a security violation during reflection
	 * @throws InstantiationException       if the class cannot be instantiated
	 * @throws IllegalAccessException       if the constructor is inaccessible
	 * @throws IllegalArgumentException     if the constructor arguments are invalid
	 * @throws InvocationTargetException    if the constructor invocation causes an exception
	 */
	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className);
		Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
		myLevel.addObserver(this);
		Scene scene = myLevel.initializeScene();
		stage.setScene(scene);
		myLevel.startGame();
	}

	/**
	 * Handles updates from observed objects, typically for transitioning to a new level.
	 *
	 * @param observable the observed object that triggered the update
	 * @param arg        additional data passed with the update, expected to be the class name of the next level
	 */
	@Override
	public void update(Observable observable, Object arg) {
		try {
			goToLevel((String) arg);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				 | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}
}
