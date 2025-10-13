import java.util.ArrayList;
import java.util.Random;

public class AI extends Player {
    private IPlayStrategy strategy;

    public AI(String name) {
        super(name);
        this.strategy = new RandomStrategy();
    }

    private Card[] getVisibleCards()
    {
        ArrayList<Card> visibleCards = new ArrayList<>();
        for (Card card : this.getOffer()) {
            if (card.isVisible()) {
                visibleCards.add(card);
            }
        }
        return visibleCards.toArray(new Card[0]);
    }

    private int countCardsWithSign(Sign sign)
    {
        int count = 0;
        for (Card card : this.getOffer()) {
            if (card instanceof SuitCard && ((SuitCard) card).getSign() == sign) {
                count++;
            }
        }
        return count;
    }

    private boolean hasVisibleJoker()
    {
        for (Card card : this.getOffer()) {
            if (card instanceof JokerCard && card.isVisible()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        if(hasVisibleJoker() && countCardsWithSign(Sign.HEARTH) >= 2 || countCardsWithSign(Sign.HEARTH) >= 3){
            this.strategy = new RiskyStrategy();
        }
        else if(!hasVisibleJoker()){
            this.strategy = new RandomStrategy();
        }else {
            this.strategy = new SafeStrategy();
        }
        return this.strategy.makeChoice(min, max, cards, isHidingCard);
    }

    public void setRandomStrategy(IPlayStrategy strategy) {
        Random randomNumbers = new Random();
        int stategyId = randomNumbers.nextInt(0, 3);
        switch (stategyId) {
            case 0 -> this.strategy = new RandomStrategy();
            case 1 -> this.strategy = new SafeStrategy();
            case 2 -> this.strategy = new RiskyStrategy();
        }
    }
}
