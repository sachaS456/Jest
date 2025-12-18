package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.game.Game;
import player.Player;

/**
 * ScoreboardUI displays the current standings and scores of all players during gameplay.
 * Shows Jest pile sizes, visible/hidden cards, and calculated scores.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class ScoreboardUI {
    private Stage primaryStage;
    private Game game;

    /**
     * Constructs a ScoreboardUI.
     *
     * @param primaryStage the primary JavaFX stage
     * @param game the game instance
     */
    public ScoreboardUI(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    /**
     * Displays the current scoreboard.
     */
    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a0a;");

        // Title
        Label titleLabel = new Label("🏆 Current Standings 🏆");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #FFD700; -fx-padding: 20px;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #FFD700; -fx-border-width: 0 0 2 0;");

        // Scores
        VBox scoresBox = new VBox(15);
        scoresBox.setPadding(new Insets(30));
        scoresBox.setStyle("-fx-background-color: #0a0a0a;");

        int maxScore = 0;
        for (Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            if (points > maxScore) maxScore = points;
        }

        int rank = 1;
        for (Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            String medal = "";
            switch (rank) {
                case 1: medal = "🥇 "; break;
                case 2: medal = "🥈 "; break;
                case 3: medal = "🥉 "; break;
                default: medal = "  ";
            }

            VBox playerScoreBox = createPlayerScoreBox(p, points, medal, points == maxScore);
            scoresBox.getChildren().add(playerScoreBox);
            rank++;
        }

        root.setTop(titleBox);
        root.setCenter(scoresBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Creates a score display box for a player.
     *
     * @param player the player to display
     * @param points the player's current score
     * @param medal the medal icon (if applicable)
     * @param isLeader true if this player is currently leading
     * @return a VBox containing the player's score display
     */
    private VBox createPlayerScoreBox(Player player, int points, String medal, boolean isLeader) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-border-color: " + (isLeader ? "#FFD700" : "#00FFFF") +
                     "; -fx-border-width: 2; -fx-background-color: #1a1a1a;");

        String playerType = player instanceof player.AI ? "🤖" : "👤";
        Label nameLabel = new Label(medal + playerType + " " + player.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: " + (isLeader ? "#FFD700" : "#00FF00") + ";");

        Label scoreLabel = new Label("Points: " + points);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreLabel.setStyle("-fx-text-fill: #FFFF00;");

        Label jestLabel = new Label("Jest Pile: " + player.getJest().size() + " cards");
        jestLabel.setStyle("-fx-text-fill: #FFFFFF;");

        box.getChildren().addAll(nameLabel, scoreLabel, jestLabel);
        return box;
    }
}

