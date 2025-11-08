package model.cards;

import java.util.ArrayList;
import model.enums.Color;
import model.enums.Sign;

/**
 * Factory class for creating card decks used in the Jest card game.
 * This class provides static methods to generate different deck configurations,
 * including standard decks, expansion decks, and combined full decks.
 *
 * <p>Available deck configurations:</p>
 * <ul>
 *   <li><b>Standard Deck:</b> 17 cards with basic game effects</li>
 *   <li><b>Expansion Deck:</b> 9 additional cards with advanced effects</li>
 *   <li><b>Full Deck:</b> 26 cards combining standard and expansion</li>
 *   <li><b>Custom Deck:</b> Empty deck for custom configurations</li>
 * </ul>
 *
 * <p>The factory pattern is used to centralize card creation and ensure
 * consistent deck configurations across the game.</p>
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 * @see Card
 * @see SuitCard
 * @see JokerCard
 * @see CardEffect
 */
public class CardDeckFactory {

    /**
     * Creates the standard Jest deck with 17 cards.
     *
     * <p>Standard deck composition:</p>
     * <ul>
     *   <li>4 Club cards (values 1-4) with effects: HIGHEST/SPADE, LOWEST/HEARTH, HIGHEST/HEARTH, LOWEST/SPADE</li>
     *   <li>4 Spade cards (values 1-4) with effects: HIGHEST/SPADE, MAJORITY/3, MAJORITY/2, LOWEST/CLUB</li>
     *   <li>4 Heart cards (values 1-4) with JOKER effects</li>
     *   <li>4 Diamond cards (values 1-4) with effects: MAJORITY/4, HIGHEST/DIAMOND, LOWEST/DIAMOND, BEST_JEST_WITHOUT_JOKER</li>
     *   <li>1 Joker card with BEST_JEST effect</li>
     * </ul>
     *
     * @return an ArrayList containing 17 cards representing the standard deck
     */
    public static ArrayList<Card> createStandardDeck() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPADE, 1, Color.BLACK, Sign.CLUB));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.HEARTH, 2, Color.BLACK, Sign.CLUB));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.HEARTH, 3, Color.BLACK, Sign.CLUB));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.SPADE, 4, Color.BLACK, Sign.CLUB));

        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPADE, 1, Color.BLACK, Sign.SPADE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 3, 2, Color.BLACK, Sign.SPADE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 2, 3, Color.BLACK, Sign.SPADE));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.CLUB, 4, Color.BLACK, Sign.SPADE));

        cards.add(new SuitCard(true, CardEffect.JOKER, 1, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 2, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 3, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 4, Color.RED, Sign.HEARTH));

        cards.add(new SuitCard(true, CardEffect.MAJORITY, 4, 1, Color.RED, Sign.DIAMOND));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.DIAMOND, 2, Color.RED, Sign.DIAMOND));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.DIAMOND, 3, Color.RED, Sign.DIAMOND));
        cards.add(new SuitCard(true, CardEffect.BEST_JEST_WITHOUT_JOKER, 4, Color.RED, Sign.DIAMOND));

        cards.add(new JokerCard(true, CardEffect.BEST_JEST));

        return cards;
    }

    /**
     * Creates the expansion deck with 9 additional cards.
     *
     * <p>Expansion deck composition:</p>
     * <ul>
     *   <li>2 Club cards (values 1-2) with effects: MOST_CARDS, LEAST_CARDS</li>
     *   <li>2 Heart cards (values 3-4) with effects: EVEN_VALUES, ODD_VALUES</li>
     *   <li>1 Spade card (value 1) with NO_DUPLICATES effect</li>
     *   <li>1 Joker card with MOST_CARDS effect</li>
     *   <li>3 additional suit cards (values 5-7) with various effects</li>
     * </ul>
     *
     * <p>The expansion introduces new card effects and higher card values
     * to add complexity and variety to the game.</p>
     *
     * @return an ArrayList containing 9 cards representing the expansion deck
     */
    public static ArrayList<Card> createExpansionDeck() {
        ArrayList<Card> expansionCards = new ArrayList<>();

        expansionCards.add(new SuitCard(true, CardEffect.MOST_CARDS, 1, Color.BLACK, Sign.CLUB));
        expansionCards.add(new SuitCard(true, CardEffect.LEAST_CARDS, 2, Color.BLACK, Sign.CLUB));
        expansionCards.add(new SuitCard(true, CardEffect.EVEN_VALUES, 3, Color.RED, Sign.HEARTH));
        expansionCards.add(new SuitCard(true, CardEffect.ODD_VALUES, 4, Color.RED, Sign.HEARTH));
        expansionCards.add(new SuitCard(true, CardEffect.NO_DUPLICATES, 1, Color.BLACK, Sign.SPADE));

        expansionCards.add(new JokerCard(true, CardEffect.MOST_CARDS));

        expansionCards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPADE, 5, Color.BLACK, Sign.SPADE));
        expansionCards.add(new SuitCard(true, CardEffect.LOWEST, Sign.DIAMOND, 6, Color.RED, Sign.DIAMOND));
        expansionCards.add(new SuitCard(true, CardEffect.MAJORITY, 5, 7, Color.RED, Sign.HEARTH));

        return expansionCards;
    }

    /**
     * Creates a full deck combining both standard and expansion cards.
     * This method creates a new standard deck and adds all expansion cards to it.
     *
     * @return an ArrayList containing 26 cards (17 standard + 9 expansion)
     * @see #createStandardDeck()
     * @see #createExpansionDeck()
     */
    public static ArrayList<Card> createFullDeck() {
        ArrayList<Card> fullDeck = createStandardDeck();
        fullDeck.addAll(createExpansionDeck());
        return fullDeck;
    }

    /**
     * Creates an empty custom deck.
     * This method can be used as a starting point for building custom deck configurations.
     *
     * @return an empty ArrayList that can be populated with custom cards
     */
    public static ArrayList<Card> createCustomDeck() {
        return new ArrayList<>();
    }

    /**
     * Gets the number of cards in the standard deck.
     *
     * @return 17, the size of the standard deck
     */
    public static int getStandardDeckSize() {
        return 17;
    }

    /**
     * Gets the number of cards in the expansion deck.
     *
     * @return 9, the size of the expansion deck
     */
    public static int getExpansionDeckSize() {
        return 9;
    }

    /**
     * Gets the total number of cards when combining standard and expansion decks.
     *
     * @return 26, the combined size of standard and expansion decks
     */
    public static int getFullDeckSize() {
        return getStandardDeckSize() + getExpansionDeckSize();
    }
}
