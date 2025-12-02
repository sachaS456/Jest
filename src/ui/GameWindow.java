package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import model.game.Game;
import model.game.GameState;
import model.enums.Sign;
import player.AI;
import player.Human;
import player.Player;
import util.GameSaver;
import variant.GameVariant;
import model.cards.SuitCard;
import model.cards.JokerCard;

import java.util.Optional;
import java.io.File;

/**
 * Game window for managing player setup and game execution via GUI.
 * Handles player creation, turn management, and save game functionality.
 *
 * @author Jest Game Team
 * @version 1.0
 */
public class GameWindow {
    private Stage primaryStage;
    private Game game;
    private boolean isResumed;
    private int currentPlayerSetupIndex;
    private Label statusLabel;
    private VBox playerListBox;
    private volatile boolean roundContinue = false;  // Signal pour continuer après un round

    /**
     * Constructs a GameWindow for a new game.
     *
     * @param includeExpansion whether to include expansion cards
     * @param variant the game variant to play
     * @param primaryStage the primary JavaFX stage
     */
    public GameWindow(boolean includeExpansion, GameVariant variant, Stage primaryStage) {
        this.game = new Game(includeExpansion, variant);
        this.primaryStage = primaryStage;
        this.isResumed = false;
        this.currentPlayerSetupIndex = 0;
    }

    /**
     * Constructs a GameWindow for a resumed game.
     *
     * @param game the game to resume
     * @param primaryStage the primary JavaFX stage
     * @param isResumed whether this is a resumed game
     */
    public GameWindow(Game game, Stage primaryStage, boolean isResumed) {
        this.game = game;
        this.primaryStage = primaryStage;
        this.isResumed = isResumed;
    }

    /**
     * Shows the game setup window.
     */
    public void show() {
        if (!isResumed) {
            showPlayerSetup();
        } else {
            showGameResumption();
        }
    }

    /**
     * Displays the player setup screen.
     */
    private void showPlayerSetup() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Player Setup");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FF00"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        statusLabel = new Label("How many players want to play? (3 or 4)");
        statusLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14;");

        VBox playerCountBox = new VBox(10);
        playerCountBox.setAlignment(Pos.CENTER);

        Button player3Button = createStyledButton("👥 3 Players", 150, 50);
        player3Button.setOnAction(e -> {
            startPlayerNameInput(3);
        });

        Button player4Button = createStyledButton("👥 4 Players", 150, 50);
        player4Button.setOnAction(e -> {
            startPlayerNameInput(4);
        });

        playerCountBox.getChildren().addAll(player3Button, player4Button);

        contentBox.getChildren().addAll(statusLabel, playerCountBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Starts the process of inputting player names.
     *
     * @param playerCount the number of players
     */
    private void startPlayerNameInput(int playerCount) {
        currentPlayerSetupIndex = 0;
        requestPlayerName(playerCount);
    }

    /**
     * Requests the name of the next player.
     *
     * @param totalPlayers the total number of players
     */
    private void requestPlayerName(int totalPlayers) {
        if (currentPlayerSetupIndex >= totalPlayers) {
            showTrophySetup();
            return;
        }

        javafx.stage.Stage setupStage = new javafx.stage.Stage();
        setupStage.setTitle("Player " + (currentPlayerSetupIndex + 1) + " Setup");
        setupStage.setWidth(550);
        setupStage.setHeight(450);
        setupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        setupStage.initOwner(primaryStage);
        setupStage.setResizable(false);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Top: Title
        Label titleLabel = new Label("Player " + (currentPlayerSetupIndex + 1) + " Setup");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #00FF00; -fx-padding: 15;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle("-fx-background-color: #0a0a0a; -fx-border-color: #00FF00; -fx-border-width: 0 0 2 0;");

        // Center: Form
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.TOP_CENTER);
        formBox.setPadding(new Insets(25));
        formBox.setStyle("-fx-background-color: #1e1e1e;");

        // Name field
        Label nameLabel = new Label("Player Name:");
        nameLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 13;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter name...");
        nameField.setStyle("-fx-font-size: 13; -fx-padding: 8; -fx-control-inner-background: #2a2a2a; -fx-text-fill: #FFFFFF;");
        nameField.setPrefWidth(280);
        nameField.setMaxWidth(280);

        // Player type
        Label typeLabel = new Label("Player Type:");
        typeLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 13; -fx-font-weight: bold;");

        RadioButton humanRadio = new RadioButton("👤 Human");
        humanRadio.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 12;");
        humanRadio.setSelected(true);

        RadioButton aiRadio = new RadioButton("🤖 AI");
        aiRadio.setStyle("-fx-text-fill: #FF6600; -fx-font-size: 12;");

        ToggleGroup typeGroup = new ToggleGroup();
        humanRadio.setToggleGroup(typeGroup);
        aiRadio.setToggleGroup(typeGroup);

        VBox typeBox = new VBox(8);
        typeBox.setPadding(new Insets(10));
        typeBox.setStyle("-fx-border-color: #00FFFF; -fx-border-width: 1; -fx-background-color: #0a0a0a;");
        typeBox.getChildren().addAll(humanRadio, aiRadio);

        formBox.getChildren().addAll(nameLabel, nameField, typeLabel, typeBox);

        // Bottom: Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setStyle("-fx-background-color: #0a0a0a; -fx-border-color: #FFD700; -fx-border-width: 2 0 0 0;");

        Button confirmButton = createStyledButton("✅ Confirm", 140, 35);

        buttonBox.getChildren().add(confirmButton);

        root.setTop(titleBox);
        root.setCenter(formBox);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        setupStage.setScene(scene);

        // Focus sur le champ de texte
        nameField.requestFocus();

        // Handle confirm - avec vérification
        confirmButton.setOnAction(e -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Name");
                alert.setHeaderText("Empty Name");
                alert.setContentText("Please enter a player name");
                alert.showAndWait();
            } else {
                if (aiRadio.isSelected()) {
                    game.addPlayer(new AI(playerName));
                } else {
                    game.addPlayer(new player.HumanUIPlayer(playerName, primaryStage));
                }
                currentPlayerSetupIndex++;
                setupStage.close();

                // Call recursively pour le prochain joueur
                javafx.application.Platform.runLater(() -> requestPlayerName(totalPlayers));
            }
        });


        // Enter key press dans le TextField
        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                confirmButton.fire();
            }
        });

        // Show modal and wait
        setupStage.showAndWait();
    }

    /**
     * Displays the trophy setup screen.
     */
    private void showTrophySetup() {
        game.setTrophies();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("🏆 Trophies Set! 🏆");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FFD700"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30));

        Label infoLabel = new Label("Trophy cards have been set!");
        infoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14;");

        Label trophiesLabel = new Label(game.trophiesToString());
        trophiesLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 12; -fx-font-family: monospace;");

        Label playersLabel = new Label("Players:");
        playersLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14; -fx-font-weight: bold;");

        playerListBox = new VBox(5);
        playerListBox.setStyle("-fx-text-fill: #FFFFFF;");
        for (Player player : game.getPlayers()) {
            String playerType = player instanceof AI ? "🤖 AI" : "👤 Human";
            Label playerLabel = new Label("  - " + player.getName() + " " + playerType);
            playerLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 12;");
            playerListBox.getChildren().add(playerLabel);
        }

        Button startButton = createStyledButton("🎮 Start Game!", 200, 50);
        startButton.setOnAction(e -> startGame());

        contentBox.getChildren().addAll(infoLabel, trophiesLabel, playersLabel, playerListBox, startButton);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Starts the game and begins playing rounds.
     */
    private void startGame() {
        new Thread(() -> {
            playGameWithUI();
        }).start();
    }

    /**
     * Plays the game with UI updates at each step.
     */
    private void playGameWithUI() {
        boolean includeExpansion = game.getCards().size() > model.cards.CardDeckFactory.getStandardDeckSize();

        if (!isResumed) {
            game.setTrophies();
        }

        while (!game.getCards().isEmpty()) {
            playRoundWithUI();

            // Afficher le menu de fin de round et attendre que l'utilisateur clique sur Continue
            roundContinue = false;
            javafx.application.Platform.runLater(() -> {
                showRoundEndMenu();
            });

            // Attendre que l'utilisateur clique sur le bouton Continue
            while (!roundContinue) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Reveal final cards and trophies
        for (player.Player player : game.getPlayers()) {
            player.addLastCardToJest();
        }

        game.giveTrophyCard();

        // Calculate winner
        player.Player winner = null;
        int maxPoints = 0;
        for (player.Player player : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(player);
            if (winner == null || points > maxPoints) {
                winner = player;
                maxPoints = points;
            }
        }

        final player.Player finalWinner = winner;
        javafx.application.Platform.runLater(() -> {
            showFinalResults(finalWinner);
        });
    }

    /**
     * Plays a round with UI updates.
     */
    private void playRoundWithUI() {
        game.setRoundNumber(game.getRoundNumber() + 1);
        game.getVariant().applyRoundStartRules(game);

        javafx.application.Platform.runLater(() -> {
            showRoundStart();
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        game.distribute();

        player.Player currentPlayer = game.getPlayersOrder();

        while (currentPlayer != null) {
            final player.Player playerToPlay = currentPlayer;

            javafx.application.Platform.runLater(() -> {
                showPlayerTurn(playerToPlay);
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            currentPlayer = playerToPlay.playTurn(game);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.getVariant().applyRoundEndRules(game);
    }

    /**
     * Shows the start of a new round.
     */
    private void showRoundStart() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Round " + game.getRoundNumber() + " Starting!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FFFF"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(40));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        Label infoLabel = new Label("Preparing cards...\nCards are being distributed to all players.");
        infoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16;");
        infoLabel.setWrapText(true);

        Label remainingLabel = new Label("Remaining cards in deck: " + game.getCards().size());
        remainingLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 14;");

        contentBox.getChildren().addAll(infoLabel, remainingLabel);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the turn of a player with available cards to pick.
     */
    private void showPlayerTurn(player.Player currentPlayer) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        String playerType = currentPlayer instanceof AI ? "🤖 AI" : "👤 Human";
        String titleText = playerType + " " + currentPlayer.getName() + "'s Turn";
        Label titleLabel = new Label(titleText);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FF00"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30));
        titleBox.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #00FF00; -fx-border-width: 0 0 2 0;");

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(30));
        contentBox.setStyle("-fx-background-color: #1e1e1e;");

        Label actionLabel = new Label("Selecting a card from opponents...");
        actionLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 16;");

        // Afficher les cartes disponibles des adversaires
        VBox playersOffersBox = new VBox(10);
        playersOffersBox.setStyle("-fx-padding: 15; -fx-border-color: #00FFFF; -fx-border-width: 1;");

        Label offersTitle = new Label("Available Cards:");
        offersTitle.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14; -fx-font-weight: bold;");
        playersOffersBox.getChildren().add(offersTitle);

        for (Player opponent : game.getPlayers()) {
            if (opponent != currentPlayer) {
                HBox opponentBox = new HBox(10);
                opponentBox.setPadding(new Insets(10));
                opponentBox.setStyle("-fx-border-color: #FF6600; -fx-border-width: 1; -fx-background-color: #0a0a0a;");

                String opponentType = opponent instanceof AI ? "🤖" : "👤";
                Label opponentNameLabel = new Label(opponentType + " " + opponent.getName() + ":");
                opponentNameLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-weight: bold;");

                if (opponent.getVisibleCard() != null && opponent.getHiddenCard() != null) {
                    VBox cardBox = new VBox(5);
                    cardBox.setAlignment(Pos.CENTER);

                    Label visibleLabel = new Label("📍 Visible Card:");
                    visibleLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-weight: bold;");

                    Node visibleCardNode = formatCardNode(opponent.getVisibleCard());

                    Label hiddenLabel = new Label("🫣 Hidden Card");
                    hiddenLabel.setStyle("-fx-text-fill: #FF6600; -fx-font-weight: bold;");

                    cardBox.getChildren().addAll(visibleLabel, visibleCardNode, hiddenLabel);
                    opponentBox.getChildren().addAll(opponentNameLabel, cardBox);
                } else {
                    Label noOfferLabel = new Label("(No offer)");
                    noOfferLabel.setStyle("-fx-text-fill: #CCCCCC;");
                    opponentBox.getChildren().addAll(opponentNameLabel, noOfferLabel);
                }

                playersOffersBox.getChildren().add(opponentBox);
            }
        }

        Label roundLabel = new Label("Round " + game.getRoundNumber() + " • Players with cards: " + game.countPlayersWithFullOffer());
        roundLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 12;");

        contentBox.getChildren().addAll(actionLabel, playersOffersBox, roundLabel);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Charge l'image d'une carte et la retourne comme ImageView.
     *
     * @param card la carte dont charger l'image
     * @return un ImageView contenant l'image de la carte
     */
    private ImageView getCardImageView(model.cards.Card card) {
        Image img = null;

        try {
            String imagePath = null;

            if (card instanceof SuitCard) {
                SuitCard suitCard = (SuitCard) card;
                int value = suitCard.getValue();
                String color = suitCard.getColor().toString().toLowerCase(); // "red" ou "black"
                String sign = convertSignToImageName(suitCard.getSign());

                // Format du nom de fichier: {value}_{color}_{sign}.png
                // Exemple: 1_red_heart.png, 2_black_spade.png
                imagePath = "Assets/Images/" + value + "_" + color + "_" + sign + ".png";
            } else if (card instanceof JokerCard) {
                imagePath = "Assets/Images/joker.png";
            }

            if (imagePath != null) {
                // Essayer d'abord avec file: URL
                java.io.File imageFile = new java.io.File(imagePath);
                if (imageFile.exists()) {
                    img = new Image("file:///" + imageFile.getAbsolutePath().replace("\\", "/"),
                                    80, 120, true, true);
                } else {
                    System.err.println("Image non trouvée: " + imageFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            e.printStackTrace();
            img = null;
        }

        // Fallback si l'image n'est pas trouvée
        if (img == null || img.isError()) {
            try {
                java.io.File jokerFile = new java.io.File("Assets/Images/joker.png");
                if (jokerFile.exists()) {
                    img = new Image("file:///" + jokerFile.getAbsolutePath().replace("\\", "/"),
                                    80, 120, true, true);
                } else {
                    img = createPlaceholderImage();
                }
            } catch (Exception e) {
                img = createPlaceholderImage();
            }
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        iv.setPreserveRatio(false);
        return iv;
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
        javafx.scene.image.WritableImage placeholder = new javafx.scene.image.WritableImage(80, 120);
        javafx.scene.image.PixelWriter writer = placeholder.getPixelWriter();
        javafx.scene.paint.Color fillColor = javafx.scene.paint.Color.web("#CCCCCC");

        for (int y = 0; y < 120; y++) {
            for (int x = 0; x < 80; x++) {
                writer.setColor(x, y, fillColor);
            }
        }
        return placeholder;
    }

    /**
     * Formate une carte en Node avec image pour l'affichage.
     *
     * @param card la carte à formater
     * @return un Node contenant l'image et les informations de la carte
     */
    private Node formatCardNode(model.cards.Card card) {
        if (card == null) {
            Label label = new Label("(No card)");
            label.setStyle("-fx-text-fill: #CCCCCC;");
            return label;
        }

        if (!card.isVisible()) {
            Label label = new Label("🫣 Hidden");
            label.setStyle("-fx-text-fill: #FF6600;");
            return label;
        }

        HBox cardBox = new HBox(8);
        cardBox.setAlignment(Pos.CENTER);

        ImageView iv = getCardImageView(card);

        Label infoLabel = new Label();
        if (card instanceof SuitCard) {
            SuitCard suitCard = (SuitCard) card;
            infoLabel.setText(suitCard.getValue() + " " + suitCard.getSign().toString());
        } else if (card instanceof JokerCard) {
            infoLabel.setText("Joker");
        }
        infoLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 12;");

        cardBox.getChildren().addAll(iv, infoLabel);
        return cardBox;
    }

    /**
     * Formats a card for display.
     * Cette méthode est conservée pour compatibilité avec les anciennes versions.
     *
     * @param card the card to format
     * @return a formatted string representation
     */
    private String formatCard(model.cards.Card card) {
        if (!card.isVisible()) {
            return "🫣 Hidden";
        }

        if (card instanceof model.cards.SuitCard) {
            model.cards.SuitCard suitCard = (model.cards.SuitCard) card;
            String value = String.valueOf(suitCard.getValue());
            String sign = suitCard.getSign().toString();
            String color = suitCard.getColor().toString();
            return value + " " + sign + " (" + color + ")";
        } else if (card instanceof model.cards.JokerCard) {
            return "Joker Card";
        }
        return "Unknown Card";
    }

    /**
     * Shows the end of round menu with save and continue options.
     */
    private void showRoundEndMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Round " + game.getRoundNumber() + " Complete!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FFD700"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(30));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        Label infoLabel = new Label("Remaining cards: " + game.getCards().size());
        infoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14;");

        Label scoreLabel = new Label("Current Scores:");
        scoreLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14; -fx-font-weight: bold;");

        VBox scoresBox = new VBox(5);
        boolean includeExpansion = game.getCards().size() > model.cards.CardDeckFactory.getStandardDeckSize();

        for (player.Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            Label playerScore = new Label("  " + p.getName() + ": " + points + " pts");
            playerScore.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 12;");
            scoresBox.getChildren().add(playerScore);
        }

        contentBox.getChildren().addAll(infoLabel, scoreLabel, scoresBox);

        // Boutons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        Button saveButton = createStyledButton("💾 Save Game", 180, 50);
        saveButton.setOnAction(e -> {
            // Dialog pour le nom de la sauvegarde
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Save Game");
            dialog.setHeaderText("Enter a name for this save:");
            dialog.setContentText("Name (optional):");
            dialog.getDialogPane().setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #FFFFFF;");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String saveName = result.get().trim();
                if (GameSaver.saveGame(game, includeExpansion, saveName.isEmpty() ? null : saveName)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Game saved!");
                    alert.setContentText("Your game has been saved successfully.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Save failed");
                    alert.setContentText("Failed to save the game.");
                    alert.showAndWait();
                }
            }
        });

        Button continueButton = createStyledButton("▶ Continue", 180, 50);
        continueButton.setOnAction(e -> {
            roundContinue = true;
        });

        buttonBox.getChildren().addAll(saveButton, continueButton);
        contentBox.getChildren().add(buttonBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Shows the final results and winner.
     */
    private void showFinalResults(player.Player winner) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("🏆 Final Results 🏆");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FFD700"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(40));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        Label winnerLabel = new Label("🎊 " + winner.getName() + " Wins! 🎊");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        winnerLabel.setStyle("-fx-text-fill: #00FF00;");

        Label scoresTitle = new Label("Final Scores:");
        scoresTitle.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 16; -fx-font-weight: bold;");

        VBox finalScoresBox = new VBox(10);
        finalScoresBox.setAlignment(Pos.CENTER);

        int maxScore = 0;
        for (player.Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            if (points > maxScore) maxScore = points;
        }

        for (player.Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            String star = (points == maxScore) ? "⭐ " : "  ";
            Label playerFinalScore = new Label(star + p.getName() + ": " + points + " points");
            playerFinalScore.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 14;");
            finalScoresBox.getChildren().add(playerFinalScore);
        }

        Label finalMessage = new Label("Thanks for playing Jest!");
        finalMessage.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14;");

        Button mainMenuButton = createStyledButton("🏠 Return to Main Menu", 220, 50);
        mainMenuButton.setOnAction(e -> {
            MainMenuUI mainMenu = new MainMenuUI();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button exitButton = createStyledButton("❌ Exit", 220, 50);
        exitButton.setOnAction(e -> primaryStage.close());

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(mainMenuButton, exitButton);

        contentBox.getChildren().addAll(winnerLabel, scoresTitle, finalScoresBox, finalMessage, buttonBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }



    /**
     * Displays the game resumption confirmation screen.
     */
    private void showGameResumption() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Game Resumed!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FF00"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        Label roundLabel = new Label("Resuming from Round " + game.getRoundNumber());
        roundLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 16;");

        Label playersLabel = new Label("Players:");
        playersLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14; -fx-font-weight: bold;");

        VBox playersBox = new VBox(5);
        for (Player player : game.getPlayers()) {
            String playerType = player instanceof AI ? "🤖 AI" : "👤 Human";
            Label playerLabel = new Label("  - " + player.getName() + " " + playerType);
            playerLabel.setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 12;");
            playersBox.getChildren().add(playerLabel);
        }

        Button continueButton = createStyledButton("▶ Continue Game", 200, 50);
        continueButton.setOnAction(e -> startGame());

        contentBox.getChildren().addAll(roundLabel, playersLabel, playersBox, continueButton);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Creates a styled button with consistent appearance.
     *
     * @param text the button text
     * @param width the button width
     * @param height the button height
     * @return a styled button
     */
    private Button createStyledButton(String text, int width, int height) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setStyle("-fx-font-size: 14; -fx-padding: 10; -fx-background-color: #FF6600; " +
                "-fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-cursor: hand;");
        return button;
    }
}

