public abstract class Card {
    private boolean isVisible;
    private final CardEffect cardEffect;

    public Card(boolean isVisible, CardEffect cardEffect) {
        this.isVisible = isVisible;
        this.cardEffect = cardEffect;
    }

    public boolean isVisible() {
        return isVisible;
    }
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public CardEffect getCardEffect() {
        return cardEffect;
    }
}
