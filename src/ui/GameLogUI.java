package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * GameLogUI displays a real-time log of game actions and events.
 * Shows all picks, plays, and important game events in chronological order.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class GameLogUI {
    private Stage primaryStage;
    private VBox logContent;
    private ArrayList<String> logEntries;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Constructs a GameLogUI.
     *
     * @param primaryStage the primary JavaFX stage
     */
    public GameLogUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.logEntries = new ArrayList<>();
    }

    /**
     * Adds a log entry with timestamp.
     *
     * @param message the message to log
     */
    public void addLogEntry(String message) {
        String timestamp = LocalTime.now().format(timeFormatter);
        String logEntry = "[" + timestamp + "] " + message;
        logEntries.add(logEntry);
        updateDisplay();
    }

    /**
     * Displays the game log.
     */
    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0a0a;");

        // Title
        Label titleLabel = new Label("📋 Game Log 📋");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #00FFFF; -fx-padding: 15px;");

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #00FFFF; -fx-border-width: 0 0 2 0;");

        // Log content
        logContent = new VBox(8);
        logContent.setPadding(new Insets(10));
        logContent.setStyle("-fx-background-color: #0a0a0a;");

        // Add existing entries
        for (String entry : logEntries) {
            addLogEntryLabel(entry);
        }

        ScrollPane scrollPane = new ScrollPane(logContent);
        scrollPane.setStyle("-fx-control-inner-background: #0a0a0a;");
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        root.setTop(titleBox);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Updates the display with current log entries.
     */
    private void updateDisplay() {
        if (logContent != null) {
            logContent.getChildren().clear();
            for (String entry : logEntries) {
                addLogEntryLabel(entry);
            }
            // Auto-scroll to bottom
            logContent.layout();
        }
    }

    /**
     * Adds a log entry label to the display.
     *
     * @param entry the log entry text
     */
    private void addLogEntryLabel(String entry) {
        Label label = new Label(entry);
        label.setFont(Font.font("Courier New", 11));
        label.setStyle("-fx-text-fill: #00FF00;");
        label.setWrapText(true);
        logContent.getChildren().add(label);
    }

    /**
     * Clears all log entries.
     */
    public void clear() {
        logEntries.clear();
        if (logContent != null) {
            logContent.getChildren().clear();
        }
    }
}

