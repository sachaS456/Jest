package ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CardSelectionUI handles the graphical display and selection of cards during gameplay.
 * Allows players to choose cards visually instead of through console input.
 *
 * @author Jest Game Team
 * @version 1.0
 */
public class CardSelectionUI {
    private Stage primaryStage;
    private volatile AtomicInteger selectedCard;
    private volatile boolean cardSelected;

    /**
     * Constructs a CardSelectionUI.
     *
     * @param primaryStage the primary JavaFX stage
     */
    public CardSelectionUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.selectedCard = new AtomicInteger(-1);
        this.cardSelected = false;
    }

    /**
     * Shows a card selection dialog.
     *
     * @param cards the list of cards to choose from
     * @param description description of what the player should choose
     * @return the index of the selected card (1-indexed)
     */
    public int showCardSelection(ArrayList<Card> cards, String description) {
        selectedCard.set(-1);
        cardSelected = false;

        Platform.runLater(() -> {
            showSelectionScene(cards, description);
        });

        // Wait for user selection
        while (!cardSelected) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return selectedCard.get();
    }

    /**
     * Creates and displays the card selection scene.
     *
     * @param cards the list of cards to choose from
     * @param description the description of the choice
     */
    private void showSelectionScene(ArrayList<Card> cards, String description) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Top: Title
        Label titleLabel = new Label("Select a Card");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #00FF00;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        // Center: Cards display
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.TOP_CENTER);

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14;");
        descLabel.setWrapText(true);
        contentBox.getChildren().add(descLabel);

        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(15);
        cardsGrid.setVgap(15);
        cardsGrid.setAlignment(Pos.CENTER);
        cardsGrid.setPadding(new Insets(20));

        int column = 0;
        int row = 0;
        for (int i = 0; i < cards.size(); i++) {
            final int index = i + 1;
            Card card = cards.get(i);
            if (card == null) continue;

            Button cardButton = createCardButton(card, index);
            cardButton.setOnAction(e -> {
                selectedCard.set(index);
                cardSelected = true;
            });

            cardsGrid.add(cardButton, column, row);
            column++;
            if (column > 3) {
                column = 0;
                row++;
            }
        }

        contentBox.getChildren().add(cardsGrid);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    /**
     * Creates a visual button for a card.
     *
     * @param card the card to display
     * @param index the index of the card
     * @return a styled button
     */
    private Button createCardButton(Card card, int index) {
        String cardText = formatCard(card) + "\n#" + index;
        Button button = new Button(cardText);
        button.setPrefWidth(120);
        button.setPrefHeight(160);
        button.setFont(Font.font("Arial", 11));
        button.setStyle("-fx-padding: 10; -fx-background-color: #3366CC; " +
                "-fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-cursor: hand; " +
                "-fx-border-color: #FFFF00; -fx-border-width: 2;");
        button.setWrapText(true);
        return button;
    }

    /**
     * Formats a card for display.
     *
     * @param card the card to format
     * @return a formatted string representation of the card
     */
    /**
     * Formats a card for display.
     *
     * @param card the card to format
     * @return a formatted string representation of the card
     */
    private String formatCard(Card card) {
        if (!card.isVisible()) {
            return "🫣\nHidden";
        }

        if (card instanceof SuitCard) {
            SuitCard suitCard = (SuitCard) card;
            return suitCard.getValue() + "\n" + suitCard.getSign().toString();
        } else if (card instanceof JokerCard) {
            return "Joker";
        }
        return "Card";
    }

    /**
     * Shows a card hiding selection dialog (for choosing which card to hide).
     *
     * @param card1 the first card option
     * @param card2 the second card option
     * @param playerName the name of the player choosing
     * @return 1 to hide card1, 2 to hide card2
     */
    public int showHidingSelection(Card card1, Card card2, String playerName) {
        selectedCard.set(-1);
        cardSelected = false;

        Platform.runLater(() -> {
            showHidingScene(card1, card2, playerName);
        });

        // Wait for user selection
        while (!cardSelected) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return selectedCard.get();
    }

    /**
     * Creates and displays the card hiding selection scene.
     *
     * @param card1 the first card option
     * @param card2 the second card option
     * @param playerName the name of the player
     */
    private void showHidingScene(Card card1, Card card2, String playerName) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Top: Title
        Label titleLabel = new Label("Choose Card to Hide");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #00FF00;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        // Center: Two card options
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.CENTER);

        Label descLabel = new Label(playerName + ", which card do you want to hide?");
        descLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16;");
        contentBox.getChildren().add(descLabel);

        GridPane cardsGrid = new GridPane();
        cardsGrid.setHgap(40);
        cardsGrid.setVgap(20);
        cardsGrid.setAlignment(Pos.CENTER);
        cardsGrid.setPadding(new Insets(20));

        Button card1Button = createCardButton(card1, 1);
        card1Button.setPrefWidth(140);
        card1Button.setPrefHeight(180);
        card1Button.setOnAction(e -> {
            selectedCard.set(1);
            cardSelected = true;
        });

        Button card2Button = createCardButton(card2, 2);
        card2Button.setPrefWidth(140);
        card2Button.setPrefHeight(180);
        card2Button.setOnAction(e -> {
            selectedCard.set(2);
            cardSelected = true;
        });

        cardsGrid.add(card1Button, 0, 0);
        cardsGrid.add(card2Button, 1, 0);

        contentBox.getChildren().add(cardsGrid);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }
}

