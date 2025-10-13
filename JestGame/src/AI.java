import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class AI extends Player {
    private final IPlayStrategy strategy;

    public AI(String name) {
        super(name);
        this.strategy = new RandomStrategy();
    }


    @Override
    public int makeChoice(int min, int max, ArrayList<Card> cards, boolean isHidingCard) {
        return this.strategy.makeChoice(min, max, cards, isHidingCard);
    }
}
