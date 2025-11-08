package strategy;

import java.util.ArrayList;
import model.cards.Card;
import model.cards.SuitCard;
import model.enums.Sign;

/**
 * Implements a conservative play strategy for AI players.
 * This strategy focuses on minimizing risk by preferring spades and clubs,
 * which are typically worth fewer points in the Jest game.
 *
 * <p>Strategy behavior:</p>
 * <ul>
 *   <li>When hiding cards: prioritizes hiding spades or clubs</li>
 *   <li>When picking cards: targets visible spades or clubs</li>
 *   <li>Avoids high-value suits (diamonds and hearts) when possible</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see IPlayStrategy
 */
public class SafeStrategy implements IPlayStrategy {
    /**
     * Makes a strategic choice for the AI player based on safe, conservative play.
     *
     * <p>When hiding a card (isHidingCard = true):</p>
     * <ul>
     *   <li>Returns 1 if the first card is a spade or club (hide it)</li>
     *   <li>Returns 2 otherwise (hide the second card)</li>
     * </ul>
     *
     * <p>When picking a card (isHidingCard = false):</p>
     * <ul>
     *   <li>Searches for visible spades or clubs and returns their position (1-indexed)</li>
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
        // if the AI has to hide a card he will try to hide SPADES or CLUBS
        if(isHidingCard){
            if(cards.getFirst() instanceof SuitCard && (((SuitCard) cards.getFirst()).getSign() == Sign.SPADE || ((SuitCard) cards.getFirst()).getSign() == Sign.CLUB)){
                return 1;
            }else {
                return 2;
            }
        }else {
            // if AI has to pick a card he will try to pick SPADES or CLUBS
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i) instanceof SuitCard && ((SuitCard) cards.get(i)).isVisible() && (((SuitCard) cards.get(i)).getSign() == Sign.SPADE || ((SuitCard) cards.get(i)).getSign() == Sign.CLUB)){
                    return i+1;
                }
            }
        }

        return 0;
    }
}
