package ui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * LoadingUI displays a loading animation while AI players make their choices.
 * Shows a progress indicator and message to inform the user what's happening.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class LoadingUI {
    private Stage primaryStage;
    private String message;

    /**
     * Constructs a LoadingUI.
     *
     * @param primaryStage the primary JavaFX stage
     * @param message the message to display
     */
    public LoadingUI(Stage primaryStage, String message) {
        this.primaryStage = primaryStage;
        this.message = message;
    }

    /**
     * Shows the loading screen.
     */
    public void show() {
        Platform.runLater(() -> {
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #1e1e1e;");

            VBox contentBox = new VBox(30);
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setPadding(new Insets(60));

            // Progress indicator
            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setStyle("-fx-progress-color: #00FF00;");
            progressIndicator.setPrefSize(80, 80);

            // Message label
            Label messageLabel = new Label(message);
            messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            messageLabel.setTextFill(Color.web("#00FF00"));
            messageLabel.setWrapText(true);
            messageLabel.setAlignment(Pos.CENTER);

            // Sub message
            Label subMessageLabel = new Label("🤖 AI players are making their choices...");
            subMessageLabel.setFont(Font.font("Arial", 14));
            subMessageLabel.setTextFill(Color.web("#FFFF00"));

            // Animated dots
            Label dotsLabel = new Label("");
            dotsLabel.setFont(Font.font("Arial", 16));
            dotsLabel.setTextFill(Color.web("#00FFFF"));

            // Animate dots
            animateDots(dotsLabel);

            contentBox.getChildren().addAll(
                progressIndicator,
                messageLabel,
                subMessageLabel,
                dotsLabel
            );

            root.setCenter(contentBox);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
        });
    }

    /**
     * Animates dots to show activity.
     */
    private void animateDots(Label dotsLabel) {
        new Thread(() -> {
            int dotCount = 0;
            while (true) {
                final String dots = ".".repeat(dotCount % 4);
                Platform.runLater(() -> dotsLabel.setText(dots));

                dotCount++;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }
}

