package strategy;

import java.util.ArrayList;
import java.util.Random;
import model.cards.Card;

/**
 * Implements a random play strategy for AI players.
 * This strategy makes completely random choices without any strategic considerations,
 * selecting cards purely by chance within the valid range.
 *
 * <p>Strategy behavior:</p>
 * <ul>
 *   <li>Uses a random number generator to select cards</li>
 *   <li>No preference for any particular card type or suit</li>
 *   <li>Provides unpredictable gameplay</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see IPlayStrategy
 */
public class RandomStrategy implements IPlayStrategy {

    /**
     * Makes a random choice for the AI player without any strategic consideration.
     * The choice is generated using a random number generator within the specified range.
     *
     * @param min the minimum value in the valid range (inclusive)
     * @param max the maximum value in the valid range (inclusive)
     * @param cards the list of cards to choose from (not used in this strategy)
     * @param isHidingCard true if the AI is hiding a card, false if picking a card (not used in this strategy)
     * @return a randomly generated number between min and max (inclusive)
     */
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        Random randomNumbers = new Random();
        int cardToPick = randomNumbers.nextInt(min, max+1);
        System.out.println(cardToPick);
        return cardToPick;
    }
}
