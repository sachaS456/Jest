package player;

import javafx.stage.Stage;
import model.cards.Card;
import ui.CardSelectionUI;

import java.util.ArrayList;

/**
 * HumanUIPlayer extends Human to provide graphical UI interaction for human players.
 * Instead of console input, uses JavaFX dialogs for card selection.
 *
 * @author Jest Game Team
 * @version 1.0
 */
public class HumanUIPlayer extends Human {
    private CardSelectionUI cardSelectionUI;

    /**
     * Constructs a HumanUIPlayer with UI capabilities.
     *
     * @param name the player's name
     * @param primaryStage the JavaFX primary stage for UI dialogs
     */
    public HumanUIPlayer(String name, Stage primaryStage) {
        super(name);
        this.cardSelectionUI = new CardSelectionUI(primaryStage);
    }

    /**
     * Makes a choice using the graphical UI instead of console input.
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @param cards the list of cards available for the decision
     * @param cardOwners the list of card owners (parallel to cards)
     * @param isHidingCard true if choosing which card to hide, false if choosing which card to pick
     * @return the chosen option (typically 1-indexed)
     */
    public int makeChoice(int min, int max, ArrayList<Card> cards, ArrayList<Player> cardOwners, boolean isHidingCard) {
        if (isHidingCard) {
            if (cards.size() == 1) {
                return 1;
            } else if (cards.size() == 2) {
                String description = "Choose which card to hide:";
                return cardSelectionUI.showHidingSelection(cards.get(0), cards.get(1), this.getName());
            }
        } else {
            String description = "Choose a card to pick (1-" + cards.size() + "):";
            return cardSelectionUI.showCardSelection(cards, cardOwners, description);
        }

        return 1;
    }

    /**
     * Makes a choice using the graphical UI instead of console input.
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @param cards the list of cards available for the decision
     * @param isHidingCard true if choosing which card to hide, false if choosing which card to pick
     * @return the chosen option (typically 1-indexed)
     */
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        if (isHidingCard) {
            if (cards.size() == 1) {
                return 1; // Default: hide first card
            } else if (cards.size() == 2) {
                String description = "Choose which card to hide:";
                return cardSelectionUI.showHidingSelection(cards.get(0), cards.get(1), this.getName());
            }
        } else {
            String description = "Choose a card to pick (1-" + cards.size() + "):";
            return cardSelectionUI.showCardSelection(cards, description);
        }

        return 1; // Default fallback
    }
}

