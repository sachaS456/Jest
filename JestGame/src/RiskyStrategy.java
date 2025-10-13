import java.util.ArrayList;

public class RiskyStrategy implements IPlayStrategy {

    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        // if the AI has to hide a card he will try to hide Joker or Hearth
        if(isHidingCard){
            if(cards.getFirst() instanceof SuitCard && (((SuitCard) cards.getFirst()).getSign() == Sign.HEARTH) || cards.getFirst() instanceof JokerCard){
                return 0;
            }else {
                return 1;
            }
        }else {
            // if AI has to pick a card he will try to pick Joker or Hearth
            for(int i = 0; i < cards.size(); i++){
                if(cards.get(i) instanceof SuitCard && ((SuitCard) cards.get(i)).isVisible() && (((SuitCard) cards.get(i)).getSign() == Sign.HEARTH || cards.get(i) instanceof JokerCard)){
                    return i;
                }
            }
        }

        return 0;
    }
}
