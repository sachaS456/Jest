package player;

import javafx.stage.Stage;
import ui.AnimatedGameBoardUI;

/**
 * AIPlayer extends AI to provide visual feedback during AI turns in the GUI.
 * Shows animations and board updates when the AI makes decisions.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class AIPlayer extends AI {
    private AnimatedGameBoardUI gameBoardUI;

    /**
     * Constructs an AIPlayer with UI capabilities.
     *
     * @param name the AI player's name
     */
    public AIPlayer(String name) {
        super(name);
        this.gameBoardUI = null;
    }

    /**
     * Sets the game board UI for visual feedback.
     *
     * @param gameBoardUI the animated game board UI
     */
    public void setGameBoardUI(AnimatedGameBoardUI gameBoardUI) {
        this.gameBoardUI = gameBoardUI;
    }

    /**
     * Gets the game board UI.
     *
     * @return the game board UI, or null if not set
     */
    public AnimatedGameBoardUI getGameBoardUI() {
        return gameBoardUI;
    }
}

