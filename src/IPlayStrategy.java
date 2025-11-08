import java.util.ArrayList;

/**
 * Defines the contract for AI player decision-making strategies in the Jest card game.
 * This interface allows different AI behaviors to be implemented and plugged into AI players,
 * following the Strategy design pattern.
 *
 * <p>Implementations of this interface define how an AI player makes choices during the game,
 * such as which card to hide or which card to pick from opponents.</p>
 *
 * <p>Available implementations:</p>
 * <ul>
 *   <li>{@link SafeStrategy} - Conservative play, prefers spades and clubs</li>
 *   <li>{@link RiskyStrategy} - Aggressive play, targets hearts and jokers</li>
 *   <li>{@link RandomStrategy} - Random play, makes unpredictable choices</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see AI
 * @see SafeStrategy
 * @see RiskyStrategy
 * @see RandomStrategy
 */
public interface IPlayStrategy {
    /**
     * Makes a strategic choice for an AI player based on the current game situation.
     * This method is called whenever the AI needs to make a decision during gameplay.
     *
     * <p>The method is used in two contexts:</p>
     * <ul>
     *   <li><b>Hiding a card</b> (isHidingCard = true): Choose which of two dealt cards to hide (1 or 2)</li>
     *   <li><b>Picking a card</b> (isHidingCard = false): Choose which opponent's card to pick (1 to max)</li>
     * </ul>
     *
     * @param min the minimum valid choice value (inclusive), typically 1
     * @param max the maximum valid choice value (inclusive), represents the number of available options
     * @param cards the list of cards relevant to the decision context
     * @param isHidingCard true if the AI is choosing which card to hide in their offer,
     *                     false if the AI is choosing which card to pick from opponents
     * @return the chosen option as an integer (typically 1-indexed), or 0 if no valid choice can be made
     */
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard);
}
