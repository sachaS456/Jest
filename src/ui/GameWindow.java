package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.game.Game;
import model.game.GameState;
import player.AI;
import player.Human;
import player.Player;
import util.GameSaver;
import variant.GameVariant;

import java.util.Optional;

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

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player Name");
        dialog.setHeaderText("Player " + (currentPlayerSetupIndex + 1) + " Name");
        dialog.setContentText("Enter your name (type 'bot' to be an AI):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String playerName = result.get().trim();
            if (!playerName.isEmpty()) {
                if (playerName.toLowerCase().contains("bot")) {
                    game.addPlayer(new AI(playerName));
                } else {
                    game.addPlayer(new Human(playerName));
                }
                currentPlayerSetupIndex++;
                requestPlayerName(totalPlayers);
            }
        }
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
            game.playGame(isResumed);
            javafx.application.Platform.runLater(() -> {
                showGameOver();
            });
        }).start();
    }

    /**
     * Displays the game over screen.
     */
    private void showGameOver() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("🎉 Game Over! 🎉");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FFD700"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        Label messageLabel = new Label("Thanks for playing Jest!");
        messageLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 18;");

        Button mainMenuButton = createStyledButton("🏠 Return to Main Menu", 200, 50);
        mainMenuButton.setOnAction(e -> {
            MainMenuUI mainMenu = new MainMenuUI();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button exitButton = createStyledButton("❌ Exit", 200, 50);
        exitButton.setOnAction(e -> primaryStage.close());

        contentBox.getChildren().addAll(messageLabel, mainMenuButton, exitButton);

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

