/**
 * Represents a Joker card in the Jest card game.
 * This special card type extends Card and typically carries high scoring value
 * or unique effects in the game.
 *
 * <p>Joker cards are distinct from suit cards as they don't have a color, sign, or numeric value.
 * They can be configured with various effects depending on the game rules.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Card
 * @see SuitCard
 */
public class JokerCard extends Card {

    /**
     * Constructs a JokerCard with basic properties.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this joker card
     */
    public JokerCard(boolean isVisible, CardEffect cardEffect) {
        super(isVisible, cardEffect);
    }

    /**
     * Constructs a JokerCard with a sign-based effect.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this joker card
     * @param signEffect the sign parameter for the card effect
     */
    public JokerCard(boolean isVisible, CardEffect cardEffect, Sign signEffect) {
        super(isVisible, cardEffect,  signEffect);
    }

    /**
     * Constructs a JokerCard with a value-based effect.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this joker card
     * @param valueEffect the value parameter for the card effect
     */
    public JokerCard(boolean isVisible, CardEffect cardEffect, int valueEffect) {
        super(isVisible, cardEffect, valueEffect);
    }

    /**
     * Returns a string representation of the JokerCard.
     *
     * @return the string "Joker \n"
     */
    @Override
    public String toString() {
        return "Joker \n";
    }

    /**
     * Accepts a CardVisitor to perform operations on this card.
     * This method is part of the Visitor pattern implementation.
     *
     * @param visitor the CardVisitor that will visit this joker card
     */
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }
}
