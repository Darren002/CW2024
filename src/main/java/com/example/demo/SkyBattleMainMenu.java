package com.example.demo;

import com.example.demo.controller.Controller;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

public class SkyBattleMainMenu extends Application {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/mainmenu.jpg";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);

        Button playButton = new Button("Play");
        Button instructionsButton = new Button("Instructions");
        Button exitButton = new Button("Exit");

        playButton.setStyle("-fx-background-color: transparent;");
        instructionsButton.setStyle("-fx-background-color: transparent;");
        exitButton.setStyle("-fx-background-color: transparent;");

        playButton.setPrefSize(1000, 220);
        instructionsButton.setPrefSize(1000, 280);
        exitButton.setPrefSize(1000, 450);

        playButton.setLayoutX(screenWidth / 2 - playButton.getPrefWidth() / 2);
        playButton.setLayoutY(screenHeight / 3);

        instructionsButton.setLayoutX(screenWidth / 2 - instructionsButton.getPrefWidth() / 2);
        instructionsButton.setLayoutY(screenHeight / 2);

        exitButton.setLayoutX(screenWidth / 2 - exitButton.getPrefWidth() / 2);
        exitButton.setLayoutY(2 * screenHeight / 3);

        playButton.setOnAction(e -> startGame(primaryStage));
        instructionsButton.setOnAction(e -> showInstructions(primaryStage));
        exitButton.setOnAction(e -> primaryStage.close());

        Pane root = new Pane();
        root.getChildren().addAll(backgroundView, playButton, instructionsButton, exitButton);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Sky Battle Main Menu");
        primaryStage.setScene(scene);

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.show();
    }

    private void startGame(Stage primaryStage) {
        try {
            showTransitionScreen(
                    primaryStage,
                    "/com/example/demo/images/levelone.jpg",
                    Duration.seconds(3),
                    () -> {
                        try {
                            Controller controller = new Controller(primaryStage);
                            controller.launchGame();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInstructions(Stage primaryStage) {
        Image instructionImage = new Image(getClass().getResource("/com/example/demo/images/Instruction.jpg").toExternalForm());
        ImageView instructionView = new ImageView(instructionImage);
        instructionView.setFitWidth(primaryStage.getWidth());
        instructionView.setFitHeight(primaryStage.getHeight());
        instructionView.setPreserveRatio(false);

        Button backButton = new Button(".....");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");
        backButton.setLayoutX(primaryStage.getWidth() - 100);
        backButton.setLayoutY(20);
        backButton.setPrefSize(1000, 50);
        backButton.setOnAction(e -> returnToMainMenu(primaryStage));

        Pane instructionRoot = new Pane();
        instructionRoot.getChildren().addAll(instructionView, backButton);

        Scene instructionScene = new Scene(instructionRoot, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(instructionScene);
        primaryStage.show();
    }

    private void returnToMainMenu(Stage primaryStage) {
        try {
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
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
