/**
 * Defines the contract for the Visitor pattern implementation used to traverse and process cards.
 * This interface enables operations to be performed on different card types (SuitCard, JokerCard)
 * without modifying the card classes themselves.
 *
 * <p>The Visitor pattern is particularly useful for:</p>
 * <ul>
 *   <li>Score calculation across different card types</li>
 *   <li>Card analysis and statistics gathering</li>
 *   <li>Applying operations to card collections without type-checking</li>
 *   <li>Maintaining separation of concerns between card data and operations</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 * CardVisitor visitor = new JestScoreVisitor();
 * for (Card card : player.getJest()) {
 *     card.accept(visitor);
 * }
 * int totalScore = visitor.getScore();
 * </pre>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Card
 * @see SuitCard
 * @see JokerCard
 * @see JestScoreVisitor
 */
public interface CardVisitor {
    /**
     * Visits a SuitCard and performs operations specific to suit cards.
     * This method is called when a SuitCard accepts this visitor.
     * Implementations should handle suit card properties such as value, color, and sign.
     *
     * @param card the SuitCard being visited
     */
    void visit(SuitCard card);

    /**
     * Visits a JokerCard and performs operations specific to joker cards.
     * This method is called when a JokerCard accepts this visitor.
     * Implementations should handle joker-specific logic and effects.
     *
     * @param card the JokerCard being visited
     */
    void visit(JokerCard card);

    /**
     * Returns the accumulated score from visiting cards.
     * This method should be called after all cards have been visited
     * to retrieve the final calculated score.
     *
     * <p>The meaning of the score depends on the visitor implementation.
     * For example, {@link JestScoreVisitor} returns the total Jest points
     * based on the game's scoring rules.</p>
     *
     * @return the calculated score based on visited cards
     */
    int getScore();
}
