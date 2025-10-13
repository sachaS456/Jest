import java.util.ArrayList;

public abstract class Card {
    private boolean isVisible;
    private final CardEffect cardEffect;
    private int valueEffect;
    private Sign signEffect;

    public Card(boolean isVisible, CardEffect cardEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.HIGHEST || cardEffect == CardEffect.LOWEST ||  cardEffect == CardEffect.MAJORITY) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter");
        }
        this.cardEffect = cardEffect;
    }

    public Card(boolean isVisible, CardEffect cardEffect, Sign signEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.MAJORITY) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter of type 'Sign'");
        }
        this.cardEffect = cardEffect;
        this.signEffect = signEffect;
    }

    public Card(boolean isVisible, CardEffect cardEffect, int valueEffect) {
        this.isVisible = isVisible;
        if (cardEffect == CardEffect.HIGHEST || cardEffect == CardEffect.LOWEST) {
            throw new IllegalArgumentException("Invalid Card Effect, you must give a parameter of type 'int' value (between 1 and 7)");
        }
        this.cardEffect = cardEffect;
        this.valueEffect = valueEffect;
    }

    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public  String getCardEffectCode() {
        if (signEffect != null) {
            return String.format(cardEffect.toString()) + " " + signEffect.toString();
        } else if (valueEffect != 0) {
            return String.format(cardEffect.toString()) + " " + valueEffect;
        }

        return String.format(cardEffect.toString());
    }

    public Player checkEffect(ArrayList<Player> players) {
        return this.cardEffect.CheckEffect(this.valueEffect, this.signEffect, players);
    }

    public abstract void accept(CardVisitor visitor);
}
