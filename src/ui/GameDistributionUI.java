package ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.game.Game;
import player.Player;

/**
 * GameDistributionUI displays the card distribution phase with visual feedback.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class GameDistributionUI {
    private Stage primaryStage;
    private Game game;
    private volatile boolean distributionComplete = false;

    /**
     * Constructs a GameDistributionUI.
     *
     * @param primaryStage the primary JavaFX stage
     * @param game the game instance
     */
    public GameDistributionUI(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    /**
     * Shows the distribution animation and waits for completion.
     */
    public void showAndWait() {
        distributionComplete = false;

        Platform.runLater(() -> {
            showDistribution();
        });

        // Wait for distribution to complete
        while (!distributionComplete) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Displays the distribution scene.
     */
    private void showDistribution() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("🎴 Distributing Cards... 🎴");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #00FF00;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        Label roundLabel = new Label("Round " + game.getRoundNumber());
        roundLabel.setFont(Font.font("Arial", 18));
        roundLabel.setStyle("-fx-text-fill: #FFFF00;");

        VBox playersBox = new VBox(15);
        playersBox.setAlignment(Pos.CENTER);

        for (Player player : game.getPlayers()) {
            Label playerLabel = new Label("👤 " + player.getName() + " - Waiting for cards...");
            playerLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 14;");
            playersBox.getChildren().add(playerLabel);
        }

        Label deckLabel = new Label("Deck: " + game.getCards().size() + " cards remaining");
        deckLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 12;");

        contentBox.getChildren().addAll(roundLabel, playersBox, deckLabel);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Simulate distribution with animation
        PauseTransition pause = new PauseTransition(Duration.millis(1500));
        pause.setOnFinished(event -> {
            distributionComplete = true;
        });
        pause.play();
    }
}

