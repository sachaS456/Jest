import java.util.ArrayList;

public class CardDeckFactory {

    public static ArrayList<Card> createStandardDeck() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPIKE, 1, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.HEARTH, 2, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.HEARTH, 3, Color.BLACK, Sign.CLOVER));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.SPIKE, 4, Color.BLACK, Sign.CLOVER));

        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPIKE, 1, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 3, 2, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.MAJORITY, 2, 3, Color.BLACK, Sign.SPIKE));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.CLOVER, 4, Color.BLACK, Sign.SPIKE));

        cards.add(new SuitCard(true, CardEffect.JOKER, 1, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 2, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 3, Color.RED, Sign.HEARTH));
        cards.add(new SuitCard(true, CardEffect.JOKER, 4, Color.RED, Sign.HEARTH));

        cards.add(new SuitCard(true, CardEffect.MAJORITY, 4, 1, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.TILE, 2, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.LOWEST, Sign.TILE, 3, Color.RED, Sign.TILE));
        cards.add(new SuitCard(true, CardEffect.BEST_JEST_WITHOUT_JOKER, 4, Color.RED, Sign.TILE));

        cards.add(new JokerCard(true, CardEffect.BEST_JEST));

        return cards;
    }

    public static ArrayList<Card> createExpansionDeck() {
        ArrayList<Card> expansionCards = new ArrayList<>();

        expansionCards.add(new SuitCard(true, CardEffect.MOST_CARDS, 1, Color.BLACK, Sign.CLOVER));
        expansionCards.add(new SuitCard(true, CardEffect.LEAST_CARDS, 2, Color.BLACK, Sign.CLOVER));
        expansionCards.add(new SuitCard(true, CardEffect.EVEN_VALUES, 3, Color.RED, Sign.HEARTH));
        expansionCards.add(new SuitCard(true, CardEffect.ODD_VALUES, 4, Color.RED, Sign.HEARTH));
        expansionCards.add(new SuitCard(true, CardEffect.NO_DUPLICATES, 1, Color.BLACK, Sign.SPIKE));

        expansionCards.add(new JokerCard(true, CardEffect.MOST_CARDS));

        expansionCards.add(new SuitCard(true, CardEffect.HIGHEST, Sign.SPIKE, 5, Color.BLACK, Sign.SPIKE));
        expansionCards.add(new SuitCard(true, CardEffect.LOWEST, Sign.TILE, 6, Color.RED, Sign.TILE));
        expansionCards.add(new SuitCard(true, CardEffect.MAJORITY, 5, 7, Color.RED, Sign.HEARTH));

        return expansionCards;
    }

    public static ArrayList<Card> createFullDeck() {
        ArrayList<Card> fullDeck = createStandardDeck();
        fullDeck.addAll(createExpansionDeck());
        return fullDeck;
    }

    public static ArrayList<Card> createCustomDeck() {
        return new ArrayList<>();
    }

    public static int getStandardDeckSize() {
        return 17;
    }
    public static int getExpansionDeckSize() {
        return 9;
    }

    public static int getFullDeckSize() {
        return getStandardDeckSize() + getExpansionDeckSize();
    }
}
