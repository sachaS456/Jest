public class JokerCard extends Card {

    public JokerCard(boolean isVisible, CardEffect cardEffect) {
        super(isVisible, cardEffect);
    }

    public JokerCard(boolean isVisible, CardEffect cardEffect, Sign signEffect) {
        super(isVisible, cardEffect,  signEffect);
    }

    public JokerCard(boolean isVisible, CardEffect cardEffect, int valueEffect) {
        super(isVisible, cardEffect, valueEffect);
    }

    @Override
    public String toString() {
        return "Joker \n";
    }
}
