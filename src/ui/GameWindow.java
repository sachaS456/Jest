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
import model.cards.Card;
import model.game.Game;
import model.game.GameState;
import model.enums.Sign;
import player.AI;
import player.AIPlayer;
import player.Human;
import player.Player;
import util.GameSaver;
import variant.GameVariant;
import model.cards.SuitCard;
import model.cards.JokerCard;

import java.util.ArrayList;
import java.util.Optional;
import java.io.File;

/**
 * Game window for managing player setup and game execution via GUI.
 * Handles player creation, turn management, and save game functionality.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
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
    private volatile boolean shouldQuit = false;     // Signal pour quitter l'application

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
                    game.addPlayer(new AIPlayer(playerName));
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

        while (!game.getCards().isEmpty() && !shouldQuit) {
            playRoundWithUI();

            // Afficher le menu de fin de round et attendre que l'utilisateur clique sur Continue
            roundContinue = false;
            javafx.application.Platform.runLater(() -> {
                showRoundEndMenu();
            });

            // Attendre que l'utilisateur clique sur le bouton Continue ou Quit
            while (!roundContinue && !shouldQuit) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Si l'utilisateur a choisi de quitter, retourner sans continuer
        if (shouldQuit) {
            return;
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

        // Créer l'interface de plateau animé avant la distribution
        AnimatedGameBoardUI gameBoardUI = new AnimatedGameBoardUI(primaryStage, game);

        // Assigner l'UI à tous les joueurs
        for (Player player : game.getPlayers()) {
            if (player instanceof AIPlayer) {
                ((AIPlayer) player).setGameBoardUI(gameBoardUI);
            } else if (player instanceof player.HumanUIPlayer) {
                ((player.HumanUIPlayer) player).setGameBoardUI(gameBoardUI);
            }
        }

        // Distribution personnalisée avec affichage du plateau
        distributeWithUI(gameBoardUI);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        player.Player currentPlayer = game.getPlayersOrder();

        while (currentPlayer != null) {
            final player.Player playerToPlay = currentPlayer;

            // Afficher le début du tour du joueur
            gameBoardUI.showPlayerTurnStart(playerToPlay);

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            currentPlayer = playerToPlay.playTurn(game);

            // Mettre à jour l'affichage après le tour
            gameBoardUI.updatePlayersDisplay();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.getVariant().applyRoundEndRules(game);
    }

    /**
     * Custom distribution that shows loading animation after human players finish their actions.
     */
    private void distributeWithUI(AnimatedGameBoardUI gameBoardUI) {
        // Récupérer les cartes à distribuer (logique de Game.distribute())
        ArrayList<Card> distributionPool = new ArrayList<>();

        if (game.getRoundNumber() != 1) {
            ArrayList<model.cards.Card> gameCards = new ArrayList<>();
            ArrayList<model.cards.Card> playerCards = new ArrayList<>();

            for (int i = 0; i < game.getPlayers().size() && !game.getCards().isEmpty(); i++) {
                int random = (int) (game.getCards().size() * Math.random());
                gameCards.add(game.getCards().remove(random));
            }

            for (Player player : game.getPlayers()) {
                model.cards.Card removedCard = player.removeLastCardFromOffer();
                if (removedCard != null) {
                    removedCard.setVisible(true);
                    playerCards.add(removedCard);
                }
            }

            distributionPool.addAll(gameCards);
            distributionPool.addAll(playerCards);
        } else {
            distributionPool = game.getCards();
        }

        // Distribution aux joueurs
        int cardsNeeded = game.getPlayers().size() * 2;
        boolean loadingShown = false;
        int humanPlayerIndex = -1;

        // Trouver l'index du premier joueur humain et définir les trophées
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            if (player instanceof player.HumanUIPlayer) {
                player.HumanUIPlayer humanPlayer = (player.HumanUIPlayer) player;
                humanPlayer.getCardSelectionUI().setTrophies(game.getTrophies());
                if (humanPlayerIndex == -1) {
                    humanPlayerIndex = i;
                }
            }
        }

        if (distributionPool.size() < cardsNeeded) {
            for (int i = 0; i < game.getPlayers().size(); i++) {
                Player player = game.getPlayers().get(i);
                if (!distributionPool.isEmpty()) {
                    int random = (int) (distributionPool.size() * Math.random());
                    model.cards.Card card1 = distributionPool.remove(random);
                    player.chooseCardToHide(card1, null);

                    // Afficher le loading après qu'un joueur humain ait choisi
                    if (!loadingShown && i == humanPlayerIndex) {
                        showLoadingAfterHumanChoice();
                        loadingShown = true;
                    }
                }
            }
        } else {
            for (int i = 0; i < game.getPlayers().size(); i++) {
                Player player = game.getPlayers().get(i);
                if (distributionPool.size() >= 2) {
                    int random = (int) (distributionPool.size() * Math.random());
                    model.cards.Card card1 = distributionPool.remove(random);
                    random = (int) (distributionPool.size() * Math.random());
                    model.cards.Card card2 = distributionPool.remove(random);
                    player.chooseCardToHide(card1, card2);

                    // Afficher le loading après qu'un joueur humain ait choisi
                    if (!loadingShown && i == humanPlayerIndex) {
                        showLoadingAfterHumanChoice();
                        loadingShown = true;
                    }
                } else if (distributionPool.size() == 1) {
                    model.cards.Card card1 = distributionPool.remove(0);
                    player.chooseCardToHide(card1, null);

                    // Afficher le loading après qu'un joueur humain ait choisi
                    if (!loadingShown && i == humanPlayerIndex) {
                        showLoadingAfterHumanChoice();
                        loadingShown = true;
                    }
                } else {
                    break;
                }
            }
        }

        // Après toute la distribution, afficher le plateau
        javafx.application.Platform.runLater(() -> {
            gameBoardUI.show();

            // Vérifier s'il y a des IA pour afficher le message approprié
            boolean hasAI = false;
            for (Player player : game.getPlayers()) {
                if (player instanceof AIPlayer) {
                    hasAI = true;
                    break;
                }
            }

            if (hasAI) {
                gameBoardUI.showAIPlayingMessage();
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Shows a loading animation after a human player makes their choice.
     */
    private void showLoadingAfterHumanChoice() {
        javafx.application.Platform.runLater(() -> {
            LoadingUI loadingUI = new LoadingUI(primaryStage, "Other players are choosing their cards");
            loadingUI.show();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
     * Prend en compte les cartes d'extension avec des effets spéciaux.
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

                // Construction du nom de fichier de base
                String baseImageName = value + "_" + color + "_" + sign;

                // Vérifier si c'est une carte d'extension avec un effet spécial
                String specialSuffix = getSpecialCardSuffix(suitCard);
                if (specialSuffix != null) {
                    // Essayer d'abord avec le suffixe spécial
                    imagePath = "Assets/Images/" + baseImageName + specialSuffix + ".png";
                    java.io.File specialFile = new java.io.File(imagePath);
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
                java.io.File jokerSpecialFile = new java.io.File(jokerImagePath);

                if (jokerCard.getCardEffectCode().equals("MOST_CARDS") && jokerSpecialFile.exists()) {
                    imagePath = jokerImagePath;
                } else {
                    imagePath = "Assets/Images/joker.png";
                }
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
     * Charge l'image du dos de carte (back.png) pour les cartes cachées.
     *
     * @return un ImageView contenant l'image du dos de carte
     */
    private ImageView getBackImageView() {
        Image img = null;

        try {
            java.io.File backFile = new java.io.File("Assets/Images/back.png");
            if (backFile.exists()) {
                img = new Image("file:///" + backFile.getAbsolutePath().replace("\\", "/"),
                                80, 120, true, true);
            } else {
                System.err.println("Image back.png non trouvée: " + backFile.getAbsolutePath());
                img = createPlaceholderImage();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de back.png: " + e.getMessage());
            img = createPlaceholderImage();
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(80);
        iv.setFitHeight(120);
        iv.setPreserveRatio(false);
        return iv;
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

        Button quitButton = createStyledButton("❌ Quit", 180, 50);
        quitButton.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Quit");
            confirmAlert.setHeaderText("Are you sure you want to quit?");
            confirmAlert.setContentText("The game will be closed without saving.");

            Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                shouldQuit = true;
                roundContinue = true;  // Signal pour sortir des boucles
                javafx.application.Platform.runLater(() -> {
                    primaryStage.close();
                });
            }
        });

        buttonBox.getChildren().addAll(saveButton, continueButton, quitButton);
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

        // ScrollPane pour le contenu
        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        Label winnerLabel = new Label("🎊 " + winner.getName() + " Wins! 🎊");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        winnerLabel.setStyle("-fx-text-fill: #00FF00;");

        Label scoresTitle = new Label("Final Scores & Jests:");
        scoresTitle.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 16; -fx-font-weight: bold;");

        VBox playersDetailsBox = new VBox(15);
        playersDetailsBox.setAlignment(Pos.CENTER);

        int maxScore = 0;
        for (player.Player p : game.getPlayers()) {
            int points = game.getVariant().calculatePoints(p);
            if (points > maxScore) maxScore = points;
        }

        // Afficher chaque joueur avec son score et son Jest
        for (player.Player p : game.getPlayers()) {
            VBox playerBox = createPlayerJestDisplay(p, maxScore);
            playersDetailsBox.getChildren().add(playerBox);
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

        contentBox.getChildren().addAll(winnerLabel, scoresTitle, playersDetailsBox, finalMessage, buttonBox);

        // Wrap dans un ScrollPane
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1e1e1e; -fx-background-color: #1e1e1e;");

        root.setTop(titleBox);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    /**
     * Creates a display for a player's Jest and score.
     */
    private VBox createPlayerJestDisplay(player.Player player, int maxScore) {
        VBox playerBox = new VBox(10);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setPadding(new Insets(15));
        playerBox.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #00FFFF; " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        playerBox.setMaxWidth(900);

        // Player info and score
        int points = game.getVariant().calculatePoints(player);
        String star = (points == maxScore) ? "⭐ " : "  ";
        String playerType = player instanceof AI ? "🤖" : "👤";

        Label playerNameLabel = new Label(star + playerType + " " + player.getName() + " - " + points + " points");
        playerNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        playerNameLabel.setStyle("-fx-text-fill: #00FF00;");

        // Jest cards
        Label jestLabel = new Label("Jest (" + player.getJest().size() + " cards):");
        jestLabel.setStyle("-fx-text-fill: #FFFF00; -fx-font-size: 14;");

        // Display cards in a horizontal flow
        javafx.scene.layout.FlowPane cardsFlow = new javafx.scene.layout.FlowPane();
        cardsFlow.setHgap(8);
        cardsFlow.setVgap(8);
        cardsFlow.setAlignment(Pos.CENTER);
        cardsFlow.setPadding(new Insets(10));
        cardsFlow.setStyle("-fx-background-color: #1a1a1a; -fx-border-radius: 5; -fx-background-radius: 5;");

        for (model.cards.Card card : player.getJest()) {
            VBox cardDisplay = createSmallCardDisplay(card);
            cardsFlow.getChildren().add(cardDisplay);
        }

        playerBox.getChildren().addAll(playerNameLabel, jestLabel, cardsFlow);
        return playerBox;
    }

    /**
     * Creates a small card display with image.
     */
    private VBox createSmallCardDisplay(model.cards.Card card) {
        VBox cardBox = new VBox(3);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(5));
        cardBox.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #FFD700; " +
                "-fx-border-width: 1; -fx-border-radius: 3; -fx-background-radius: 3;");

        // Card image
        ImageView cardImageView = getSmallCardImageView(card);
        cardBox.getChildren().add(cardImageView);

        return cardBox;
    }

    /**
     * Loads a small card image for final results display.
     */
    private ImageView getSmallCardImageView(model.cards.Card card) {
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
                    java.io.File specialFile = new java.io.File(imagePath);
                    if (!specialFile.exists()) {
                        imagePath = "Assets/Images/" + baseImageName + ".png";
                    }
                } else {
                    imagePath = "Assets/Images/" + baseImageName + ".png";
                }
            } else if (card instanceof JokerCard) {
                JokerCard jokerCard = (JokerCard) card;
                String jokerImagePath = "Assets/Images/joker_most_cards.png";
                java.io.File jokerSpecialFile = new java.io.File(jokerImagePath);

                if (jokerCard.getCardEffectCode().equals("MOST_CARDS") && jokerSpecialFile.exists()) {
                    imagePath = jokerImagePath;
                } else {
                    imagePath = "Assets/Images/joker.png";
                }
            }

            if (imagePath != null) {
                java.io.File imageFile = new java.io.File(imagePath);
                if (imageFile.exists()) {
                    img = new Image("file:///" + imageFile.getAbsolutePath().replace("\\", "/"),
                                    50, 75, true, true);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
        }

        if (img == null || img.isError()) {
            img = createSmallPlaceholderImage();
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(50);
        iv.setFitHeight(75);
        iv.setPreserveRatio(false);
        return iv;
    }

    /**
     * Creates a small placeholder image.
     */
    private Image createSmallPlaceholderImage() {
        javafx.scene.image.WritableImage placeholder = new javafx.scene.image.WritableImage(50, 75);
        javafx.scene.image.PixelWriter writer = placeholder.getPixelWriter();
        javafx.scene.paint.Color fillColor = javafx.scene.paint.Color.web("#666666");

        for (int y = 0; y < 75; y++) {
            for (int x = 0; x < 50; x++) {
                writer.setColor(x, y, fillColor);
            }
        }
        return placeholder;
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

