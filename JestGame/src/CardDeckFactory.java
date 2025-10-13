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

    public static ArrayList<Card> createCustomDeck() {
        return new ArrayList<>();
    }

    public static int getStandardDeckSize() {
        return 17;
    }
}
