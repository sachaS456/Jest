import java.util.ArrayList;

public class SafeStrategy implements IPlayStrategy {
    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        // if the AI has to hide a card he will try to hide Spades or Clubs
        if(isHidingCard){
            if(cards.getFirst() instanceof SuitCard && (((SuitCard) cards.getFirst()).getSign() == Sign.SPIKE || ((SuitCard) cards.getFirst()).getSign() == Sign.CLOVER)){
                return 0;
            }else {
                return 1;
            }
        }else {
            // if AI has to pick a card he will try to pick Spades or Clubs
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i) instanceof SuitCard && ((SuitCard) cards.get(i)).isVisible() && (((SuitCard) cards.get(i)).getSign() == Sign.SPIKE || ((SuitCard) cards.get(i)).getSign() == Sign.CLOVER)){
                    return i;
                }
            }
        }

        return 0;
    }
}
