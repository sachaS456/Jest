package ui;

import javafx.scene.paint.Color;

/**
 * UIConstants contains all UI configuration constants and styles for the Jest game.
 * Centralizes color schemes, fonts, and layout dimensions.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class UIConstants {
    // Color Palette
    public static final String COLOR_BG_DARK = "#1e1e1e";
    public static final String COLOR_BG_DARKER = "#0a0a0a";
    public static final String COLOR_BG_LIGHT = "#2a2a2a";

    public static final String COLOR_PRIMARY = "#00FF00";      // Vert lime
    public static final String COLOR_SECONDARY = "#00FFFF";    // Cyan
    public static final String COLOR_TERTIARY = "#FFFF00";     // Jaune
    public static final String COLOR_ACCENT = "#FF6600";       // Orange
    public static final String COLOR_GOLD = "#FFD700";         // Or
    public static final String COLOR_DANGER = "#FF0000";       // Rouge

    public static final String COLOR_TEXT_PRIMARY = "#FFFFFF";
    public static final String COLOR_TEXT_SECONDARY = "#CCCCCC";

    public static final String COLOR_CARD_AI = "#3366CC";      // Bleu
    public static final String COLOR_CARD_VISIBLE = "#00FFFF";
    public static final String COLOR_CARD_HIDDEN = "#FF6600";

    // Button Styles
    public static final String BUTTON_STYLE_PRIMARY =
        "-fx-font-size: 14; -fx-padding: 10; -fx-background-color: #FF6600; " +
        "-fx-text-fill: #FFFFFF; -fx-border-radius: 5; -fx-cursor: hand;";

    public static final String BUTTON_STYLE_SECONDARY =
        "-fx-font-size: 12; -fx-padding: 10; -fx-text-alignment: left; " +
        "-fx-background-color: #3366CC; -fx-text-fill: #FFFFFF; -fx-border-radius: 5;";

    // Border Styles
    public static final String BORDER_STANDARD = "-fx-border-color: #00FFFF; -fx-border-width: 2;";
    public static final String BORDER_GOLD = "-fx-border-color: #FFD700; -fx-border-width: 2;";

    // Window Sizes
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int WINDOW_WIDTH_SMALL = 800;
    public static final int WINDOW_HEIGHT_SMALL = 600;

    // Card Display Sizes
    public static final int CARD_WIDTH = 120;
    public static final int CARD_HEIGHT = 160;
    public static final int CARD_WIDTH_LARGE = 140;
    public static final int CARD_HEIGHT_LARGE = 180;

    // Font Sizes
    public static final int FONT_SIZE_TITLE = 36;
    public static final int FONT_SIZE_SUBTITLE = 28;
    public static final int FONT_SIZE_HEADING = 16;
    public static final int FONT_SIZE_NORMAL = 14;
    public static final int FONT_SIZE_SMALL = 11;

    // Padding and Insets
    public static final double PADDING_LARGE = 40;
    public static final double PADDING_MEDIUM = 30;
    public static final double PADDING_SMALL = 15;

    /**
     * Gets a styled label style string for titles.
     *
     * @param color the color hex code
     * @return the style string
     */
    public static String getTitleStyle(String color) {
        return "-fx-text-fill: " + color + "; -fx-font-size: " + FONT_SIZE_TITLE + ";";
    }

    /**
     * Gets a styled label style string for headings.
     *
     * @param color the color hex code
     * @return the style string
     */
    public static String getHeadingStyle(String color) {
        return "-fx-text-fill: " + color + "; -fx-font-size: " + FONT_SIZE_HEADING + ";";
    }

    /**
     * Gets a styled label style string for normal text.
     *
     * @param color the color hex code
     * @return the style string
     */
    public static String getTextStyle(String color) {
        return "-fx-text-fill: " + color + "; -fx-font-size: " + FONT_SIZE_NORMAL + ";";
    }

    /**
     * Gets a complete background style for containers.
     *
     * @param backgroundColor the background color hex code
     * @param borderColor the border color hex code (null for no border)
     * @return the style string
     */
    public static String getContainerStyle(String backgroundColor, String borderColor) {
        String style = "-fx-background-color: " + backgroundColor + ";";
        if (borderColor != null) {
            style += " -fx-border-color: " + borderColor + "; -fx-border-width: 2;";
        }
        return style;
    }
}

