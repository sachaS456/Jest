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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.enums.Sign;
import player.Player;
import player.AI;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;

/**
 * CardSelectionUI handles the graphical display and selection of cards during gameplay.
 * Allows players to choose cards visually instead of through console input.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
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
        return showCardSelection(cards, null, description);
    }

    /**
     * Shows a card selection dialog with card owners.
     *
     * @param cards the list of cards to choose from
     * @param cardOwners the list of card owners (parallel to cards)
     * @param description description of what the player should choose
     * @return the index of the selected card (1-indexed)
     */
    public int showCardSelection(ArrayList<Card> cards, ArrayList<Player> cardOwners, String description) {
        selectedCard.set(-1);
        cardSelected = false;

        Platform.runLater(() -> {
            showSelectionScene(cards, cardOwners, description);
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
     * @param cardOwners the list of card owners (can be null)
     * @param description the description of the choice
     */
    private void showSelectionScene(ArrayList<Card> cards, ArrayList<Player> cardOwners, String description) {
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

            Player owner = (cardOwners != null && i < cardOwners.size()) ? cardOwners.get(i) : null;
            Button cardButton = createCardButton(card, index, owner);
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
     * Legacy method - kept for compatibility
     */
    private void showSelectionScene(ArrayList<Card> cards, String description) {
        showSelectionScene(cards, null, description);
    }

    /**
     * Creates a visual button for a card with owner info and image display.
     * For visible cards: displays only the image
     * For hidden cards: displays "Hidden" in orange text on colored background
     *
     * @param card the card to display
     * @param index the index of the card
     * @param owner the owner of the card (can be null)
     * @return a styled button
     */
    private Button createCardButton(Card card, int index, Player owner) {
        Button button = new Button();
        // Dimensions exactes des images: 60x90
        button.setPrefWidth(60);
        button.setPrefHeight(90);
        button.setMinWidth(60);
        button.setMinHeight(90);
        button.setMaxWidth(60);
        button.setMaxHeight(90);

        if (card.isVisible()) {
            // Pour les cartes visibles: afficher UNIQUEMENT l'image
            button.setStyle("-fx-padding: 0; -fx-background-color: transparent; " +
                    "-fx-border-color: transparent; -fx-cursor: hand;");
            ImageView cardImageView = getCardImageView(card);
            button.setGraphic(cardImageView);
        } else {
            // Pour les cartes cachées: afficher l'image back.png
            button.setStyle("-fx-padding: 0; -fx-background-color: transparent; " +
                    "-fx-border-color: transparent; -fx-cursor: hand;");
            ImageView backImageView = getBackImageView();
            button.setGraphic(backImageView);
        }

        return button;
    }

    /**
     * Creates a legacy text-based button (kept for compatibility).
     *
     * @param card the card to display
     * @param index the index of the card
     * @return a styled button
     */
    private Button createLegacyCardButton(Card card, int index) {
        String cardText = formatCard(card);

        Button button = new Button(cardText);
        button.setPrefWidth(140);
        button.setPrefHeight(180);
        button.setFont(Font.font("Arial", 10));
        button.setStyle("-fx-padding: 10; -fx-background-color: #3366CC; " +
                "-fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-cursor: hand; " +
                "-fx-border-color: #FFFF00; -fx-border-width: 2;");
        button.setWrapText(true);
        return button;
    }

    /**
     * Charge l'image d'une carte et la retourne comme ImageView.
     * Prend en compte les cartes d'extension avec des effets spéciaux.
     *
     * @param card la carte dont charger l'image
     * @return un ImageView contenant l'image de la carte
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

                // Construction du nom de fichier de base
                String baseImageName = value + "_" + color + "_" + sign;

                // Vérifier si c'est une carte d'extension avec un effet spécial
                String specialSuffix = getSpecialCardSuffix(suitCard);
                if (specialSuffix != null) {
                    // Essayer d'abord avec le suffixe spécial
                    imagePath = "Assets/Images/" + baseImageName + specialSuffix + ".png";
                    File specialFile = new File(imagePath);
                    if (!specialFile.exists()) {
                        // Sinon utiliser l'image de base
                        imagePath = "Assets/Images/" + baseImageName + ".png";
                    }
                } else {
                    imagePath = "Assets/Images/" + baseImageName + ".png";
                }
            } else if (card instanceof JokerCard) {
                // Vérifier si c'est le Joker d'extension (MOST_CARDS)
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
                                    60, 90, true, true);
                } else {
                    System.err.println("Image non trouvée: " + imageFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            img = null;
        }

        // Fallback si l'image n'est pas trouvée
        if (img == null || img.isError()) {
            try {
                File jokerFile = new File("Assets/Images/joker.png");
                if (jokerFile.exists()) {
                    img = new Image("file:///" + jokerFile.getAbsolutePath().replace("\\", "/"),
                                    60, 90, true, true);
                } else {
                    img = createPlaceholderImage();
                }
            } catch (Exception e) {
                img = createPlaceholderImage();
            }
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(60);
        iv.setFitHeight(90);
        iv.setPreserveRatio(false);
        return iv;
    }

    /**
     * Retourne le suffixe spécial pour les cartes d'extension.
     *
     * @param suitCard la carte à vérifier
     * @return le suffixe à ajouter au nom de fichier, ou null si pas de suffixe spécial
     */
    private String getSpecialCardSuffix(SuitCard suitCard) {
        String effect = suitCard.getCardEffectCode();
        int value = suitCard.getValue();
        Sign sign = suitCard.getSign();

        // Cartes d'extension avec images spéciales
        // 1_black_club_most_cards.png
        if (value == 1 && sign == Sign.CLUB && effect.equals("MOST_CARDS")) {
            return "_most_cards";
        }
        // 2_black_club_least_cards.png
        if (value == 2 && sign == Sign.CLUB && effect.equals("LEAST_CARDS")) {
            return "_least_cards";
        }
        // 3_red_heart_even.png
        if (value == 3 && sign == Sign.HEARTH && effect.equals("EVEN_VALUES")) {
            return "_even";
        }
        // 4_red_heart_odd.png
        if (value == 4 && sign == Sign.HEARTH && effect.equals("ODD_VALUES")) {
            return "_odd";
        }
        // 1_black_spade_no_duplicates.png
        if (value == 1 && sign == Sign.SPADE && effect.equals("NO_DUPLICATES")) {
            return "_no_duplicates";
        }

        return null;
    }

    /**
     * Convertit un énumérateur Sign en nom de fichier image.
     *
     * @param sign le signe à convertir
     * @return le nom du signe pour le fichier image
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
     * Crée une image placeholder si l'image réelle n'est pas trouvée.
     *
     * @return une Image placeholder
     */
    private Image createPlaceholderImage() {
        javafx.scene.image.WritableImage placeholder = new javafx.scene.image.WritableImage(60, 90);
        javafx.scene.image.PixelWriter writer = placeholder.getPixelWriter();
        javafx.scene.paint.Color fillColor = javafx.scene.paint.Color.web("#666666");

        for (int y = 0; y < 90; y++) {
            for (int x = 0; x < 60; x++) {
                writer.setColor(x, y, fillColor);
            }
        }
        return placeholder;
    }

    /**
     * Charge l'image du dos de carte (back.png) pour les cartes cachées.
     *
     * @return un ImageView contenant l'image du dos de carte
     */
    private ImageView getBackImageView() {
        Image img = null;

        try {
            File backFile = new File("Assets/Images/back.png");
            if (backFile.exists()) {
                img = new Image("file:///" + backFile.getAbsolutePath().replace("\\", "/"),
                                60, 90, true, true);
            } else {
                System.err.println("Image back.png non trouvée: " + backFile.getAbsolutePath());
                img = createPlaceholderImage();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de back.png: " + e.getMessage());
            img = createPlaceholderImage();
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(60);
        iv.setFitHeight(90);
        iv.setPreserveRatio(false);
        return iv;
    }

    /**
     * Legacy method - kept for compatibility
     */
    private Button createCardButton(Card card, int index) {
        return createCardButton(card, index, null);
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
        Button card2Button = createCardButton(card2, 2);
        card1Button.setPrefWidth(140);
        card1Button.setPrefHeight(180);
        card1Button.setOnAction(e -> {
            if (!cardSelected) {
                selectedCard.set(1);
                cardSelected = true;
                card1Button.setDisable(true);
                card2Button.setDisable(true);
            }
        });
        card2Button.setPrefWidth(140);
        card2Button.setPrefHeight(180);
        card2Button.setOnAction(e -> {
            if (!cardSelected) {
                selectedCard.set(2);
                cardSelected = true;
                card1Button.setDisable(true);
                card2Button.setDisable(true);
            }
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

