package model.cards;

import model.enums.Color;
import model.enums.Sign;
import visitor.CardVisitor;

/**
 * Represents a standard playing card with a suit (color and sign) and a numeric value.
 * This class extends Card and adds specific properties for suited cards.
 *
 * @author Jest Game & Gatien Genevois & Sacha Himber
 * @version 1.0
 */
public class SuitCard extends Card{
    /** The numeric value of the card */
    private int value;
    /** The color of the card */
    private Color color;
    /** The sign/suit of the card */
    private Sign sign;

    /**
     * Constructs a SuitCard with basic properties.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this card
     * @param value the numeric value of the card
     * @param color the color of the card
     * @param sign the sign/suit of the card
     */
    public SuitCard(boolean isVisible, CardEffect cardEffect, int value, Color color, Sign sign) {
        super(isVisible, cardEffect);
        this.value = value;
        this.color = color;
        this.sign = sign;
    }

    /**
     * Constructs a SuitCard with a value-based effect.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this card
     * @param valueEffect the value parameter for the card effect
     * @param value the numeric value of the card
     * @param color the color of the card
     * @param sign the sign/suit of the card
     */
    public SuitCard(boolean isVisible, CardEffect cardEffect, int valueEffect, int value, Color color, Sign sign) {
        super(isVisible, cardEffect,  valueEffect);
        this.value = value;
        this.color = color;
        this.sign = sign;
    }

    /**
     * Constructs a SuitCard with a sign-based effect.
     *
     * @param isVisible indicates whether the card is visible to players
     * @param cardEffect the special effect associated with this card
     * @param signEffect the sign parameter for the card effect
     * @param value the numeric value of the card
     * @param color the color of the card
     * @param sign the sign/suit of the card
     */
    public SuitCard(boolean isVisible, CardEffect cardEffect, Sign signEffect, int value, Color color, Sign sign) {
        super(isVisible, cardEffect, signEffect);
        this.value = value;
        this.color = color;
        this.sign = sign;
    }

    /**
     * Returns a string representation of the SuitCard.
     * The format is: "value color sign\n"
     *
     * @return a formatted string containing the card's value, color, and sign
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(this.value + " ");
        text.append(this.color.toString() + " ");
        text.append(this.sign.toString() + " ");
        text.append("\n");
        return text.toString();
    }

    /**
     * Gets the numeric value of the card.
     *
     * @return the card's value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the color of the card.
     *
     * @return the card's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the sign/suit of the card.
     *
     * @return the card's sign
     */
    public Sign getSign() {
        return sign;
    }

    /**
     * Accepts a CardVisitor to perform operations on this card.
     * This method is part of the Visitor pattern implementation.
     *
     * @param visitor the CardVisitor that will visit this card
     */
    @Override
    public void accept(CardVisitor visitor) {
        visitor.visit(this);
    }
}
