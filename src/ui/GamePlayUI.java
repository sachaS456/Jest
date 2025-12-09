package ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.game.Game;
import player.AI;
import player.Player;

import java.util.ArrayList;

/**
 * GamePlayUI handles the display of the game board during gameplay.
 * Shows players, their cards (visible/hidden), and the current game state.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class GamePlayUI {
    private Stage primaryStage;
    private Game game;
    private Scene gameScene;

    /**
     * Constructs a GamePlayUI.
     *
     * @param primaryStage the primary JavaFX stage
     * @param game the game instance
     */
    public GamePlayUI(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    /**
     * Creates and displays the game play scene.
     */
    public void show() {
        gameScene = createGameScene();
        primaryStage.setScene(gameScene);
    }

    /**
     * Creates the main game scene showing all players and their offers.
     *
     * @return the game scene
     */
    private Scene createGameScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a0a;");

        // Top: Game info
        root.setTop(createGameInfoPanel());

        // Center: Players display
        root.setCenter(createPlayersPanel());

        // Bottom: Deck and actions info
        root.setBottom(createBottomPanel());

        return new Scene(root, 1000, 700);
    }

    /**
     * Creates the game info panel at the top.
     *
     * @return the info panel
     */
    private VBox createGameInfoPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #FFD700; -fx-border-width: 0 0 2 0;");
        panel.setAlignment(Pos.CENTER);

        Label roundLabel = new Label("Round " + game.getRoundNumber());
        roundLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        roundLabel.setStyle("-fx-text-fill: #00FFFF;");

        Label variantLabel = new Label("Variant: " + game.getVariant().getName());
        variantLabel.setFont(Font.font("Arial", 12));
        variantLabel.setStyle("-fx-text-fill: #FFFFFF;");

        panel.getChildren().addAll(roundLabel, variantLabel);
        return panel;
    }

    /**
     * Creates the players panel showing all players and their cards.
     *
     * @return the players panel
     */
    private HBox createPlayersPanel() {
        HBox playersBox = new HBox(20);
        playersBox.setPadding(new Insets(30));
        playersBox.setAlignment(Pos.CENTER);
        playersBox.setStyle("-fx-background-color: #0a0a0a;");

        for (Player player : game.getPlayers()) {
            playersBox.getChildren().add(createPlayerCard(player));
        }

        return playersBox;
    }

    /**
     * Creates a visual card for a player showing their offers and jest pile.
     *
     * @param player the player to display
     * @return a VBox containing the player's visual representation
     */
    private VBox createPlayerCard(Player player) {
        VBox playerBox = new VBox(12);
        playerBox.setPadding(new Insets(15));
        playerBox.setStyle("-fx-border-color: #00FFFF; -fx-border-width: 2; -fx-border-radius: 5; " +
                "-fx-background-color: #1a1a1a;");
        playerBox.setAlignment(Pos.TOP_CENTER);

        // Player name
        String playerType = player instanceof AI ? "🤖" : "👤";
        Label nameLabel = new Label(playerType + " " + player.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #00FF00;");

        // Jest pile size
        Label jestLabel = new Label("Jest: " + player.getJest().size() + " cards");
        jestLabel.setFont(Font.font("Arial", 11));
        jestLabel.setStyle("-fx-text-fill: #FFFF00;");

        // Visible and hidden cards
        VBox offersBox = new VBox(10);
        offersBox.setAlignment(Pos.CENTER);

        Label offersTitle = new Label("Current Offer:");
        offersTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        offersTitle.setStyle("-fx-text-fill: #FFD700;");

        // Visible card
        if (player.getVisibleCard() != null) {
            Label visibleLabel = new Label("Visible: " + formatCard(player.getVisibleCard()));
            visibleLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 11;");
            offersBox.getChildren().add(visibleLabel);
        }

        // Hidden card
        if (player.getHiddenCard() != null) {
            Label hiddenLabel = new Label("Hidden: 🫣");
            hiddenLabel.setStyle("-fx-text-fill: #FF6600; -fx-font-size: 11;");
            offersBox.getChildren().add(hiddenLabel);
        }

        if (player.getVisibleCard() == null && player.getHiddenCard() == null) {
            Label noCardsLabel = new Label("(No offer)");
            noCardsLabel.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 11;");
            offersBox.getChildren().add(noCardsLabel);
        }

        playerBox.getChildren().addAll(nameLabel, jestLabel, offersTitle, offersBox);
        return playerBox;
    }

    /**
     * Creates the bottom panel showing deck info.
     *
     * @return the bottom panel
     */
    private VBox createBottomPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #FFD700; -fx-border-width: 2 0 0 0;");
        panel.setAlignment(Pos.CENTER);

        Label deckLabel = new Label("Remaining Cards in Deck: " + game.getCards().size());
        deckLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        deckLabel.setStyle("-fx-text-fill: #00FF00;");

        int trophiesCount = 0;
        Card[] trophyCards = game.getTrophyCards();
        if (trophyCards != null) {
            for (Card card : trophyCards) {
                if (card != null) trophiesCount++;
            }
        }
        Label trophyLabel = new Label("Trophy Cards: " + trophiesCount + " available");
        trophyLabel.setFont(Font.font("Arial", 12));
        trophyLabel.setStyle("-fx-text-fill: #FFD700;");

        panel.getChildren().addAll(deckLabel, trophyLabel);
        return panel;
    }

    /**
     * Formats a card for display.
     *
     * @param card the card to format
     * @return a formatted string representation of the card
     */
    private String formatCard(Card card) {
        if (!card.isVisible()) {
            return "🫣 Hidden";
        }

        if (card instanceof SuitCard) {
            SuitCard suitCard = (SuitCard) card;
            String value = String.valueOf(suitCard.getValue());
            String sign = suitCard.getSign().toString();
            String color = suitCard.getColor().toString();
            return value + " " + sign + " (" + color + ")";
        } else if (card instanceof JokerCard) {
            return "Joker Card";
        }
        return "Unknown Card";
    }

    /**
     * Updates the game scene with current state.
     */
    public void update() {
        if (gameScene != null) {
            Platform.runLater(() -> {
                primaryStage.setScene(createGameScene());
            });
        }
    }
}

