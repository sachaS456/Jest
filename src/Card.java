import java.util.ArrayList;
import java.io.Serializable;

/**
 * Represents an abstract card in the Jest card game.
 * This class serves as the base for all card types (SuitCard, JokerCard) and provides
 * common functionality including visibility management, card effects, and visitor pattern support.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Serializable for game state persistence</li>
 *   <li>Supports various card effects for trophy determination</li>
 *   <li>Can be visible or hidden from players</li>
 *   <li>Implements the Visitor pattern for score calculation</li>
 * </ul>
 *
 * <p>Card effects can be configured with optional parameters:</p>
 * <ul>
 *   <li>No parameter: JOKER, BEST_JEST, BEST_JEST_WITHOUT_JOKER, etc.</li>
 *   <li>Sign parameter: HIGHEST, LOWEST (specifies target suit)</li>
 *   <li>Value parameter: MAJORITY (specifies target value)</li>
 * </ul>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see SuitCard
 * @see JokerCard
 * @see CardEffect
 * @see CardVisitor
 */
public abstract class Card implements Serializable {
    /** Serial version UID for serialization compatibility */
    private static final long serialVersionUID = 1L;

    /** Indicates whether this card is visible to all players */
    private boolean isVisible;
    /** The special effect associated with this card (immutable) */
    private final CardEffect cardEffect;
    /** The value parameter for card effects that require it (e.g., MAJORITY) */
    private int valueEffect;
    /** The sign parameter for card effects that require it (e.g., HIGHEST, LOWEST) */
    private Sign signEffect;

    /**
     * Constructs a Card with a basic card effect (no parameters).
     *
     * <p>Valid effects for this constructor:</p>
     * <ul>
     *   <li>JOKER</li>
     *   <li>BEST_JEST</li>
     *   <li>BEST_JEST_WITHOUT_JOKER</li>
     *   <li>MOST_CARDS (expansion)</li>
     *   <li>LEAST_CARDS (expansion)</li>
     *   <li>EVEN_VALUES (expansion)</li>
     *   <li>ODD_VALUES (expansion)</li>
     *   <li>NO_DUPLICATES (expansion)</li>
     * </ul>
     *
     * @param isVisible true if the card should be visible to all players, false if hidden
     * @param cardEffect the card effect (must not require parameters)
     * @throws IllegalArgumentException if the card effect requires a parameter
     *         (HIGHEST, LOWEST, or MAJORITY)
     */
    public Card(boolean isVisible, CardEffect cardEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.HIGHEST || cardEffect == CardEffect.LOWEST ||  cardEffect == CardEffect.MAJORITY) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter");
        }
        this.cardEffect = cardEffect;
    }

    /**
     * Constructs a Card with a sign-based card effect.
     *
     * <p>This constructor is used for effects that target specific suits:</p>
     * <ul>
     *   <li>HIGHEST - Awards trophy to player with highest value of the specified suit</li>
     *   <li>LOWEST - Awards trophy to player with lowest value of the specified suit</li>
     * </ul>
     *
     * @param isVisible true if the card should be visible to all players, false if hidden
     * @param cardEffect the card effect (must be HIGHEST or LOWEST)
     * @param signEffect the target suit sign for the effect
     * @throws IllegalArgumentException if the card effect is MAJORITY (which requires an int value)
     */
    public Card(boolean isVisible, CardEffect cardEffect, Sign signEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.MAJORITY) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter of type 'Sign'");
        }
        this.cardEffect = cardEffect;
        this.signEffect = signEffect;
    }

    /**
     * Constructs a Card with a value-based card effect.
     *
     * <p>This constructor is used for the MAJORITY effect:</p>
     * <ul>
     *   <li>MAJORITY - Awards trophy to player with most cards of the specified value (1-7)</li>
     * </ul>
     *
     * @param isVisible true if the card should be visible to all players, false if hidden
     * @param cardEffect the card effect (typically MAJORITY)
     * @param valueEffect the target card value for the effect (between 1 and 7)
     * @throws IllegalArgumentException if the card effect is HIGHEST or LOWEST
     *         (which require a Sign parameter)
     */
    public Card(boolean isVisible, CardEffect cardEffect, int valueEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.HIGHEST || cardEffect == CardEffect.LOWEST) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter of type 'int' value (between 1 and 7)");
        }
        this.cardEffect = cardEffect;
        this.valueEffect = valueEffect;
    }

    /**
     * Checks whether this card is visible to all players.
     *
     * @return true if the card is visible, false if hidden
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets the visibility of this card.
     * Hidden cards are not shown to other players but can still be picked.
     *
     * @param isVisible true to make the card visible, false to hide it
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Returns a formatted string representation of the card effect with its parameters.
     * The format varies based on the effect type:
     * <ul>
     *   <li>Sign-based effects: "EFFECT_NAME SIGN" (e.g., "HIGHEST SPADE")</li>
     *   <li>Value-based effects: "EFFECT_NAME VALUE" (e.g., "MAJORITY 3")</li>
     *   <li>No-parameter effects: "EFFECT_NAME" (e.g., "BEST_JEST")</li>
     * </ul>
     *
     * @return a string describing the card effect and its parameters
     */
    public  String getCardEffectCode() {
        if (signEffect != null) {
            return String.format(cardEffect.toString()) + " " + signEffect.toString();
        } else if (valueEffect != 0) {
            return String.format(cardEffect.toString()) + " " + valueEffect;
        }

        return String.format(cardEffect.toString());
    }

    /**
     * Evaluates this card's effect to determine which player wins it as a trophy.
     * This method is called at the end of the game to award trophy cards based on
     * the card's specific effect and the players' jest piles.
     *
     * @param players the list of all players to evaluate for the trophy
     * @return the Player who wins this trophy card, or null if no player qualifies
     * @see CardEffect#CheckEffect(int, Sign, ArrayList)
     */
    public Player checkEffect(ArrayList<Player> players) {
        return this.cardEffect.CheckEffect(this.valueEffect, this.signEffect, players);
    }

    /**
     * Accepts a CardVisitor to perform operations on this card.
     * This method is part of the Visitor pattern implementation and must be
     * implemented by concrete card classes (SuitCard, JokerCard).
     *
     * <p>Typical usage:</p>
     * <pre>
     * CardVisitor visitor = new JestScoreVisitor();
     * card.accept(visitor);
     * </pre>
     *
     * @param visitor the CardVisitor that will visit this card
     * @see CardVisitor
     */
    public abstract void accept(CardVisitor visitor);
}
