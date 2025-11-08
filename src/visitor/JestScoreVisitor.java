package visitor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import model.cards.SuitCard;
import model.cards.JokerCard;
import model.enums.Sign;

/**
 * Implements the CardVisitor interface to calculate scores for a player's jest pile
 * according to the Jest game scoring rules.
 *
 * <p>Scoring rules:</p>
 * <ul>
 *   <li>Spades and Clubs: add their value to the score</li>
 *   <li>Diamonds: subtract their value from the score</li>
 *   <li>Hearts: scoring depends on Joker presence and heart count</li>
 *   <li>Single card bonus: +4 points for having exactly one card of any suit</li>
 *   <li>Matching pairs: +2 points for each matching Spade/Club value pair</li>
 *   <li>Joker effects: modifies heart scoring and adds bonus if no hearts are present</li>
 * </ul>
 *
 * <p>This class uses the Visitor pattern to traverse a player's card collection
 * and accumulate the total score based on the game's complex scoring logic.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see CardVisitor
 * @see Card
 */
public class JestScoreVisitor implements CardVisitor {
    /** The accumulated score for the jest pile */
    private int score = 0;
    /** Flag indicating whether a Joker card is present in the jest pile */
    private boolean hasJoker = false;
    /** Map organizing cards by their sign, storing their numeric values */
    private Map<Sign, List<Integer>> cardsBySign;

    /**
     * Constructs a new JestScoreVisitor.
     * Initializes the card map with empty lists for each suit sign.
     */
    public JestScoreVisitor() {
        cardsBySign = new EnumMap<>(Sign.class);
        for (Sign sign : Sign.values()) {
            cardsBySign.put(sign, new ArrayList<>());
        }
    }

    /**
     * Visits a SuitCard and adds its value to the appropriate sign category.
     * This method is called for each suit card in the jest pile during scoring.
     *
     * @param card the SuitCard being visited
     */
    @Override
    public void visit(SuitCard card) {
        cardsBySign.get(card.getSign()).add(card.getValue());
    }

    /**
     * Visits a JokerCard and marks its presence.
     * The Joker affects heart scoring rules and provides bonuses.
     *
     * @param card the JokerCard being visited
     */
    @Override
    public void visit(JokerCard card) {
        hasJoker = true;
    }

    /**
     * Calculates and returns the total score for the jest pile based on Jest game rules.
     *
     * <p>Scoring calculation steps:</p>
     * <ol>
     *   <li>Add values of all Spades and Clubs</li>
     *   <li>Subtract values of all Diamonds</li>
     *   <li>Apply heart scoring (affected by Joker presence):
     *     <ul>
     *       <li>With Joker: 1-3 hearts subtract their value, 4 hearts add their value</li>
     *       <li>Without Joker: hearts have no effect on score</li>
     *     </ul>
     *   </li>
     *   <li>Add +4 bonus for Joker if no hearts present</li>
     *   <li>Add +4 for each suit with exactly one card</li>
     *   <li>Add +2 for each Spade value that matches a Club value</li>
     * </ol>
     *
     * @return the calculated total score for the jest pile
     */
    @Override
    public int getScore() {
        int heartCount = cardsBySign.get(Sign.HEARTH).size();
        int spadeCount = cardsBySign.get(Sign.SPADE).size();
        int clubCount = cardsBySign.get(Sign.CLUB).size();
        int diamondCount = cardsBySign.get(Sign.DIAMOND).size();

        // Add Spades and Clubs
        for (Integer value : cardsBySign.get(Sign.SPADE)) {
            score += value;
        }
        for (Integer value : cardsBySign.get(Sign.CLUB)) {
            score += value;
        }
        // Subtract Diamonds
        for (Integer value : cardsBySign.get(Sign.DIAMOND)) {
            score -= value;
        }

        // Joker effects on hearts
        if (hasJoker) {
            if (heartCount >= 1 && heartCount <= 3) {
                for (Integer value : cardsBySign.get(Sign.HEARTH)) {
                    score -= value;
                }
            } else if (heartCount == 4) {
                for (Integer value : cardsBySign.get(Sign.HEARTH)) {
                    score += value;
                }
            }

            if (heartCount == 0) {
                score += 4;
            }
        }

        // Single card bonuses
        if (heartCount == 1) score += 4;
        if (spadeCount == 1) score += 4;
        if (clubCount == 1) score += 4;
        if (diamondCount == 1) score += 4;

        // Matching Spade/Club value pairs
        for (Integer SPADEValue : cardsBySign.get(Sign.SPADE)) {
            if (cardsBySign.get(Sign.CLUB).contains(SPADEValue)) {
                score += 2;
            }
        }

        return score;
    }
}
