package ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.cards.Card;
import model.cards.JokerCard;
import model.cards.SuitCard;
import model.enums.Sign;
import model.game.Game;
import player.AI;
import player.Player;

import java.io.File;
import java.util.ArrayList;

/**
 * AnimatedGameBoardUI displays the game board with animations during AI turns.
 * Shows card movements and player actions visually.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class AnimatedGameBoardUI {
    private Stage primaryStage;
    private Game game;
    private BorderPane mainLayout;
    private VBox centerPlayersBox;
    private Label currentActionLabel;

    /**
     * Constructs an AnimatedGameBoardUI.
     *
     * @param primaryStage the primary JavaFX stage
     * @param game the game instance
     */
    public AnimatedGameBoardUI(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
    }

    /**
     * Shows the game board.
     */
    public void show() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #0a0a0a;");

        // Top: Game info
        mainLayout.setTop(createGameInfoPanel());

        // Center: Players display
        centerPlayersBox = new VBox(15);
        centerPlayersBox.setAlignment(Pos.CENTER);
        centerPlayersBox.setPadding(new Insets(20));
        updatePlayersDisplay();

        mainLayout.setCenter(centerPlayersBox);

        // Bottom: Current action
        currentActionLabel = new Label("");
        currentActionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        currentActionLabel.setTextFill(Color.web("#FFFF00"));
        currentActionLabel.setPadding(new Insets(20));
        currentActionLabel.setAlignment(Pos.CENTER);
        currentActionLabel.setMaxWidth(Double.MAX_VALUE);
        currentActionLabel.setStyle("-fx-background-color: #1a1a1a;");

        mainLayout.setBottom(currentActionLabel);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
    }

    /**
     * Creates the game info panel at the top.
     */
    private VBox createGameInfoPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #FFD700; -fx-border-width: 0 0 2 0;");
        panel.setAlignment(Pos.CENTER);

        Label roundLabel = new Label("Round " + game.getRoundNumber());
        roundLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        roundLabel.setTextFill(Color.web("#00FFFF"));

        Label variantLabel = new Label("Variant: " + game.getVariant().getName());
        variantLabel.setFont(Font.font("Arial", 12));
        variantLabel.setTextFill(Color.web("#FFFFFF"));

        Label deckLabel = new Label("Cards in deck: " + game.getCards().size() + " | Trophies: " + game.getTrophies().length);
        deckLabel.setFont(Font.font("Arial", 12));
        deckLabel.setTextFill(Color.web("#FFD700"));

        panel.getChildren().addAll(roundLabel, variantLabel, deckLabel);
        return panel;
    }

    /**
     * Updates the players display with their current offers.
     */
    public void updatePlayersDisplay() {
        Platform.runLater(() -> {
            centerPlayersBox.getChildren().clear();

            for (Player player : game.getPlayers()) {
                HBox playerBox = createPlayerBox(player);
                centerPlayersBox.getChildren().add(playerBox);
            }
        });
    }

    /**
     * Creates a visual box for a player showing their cards.
     */
    private HBox createPlayerBox(Player player) {
        HBox playerBox = new HBox(20);
        playerBox.setPadding(new Insets(15));
        playerBox.setAlignment(Pos.CENTER_LEFT);
        playerBox.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #00FFFF; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        playerBox.setMinHeight(150);

        // Player info
        VBox playerInfo = new VBox(5);
        playerInfo.setAlignment(Pos.CENTER);
        playerInfo.setPrefWidth(150);

        String playerType = player instanceof AI ? "🤖" : "👤";
        Label nameLabel = new Label(playerType + " " + player.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#00FF00"));

        Label jestCountLabel = new Label("Jest: " + player.getJest().size() + " cards");
        jestCountLabel.setFont(Font.font("Arial", 12));
        jestCountLabel.setTextFill(Color.web("#FFFFFF"));

        playerInfo.getChildren().addAll(nameLabel, jestCountLabel);

        // Player's offer (2 cards)
        HBox offerBox = new HBox(15);
        offerBox.setAlignment(Pos.CENTER);

        Card[] offer = player.getOffer();
        if (offer != null && offer.length > 0) {
            for (int i = 0; i < offer.length; i++) {
                if (offer[i] != null) {
                    VBox cardBox = createCardDisplay(offer[i]);
                    cardBox.setId("player_" + player.getName() + "_card_" + i);
                    offerBox.getChildren().add(cardBox);
                }
            }
        }

        playerBox.getChildren().addAll(playerInfo, offerBox);
        return playerBox;
    }

    /**
     * Creates a visual display for a single card.
     */
    private VBox createCardDisplay(Card card) {
        VBox cardBox = new VBox(5);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(5));

        ImageView cardImageView;
        if (card.isVisible()) {
            cardImageView = getCardImageView(card);
        } else {
            cardImageView = getBackImageView();
        }

        cardBox.getChildren().add(cardImageView);
        return cardBox;
    }

    /**
     * Animates a card being picked from one player to another.
     *
     * @param fromPlayer the player giving the card
     * @param toPlayer the player receiving the card
     * @param cardIndex the index of the card being picked (0 or 1)
     */
    public void animateCardPick(Player fromPlayer, Player toPlayer, int cardIndex) {
        Platform.runLater(() -> {
            updatePlayersDisplay();

            currentActionLabel.setText(toPlayer.getName() + " picks a card from " + fromPlayer.getName());

            // Attendre un peu pour que l'utilisateur voie l'action
            PauseTransition pause = new PauseTransition(Duration.millis(1500));
            pause.play();
        });

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Animates a player hiding a card.
     *
     * @param player the player hiding the card
     */
    public void animateCardHiding(Player player) {
        Platform.runLater(() -> {
            currentActionLabel.setText(player.getName() + " is hiding a card...");

            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(e -> updatePlayersDisplay());
            pause.play();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shows a player's turn starting.
     *
     * @param player the player whose turn is starting
     */
    public void showPlayerTurnStart(Player player) {
        Platform.runLater(() -> {
            String playerType = player instanceof AI ? "🤖 AI" : "👤 Human";
            currentActionLabel.setText(playerType + " " + player.getName() + "'s turn!");
            currentActionLabel.setTextFill(Color.web("#00FF00"));
        });
    }

    /**
     * Clears the current action message.
     */
    public void clearActionMessage() {
        Platform.runLater(() -> {
            currentActionLabel.setText("");
        });
    }

    /**
     * Shows a message indicating that AIs are playing.
     */
    public void showAIPlayingMessage() {
        Platform.runLater(() -> {
            currentActionLabel.setText("🤖 AI players are now playing...");
            currentActionLabel.setTextFill(Color.web("#FF6600"));
        });
    }

    /**
     * Loads a card image.
     */
    private ImageView getCardImageView(Card card) {
        Image img = null;

        try {
            String imagePath = null;

            if (card instanceof SuitCard) {
                SuitCard suitCard = (SuitCard) card;
                int value = suitCard.getValue();
                String color = suitCard.getColor().toString().toLowerCase();
                String sign = convertSignToImageName(suitCard.getSign());

                String baseImageName = value + "_" + color + "_" + sign;
                String specialSuffix = getSpecialCardSuffix(suitCard);

                if (specialSuffix != null) {
                    imagePath = "Assets/Images/" + baseImageName + specialSuffix + ".png";
                    File specialFile = new File(imagePath);
                    if (!specialFile.exists()) {
                        imagePath = "Assets/Images/" + baseImageName + ".png";
                    }
                } else {
                    imagePath = "Assets/Images/" + baseImageName + ".png";
                }
            } else if (card instanceof JokerCard) {
                JokerCard jokerCard = (JokerCard) card;
                String jokerImagePath = "Assets/Images/joker_most_cards.png";
                File jokerSpecialFile = new File(jokerImagePath);

                if (jokerCard.getCardEffectCode().equals("MOST_CARDS") && jokerSpecialFile.exists()) {
                    imagePath = jokerImagePath;
                } else {
                    imagePath = "Assets/Images/joker.png";
                }
            }

            if (imagePath != null) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    img = new Image("file:///" + imageFile.getAbsolutePath().replace("\\", "/"),
                                    80, 120, true, true);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
        }

        if (img == null || img.isError()) {
            img = createPlaceholderImage();
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        iv.setPreserveRatio(false);
        return iv;
    }

    /**
     * Loads the back card image.
     */
    private ImageView getBackImageView() {
        Image img = null;

        try {
            File backFile = new File("Assets/Images/back.png");
            if (backFile.exists()) {
                img = new Image("file:///" + backFile.getAbsolutePath().replace("\\", "/"),
                                80, 120, true, true);
            } else {
                img = createPlaceholderImage();
            }
        } catch (Exception e) {
            img = createPlaceholderImage();
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        iv.setPreserveRatio(false);
        return iv;
    }

    /**
     * Gets special card suffix for expansion cards.
     */
    private String getSpecialCardSuffix(SuitCard suitCard) {
        String effect = suitCard.getCardEffectCode();
        int value = suitCard.getValue();
        Sign sign = suitCard.getSign();

        if (value == 1 && sign == Sign.CLUB && effect.equals("MOST_CARDS")) {
            return "_most_cards";
        }
        if (value == 2 && sign == Sign.CLUB && effect.equals("LEAST_CARDS")) {
            return "_least_cards";
        }
        if (value == 3 && sign == Sign.HEARTH && effect.equals("EVEN_VALUES")) {
            return "_even";
        }
        if (value == 4 && sign == Sign.HEARTH && effect.equals("ODD_VALUES")) {
            return "_odd";
        }
        if (value == 1 && sign == Sign.SPADE && effect.equals("NO_DUPLICATES")) {
            return "_no_duplicates";
        }

        return null;
    }

    /**
     * Converts Sign enum to image name.
     */
    private String convertSignToImageName(Sign sign) {
        switch (sign) {
            case SPADE: return "spade";
            case CLUB: return "club";
            case DIAMOND: return "diamond";
            case HEARTH: return "heart";
            default: return "spade";
        }
    }

    /**
     * Creates a placeholder image.
     */
    private Image createPlaceholderImage() {
        javafx.scene.image.WritableImage placeholder = new javafx.scene.image.WritableImage(80, 120);
        javafx.scene.image.PixelWriter writer = placeholder.getPixelWriter();
        Color fillColor = Color.web("#666666");

        for (int y = 0; y < 120; y++) {
            for (int x = 0; x < 80; x++) {
                writer.setColor(x, y, fillColor);
            }
        }
        return placeholder;
    }
}

