package com.example.demo.display;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents a health bar displayed in the game.
 * It visually indicates the player's health, with the health bar color changing based on the remaining health.
 */
public class HealthBar extends StackPane {

    /** The background of the health bar. */
    private final Rectangle backgroundBar;

    /** The actual health bar, showing the current health. */
    private final Rectangle healthBar;

    /** The width of the health bar. */
    private final double barWidth;

    /** The height of the health bar. */
    private final double barHeight;

    /**
     * Constructs a HealthBar with the specified width and height.
     * Initializes the background and health bar with rounded corners.
     *
     * @param width  the width of the health bar
     * @param height the height of the health bar
     */
    public HealthBar(double width, double height) {
        this.barWidth = width;
        this.barHeight = height;

        // Initialize the background bar
        backgroundBar = new Rectangle(width, height);
        backgroundBar.setFill(Color.DARKGRAY);
        backgroundBar.setArcWidth(10);
        backgroundBar.setArcHeight(10);

        // Initialize the health bar
        healthBar = new Rectangle(width, height);
        healthBar.setFill(Color.LIMEGREEN);
        healthBar.setArcWidth(10);
        healthBar.setArcHeight(10);

        // Add both bars to the layout
        getChildren().addAll(backgroundBar, healthBar);
    }

    /**
     * Updates the health bar's width and color based on the current and maximum health.
     * The health bar color changes based on the health ratio: green for high, orange for medium, and red for low health.
     *
     * @param currentHealth the current health of the player
     * @param maxHealth     the maximum health of the player
     */
    public void updateHealth(double currentHealth, double maxHealth) {
        double healthRatio = currentHealth / maxHealth;
        healthBar.setWidth(barWidth * healthRatio);

        // Change color based on health ratio
        healthBar.setFill(healthRatio > 0.5 ? Color.LIGHTGREEN : (healthRatio > 0.2 ? Color.ORANGE : Color.RED));
    }
}
