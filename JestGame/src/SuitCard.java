public class SuitCard extends Card{
    private int value;
    private Color color;
    private Sign sign;

    public SuitCard(boolean isVisible, CardEffect cardEffect, int value, Color color, Sign sign) {
        super(isVisible, cardEffect);
        this.value = value;
        this.color = color;
        this.sign = sign;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public Sign getSign() {
        return sign;
    }
}
