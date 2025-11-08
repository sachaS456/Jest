package strategy;

import java.util.ArrayList;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.enums.Sign;

/**
 * Implements an aggressive play strategy for AI players.
 * This strategy focuses on maximizing potential points by preferring hearts and jokers,
 * which are typically worth more points in the Jest game.
 *
 * <p>Strategy behavior:</p>
 * <ul>
 *   <li>When hiding cards: prioritizes hiding hearts or jokers</li>
 *   <li>When picking cards: targets visible hearts or jokers</li>
 *   <li>Seeks high-value cards to maximize scoring potential</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see IPlayStrategy
 */
public class RiskyStrategy implements IPlayStrategy {

    /**
     * Makes a strategic choice for the AI player based on aggressive, high-risk play.
     *
     * <p>When hiding a card (isHidingCard = true):</p>
     * <ul>
     *   <li>Returns 1 if the first card is a heart or joker (hide it)</li>
     *   <li>Returns 2 otherwise (hide the second card)</li>
     * </ul>
     *
     * <p>When picking a card (isHidingCard = false):</p>
     * <ul>
     *   <li>Searches for visible hearts or jokers and returns their position (1-indexed)</li>
     *   <li>Returns 0 if no suitable card is found</li>
     * </ul>
     *
     * @param min the minimum value in the valid range (not used in this strategy)
     * @param max the maximum value in the valid range (not used in this strategy)
     * @param cards the list of cards to choose from
     * @param isHidingCard true if the AI is hiding a card, false if picking a card
     * @return the 1-indexed position of the chosen card, or 0 if no suitable choice is found
     */
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        // if the AI has to hide a card he will try to hide Joker or Hearth
        if(isHidingCard){
            if(cards.getFirst() instanceof SuitCard && (((SuitCard) cards.getFirst()).getSign() == Sign.HEARTH) || cards.getFirst() instanceof JokerCard){
                return 1;
            }else {
                return 2;
            }
        }else {
            // if AI has to pick a card he will try to pick Joker or Hearth
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i) instanceof SuitCard && ((SuitCard) cards.get(i)).isVisible() && (((SuitCard) cards.get(i)).getSign() == Sign.HEARTH || cards.get(i) instanceof JokerCard)){
                    return i+1;
                }
            }
        }

        return 0;
    }
}
