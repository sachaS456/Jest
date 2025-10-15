import java.util.ArrayList;
import java.util.Random;

public class RandomStrategy implements IPlayStrategy {

    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        Random randomNumbers = new Random();
        int cardToPick = randomNumbers.nextInt(min, max+1);
        System.out.println(cardToPick);
        return cardToPick;
    }
}
