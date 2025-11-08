package player;

import java.util.ArrayList;
import java.util.Random;
import model.cards.Card;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.enums.Sign;
import strategy.IPlayStrategy;
import strategy.SafeStrategy;
import strategy.RiskyStrategy;
import strategy.RandomStrategy;

/**
 * Represents an AI (Artificial Intelligence) player in the Jest card game.
 * This class extends Player and implements automated decision-making through
 * pluggable play strategies that adapt based on the game state.
 *
 * <p>AI Strategy Selection:</p>
 * <ul>
 *   <li><b>RiskyStrategy:</b> Selected when AI has a visible Joker with 2+ Hearts, or 3+ Hearts total</li>
 *   <li><b>RandomStrategy:</b> Selected when AI has no visible Joker</li>
 *   <li><b>SafeStrategy:</b> Selected in other situations (has visible Joker but not enough Hearts)</li>
 * </ul>
 *
 * <p>The AI dynamically adjusts its strategy on each turn based on its current cards,
 * allowing for adaptive gameplay that responds to the game situation.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Player
 * @see IPlayStrategy
 * @see SafeStrategy
 * @see RiskyStrategy
 * @see RandomStrategy
 */
public class AI extends Player {
    /** The current play strategy being used by this AI player */
    private IPlayStrategy strategy;

    /**
     * Constructs a new AI player with the specified name.
     * The AI is initialized with a RandomStrategy by default.
     *
     * @param name the name of the AI player
     */
    public AI(String name) {
        super(name);
        this.strategy = new RandomStrategy();
    }

    /**
     * Gets all visible cards from the AI's current offer.
     * This method filters the offer array to return only cards that are visible.
     *
     * @return an array containing only the visible cards from the offer
     */
    private Card[] getVisibleCards()
    {
        ArrayList<Card> visibleCards = new ArrayList<>();
        for (Card card : this.getOffer()) {
            if (card.isVisible()) {
                visibleCards.add(card);
            }
        }
        return visibleCards.toArray(new Card[0]);
    }

    /**
     * Counts how many cards of a specific suit sign are in the AI's offer.
     * Only SuitCards are considered; Joker cards are ignored.
     *
     * @param sign the suit sign to count (SPADE, CLUB, DIAMOND, or HEARTH)
     * @return the number of cards with the specified sign in the offer
     */
    private int countCardsWithSign(Sign sign)
    {
        int count = 0;
        for (Card card : this.getOffer()) {
            if (card instanceof SuitCard && ((SuitCard) card).getSign() == sign) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if the AI has a visible Joker card in its offer.
     *
     * @return true if at least one visible Joker is present, false otherwise
     */
    private boolean hasVisibleJoker()
    {
        for (Card card : this.getOffer()) {
            if (card instanceof JokerCard && card.isVisible()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a strategic choice for the AI player based on adaptive strategy selection.
     * The AI analyzes its current cards and selects an appropriate strategy before making the choice.
     *
     * <p>Strategy selection logic:</p>
     * <ol>
     *   <li>If (visible Joker AND 2+ Hearts) OR 3+ Hearts total → Use RiskyStrategy</li>
     *   <li>Else if no visible Joker → Use RandomStrategy</li>
     *   <li>Else → Use SafeStrategy</li>
     * </ol>
     *
     * <p>This dynamic approach allows the AI to:</p>
     * <ul>
     *   <li>Play aggressively when holding valuable Heart combinations</li>
     *   <li>Play unpredictably when lacking strategic cards</li>
     *   <li>Play conservatively in neutral situations</li>
     * </ul>
     *
     * @param min the minimum valid choice value (inclusive)
     * @param max the maximum valid choice value (inclusive)
     * @param cards the list of cards available for the decision
     * @param isHidingCard true if choosing which card to hide, false if choosing which card to pick
     * @return the AI's strategic choice delegated to the selected strategy
     */
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        if(hasVisibleJoker() && countCardsWithSign(Sign.HEARTH) >= 2 || countCardsWithSign(Sign.HEARTH) >= 3){
            this.strategy = new RiskyStrategy();
        }
        else if(!hasVisibleJoker()){
            this.strategy = new RandomStrategy();
        }else {
            this.strategy = new SafeStrategy();
        }
        return this.strategy.makeChoice(min, max, cards, isHidingCard);
    }

    /**
     * Sets a random strategy for the AI player.
     * This method randomly selects one of the three available strategies:
     * RandomStrategy, SafeStrategy, or RiskyStrategy.
     *
     * <p>Note: The strategy parameter is currently not used; the method
     * generates a random selection regardless of the input.</p>
     *
     * @param strategy the strategy parameter (currently unused)
     * @deprecated This method's parameter is not used. Consider using a parameterless version
     *             or implementing the parameter functionality.
     */
    public void setRandomStrategy(IPlayStrategy strategy) {
        Random randomNumbers = new Random();
        int stategyId = randomNumbers.nextInt(0, 3);
        switch (stategyId) {
            case 0 -> this.strategy = new RandomStrategy();
            case 1 -> this.strategy = new SafeStrategy();
            case 2 -> this.strategy = new RiskyStrategy();
        }
    }
}
