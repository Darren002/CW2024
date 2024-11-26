package com.example.demo;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {
    private final Rectangle backgroundBar;
    private final Rectangle healthBar;
    private final double barWidth;
    private final double barHeight;

    public HealthBar(double width, double height) {
        this.barWidth = width;
        this.barHeight = height;


        backgroundBar = new Rectangle(width, height);
        backgroundBar.setFill(Color.DARKGRAY);
        backgroundBar.setArcWidth(10);
        backgroundBar.setArcHeight(10);


        healthBar = new Rectangle(width, height);
        healthBar.setFill(Color.LIMEGREEN);
        healthBar.setArcWidth(10);
        healthBar.setArcHeight(10);

        getChildren().addAll(backgroundBar, healthBar);
    }

    public void updateHealth(double currentHealth, double maxHealth) {
        double healthRatio = currentHealth / maxHealth;
        healthBar.setWidth(barWidth * healthRatio);
        healthBar.setFill(healthRatio > 0.5 ? Color.LIGHTGREEN : (healthRatio > 0.2 ? Color.ORANGE : Color.RED));
    }
}
