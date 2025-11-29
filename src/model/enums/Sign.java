package model.enums;

/**
 * Represents the four suits/signs of playing cards.
 * This enumeration defines the traditional card suits used in the Jest game.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public enum Sign {
    /** The spade suit */
    SPADE("♠"),
    /** The club suit */
    CLUB("♣"),
    /** The diamond suit */
    DIAMOND("♦"),
    /** The heart suit */
    HEARTH("♥");

    private final String symbol;

    /**
     * Constructs a Sign with its visual symbol.
     *
     * @param symbol the visual representation of the suit
     */
    Sign(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the visual symbol for this suit.
     *
     * @return the suit symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the name of the sign.
     *
     * @return the name (SPADE, CLUB, DIAMOND, HEARTH)
     */
    @Override
    public String toString() {
        return symbol;
    }
}
