package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.game.Game;
import util.GameSaver;

/**
 * Main GUI entry point for the Jest card game.
 * Provides a JavaFX-based graphical interface for game setup and menus.
 *
 * @author Jest Game Team
 * @version 1.0
 */
public class MainMenuUI extends Application {
    private Stage primaryStage;

    /**
     * Starts the JavaFX application and displays the main menu.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Jest Card Game");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(true);

        showMainMenu();
        primaryStage.show();
    }

    /**
     * Displays the main menu with options to start new game, load game, or quit.
     */
    private void showMainMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Title
        Label titleLabel = new Label("🎴 Jest Card Game 🎴");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FF00"));
        titleLabel.setStyle("-fx-text-fill: #00FF00; -fx-padding: 20px;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        // Menu buttons
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(30));

        Button newGameButton = createStyledButton("🎮 New Game", 200, 50);
        newGameButton.setOnAction(e -> showExpansionChoice());

        Button loadGameButton = createStyledButton("💾 Load Game", 200, 50);
        loadGameButton.setOnAction(e -> showLoadGameMenu());

        Button quitButton = createStyledButton("❌ Quit", 200, 50);
        quitButton.setOnAction(e -> primaryStage.close());

        // Check if there are saved games
        String[] saves = GameSaver.listSaves();
        if (saves.length == 0) {
            loadGameButton.setDisable(true);
        }

        menuBox.getChildren().addAll(newGameButton, loadGameButton, quitButton);

        root.setTop(titleBox);
        root.setCenter(menuBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Displays the expansion card selection menu.
     */
    private void showExpansionChoice() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Include Expansion Cards?");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FFFF00"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        Label infoLabel = new Label("Expansion cards include new mechanics and effects!\n" +
                "Standard: " + model.cards.CardDeckFactory.getStandardDeckSize() + " cards\n" +
                "Full: " + model.cards.CardDeckFactory.getFullDeckSize() + " cards");
        infoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 14;");
        infoLabel.setWrapText(true);

        Button yesButton = createStyledButton("✅ Yes", 150, 50);
        yesButton.setOnAction(e -> showVariantChoice(true));

        Button noButton = createStyledButton("❌ No", 150, 50);
        noButton.setOnAction(e -> showVariantChoice(false));

        Button backButton = createStyledButton("← Back", 150, 50);
        backButton.setOnAction(e -> showMainMenu());

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(yesButton, noButton, backButton);

        contentBox.getChildren().addAll(infoLabel, buttonBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Displays the game variant selection menu.
     *
     * @param includeExpansion whether expansion cards are included
     */
    private void showVariantChoice(boolean includeExpansion) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Choose a Game Variant");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#00FFFF"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        variant.ClassicVariant classicVariant = new variant.ClassicVariant();
        variant.SpeedVariant speedVariant = new variant.SpeedVariant();
        variant.HighStakesVariant highStakesVariant = new variant.HighStakesVariant();

        Button classicButton = createVariantButton(classicVariant.getName(), classicVariant.getDescription());
        classicButton.setOnAction(e -> startGame(includeExpansion, classicVariant));

        Button speedButton = createVariantButton(speedVariant.getName(), speedVariant.getDescription());
        speedButton.setOnAction(e -> startGame(includeExpansion, speedVariant));

        Button highStakesButton = createVariantButton(highStakesVariant.getName(), highStakesVariant.getDescription());
        highStakesButton.setOnAction(e -> startGame(includeExpansion, highStakesVariant));

        Button backButton = createStyledButton("← Back", 150, 50);
        backButton.setOnAction(e -> showExpansionChoice());

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(classicButton, speedButton, highStakesButton, backButton);

        contentBox.getChildren().addAll(buttonBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Creates a variant selection button with name and description.
     *
     * @param name the variant name
     * @param description the variant description
     * @return a styled button
     */
    private Button createVariantButton(String name, String description) {
        Button button = new Button(name + "\n" + description);
        button.setPrefWidth(350);
        button.setPrefHeight(80);
        button.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-text-alignment: left; " +
                "-fx-background-color: #3366CC; -fx-text-fill: #FFFFFF; -fx-border-radius: 5;");
        button.setWrapText(true);
        return button;
    }

    /**
     * Displays the load game menu.
     */
    private void showLoadGameMenu() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        Label titleLabel = new Label("Select a Save to Load");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(javafx.scene.paint.Color.web("#FF00FF"));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));

        String[] saves = GameSaver.listSaves();
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));

        for (String save : saves) {
            Button saveButton = createStyledButton("📁 " + save, 250, 40);
            saveButton.setOnAction(e -> loadGame(save));
            contentBox.getChildren().add(saveButton);
        }

        Button backButton = createStyledButton("← Back", 150, 50);
        backButton.setOnAction(e -> showMainMenu());

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.getChildren().add(backButton);

        contentBox.getChildren().add(buttonBox);

        root.setTop(titleBox);
        root.setCenter(contentBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    /**
     * Loads a saved game from file and transitions to game window.
     *
     * @param saveName the name of the save file to load
     */
    private void loadGame(String saveName) {
        model.game.GameState loadedState = GameSaver.loadGame(saveName);
        if (loadedState != null) {
            Game game = Game.restoreGame(loadedState);
            GameWindow gameWindow = new GameWindow(game, primaryStage, true);
            gameWindow.show();
        } else {
            showErrorDialog("Failed to load game");
            showLoadGameMenu();
        }
    }

    /**
     * Starts a new game with the specified expansion and variant settings.
     *
     * @param includeExpansion whether to include expansion cards
     * @param variant the game variant to play
     */
    private void startGame(boolean includeExpansion, variant.GameVariant variant) {
        GameWindow gameWindow = new GameWindow(includeExpansion, variant, primaryStage);
        gameWindow.show();
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

    /**
     * Shows an error dialog.
     *
     * @param message the error message to display
     */
    private void showErrorDialog(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Game Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main entry point for the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

